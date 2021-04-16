package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * <p> Title: X4010BO </p>
 * <p> Description: 预算单位附件信息维护</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月24日
 */
public class X4010BO extends BeanVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
    /** 全局事件编号 */
    private String globalEventNo;
    /** 单位公务卡总授信额度 */
    private BigDecimal orgAllQuota;
    /** 个人公务卡最大授信额度 */
    private BigDecimal personMaxQuota;
    /** 证件号 */
    private String idNumber;
    /** 账单日*/
    private String billDay;
    /** 构件列表 */
    private List<CoreActivityArtifactRel> artifactList;

	public List<CoreActivityArtifactRel> getArtifactList() {
		return artifactList;
	}

	public void setArtifactList(List<CoreActivityArtifactRel> artifactList) {
		this.artifactList = artifactList;
	}

	public String getBillDay() {
		return billDay;
	}

	public void setBillDay(String billDay) {
		this.billDay = billDay;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public BigDecimal getOrgAllQuota() {
        return orgAllQuota;
    }

    public void setOrgAllQuota(BigDecimal orgAllQuota) {
        this.orgAllQuota = orgAllQuota;
    }

    public BigDecimal getPersonMaxQuota() {
        return personMaxQuota;
    }

    public void setPersonMaxQuota(BigDecimal personMaxQuota) {
        this.personMaxQuota = personMaxQuota;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getGlobalEventNo() {
        return globalEventNo;
    }

    public void setGlobalEventNo(String globalEventNo) {
        this.globalEventNo = globalEventNo;
    }

}
