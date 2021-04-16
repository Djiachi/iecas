package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.service.business.EventCommArea;

public class X5355BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 9115405093299322643L;

    /** 公共区域信息 */
    private EventCommArea eventCommArea;
    /** 构件列表 */
    private List<CoreActivityArtifactRel> artifactList;
    /** 客户代码 [12,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String customerNo;
    /** 业务项目代码 [9,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String businessProgramNo;
    /** 账单日 [10,0] */
    protected Integer billDay;
    /** 下一账单日期 [10,0] */
    protected String nextBillDate;
    /** 当前周期号 [10,0] */
    protected Integer currentCycleNumber;
    /** 约定扣款状态  0-未设置1-已设置 [1,0] */
    protected String directDebitStatus;
    /** 约定扣款方式  0-最小还款1-全额还款 [1,0] */
    protected String directDebitMode;
    /** 约定还款银行号 [7,0] */
    protected String directDebitBankNo;
    /** 约定还款账户号 [25,0] */
    protected String directDebitAccountNo;
    /** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	public EventCommArea getEventCommArea() {
		return eventCommArea;
	}
	public void setEventCommArea(EventCommArea eventCommArea) {
		this.eventCommArea = eventCommArea;
	}
	public List<CoreActivityArtifactRel> getArtifactList() {
		return artifactList;
	}
	public void setArtifactList(List<CoreActivityArtifactRel> artifactList) {
		this.artifactList = artifactList;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getBusinessProgramNo() {
		return businessProgramNo;
	}
	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}
	public Integer getBillDay() {
		return billDay;
	}
	public void setBillDay(Integer billDay) {
		this.billDay = billDay;
	}
	public String getNextBillDate() {
		return nextBillDate;
	}
	public void setNextBillDate(String nextBillDate) {
		this.nextBillDate = nextBillDate;
	}
	public Integer getCurrentCycleNumber() {
		return currentCycleNumber;
	}
	public void setCurrentCycleNumber(Integer currentCycleNumber) {
		this.currentCycleNumber = currentCycleNumber;
	}
	public String getDirectDebitStatus() {
		return directDebitStatus;
	}
	public void setDirectDebitStatus(String directDebitStatus) {
		this.directDebitStatus = directDebitStatus;
	}
	public String getDirectDebitMode() {
		return directDebitMode;
	}
	public void setDirectDebitMode(String directDebitMode) {
		this.directDebitMode = directDebitMode;
	}
	public String getDirectDebitBankNo() {
		return directDebitBankNo;
	}
	public void setDirectDebitBankNo(String directDebitBankNo) {
		this.directDebitBankNo = directDebitBankNo;
	}
	public String getDirectDebitAccountNo() {
		return directDebitAccountNo;
	}
	public void setDirectDebitAccountNo(String directDebitAccountNo) {
		this.directDebitAccountNo = directDebitAccountNo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public CoreEventActivityRel getCoreEventActivityRel() {
		return coreEventActivityRel;
	}
	public void setCoreEventActivityRel(CoreEventActivityRel coreEventActivityRel) {
		this.coreEventActivityRel = coreEventActivityRel;
	}
	@Override
	public String toString() {
		return "X5355BO [eventCommArea=" + eventCommArea + ", artifactList=" + artifactList + ", customerNo="
				+ customerNo + ", businessProgramNo=" + businessProgramNo + ", billDay=" + billDay + ", nextBillDate="
				+ nextBillDate + ", currentCycleNumber=" + currentCycleNumber + ", directDebitStatus="
				+ directDebitStatus + ", directDebitMode=" + directDebitMode + ", directDebitBankNo="
				+ directDebitBankNo + ", directDebitAccountNo=" + directDebitAccountNo + ", coreEventActivityRel="
				+ coreEventActivityRel + "]";
	}
	
}
