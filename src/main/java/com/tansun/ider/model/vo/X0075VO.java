package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.dao.beta.entity.CorePcdInstan;
import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.service.business.EventCommArea;

/**
 * 构件实例
 * 
 * @ClassName X0075BO
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zhangte
 * @Date 2018年8月15日 下午2:00:02
 * @version 1.0.0
 */
public class X0075VO extends BeanVO implements Serializable {

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = -2658588131689614376L;
	private EventCommArea eventCommArea;
	/* 创建一个全局流水号 */
	private String globalEventNo;
	/** 运营模式 [3,0] Not NULL */
	@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
	private String operationMode;
	/** 构件编号 [15,0] Not NULL */
	@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
	private String artifactNo;
	@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
	/** 元件编号 需要根据以下维度代码唯一确定该元件 [15,0] Not NULL */
	private String elementNo;
	private String elementDesc;
	/** 维度代码1, 根据构件设定的实例化维度赋值相应的值，如果没有设定则初始化为空 [14,0] */
	private String instanCode1;
	/** 维度代码2 [14,0] */
	private String instanCode2;
	/** 维度代码3 [14,0] */
	private String instanCode3;
	/** 维度代码4 [14,0] */
	private String instanCode4;
	/** 维度代码5 [14,0] */
	private String instanCode5;
	/** 维度代码[14,0] */
	private String instanCode;
	/** 执行顺序 [10,0] */
	private Integer performOrder;
	private String artifactInstanId;
	private String id;
	 /** PCD编号 [8,0] Not NULL */
    protected String pcdNo;
    /** PCD描述 [50,0] */
    protected String pcdDesc;
    
    protected String instanDesc1;
    protected String instanDesc2;
    protected String instanDesc3;
    protected String instanDesc4;
    protected String instanDesc5;
    
    private String artifactDesc;
    /** pcd实例集合  */
	private List<CorePcdInstan>  pcdInstanList;
	 /**分段类型   */
	private String segmentType;
	public String getPcdNo() {
		return pcdNo;
	}
	public String getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }

    public List<CorePcdInstan> getPcdInstanList() {
        return pcdInstanList;
    }

    public void setPcdInstanList(List<CorePcdInstan> pcdInstanList) {
        this.pcdInstanList = pcdInstanList;
    }

    public void setPcdNo(String pcdNo) {
		this.pcdNo = pcdNo;
	}

	public String getPcdDesc() {
		return pcdDesc;
	}

	public void setPcdDesc(String pcdDesc) {
		this.pcdDesc = pcdDesc;
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

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getArtifactNo() {
		return artifactNo;
	}

	public void setArtifactNo(String artifactNo) {
		this.artifactNo = artifactNo;
	}

	public String getElementNo() {
		return elementNo;
	}

	public void setElementNo(String elementNo) {
		this.elementNo = elementNo;
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

	public Integer getPerformOrder() {
		return performOrder;
	}

	public void setPerformOrder(Integer performOrder) {
		this.performOrder = performOrder;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getArtifactInstanId() {
		return artifactInstanId;
	}

	public void setArtifactInstanId(String artifactInstanId) {
		this.artifactInstanId = artifactInstanId;
	}

	@Override
	public String toString() {
		return "X0075VO [eventCommArea=" + eventCommArea + ", globalEventNo=" + globalEventNo + ", operationMode="
				+ operationMode + ", artifactNo=" + artifactNo + ", elementNo=" + elementNo + ", elementDesc="
				+ elementDesc + ", instanCode1=" + instanCode1 + ", instanCode2=" + instanCode2 + ", instanCode3="
				+ instanCode3 + ", instanCode4=" + instanCode4 + ", instanCode5=" + instanCode5 + ", instanCode="
				+ instanCode + ", performOrder=" + performOrder + ", artifactInstanId=" + artifactInstanId + ", id="
				+ id + "]";
	}

	public String getInstanCode() {
		return instanCode;
	}

	public void setInstanCode(String instanCode) {
		this.instanCode = instanCode;
	}

	public String getElementDesc() {
		return elementDesc;
	}

	public void setElementDesc(String elementDesc) {
		this.elementDesc = elementDesc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getArtifactDesc() {
		return artifactDesc;
	}

	public void setArtifactDesc(String artifactDesc) {
		this.artifactDesc = artifactDesc;
	}
}
