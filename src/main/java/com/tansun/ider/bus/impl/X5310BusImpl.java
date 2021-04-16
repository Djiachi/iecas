package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5310Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5310BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;

/**
 * 
 * 判断该产品包含的业务项目是否需要输入账单日
 * 
 * @author wt
 *
 */
@Service
public class X5310BusImpl implements X5310Bus {

	@Autowired
	private HttpQueryService httpQueryService;

	@Override
	public Object busExecute(X5310BO x5310bo) throws Exception {
		// 验证输入字段必须输入
		SpringUtil.getBean(ValidatorUtil.class).validate(x5310bo);

		PageBean<X5310BO> page = new PageBean<>();

		String operationMode = x5310bo.getOperationMode();
		String productObjectCode = x5310bo.getProductObjectCode();
		List<CoreProductBusinessScope> coreBusinessProgramScopeList = httpQueryService
				.queryProductBusinessScope(productObjectCode, operationMode);
		List<X5310BO> x5310BOList = new ArrayList<>();
		if (null != coreBusinessProgramScopeList && !coreBusinessProgramScopeList.isEmpty()) {
			for (CoreProductBusinessScope coreProductBusinessScope : coreBusinessProgramScopeList) {
				X5310BO x5310BO = new X5310BO();
				String businessProgramNo = coreProductBusinessScope.getBusinessProgramNo();
				EventCommArea eventCommArea = new EventCommArea();
				eventCommArea.setEcommOperMode(operationMode);
				eventCommArea.setEcommBusinessProgramCode(businessProgramNo);
				// 账单日是否输入计算
				String statementDay = statementDayFollow(BSC.ARTIFACT_NO_505, eventCommArea);
				// cycle刷新根据那种内容
				String cycleFrequency = cycleFrequency(BSC.ARTIFACT_NO_506, eventCommArea);
				if (StringUtil.isNotBlank(statementDay)) {
					if (Constant.ACC_DATE_GENERATE_N2.equals(statementDay)) {
						x5310BOList.add(x5310BO);
						CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode,
								businessProgramNo);
						if (null == coreBusinessProgram) {
							throw new BusinessException("CUS-00014", "业务项目");
						}
						x5310BO.setProgramDesc(coreBusinessProgram.getProgramDesc());
						x5310BO.setBusinessProgramNo(businessProgramNo);
						x5310BO.setOperationMode(operationMode);
						x5310BO.setProductObjectCode(productObjectCode);
					}
				} else {
					throw new BusinessException("CUS-00100", businessProgramNo);
				}
				if (StringUtil.isNotBlank(cycleFrequency)) {
					if (Constant.CYCLE_BY_MONTH.equals(cycleFrequency)) { // 506AAA0100
						x5310BO.setCycleFrequency("month");
					} else if (Constant.CYCLE_BY_WEEK.equals(cycleFrequency)) { // 506AAA0101
						x5310BO.setCycleFrequency("week");
					} else if (Constant.CYCLE_NOT_APPLY.equals(cycleFrequency)) { // 506AAA0101
						x5310BO.setCycleFrequency("notApply");
					}
				} else {
					throw new BusinessException("CUS-00101", productObjectCode);
				}
			}
		} else {
			throw new BusinessException("CUS-00099", productObjectCode);
		}
		page.setRows(x5310BOList);
		page.setTotalCount(x5310BOList.size());
		return page;
	}

	/**
	 * cycle 账单日设置，按照每月，还是按照每周
	 * 
	 * @param artifactNo506
	 * @param eventCommArea
	 * @return
	 * @throws Exception
	 */
	private String cycleFrequency(String artifactNo506, EventCommArea eventCommArea) throws Exception {
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> elePcdResultMap = artService.getElementByArtifact(artifactNo506, eventCommArea);
		Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (Constant.CYCLE_BY_MONTH.equals(entry.getKey())) { // 506AAA0100
				return Constant.CYCLE_BY_MONTH;
			} else if (Constant.CYCLE_BY_WEEK.equals(entry.getKey())) { // 506AAA0101
				return Constant.CYCLE_BY_WEEK;
			}else if (Constant.CYCLE_NOT_APPLY.equals(entry.getKey())) { // 506AAA0101
				return Constant.CYCLE_NOT_APPLY;
			}
		}
		return null;
	}

	/**
	 * 505 构建验证账单日是否需要输入
	 * 
	 * @param artifactNo
	 * @param eventCommArea
	 * @return
	 * @throws Exception
	 */
	public String statementDayFollow(String artifactNo505, EventCommArea eventCommArea) throws Exception {
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		Map<String, String> elePcdResultMap = artService.getElementByArtifact(artifactNo505, eventCommArea);
		Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (Constant.ACC_DATE_GENERATE_N1.equals(entry.getKey())) { // 505AAA0100
				// 获取客户信息表表中账单日
				return Constant.ACC_DATE_GENERATE_N1;
			} else if (Constant.ACC_DATE_GENERATE_N2.equals(entry.getKey())) { // 505AAA0101
				// 获取账单日为，获取客户预先设置账单日
				return Constant.ACC_DATE_GENERATE_N2;
			} else if (Constant.ACC_DATE_GENERATE_ZERO.equals(entry.getKey())) { // 505AAA0102
				// 账单日、下一账单日赋值为0
				return Constant.ACC_DATE_GENERATE_ZERO;
			}else if (Constant.ACC_DATE_OFFICIAL_CARD.equals(entry.getKey())) { // 505AAA0103
				// 账单日、下一账单日赋值为0
				return Constant.OFFICIAL_CARD;
			}else if (Constant.ACC_DATE_GENERATE_Y.equals(entry.getKey())) {
				return Constant.ACC_DATE_GENERATE_Y;
			}
		}
		return null;
	}

}
