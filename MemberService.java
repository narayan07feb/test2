package com.optum.ogn.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.optum.ogn.constants.Constants;
import com.optum.ogn.constants.Constants.StatusMsgNm;
import com.optum.ogn.dao.impl.DomainObjectManagementService;
import com.optum.ogn.domain.Mbr;
import com.optum.ogn.domain.MbrPrtl;
import com.optum.ogn.domain.MbrRgstTyp;
import com.optum.ogn.domain.MbrSts;
import com.optum.ogn.exceptions.GlobalNavException;
import com.optum.ogn.exceptions.MessageType;
import com.optum.ogn.exceptions.MessagesType;
import com.optum.ogn.exceptions.StatusType;
import com.optum.ogn.exceptions.ValidationFailedException;
import com.optum.ogn.mapper.MemberPortalMapper;
import com.optum.ogn.model.GetMemberAttrResponse;
import com.optum.ogn.model.UpdateMemberStatus.RegistrationTypeEnum;
import com.optum.ogn.persistence.JpaPortalRepository;
import com.optum.ogn.persistence.MbrRegsTypeRepository;
import com.optum.ogn.persistence.MbrStsRepository;
import com.optum.ogn.persistence.MemberPortalRepository;
import com.optum.ogn.persistence.MemberRepository;
import com.optum.ogn.persistence.PortalRepository;

public class MemberService extends DomainObjectManagementService<Mbr> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);
	MemberRepository jpaMemberRepository;
	MbrStsRepository jpaMbrStsRepository;
	MemberPortalRepository jpaMemberPortalRepository;
	MbrRegsTypeRepository jpaMbrRegsTypeRepositry;
	public List<GetMemberAttrResponse> memAttrResList = null;
	private MemberPortalMapper mbrPrtlMapper = new MemberPortalMapper();

	@Autowired
	public MemberService(final MemberRepository jpaMemberRepository, final MbrStsRepository jpaMbrStsRepository,
			final MemberPortalRepository jpaMemberPortalRepository,final MbrRegsTypeRepository jpaMbrRegsTypeRepository) {
		super(jpaMemberRepository);
		this.jpaMemberRepository = jpaMemberRepository;
		this.jpaMbrStsRepository = jpaMbrStsRepository;
		this.jpaMemberPortalRepository = jpaMemberPortalRepository;
		this.jpaMbrRegsTypeRepositry=jpaMbrRegsTypeRepository;	
	}

	public Mbr deleteOrPurge(Mbr member) throws GlobalNavException {
		Mbr savedMember = null, alreadyExistedMbr = null;
		MbrSts persistedMbrSts = null;
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
		try {

			alreadyExistedMbr = jpaMemberRepository.findMbrByUniqueId(member);
			if (alreadyExistedMbr != null) {

				persistedMbrSts = jpaMbrStsRepository
						.findByMbrStsbyMbrStsDesc(member.getMbrByMbrStsId().getMbrStsDesc());
				if (member.getMbrByMbrStsId() != null) {
					alreadyExistedMbr = mbrPrtlMapper.updateMbrWithMbrStatus(alreadyExistedMbr, persistedMbrSts);
					// alreadyExistedMbr.setMbrByMbrStsId(persistedMbrSts);
				}
				savedMember = persist(alreadyExistedMbr);

			} else {
				MessagesType mts = new MessagesType();
				MessageType mt = new MessageType();
				mt.setCode("404");
				mt.setSeverity(Constants.Severity.EXCEPTION.name());
				mt.setName(Constants.StatusMsgNm.MEMBER_NOT_FOUND.name());
				StringBuilder sb = new StringBuilder();

				sb.append("No record found in PDB with");
				if (member != null) {
					sb.append("HealthSafeId:'").append(member.getHealthSafeId()).append("'\n");
				} else {
					sb.append("HealthSafeId:'").append("").append("'\n");
				}
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

			sb.append("Exception in finding a member with ");
			if (member != null) {
				sb.append("HealthSafeId:'").append(member.getHealthSafeId()).append("'\n");
			} else {
				sb.append("HealthSafeId:'").append("").append("'\n");
			}
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			throw new GlobalNavException(st);
		}
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: END");
		return savedMember;

	}

	public Mbr updateRegistrationType(Mbr member, String updatedType) throws GlobalNavException {
		Mbr savedMember = null, alreadyExistedMbr = null;
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
		try {
			alreadyExistedMbr = jpaMemberRepository.findMbrByUniqueId(member);
			if (alreadyExistedMbr != null) {
				int newStatus = 1;
				if (updatedType.equalsIgnoreCase(RegistrationTypeEnum.Headless.name())) {
					newStatus = 2;
				}
			
//				MbrRgstTyp mbrRgstyp=jpaMbrRegsTypeRepositry.findMbrRegistrationTypeId(updatedType);
//				newStatus=mbrRgstyp.getMbrRgstTypId();
//					
				LOGGER.info("New Status Value for Registration Type "+newStatus);
				alreadyExistedMbr = mbrPrtlMapper.updateMbrWithRegistrationType(alreadyExistedMbr, newStatus);
				// alreadyExistedMbr.setMbrByMbrStsId(persistedMbrSts);
				savedMember = persist(alreadyExistedMbr);

			} else {
				MessagesType mts = new MessagesType();
				MessageType mt = new MessageType();
				mt.setCode("404");
				mt.setSeverity(Constants.Severity.EXCEPTION.name());
				mt.setName(Constants.StatusMsgNm.MEMBER_NOT_FOUND.name());
				StringBuilder sb = new StringBuilder();

				sb.append("No record found in PDB with");
				if (member != null) {
					sb.append("HealthSafeId:'").append(member.getHealthSafeId()).append("'\n");
				} else {
					sb.append("HealthSafeId:'").append("").append("'\n");
				}
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

			sb.append("Exception in finding a member with ");
			if (member != null) {
				sb.append("HealthSafeId:'").append(member.getHealthSafeId()).append("'\n");
			} else {
				sb.append("HealthSafeId:'").append("").append("'\n");
			}
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			throw new GlobalNavException(st);
		}
		LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: END");
		return savedMember;

	}

	public Mbr createOrUpdate(Mbr member) throws GlobalNavException {

		Mbr savedMember = null, alreadyExistedMbr = null;

		try {
			LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: START");
			alreadyExistedMbr = jpaMemberRepository.findMbrByUniqueId(member);
			if (alreadyExistedMbr != null) {
				alreadyExistedMbr = mbrPrtlMapper.updateMbrWithNewValues(alreadyExistedMbr, member);
				// member.setPrvsnMbrId(alreadyExistedMbr.getPrvsnMbrId());
				savedMember = persist(alreadyExistedMbr);
			} else {
				savedMember = persist(member);
			}
			LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName() + " :: END");
		} catch (ValidationFailedException ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			StringBuilder sb = null;
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.MBR_VALIDATION_ERROR.name());
			sb = new StringBuilder();

			sb.append("Member table validations are not successful.\n");
			if (member != null) {
				sb.append("provision member Id:'").append(member.getPrvsnMbrId()).append("'\n");
				sb.append("HealthSafeId:'").append(member.getHealthSafeId()).append("'\n");
			} else {
				sb.append("provision member Id:'").append("").append("'\n");
				sb.append("HealthSafeId:'").append("").append("'\n");
			}
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(mt.getDescription(), ex);

			throw new GlobalNavException(st);
		} catch (GlobalNavException ex) {
			throw ex;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			StringBuilder sb = null;

			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.EXCEPTION_ENCOUNTERED.name());
			sb = new StringBuilder();
			sb.append("Exception in creating or updating member data.\n");
			if (member != null) {
				sb.append("provision member Id:'").append(member.getPrvsnMbrId()).append("'\n");
				sb.append("HealthSafeId:'").append(member.getHealthSafeId()).append("'\n");
			} else {
				sb.append("provision member Id:'").append("").append("'\n");
				sb.append("HealthSafeId:'").append("").append("'\n");
			}
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(mt.getDescription(), ex);
			throw new GlobalNavException(st);
		}
		return savedMember;
	}

	public StatusMsgNm updateMbr(Mbr mbrByMbrId) {
		return null;
	}

	public Mbr getMbrByHealthSafeId(String healthSafeId) throws GlobalNavException {

		Mbr alreadyExistedMbr = null;
		try {
			alreadyExistedMbr = jpaMemberRepository.findMbrByHealthSafeId(healthSafeId);
			if (alreadyExistedMbr != null) {
				return alreadyExistedMbr;
			} else {
				return null;
			}
		} catch (ValidationFailedException ex) {

			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.MBR_VALIDATION_ERROR.name());
			StringBuilder sb = new StringBuilder();
			sb.append("Member table validations are not successful.\n");
			sb.append("HealthSafeId:'").append(healthSafeId).append("'\n");
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(mt.getDescription(), ex);
			throw new GlobalNavException(st);
		} catch (GlobalNavException ex) {
			throw ex;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.MEMBER_NOT_FOUND.name());
			StringBuilder sb = new StringBuilder();

			sb.append("Exception in finding a member with HealthSafeId\n");
			sb.append("HealthSafeId:'").append(healthSafeId).append("'\n");
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(mt.getDescription(), ex);
			throw new GlobalNavException(st);
		}
	}

	public Mbr getMbrByHealthSafeIdProvisionMemberId(String healthSafeId, String provisionMemberId)
			throws GlobalNavException {

		Mbr alreadyExistedMbr = null;
		try {
			alreadyExistedMbr = jpaMemberRepository.findMbrByByHealthSafeIdProvisionMemberId(healthSafeId,
					provisionMemberId);
			if (alreadyExistedMbr != null) {
				return alreadyExistedMbr;
			} else {
				return null;
			}
		} catch (ValidationFailedException ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.MBR_VALIDATION_ERROR.name());
			StringBuilder sb = new StringBuilder();

			sb.append("Member table validations are not successful.\n");
			sb.append("Provision Member Id:'").append(provisionMemberId).append("'\n");
			sb.append("HealthSafeId:'").append(healthSafeId).append("'\n");
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(mt.getDescription(), ex);
			throw new GlobalNavException(st);
		} catch (GlobalNavException ex) {
			throw ex;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.MEMBER_NOT_FOUND.name());
			StringBuilder sb = new StringBuilder();

			sb.append("Exception occured while finding a member with Provision member Id and HealthSafeId\n");
			sb.append("Provision Member Id:'").append(provisionMemberId).append("'\n");
			sb.append("HealthSafeId:'").append(healthSafeId).append("'\n");
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(mt.getDescription(), ex);
			throw new GlobalNavException(st);
		}
	}

	public Mbr getMbrByUniqueId(Mbr mbr) throws GlobalNavException {

		Mbr alreadyExistedMbr = null;
		try {
			alreadyExistedMbr = jpaMemberRepository.findMbrByUniqueId(mbr);
			return alreadyExistedMbr;
		} catch (ValidationFailedException ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.MBR_VALIDATION_ERROR.name());
			StringBuilder sb = new StringBuilder();

			sb.append("Member table validations are not successful.\n");
			if (mbr != null) {
				sb.append("provision member Id:'").append(mbr.getPrvsnMbrId()).append("'\n");
				sb.append("HealthSafeId:'").append(mbr.getHealthSafeId()).append("'\n");
			} else {
				sb.append("provision member Id:'").append("").append("'\n");
				sb.append("HealthSafeId:'").append("").append("'\n");
			}
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(mt.getDescription(), ex);
			throw new GlobalNavException(st);
		} catch (GlobalNavException ex) {
			throw ex;
		} catch (Exception ex) {
			MessagesType mts = new MessagesType();
			MessageType mt = new MessageType();
			mt.setCode("500");
			mt.setSeverity(Constants.Severity.EXCEPTION.name());
			mt.setName(Constants.StatusMsgNm.MEMBER_NOT_FOUND.name());

			StringBuilder sb = new StringBuilder();

			sb.append("Exception in finding a member with provision MemberId and HealthSafeId\n");
			if (mbr != null) {
				sb.append("provision member Id:'").append(mbr.getPrvsnMbrId()).append("'\n");
				sb.append("HealthSafeId:'").append(mbr.getHealthSafeId()).append("'\n");
			} else {
				sb.append("provision member Id:'").append("").append("'\n");
				sb.append("HealthSafeId:'").append("").append("'\n");
			}
			mt.setDescription(sb.toString());
			mts.getMessage().add(mt);
			StatusType st = new StatusType();
			st.setMessages(mts);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(mt.getDescription(), ex);
			throw new GlobalNavException(st);
		}

	}

}