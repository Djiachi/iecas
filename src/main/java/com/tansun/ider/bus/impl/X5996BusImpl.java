package com.tansun.ider.bus.impl;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5996Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreCustomerUnifyInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerUnifyInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5345BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 最新周期号客户统一日期查询
 * 
 * @author wt 2018年10月15日
 */
@Service
public class X5996BusImpl implements X5996Bus {

	@Autowired
	private CoreCustomerUnifyInfoDaoImpl coreCustomerUnifyInfoDaoImpl;

	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl;

	@Resource
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;
	
    @Autowired
    private HttpQueryService httpQueryService;

	@Override
	public Object busExecute(X5345BO x5345bo) throws Exception {
		PageBean<X5345BO> page = new PageBean<>();
		// 公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// // 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5345bo,eventCommAreaNonFinance);

		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		String idType = eventCommAreaNonFinance.getIdType();
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		// 客户代码
		String customerNo = eventCommAreaNonFinance.getCustomerNo();
		//业务项目代码
		String businessProgramNo = eventCommAreaNonFinance.getBusinessProgramNo();

		//业务项目中的周期号
		Integer currentCycleNumber = eventCommAreaNonFinance.getCycleNumber();

		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		CoreCustomer coreCustomer = null;
		if (StringUtil.isNotBlank(idNumber) && StringUtil.isNotBlank(idType)) {
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
			coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (null == coreCustomer) {
				throw new BusinessException("CUS-00014", "客户基本");
			} else {
				customerNo = coreCustomer.getCustomerNo();
			}
		}
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDaoImpl
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (null == coreMediaBasicInfo) {
				throw new BusinessException("CUS-00014", "媒介基本");
			} else {
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
			}
		}

		CoreCustomerUnifyInfoSqlBuilder coreCustomerUnifyInfoSqlBuilder = new CoreCustomerUnifyInfoSqlBuilder();
		coreCustomerUnifyInfoSqlBuilder.andCustomerNoEqualTo(customerNo);
		coreCustomerUnifyInfoSqlBuilder.andCycleNumberEqualTo(currentCycleNumber - 1);
		coreCustomerUnifyInfoSqlBuilder.andBusinessProgramNoEqualTo(businessProgramNo);
		int totalCount = coreCustomerUnifyInfoDaoImpl.countBySqlBuilder(coreCustomerUnifyInfoSqlBuilder);
		page.setTotalCount(totalCount);

		if (null != x5345bo.getPageSize() && null != x5345bo.getIndexNo()) {
			coreCustomerUnifyInfoSqlBuilder.setPageSize(x5345bo.getPageSize());
			coreCustomerUnifyInfoSqlBuilder.setIndexNo(x5345bo.getIndexNo());
			page.setPageSize(x5345bo.getPageSize());
			page.setIndexNo(x5345bo.getIndexNo());
		}
		
        List<X5345BO> listX5345BO = new ArrayList<>();
        String entrys = Constant.EMPTY_LIST;

		if (totalCount > 0) {
			List<CoreCustomerUnifyInfo> listCoreCustomerUnifyInfos = coreCustomerUnifyInfoDaoImpl
					.selectListBySqlBuilder(coreCustomerUnifyInfoSqlBuilder);
			for (CoreCustomerUnifyInfo coreCustomerUnifyInfo : listCoreCustomerUnifyInfos) {
				X5345BO x5345BO = new X5345BO();
				CachedBeanCopy.copyProperties(coreCustomerUnifyInfo,x5345BO);
				if (StringUtil.isNotBlank(coreCustomerUnifyInfo.getBusinessProgramNo())) {
					coreCustomerSqlBuilder.andCustomerNoEqualTo(coreCustomerUnifyInfo.getCustomerNo());
					coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
					if (null != coreCustomer) {
						String operationMode = coreCustomer.getOperationMode();
						CoreBusinessProgram coreBusinessProgram = httpQueryService
	                    		.queryBusinessProgram(operationMode, coreCustomerUnifyInfo.getBusinessProgramNo());
	                    if (coreBusinessProgram != null) {
	                    	x5345BO.setProgramDesc(coreBusinessProgram.getProgramDesc());
	                    }
					}
					
				}
				listX5345BO.add(x5345BO);
			}
			page.setRows(listX5345BO);

		}

		return page;
	}

}
