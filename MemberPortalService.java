package com.optum.ogn.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.optum.ogn.constants.Constants;
import com.optum.ogn.dao.impl.DomainObjectManagementService;
import com.optum.ogn.domain.Mbr;
import com.optum.ogn.domain.MbrPrtl;
import com.optum.ogn.domain.MbrRgstTyp;
import com.optum.ogn.domain.MbrSts;
import com.optum.ogn.domain.Prtl;
import com.optum.ogn.exceptions.GlobalNavException;
import com.optum.ogn.exceptions.MessageType;
import com.optum.ogn.exceptions.MessagesType;
import com.optum.ogn.exceptions.StatusType;
import com.optum.ogn.mapper.MemberPortalMapper;
import com.optum.ogn.model.GetMemberAttrResponse;
import com.optum.ogn.model.GetMembersRequestDetails;
import com.optum.ogn.model.MemberRequest;
import com.optum.ogn.persistence.MbrRegsTypeRepository;
import com.optum.ogn.persistence.MbrStsRepository;
import com.optum.ogn.persistence.MemberPortalRepository;
import com.optum.ogn.persistence.MemberRepository;
import com.optum.ogn.persistence.PortalRepository;
import com.optum.ogn.util.ProvisioningUtil;

public class MemberPortalService extends DomainObjectManagementService<MbrPrtl> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemberPortalService.class);
	private final MemberService memberService;
	private final PortalRepository jpaPortalRepository;
	private final MemberPortalRepository jpaMemberPortalRepository;
	private final MbrRegsTypeRepository jpaMbrRegstRepository;

	public List<GetMemberAttrResponse> memAttrResList = null;
	public MemberPortalMapper mbrPrtlMapper = new MemberPortalMapper();
	private final MbrStsRepository jpaMbrStsRepository;

	@Autowired
	public MemberPortalService(final MemberRepository jpaMemberRepository, final PortalRepository jpaPortalRepository,
			final MemberPortalRepository jpaMemberPortalRepository, final MemberService memberService,
			final PortalService portalService, final MbrStsRepository jpaMbrStsRepository,
			MbrRegsTypeRepository jpaMbrRegstRepository) {
		super(jpaMemberPortalRepository);
		// added new argument for mbr registration type
		this.jpaPortalRepository = jpaPortalRepository;
		this.jpaMemberPortalRepository = jpaMemberPortalRepository;
		this.memberService = memberService;
		this.jpaMbrStsRepository = jpaMbrStsRepository;
		this.jpaMbrRegstRepository=jpaMbrRegstRepository;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = GlobalNavException.class)
	public MbrPrtl createOrUpdate(MbrPrtl mbrPrtl) throws GlobalNavException {

		MessagesType mts = null;
		StringBuilder sb = null;
		StatusType st = null;
		try {
			LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
			Mbr persistedMbr = null;
			Prtl persistedPrtl = null;
			MbrPrtl alreadyExistedMbrPrtl = null;

			// assumption is member details should be there.
			persistedMbr = memberService.getMbrByUniqueId(mbrPrtl.getMbrByMbrId());

			if (persistedMbr == null || persistedMbr.getPrvsnMbrId() <= 0) {
				if (mts == null)
					mts = new MessagesType();
				MessageType mt = new MessageType();
				mt.setCode("400");
				mt.setSeverity(Constants.Severity.FAULT.name());
				mt.setName(Constants.StatusMsgNm.MEMBER_NOT_FOUND.name());
				sb = new StringBuilder();

				sb.append("Could not locate the the member with provision MemberId/HealthSafeId\n");
				if (mbrPrtl.getMbrByMbrId() != null) {
					sb.append("provision member Id:'").append(mbrPrtl.getMbrByMbrId().getPrvsnMbrId()).append("'\n");
					sb.append("HealthSafeId:'").append(mbrPrtl.getMbrByMbrId().getHealthSafeId()).append("'\n");
				} else {
					sb.append("provision member Id:'").append("").append("'\n");
					sb.append("HealthSafeId:'").append("").append("'\n");
				}
				mt.setDescription(sb.toString());
				mts.getMessage().add(mt);
				if (st == null) {
					st = new StatusType();
				}
				st.setMessages(mts);
				throw new GlobalNavException(st);

			} else {
				mbrPrtl.setMbrByMbrId(persistedMbr);
			}
			// assumption is portal data should always be there, no need to
			// create.

			persistedPrtl = jpaPortalRepository.findByPrtlUniqueId(mbrPrtl.getPrtlByPrtlId());

			if (persistedPrtl == null || persistedPrtl.getPrtlId() <= 0) {
				if (mts == null)
					mts = new MessagesType();
				MessageType mt = new MessageType();
				mt.setCode("400");
				mt.setSeverity(Constants.Severity.FAULT.name());
				mt.setName(Constants.StatusMsgNm.PORTAL_NOT_FOUND.name());
				sb = new StringBuilder();
				sb.append("Could not locate the the portal with the given portal short name\n");
				if (mbrPrtl.getPrtlByPrtlId() != null) {
					sb.append("Portal short name:'").append(mbrPrtl.getPrtlByPrtlId().getPrtlShrtNm()).append("'\n");
				} else {
					sb.append("Portal short name:'").append("").append("'\n");
				}
				mt.setDescription(sb.toString());
				mts.getMessage().add(mt);
				if (st == null) {
					st = new StatusType();
				}
				st.setMessages(mts);
				throw new GlobalNavException(st);
			} else
				mbrPrtl.setPrtlByPrtlId(persistedPrtl);

			if (persistedMbr != null && persistedPrtl != null) {

				// alreadyExistedMbrPrtl=jpaMemberPortalRepository.findByMbrPrtlUniqueId(persistedMbr.getPrvsnMbrId(),
				// persistedPrtl.getPrtlId());
				// alreadyExistedMbrPrtl=jpaMemberPortalRepository.findByMbrPrtlUniqueId(persistedMbr.getPrvsnMbrId(),
				// persistedPrtl.getPrtlId(),
				// mbrPrtl.getMbrPrtlPolNbr(),
				// mbrPrtl.getMbrPrtlSbscrId());
				// Fix for member id change scenarios- DE67016 Fix
				String prtlShortName = mbrPrtl.getPrtlByPrtlId().getPrtlShrtNm();
				if (StringUtils.equalsIgnoreCase(prtlShortName, "MYUHC")) {
					alreadyExistedMbrPrtl = jpaMemberPortalRepository.findByMbrPrtlUniqueId(
							persistedMbr.getPrvsnMbrId(), persistedPrtl.getPrtlId(), mbrPrtl.getMbrPrtlPolNbr(),
							mbrPrtl.getMbrPrtlSbscrId());
				} else {
					List<MbrPrtl> mbrPrtlRecs = jpaMemberPortalRepository
							.findByMbrPrtlUniqueId(persistedMbr.getPrvsnMbrId(), persistedPrtl.getPrtlId());
					if (!CollectionUtils.isEmpty(mbrPrtlRecs) && mbrPrtlRecs.size() == 1) {
						alreadyExistedMbrPrtl = mbrPrtlRecs.get(0);
					} else if (!CollectionUtils.isEmpty(mbrPrtlRecs) && mbrPrtlRecs.size() > 1) {
						if (mts == null)
							mts = new MessagesType();
						MessageType mt = new MessageType();
						mt.setCode("400");
						mt.setSeverity(Constants.Severity.FAULT.name());
						mt.setName(Constants.StatusMsgNm.PORTAL_VALIDATION_ERROR.name());
						sb = new StringBuilder();
						sb.append("There are more than one portal records for\n");
						if (mbrPrtl.getPrtlByPrtlId() != null) {
							sb.append("Portal short name:'").append(mbrPrtl.getPrtlByPrtlId().getPrtlShrtNm())
									.append("'\n");
						} else {
							sb.append("Portal short name:'").append("").append("'\n");
						}
						mt.setDescription(sb.toString());
						mts.getMessage().add(mt);
						if (st == null) {
							st = new StatusType();
						}
						st.setMessages(mts);
						throw new GlobalNavException(st);
					}
				}

				/*
				 * List<MbrPrtl>
				 * mbrPrtlRecs=jpaMemberPortalRepository.findByMbrPrtlUniqueId(
				 * persistedMbr.getPrvsnMbrId(), persistedPrtl.getPrtlId());
				 * if(!CollectionUtils.isEmpty(mbrPrtlRecs) &&
				 * mbrPrtlRecs.size()>0){
				 * if(StringUtils.equalsIgnoreCase(prtlShortName,"CCD")){
				 * if(!StringUtils.isEmpty(mbrPrtl.getMbrPrtlSpcMbrId())){
				 * for(MbrPrtl mbrPrtlrec:mbrPrtlRecs){
				 * if(StringUtils.equalsIgnoreCase(ProvisioningUtil.
				 * trimLeadingZeros(mbrPrtlrec.getMbrPrtlSpcMbrId()),
				 * ProvisioningUtil.trimLeadingZeros(mbrPrtl.getMbrPrtlSpcMbrId(
				 * )))){ alreadyExistedMbrPrtl=mbrPrtlrec; } } } } else
				 * if(StringUtils.equalsIgnoreCase(prtlShortName,"CAP")){
				 * if(!StringUtils.isEmpty(mbrPrtl.getMbrPrtlSsn())){
				 * for(MbrPrtl mbrPrtlrec:mbrPrtlRecs){
				 * if(StringUtils.equalsIgnoreCase(mbrPrtlrec.getMbrPrtlSsn(),
				 * mbrPrtl.getMbrPrtlSsn())){ alreadyExistedMbrPrtl=mbrPrtlrec;
				 * } } } } else{
				 * if(!StringUtils.isEmpty(mbrPrtl.getMbrPrtlSbscrId()) ||
				 * !StringUtils.isEmpty(mbrPrtl.getMbrPrtlPolNbr())){
				 * for(MbrPrtl mbrPrtlrec:mbrPrtlRecs){
				 * if(!StringUtils.isEmpty(mbrPrtl.getMbrPrtlSbscrId()) &&
				 * !StringUtils.isEmpty(mbrPrtl.getMbrPrtlPolNbr())){
				 * if(StringUtils.equalsIgnoreCase(ProvisioningUtil.
				 * trimLeadingZeros(mbrPrtlrec.getMbrPrtlSbscrId()),
				 * ProvisioningUtil.trimLeadingZeros(mbrPrtl.getMbrPrtlSbscrId()
				 * )) && StringUtils.equalsIgnoreCase(ProvisioningUtil.
				 * trimLeadingZeros(mbrPrtlrec.getMbrPrtlPolNbr()),
				 * ProvisioningUtil.trimLeadingZeros(mbrPrtl.getMbrPrtlPolNbr())
				 * )){ alreadyExistedMbrPrtl=mbrPrtlrec; } } else
				 * if(!StringUtils.isEmpty(mbrPrtl.getMbrPrtlSbscrId()) &&
				 * StringUtils.isEmpty(mbrPrtl.getMbrPrtlPolNbr())){
				 * if(StringUtils.equalsIgnoreCase(ProvisioningUtil.
				 * trimLeadingZeros(mbrPrtlrec.getMbrPrtlSbscrId()),
				 * ProvisioningUtil.trimLeadingZeros(mbrPrtl.getMbrPrtlSbscrId()
				 * ))){ alreadyExistedMbrPrtl=mbrPrtlrec; } } } }
				 * 
				 * } }
				 */

				if (StringUtils.equalsIgnoreCase(prtlShortName, "MYUHC") && alreadyExistedMbrPrtl != null) {
					if (!StringUtils.equalsIgnoreCase(alreadyExistedMbrPrtl.getMbrPrtlPolNbr(),
							mbrPrtl.getMbrPrtlPolNbr())
							|| !StringUtils.equalsIgnoreCase(alreadyExistedMbrPrtl.getMbrPrtlSbscrId(),
									mbrPrtl.getMbrPrtlSbscrId())) {
						alreadyExistedMbrPrtl = null;
					}
				}

				if (alreadyExistedMbrPrtl != null) {
					alreadyExistedMbrPrtl = mbrPrtlMapper.updateMbrPrtlWithNewValues(alreadyExistedMbrPrtl, mbrPrtl);
					mbrPrtl = persist(alreadyExistedMbrPrtl);
				} else {
					mbrPrtl = persist(mbrPrtl);
				}

			}
			LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: END");
			return mbrPrtl;
		} catch (GlobalNavException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			if (mts == null)
				mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.EXCEPTION_ENCOUNTERED.name());
			if (sb == null)
				sb = new StringBuilder();
			else
				sb.setLength(0);
			sb.append("Exception while creating or updating Member portal data\n");
			if (mbrPrtl.getMbrByMbrId() != null) {
				sb.append("provision member Id:'").append(mbrPrtl.getMbrByMbrId().getPrvsnMbrId()).append("'\n");
				sb.append("HealthSafeId:'").append(mbrPrtl.getMbrByMbrId().getHealthSafeId()).append("'\n");
			} else {
				sb.append("provision member Id:'").append("").append("'\n");
				sb.append("HealthSafeId:'").append("").append("'\n");
			}

			if (mbrPrtl.getPrtlByPrtlId() != null) {

				sb.append("Portal short name:'").append(mbrPrtl.getPrtlByPrtlId().getPrtlShrtNm()).append("'\n");
			} else {
				sb.append("Portal short name:'").append("").append("'\n");
			}
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			if (st == null) {
				st = new StatusType();
			}
			st.setMessages(mts);
			// if(LOGGER.isDebugEnabled())
			// LOGGER.debug(mt.getDescription(),ex);
			throw new GlobalNavException(st);
		}
	}

	public List<MbrPrtl> getMbrPrtlByHealthSafeId(String healthSafeId) throws GlobalNavException {

		try {

			return jpaMemberPortalRepository.findByMbrPrtlByHealthSafeId(healthSafeId);

		} catch (GlobalNavException ex) {
			throw ex;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.NOTFOUND.name());

			StringBuilder sb = new StringBuilder();

			sb.append("Exception in lookup member portal table with HealthSafeId\n");
			sb.append("HealthSafeId:'").append(healthSafeId).append("'\n");

			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();

			st.setMessages(mts);
			// if(LOGGER.isDebugEnabled())
			// LOGGER.debug(mt.getDescription(),ex);
			throw new GlobalNavException(st);
		}

	}

	public List<MbrPrtl> getMbrPrtlByProvisionMemberIdPortalShortNm(String provisionMemberId, String portalShortNm)
			throws GlobalNavException {

		try {

			return jpaMemberPortalRepository.findByMbrPrtlByProvisionMemberIdPortalShortNm(provisionMemberId,
					portalShortNm);

		} catch (GlobalNavException ex) {
			// ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {
			// ex.printStackTrace();
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.NOTFOUND.name());
			StringBuilder sb = new StringBuilder();
			sb.append("Exception in lookup member portal table with provisionMemberId and portalShortNm\n");
			sb.append("provisionMemberId:'").append(provisionMemberId).append("'\n");
			sb.append("portalShortNm:'").append(portalShortNm).append("'\n");

			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();

			st.setMessages(mts);
			// if(LOGGER.isDebugEnabled())
			// LOGGER.debug(mt.getDescription(),ex);
			throw new GlobalNavException(st);
		}

	}

	public MbrRgstTyp getMbrRegstType(Mbr mbr) throws GlobalNavException {
		try {
			/*
			 * if(!StringUtils.isEmpty(sixDigitSsn)){ List<MbrPrtl>
			 * mbrPrtls=jpaMemberPortalRepository.findMbrPrtl(healthSafeId,
			 * provisionMemberId,portalShortNm,firstNm,lastNm,aliasFIrstNameList
			 * ,aliasLastNameList,dob,subscriberId,zip,ssn,mbrPrtlSpcMbrId,
			 * policyNbr); if(!CollectionUtils.isEmpty(mbrPrtls)){ List<MbrPrtl>
			 * mbrPrtlList=new ArrayList<MbrPrtl>(); for(MbrPrtl
			 * mbrPrtl:mbrPrtls){
			 * if(mbrPrtl.getMbrByMbrId().getMdmMbrSsn().equalsIgnoreCase(
			 * sixDigitSsn)) mbrPrtlList.add(mbrPrtl); } return mbrPrtlList; }
			 * else{ return null; } } else{
			 */
			
			// added new argument for mbr registration type
			
			return jpaMbrRegstRepository.findMbrRegistrationType(mbr);
			// }
		} catch (GlobalNavException ex) {
			throw ex;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.NOTFOUND.name());
			StringBuilder sb = new StringBuilder();
			sb.append("Exception in lookup member registration type table with registration type Id\n");
			sb.append("RegistrationTypeId:'").append(mbr.getMbrRgstTypId()).append("'\n");

			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			// if(LOGGER.isDebugEnabled())
			// LOGGER.debug(mt.getDescription(),ex);
			throw new GlobalNavException(st);
		}
	}
	public MbrRgstTyp getMbrRegstTypeId(String registrationType) throws GlobalNavException {
		try {
			return jpaMbrRegstRepository.findMbrRegistrationTypeId(registrationType);
		} catch (GlobalNavException ex) {
			throw ex;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.NOTFOUND.name());
			StringBuilder sb = new StringBuilder();
			sb.append("Exception in lookup member registration type table with registration type \n");
			sb.append("RegistrationType:'").append(registrationType).append("'\n");

			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			// if(LOGGER.isDebugEnabled())
			// LOGGER.debug(mt.getDescription(),ex);
			throw new GlobalNavException(st);
		}
	}

	public List<MbrPrtl> getMbrPrtl(String healthSafeId, String provisionMemberId, String portalShortNm, String firstNm,
			String lastNm, Set<String> aliasFIrstNameList, Set<String> aliasLastNameList, String dob,
			String subscriberId, String zip, String ssn, String mbrPrtlSpcMbrId, String policyNbr)
			throws GlobalNavException {
		try {
			/*
			 * if(!StringUtils.isEmpty(sixDigitSsn)){ List<MbrPrtl>
			 * mbrPrtls=jpaMemberPortalRepository.findMbrPrtl(healthSafeId,
			 * provisionMemberId,portalShortNm,firstNm,lastNm,aliasFIrstNameList
			 * ,aliasLastNameList,dob,subscriberId,zip,ssn,mbrPrtlSpcMbrId,
			 * policyNbr); if(!CollectionUtils.isEmpty(mbrPrtls)){ List<MbrPrtl>
			 * mbrPrtlList=new ArrayList<MbrPrtl>(); for(MbrPrtl
			 * mbrPrtl:mbrPrtls){
			 * if(mbrPrtl.getMbrByMbrId().getMdmMbrSsn().equalsIgnoreCase(
			 * sixDigitSsn)) mbrPrtlList.add(mbrPrtl); } return mbrPrtlList; }
			 * else{ return null; } } else{
			 */
			return jpaMemberPortalRepository.findMbrPrtl(healthSafeId, provisionMemberId, portalShortNm, firstNm,
					lastNm, aliasFIrstNameList, aliasLastNameList, dob, subscriberId, zip, ssn, mbrPrtlSpcMbrId,
					policyNbr);
			// }
		} catch (GlobalNavException ex) {
			throw ex;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.NOTFOUND.name());
			StringBuilder sb = new StringBuilder();
			sb.append("Exception in lookup member/member portal tables with the given search criteria\n");
			sb.append("HealthSafeId:'").append(healthSafeId).append("'\n");
			sb.append("provisionMemberId :'").append(provisionMemberId).append("'\n");
			sb.append("portalShortNm :'").append(portalShortNm).append("'\n");
			sb.append("first Name :'").append(firstNm).append("'\n");
			sb.append("last Name :'").append(lastNm).append("'\n");
			sb.append("Date of Birth :'").append(dob).append("'\n");
			sb.append("subscriberId :'").append(subscriberId).append("'\n");
			sb.append("Zip Code :'").append(zip).append("'\n");
			sb.append("SSN :'").append(ssn).append("'\n");

			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			// if(LOGGER.isDebugEnabled())
			// LOGGER.debug(mt.getDescription(),ex);
			throw new GlobalNavException(st);
		}
	}

	public List<MbrPrtl> getMbrPrtl(List<MemberRequest> memberRequestList, boolean dupcheck) throws GlobalNavException {
		try {

			return jpaMemberPortalRepository.findMbrPrtl(memberRequestList, dupcheck);

		} catch (GlobalNavException ex) {
			throw ex;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.NOTFOUND.name());
			StringBuilder sb = new StringBuilder();
			sb.append("Exception in lookup member/member portal tables with the given search criteria\n");
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			throw new GlobalNavException(st);
		}
	}

	/*
	 * Retrieves MbrPortal details based on subscriber Id
	 */
	public List<MbrPrtl> getMbrPrtlBySubscriberId(String subscriberId, String policyNbr) throws GlobalNavException {
		try {

			return jpaMemberPortalRepository.findMbrPrtlBySubscriberId(subscriberId, policyNbr);
		} catch (GlobalNavException ex) {
			throw ex;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.NOTFOUND.name());
			StringBuilder sb = new StringBuilder();

			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			// if(LOGGER.isDebugEnabled())
			// LOGGER.debug(mt.getDescription(),ex);
			throw new GlobalNavException(st);
		}
	}

	public void deleteOrPurge(Set<MbrPrtl> mbrPrtls) throws GlobalNavException {
		MbrSts persistedMbrSts = null;

		MbrPrtl savedMbrPrtl = null;
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
		try {

			if (!CollectionUtils.isEmpty(mbrPrtls)) {
				for (MbrPrtl mbrprtl : mbrPrtls) {
					persistedMbrSts = jpaMbrStsRepository
							.findByMbrStsbyMbrStsDesc(mbrprtl.getPrtlByMbrStsId().getMbrStsDesc());
					mbrPrtlMapper.updateMbrPrtlWithMbrStatus(mbrprtl, persistedMbrSts);
					savedMbrPrtl = persist(mbrprtl);

				}

			}

			else {
				MessagesType mts = new MessagesType();
				MessageType mt = new MessageType();
				mt.setCode("404");
				mt.setSeverity(Constants.Severity.EXCEPTION.name());
				mt.setName(Constants.StatusMsgNm.MEMBER_NOT_FOUND.name());
				StringBuilder sb = new StringBuilder();

				sb.append("No Portal records found in PDB for search crietria");
				mt.setDescription(sb.toString());
				mts.getMessage().add(mt);
				StatusType st = new StatusType();
				st.setMessages(mts);
				throw new GlobalNavException(st);
			}
		} catch (GlobalNavException gne) {
			throw gne;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.MEMBER_NOT_FOUND.name());
			StringBuilder sb = new StringBuilder();

			sb.append("Exception in finding a member portals");

			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			throw new GlobalNavException(st);
		}
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: END");

	}

	public void updateMemberPortalEligibilityFailDt(Mbr mbr) throws GlobalNavException {

		MbrPrtl savedMbrPrtl = null;
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
		try {
			if (!CollectionUtils.isEmpty(mbr.getMbrPrtls())) {
				for (MbrPrtl mbrPrtl : mbr.getMbrPrtls()) {
					List<MbrPrtl> mbrPrtlsRetrieved = jpaMemberPortalRepository
							.findByMbrPrtlByProvisionMemberIdPortalShortNm(new Integer(mbr.getPrvsnMbrId()).toString(),
									mbrPrtl.getPrtlByPrtlId().getPrtlShrtNm());
					if (!CollectionUtils.isEmpty(mbrPrtlsRetrieved)) {
						for (MbrPrtl mbrprtlRetrieved : mbrPrtlsRetrieved) {
							if (mbrPrtl.getMbrPrtlEligFailDt() != null)
								mbrprtlRetrieved.setMbrPrtlEligFailDt(mbrPrtl.getMbrPrtlEligFailDt());
							else
								mbrprtlRetrieved.setMbrPrtlEligFailDt(null);
							savedMbrPrtl = persist(mbrprtlRetrieved);

						}

					}

					else {
						MessagesType mts = new MessagesType();
						MessageType mt = new MessageType();
						mt.setCode("404");
						mt.setSeverity(Constants.Severity.EXCEPTION.name());
						mt.setName(Constants.StatusMsgNm.MEMBER_NOT_FOUND.name());
						StringBuilder sb = new StringBuilder();

						sb.append("No Portal records found in PDB for search crietria");
						mt.setDescription(sb.toString());
						mts.getMessage().add(mt);
						StatusType st = new StatusType();
						st.setMessages(mts);
						throw new GlobalNavException(st);
					}
				}
			}
		} catch (GlobalNavException gne) {
			throw gne;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.MEMBER_NOT_FOUND.name());
			StringBuilder sb = new StringBuilder();

			sb.append("Exception in finding a member portals");

			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			throw new GlobalNavException(st);
		}
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: END");

	}
}