package com.optum.ogn.model;

import com.ibm.mdm.port.party.GetPartyOutputType;
import com.ibm.mdm.port.party.GetPersonOutputType;
import com.optum.ogn.domain.RequestObject;
import com.optum.ogn.eligibilityschema.MemberEligibility;
import com.optum.ogn.eligibilityschema.PortalDestinationType;
import com.optum.ogn.mdm.soap.physical.TCRMPartyIdentificationBObjType;
import com.optum.ogn.mdm.soap.physical.TCRMPersonNameBObjType;
import com.optum.ogn.mdm.soap.physical.TCRMPersonSearchResultBObjType;
import com.optum.ogn.mdm.soap.physical.XPersonHealthcareBObjType;
import com.optum.ogn.provision.model.GetMemberAttrResponse;
import com.optum.ogn.provision.model.MbrPrtlAtr;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SessionInfoWrapper implements Serializable {

    protected SessionInfo sessionInfo;

    @PostConstruct
    private void init() {
        sessionInfo = new SessionInfo();
    }

    public SessionInfo getSessionInfo() {
        return sessionInfo;
    }

    public InboundParameter getInboundParameter() {
        return sessionInfo.getInboundParameter();
    }

    public void setInboundParameter(InboundParameter inboundParameter) {
        sessionInfo.setInboundParameter(inboundParameter);
    }

    public MemberEligibility getMemberEligibility() {
        return sessionInfo.getMemberEligibility();
    }

    public void setMemberEligibility(MemberEligibility memberEligibility) {
        sessionInfo.setMemberEligibility(memberEligibility);
    }

    public GetMemberAttrResponse getProvisionEnrolledSites() {
        return sessionInfo.getProvisionEnrolledSites();
    }

    public void setProvisionEnrolledSites(GetMemberAttrResponse provisionEnrolledSites) {
        sessionInfo.setProvisionEnrolledSites(provisionEnrolledSites);
    }

    public List<TCRMPersonSearchResultBObjType> getTemporaryMDMresult() {
        return sessionInfo.getTemporaryMDMresult();
    }

    public void setTemporaryMDMresult(List<TCRMPersonSearchResultBObjType> temporaryMDMresult) {
        sessionInfo.setTemporaryMDMresult(temporaryMDMresult);
    }

    public Map<String, GetPartyOutputType> getTemporaryMDMParty() {
        return sessionInfo.getTemporaryMDMParty();
    }

    public void setTemporaryMDMParty(Map<String, GetPartyOutputType> temporaryMDMParty) {
        sessionInfo.setTemporaryMDMParty(temporaryMDMParty);
    }

    //MYUHC Changes Start
    public String getPortalIndicator()
    {
    	return sessionInfo.getPortalIndicator();
    }

    public void setPortalIndicator(String portal)
    {
    	sessionInfo.setPortalIndicator(portal);
    }

    public List<MyuhcPlansInfo> getMyuhcPlanInfo() {
        return sessionInfo.getMyuhcPlanInfo();
    }

    public void setMyuhcPlanInfo(List<MyuhcPlansInfo> myuhcPlanInfo) {
        sessionInfo.setMyuhcPlanInfo(myuhcPlanInfo);
    }

    public String getGroupNumber() {
        return sessionInfo.getGroupNumber();
    }

    public void setGroupNumber(String groupNumber) {
        sessionInfo.setGroupNumber(groupNumber);
    }

    public String getStatusCode() {
        return sessionInfo.getStatusCode();
    }

    public void setStatusCode(String myuhcStatusCode) {
        sessionInfo.setStatusCode(myuhcStatusCode);
    }

    public String getEligibilityKey() {
        return sessionInfo.getEligibilityKey();
    }

    public void setEligibilityKey(String eligibilityKey) {
        sessionInfo.setEligibilityKey(eligibilityKey);
    }

　
    //MYUHC Changes End.

　
　
    public int getuICounter() {
        return sessionInfo.getuICounter();
    }

    public void setuICounter(int uICounter) {
        sessionInfo.setuICounter(uICounter);
    }

    public String getFirstName() {
        return sessionInfo.getFirstName();
    }

    public void setFirstName(String firstName) {
        sessionInfo.setFirstName(firstName);
    }

    public String getLastName() {
        return sessionInfo.getLastName();
    }

    public void setLastName(String lastName) {
        sessionInfo.setLastName(lastName);
    }

    public Date getDob() {
        return sessionInfo.getDob();
    }

    public void setDob(String dob) {
        sessionInfo.setDob(dob);
    }

    public void setDob(Date dob) {
        sessionInfo.setDob(dob);
    }

    public String getZipCode() {
        return sessionInfo.getZipCode();
    }

    public void setZipCode(String zipCode) {
        sessionInfo.setZipCode(zipCode);
    }

    public String getMemberId() {
        return sessionInfo.getMemberId();
    }

    public void setMemberId(String memberId) {
        sessionInfo.setMemberId(memberId);
    }

    public String getPolicyId() {
        return sessionInfo.getPolicyId();
    }

    public void setPolicyId(String policyId) {
        sessionInfo.setPolicyId(policyId);
    }

    public String getSubscriberId() {
        return sessionInfo.getSubscriberId();
    }

    public void setSubscriberId(String subscriberId) {
        sessionInfo.setSubscriberId(subscriberId);
    }

    public String getSsn() {
        return sessionInfo.getSsn();
    }

    public void setSsn(String ssn) {
        sessionInfo.setSsn(ssn);
    }

    public boolean isHealthSafeIdFlag() {
        return sessionInfo.isHealthSafeIdFlag();
    }

    public void setHealthSafeIdFlag(boolean healthSafeIdFlag) {
        sessionInfo.setHealthSafeIdFlag(healthSafeIdFlag);
    }

    public String getSequenceId() {
        return sessionInfo.getSequenceId();
    }

    public void setSequenceId(String sequenceId) {
        sessionInfo.setSequenceId(sequenceId);
    }

    public boolean isRegisterError() {
        return sessionInfo.isRegisterError();
    }

    public void setRegisterError(boolean registerError) {
        sessionInfo.setRegisterError(registerError);
    }

    public boolean isMultipleUUIDError() {
        return sessionInfo.isMultipleUUIDError();
    }

    public void setMultipleUUIDError(boolean multipleUUIDError) {
        sessionInfo.setMultipleUUIDError(multipleUUIDError);
    }

    public boolean isMdmOverride() {
        return sessionInfo.isMdmOverride();
    }

    public void setMdmOverride(boolean mdmOverride) {
        sessionInfo.setMdmOverride(mdmOverride);
    }

    public boolean isEligibilityFound() {
        return sessionInfo.isEligibilityFound();
    }

    public void setCapIneligible (boolean capIneligible) {
        sessionInfo.setCapIneligible(capIneligible);
    }

    public void setMyuhcIneligible(boolean myuhcIneligible) {
		sessionInfo.setMyuhcIneligible(myuhcIneligible);
	}

    public boolean isCAPFound() {

        return sessionInfo.isCAPFound();
    }

    public boolean isCAPFoundInProvision() {
        return sessionInfo.isCAPFoundInProvision();
    }

    public boolean isCAPFoundInEligibility() {
        return sessionInfo.isCAPFoundInEligibility();
    }

    public void setLawwIneligible (boolean lawwIneligible) {
        sessionInfo.setLawwIneligible(lawwIneligible);
    }
    
    public void setWellnessIneligible (boolean wellnessIneligible) {
        sessionInfo.setWellnessIneligible(wellnessIneligible);
    }

    public boolean isLAWWFound() {
        return sessionInfo.isLAWWFound();
    }

    public boolean isLAWWFoundInProvision() {
        return sessionInfo.isLAWWFoundInProvision();
    }

    public boolean isLAWWFoundInEligibility() {
        return sessionInfo.isLAWWFoundInEligibility();
    }

    public boolean isRXFound() {
        return sessionInfo.isRXFound();
    }
    //MYUHC Changes Start
    public boolean isMYUHCFound() {
        return sessionInfo.isMYUHCFound();
    }

    public boolean isMYUHCFoundInProvision() {
        return sessionInfo.isMYUHCFoundInProvision();
    }

    public boolean isMYUHCFoundInEligibility() {
        return sessionInfo.isMYUHCFoundInEligibility();
    }
    //MYUHC Changes end

//    public boolean isFRONTDOORFound() {
//        return sessionInfo.isFRONTDOORFound();
//    }
    
    public boolean isCCDFound() {
        return sessionInfo.isCCDFound();
    }

    public boolean isCCDFoundInProvision() {
        return sessionInfo.isCCDFoundInProvision();
    }

    public boolean isCCDFoundInEligibility() {
        return sessionInfo.isCCDFoundInEligibility();
    }

    public boolean isAPIAction() {
        return sessionInfo.isAPIAction();
    }

    public boolean isSignInAction() {

        return sessionInfo.isSignInAction();
    }

    public boolean isRegisterAction() {
        return sessionInfo.isRegisterAction();
    }

    public boolean isMDMGoldenRecordFound() {
        return sessionInfo.isMDMGoldenRecordFound();
    }

    public Map<String, XPersonHealthcareBObjType> getSubscriberIdsFromMDMGolden() {

        return sessionInfo.getSubscriberIdsFromMDMGolden();
    }

    public String getEntityIdFromMDMGolden() {

        return sessionInfo.getEntityIdFromMDMGolden();
    }

    public boolean isMDMDifferFromProvision() {
        return sessionInfo.isMDMDifferFromProvision();
    }

    //Myuhc Changes Strat
    public boolean isDestinationMYUHC() {
        return sessionInfo.isDestinationMYUHC();
    }
    //Myuhc Changes End

    public boolean isDestinationLAWW() {
        return sessionInfo.isDestinationLAWW();
    }

    public boolean isDestinationCAP() {
        return sessionInfo.isDestinationCAP();
    }

    public boolean isDestinationRX() {
        return sessionInfo.isDestinationRX();
    }

    public boolean isDestinationFRONTDOOR() {
        return sessionInfo.isDestinationFRONTDOOR();
    }
    
    public boolean isDestinationCCD() {
        return sessionInfo.isDestinationCCD();
    }

    public boolean isAccessTypeTIER1() {
        return sessionInfo.isAccessTypeTIER1();
    }

    public boolean isAccessTypeTIER2() {
        return sessionInfo.isAccessTypeTIER2();
    }

    public MbrPrtlAtr getEnrolledPortalByName (PortalDestinationType portalType) {
        return sessionInfo.getEnrolledPortalByName(portalType);
    }
    public List<MbrPrtlAtr> getEnrolledPortalByName(PortalDestinationType portalType, boolean all) {
    	return sessionInfo.getEnrolledPortalByName(portalType, all);
    }
    public void setEnrolledPortalByName(PortalDestinationType portalType, MbrPrtlAtr mbrPrtlAtr) {
    	sessionInfo.setEnrolledPortalByName(portalType, mbrPrtlAtr);
    }

    public String getClientIp() {
    	return sessionInfo.getClientIp();
    }
    public void setClientIp(String clientIp) {
    	sessionInfo.setClientIp(clientIp);
    }
    public String getSourceIp() {
    	return sessionInfo.getSourceIp();
    }
    public void setSourceIp(String sourceIp) {
    	sessionInfo.setSourceIp(sourceIp);
    }
    
    /**
	 * @return the decryptSSN
	 */
	public String getDecryptSSN() {
		return sessionInfo.getDecryptSSN();
	}

	/**
	 * @param decryptSSN the decryptSSN to set
	 */
	public void setDecryptSSN(String decryptSSN) {
		sessionInfo.setDecryptSSN(decryptSSN);
	}
    
    public Date getTermsAgreeDate() {
		return sessionInfo.getTermsAgreeDate();
	}

	public void setTermsAgreeDate(String termsAgreeDate) {
		sessionInfo.setTermsAgreeDate(termsAgreeDate); 
	}
	
	public String getEmployeeId() {
		return sessionInfo.getEmployeeId();
	}
	
	public void setEmployeeId(String employeeId) {
		sessionInfo.setEmployeeId(employeeId);
	}
	
	public String getPolicyNumber() {
		return sessionInfo.getPolicyNumber();
	}
	
	public void setPolicyNumber(String policyNumber) {
		sessionInfo.setPolicyNumber(policyNumber);
	}
	public List<RequestObject> getRequestObjectList() {
		return sessionInfo.getRequestObjectList();
	}

	public void setRequestObjectList(List<RequestObject> requestObjectList) {
		sessionInfo.setRequestObjectList(requestObjectList);
	}
	public boolean isLobsbPrefetch() {
		return sessionInfo.isLobsbPrefetch();
	}

	public void setLobsbPrefetch(boolean lobsbPrefetch) {
		sessionInfo.setLobsbPrefetch(lobsbPrefetch);
	}

    public boolean isFromCache() {
        return sessionInfo.isFromCache();
    }

    public void setFromCache(boolean fromCache) {
        sessionInfo.setFromCache(fromCache);
    }

    public String getCacheLastUpdateStamp() {
        return sessionInfo.getCacheLastUpdateStamp();
    }

    public void setCacheLastUpdateStamp(String cacheLastUpdateStamp) {
        sessionInfo.setCacheLastUpdateStamp(cacheLastUpdateStamp);
    }

    public String getBrandUrl() {
		return sessionInfo.getBrandUrl();
	}

	public void setBrandUrl(String brandUrl) {
		sessionInfo.setBrandUrl(brandUrl);
	}
	public String getSignOutUrl() {
		return sessionInfo.getSignOutUrl();
	}

	public void setSignOutUrl(String signOutUrl) {
		sessionInfo.setSignOutUrl(signOutUrl);
	}
	public String getTimeOutUrl() {
		return sessionInfo.getTimeOutUrl();
	}

	public void setTimeOutUrl(String timeOutUrl) {
		sessionInfo.setTimeOutUrl(timeOutUrl);
	}

	public String getDestinationUrl() {
		return sessionInfo.getDestinationUrl();
	}

	public void setDestinationUrl(String destinationUrl) {
		sessionInfo.setDestinationUrl(destinationUrl);
	}

	public String getDestinationSiteminderUrl() {
		return sessionInfo.getDestinationSiteminderUrl();
	}

	public void setDestinationSiteminderUrl(String destinationSiteminderUrl) {
		sessionInfo.setDestinationSiteminderUrl(destinationSiteminderUrl);
	}

	public String getHealthsafeIdSiteminderUrl() {
		return sessionInfo.getHealthsafeIdSiteminderUrl();
	}

	public void setHealthsafeIdSiteminderUrl(String healthsafeIdSiteminderUrl) {
		sessionInfo.setHealthsafeIdSiteminderUrl(healthsafeIdSiteminderUrl);
	}
	
	public Map<String, GetPersonOutputType> getAliasToMDMParty() {
		return sessionInfo.getAliasToMDMParty();
	}

	public void setAliasToMDMParty(
			Map<String, GetPersonOutputType> aliasToMDMParty) {
		sessionInfo.setAliasToMDMParty(aliasToMDMParty);
	}
	public String getUnAuthenticatedUserID() {
		return sessionInfo.getUserId();
	}

	public void setUnAuthenticatedUserID(String userId) {
		sessionInfo.setUserId(userId);
	}

	public String getUnAuthenticatedUUID() {
		return sessionInfo.getUuid();
	}

	public void setUnAuthenticatedUUID(String uuid) {
		sessionInfo.setUuid(uuid);
	}
	
	public String getMobileNumber() {
		return sessionInfo.getMobileNumber();
	}

	public void setMobileNumber(String mobileNumber) {
		sessionInfo.setMobileNumber(mobileNumber);
	}
	
	public String getMobileType() {
		return sessionInfo.getMobileType();
	}

	public void setMobileType(String mobileType) {
		sessionInfo.setMobileType(mobileType);
	}
	
	public String getEmail() {
		return sessionInfo.getEmail();
	}

	public void setEmail(String email) {
		 sessionInfo.setEmail(email);
	}
	public SecurityContextDataModel getSecurityContext() {
		return sessionInfo.getSecurityContext();
	}

	public void setSecurityContext(SecurityContextDataModel securityContext) {
		sessionInfo.setSecurityContext(securityContext);
	}

	public void setisMobileTypeHome(boolean mobileType) {
		sessionInfo.setisMobileTypeHome(mobileType);
	}

	public boolean isHome() {
		return sessionInfo.isHome();
	}

    public boolean isPartialEligibility() {
        return sessionInfo.isPartialEligibility();
    }
    
	
	//GE HUB changes
	
	public boolean isDestinationGEHUB() {
        return sessionInfo.isDestinationGEHUB();
    }
    public Map<String, XPersonHealthcareBObjType> getPolicyNumsFromMDMGolden() {
    	return sessionInfo.getPolicyNumsFromMDMGolden();
    }
    public Map<String, TCRMPartyIdentificationBObjType> getrxMemberIdsFromMDMGolden() {
    	return sessionInfo.getrxMemberIdsFromMDMGolden();
    }
    
    public List<TCRMPersonNameBObjType> getAliasPersonNameFromMDMGolden(){
    	return sessionInfo.getAliasPersonNameFromMDMGolden();
    }
    

	public String getEncryptedSsn() {
		return sessionInfo.getEncryptedSsn();
	}
	public void setEncryptedSsn(String encryptedSsn) {
		sessionInfo.setEncryptedSsn(encryptedSsn);
	}
	public String getHashedSsn() {
		return sessionInfo.getHashedSsn();
	}
	public void setHashedSsn(String hashedSsn) {
		sessionInfo.setHashedSsn(hashedSsn);
	}

	public boolean isRxFoundInEligibillity() {
		return sessionInfo.isRxFoundInEligibillity();
	}
	public boolean isExternalCallException() {
		return sessionInfo.isExternalCallException();
	}
	public void setExternalCallException(boolean isExternalCallException) {
		sessionInfo.setExternalCallException(isExternalCallException);
	}
	public MemberEligibility getDbCachedMemberEligibility() {
		return sessionInfo.getDbCachedMemberEligibility();
	}
	public void setDbCachedMemberEligibility(MemberEligibility dbCachedMemberEligibility) {
		sessionInfo.setDbCachedMemberEligibility(dbCachedMemberEligibility);
	}
	public Date getDbCachedMemberEligibilityUpdatedTime() {
		return sessionInfo.getDbCachedMemberEligibilityUpdatedTime();
	}
	public void setDbCachedMemberEligibilityUpdatedTime(Date dbCachedMemberEligibilityUpdatedTime) {
		sessionInfo.setDbCachedMemberEligibilityUpdatedTime(dbCachedMemberEligibilityUpdatedTime);
	}

	public boolean isDestinationHealthyNotes() {
		 return sessionInfo.isDestinationHealthyNotes();
	}
	public boolean isDestinationWellness() {
		 return sessionInfo.isDestinationWellness();
	}

}
