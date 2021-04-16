package com.tansun.ider.model.bo;

import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;

public class X5280BO {
	
    /** 活动与构件对应关系表 */
    List<CoreActivityArtifactRel> activityArtifactList;
    private String operationMode;
    private String productObjectCode;
    private String flagl;
    private Boolean flag2;
    
	public List<CoreActivityArtifactRel> getActivityArtifactList() {
		return activityArtifactList;
	}

	public void setActivityArtifactList(List<CoreActivityArtifactRel> activityArtifactList) {
		this.activityArtifactList = activityArtifactList;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public Boolean getFlag2() {
		return flag2;
	}

	public void setFlag2(Boolean flag2) {
		this.flag2 = flag2;
	}

	public String getFlagl() {
		return flagl;
	}

	public void setFlagl(String flagl) {
		this.flagl = flagl;
	}

	
}
