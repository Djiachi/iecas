package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.service.business.EventCommArea;

/**
 * 收费项目实例新增
 * 
 * @ClassName X0135BO
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2018年9月14日 上午10:30:29
 * @version 1.0.0
 */
public class X0135BO extends BeanVO implements Serializable {

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = -1037755977817677704L;
	private EventCommArea eventCommArea;
	/* 创建一个全局流水号 */
	private String globalEventNo;
	/** 运营模式 [3,0] Not NULL */
	private String operationMode;
	/** 收费项目编号 [8,0] Not NULL */
	private String feeItemNo;
	private String feeType;
	/** 实例代码1 [14,0] */
	private String instanCode1;
	/** 实例代码2 [14,0] */
	private String instanCode2;
	/** 实例代码3 [14,0] */
	private String instanCode3;
	/** 实例代码4 [14,0] */
	private String instanCode4;
	/** 实例代码5 [14,0] */
	private String instanCode5;
	/** 计费方式 F：固定金额 M：费用矩阵 [1,0] */
//	private String assessmentMethod;
	/** 基本费用 [18,0] */
	private BigDecimal baseFee;
	/** 附卡基准年费 [18,0] */
	private BigDecimal supplementBaseFee;
	/** 最低收费金额 [18,0] */
	private BigDecimal minFeeAmt;
	/** 最高收费金额 [18,0] */
	private BigDecimal maxFeeAmt;
	/** 矩阵应用方式 S：全额套档 P：超额累进 [1,0] */
	private String matrixAppMode;
	/** 费用标识 D：数值/金额 P：百分比 [1,0] */
	private String feeFlag;
	/** 笔数1 设置具体笔数，不使用时默认为0 [10,0] */
	private Integer transCount1;
	/** 匹配关系1 AND/OR 不使用时，默认为空格 [3,0] */
	private String matchRelation1;
	/** 金额1 设置具体金额，不使用时默认0 [18,0] */
	private BigDecimal transAmount1;
	/** 费率1 金额/百分比 根据费用标识指定 [14,9] */
	private BigDecimal feeRate1;
	/** 笔数2 [10,0] */
	private Integer transCount2;
	/** 匹配关系2 [3,0] */
	private String matchRelation2;
	/** 金额2 [18,0] */
	private BigDecimal transAmount2;
	/** 费率2 [14,9] */
	private BigDecimal feeRate2;
	/** 笔数3 [10,0] */
	private Integer transCount3;
	/** 匹配关系3 [3,0] */
	private String matchRelation3;
	/** 金额3 [18,0] */
	private BigDecimal transAmount3;
	/** 费率3 [14,9] */
	private BigDecimal feeRate3;
	/** 笔数4 [10,0] */
	private Integer transCount4;
	/** 匹配关系4 [3,0] */
	private String matchRelation4;
	/** 金额4 [18,0] */
	private BigDecimal transAmount4;
	/** 费率4 [14,9] */
	private BigDecimal feeRate4;
	/** 笔数5 [10,0] */
	private Integer transCount5;
	/** 匹配关系5 [3,0] */
	private String matchRelation5;
	/** 金额5 [18,0] */
	private BigDecimal transAmount5;
	/** 费率5 [14,9] */
	private BigDecimal feeRate5;
	/** 0:一次性收取，1:分期收取 [1,0] */
	private String feeCollectType;

	private String feeItemInstanId;
    /** 费用矩阵应用维度 [10,0] */
	private Integer feeMatrixApplicationDimension;
	
	private String redisKey;
	
	private String instanDesc1;
	private String instanDesc2;
	private String instanDesc3;
	private String instanDesc4;
	private String instanDesc5;
	
	/** 费用描述 [50,0] Not NULL */
	private String feeDesc;
	private String id;
	 /** 免除周期 [1,0] */
    protected String waiveCycle;
    /** 免除次数 [10,0] */
    protected Integer waiveCount;
    
	private String baseFee1;
	
	private String instanCode;
	private String itemType;
	
	public String getWaiveCycle() {
		return waiveCycle;
	}

	public void setWaiveCycle(String waiveCycle) {
		this.waiveCycle = waiveCycle;
	}

	public Integer getWaiveCount() {
		return waiveCount;
	}

	public void setWaiveCount(Integer waiveCount) {
		this.waiveCount = waiveCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRedisKey() {
		return redisKey;
	}

	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}
	
	
	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getFeeItemNo() {
		return feeItemNo;
	}

	public void setFeeItemNo(String feeItemNo) {
		this.feeItemNo = feeItemNo;
	}

	public String getInstanCode1() {
		return instanCode1;
	}

	public void setInstanCode1(String instanCode1) {
		this.instanCode1 = instanCode1;
	}

	public String getInstanCode2() {
		return instanCode2;
	}

	public void setInstanCode2(String instanCode2) {
		this.instanCode2 = instanCode2;
	}

	public String getInstanCode3() {
		return instanCode3;
	}

	public void setInstanCode3(String instanCode3) {
		this.instanCode3 = instanCode3;
	}

	public String getInstanCode4() {
		return instanCode4;
	}

	public void setInstanCode4(String instanCode4) {
		this.instanCode4 = instanCode4;
	}

	public String getInstanCode5() {
		return instanCode5;
	}

	public void setInstanCode5(String instanCode5) {
		this.instanCode5 = instanCode5;
	}


	public BigDecimal getBaseFee() {
		return baseFee;
	}

	public void setBaseFee(BigDecimal baseFee) {
		this.baseFee = baseFee;
	}

	public BigDecimal getSupplementBaseFee() {
		return supplementBaseFee;
	}

	public void setSupplementBaseFee(BigDecimal supplementBaseFee) {
		this.supplementBaseFee = supplementBaseFee;
	}

	public BigDecimal getMinFeeAmt() {
		return minFeeAmt;
	}

	public void setMinFeeAmt(BigDecimal minFeeAmt) {
		this.minFeeAmt = minFeeAmt;
	}

	public BigDecimal getMaxFeeAmt() {
		return maxFeeAmt;
	}

	public void setMaxFeeAmt(BigDecimal maxFeeAmt) {
		this.maxFeeAmt = maxFeeAmt;
	}

	public String getMatrixAppMode() {
		return matrixAppMode;
	}

	public void setMatrixAppMode(String matrixAppMode) {
		this.matrixAppMode = matrixAppMode;
	}

	public String getFeeFlag() {
		return feeFlag;
	}

	public void setFeeFlag(String feeFlag) {
		this.feeFlag = feeFlag;
	}

	public Integer getTransCount1() {
		return transCount1;
	}

	public void setTransCount1(Integer transCount1) {
		this.transCount1 = transCount1;
	}

	public String getMatchRelation1() {
		return matchRelation1;
	}

	public void setMatchRelation1(String matchRelation1) {
		this.matchRelation1 = matchRelation1;
	}

	public BigDecimal getTransAmount1() {
		return transAmount1;
	}

	public void setTransAmount1(BigDecimal transAmount1) {
		this.transAmount1 = transAmount1;
	}

	public BigDecimal getFeeRate1() {
		return feeRate1;
	}

	public void setFeeRate1(BigDecimal feeRate1) {
		this.feeRate1 = feeRate1;
	}

	public Integer getTransCount2() {
		return transCount2;
	}

	public void setTransCount2(Integer transCount2) {
		this.transCount2 = transCount2;
	}

	public String getMatchRelation2() {
		return matchRelation2;
	}

	public void setMatchRelation2(String matchRelation2) {
		this.matchRelation2 = matchRelation2;
	}

	public BigDecimal getTransAmount2() {
		return transAmount2;
	}

	public void setTransAmount2(BigDecimal transAmount2) {
		this.transAmount2 = transAmount2;
	}

	public BigDecimal getFeeRate2() {
		return feeRate2;
	}

	public void setFeeRate2(BigDecimal feeRate2) {
		this.feeRate2 = feeRate2;
	}

	public Integer getTransCount3() {
		return transCount3;
	}

	public void setTransCount3(Integer transCount3) {
		this.transCount3 = transCount3;
	}

	public String getMatchRelation3() {
		return matchRelation3;
	}

	public void setMatchRelation3(String matchRelation3) {
		this.matchRelation3 = matchRelation3;
	}

	public BigDecimal getTransAmount3() {
		return transAmount3;
	}

	public void setTransAmount3(BigDecimal transAmount3) {
		this.transAmount3 = transAmount3;
	}

	public BigDecimal getFeeRate3() {
		return feeRate3;
	}

	public void setFeeRate3(BigDecimal feeRate3) {
		this.feeRate3 = feeRate3;
	}

	public Integer getTransCount4() {
		return transCount4;
	}

	public void setTransCount4(Integer transCount4) {
		this.transCount4 = transCount4;
	}

	public String getMatchRelation4() {
		return matchRelation4;
	}

	public void setMatchRelation4(String matchRelation4) {
		this.matchRelation4 = matchRelation4;
	}

	public BigDecimal getTransAmount4() {
		return transAmount4;
	}

	public void setTransAmount4(BigDecimal transAmount4) {
		this.transAmount4 = transAmount4;
	}

	public BigDecimal getFeeRate4() {
		return feeRate4;
	}

	public void setFeeRate4(BigDecimal feeRate4) {
		this.feeRate4 = feeRate4;
	}

	public Integer getTransCount5() {
		return transCount5;
	}

	public void setTransCount5(Integer transCount5) {
		this.transCount5 = transCount5;
	}

	public String getMatchRelation5() {
		return matchRelation5;
	}

	public void setMatchRelation5(String matchRelation5) {
		this.matchRelation5 = matchRelation5;
	}

	public BigDecimal getTransAmount5() {
		return transAmount5;
	}

	public void setTransAmount5(BigDecimal transAmount5) {
		this.transAmount5 = transAmount5;
	}

	public BigDecimal getFeeRate5() {
		return feeRate5;
	}

	public void setFeeRate5(BigDecimal feeRate5) {
		this.feeRate5 = feeRate5;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
    public String toString() {
        return "X0135BO [eventCommArea=" + eventCommArea + ", globalEventNo=" + globalEventNo + ", operationMode="
                + operationMode + ", feeItemNo=" + feeItemNo + ", feeType=" + feeType + ", instanCode1=" + instanCode1
                + ", instanCode2=" + instanCode2 + ", instanCode3=" + instanCode3 + ", instanCode4=" + instanCode4
                + ", instanCode5=" + instanCode5 + ", baseFee=" + baseFee + ", supplementBaseFee=" + supplementBaseFee
                + ", minFeeAmt=" + minFeeAmt + ", maxFeeAmt=" + maxFeeAmt + ", matrixAppMode=" + matrixAppMode
                + ", feeFlag=" + feeFlag + ", transCount1=" + transCount1 + ", matchRelation1=" + matchRelation1
                + ", transAmount1=" + transAmount1 + ", feeRate1=" + feeRate1 + ", transCount2=" + transCount2
                + ", matchRelation2=" + matchRelation2 + ", transAmount2=" + transAmount2 + ", feeRate2=" + feeRate2
                + ", transCount3=" + transCount3 + ", matchRelation3=" + matchRelation3 + ", transAmount3="
                + transAmount3 + ", feeRate3=" + feeRate3 + ", transCount4=" + transCount4 + ", matchRelation4="
                + matchRelation4 + ", transAmount4=" + transAmount4 + ", feeRate4=" + feeRate4 + ", transCount5="
                + transCount5 + ", matchRelation5=" + matchRelation5 + ", transAmount5=" + transAmount5 + ", feeRate5="
                + feeRate5 + ", feeCollectType=" + feeCollectType + ", feeItemInstanId=" + feeItemInstanId
                + ", feeMatrixApplicationDimension=" + feeMatrixApplicationDimension + ", redisKey=" + redisKey
                + ", instanDesc1=" + instanDesc1 + ", instanDesc2=" + instanDesc2 + ", instanDesc3=" + instanDesc3
                + ", instanDesc4=" + instanDesc4 + ", instanDesc5=" + instanDesc5 + ", feeDesc=" + feeDesc + ", id="
                + id + ", waiveCycle=" + waiveCycle + ", waiveCount=" + waiveCount + ", baseFee1=" + baseFee1
                + ", instanCode=" + instanCode + ", itemType=" + itemType + "]";
    }

    public EventCommArea getEventCommArea() {
		return eventCommArea;
	}

	public void setEventCommArea(EventCommArea eventCommArea) {
		this.eventCommArea = eventCommArea;
	}

	public String getGlobalEventNo() {
		return globalEventNo;
	}

	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}

	public String getFeeItemInstanId() {
		return feeItemInstanId;
	}

	public void setFeeItemInstanId(String feeItemInstanId) {
		this.feeItemInstanId = feeItemInstanId;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Integer getFeeMatrixApplicationDimension() {
		return feeMatrixApplicationDimension;
	}

	public void setFeeMatrixApplicationDimension(Integer feeMatrixApplicationDimension) {
		this.feeMatrixApplicationDimension = feeMatrixApplicationDimension;
	}

	public String getFeeCollectType() {
		return feeCollectType;
	}

	public void setFeeCollectType(String feeCollectType) {
		this.feeCollectType = feeCollectType;
	}

	public String getFeeDesc() {
		return feeDesc;
	}

	public void setFeeDesc(String feeDesc) {
		this.feeDesc = feeDesc;
	}

	public String getInstanDesc1() {
		return instanDesc1;
	}

	public void setInstanDesc1(String instanDesc1) {
		this.instanDesc1 = instanDesc1;
	}

	public String getInstanDesc2() {
		return instanDesc2;
	}

	public void setInstanDesc2(String instanDesc2) {
		this.instanDesc2 = instanDesc2;
	}

	public String getInstanDesc3() {
		return instanDesc3;
	}

	public void setInstanDesc3(String instanDesc3) {
		this.instanDesc3 = instanDesc3;
	}

	public String getInstanDesc4() {
		return instanDesc4;
	}

	public void setInstanDesc4(String instanDesc4) {
		this.instanDesc4 = instanDesc4;
	}

	public String getInstanDesc5() {
		return instanDesc5;
	}

	public void setInstanDesc5(String instanDesc5) {
		this.instanDesc5 = instanDesc5;
	}

	public String getBaseFee1() {
		return baseFee1;
	}

	public void setBaseFee1(String baseFee1) {
		this.baseFee1 = baseFee1;
	}

    public String getInstanCode() {
        return instanCode;
    }

    public void setInstanCode(String instanCode) {
        this.instanCode = instanCode;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
