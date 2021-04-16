package com.tansun.ider.model.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5511发行卡BIN
 * @Author lianhuan
 * @Date 2018年4月25日上午11:30:55
 */
public class X5511BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = -5666151452489974741L;

    /** 发行卡BIN [10,0] Not NULL */
    //@NotBlank(message = "验证字符串非null，且长度必须大于0 ")
   // @Digits(integer = 10, fraction = 0)
    @NotNull(message = "验证字符串非null，且长度必须大于0")
    private Integer bin;
    /** V：VISA M：Mastercard J:JCB C:CUP A:AMEX [1,0] Not NULL */
    private String organizeTyp;
    /** 卡类型 [2,0] */
    private String cardTyp;
    /** 清算币种1 [3,0] Not NULL */
    private String resolutionCurr1;
    /** 清算币种2 [3,0] Not NULL */
    private String resolutionCurr2;

    public Integer getBin() {
        return bin;
    }

    public void setBin(Integer bin) {
        this.bin = bin;
    }

    public String getOrganizeTyp() {
        return organizeTyp;
    }

    public void setOrganizeTyp(String organizeTyp) {
        this.organizeTyp = organizeTyp;
    }

    public String getCardTyp() {
        return cardTyp;
    }

    public void setCardTyp(String cardTyp) {
        this.cardTyp = cardTyp;
    }

    public String getResolutionCurr1() {
        return resolutionCurr1;
    }

    public void setResolutionCurr1(String resolutionCurr1) {
        this.resolutionCurr1 = resolutionCurr1;
    }

    public String getResolutionCurr2() {
        return resolutionCurr2;
    }

    public void setResolutionCurr2(String resolutionCurr2) {
        this.resolutionCurr2 = resolutionCurr2;
    }

    @Override
    public String toString() {
        return "X5511BO [bin=" + bin + ", organizeTyp=" + organizeTyp + ", cardTyp=" + cardTyp + ", resolutionCurr1="
                + resolutionCurr1 + ", resolutionCurr2=" + resolutionCurr2 + "]";
    }

}
