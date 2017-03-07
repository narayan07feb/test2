package com.optum.ogn.domain;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * The persistent class for the mbr database table.
 */
@Entity
@Table(name="mbr")
@NamedQuery(name="Mbr.findAll", query="SELECT m FROM Mbr m")
public class Mbr extends BaseAbstractEntity implements Serializable,DomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PRVSN_MBR_ID", unique=true, nullable=false)
	private int prvsnMbrId;

	@Column(name="CREAT_BY", nullable=false, length=40)
	private String creatBy;

	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREAT_TS", nullable=false)
	private Timestamp creatTs;

	@Column(name="EMAIL_ADDR", length=255)
	private String emailAddr;

	@Column(name="MDM_FST_NM", nullable=false, length=40)
	private String mdmFstNm;

	@Column(name="MDM_LST_NM", nullable=false, length=40)
	private String mdmLstNm;

	@Temporal(TemporalType.DATE)
	@Column(name="MDM_MBR_DOB")
	private Date mdmMbrDob;

	@Column(name="MDM_MBR_EID", length=20)
	private String mdmMbrEid;

	@Column(name="MDM_MBR_SBSCR_ID", length=20)
	private String mdmMbrSbscrId;

	
	@Column(name="MDM_MBR_SSN", length=512)
	private String mdmMbrSsn;
	
	@Column(name="MDM_MBR_ENCRP_SSN", length=512)
	private String mdmMbrEncrpSsn;
	
	@Column(name="MDM_MBR_POL_NBR", length=30)
	private String mdmMbrPolNbr;

	@Column(name="MDM_MBR_ZIP_CD", length=10)
	private String mdmMbrZipCd;
	
	@Column(name="MDM_OVRD_IND", nullable=false, length=1)
	private String mdmOvrdInd;


	@Column(name="HLTHSF_ID", length=36)
	private String healthSafeId;

	@Column(name="PREF_NM", length=40)
	private String prefNm;

	@Temporal(TemporalType.DATE)
	@Column(name="TERM_AND_COND_ACPT_DT")
	private Date termAndCondAcptDt;

	@Column(name="UPDT_BY", nullable=false, length=40)
	private String updtBy;

	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDT_TS", nullable=false)
	private Timestamp updtTs;

	//bi-directional many-to-one association to MbrPrtl
	@OneToMany(mappedBy="mbrByMbrId")
	private Set<MbrPrtl> mbrPrtls;

	//CAGM fields for RX-START
	@Column(name="MDM_MBR_CARR_ID", length=25)
	private String mdmCarrierId;
	
	@Column(name="MDM_MBR_EMPLR_ID", length=25)
	private String mdmEmployerId;
	
	@Column(name="MDM_MBR_ACCT_ID", length=25)
	private String mdmAccountId;
	
	@Column(name="MDM_MBR_PRTCP_ID", length=25)
	private String mdmParticipantId;
	
	@Column(name="MDM_MBR_CLM_ID", length=25)
	private String mdmRxclaimsMemberId;
	//CAGM fields for RX-END
	
	@ManyToOne
	@JoinColumn(name="MBR_STS_ID", nullable=true)
	private MbrSts mbrByMbrStsId;

	//this code will work in case of new column added
//	@Column(name="new_column_name", length=25)
//	private String newColumnName;
//
//	public void setNewColumnName(String newColumnName) {
//		this.newColumnName = newColumnName;
//	}
//
//	public String getNewColumnName() {
//		return newColumnName;
//	}

	public Mbr() {
	}

	
	
	public MbrSts getMbrByMbrStsId() {
		return mbrByMbrStsId;
	}



	public void setMbrByMbrStsId(MbrSts mbrByMbrStsId) {
		this.mbrByMbrStsId = mbrByMbrStsId;
	}



	public int getPrvsnMbrId() {
		return this.prvsnMbrId;
	}

	public void setPrvsnMbrId(int prvsnMbrId) {
		this.prvsnMbrId = prvsnMbrId;
	}

	public String getCreatBy() {
		return this.creatBy;
	}

	public void setCreatBy(String creatBy) {
		this.creatBy = creatBy;
	}
	public String getMdmMbrPolNbr() {
		return this.mdmMbrPolNbr;
	}

	public void setMdmMbrPolNbr(String mdmMbrPolNbr) {
		this.mdmMbrPolNbr = mdmMbrPolNbr;
	}
	public Timestamp getCreatTs() {
		return this.creatTs;
	}

	public void setCreatTs(Timestamp creatTs) {
		this.creatTs = creatTs;
	}

	public String getEmailAddr() {
		return this.emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public String getMdmFstNm() {
		return this.mdmFstNm;
	}

	public void setMdmFstNm(String mdmFstNm) {
		this.mdmFstNm = mdmFstNm;
	}
	public String getMdmOvrdInd() {
		return this.mdmOvrdInd;
	}

	public void setMdmOvrdInd(String mdmOvrdInd) {
		this.mdmOvrdInd = mdmOvrdInd;
	}

	public String getMdmLstNm() {
		return this.mdmLstNm;
	}

	public void setMdmLstNm(String mdmLstNm) {
		this.mdmLstNm = mdmLstNm;
	}

	public Date getMdmMbrDob() {
		return this.mdmMbrDob;
	}

	public void setMdmMbrDob(Date mdmMbrDob) {
		this.mdmMbrDob = mdmMbrDob;
	}

	public String getMdmMbrEid() {
		return this.mdmMbrEid;
	}

	public void setMdmMbrEid(String mdmMbrEid) {
		this.mdmMbrEid = mdmMbrEid;
	}

	public String getMdmMbrSbscrId() {
		return this.mdmMbrSbscrId;
	}

	public void setMdmMbrSbscrId(String mdmMbrSbscrId) {
		this.mdmMbrSbscrId = mdmMbrSbscrId;
	}

	public String getMdmMbrSsn() {
		return this.mdmMbrSsn;
	}

	public void setMdmMbrSsn(String mdmMbrSsn) {
		this.mdmMbrSsn = mdmMbrSsn;
	}
	
	public String getMdmMbrEncrpSsn() {
		return this.mdmMbrEncrpSsn;
	}

	public void setMdmMbrEncrpSsn(String mdmMbrEncrpSsn) {
		this.mdmMbrEncrpSsn = mdmMbrEncrpSsn;
	}

	public String getMdmMbrZipCd() {
		return this.mdmMbrZipCd;
	}

	public void setMdmMbrZipCd(String mdmMbrZipCd) {
		this.mdmMbrZipCd = mdmMbrZipCd;
	}

	public String getHealthSafeId() {
		return this.healthSafeId;
	}

	public void setHealthSafeId(String healthSafeId) {
		this.healthSafeId = healthSafeId;
	}

	public String getPrefNm() {
		return this.prefNm;
	}

	public void setPrefNm(String prefNm) {
		this.prefNm = prefNm;
	}

	public Date getTermAndCondAcptDt() {
		return this.termAndCondAcptDt;
	}

	public void setTermAndCondAcptDt(Date termAndCondAcptDt) {
		this.termAndCondAcptDt = termAndCondAcptDt;
	}

	public String getUpdtBy() {
		return this.updtBy;
	}

	public void setUpdtBy(String updtBy) {
		this.updtBy = updtBy;
	}

	public Timestamp getUpdtTs() {
		return this.updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	
	

	public String getMdmCarrierId() {
		return mdmCarrierId;
	}

	public void setMdmCarrierId(String mdmCarrierId) {
		this.mdmCarrierId = mdmCarrierId;
	}

	public String getMdmEmployerId() {
		return mdmEmployerId;
	}

	public void setMdmEmployerId(String mdmEmployerId) {
		this.mdmEmployerId = mdmEmployerId;
	}

	public String getMdmAccountId() {
		return mdmAccountId;
	}

	public void setMdmAccountId(String mdmAccountId) {
		this.mdmAccountId = mdmAccountId;
	}

	public String getMdmParticipantId() {
		return mdmParticipantId;
	}

	public void setMdmParticipantId(String mdmParticipantId) {
		this.mdmParticipantId = mdmParticipantId;
	}

	public String getMdmRxclaimsMemberId() {
		return mdmRxclaimsMemberId;
	}

	public void setMdmRxclaimsMemberId(String mdmRxclaimsMemberId) {
		this.mdmRxclaimsMemberId = mdmRxclaimsMemberId;
	}

	public Set<MbrPrtl> getMbrPrtls() {
		return this.mbrPrtls;
	}

	public void setMbrPrtls(Set<MbrPrtl> mbrPrtls) {
		this.mbrPrtls = mbrPrtls;
	}

	public MbrPrtl addMbrPrtl(MbrPrtl mbrPrtl) {
		getMbrPrtls().add(mbrPrtl);
		mbrPrtl.setMbrByMbrId(this);

		return mbrPrtl;
	}

	public MbrPrtl removeMbrPrtl(MbrPrtl mbrPrtl) {
		getMbrPrtls().remove(mbrPrtl);
		mbrPrtl.setMbrByMbrId(null);

		return mbrPrtl;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mbr mbr = (Mbr) o;

        if (prvsnMbrId != mbr.prvsnMbrId) return false;
        if (mdmFstNm != null ? !mdmFstNm.equals(mbr.mdmFstNm) : mbr.mdmFstNm != null) return false;
        if (mdmLstNm != null ? !mdmLstNm.equals(mbr.mdmLstNm) : mbr.mdmLstNm != null) return false;
        if (mdmMbrDob != null ? !mdmMbrDob.equals(mbr.mdmMbrDob) : mbr.mdmMbrDob != null) return false;
        if (mdmMbrSbscrId != null ? !mdmMbrSbscrId.equals(mbr.mdmMbrSbscrId) : mbr.mdmMbrSbscrId != null) return false;
        if (mdmMbrPolNbr != null ? !mdmMbrPolNbr.equals(mbr.mdmMbrPolNbr) : mbr.mdmMbrPolNbr != null) return false;
        if (mdmMbrZipCd != null ? !mdmMbrZipCd.equals(mbr.mdmMbrZipCd) : mbr.mdmMbrZipCd != null) return false;
        if (mdmMbrSsn != null ? !mdmMbrSsn.equals(mbr.mdmMbrSsn) : mbr.mdmMbrSsn != null) return false;
        if (mdmMbrEid != null ? !mdmMbrEid.equals(mbr.mdmMbrEid) : mbr.mdmMbrEid != null) return false;
        if (termAndCondAcptDt != null ? !termAndCondAcptDt.equals(mbr.termAndCondAcptDt) : mbr.termAndCondAcptDt != null) return false;
        if (mdmOvrdInd != null ? !mdmOvrdInd.equals(mbr.mdmOvrdInd) : mbr.mdmOvrdInd != null) return false;
        if (healthSafeId != null ? !healthSafeId.equals(mbr.healthSafeId) : mbr.healthSafeId != null) return false;
        if (creatBy != null ? !creatBy.equals(mbr.creatBy) : mbr.creatBy != null) return false;
        if (creatTs != null ? !creatTs.equals(mbr.creatTs) : mbr.creatTs != null) return false;
        if (updtBy != null ? !updtBy.equals(mbr.updtBy) : mbr.updtBy != null) return false;
        if (updtTs != null ? !updtTs.equals(mbr.updtTs) : mbr.updtTs != null) return false;
        if (mdmCarrierId != null ? !mdmCarrierId.equals(mbr.mdmCarrierId) : mbr.mdmCarrierId != null) return false;
        if (mdmEmployerId != null ? !mdmEmployerId.equals(mbr.mdmEmployerId) : mbr.mdmEmployerId != null) return false;
        if (mdmAccountId != null ? !mdmAccountId.equals(mbr.mdmAccountId) : mbr.mdmAccountId != null) return false;
        if (mdmParticipantId != null ? !mdmParticipantId.equals(mbr.mdmParticipantId) : mbr.mdmParticipantId != null) return false;
        if (mdmRxclaimsMemberId != null ? !mdmRxclaimsMemberId.equals(mbr.mdmRxclaimsMemberId) : mbr.mdmRxclaimsMemberId != null) return false;

        return true;
    }
	
    @Override
    public int hashCode() {
        int result = prvsnMbrId;
        result = 31 * result + (healthSafeId != null ? healthSafeId.hashCode() : 0);
        result = 31 * result + (mdmFstNm != null ? mdmFstNm.hashCode() : 0);
        result = 31 * result + (mdmLstNm != null ? mdmLstNm.hashCode() : 0);
        result = 31 * result + (mdmMbrDob != null ? mdmMbrDob.hashCode() : 0);
        result = 31 * result + (mdmMbrSbscrId != null ? mdmMbrSbscrId.hashCode() : 0);
        result = 31 * result + (mdmMbrPolNbr != null ? mdmMbrPolNbr.hashCode() : 0);
        result = 31 * result + (mdmMbrZipCd != null ? mdmMbrZipCd.hashCode() : 0);
        result = 31 * result + (mdmMbrSsn != null ? mdmMbrSsn.hashCode() : 0);
        result = 31 * result + (termAndCondAcptDt != null ? termAndCondAcptDt.hashCode() : 0);
        result = 31 * result + (mdmMbrEid != null ? mdmMbrEid.hashCode() : 0);
        result = 31 * result + (mdmOvrdInd != null ? mdmOvrdInd.hashCode() : 0);
        result = 31 * result + (creatBy != null ? creatBy.hashCode() : 0);
        result = 31 * result + (creatTs != null ? creatTs.hashCode() : 0);
        result = 31 * result + (updtBy != null ? updtBy.hashCode() : 0);
        result = 31 * result + (updtTs != null ? updtTs.hashCode() : 0);
        return result;
    }

    public boolean checkIsValid() {
        return  isNotBlank(getMdmFstNm()) && isNotBlank(getMdmLstNm());
    }

}
