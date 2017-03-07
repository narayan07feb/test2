package com.optum.ogn.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;



import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.optum.ogn.app.ConnectionSettings;
import com.optum.ogn.audit.AuditManager;
import com.optum.ogn.audit.EligibilityAudit;
import com.optum.ogn.domain.ELIGIBILITY_RESPONSE_CODE;
import com.optum.ogn.domain.MyUhcRequestObject;
import com.optum.ogn.domain.MyUhcResponseObject;
import com.optum.ogn.domain.PRE_HANDLE_ROUTINE_STATUS;
import com.optum.ogn.domain.RequestObject;
import com.optum.ogn.domain.ResponseObject;
import com.optum.ogn.domain.SERVICE_RESPONSE_STATUS_CODE;
import com.optum.ogn.domain.WellnessRequestObject;
import com.optum.ogn.domain.WellnessResponseObject;
import com.optum.ogn.eligibilityschema.LawwType;
import com.optum.ogn.eligibilityschema.MemberEligibility;
import com.optum.ogn.eligibilityschema.PortalDestinationType;
import com.optum.ogn.eligibilityschema.SubscriberType;
import com.optum.ogn.exception.EligibilityError;
import com.optum.ogn.exception.SystemException;
import com.optum.ogn.model.WCPConsumerDetails;
import com.optum.ogn.model.WCPFilteringAttributes;
import com.optum.ogn.model.WCPMemberProductEligibilityRequest;
import com.optum.ogn.model.MyuhcPlansInfo;
import com.optum.ogn.model.MyuhcValidRequest;
import com.optum.ogn.model.MyuhcValidResponse;
import com.optum.ogn.model.WCPRequestDetails;
import com.optum.ogn.model.WCPRequestHeader;
import com.optum.ogn.model.WCPValidRequest;
import com.optum.ogn.model.WCPValidResponse;
import com.optum.ogn.service.CryptoService;
import com.optum.ogn.service.LawwEligibilityService;
import com.optum.ogn.service.MyuhcEligibilityService;
import com.optum.ogn.service.ProvisionDataStoreService;
import com.optum.ogn.service.WCPEligibilityService;
import com.optum.ogn.service.WXSService;
import com.optum.ogn.util.AuthenticationHelper;
import com.optum.ogn.util.GracePeriodLogicUtil;
import com.optum.ogn.util.LAWWEligibilityHelper;
import com.optum.ogn.util.MDMHelperV2;
import com.optum.ogn.util.ProvisionDataSourceHelper;

public class CheckWellnessEligibilityRoutineController extends ScopeBasedController
		implements EligibilityRoutineControllerInterface<WellnessRequestObject, WellnessResponseObject> {
    private Logger logger = LoggerFactory
            .getLogger(CheckWellnessEligibilityRoutineController.class.getName());

	@Inject
	private EligibilityController eligibilityController;
	
	@Inject
	private SessionValidationController validationController;
	
	@Inject
	private ProvisionDataStoreService provisionDataStoreService;
	
	@Inject
	private HttpServletRequest httpServletRequest;
	
	@Inject
	private HttpServletResponse response;
	
//	@Inject
//	private LawwEligibilityService lawwEligibilityService;
	
	@Inject
	private CryptoService cryptoService;
	
	@Inject
	private WXSService wxsService;
	
	@Inject
	private WCPEligibilityService wcpEligibilityService;

	@Override
	public PRE_HANDLE_ROUTINE_STATUS preHandleCheckRoutine() {
		if(sessionInfo().isDestinationWellness() && !ConnectionSettings.getWellnessFlag()) {
			return PRE_HANDLE_ROUTINE_STATUS.EXIT_HANDLER_ROUTINE;
		}
		if (sessionInfo().isPartialEligibility() && !sessionInfo().isDestinationWellness()) {
			return PRE_HANDLE_ROUTINE_STATUS.EXIT_HANDLER_ROUTINE;
		} else if (sessionInfo().isPartialEligibility() && sessionInfo().isDestinationWellness()) {
			if(!ConnectionSettings.getWellnessFlag()) {
				return PRE_HANDLE_ROUTINE_STATUS.RE_DIRECT_ERROR_PAGE;
			}
		}
		return PRE_HANDLE_ROUTINE_STATUS.CONTINUE_HANDLER_ROUTINE;
	}

	@Override
	public ModelAndView handleEligibilityRoutine() {
		PRE_HANDLE_ROUTINE_STATUS preHandlerRoutineStatus = preHandleCheckRoutine();
		if(preHandlerRoutineStatus==PRE_HANDLE_ROUTINE_STATUS.EXIT_HANDLER_ROUTINE)
			return exit();
		else if(preHandlerRoutineStatus==PRE_HANDLE_ROUTINE_STATUS.RE_DIRECT_ERROR_PAGE)
			return exit(EligibilityError.MYUHC_ELIGIBILITY_ERROR);
		AuditManager.getInstance().logAuditMessage(EligibilityAudit.CMEI001, sessionInfo().getSessionInfo()); // Audit

		if (sessionInfo().isMYUHCFound()) { // Registration or Cached data
			return this.processRequestObjects();
		} else { // Signin or API
        	if(sessionInfo().isDestinationMYUHC()) {
        		if(sessionInfo().isFromCache()) {
                       return exit();
        		} else {
        			return this.processRequestObjects();
        		}
        	} else {
        		return this.processRequestObjects();
        	}
		}
	}
	   private ModelAndView processRequestObjects() {
	    	if(sessionInfo().getRequestObjectList()!=null 
	    			&& sessionInfo().getRequestObjectList().size()>0) {
	        	List<RequestObject> orderedRequestObjectList = MDMHelperV2.orderByEligibilitySystemAndPriority(sessionInfo().getRequestObjectList(), "WELLNESS");
				Map<String, List<MyuhcPlansInfo>> namePlanMap = new HashMap<String, List<MyuhcPlansInfo>>();
				for(RequestObject orderedRquestObject : orderedRequestObjectList) {
	        		MyUhcRequestObject myUhcRequestObject = (MyUhcRequestObject)orderedRquestObject;

	                try {
	                   	ELIGIBILITY_RESPONSE_CODE responseCode = callMemberByNameDOBSub(orderedRquestObject);

	                    if (sessionInfo().isMYUHCFound()) { // Subscriber id/Policy passed eligibility check
		                    if(responseCode==ELIGIBILITY_RESPONSE_CODE.SIGNIN_ELIGIBILITY_SUCCESSFUL
		                    		|| responseCode==ELIGIBILITY_RESPONSE_CODE.API_ELIGIBILITY_SUCCESSFUL
		                    		|| responseCode==ELIGIBILITY_RESPONSE_CODE.REGISTERATION_ELIGIBILITY_SUCCESSFUL) {
		    	            	if(sessionInfo().isSignInAction() && ProvisionDataSourceHelper.isMemberRecordPresent(sessionInfo().getProvisionEnrolledSites())) {
			            			String healthsafeId = sessionInfo().getProvisionEnrolledSites().getHealthSafeId();
			            			String provisionMemberId = sessionInfo().getProvisionEnrolledSites().getProvisionMemberId();
			            			GracePeriodLogicUtil.dbUpdateDateOfLoss(provisionDataStoreService, 
			            									PortalDestinationType.WCP, 
			            									healthsafeId, provisionMemberId, 
			            									null);
		    	            	}
		                    	return exit();
		                    } 
	                    } 
	                } catch(SystemException systemException) {
//	                    	if(sessionInfo().isDestinationMYUHC()) {
//	            				return exit(EligibilityError.MYUHC_ELIGIBILITY_ERROR);
//	                    	}
	                }
				}
	        	ModelAndView gracePeriodMV = this.handleGracePeriod(PortalDestinationType.WCP);
	        	if(gracePeriodMV!=null)
	        		return gracePeriodMV;
	            if (sessionInfo().isDestinationMYUHC()) { // Display an error screen because eligibility failed.
					return exit(EligibilityError.WELLNESS_ELIGIBILITY_ERROR);
	            }
	    	}
	    	return exit();

	    }
	@Override
	public ELIGIBILITY_RESPONSE_CODE callMemberByNameDOBSub(RequestObject requestObject) throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ELIGIBILITY_RESPONSE_CODE callMemberByNameDOBSub(RequestObject requestObject, boolean save)
			throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseObject<WellnessResponseObject> checkEligibility(RequestObject<WellnessRequestObject> requestObject)
			throws InterruptedException, ExecutionException {
		WellnessRequestObject wellnessRequestObject = null;
		if (requestObject instanceof WellnessRequestObject)
			wellnessRequestObject = (WellnessRequestObject) requestObject;
		else return null;
		ResponseObject<WellnessResponseObject> responseObject = null;
		String dob = AuthenticationHelper.convertDateToLocalFormat(sessionInfo().getDob());
		WCPValidRequest wcpValidRequest = new WCPValidRequest();
		WCPMemberProductEligibilityRequest wcpMemberProductEligibilityRequest=new WCPMemberProductEligibilityRequest();
		WCPRequestHeader wcprequestHeader=new WCPRequestHeader();
		wcprequestHeader.setApplicationName("RALLY");
		wcprequestHeader.setTransactionid(UUID.randomUUID().toString());
		wcpMemberProductEligibilityRequest.setWcpRequestHeader(wcprequestHeader);
		WCPConsumerDetails consumerDetails =new WCPConsumerDetails();
		consumerDetails.setFirstName(wellnessRequestObject.getFirstName());
		consumerDetails.setLastName(wellnessRequestObject.getLastName());
		consumerDetails.setDateofBirth(dob);
		consumerDetails.setSearchId(wellnessRequestObject.getSubscriberId());
		consumerDetails.setContractNumber(wellnessRequestObject.getPolicyNumber());
		wcpMemberProductEligibilityRequest.setWcpConsumerDetails(consumerDetails);
		WCPRequestDetails requestDetails=new WCPRequestDetails();
		requestDetails.setRequestType("BIG5");
		requestDetails.setSearchType("PRODUCT");
		wcpMemberProductEligibilityRequest.setWcpRequestDetails(requestDetails);
		WCPFilteringAttributes filteringAttributes=new WCPFilteringAttributes();
		filteringAttributes.setApplyFilters("true");
		filteringAttributes.setIncludeExtendedAttributes("false");
		wcpMemberProductEligibilityRequest.setWcpfilteringAttributes(filteringAttributes);
		wcpValidRequest.setMemberProductEligibilityRequest(wcpMemberProductEligibilityRequest);
		WCPValidResponse wcpResponse = null;
		try {
			if(wcpValidRequest!=null) {
				wcpResponse = wcpEligibilityService.getWcpEligibility(wcpValidRequest).get();
				WellnessResponseObject wellnessResponseObject=new WellnessResponseObject();
				wellnessResponseObject.setWCPResponse(wcpResponse);
				if(wcpResponse!=null && wcpResponse.getMemberProductEligibilityResponse()!=null
						&& wcpResponse.getMemberProductEligibilityResponse().getExceptionDetail()!=null){
					sessionInfo().setStatusCode("NotFound");
					responseObject = new ResponseObject<WellnessResponseObject>(SERVICE_RESPONSE_STATUS_CODE.SERVICE_FAILED,
							wcpResponse.getMemberProductEligibilityResponse().getExceptionDetail().getExceptionMessage(),
							null);
					responseObject.setResponse(null);
				}
				else{
					if(wcpResponse!=null && wcpResponse.getMemberProductEligibilityResponse()!=null &&
							wcpResponse.getMemberProductEligibilityResponse().getProductDetails()!=null
							&& wcpResponse.getMemberProductEligibilityResponse().getProductDetails().getProduct()!=null &&
							wcpResponse.getMemberProductEligibilityResponse().getProductDetails().getProduct().size()>=1){
						sessionInfo().setStatusCode(SERVICE_RESPONSE_STATUS_CODE.MEMBER_FOUND.toString());
						responseObject = new ResponseObject<WellnessResponseObject>(SERVICE_RESPONSE_STATUS_CODE.MEMBER_FOUND,
								null,
								null);
						responseObject.setResponse(wellnessResponseObject);
						this.updateMemberEligibilityObject(requestObject,responseObject);
					} else {
						sessionInfo().setStatusCode("NotFound");
						responseObject = new ResponseObject<WellnessResponseObject>(SERVICE_RESPONSE_STATUS_CODE.MEMBER_NOT_FOUND,
								null,
								null);
						responseObject.setResponse(null);
					}
				}
			} 

		} catch (Exception e) {
			
			AuditManager.getInstance().logAuditMessage(EligibilityAudit.CLEO024, sessionInfo().getSessionInfo()); // Audit
		}
		return responseObject;
	}

	@Override
	public ModelAndView exit(EligibilityError eligibilityError) {
		if (AuthenticationHelper.isServiceCall(httpServletRequest)
				|| sessionInfo().isAPIAction()) {

			response.setStatus(HttpStatus.NOT_FOUND.value());

			return AuthenticationHelper.createServiceResponse(sessionInfo(),
					eligibilityError,
					PortalDestinationType.HEN.name(),null);

		} else {

			if (StringUtils.isNotBlank(sessionInfo().getInboundParameter().getErrorUrl()) 
					&& sessionInfo().isSignInAction()
					&& sessionInfo().isDestinationMYUHC()) {

				StringBuilder targetUrl = new StringBuilder(sessionInfo().getInboundParameter().getErrorUrl());
				if (StringUtils.contains(sessionInfo().getInboundParameter().getErrorUrl(), "?")) {
					targetUrl.append("&");
				} else {
					targetUrl.append("?");
				}
				targetUrl.append("errorCode=").append(eligibilityError.name());

				return AuthenticationHelper.redirectTargetURL(targetUrl.toString());

			} else {

				RedirectView mv = new RedirectView(
						"/content/healthsafeid/SigninError1/" + sessionInfo().getInboundParameter().getPortalBrand()
								+ "/" + sessionInfo().getInboundParameter().getAction().name().toLowerCase());
				mv.setHttp10Compatible(false);

				return new ModelAndView(mv);
			}
		}
	}

	@Override
	public ModelAndView exit() {
		return eligibilityController.handlePostCheckWellnessEligibilityRoutine();
	}

	@Override
	public void updateMemberEligibilityObject(RequestObject<WellnessRequestObject> requestObject,
			ResponseObject<WellnessResponseObject> responseObject) {
		// Set Wellness eligibilty
        if (responseObject.getResponse().getWCPResponse() != null) // WCP eligibility found
        {
                if (sessionInfo().getMemberEligibility() == null) {
                        sessionInfo().setMemberEligibility(new MemberEligibility());
                }

               
                SubscriberType subscriberType=new SubscriberType();
               


        } else { 


                sessionInfo().setWellnessIneligible(true);
        }
		
	}
	public ModelAndView handleGracePeriod(PortalDestinationType portalDestinationType) {
		if (sessionInfo().isSignInAction()
				&& ProvisionDataSourceHelper.isMemberRecordPresent(sessionInfo().getProvisionEnrolledSites())) {
			Date dateOfLastEligibilityLoss = ProvisionDataSourceHelper
					.getDateOfLastEligibilityFailureFromDbPortalRecord(sessionInfo().getProvisionEnrolledSites(),
							portalDestinationType);
			if (dateOfLastEligibilityLoss != null) {
				if (GracePeriodLogicUtil.isWithinDateOfMaximumHoursOfLossPeriod(dateOfLastEligibilityLoss,
						GracePeriodLogicUtil
								.getMaximumHoursOfEligibilityDataLossPerPortal(portalDestinationType))) {
					if (GracePeriodLogicUtil.isMemberInEligibilityLossSituation(portalDestinationType,
							sessionInfo().getDbCachedMemberEligibility())) {
						GracePeriodLogicUtil.copyCurrentMemberEligibilityWithCachedMemberEligibility(
								portalDestinationType, sessionInfo().getMemberEligibility(),
								sessionInfo().getDbCachedMemberEligibility());
						String healthsafeId = sessionInfo().getProvisionEnrolledSites().getHealthSafeId();
						String provisionMemberId = sessionInfo().getProvisionEnrolledSites().getProvisionMemberId();
						GracePeriodLogicUtil.dbUpdateDateOfLoss(provisionDataStoreService, portalDestinationType,
								healthsafeId, provisionMemberId, new Date());
						return exit();
					}
				} else {
					String healthsafeId = sessionInfo().getProvisionEnrolledSites().getHealthSafeId();
					String provisionMemberId = sessionInfo().getProvisionEnrolledSites().getProvisionMemberId();
					GracePeriodLogicUtil.dbUpdateDateOfLoss(provisionDataStoreService, portalDestinationType,
							healthsafeId, provisionMemberId, null);
				}
			} else {
				Date lastCacheUpdatedDate = sessionInfo().getDbCachedMemberEligibilityUpdatedTime();
				if (lastCacheUpdatedDate != null) {
					if (GracePeriodLogicUtil.isWithinDateOfMaximumHoursOfLossPeriod(lastCacheUpdatedDate,
							GracePeriodLogicUtil
									.getMaximumHoursOfEligibilityDataLossPerPortal(portalDestinationType))) {
						if (GracePeriodLogicUtil.isMemberInEligibilityLossSituation(portalDestinationType,
								sessionInfo().getDbCachedMemberEligibility())) {
							GracePeriodLogicUtil.copyCurrentMemberEligibilityWithCachedMemberEligibility(
									portalDestinationType, sessionInfo().getMemberEligibility(),
									sessionInfo().getDbCachedMemberEligibility());
							String healthsafeId = sessionInfo().getProvisionEnrolledSites().getHealthSafeId();
							String provisionMemberId = sessionInfo().getProvisionEnrolledSites().getProvisionMemberId();
							GracePeriodLogicUtil.dbUpdateDateOfLoss(provisionDataStoreService,
									portalDestinationType, healthsafeId, provisionMemberId, new Date());
							return exit();
						}
					} else {
						String healthsafeId = sessionInfo().getProvisionEnrolledSites().getHealthSafeId();
						String provisionMemberId = sessionInfo().getProvisionEnrolledSites().getProvisionMemberId();
						GracePeriodLogicUtil.dbUpdateDateOfLoss(provisionDataStoreService, portalDestinationType,
								healthsafeId, provisionMemberId, null);
					}
				}

			}
		}
		return null;
	}
}
