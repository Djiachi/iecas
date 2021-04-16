package com.tansun.ider.bus.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5650Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgramScope;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.ProductType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5650BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;

/**
 * 支付计划消贷分期账户查询
 * @author liuyanxi
 *
 */
@Service
public class X5650BusImpl implements X5650Bus {

	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Resource
	private HttpQueryService httpQueryService;

	@Override
	public Object busExecute(X5650BO x5650bo) throws Exception {
		String externalIdentificationNo = x5650bo.getExternalIdentificationNo();
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			List<CoreMediaBasicInfo> list = coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (list == null || list.size() == 0) {
				throw new BusinessException("CUS-00018");
			}
			this.checkProductType(list.get(0));
		}
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		eventCommAreaNonFinance.setExternalIdentificationNo(x5650bo.getExternalIdentificationNo());
		eventCommAreaNonFinance.setIndexNo(x5650bo.getIndexNo());
		eventCommAreaNonFinance.setPageNum(x5650bo.getPageNum());
		eventCommAreaNonFinance.setPageSize(x5650bo.getPageSize());
		return eventCommAreaNonFinance;
	}	
	/**
	 * 验证产品是否是信贷产品
	 * @param coreMediaBasicInfo
	 * @throws Exception
	 */
    public void checkProductType(CoreMediaBasicInfo coreMediaBasicInfo) throws Exception {
    	//产品对象代码+运营模式->业务项目代码 
		List<CoreProductBusinessScope> coreProductBusinessScope = 
				httpQueryService.queryProductBusinessScope(coreMediaBasicInfo.getProductObjectCode(),coreMediaBasicInfo.getOperationMode());
		if(coreProductBusinessScope == null || coreProductBusinessScope.size()==0){
			throw new BusinessException("CUS-00059");
		}
		//查询产品信息
		CoreProductObject  coreProductObject  = 
				httpQueryService.queryProductObject(coreMediaBasicInfo.getOperationMode(), coreMediaBasicInfo.getProductObjectCode());
		if(coreProductObject==null){
			throw new BusinessException("CUS-00006");
		}
    	 // 查询业务项目范围
        List<CoreBusinessProgramScope> coreBusinessProgramScope = 
        		httpQueryService.queryBusinessProgramScope(coreProductBusinessScope.get(0).getBusinessProgramNo(),coreMediaBasicInfo.getOperationMode());
        if(coreBusinessProgramScope == null || coreBusinessProgramScope.size()==0){
			throw new BusinessException("CUS-00063");
		}
		//业务项目代码+主客户代码->账单日等
        String businessProgramNo = coreProductBusinessScope.get(0).getBusinessProgramNo();
        CoreBusinessProgram businessProgram =
                httpQueryService.queryBusinessProgram(coreMediaBasicInfo.getOperationMode(), businessProgramNo);
        if (businessProgram == null) {
            // TODO产品不存在
            throw new BusinessException("COR-12017");
        }
        CoreBusinessType coreBusinessType =
                httpQueryService.queryBusinessType(coreMediaBasicInfo.getOperationMode(), businessProgram.getDefaultBusinessType());
        if (coreBusinessType == null) {
            // TODO产品不存在
            throw new BusinessException("COR-12017");
        }
        if (!ProductType.LOAN.getValue().equals(businessProgram.getProgramType())) {
            throw new BusinessException("COR-12017");
        }
    }
}
