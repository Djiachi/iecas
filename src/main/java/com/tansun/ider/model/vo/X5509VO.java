package com.tansun.ider.model.vo;

import java.io.Serializable;

import com.tansun.ider.dao.beta.entity.CoreElement;

/**
 * @Desc:X5509元件
 * @Author lianhuan
 * @Date 2018年4月24日下午4:16:00
 */
public class X5509VO extends CoreElement implements Serializable {

    private static final long serialVersionUID = 5035069993911420241L;
    /** PCD编号 : PCD编号 [8,0] Not NULL */
    protected String pcdNo;
    /** PCD描述 : PCD描述 [50,0] Not NULL */
    protected String pcdMsg;

    public String getPcdNo() {
        return pcdNo;
    }

    public void setPcdNo(String pcdNo) {
        this.pcdNo = pcdNo;
    }

    public String getPcdMsg() {
        return pcdMsg;
    }

    public void setPcdMsg(String pcdMsg) {
        this.pcdMsg = pcdMsg;
    }

    @Override
    public String toString() {
        return "X5509VO [pcdNo=" + pcdNo + ", pcdMsg=" + pcdMsg + "]";
    }

}
