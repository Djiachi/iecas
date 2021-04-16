package com.tansun.ider.util;

import java.util.HashMap;
import java.util.Map;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.dao.beta.entity.CoreIssueCardBin;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;

/**
 * @Desc:制卡请求公共处理类
 * @Author wt
 * @Date 2018年5月31日 下午4:04:10
 */
public class CardMakingUtil {

	
	/**
	 * 
	* @Description: 生成磁道信息,并且拼接磁道内容
	* @param coreMediaCardInfo
	* @param coreMediaBasicInfo
	* @param coreIssueCardBin
	* @return
	 */
	public static Map<String, Object> getTrackInfo(CoreMediaCardInfo coreMediaCardInfo,
			String  externalIdentificationNo ,String expirationDate, CoreIssueCardBin coreIssueCardBin,String cvv,CoreMediaObject coreMediaObject) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 外部识别号	外部识别号由原先的19位变为现在的16位且以后长度不定，所以这里不做截取操作  add by wangxi 2019/7/4
//		String externalIdentificationNoStr = externalIdentificationNo.substring(3, 18);
		StringBuffer sb1 = new StringBuffer();
		sb1.append(";");
		sb1.append("B");
		sb1.append(externalIdentificationNo);
		sb1.append("^");
		sb1.append(coreMediaCardInfo.getEmbosserName1());
		sb1.append("^");
		String YYYY = "";
		String MM = "";
		if (StringUtil.isNotBlank(expirationDate)) {
			YYYY = expirationDate.substring(2, 4);
			MM = expirationDate.substring(0, 2);
		}else {
			YYYY= "00";
			MM = "0";
		}
		sb1.append(YYYY+MM);
		sb1.append("000");
		sb1.append("00000");
		sb1.append("00000000");
		sb1.append("00"+cvv+"000000");
		sb1.append("?");
		StringBuffer sb2 = new StringBuffer();
		sb2.append(";");
		sb2.append(externalIdentificationNo);
		sb2.append("=");
		sb2.append(YYYY+MM);
		sb2.append(coreMediaObject.getServiceCode());
		sb2.append("00000");
		sb2.append(cvv+"00000");
		sb2.append("?");
		sb2.append("0");
		map.put("magneticChannel2", sb2);
		StringBuffer sb3 = new StringBuffer();
		sb3.append(";");
		sb3.append("99");
		sb3.append(externalIdentificationNo);
		sb3.append("=");
		sb3.append("156");
		sb3.append("156");
		sb3.append("2");
		sb3.append("9");
		sb3.append("9000");
		sb3.append("1");
		sb3.append("3");
		sb3.append("0");
		sb3.append("10");
		sb3.append("10");
		sb3.append("10");
		sb3.append(YYYY+MM);
		sb3.append("1");
		sb3.append("=");
		sb3.append("0");
		sb3.append("=");
		sb3.append("0");
		sb3.append("=");
		sb3.append("0");
		sb3.append(coreIssueCardBin.getBinNo());
		sb3.append("00000");
		sb3.append("00");
		map.put("magneticChannel1", sb1);
		map.put("magneticChannel2", sb2);
		map.put("magneticChannel3", sb3);
		return map;
	}
	
}
