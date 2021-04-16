package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5523 封锁码
 * @Author lianhuan
 * @Date 2019年4月25日下午3:03:34
 */
public class X5523BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;

    /** 运营模式 [3,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String operationMode;
    /** 封锁码类别 [1,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    private String blockType;
    /** 封锁码场景序号 [10,0] Not NULL */
    @NotNull
    @Digits(integer = 10, fraction = 0)
    private Integer sceneSerialNo;
    /** 优先级 [10,0] */
    private Integer performOrder;
    /** 封锁码场景描述 [50,0] */
    private String sceneDesc;
    /** 封锁范围 C：客户级A：业务类型级P：产品级M：媒介级 [50,0] */
    private String blockScope;
    
    /** X5508构件基础元件 */
    private List<X5523TwoBO> list;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public Integer getSceneSerialNo() {
        return sceneSerialNo;
    }

    public void setSceneSerialNo(Integer sceneSerialNo) {
        this.sceneSerialNo = sceneSerialNo;
    }

    public Integer getPerformOrder() {
        return performOrder;
    }

    public void setPerformOrder(Integer performOrder) {
        this.performOrder = performOrder;
    }

    public String getSceneDesc() {
        return sceneDesc;
    }

    public void setSceneDesc(String sceneDesc) {
        this.sceneDesc = sceneDesc;
    }

    public String getBlockScope() {
        return blockScope;
    }

    public void setBlockScope(String blockScope) {
        this.blockScope = blockScope;
    }

	public List<X5523TwoBO> getList() {
		return list;
	}

	public void setList(List<X5523TwoBO> list) {
		this.list = list;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "X5523BO [operationMode=" + operationMode + ", blockType="
				+ blockType + ", sceneSerialNo=" + sceneSerialNo
				+ ", performOrder=" + performOrder + ", sceneDesc=" + sceneDesc
				+ ", blockScope=" + blockScope + ", list=" + list + "]";
	}

}
