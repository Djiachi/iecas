package com.tansun.ider.bus.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X4050Bus;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X4050BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.HttpQueryServiceImpl;
/**
 * 验证是否是公务卡查询
 * @Description:TODO()   
 * @author: sunyaoyao
 * @date:   2019年5月17日 上午11:27:18   
 *
 */
@Service
public class X4050BusImpl implements X4050Bus {
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private HttpQueryServiceImpl httpQueryServiceImpl;

	@Override
	public Object busExecute(X4050BO x4050bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		String externalIdentificationNo = x4050bo.getExternalIdentificationNo();
		if(StringUtil.isNotBlank(externalIdentificationNo)){
			CachedBeanCopy.copyProperties(x4050bo,eventCommAreaNonFinance);
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if(coreMediaBasicInfo!=null){
				String operationMode = coreMediaBasicInfo.getOperationMode();
				String productObjectCode = coreMediaBasicInfo.getProductObjectCode();
				EventCommArea eventCommArea = new EventCommArea();
				eventCommArea.setEcommOperMode(operationMode);
				eventCommArea.setEcommProdObjId(productObjectCode);
				List<CoreProductBusinessScope> queryProductBusinessScope = httpQueryServiceImpl.queryProductBusinessScope(productObjectCode,operationMode);
				for (CoreProductBusinessScope coreProductBusinessScope : queryProductBusinessScope) {
					eventCommArea.setEcommBusinessProgramCode(coreProductBusinessScope.getBusinessProgramNo());
					CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
					Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_501, eventCommArea);
					Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, String> entry = it.next();
						if (Constant.OFFICIAL_CARD.equals(entry.getKey())) { // 501AAA0101预算单位业务
							break;
						}else{
							throw new BusinessException("此业务项目非公务卡！");
						}
					}
				}
				
			}else{
				throw new BusinessException("获取不到客户媒介信息！");
			}
		}else{
			throw new BusinessException("未获取外部识别号！");
		}
		return eventCommAreaNonFinance;
	}

}
