package com.optum.ogn.mapper;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.optum.ogn.domain.BaseAbstractEntity;
import com.optum.ogn.domain.Mbr;
import com.optum.ogn.domain.MbrPrtl;
import com.optum.ogn.domain.MbrRgstTyp;
import com.optum.ogn.domain.MbrSts;
import com.optum.ogn.domain.Prtl;
import com.optum.ogn.exceptions.GlobalNavException;
import com.optum.ogn.model.AddMemberAttrRequest;
import com.optum.ogn.model.GetMemberAttrResponse;
import com.optum.ogn.model.GetMemberPortalAttrResponse;
import com.optum.ogn.model.MbrPrtlAtr;
import com.optum.ogn.model.UpdateMemberStatus;
import com.optum.ogn.model.UpdateMemberStatus.AttributetobeUpdatedEnum;
import com.optum.ogn.model.UpdateMemberStatus.DatatobeUpdatedEnum;
import com.optum.ogn.model.UpdateMemberStatus.StatusEnum;
import com.optum.ogn.service.MemberPortalService;
import com.optum.ogn.service.MemberService;
import com.optum.ogn.service.PortalService;
import com.optum.ogn.util.ProvisioningUtil;

public class MemberPortalMapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(MemberPortalMapper.class);
	// private static final DateFormat dateFormatter = new
	// SimpleDateFormat("yyyy-MM-dd");
	// private static final Calendar calendar = Calendar.getInstance();

	public MemberPortalService memberPortalService;
	public PortalService portalService;
	public MemberService memberService;

	@Autowired
	public MemberPortalMapper(final MemberPortalService memberPortalService, final PortalService portalService,
			final MemberService memberService) {
		this.memberPortalService = memberPortalService;
		this.portalService = portalService;
		this.memberService = memberService;
	}

	public MemberPortalMapper() {
		super();
	}

	public Mbr mapUpdateMemberStatusRequest2Mbr(UpdateMemberStatus body) throws Exception {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
		Mbr mbr = new Mbr();
		try {
			MbrSts mbrSts = new MbrSts();

			mbr.setHealthSafeId(body.getHealthsafeid());
			if (AttributetobeUpdatedEnum.MemberStatus.toString()
					.equalsIgnoreCase(body.getAttributetobeUpdated().toString())) {
				if (body.getStatus() != null) {
					mbrSts.setMbrStsDesc(body.getStatus().toString());
				} else {
					mbrSts.setMbrStsDesc(StatusEnum.Purged.toString());
				}

				if (body.getDatatobeUpdated().toString().equalsIgnoreCase(DatatobeUpdatedEnum.Member.name())) {
					mbr.setMbrByMbrStsId(mbrSts);
				}
				if (body.getDatatobeUpdated().toString().equalsIgnoreCase(DatatobeUpdatedEnum.Eligibility.name())) {
					mbr = mapMbrPrtls(body.getPortalShortNmList(), mbrSts, mbr);

				}
				if (body.getDatatobeUpdated().toString().equalsIgnoreCase(DatatobeUpdatedEnum.Both.name())) {
					mbr = mapMbrPrtls(body.getPortalShortNmList(), mbrSts, mbr);
					mbr.setMbrByMbrStsId(mbrSts);
				}
			}
			if (AttributetobeUpdatedEnum.MemberPortalEligibilityFailDt.toString()
					.equalsIgnoreCase(body.getAttributetobeUpdated().toString())) {
				mbr.setPrvsnMbrId(new Integer(body.getProvisionMemberId()).intValue());
				if (body.getDatatobeUpdated().toString().equalsIgnoreCase(DatatobeUpdatedEnum.Eligibility.name())) {
					mbr = mapMbrPrtls(body.getPortalShortNmList(), body.getEligibilityFailDate(), mbr);
				}
			}
			LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: END");

		} catch (Exception ex) {
			throw ex;
		}
		return mbr;
	}

	private Mbr mapMbrPrtls(String portalShortNames, MbrSts mbrSts, Mbr mbr) {
		Set<String> portalshortNmList = ProvisioningUtil.convertCommaseparatedStringtoList(portalShortNames);

		if (!CollectionUtils.isEmpty(portalshortNmList)) {
			Set<MbrPrtl> mbrPrtlsList = new HashSet<MbrPrtl>();
			for (String portalShortName : portalshortNmList) {
				MbrPrtl mbrprtl = new MbrPrtl();
				Prtl prtl = new Prtl();
				prtl.setPrtlShrtNm(portalShortName);
				mbrprtl.setPrtlByPrtlId(prtl);
				mbrprtl.setPrtlByMbrStsId(mbrSts);
				mbrPrtlsList.add(mbrprtl);
			}
			mbr.setMbrPrtls(mbrPrtlsList);
		}

		return mbr;
	}

	private Mbr mapMbrPrtls(String portalShortNames, String eligibilityFailDt, Mbr mbr) throws ParseException {
		Set<String> portalshortNmList = ProvisioningUtil.convertCommaseparatedStringtoList(portalShortNames);
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		if (!CollectionUtils.isEmpty(portalshortNmList)) {
			Set<MbrPrtl> mbrPrtlsList = new HashSet<MbrPrtl>();
			for (String portalShortName : portalshortNmList) {
				MbrPrtl mbrprtl = new MbrPrtl();
				if ("null".equalsIgnoreCase(eligibilityFailDt))
					mbrprtl.setMbrPrtlEligFailDt(null);
				else {
					if (eligibilityFailDt != null) {
						Date date = dateFormatter.parse(eligibilityFailDt);
						mbrprtl.setMbrPrtlEligFailDt(new Timestamp(date.getTime()));
					}
				}
				Prtl prtl = new Prtl();
				prtl.setPrtlShrtNm(portalShortName);
				mbrprtl.setPrtlByPrtlId(prtl);
				mbrPrtlsList.add(mbrprtl);
			}
			mbr.setMbrPrtls(mbrPrtlsList);
		}

		return mbr;
	}

	public MbrPrtl mapaddMemberAttrRequest2MbrPrtl(AddMemberAttrRequest body) throws Exception {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
		MbrPrtl mbrPrtl = null;
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		if (body != null) {
			mbrPrtl = new MbrPrtl();
			// Mapper mapper = new DozerBeanMapper();
			Mbr mbr = new Mbr();

			mbr = mapAddMemberAttrRequest2Mbr(body);

			mbrPrtl.setMbrByMbrId(mbr);

			Prtl prtl = new Prtl();
			prtl = mapAddMemberAttrRequest2Prtl(body);
			;

			mbrPrtl.setPrtlByPrtlId(prtl);

			mbrPrtl = new MbrPrtl();

			mbrPrtl.setMbrByMbrId(mbr);
			mbrPrtl.setPrtlByPrtlId(prtl);

			mbrPrtl.setMbrPrtlFstNm(body.getFirstNm());
			mbrPrtl.setMbrPrtlLstNm(body.getLastNm());
			if (isNotBlank(body.getDob()))
				mbrPrtl.setMbrPrtlDob(dateFormatter.parse(body.getDob()));
			mbrPrtl.setMbrPrtlSbscrId(ProvisioningUtil.trimLeadingZeros(body.getSubscriberId()));
			mbrPrtl.setMbrPrtlPolNbr(ProvisioningUtil.trimLeadingZeros(body.getPolicyNbr()));
			mbrPrtl.setMbrPrtlZipCd(body.getZip());
			mbrPrtl.setMbrPrtlSsn(body.getSsn());
			mbrPrtl.setMbrPrtlEncrpSsn(body.getEncrpSsn());
			mbrPrtl.setMbrPrtlSpcMbrId(ProvisioningUtil.trimLeadingZeros(body.getPortalSpcificMemeberId()));
			// New Fields for RX-START
			mbrPrtl.setMbrPrtladdrLine1(body.getAddressLine1());
			mbrPrtl.setMbrPrtladdrLine2(body.getAddressLine2());
			mbrPrtl.setMbrPrtlCity(body.getCity());
			mbrPrtl.setMbrPrtlState(body.getState());
			mbrPrtl.setMbrPrtlGender(body.getGender());
			// New Fields for RX-END
			updateAuditFields(mbrPrtl);

		}
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: END");
		return mbrPrtl;
	}

	private Prtl mapAddMemberAttrRequest2Prtl(AddMemberAttrRequest body) {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
		Prtl prtl = new Prtl();

		prtl.setPrtlShrtNm(body.getPortalShortNm());

		prtl.setPrtlVer(body.getPortalShortVer());

		updateAuditFields(prtl);
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
		return prtl;
	}

	private void updateAuditFields(BaseAbstractEntity domainObj) {
		domainObj.setCreatBy("OGN-PROVISIONING-SERVICE-APP");
		domainObj.setUpdtBy("OGN-PROVISIONING-SERVICE-APP");
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		domainObj.setCreatTs(new Timestamp(now.getTime()));
		domainObj.setUpdtTs(new Timestamp(now.getTime()));
	}

	private void updateRecordTS(BaseAbstractEntity domainObj) {
		domainObj.setUpdtBy("OGN-PROVISIONING-SERVICE-APP");
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		domainObj.setUpdtTs(new Timestamp(now.getTime()));
	}

	/*
	 * populating MemberPortal response from DB results
	 */
	public GetMemberPortalAttrResponse mapMbrPrtl2GetMemberPortalAttrResponse(List<MbrPrtl> list)
			throws ParseException, GlobalNavException {
		List<MbrPrtlAtr> mbrPrtlAtrList = new ArrayList<MbrPrtlAtr>();
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		GetMemberPortalAttrResponse getMemberPortalAttrResponse = new GetMemberPortalAttrResponse();
		if (!CollectionUtils.isEmpty(list)) {

			for (MbrPrtl mbrPrtl : list) {
				MbrPrtlAtr mbrPrtlAtr = new MbrPrtlAtr();
				if (mbrPrtl.getPrtlByPrtlId() != null) {
					mbrPrtlAtr.setPortalShortNm(mbrPrtl.getPrtlByPrtlId().getPrtlShrtNm());
					mbrPrtlAtr.setPortalSpecificMemberIdName(mbrPrtl.getPrtlByPrtlId().getMbrPrtlSpcMbrIdNm());
				}
				mbrPrtlAtr.setPortalFirstNm(mbrPrtl.getMbrPrtlFstNm());
				mbrPrtlAtr.setPortalLastNm(mbrPrtl.getMbrPrtlLstNm());
				if (mbrPrtl.getMbrPrtlDob() != null && isNotBlank(mbrPrtl.getMbrPrtlDob().toString()))
					mbrPrtlAtr.setPortalDob(dateFormatter.format(mbrPrtl.getMbrPrtlDob()));
				mbrPrtlAtr.setPortalSubscriberId(mbrPrtl.getMbrPrtlSbscrId());
				mbrPrtlAtr.setPortalPolicyNbr(mbrPrtl.getMbrPrtlPolNbr());
				mbrPrtlAtr.setPortalZip(mbrPrtl.getMbrPrtlZipCd());
				mbrPrtlAtr.setPortalSsn(mbrPrtl.getMbrPrtlSsn());
				mbrPrtlAtr.setPortalEncrpSsn(mbrPrtl.getMbrPrtlEncrpSsn());
				mbrPrtlAtr.setPortalSpecificMemberId(mbrPrtl.getMbrPrtlSpcMbrId());
				if (mbrPrtl.getMbrPrtlEligFailDt() != null)
					mbrPrtlAtr.setPortalEligFailDt(dateFormatter.format(mbrPrtl.getMbrPrtlEligFailDt()));
				;
				if (mbrPrtl.getMbrByMbrId() != null) {
					GetMemberAttrResponse getMemberAttrResponse = new GetMemberAttrResponse();
					getMemberAttrResponse.setHealthSafeId(mbrPrtl.getMbrByMbrId().getHealthSafeId());
					getMemberAttrResponse
							.setProvisionMemberId(new Integer(mbrPrtl.getMbrByMbrId().getPrvsnMbrId()).toString());
					getMemberAttrResponse.setMdmFirstNm(mbrPrtl.getMbrByMbrId().getMdmFstNm());
					getMemberAttrResponse.setMdmLastNm(mbrPrtl.getMbrByMbrId().getMdmLstNm());
					if (mbrPrtl.getMbrByMbrId().getMdmMbrDob() != null
							&& isNotBlank(mbrPrtl.getMbrByMbrId().getMdmMbrDob().toString()))
						getMemberAttrResponse.setMdmDob(dateFormatter.format(mbrPrtl.getMbrByMbrId().getMdmMbrDob()));

					mbrPrtlAtr.setMember(getMemberAttrResponse);
				}
				mbrPrtlAtrList.add(mbrPrtlAtr);
			}
			getMemberPortalAttrResponse.setMemberPortalAttributeList(mbrPrtlAtrList);
		}
		return getMemberPortalAttrResponse;
	}

	public List<GetMemberAttrResponse> mapMbrPrtl2GetMemberAttrResponse(List<MbrPrtl> list)
			throws ParseException, GlobalNavException {

		List<GetMemberAttrResponse> memAttrResList = new ArrayList<GetMemberAttrResponse>();
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		Map<Integer, Mbr> prvsnMbrIdMbrMap = new HashMap<Integer, Mbr>();
		Map<Integer, Set<MbrPrtl>> prvsnMbrIdMbrPrtlMap = new HashMap<Integer, Set<MbrPrtl>>();

		for (MbrPrtl mbrPrtl : list) {
			if (mbrPrtl.getMbrByMbrId() != null) {
				if (prvsnMbrIdMbrMap.containsKey(mbrPrtl.getMbrByMbrId().getPrvsnMbrId())) {

				} else {
					prvsnMbrIdMbrMap.put(mbrPrtl.getMbrByMbrId().getPrvsnMbrId(), mbrPrtl.getMbrByMbrId());
				}
			}
		}

		for (MbrPrtl mbrPrtl : list) {
			if (mbrPrtl.getMbrByMbrId() != null) {
				if (prvsnMbrIdMbrPrtlMap.containsKey(mbrPrtl.getMbrByMbrId().getPrvsnMbrId())) {
					if (mbrPrtl.getMbrPrtlId() != 0)
						prvsnMbrIdMbrPrtlMap.get(mbrPrtl.getMbrByMbrId().getPrvsnMbrId()).add(mbrPrtl);
				} else {
					Set<MbrPrtl> prtlList = new HashSet<MbrPrtl>();
					prtlList.add(mbrPrtl);
					prvsnMbrIdMbrPrtlMap.put(mbrPrtl.getMbrByMbrId().getPrvsnMbrId(), prtlList);
				}
			}
		}

		for (Integer prvsnMbrId : prvsnMbrIdMbrMap.keySet()) {
			Mbr mbr = prvsnMbrIdMbrMap.get(prvsnMbrId);
			GetMemberAttrResponse memAttrRes = new GetMemberAttrResponse();
			String registrationType = memberPortalService.getMbrRegstType(mbr).getMbrRgstTypDesc();
			memAttrRes.setMbrRegistrationType(registrationType);

			LOGGER.info("Registration Type : Start" + registrationType);

			memAttrRes.setProvisionMemberId(prvsnMbrId.toString());

			if (mbr != null) {
				memAttrRes.setHealthSafeId(mbr.getHealthSafeId());
				memAttrRes.setMdmMemberEid(mbr.getMdmMbrEid());
				memAttrRes.setMdmFirstNm(mbr.getMdmFstNm());
				memAttrRes.setMdmLastNm(mbr.getMdmLstNm());
				if (mbr.getMdmMbrDob() != null && isNotBlank(mbr.getMdmMbrDob().toString()))
					memAttrRes.setMdmDob(dateFormatter.format(mbr.getMdmMbrDob()));
				memAttrRes.setMdmSubscriberId(mbr.getMdmMbrSbscrId());
				memAttrRes.setMdmPolicyNbr(mbr.getMdmMbrPolNbr());
				memAttrRes.setMdmZip(mbr.getMdmMbrZipCd());
				memAttrRes.setMdmSsn(mbr.getMdmMbrSsn());
				memAttrRes.setMdmEncrpSsn(mbr.getMdmMbrEncrpSsn());
				// NEW CAGM Fields-START
				memAttrRes.setMdmCarrierId(mbr.getMdmCarrierId());
				memAttrRes.setMdmAccountId(mbr.getMdmAccountId());
				memAttrRes.setMdmParticipantId(mbr.getMdmParticipantId());
				memAttrRes.setMdmEmployerId(mbr.getMdmEmployerId());
				memAttrRes.setMdmRXclaimsMemberId(mbr.getMdmRxclaimsMemberId());
				// NEW CAGM Fields-END
				if (mbr.getTermAndCondAcptDt() != null && isNotBlank(mbr.getTermAndCondAcptDt().toString()))
					memAttrRes.setMdmTermAndConditions(dateFormatter.format(mbr.getTermAndCondAcptDt()));

				if (mbr.getMdmOvrdInd() != null) {
					if (mbr.getMdmOvrdInd().equalsIgnoreCase("0"))
						memAttrRes.setMdmOverrideInd("false");
					else
						memAttrRes.setMdmOverrideInd("true");
				}
				Set<MbrPrtl> mbrPrtlSet = prvsnMbrIdMbrPrtlMap.get(prvsnMbrId);
				List<MbrPrtlAtr> mbrPrtlAtrList = new ArrayList<MbrPrtlAtr>();
				for (MbrPrtl mbrPrtl : mbrPrtlSet) {
					MbrPrtlAtr mbrPrtlAtr = new MbrPrtlAtr();
					if (mbrPrtl.getPrtlByPrtlId() != null) {
						mbrPrtlAtr.setPortalShortNm(mbrPrtl.getPrtlByPrtlId().getPrtlShrtNm());
						mbrPrtlAtr.setPortalSpecificMemberIdName(mbrPrtl.getPrtlByPrtlId().getMbrPrtlSpcMbrIdNm());
					}
					mbrPrtlAtr.setPortalFirstNm(mbrPrtl.getMbrPrtlFstNm());
					mbrPrtlAtr.setPortalLastNm(mbrPrtl.getMbrPrtlLstNm());
					if (mbrPrtl.getMbrPrtlDob() != null && isNotBlank(mbrPrtl.getMbrPrtlDob().toString()))
						mbrPrtlAtr.setPortalDob(dateFormatter.format(mbrPrtl.getMbrPrtlDob()));
					mbrPrtlAtr.setPortalSubscriberId(mbrPrtl.getMbrPrtlSbscrId());
					mbrPrtlAtr.setPortalPolicyNbr(mbrPrtl.getMbrPrtlPolNbr());
					mbrPrtlAtr.setPortalZip(mbrPrtl.getMbrPrtlZipCd());
					mbrPrtlAtr.setPortalSsn(mbrPrtl.getMbrPrtlSsn());
					mbrPrtlAtr.setPortalEncrpSsn(mbrPrtl.getMbrPrtlEncrpSsn());
					mbrPrtlAtr.setPortalSpecificMemberId(mbrPrtl.getMbrPrtlSpcMbrId());
					// New Fields for RX-START
					mbrPrtlAtr.setPortalAddressLine1(mbrPrtl.getMbrPrtladdrLine1());
					mbrPrtlAtr.setPortalAddressLine2(mbrPrtl.getMbrPrtladdrLine2());
					mbrPrtlAtr.setPortalCity(mbrPrtl.getMbrPrtlCity());
					mbrPrtlAtr.setPortalState(mbrPrtl.getMbrPrtlState());
					mbrPrtlAtr.setPortalGender(mbrPrtl.getMbrPrtlGender());
					// New Fields for RX-END
					if (mbrPrtl.getMbrPrtlEligFailDt() != null)
						mbrPrtlAtr.setPortalEligFailDt(dateFormatter.format(mbrPrtl.getMbrPrtlEligFailDt()));
					;
					mbrPrtlAtrList.add(mbrPrtlAtr);
				}
				memAttrRes.setMemberPortalAttributeList(mbrPrtlAtrList);

			}

			memAttrResList.add(memAttrRes);
		}
		return memAttrResList;
	}

	public Mbr mapAddMemberAttrRequest2Mbr(AddMemberAttrRequest body) throws ParseException {
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Mbr mbr = new Mbr();

		if (isNotBlank(body.getProvisionMemberId())) {
			Integer prvMbrId = Integer.parseInt(body.getProvisionMemberId());
			mbr.setPrvsnMbrId(prvMbrId.intValue());
		} else
			mbr.setPrvsnMbrId(0);
		if (body.getHealthSafeId() == null || body.getHealthSafeId().isEmpty())
			mbr.setHealthSafeId(null);
		else
			mbr.setHealthSafeId(body.getHealthSafeId());
		mbr.setMdmMbrEid(body.getMdmMemberEid());

		mbr.setMdmFstNm(body.getFirstNm());
		mbr.setMdmLstNm(body.getLastNm());

		LOGGER.info("Registration Type value " + body.getRegistrationTypeId());

		if (isNotBlank(body.getDob()))
			mbr.setMdmMbrDob(dateFormatter.parse(body.getDob()));

		mbr.setMdmMbrSbscrId(ProvisioningUtil.trimLeadingZeros(body.getSubscriberId()));
		mbr.setMdmMbrPolNbr(ProvisioningUtil.trimLeadingZeros(body.getPolicyNbr()));

		mbr.setMdmMbrZipCd(body.getZip());
		mbr.setMdmMbrSsn(body.getSsn());
		mbr.setMdmMbrEncrpSsn(body.getEncrpSsn());
		String registrationType=body.getRegistrationTypeId();
		if (isNotBlank(registrationType)) {
			MbrRgstTyp mbrRgstyp=memberPortalService.getMbrRegstTypeId(registrationType);
			mbr.setMbrRgstTypId(mbrRgstyp.getMbrRgstTypId());
		}

		if (isNotBlank(body.getTermAndConditions()))
			mbr.setTermAndCondAcptDt(dateFormatter.parse(body.getTermAndConditions()));
		if (isNotBlank(body.getOverrideInd())) {
			if (body.getOverrideInd().equalsIgnoreCase("true") || body.getOverrideInd().equalsIgnoreCase("1"))
				mbr.setMdmOvrdInd("1");
			if (body.getOverrideInd().equalsIgnoreCase("false") || body.getOverrideInd().equalsIgnoreCase("0"))
				mbr.setMdmOvrdInd("0");
		} else {
			mbr.setMdmOvrdInd("0");
		}
		// CAGM Fields for RX
		mbr.setMdmCarrierId(body.getCarrierId());
		mbr.setMdmAccountId(body.getAccountId());
		mbr.setMdmParticipantId(body.getParticipantId());
		mbr.setMdmEmployerId(body.getEmployerId());
		mbr.setMdmRxclaimsMemberId(ProvisioningUtil.trimLeadingZeros(body.getRxclaimsMemberId()));
		updateAuditFields(mbr);
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: END");
		return mbr;
	}

	public Mbr updateMbrWithNewValues(Mbr alreadyExistedMbr, Mbr member) {

		if (member.getPrvsnMbrId() > 0) {
			alreadyExistedMbr.setPrvsnMbrId(member.getPrvsnMbrId());
		}

		if (isNotBlank(member.getHealthSafeId()))
			alreadyExistedMbr.setHealthSafeId(member.getHealthSafeId());

		if (isNotBlank(member.getMdmMbrEid()))
			alreadyExistedMbr.setMdmMbrEid(member.getMdmMbrEid());

		if (isNotBlank(member.getMdmFstNm()))
			alreadyExistedMbr.setMdmFstNm(member.getMdmFstNm());

		if (isNotBlank(member.getMdmLstNm()))
			alreadyExistedMbr.setMdmLstNm(member.getMdmLstNm());

		if (member.getMdmMbrDob() != null && isNotBlank(member.getMdmMbrDob().toString()))
			alreadyExistedMbr.setMdmMbrDob(member.getMdmMbrDob());

		if (isNotBlank(member.getMdmMbrSbscrId()))
			alreadyExistedMbr.setMdmMbrSbscrId(member.getMdmMbrSbscrId());

		if (isNotBlank(member.getMdmMbrPolNbr()))
			alreadyExistedMbr.setMdmMbrPolNbr(member.getMdmMbrPolNbr());

		if (isNotBlank(member.getMdmMbrZipCd()))
			alreadyExistedMbr.setMdmMbrZipCd(member.getMdmMbrZipCd());
		
		if (member.getMbrRgstTypId() > 0)
			alreadyExistedMbr.setMbrRgstTypId(member.getMbrRgstTypId());
		
		if (isNotBlank(member.getMdmMbrSsn()))
			alreadyExistedMbr.setMdmMbrSsn(member.getMdmMbrSsn());

		if (isNotBlank(member.getMdmMbrEncrpSsn()))
			alreadyExistedMbr.setMdmMbrEncrpSsn(member.getMdmMbrEncrpSsn());

		if (member.getTermAndCondAcptDt() != null && isNotBlank(member.getTermAndCondAcptDt().toString()))
			alreadyExistedMbr.setTermAndCondAcptDt(member.getTermAndCondAcptDt());

		if (isNotBlank(member.getMdmOvrdInd()))
			alreadyExistedMbr.setMdmOvrdInd(member.getMdmOvrdInd());

		if (isNotBlank(member.getMdmCarrierId()))
			alreadyExistedMbr.setMdmCarrierId(member.getMdmCarrierId());

		if (isNotBlank(member.getMdmAccountId()))
			alreadyExistedMbr.setMdmAccountId(member.getMdmAccountId());

		if (isNotBlank(member.getMdmEmployerId()))
			alreadyExistedMbr.setMdmEmployerId(member.getMdmEmployerId());

		if (isNotBlank(member.getMdmParticipantId()))
			alreadyExistedMbr.setMdmParticipantId(member.getMdmParticipantId());

		if (isNotBlank(member.getMdmRxclaimsMemberId()))
			alreadyExistedMbr.setMdmRxclaimsMemberId(member.getMdmRxclaimsMemberId());

		updateRecordTS(alreadyExistedMbr);
		return alreadyExistedMbr;
	}

	public Mbr updateMbrWithMbrStatus(Mbr alreadyExistedMbr, MbrSts persistedmbrSts) {
		if (persistedmbrSts != null) {
			alreadyExistedMbr.setMbrByMbrStsId(persistedmbrSts);
		}
		updateRecordTS(alreadyExistedMbr);
		return alreadyExistedMbr;
	}

	public Mbr updateMbrWithRegistrationType(Mbr alreadyExistedMbr, int newStatusType) {
		alreadyExistedMbr.setMbrRgstTypId(newStatusType);
		updateRecordTS(alreadyExistedMbr);
		return alreadyExistedMbr;
	}

	public MbrPrtl updateMbrPrtlWithMbrStatus(MbrPrtl alreadyExistedMbrPrtl, MbrSts persistedMbrSts) {
		if (persistedMbrSts != null) {
			alreadyExistedMbrPrtl.setPrtlByMbrStsId(persistedMbrSts);
		}
		updateRecordTS(alreadyExistedMbrPrtl);
		return alreadyExistedMbrPrtl;
	}

	public MbrPrtl updateMbrPrtlWithNewValues(MbrPrtl alreadyExistedMbrPrtl, MbrPrtl mbrPrtl) {

		if (mbrPrtl.getMbrPrtlId() > 0) {
			alreadyExistedMbrPrtl.setMbrPrtlId(mbrPrtl.getMbrPrtlId());
		}

		if (isNotBlank(mbrPrtl.getMbrPrtlFstNm()))
			alreadyExistedMbrPrtl.setMbrPrtlFstNm(mbrPrtl.getMbrPrtlFstNm());

		if (isNotBlank(mbrPrtl.getMbrPrtlLstNm()))
			alreadyExistedMbrPrtl.setMbrPrtlLstNm(mbrPrtl.getMbrPrtlLstNm());

		if (mbrPrtl.getMbrPrtlDob() != null && isNotBlank(mbrPrtl.getMbrPrtlDob().toString()))
			alreadyExistedMbrPrtl.setMbrPrtlDob(mbrPrtl.getMbrPrtlDob());

		if (isNotBlank(mbrPrtl.getMbrPrtlSbscrId()))
			alreadyExistedMbrPrtl.setMbrPrtlSbscrId(mbrPrtl.getMbrPrtlSbscrId());

		if (isNotBlank(mbrPrtl.getMbrPrtlPolNbr()))
			alreadyExistedMbrPrtl.setMbrPrtlPolNbr(mbrPrtl.getMbrPrtlPolNbr());

		if (isNotBlank(mbrPrtl.getMbrPrtlZipCd()))
			alreadyExistedMbrPrtl.setMbrPrtlZipCd(mbrPrtl.getMbrPrtlZipCd());

		if (isNotBlank(mbrPrtl.getMbrPrtlSsn()))
			alreadyExistedMbrPrtl.setMbrPrtlSsn(mbrPrtl.getMbrPrtlSsn());

		if (isNotBlank(mbrPrtl.getMbrPrtlEncrpSsn()))
			alreadyExistedMbrPrtl.setMbrPrtlEncrpSsn(mbrPrtl.getMbrPrtlEncrpSsn());

		if (mbrPrtl.getMbrPrtlSpcMbrId() != null && isNotBlank(mbrPrtl.getMbrPrtlSpcMbrId().toString()))
			alreadyExistedMbrPrtl.setMbrPrtlSpcMbrId(mbrPrtl.getMbrPrtlSpcMbrId());

		if (isNotBlank(mbrPrtl.getMbrPrtladdrLine1()))
			alreadyExistedMbrPrtl.setMbrPrtladdrLine1(mbrPrtl.getMbrPrtladdrLine1());

		if (isNotBlank(mbrPrtl.getMbrPrtladdrLine2()))
			alreadyExistedMbrPrtl.setMbrPrtladdrLine2(mbrPrtl.getMbrPrtladdrLine2());

		if (isNotBlank(mbrPrtl.getMbrPrtlCity()))
			alreadyExistedMbrPrtl.setMbrPrtlCity(mbrPrtl.getMbrPrtlCity());

		if (isNotBlank(mbrPrtl.getMbrPrtlState()))
			alreadyExistedMbrPrtl.setMbrPrtlState(mbrPrtl.getMbrPrtlState());

		if (isNotBlank(mbrPrtl.getMbrPrtlGender()))
			alreadyExistedMbrPrtl.setMbrPrtlGender(mbrPrtl.getMbrPrtlGender());

		updateRecordTS(alreadyExistedMbrPrtl);
		return alreadyExistedMbrPrtl;
	}
}
