package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 消贷分期账户信息查询
 * @author liuyanxi
 *
 */
public class X5650BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;

    /** 证件号码 [30,0] */
    private String idNumber;
    /** 外部识别号 [19,0] */
    private String externalIdentificationNo;
    /** 分期类型 */
    private String stageType;
    /** 开始时间 */
    private String beginDate;
    /** 结束时间 */
    private String endDate;
    private int pageNum;

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }

    public String getStageType() {
        return stageType;
    }
    
	public void setStageType(String stageType) {
        this.stageType = stageType;
    }

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

}
