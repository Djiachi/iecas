package com.tansun.ider.model.bo;

import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

public class X5270BO {

	/** 公共区域信息 */
	private EventCommAreaNonFinance eventCommAreaNonFinance;
	/** 构件列表 */
	private List<CoreActivityArtifactRel> artifactList;
	/** 全局事件编号 */
	private String globalEventNo;
    /** 转出媒介代码 */
    private String transferMediaCode;

	public String getTransferMediaCode() {
		return transferMediaCode;
	}

	public void setTransferMediaCode(String transferMediaCode) {
		this.transferMediaCode = transferMediaCode;
	}

	public X5270BO() {
		super();
	}

	public X5270BO(EventCommAreaNonFinance eventCommAreaNonFinance, List<CoreActivityArtifactRel> artifactList,
			String globalEventNo) {
		super();
		this.eventCommAreaNonFinance = eventCommAreaNonFinance;
		this.artifactList = artifactList;
		this.globalEventNo = globalEventNo;
	}

	public EventCommAreaNonFinance getEventCommAreaNonFinance() {
		return eventCommAreaNonFinance;
	}

	public void setEventCommAreaNonFinance(EventCommAreaNonFinance eventCommAreaNonFinance) {
		this.eventCommAreaNonFinance = eventCommAreaNonFinance;
	}

	public List<CoreActivityArtifactRel> getArtifactList() {
		return artifactList;
	}

	public void setArtifactList(List<CoreActivityArtifactRel> artifactList) {
		this.artifactList = artifactList;
	}

	public String getGlobalEventNo() {
		return globalEventNo;
	}

	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}

	@Override
	public String toString() {
		return "X5270BO [eventCommAreaNonFinance=" + eventCommAreaNonFinance + ", artifactList=" + artifactList
				+ ", globalEventNo=" + globalEventNo + "]";
	}

}
