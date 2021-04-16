package com.tansun.ider.bus.impl;

import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5105Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.issue.CoreCustomerAddrDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreCustomerPersonInfoDao;
import com.tansun.ider.dao.issue.CoreCustomerRemarksDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.entity.CoreCustomerPersonInfo;
import com.tansun.ider.dao.issue.entity.CoreCustomerRemarks;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerUnifyInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerAddrSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerPersonInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerRemarksSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerUnifyInfoSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5105BO;
import com.tansun.ider.model.vo.X5105VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * @version:1.0
 * @Description: 客户资料查询
 * @author: admin/syy改
 */
@Service
public class X5105BusImpl implements X5105Bus {

	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private CoreCustomerAddrDao coreCustomerInfoDao;
	@Autowired
	private CoreCustomerPersonInfoDao coreCustomerPersonInfoDao;
	@Autowired
	private CoreCustomerRemarksDao coreCustomerRemarksDao;
	@Autowired
	private CoreCustomerUnifyInfoDaoImpl coreCustomerUnifyInfoDaoImpl;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	private static String BSS_IQ_01_0101 = "BSS.IQ.01.0101";
	
	@Override
	public Object busExecute(X5105BO x5105bo) throws Exception {
		// 事件公共公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5105bo, eventCommAreaNonFinance);
		//身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		//证件类型
		String idType = eventCommAreaNonFinance.getIdType();
		//外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		if (StringUtil.isEmpty(idNumber)
				&&StringUtil.isEmpty(externalIdentificationNo)
				&& StringUtil.isEmpty(idType)
				&& StringUtil.isEmpty(eventCommAreaNonFinance.getCustomerName())
				&& StringUtil.isEmpty(eventCommAreaNonFinance.getCustomerNo())) {
			throw new BusinessException("CUS-00014", "客户基本");
		}
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		CoreCustomer coreCustomer = null;
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		Object object = null;
		//得到事件号   根据全局事件号进行截取操作
		String globalEventNo = x5105bo.getGlobalEventNo();
		String eventNo=globalEventNo.substring(0, globalEventNo.indexOf("-"));//截取-之前的字符串
		//BSS_IQ_01_0101事件只支持无效卡查询
		if (BSS_IQ_01_0101.equals(eventNo)){
			object = queryCustomerService.queryCustomerInvalid(idType, idNumber, externalIdentificationNo);//支持查询无效卡
		} else {//查询有效卡
			object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);//查询有效卡
		}
		if(object instanceof CoreCustomer){
			coreCustomer = (CoreCustomer)object;
		}else if(object instanceof CoreMediaBasicInfo){
			coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			if (null != coreMediaBasicInfo) {
				if(coreMediaBasicInfo.getMainCustomerNo()!=null){
					coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
					coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
				}
			}
		}
//		if(StringUtil.isNotBlank(idNumber)&&StringUtil.isNotBlank(idType)){
//			 coreCustomer = queryCustomerService.queryCustomer(idType, idNumber);
//		}
//		if(StringUtil.isNotBlank(externalIdentificationNo)){
//			CoreMediaBasicInfo coreMediaBasicInfo = queryCustomerService.queryCoreMediaBasicInfoForExt(externalIdentificationNo);
//			if(coreMediaBasicInfo!=null){
//				if(coreMediaBasicInfo.getMainCustomerNo()!=null){
//					coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
//				}
//			}else{
//				throw new BusinessException("CUS-00014", "客户基本");
//			}
//		}
		else{
			if(StringUtil.isNotEmpty(eventCommAreaNonFinance.getCustomerName())){
				coreCustomerSqlBuilder.andCustomerNameEqualTo(eventCommAreaNonFinance.getCustomerName());
			}
			if(StringUtil.isNotEmpty(eventCommAreaNonFinance.getCustomerNo())){
				coreCustomerSqlBuilder.andCustomerNoEqualTo(eventCommAreaNonFinance.getCustomerNo());
			}
			coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		}
		 
//		String customerNo = queryCustomerService.queryCoreMediaBasicInfo(idNumber, externalIdentificationNo);
//		if (StringUtil.isNotBlank(customerNo)) {
//			coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
//		}
//		if (StringUtil.isNotEmpty(eventCommAreaNonFinance.getIdNumber())) {
//			coreCustomerSqlBuilder.andIdNumberEqualTo(eventCommAreaNonFinance.getIdNumber());
//		}
//		if (StringUtil.isNotEmpty(eventCommAreaNonFinance.getIdType())) {
//			coreCustomerSqlBuilder.andIdTypeEqualTo(eventCommAreaNonFinance.getIdType());
//		}
//		if (StringUtil.isNotEmpty(eventCommAreaNonFinance.getCustomerName())) {
//			coreCustomerSqlBuilder.andCustomerNameEqualTo(eventCommAreaNonFinance.getCustomerName());
//		}
//		if (StringUtil.isNotEmpty(eventCommAreaNonFinance.getCustomerNo())) {
//			coreCustomerSqlBuilder.andCustomerNoEqualTo(eventCommAreaNonFinance.getCustomerNo());
//		}

		X5105VO x5105VO = new X5105VO();
		if (coreCustomer != null) {
			// 检查所属机构
			CoreOrgan coreOrgan = httpQueryService.queryOrgan(coreCustomer.getInstitutionId());
			x5105VO.setCorporationEntityNo(coreOrgan.getCorporationEntityNo());
			// 客户地址
			CoreCustomerAddrSqlBuilder coreCustomerAddrSqlBuilder = new CoreCustomerAddrSqlBuilder();
			coreCustomerAddrSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
			List<CoreCustomerAddr> listCoreCustomerAddrs = coreCustomerInfoDao
					.selectListBySqlBuilder(coreCustomerAddrSqlBuilder);
			// 客户个人信息
			CoreCustomerPersonInfoSqlBuilder coreCustomerPersonInfoSqlBuilder = new CoreCustomerPersonInfoSqlBuilder();
			coreCustomerPersonInfoSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
			CoreCustomerPersonInfo coreCustomerPersonInfo = coreCustomerPersonInfoDao
					.selectBySqlBuilder(coreCustomerPersonInfoSqlBuilder);
			// 客户备注信息
			CoreCustomerRemarksSqlBuilder coreCustomerRemarksSqlBuilder = new CoreCustomerRemarksSqlBuilder();
			coreCustomerRemarksSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
			List<CoreCustomerRemarks> listcoreCustomerRemarks = coreCustomerRemarksDao
					.selectListBySqlBuilder(coreCustomerRemarksSqlBuilder);
			// 产品线统一日期表
			CoreCustomerUnifyInfoSqlBuilder coreCustomerUnifyInfoSqlBuilder = new CoreCustomerUnifyInfoSqlBuilder();
			coreCustomerUnifyInfoSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
			List<CoreCustomerUnifyInfo> listCoreCustomerUnifyInfos = coreCustomerUnifyInfoDaoImpl
					.selectListBySqlBuilder(coreCustomerUnifyInfoSqlBuilder);

			CachedBeanCopy.copyProperties(coreCustomer, x5105VO);

			if (listCoreCustomerAddrs != null && !listCoreCustomerAddrs.isEmpty()) {
				x5105VO.setCoreCustomerAddrs(listCoreCustomerAddrs);
			}

			if (coreCustomerPersonInfo != null) {
				CachedBeanCopy.copyProperties(coreCustomerPersonInfo, x5105VO);
			}

			if (listcoreCustomerRemarks != null && !listcoreCustomerRemarks.isEmpty()) {
				x5105VO.setListcoreCustomerRemarks(listcoreCustomerRemarks);
			}

			if (listCoreCustomerUnifyInfos != null && !listCoreCustomerUnifyInfos.isEmpty()) {
				x5105VO.setCoreCustomerUnifyInfos(listCoreCustomerUnifyInfos);
			}
		} else {
			throw new BusinessException("CUS-00014", "客户基本");
		}
		
		String entrys =Constant.EMPTY_LIST;
		entrys = coreCustomer.getId();
		//记录查询日志
		CoreEvent tempObject = new CoreEvent();
		paramsUtil.logNonInsert(x5105bo.getCoreEventActivityRel().getEventNo(), x5105bo.getCoreEventActivityRel().getActivityNo(),
				tempObject, tempObject, entrys, x5105bo.getOperatorId());
		return x5105VO;
	}

}
