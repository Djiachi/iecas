package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5410Bus;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreParmHist;
import com.tansun.ider.dao.beta.entity.CoreUser;
import com.tansun.ider.dao.beta.impl.CoreParmHistDaoImpl;
import com.tansun.ider.dao.beta.sqlbuilder.CoreParmHistSqlBuilder;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreNmnyHist;
import com.tansun.ider.dao.issue.impl.CoreNmnyHistDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreNmnyHistSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5410BO;
import com.tansun.ider.service.BetaCommonParamService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.HttpQueryServiceImpl;
import com.tansun.ider.util.ParamsUtil;

/**
 * @version:1.0
 * @Description: 非金融维护历史查询
 * @author: admin
 */
@Service
public class X5410BusImpl implements  X5410Bus {
	
	@Autowired
	private CoreParmHistDaoImpl coreParmHistDaoImpl;
	@Autowired
	private CoreNmnyHistDaoImpl coreNmnyHistDaoImpl;
	@Autowired
	private ParamsUtil paramsUtil;
	@Autowired
	private BetaCommonParamService betaCommonParamService;
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Autowired
	private HttpQueryServiceImpl httpQueryServiceImpl;
	
	@Override
	public Object busExecute(X5410BO x5410bo) throws Exception {
		// 客户号、管控层级、起止日期
		// 从客户封锁码历史信息表中查询客户、账户、产品、媒介的封锁码维护记录详情；
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5410bo, eventCommAreaNonFinance);
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		//证件类型
		String idType = eventCommAreaNonFinance.getIdType();
		// 开始日期
		String startDate = eventCommAreaNonFinance.getStartDate();
		// 结束日期
		String endDate = eventCommAreaNonFinance.getEndDate();
		//日志层级
		String logLevel = eventCommAreaNonFinance.getLogLevel();
		//登录人员
		String operatorId = eventCommAreaNonFinance.getOperatorId();
		//管理员标识
		String adminFlag = this.queryAdminFlag(operatorId);
		//运营机构
		String organNo = eventCommAreaNonFinance.getOrganNo();
		
		String customerNo = null;
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		CoreCustomer coreCustomer = null;
		Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
		if(object instanceof CoreCustomer){
			 coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
		}else if(object instanceof CoreMediaBasicInfo){
			 coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			if(coreMediaBasicInfo.getMainCustomerNo()!=null){
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
			}
		}
		
		String entrys =Constant.EMPTY_LIST;
		
		if(!adminFlag.equals("1")){
			if(null != organNo){
				CoreOrgan coreOrgan = httpQueryServiceImpl.queryOrgan(organNo);
				if(null != coreOrgan){
					if (StringUtil.isNotBlank(logLevel)) {
						if(("R").equals(logLevel)){
							PageBean<CoreParmHist> page = new PageBean<>();
							CoreParmHistSqlBuilder coreParmHistSqlBuilder = new CoreParmHistSqlBuilder();
							coreParmHistSqlBuilder.andCustomerNoEqualTo(customerNo);
							coreParmHistSqlBuilder.andLogLevelEqualTo(logLevel);
							// operate_date 维护日期
							if (StringUtil.isNotBlank(startDate)) {
								coreParmHistSqlBuilder.andOccurrDateGreaterThan(startDate);
							}
							if (StringUtil.isNotBlank(endDate)) {
								coreParmHistSqlBuilder.andOccurrDateLessThan(endDate);
							}
							coreParmHistSqlBuilder.andModifyFieldNameNotEqualTo("version");
							String corporationEntityNo = coreOrgan.getCorporationEntityNo();
							List<CoreOrgan> list = httpQueryServiceImpl.queryOrganList1(corporationEntityNo);
							
							List<CoreUser> list1 = new ArrayList<CoreUser>();
							if(null != list){
								for(int i=0;i<list.size();i++){
									List<CoreUser> listCoreUser = httpQueryServiceImpl.queryCoreUserList(list.get(i).getOrganNo());
									list1.addAll(listCoreUser);
								}
							}
							if(null != list1){
								CoreParmHistSqlBuilder coreParmHistSqlBuilder1 = new CoreParmHistSqlBuilder();
								for(int i=0;i<list1.size();i++){
									coreParmHistSqlBuilder1.orOperatorIdEqualTo(list1.get(i).getLoginName());
								}
								coreParmHistSqlBuilder.and(coreParmHistSqlBuilder1);
							}
							int totalCount = coreParmHistDaoImpl.countBySqlBuilder(coreParmHistSqlBuilder);
							page.setTotalCount(totalCount);
							if (null != x5410bo.getPageSize() && null != x5410bo.getIndexNo()) {
								coreParmHistSqlBuilder.setPageSize(x5410bo.getPageSize());
								coreParmHistSqlBuilder.setIndexNo(x5410bo.getIndexNo());
								page.setPageSize(x5410bo.getPageSize());
								page.setIndexNo(x5410bo.getIndexNo());
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
								paramsUtil.logNonInsert(x5410bo.getCoreEventActivityRel().getEventNo(), x5410bo.getCoreEventActivityRel().getActivityNo(),
										tempObject, tempObject, entrys, x5410bo.getOperatorId());
							}
							return page;
						}
						else{
							PageBean<CoreNmnyHist> page = new PageBean<>();
							CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder = new CoreNmnyHistSqlBuilder();
							coreNmnyHistSqlBuilder.andCustomerNoEqualTo(customerNo);
							if(("M").equals(logLevel)){
								coreNmnyHistSqlBuilder.andLogLevelEqualTo("M");
								coreNmnyHistSqlBuilder.andActivityNoNotEqualTo("X5065");
								coreNmnyHistSqlBuilder.andActivityNoNotEqualTo("X5055");
								coreNmnyHistSqlBuilder.andActivityNoNotEqualTo("X5300");
							}else if(("M1").equals(logLevel)){
								coreNmnyHistSqlBuilder.andLogLevelEqualTo("M");
								coreNmnyHistSqlBuilder.andActivityNoEqualTo("X5065");
							}else if(("M2").equals(logLevel)){
								coreNmnyHistSqlBuilder.andLogLevelEqualTo("M");
								CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder1 = new CoreNmnyHistSqlBuilder();
								coreNmnyHistSqlBuilder.and(coreNmnyHistSqlBuilder1.orActivityNoEqualTo("X5300").orActivityNoEqualTo("X5055"));
							}
							// operate_date 维护日期
							if (StringUtil.isNotBlank(startDate)) {
								coreNmnyHistSqlBuilder.andOccurrDateGreaterThan(startDate);
							}
							if (StringUtil.isNotBlank(endDate)) {
								coreNmnyHistSqlBuilder.andOccurrDateLessThan(endDate);
							}
							coreNmnyHistSqlBuilder.andModifyFieldNameNotEqualTo("version");
							
							String corporationEntityNo = coreOrgan.getCorporationEntityNo();
							List<CoreOrgan> list = httpQueryServiceImpl.queryOrganList1(corporationEntityNo);
							
							List<CoreUser> list1 = new ArrayList<CoreUser>();
							if(null != list){
								for(int i=0;i<list.size();i++){
									List<CoreUser> listCoreUser = httpQueryServiceImpl.queryCoreUserList(list.get(i).getOrganNo());
									list1.addAll(listCoreUser);
								}
							}
							if(null != list1){
								CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder1 = new CoreNmnyHistSqlBuilder();
								for(int i=0;i<list1.size();i++){
									coreNmnyHistSqlBuilder1.orOperatorIdEqualTo(list1.get(i).getLoginName());
								}
								coreNmnyHistSqlBuilder.and(coreNmnyHistSqlBuilder1);
							}
							int totalCount = coreNmnyHistDaoImpl.countBySqlBuilder(coreNmnyHistSqlBuilder);
							page.setTotalCount(totalCount);
							if (null != x5410bo.getPageSize() && null != x5410bo.getIndexNo()) {
								coreNmnyHistSqlBuilder.setPageSize(x5410bo.getPageSize());
								coreNmnyHistSqlBuilder.setIndexNo(x5410bo.getIndexNo());
								page.setPageSize(x5410bo.getPageSize());
								page.setIndexNo(x5410bo.getIndexNo());
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
								paramsUtil.logNonInsert(x5410bo.getCoreEventActivityRel().getEventNo(), x5410bo.getCoreEventActivityRel().getActivityNo(),
										tempObject, tempObject, entrys, x5410bo.getOperatorId());
							}
							return page;
						}

					}
				}else{
					throw new BusinessException("CUS-00014", "运营机构表");
				}
			}
		}else{
			if (StringUtil.isNotBlank(logLevel)) {
				if(("R").equals(logLevel)){
					PageBean<CoreParmHist> page = new PageBean<>();
					CoreParmHistSqlBuilder coreParmHistSqlBuilder = new CoreParmHistSqlBuilder();
					coreParmHistSqlBuilder.andCustomerNoEqualTo(customerNo);
					coreParmHistSqlBuilder.andLogLevelEqualTo(logLevel);
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
					if (null != x5410bo.getPageSize() && null != x5410bo.getIndexNo()) {
						coreParmHistSqlBuilder.setPageSize(x5410bo.getPageSize());
						coreParmHistSqlBuilder.setIndexNo(x5410bo.getIndexNo());
						page.setPageSize(x5410bo.getPageSize());
						page.setIndexNo(x5410bo.getIndexNo());
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
						paramsUtil.logNonInsert(x5410bo.getCoreEventActivityRel().getEventNo(), x5410bo.getCoreEventActivityRel().getActivityNo(),
								tempObject, tempObject, entrys, x5410bo.getOperatorId());
						
						return page;
					}
				}else{
					PageBean<CoreNmnyHist> page = new PageBean<>();
					CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder = new CoreNmnyHistSqlBuilder();
					coreNmnyHistSqlBuilder.andCustomerNoEqualTo(customerNo);
					if(("M").equals(logLevel)){
						coreNmnyHistSqlBuilder.andLogLevelEqualTo("M");
						coreNmnyHistSqlBuilder.andActivityNoNotEqualTo("X5065");
						coreNmnyHistSqlBuilder.andActivityNoNotEqualTo("X5055");
						coreNmnyHistSqlBuilder.andActivityNoNotEqualTo("X5300");
					}else if(("M1").equals(logLevel)){
						coreNmnyHistSqlBuilder.andLogLevelEqualTo("M");
						coreNmnyHistSqlBuilder.andActivityNoEqualTo("X5065");
					}else if(("M2").equals(logLevel)){
						coreNmnyHistSqlBuilder.andLogLevelEqualTo("M");
						CoreNmnyHistSqlBuilder coreNmnyHistSqlBuilder1 = new CoreNmnyHistSqlBuilder();
						coreNmnyHistSqlBuilder.and(coreNmnyHistSqlBuilder1.orActivityNoEqualTo("X5300").orActivityNoEqualTo("X5055"));
					}else{
						coreNmnyHistSqlBuilder.andLogLevelEqualTo(logLevel);
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
					if (null != x5410bo.getPageSize() && null != x5410bo.getIndexNo()) {
						coreNmnyHistSqlBuilder.setPageSize(x5410bo.getPageSize());
						coreNmnyHistSqlBuilder.setIndexNo(x5410bo.getIndexNo());
						page.setPageSize(x5410bo.getPageSize());
						page.setIndexNo(x5410bo.getIndexNo());
					}
					
					if (totalCount > 0) {
						List<CoreNmnyHist> listCoreNmnyHist = coreNmnyHistDaoImpl
								.selectListBySqlBuilder(coreNmnyHistSqlBuilder);
						page.setRows(listCoreNmnyHist);
						if(null != listCoreNmnyHist && !listCoreNmnyHist.isEmpty()){
							entrys = listCoreNmnyHist.get(0).getId();
						}
						
						return page;
					}
					
				}
				
				
			}


		}

		return false;
	}
	
	public static void main(String[] args) {
		HashMap<Object, Object> arrya = new HashMap<>();
		arrya.put(null, 1);
		
		System.out.println(arrya.get(null).toString());
	}
	
	public String queryAdminFlag(String operatorId) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		String key = Constant.PARAMS_FLAG + operatorId;
		map.put("loginName", operatorId);
		map.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
		map.put("redisKey", key);
		CoreUser coreUser = betaCommonParamService.queryAdminFlag(map);
		String adminFlag = coreUser.getAdminFlag();
		if(adminFlag == null){
			throw new BusinessException("CUS-00014", "管理员标识");
		}
		return adminFlag;
		
	}
	
}







