package com.tansun.ider.bus.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5450Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreParmHist;
import com.tansun.ider.dao.beta.entity.CoreUser;
import com.tansun.ider.dao.beta.impl.CoreParmHistDaoImpl;
import com.tansun.ider.dao.beta.impl.CoreUserDaoImpl;
import com.tansun.ider.dao.beta.sqlbuilder.CoreParmHistSqlBuilder;
import com.tansun.ider.dao.beta.sqlbuilder.CoreUserSqlBuilder;
import com.tansun.ider.dao.issue.entity.CoreNmnyHist;
import com.tansun.ider.dao.issue.impl.CoreNmnyHistDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreNmnyHistSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5450BO;
import com.tansun.ider.service.BetaCommonParamService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.ParamsUtil;

/**
 * @version:1.0
 * @Description: 用户访问历史查询
 * @author: admin
 */
@Service
public class X5450BusImpl implements  X5450Bus {
	
	@Autowired
	private CoreUserDaoImpl coreUserDaoImpl;
	
	@Autowired
	private CoreNmnyHistDaoImpl coreNmnyHistDaoImpl;
	
	@Autowired
	private CoreParmHistDaoImpl coreParmHistDaoImpl;
	
	@Autowired
	private ParamsUtil paramsUtil;
	
	@Autowired
	private BetaCommonParamService betaCommonParamService;
	
	@Override
	public Object busExecute(X5450BO x5450bo) throws Exception {
		// 客户号、管控层级、起止日期
		// 从客户封锁码历史信息表中查询客户、账户、产品、媒介的封锁码维护记录详情；
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5450bo, eventCommAreaNonFinance);
		//用户
		String customerNo = eventCommAreaNonFinance.getCustomerNo();
		// 开始日期
		String startDate = eventCommAreaNonFinance.getStartDate();
		// 结束日期
		String endDate = eventCommAreaNonFinance.getEndDate();
		//访问层级
		String logLevel = eventCommAreaNonFinance.getLogLevel();
		//访问类别
		String modifyType = eventCommAreaNonFinance.getModifyType();
		//判断用户是否存在
		this.queryUser(customerNo);
		
		String entrys =Constant.EMPTY_LIST;
		if(("R").equals(logLevel)){
			PageBean<CoreParmHist> page = new PageBean<>();
			CoreParmHistSqlBuilder coreParmHistSqlBuilder = new CoreParmHistSqlBuilder();
			if (StringUtil.isNotBlank(customerNo)) {
				coreParmHistSqlBuilder.andOperatorIdEqualTo(customerNo);
			}
			if (StringUtil.isNotBlank(logLevel)) {
				coreParmHistSqlBuilder.andLogLevelEqualTo(logLevel);
			}
			if (StringUtil.isNotBlank(modifyType)) {
				coreParmHistSqlBuilder.andModifyTypeEqualTo(modifyType);
			}
			// operate_date 维护日期
			if (StringUtil.isNotBlank(startDate)) {
				coreParmHistSqlBuilder.andOccurrDateGreaterThan(startDate);
			}
			if (StringUtil.isNotBlank(endDate)) {
				coreParmHistSqlBuilder.andOccurrDateLessThan(endDate);
			}
			coreParmHistSqlBuilder.andModifyFieldNameNotEqualTo("version");
			int totalCount = coreParmHistDaoImpl.countBySqlBuilder(coreParmHistSqlBuilder);
			page.setTotalCount(totalCount);
			if (null != x5450bo.getPageSize() && null != x5450bo.getIndexNo()) {
				coreParmHistSqlBuilder.setPageSize(x5450bo.getPageSize());
				coreParmHistSqlBuilder.setIndexNo(x5450bo.getIndexNo());
				page.setPageSize(x5450bo.getPageSize());
				page.setIndexNo(x5450bo.getIndexNo());
			}
			if (totalCount > 0) {
				List<CoreParmHist> listCoreParmHist = coreParmHistDaoImpl
						.selectListBySqlBuilder(coreParmHistSqlBuilder);
				page.setRows(listCoreParmHist);
				if(null != listCoreParmHist && !listCoreParmHist.isEmpty()){
					entrys = listCoreParmHist.get(0).getId();
				}
				//记录查询日志
				CoreEvent tempObject = new CoreEvent();
				paramsUtil.logNonInsert(x5450bo.getCoreEventActivityRel().getEventNo(), x5450bo.getCoreEventActivityRel().getActivityNo(),
						tempObject, tempObject, entrys, x5450bo.getOperatorId());
			}
			return page;
			
		}else{
			PageBean<CoreNmnyHist> page = new PageBean<>();
			CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder = new CoreNmnyHistSqlBuilder();
			if (StringUtil.isNotBlank(customerNo)) {
				coreNmnyHistSqlBuilder.andOperatorIdEqualTo(customerNo);
			}
			if (StringUtil.isNotBlank(logLevel)) {
				coreNmnyHistSqlBuilder.andLogLevelEqualTo(logLevel);
			}
			if (StringUtil.isNotBlank(modifyType)) {
				coreNmnyHistSqlBuilder.andModifyTypeEqualTo(modifyType);
			}
			// operate_date 维护日期
			if (StringUtil.isNotBlank(startDate)) {
				coreNmnyHistSqlBuilder.andOccurrDateGreaterThan(startDate);
			}
			if (StringUtil.isNotBlank(endDate)) {
				coreNmnyHistSqlBuilder.andOccurrDateLessThan(endDate);
			}
			coreNmnyHistSqlBuilder.andModifyFieldNameNotEqualTo("version");
			int totalCount = coreNmnyHistDaoImpl.countBySqlBuilder(coreNmnyHistSqlBuilder);
			page.setTotalCount(totalCount);
			if (null != x5450bo.getPageSize() && null != x5450bo.getIndexNo()) {
				coreNmnyHistSqlBuilder.setPageSize(x5450bo.getPageSize());
				coreNmnyHistSqlBuilder.setIndexNo(x5450bo.getIndexNo());
				page.setPageSize(x5450bo.getPageSize());
				page.setIndexNo(x5450bo.getIndexNo());
			}
			if (totalCount > 0) {
				List<CoreNmnyHist> listCoreNmnyHist = coreNmnyHistDaoImpl
						.selectListBySqlBuilder(coreNmnyHistSqlBuilder);
				page.setRows(listCoreNmnyHist);
				if(null != listCoreNmnyHist && !listCoreNmnyHist.isEmpty()){
					entrys = listCoreNmnyHist.get(0).getId();
				}
				//记录查询日志
				CoreEvent tempObject = new CoreEvent();
				paramsUtil.logNonInsert(x5450bo.getCoreEventActivityRel().getEventNo(), x5450bo.getCoreEventActivityRel().getActivityNo(),
						tempObject, tempObject, entrys, x5450bo.getOperatorId());
			}
			return page;
			
		}
		
	}
	
    public String queryUser(String customerNo) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
		String key = Constant.PARAMS_FLAG + customerNo;
		map.put("loginName", customerNo);
		map.put("redisKey", key);
		map.put("requestType", "1");
        CoreUser coreUser = betaCommonParamService.queryCoreUser(map);
        if (coreUser == null) {
            throw new BusinessException("CUS-00014", "用户" + customerNo);
        }
        return coreUser.getOrganization();
    }

}
