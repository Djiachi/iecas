package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 
 * @author xuhaopeng
 *
 */

public class X7250BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = -161123581321345589L;

	private String id;
	/** 卡号 */
	private String externalIdentificationNo;
	/** 证件类型 */
	private String idType;
	/** 身份证号 */
	private String idNumber;
	/** 手机号 */
	private String mobilePhone;
	/** 客户号 [12,0] Not NULL */
	private String customerNo;
	/** 运营模式 [3,0] Not NULL */
	private String operationMode;
	/** 额度树 [9,0] Not NULL */
	private String creditTreeId;
	/** 节点编号 [3,0] Not NULL */
	private String creditNodeNo;
	/** 节点类型 L：层级节点 B：业务节点 [1,0] Not NULL */
	private String creditNodeTyp;
	/** 上层节点编号 [3,0] Not NULL */
	private String upperNodeNo;
	/** 双重检查节点编号 [3,0] */
	private String doubleCheckNodeCode;
	/** 额度种类 C：信用S：抵押O：其他 [1,0] Not NULL */
	private String creditCategory;
	/** 额度性质 R：循环O：一次M：多次 [1,0] Not NULL */
	private String creditProperty;
	/** 额度计算方式 0：子节点授信之和1：子节点授信最大值 [1,0] */
	private String creditComputeMode;
	/** 交易识别代码 [4,0] Not NULL */
	private String transIdentifiNo;
    /** 管控场景代码*/
    private String contrlSceneCode;
	/** 描述 [50,0] Not NULL */
	private String creditDesc;
	/** 额度检查标志 [1,0] */
	protected String creditCheckFlag;
	/** 无效标识 1： 无效 其他： 有效 [1,0] */
	private String invalidFlag;
	/** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
	private String gmtCreate;
	/** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
	private Date timestamp;
	/** 版本号 [10,0] Not NULL */
	private Integer version;
	/** 授信标记 Y：授信， 其他：非授信 */
	private String creditFlag;
	/** 调额标记 Y：调额， 其他：非调额 */
	private String adjustFlag;
	private String requestType;
	/**产品对象*/
    private String productObjectCode;
    
	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getCreditTreeId() {
		return creditTreeId;
	}

	public void setCreditTreeId(String creditTreeId) {
		this.creditTreeId = creditTreeId;
	}

	public String getCreditNodeNo() {
		return creditNodeNo;
	}

	public void setCreditNodeNo(String creditNodeNo) {
		this.creditNodeNo = creditNodeNo;
	}

	public String getCreditNodeTyp() {
		return creditNodeTyp;
	}

	public void setCreditNodeTyp(String creditNodeTyp) {
		this.creditNodeTyp = creditNodeTyp;
	}

	public String getUpperNodeNo() {
		return upperNodeNo;
	}

	public void setUpperNodeNo(String upperNodeNo) {
		this.upperNodeNo = upperNodeNo;
	}

	public String getDoubleCheckNodeCode() {
		return doubleCheckNodeCode;
	}

	public void setDoubleCheckNodeCode(String doubleCheckNodeCode) {
		this.doubleCheckNodeCode = doubleCheckNodeCode;
	}

	public String getCreditCategory() {
		return creditCategory;
	}

	public void setCreditCategory(String creditCategory) {
		this.creditCategory = creditCategory;
	}

	public String getCreditProperty() {
		return creditProperty;
	}

	public void setCreditProperty(String creditProperty) {
		this.creditProperty = creditProperty;
	}

	public String getCreditComputeMode() {
		return creditComputeMode;
	}

	public void setCreditComputeMode(String creditComputeMode) {
		this.creditComputeMode = creditComputeMode;
	}

	public String getTransIdentifiNo() {
		return transIdentifiNo;
	}

	public void setTransIdentifiNo(String transIdentifiNo) {
		this.transIdentifiNo = transIdentifiNo;
	}

	public String getCreditDesc() {
		return creditDesc;
	}

	public void setCreditDesc(String creditDesc) {
		this.creditDesc = creditDesc;
	}

	public String getInvalidFlag() {
		return invalidFlag;
	}

	public String getCreditCheckFlag() {
		return creditCheckFlag;
	}

	public void setCreditCheckFlag(String creditCheckFlag) {
		this.creditCheckFlag = creditCheckFlag;
	}

	public void setInvalidFlag(String invalidFlag) {
		this.invalidFlag = invalidFlag;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCreditFlag() {
		return creditFlag;
	}

	public void setCreditFlag(String creditFlag) {
		this.creditFlag = creditFlag;
	}

	public String getAdjustFlag() {
		return adjustFlag;
	}

	public void setAdjustFlag(String adjustFlag) {
		this.adjustFlag = adjustFlag;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

    public String getContrlSceneCode() {
        return contrlSceneCode;
    }

    public void setContrlSceneCode(String contrlSceneCode) {
        this.contrlSceneCode = contrlSceneCode;
    }

    @Override
    public String toString() {
        return "X7250BO{" +
                "id='" + id + '\'' +
                ", externalIdentificationNo='" + externalIdentificationNo + '\'' +
                ", idType='" + idType + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", customerNo='" + customerNo + '\'' +
                ", operationMode='" + operationMode + '\'' +
                ", creditTreeId='" + creditTreeId + '\'' +
                ", creditNodeNo='" + creditNodeNo + '\'' +
                ", creditNodeTyp='" + creditNodeTyp + '\'' +
                ", upperNodeNo='" + upperNodeNo + '\'' +
                ", doubleCheckNodeCode='" + doubleCheckNodeCode + '\'' +
                ", creditCategory='" + creditCategory + '\'' +
                ", creditProperty='" + creditProperty + '\'' +
                ", creditComputeMode='" + creditComputeMode + '\'' +
                ", transIdentifiNo='" + transIdentifiNo + '\'' +
                ", contrlSceneCode='" + contrlSceneCode + '\'' +
                ", creditDesc='" + creditDesc + '\'' +
                ", creditCheckFlag='" + creditCheckFlag + '\'' +
                ", invalidFlag='" + invalidFlag + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", timestamp=" + timestamp +
                ", version=" + version +
                ", creditFlag='" + creditFlag + '\'' +
                ", adjustFlag='" + adjustFlag + '\'' +
                ", requestType='" + requestType + '\'' +
                '}';
    }
}
