package com.tansun.ider.util;

/**
 * 
 * ClassName:CoreCardNumberUtil 
 * desc:核心卡号算法生成
 * @author wt
 * @date 2018年4月19日 下午2:59:46
 */
public class CoreCardNumberUtil {

	/**
	 * 同步获取卡号客户号生成规则 支持卡账人生成
	 * 
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param card_head
	 *            卡头部六位
	 * @return
	 */
	public synchronized static String getBrankNumber(String card_head) {

		int indexStart = card_head.indexOf("R");
		String strBin = card_head.substring(0, indexStart);
		// 卡后9位生成按时间戳生成
		String num = CoreNumberRuleUtil.getUnixTime();
		// 拼成前15位，卡头核心梅举定义
		String st = strBin + num;
		// 加上最后一位luhn算法
		return st + CoreNumberRuleUtil.getBankCardCheckCode(st);
	}

	/**
	 * 支持卡/账/人生成规则
	 * 
	 * @param card_head
	 * @param strBin
	 * @return
	 */
	public synchronized static String getAccountNumber(String card_head, String strBin,String binSequenceNumber) {
		String st = "";
		if (strBin != null && !strBin.equals("")) {
			// 拼成前15位，卡头核心梅举定义
			st = strBin+binSequenceNumber;
		} else {
			return "";
		}
		// 加上最后一位luhn算法
		return card_head +st + CoreNumberRuleUtil.getBankCardCheckCode(st);
	}

}