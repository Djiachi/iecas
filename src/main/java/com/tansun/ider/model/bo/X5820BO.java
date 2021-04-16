/**  

* @Title: X5820.java

* @Function

* @Description:  

* @author baiyu

* @date 2019年5月15日  

* @version R04.00 

*/  
package com.tansun.ider.model.bo;

import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.model.ResultGns;

/**  

* @ClassName: X5820  

* @Function:

* @Description:  

* @author baiyu

* @date 2019年5月15日  

* @version R04.00 

*/
public class X5820BO {
    
	/** 是否转卡标志 */
	private String isGetLossFee;
    /** 媒介记录表 */
    private List<ResultGns> coreMediaBasicInfoList;
    /** 活动与构件对应关系表 */
    CoreEventActivityRel coreEventActivityRel;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
    /** 外部识别号 [19,0] Not NULL */
    protected String externalIdentificationNo;
    /** 证件类型 [1,0] */
    protected String idType;
    /** 证件号[30,0] */
    protected String idNumber;
    /** 交易日期 [10,0] Not NULL */
    protected String transDate;
    /** 交易时间 [12,0] Not NULL */
    protected String transTime;
    /** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 全局事件编号 */
    private String globalEventNo;
    /** 全局流水号 [32,0] */
    protected String globalSerialNumbr;
    /** 机构号 [10,0] */
    protected String organNo;
    /** 场景码 [6,0] */
    protected String authSceneCode;
    /** 产品对象 [9,0] */
    protected String productObjectCode;
    /** 业务类型 [9,0] */
    protected String businessType;
    /** 封锁码类别 [1,0] */
    protected String blockCodeType;
    /** 封锁码场景 [10,0] */
    protected Integer blockCodeScene;
    /** 发送状态 S-发送成功 F-发送失败  N-不发送 [1,0] */
    protected String sendStatus;
    /** 重发次数 [10,0] */
    protected Integer resendTime;
    /** 处理结果 [8,0] */
    protected String dealResult;
    /** 交易金额 [11,0] */
    protected Long transAmount;
    /** 交易币种 [3,0] */
    protected String transCurrCde;
    /** 交易描述 [50,0] */
    protected String transDesc;
    /** 入账金额 [11,0] */
    protected Long postingAmount;
    /** 入账币种 [3,0] */
    protected String postingCurrencyCode;
    /** 回应码 [2,0] */
    protected String externalResponseCode;
    /** 拒绝原因码 [3,0] */
    protected String internalResponseCode;
    /** 授权码 [6,0] */
    protected String authCode;
    /** 通知最后发送日期 [10,0] */
    protected String finalSendDate;
    /** 通知最后发送时间 [12,0] */
    protected String finalSendTime;
 // 费用层级，费用免除信息表中使用
    private String feeLevel;
    // 费用层级代码，费用免除信息表中使用
    private String feeLevelCode;
    public X5820BO() {
    
    }
     
    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }
    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public CoreEventActivityRel getCoreEventActivityRel() {
        return coreEventActivityRel;
    }

    public void setCoreEventActivityRel(CoreEventActivityRel coreEventActivityRel) {
        this.coreEventActivityRel = coreEventActivityRel;
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

    /** 取值 <== 全局流水号 [32,0] */
    public String getGlobalSerialNumbr() {
        return globalSerialNumbr;
    }

    /** 赋值 ==> 全局流水号 [32,0] */
    public void setGlobalSerialNumbr(String globalSerialNumbr) {
        this.globalSerialNumbr = globalSerialNumbr == null ? null : globalSerialNumbr.trim();
    }

    /** 取值 <== 机构号 [10,0] */
    public String getOrganNo() {
        return organNo;
    }

    /** 赋值 ==> 机构号 [10,0] */
    public void setOrganNo(String organNo) {
        this.organNo = organNo == null ? null : organNo.trim();
    }

    /** 取值 <== 场景码 [6,0] */
    public String getAuthSceneCode() {
        return authSceneCode;
    }

    /** 赋值 ==> 场景码 [6,0] */
    public void setAuthSceneCode(String authSceneCode) {
        this.authSceneCode = authSceneCode == null ? null : authSceneCode.trim();
    }

    /** 取值 <== 产品对象 [9,0] */
    public String getProductObjectCode() {
        return productObjectCode;
    }

    /** 赋值 ==> 产品对象 [9,0] */
    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode == null ? null : productObjectCode.trim();
    }

    /** 取值 <== 业务类型 [9,0] */
    public String getBusinessType() {
        return businessType;
    }

    /** 赋值 ==> 业务类型 [9,0] */
    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    /** 取值 <== 封锁码类别 [1,0] */
    public String getBlockCodeType() {
        return blockCodeType;
    }

    /** 赋值 ==> 封锁码类别 [1,0] */
    public void setBlockCodeType(String blockCodeType) {
        this.blockCodeType = blockCodeType == null ? null : blockCodeType.trim();
    }

    /** 取值 <== 封锁码场景 [10,0] */
    public Integer getBlockCodeScene() {
        return blockCodeScene;
    }

    /** 赋值 ==> 封锁码场景 [10,0] */
    public void setBlockCodeScene(Integer blockCodeScene) {
        this.blockCodeScene = blockCodeScene;
    }

    /** 取值 <== 发送状态 S-发送成功 F-发送失败  N-不发送 [1,0] */
    public String getSendStatus() {
        return sendStatus;
    }

    /** 赋值 ==> 发送状态 S-发送成功 F-发送失败  N-不发送 [1,0] */
    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus == null ? null : sendStatus.trim();
    }

    /** 取值 <== 重发次数 [10,0] */
    public Integer getResendTime() {
        return resendTime;
    }

    /** 赋值 ==> 重发次数 [10,0] */
    public void setResendTime(Integer resendTime) {
        this.resendTime = resendTime;
    }

    /** 取值 <== 处理结果 [8,0] */
    public String getDealResult() {
        return dealResult;
    }

    /** 赋值 ==> 处理结果 [8,0] */
    public void setDealResult(String dealResult) {
        this.dealResult = dealResult == null ? null : dealResult.trim();
    }

    /** 取值 <== 交易金额 [11,0] */
    public Long getTransAmount() {
        return transAmount;
    }

    /** 赋值 ==> 交易金额 [11,0] */
    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

    /** 取值 <== 交易币种 [3,0] */
    public String getTransCurrCde() {
        return transCurrCde;
    }

    /** 赋值 ==> 交易币种 [3,0] */
    public void setTransCurrCde(String transCurrCde) {
        this.transCurrCde = transCurrCde == null ? null : transCurrCde.trim();
    }

    /** 取值 <== 交易描述 [50,0] */
    public String getTransDesc() {
        return transDesc;
    }

    /** 赋值 ==> 交易描述 [50,0] */
    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc == null ? null : transDesc.trim();
    }

    /** 取值 <== 入账金额 [11,0] */
    public Long getPostingAmount() {
        return postingAmount;
    }

    /** 赋值 ==> 入账金额 [11,0] */
    public void setPostingAmount(Long postingAmount) {
        this.postingAmount = postingAmount;
    }

    /** 取值 <== 入账币种 [3,0] */
    public String getPostingCurrencyCode() {
        return postingCurrencyCode;
    }

    /** 赋值 ==> 入账币种 [3,0] */
    public void setPostingCurrencyCode(String postingCurrencyCode) {
        this.postingCurrencyCode = postingCurrencyCode == null ? null : postingCurrencyCode.trim();
    }

    /** 取值 <== 回应码 [2,0] */
    public String getExternalResponseCode() {
        return externalResponseCode;
    }

    /** 赋值 ==> 回应码 [2,0] */
    public void setExternalResponseCode(String externalResponseCode) {
        this.externalResponseCode = externalResponseCode == null ? null : externalResponseCode.trim();
    }

    /** 取值 <== 拒绝原因码 [3,0] */
    public String getInternalResponseCode() {
        return internalResponseCode;
    }

    /** 赋值 ==> 拒绝原因码 [3,0] */
    public void setInternalResponseCode(String internalResponseCode) {
        this.internalResponseCode = internalResponseCode == null ? null : internalResponseCode.trim();
    }

    /** 取值 <== 授权码 [6,0] */
    public String getAuthCode() {
        return authCode;
    }

    /** 赋值 ==> 授权码 [6,0] */
    public void setAuthCode(String authCode) {
        this.authCode = authCode == null ? null : authCode.trim();
    }

    /** 取值 <== 通知最后发送日期 [10,0] */
    public String getFinalSendDate() {
        return finalSendDate;
    }

    /** 赋值 ==> 通知最后发送日期 [10,0] */
    public void setFinalSendDate(String finalSendDate) {
        this.finalSendDate = finalSendDate == null ? null : finalSendDate.trim();
    }

    /** 取值 <== 通知最后发送时间 [12,0] */
    public String getFinalSendTime() {
        return finalSendTime;
    }

    /** 赋值 ==> 通知最后发送时间 [12,0] */
    public void setFinalSendTime(String finalSendTime) {
        this.finalSendTime = finalSendTime == null ? null : finalSendTime.trim();
    }

	public List<ResultGns> getCoreMediaBasicInfoList() {
		return coreMediaBasicInfoList;
	}

	public void setCoreMediaBasicInfoList(List<ResultGns> coreMediaBasicInfoList) {
		this.coreMediaBasicInfoList = coreMediaBasicInfoList;
	}

	public String getIsGetLossFee() {
		return isGetLossFee;
	}

	public void setIsGetLossFee(String isGetLossFee) {
		this.isGetLossFee = isGetLossFee;
	}

    public String getFeeLevel() {
        return feeLevel;
    }

    public void setFeeLevel(String feeLevel) {
        this.feeLevel = feeLevel;
    }

    public String getFeeLevelCode() {
        return feeLevelCode;
    }

    public void setFeeLevelCode(String feeLevelCode) {
        this.feeLevelCode = feeLevelCode;
    }

  

}
