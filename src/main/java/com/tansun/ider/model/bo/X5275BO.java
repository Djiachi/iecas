package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

public class X5275BO extends BeanVO implements Serializable{
	
    private static final long serialVersionUID = 5631117884270876035L;
    /** 活动与构件对应关系表 */
    CoreEventActivityRel coreEventActivityRel;
    /** 活动与构件对应关系表 */
    List<CoreActivityArtifactRel> activityArtifactList;
    /** 全局事件编号 */
    private String globalEventNo;
    /** 生命周期节点类型  */
    private String lifecycleNodeType;
    /** 客户号  */
    private String customerNo;
    public CoreEventActivityRel getCoreEventActivityRel() {
        return coreEventActivityRel;
    }
    public void setCoreEventActivityRel(CoreEventActivityRel coreEventActivityRel) {
        this.coreEventActivityRel = coreEventActivityRel;
    }
    public List<CoreActivityArtifactRel> getActivityArtifactList() {
        return activityArtifactList;
    }
    public void setActivityArtifactList(List<CoreActivityArtifactRel> activityArtifactList) {
        this.activityArtifactList = activityArtifactList;
    }
    public String getGlobalEventNo() {
        return globalEventNo;
    }
    public void setGlobalEventNo(String globalEventNo) {
        this.globalEventNo = globalEventNo;
    }
    public String getLifecycleNodeType() {
        return lifecycleNodeType;
    }
    public void setLifecycleNodeType(String lifecycleNodeType) {
        this.lifecycleNodeType = lifecycleNodeType;
    }
    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    
    
	
}
