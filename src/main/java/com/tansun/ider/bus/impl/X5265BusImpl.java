package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.framework.validation.service.ValidatorUtil;
import com.tansun.ider.bus.X5265Bus;
import com.tansun.ider.dao.beta.entity.CoreControlProject;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.issue.CoreCustomerContrlViewDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlView;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerContrlViewSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5265BO;
import com.tansun.ider.model.vo.X5265VO;
import com.tansun.ider.service.BetaCommonParamService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.web.WSC;

/**
 * @version:1.0
 * @Description: 封锁码管控视图查询
 * @author: admin
 */
@Service
public class X5265BusImpl implements X5265Bus {

	// 查询 000 全部列表
	private static String QUERY_FLAG_0 = "0";
	// 按照条件查询
	private static String QUERY_FLAG_1 = "1";
	@Autowired
	private CoreCustomerContrlViewDao coreCustomerContrlViewDaoImpl;
	@Autowired
	private BetaCommonParamService betaCommonParamService;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private CoreCustomerDao CoreCustomerDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
    private HttpQueryService httpQueryService; 
	@Override
	public Object busExecute(X5265BO X5265BO) throws Exception {
		// 判断输入的各字段是否为空
		SpringUtil.getBean(ValidatorUtil.class).validate(X5265BO);
		// 身份证号
		String idNumber = X5265BO.getIdNumber();
		String idType = X5265BO.getIdType();
		// 外部识别号
		String externalIdentificationNo = X5265BO.getExternalIdentificationNo();
		String customerNo = "";
		String operationMode = "";
		// 管控项目
		String controlProjectNo = X5265BO.getControlProjectNo();
		// 管控层级
		String contrlLevel = X5265BO.getContrlLevel();
		// 层级代码
		String levelCode = X5265BO.getLevelCode();
		String startDate = X5265BO.getStartDate();
		String endDate = X5265BO.getEndDate();
		// 查询方式 0 查询 000 列表， 1 查询根据条件查询
		String queryFlag = X5265BO.getQueryFlag();
		PageBean<X5265VO> page = new PageBean<>();
		CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder = new CoreCustomerContrlViewSqlBuilder();
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			coreMediaBasicInfo = queryCustomerService.queryCoreMediaBasicInfoForExt(externalIdentificationNo);
			if (null != coreMediaBasicInfo) {
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
				operationMode = coreMediaBasicInfo.getOperationMode();
			} else {
				throw new BusinessException("CUS-00014", "客户基本");
			}
		}
		CoreCustomer coreCustomer = null;
		if (StringUtil.isNotBlank(idType) && StringUtil.isNotBlank(idNumber)) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
			coreCustomer = CoreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (null != coreCustomer) {
				customerNo = coreCustomer.getCustomerNo();
				operationMode = coreCustomer.getOperationMode();
			} else {
				throw new BusinessException("CUS-00014", "客户基本");
			}
		}
		if (StringUtil.isBlank(customerNo)) {
			throw new BusinessException("COR-10048");
		} else {
			coreCustomerContrlViewSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		//查询条件 0 或者 1
		if (StringUtil.isNotBlank(queryFlag) && QUERY_FLAG_0.equals(queryFlag)) {
			coreCustomerContrlViewSqlBuilder.andContrlSerialNoEqualTo(000);
		} else if (StringUtil.isNotBlank(queryFlag) && QUERY_FLAG_1.equals(queryFlag)) {
			/*if (StringUtil.isBlank(contrlLevel)
					|| StringUtil.isBlank(startDate) || StringUtil.isBlank(endDate) ) {
				throw new BusinessException("CUS-00068");
			}*/
			if (StringUtil.isNotBlank(controlProjectNo)) {
				coreCustomerContrlViewSqlBuilder.andControlProjectNoEqualTo(controlProjectNo);
			}
			if (StringUtil.isNotBlank(contrlLevel)) {
				coreCustomerContrlViewSqlBuilder.andContrlLevelEqualTo(contrlLevel);
			}
			if (StringUtil.isNotBlank(levelCode)) {
				coreCustomerContrlViewSqlBuilder.andLevelCodeEqualTo(levelCode);
			}
			//查询开始日期，以及结束日期
			if (StringUtil.isNotBlank(startDate)) {
				coreCustomerContrlViewSqlBuilder.andContrlStartDateGreaterThanOrEqualTo(startDate);
			}
			if (StringUtil.isNotBlank(endDate)) {
				coreCustomerContrlViewSqlBuilder.andContrlEndDateLessThanOrEqualTo(endDate);
			}else {
				coreCustomerContrlViewSqlBuilder.andContrlEndDateIsNull();
			}
		}
		
		int totalCount = coreCustomerContrlViewDaoImpl.countBySqlBuilder(coreCustomerContrlViewSqlBuilder);
		page.setTotalCount(totalCount);

		if (null != X5265BO.getPageSize() && null != X5265BO.getIndexNo()) {
			coreCustomerContrlViewSqlBuilder.setPageSize(X5265BO.getPageSize());
			coreCustomerContrlViewSqlBuilder.setIndexNo(X5265BO.getIndexNo());
			page.setPageSize(X5265BO.getPageSize());
			page.setIndexNo(X5265BO.getIndexNo());
		}

		if (totalCount > 0) {
			List<CoreCustomerContrlView> list = coreCustomerContrlViewDaoImpl
					.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder);
			List<X5265VO> listX5265BO = new ArrayList<>();
			for (CoreCustomerContrlView coreCustomerContrlView : list) {
				X5265VO x5265VO = new X5265VO();
				CachedBeanCopy.copyProperties(coreCustomerContrlView, x5265VO);
				// CoreControlProjectSqlBuilder coreControlProjectSqlBuilder =
				// new CoreControlProjectSqlBuilder();
				// coreControlProjectSqlBuilder.andControlProjectNoEqualTo(coreCustomerContrlView.getControlProjectNo());
				// CoreControlProject coreControlProject =
				// coreControlProjectDao.selectBySqlBuilder(coreControlProjectSqlBuilder);
				Map<String, String> coreControlProjectMap = new HashMap<String, String>();
				coreControlProjectMap.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
				coreControlProjectMap.put(Constant.CONTROL_PROJECT_NO, coreCustomerContrlView.getControlProjectNo());
				coreControlProjectMap.put(Constant.PARAM_OPER_MODE, operationMode);
				String key = Constant.PARAMS_FLAG + operationMode + coreCustomerContrlView.getControlProjectNo();
				coreControlProjectMap.put(WSC.REDIS_KEY, key);
				CoreControlProject coreControlProject = betaCommonParamService
						.queryCoreControlProject(coreControlProjectMap);
				if (null != coreControlProject) {
					x5265VO.setControlDesc(coreControlProject.getControlDesc());
					x5265VO.setControlMode(coreControlProject.getControlMode());
				}
				if (coreCustomerContrlView.getContrlLevel().equals("M")) {
					CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
					coreMediaBasicInfoSqlBuilder.andMediaUnitCodeEqualTo(coreCustomerContrlView.getLevelCode());
					CoreMediaBasicInfo coreMediaBasicInfo1 = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
					if (null != coreMediaBasicInfo1) {
						x5265VO.setExternalIdentificationNo(coreMediaBasicInfo1.getExternalIdentificationNo());;
					}
				}
				if(StringUtil.isNotBlank(x5265VO.getCurrencyCode())){
                    CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5265VO.getCurrencyCode());
                    if(null != coreCurrency){
                        x5265VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
                    }
                }
				listX5265BO.add(x5265VO);
			}
			page.setRows(listX5265BO);
		}

		return page;
	}
}
