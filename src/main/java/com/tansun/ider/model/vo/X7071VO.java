package com.tansun.ider.model.vo;

public class X7071VO {

	/* 返回代码 */
	private String returnCode;
	/*分期编号*/
	private String stageControlNum;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getStageControlNum() {
		return stageControlNum;
	}

	public void setStageControlNum(String stageControlNum) {
		this.stageControlNum = stageControlNum;
	}

	@Override
	public String toString() {
		return "X7071VO [returnCode=" + returnCode + ", stageControlNum=" + stageControlNum + "]";
	}

}
