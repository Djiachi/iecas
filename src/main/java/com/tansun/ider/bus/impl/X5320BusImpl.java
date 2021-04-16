package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5320Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerBillDayDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.enums.YesOrNo;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5320BO;
import com.tansun.ider.model.bo.X5960BO;
import com.tansun.ider.model.vo.X5320VO;
import com.tansun.ider.service.BetaCommonParamService;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;

/**
 * @version:1.0
 * @Description: 客户业务项目查询
 * @author: admin
 */
@Service
public class X5320BusImpl implements X5320Bus {

//	@Autowired
//	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Resource
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private CoreCustomerBillDayDaoImpl coreCustomerBillDayDaoImpl;
	@Autowired
	private BetaCommonParamService betaCommonParamService;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerBillDayDao coreCustomerBillDayDao;

	@Override
	public Object busExecute(X5320BO x5320bo) throws Exception {
		// 公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5320bo, eventCommAreaNonFinance);
		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		//证件类型
		String idType = eventCommAreaNonFinance.getIdType();
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		// 客户代码
		String customerNo = eventCommAreaNonFinance.getCustomerNo();
		PageBean<X5320VO> page = new PageBean<>();
		CoreCustomer coreCustomer = null;
		String operationMode = "";
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
		if(object instanceof CoreCustomer){
			 coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
			operationMode = coreCustomer.getOperationMode();
		}else if(object instanceof CoreMediaBasicInfo){
			 coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			 operationMode = coreMediaBasicInfo.getOperationMode();
			if(coreMediaBasicInfo.getMainCustomerNo()!=null){
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
			}
		}
        
//		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
//		if (StringUtil.isNotBlank(idNumber)&&StringUtil.isNotBlank(idType)) {
//			Object object = queryCustomerService.queryCustomer(idType, idNumber,null);
//			if(object instanceof CoreCustomer){
//				coreCustomer = (CoreCustomer)object;
//				customerNo = coreCustomer.getCustomerNo();
//			}
//		}
//			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
//			coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
//			if (null == coreCustomer) {
//				throw new BusinessException("CUS-00014", "客户基本");
//			} else {
//				customerNo = coreCustomer.getCustomerNo();
//			}
//		}
//		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
//		if (StringUtil.isNotBlank(externalIdentificationNo)) {
//			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
//			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
//			coreMediaBasicInfo = coreMediaBasicInfoDaoImpl.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
//			if (null == coreMediaBasicInfo) {
//				throw new BusinessException("CUS-00014", "媒介基本");
//			} else {
//				customerNo = coreMediaBasicInfo.getMainCustomerNo();
//			}
//		}
		CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
		coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
		int totalCount = coreCustomerBillDayDaoImpl.countBySqlBuilder(coreCustomerBillDaySqlBuilder);
		page.setTotalCount(totalCount);
		if (null != coreCustomer) {
			operationMode = coreCustomer.getOperationMode();
		} else {
			operationMode = coreMediaBasicInfo.getOperationMode();
		}

		if (null != x5320bo.getPageSize() && null != x5320bo.getIndexNo()) {
			coreCustomerBillDaySqlBuilder.setPageSize(x5320bo.getPageSize());
			coreCustomerBillDaySqlBuilder.setIndexNo(x5320bo.getIndexNo());
			page.setPageSize(x5320bo.getPageSize());
			page.setIndexNo(x5320bo.getIndexNo());
		}
		if (totalCount > 0) {
			List<X5320VO> listX5320VO = new ArrayList<>();
			List<CoreCustomerBillDay> listCoreCustomerBillDays = coreCustomerBillDayDaoImpl
					.selectListBySqlBuilder(coreCustomerBillDaySqlBuilder);
			for (CoreCustomerBillDay coreCustomerBillDay : listCoreCustomerBillDays) {
				X5320VO x5320VO = new X5320VO();
				String businessProgramNo = coreCustomerBillDay.getBusinessProgramNo();
				EventCommArea eventCommArea = new EventCommArea();
				eventCommArea.setEcommBusinessProgramCode(businessProgramNo);
				eventCommArea.setEcommCustId(coreCustomerBillDay.getCustomerNo());
				eventCommArea.setEcommOperMode(operationMode);
				CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
				Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_505, eventCommArea);
				Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
				String cycleModel = "N";
				while (it.hasNext()) {
					Map.Entry<String, String> entry = it.next();
					if (Constant.ACC_DATE_GENERATE_Y.equals(entry.getKey())) { // 505AAA0199
						// 获取客户信息表表中账单日
						cycleModel = "Y";
					}
				}
				if (YesOrNo.YES.getValue().equals(cycleModel)) {
				}else {
					CachedBeanCopy.copyProperties(coreCustomerBillDay, x5320VO);
				}
				x5320VO.setCycleModel(cycleModel);
				// CoreBusinessProgramSqlBuilder coreBusinessProgramSqlBuilder =
				// new CoreBusinessProgramSqlBuilder();
				// coreBusinessProgramSqlBuilder.andBusinessProgramNoEqualTo(coreCustomerBillDay.getBusinessProgramNo());
				// CoreBusinessProgram coreBusinessProgram =
				// coreBusinessProgramDao
				// .selectBySqlBuilder(coreBusinessProgramSqlBuilder);
				CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode,
						coreCustomerBillDay.getBusinessProgramNo());
				if (coreBusinessProgram != null) {
					x5320VO.setProgramDesc(coreBusinessProgram.getProgramDesc());
				}
				x5320VO.setOperationMode(operationMode);
				listX5320VO.add(x5320VO);
			}
			page.setRows(listX5320VO);
		}

		return page;
	}
	
    @Override
    public Object queryCustomerBillDay(X5320BO x5320bo) throws Exception {
		// 身份证号
		String idNumber = x5320bo.getIdNumber();
		//证件类型
		String idType = x5320bo.getIdType();
		// 外部识别号
		String externalIdentificationNo = x5320bo.getExternalIdentificationNo();
		// 客户代码
		String customerNo = x5320bo.getCustomerNo();
		String businessProgramNo = x5320bo.getBusinessProgramNo();
		
		CoreCustomer coreCustomer = null;
		String operationMode = "";
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		
		Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
		if(object instanceof CoreCustomer){
			 coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
			operationMode = coreCustomer.getOperationMode();
		}else if(object instanceof CoreMediaBasicInfo){
			 coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			 operationMode = coreMediaBasicInfo.getOperationMode();
			if(coreMediaBasicInfo.getMainCustomerNo()!=null){
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
			}
		}
    	
		CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
		CoreCustomerBillDay coreCustomerBillDay = null;
        if (StringUtil.isNotBlank(customerNo) && StringUtil.isNotBlank(businessProgramNo)) {
        	coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(customerNo);
        	coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
        	coreCustomerBillDay = coreCustomerBillDayDao.selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
        }
        return coreCustomerBillDay;
    }

}
