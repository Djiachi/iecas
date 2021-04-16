package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X4065Bus;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreBudgetOrgCustRelDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgCustRel;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgCustRelSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X4050BO;
import com.tansun.ider.model.vo.X4055VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
/**
 * 公务卡未出、金融、已出按照预算单位编码查询
 * @Description:TODO()   
 * @author: sunyaoyao
 * @date:   2019年5月17日 上午11:27:18   
 *
 */
@Service
public class X4065BusImpl implements X4065Bus {
	@Autowired
	private CoreBudgetOrgCustRelDao coreBudgetOrgCustRelDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Resource
	private HttpQueryService httpQueryService;
	@Override
	public Object busExecute(X4050BO x4050bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		X4055VO x4055vo = null;
		List<X4055VO> list = new ArrayList<X4055VO>();
		PageBean<X4055VO> page = new PageBean<>();
		CachedBeanCopy.copyProperties(x4050bo,eventCommAreaNonFinance);
		//预算单位编码
		String idNumber = x4050bo.getIdNumber();
		CoreBudgetOrgCustRelSqlBuilder budgetOrgCustRelSqlBuilder = new CoreBudgetOrgCustRelSqlBuilder();
		budgetOrgCustRelSqlBuilder.andBudgetOrgCodeEqualTo(idNumber);
		budgetOrgCustRelSqlBuilder.setIndexNo(x4050bo.getIndexNo());
		budgetOrgCustRelSqlBuilder.setPageSize(x4050bo.getPageSize());
//		PageBean<CoreBudgetOrgCustRel> page = new PageBean<CoreBudgetOrgCustRel>();
		List<CoreBudgetOrgCustRel> lists = coreBudgetOrgCustRelDao.selectListBySqlBuilder(budgetOrgCustRelSqlBuilder);
		CoreMediaBasicInfoSqlBuilder  basicInfoSqlBuilder = null;
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = null;
		for (CoreBudgetOrgCustRel coreBudgetOrgCustRel : lists) {
			x4055vo = new X4055VO();
			String externalIdentificationNo = coreBudgetOrgCustRel.getExternalIdentificationNo();
			basicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			basicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(basicInfoSqlBuilder);
			if(coreMediaBasicInfo==null){
				x4055vo.setExternalIdentificationNo(externalIdentificationNo);
				x4055vo.setCustomerName("该外部识别号在媒介基本信息表中不存在！");
			}else{
				CoreProductObject coreProductObject = httpQueryService.queryProductObject(coreMediaBasicInfo.getOperationMode(), coreMediaBasicInfo.getProductObjectCode());
				x4055vo.setExternalIdentificationNo(externalIdentificationNo);
//				x4055vo.setCustomerArea(coreBudgetOrgCustRel.getCustomerArea());
				x4055vo.setInvalidFlag(coreMediaBasicInfo.getInvalidFlag());
				x4055vo.setMediaUnitCode(coreMediaBasicInfo.getMediaUnitCode());
				x4055vo.setOfficialCardType(coreProductObject.getProductDesc());
				x4055vo.setIdNumber(coreBudgetOrgCustRel.getIdNumber());
				x4055vo.setIdType(coreBudgetOrgCustRel.getIdType());
				String mainCustomerNo = coreMediaBasicInfo.getMainCustomerNo();
				coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
				coreCustomerSqlBuilder.andCustomerNoEqualTo(mainCustomerNo);
				CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
				if(coreCustomer!=null){
					x4055vo.setCustomerName(coreCustomer.getCustomerName());
				}
			}
			list.add(x4055vo);
		}
		int i = coreBudgetOrgCustRelDao.countBySqlBuilder(budgetOrgCustRelSqlBuilder);
		page.setPageSize(x4050bo.getPageSize());
        page.setTotalCount(i);
		if(i>0){
			page.setRows(list);
		}
		return page;
	}

}
