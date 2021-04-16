package com.tansun.ider.bus.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.ider.bus.X5280Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5280BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CardUtils;

/**
 * 
 * 查询产品是否支持自动配号
 * @author wt
 *
 */
@Service
public class X5280BusImpl implements X5280Bus{
	
	private static Logger logger = LoggerFactory.getLogger(X5280BusImpl.class);
	
	@Override
	public Object busExecute(X5280BO x52800bo) throws Exception {
		String operationMode = x52800bo.getOperationMode();
		String productObjectCode = x52800bo.getProductObjectCode();
		List<CoreActivityArtifactRel> artifactList = x52800bo.getActivityArtifactList();
		EventCommArea eventCommArea_424 = new EventCommArea();
		eventCommArea_424.setEcommProdObjId(productObjectCode);
		eventCommArea_424.setEcommOperMode(operationMode);
		// 验证该活动是否配置构件信息
		Boolean element01 = false; // 是否跳过自动配号 false-自动配号 true-不自动配号
		String element02 = ""; //
		Boolean element03 = false; //
		Boolean checkResult = CardUtils.checkArtifactExist(BSC.ARTIFACT_NO_424, artifactList);
		if (!checkResult) {
			throw new BusinessException("COR-10002");
		}
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> resultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_424, eventCommArea_424);
		Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			// 424AAA01
			if (Constant.AUTOMATIC_STANDARD_BIN_FORMAT.equals(key)) {
				element01 = true;
			} else if (Constant.AUTOMATIC_NON_STANDARD_BIN_FORMAT.equals(key)) {
				element01 = false;
			}
			// 424AAA02
			if (Constant.AUTOMATIC_ALLOW_OPTIONAL_CARD_NUMBER.equals(key)) {
				element02 = "true"; //
			} else if (Constant.AUTOMATIC_NO_OPTIONAL_CARD_NUMBER_ALLOWED.equals(key)) {
				element02 = "false"; //
			}
			// 424AAA03
			if (Constant.AUTOMATIC_NUMBER_ASSIGNMENT.equals(key)) {
				element03 = true;
			} else if (Constant.AUTOMATIC_NUMBER_NOT_APPLY.equals(key)) {
				element03 = false;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
			}
		}
		if (element01) {
			if ("false".equals(element02) && element03) {
				// 特殊号必须为空，并根据卡BIN随机生成新的号码
				x52800bo.setFlagl("2");
			} else if ("true".equals(element02) && element03) {
				// 检查如果外部识别号已输入，则跳过自动配号处理，进行特殊号码检查；如果外部识别号未输入，则根据卡BIN随机生成新的号码
				//根据卡Bin进行自动配号检查
				x52800bo.setFlagl("3");
				}else {
					//根据卡Bin进行自动配号检查
					x52800bo.setFlagl("2");
				}
			} else {
				//特殊号必须输入 
				x52800bo.setFlagl("1");
		}
		return x52800bo;
	}

}
