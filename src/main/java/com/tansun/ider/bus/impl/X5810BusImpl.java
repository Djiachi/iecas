package com.tansun.ider.bus.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5810Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEffectivenessCode;
import com.tansun.ider.dao.beta.entity.CoreEvent;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.impl.CoreAccountDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreProductDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.AuthEventCommAreaNonFinanceBean;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5810BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.service.impl.QueryCustomerServiceImpl;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtil;
import com.tansun.ider.util.OperationModeUtil;

/**
 * 
 * @ClassName:  X5810BusImpl   
 * @Description:客户 产品注销 
 * @author: wangxi
 * @date:   2019年4月22日 下午8:27:56   
 *
 */
@Service
public class X5810BusImpl implements X5810Bus {

	@Autowired
	private CoreAccountDaoImpl coreAccountDaoImpl; //账户基本信息表
	@Autowired
	private CoreProductDaoImpl coreProductDaoImpl; //产品单元基本信息
	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl; //客户基本信息
	@Autowired
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl; //媒介单元基本信息
	@Autowired
	private QueryCustomerServiceImpl queryCustomerServiceImpl;
	@Autowired
	private HttpQueryService httpQueryService;
	@Resource
	private OperationModeUtil operationModeUtil;
	@Value("${global.target.service.nofn.SP012002}")
	private String spEventNo;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object busExecute(X5810BO x5810bo) throws Exception {
		// 事件公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		EventCommArea eventCommArea = new EventCommArea();//调用403构建所需
		// 构件清单
        List<CoreActivityArtifactRel> artifactList = x5810bo.getActivityArtifactList();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5810bo,eventCommAreaNonFinance);
 		
		//客户代码
		String customerNo = x5810bo.getCustomerNo();
		//产品对象代码
		String productObjectCode = x5810bo.getProductObjectCode();
		
		//因为在调用“根据403构件决定产品对象编码”方法所需传送运营模式参数，所以需要根据客户代码去查询客户信息表中运营模式[operation_mode]，因为它是唯一的。2019/05/21 add by wangxi
        CoreCustomerSqlBuilder coreCustomerSqlBuilderQuery = new CoreCustomerSqlBuilder();
        if (StringUtil.isNotBlank(customerNo)) {
            coreCustomerSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
        } 
        CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilderQuery);
        String operationMode = "";
        if(null != coreCustomer){
            operationMode = coreCustomer.getOperationMode();
        }
        eventCommArea.setEcommOperMode(operationMode);//运营模式
        
        
        /**
         * 根据产品对象代码去产品对象表中进行查询操作，得到该产品的套卡对方产品对象代码
         * add by wangxi 2019/8/9	只上POC
         */
        String reproductCodeSet = null;
        CoreProductObject coreProductObject = checkDate(operationMode,productObjectCode);
        //套卡对方产品对象代码
        String productCodeSet = coreProductObject.getProductCodeSet();
        x5810bo.setProductCodeSet(productCodeSet);//注销时也需要对套卡对方产品对象代码进行注销操作，所以在此需要把值传过去
        if(StringUtil.isNotBlank(productCodeSet)){
        	//判断套卡对方产品对象代码的产品类型：集中核算产品or独立核算产品
        	eventCommArea.setEcommProdObjId(productCodeSet);//套卡对方产品对象代码
        	reproductCodeSet = getProObjCode(eventCommArea, artifactList);//得到套卡对方产品对象代码的产品类型
        }
        
		// 根据403构件决定产品对象编码
        //判断产品对象代码的产品类型：集中核算产品or独立核算产品
        eventCommArea.setEcommProdObjId(productObjectCode);//产品对象代码
        String reproductObjectCode = getProObjCode(eventCommArea, artifactList);//得到产品对象代码的产品类型
       
        //客户产品在做注销操作之前，需要根据账户基本信息表内的信息做一些校验的判断依据。
        CoreAccountSqlBuilder coreAccountSqlBuilderQuery = new CoreAccountSqlBuilder();
        CoreAccountSqlBuilder coreAccountSqlBuilderStr = new CoreAccountSqlBuilder();//拼接or的关系-POC
        
        if (StringUtil.isNotBlank(customerNo) && StringUtil.isNotBlank(productObjectCode)) {//判断两个产品类型,做查询拼接条件-POC
        	if(StringUtil.isNotBlank(productCodeSet)){//当套卡对方产品对象代码不为空时,有4种情况   为0是集中核算产品类型
        		if("0".equals(reproductObjectCode) && "0".equals(reproductCodeSet)){//1. 产品对象代码和套卡对方产品对象代码都为0
        			coreAccountSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
                    coreAccountSqlBuilderQuery.andProductObjectCodeEqualTo("0");
        		}else if("0".equals(reproductObjectCode) && !"0".equals(reproductCodeSet)){//2. 产品对象代码为0,套卡对方产品对象代码不为0
        			coreAccountSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
        			coreAccountSqlBuilderStr.orProductObjectCodeEqualTo("0");
       	 			coreAccountSqlBuilderStr.orProductObjectCodeEqualTo(productCodeSet);
       	 			coreAccountSqlBuilderQuery.and(coreAccountSqlBuilderStr);//拼接SqlBuilder
        		}else if(!"0".equals(reproductObjectCode) && "0".equals(reproductCodeSet)){//3. 产品对象代码不为0,套卡对方产品对象代码为0
        			coreAccountSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
        			coreAccountSqlBuilderStr.orProductObjectCodeEqualTo(productObjectCode);
       	 			coreAccountSqlBuilderStr.orProductObjectCodeEqualTo("0");
       	 			coreAccountSqlBuilderQuery.and(coreAccountSqlBuilderStr);//拼接SqlBuilder
        		}else if(!"0".equals(reproductObjectCode) && !"0".equals(reproductCodeSet)){//4. 产品对象代码和套卡对方产品对象代码都不为0
        			coreAccountSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
        			coreAccountSqlBuilderStr.orProductObjectCodeEqualTo(productObjectCode);
       	 			coreAccountSqlBuilderStr.orProductObjectCodeEqualTo(productCodeSet);
       	 			coreAccountSqlBuilderQuery.and(coreAccountSqlBuilderStr);//拼接SqlBuilder
        		}
        		
       	 	}else{//原逻辑
       	 		//如果产品对象类型为集中核算产品对象类型，那么产品对象类型查询条件改为0	  add by wangxi 2019/6/15
       	 		if("0".equals(reproductObjectCode)){
       	 			coreAccountSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
       	 			coreAccountSqlBuilderQuery.andProductObjectCodeEqualTo("0");
       	 		}else{//反之，正常进行赋值操作
       	 			coreAccountSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
       	 			coreAccountSqlBuilderQuery.andProductObjectCodeEqualTo(productObjectCode);
       	 		}
       	 	}
        } 
        List<CoreAccount> coreAccountList = coreAccountDaoImpl.selectListBySqlBuilder(coreAccountSqlBuilderQuery);
        x5810bo.setCoreAccountList(coreAccountList);
        
        if(StringUtils.isNotBlank(reproductCodeSet)){//当套卡对方产品对象代码不为空时,有4种情况   为0是集中核算产品类型-POC
        	//0表示该产品类型为集中核算模式
        	if("0".equals(reproductObjectCode) && "0".equals(reproductCodeSet)){//1. 产品对象代码和套卡对方产品对象代码都为0
        		this.checkObjectCodeSet(x5810bo);//产品对象代码和套卡对方产品对象代码都走集中核算判断
    		}else if("0".equals(reproductObjectCode) && !"0".equals(reproductCodeSet)){//2. 产品对象代码为0,套卡对方产品对象代码不为0
    			this.checkReProductObjectCode(x5810bo);//产品对象代码走集中核算判断
    			//套卡对方产品对象代码走独立核算校验
        		queryCustomerServiceImpl.logoutCheckMode(customerNo,productCodeSet,coreAccountList,0,null);
    		}else if(!"0".equals(reproductObjectCode) && "0".equals(reproductCodeSet)){//3. 产品对象代码不为0,套卡对方产品对象代码为0
    			this.checkReProductCodeSet(x5810bo);//套卡对方产品对象代码走集中核算判断
    			//产品对象代码走独立核算校验
        		queryCustomerServiceImpl.logoutCheckMode(customerNo,productObjectCode,coreAccountList,0,null);
    		}else if(!"0".equals(reproductObjectCode) && !"0".equals(reproductCodeSet)){//4. 产品对象代码和套卡对方产品对象代码都不为0
    			// 产品对象代码独立核算产品检验
        		queryCustomerServiceImpl.logoutCheckMode(customerNo,productObjectCode,coreAccountList,0,null);
        		// 套卡对方产品对象代码独立核算产品检验
        		queryCustomerServiceImpl.logoutCheckMode(customerNo,productCodeSet,coreAccountList,0,null);
    		}
        	
        }else{//原逻辑
        	//当得到的产品类型为0时，那么该产品类型为集中核算产品
        	if(StringUtils.isNotBlank(reproductObjectCode) && reproductObjectCode.equals("0")){
        		//判断客户当前名下是否多于一个集中核算产品
        		CoreProductSqlBuilder coreProductSqlBuilderQList = new CoreProductSqlBuilder();
        		if (StringUtil.isNotBlank(customerNo)) {
        			coreProductSqlBuilderQList.andCustomerNoEqualTo(customerNo);
        		} 
        		List<CoreProduct> coreProductList = coreProductDaoImpl.selectListBySqlBuilder(coreProductSqlBuilderQList);
        		if (null != coreProductList && coreProductList.size() > 1){
        			int i=0;
        			for (CoreProduct coreProduct : coreProductList) {
        				//增加排除独立核算产品的状态查询，走403构件，如果不为0，则为独立核算产品，跳出，走下一个循环。   add by wangxi 2019/6/15
        				String pro = coreProduct.getProductObjectCode();
        				eventCommArea.setEcommProdObjId(pro);//产品对象代码
        				String repro = getProObjCode(eventCommArea, artifactList);//得到该产品的产品类型
        				if(!"0".equals(repro)){
        					continue;
        				}
        				
        				String statusCodepro = coreProduct.getStatusCode();//状态：1 正常,8关闭
        				if (statusCodepro.equals("1")){
        					i++;
        				}
        			}
        			if(i==0){
        				throw new BusinessException("CUS-00083", "产品状态已关闭");
        			}
        			if(i==1){
        				// 集中核算产品注销时检验方式,检核方式需将筛选的产品对象替换为0。
        				queryCustomerServiceImpl.logoutCheckMode(customerNo,productObjectCode,coreAccountList,1,null);
        			}
        		}else{
        			// 集中核算产品注销时检验方式,检核方式需将筛选的产品对象替换为0。
        			queryCustomerServiceImpl.logoutCheckMode(customerNo,productObjectCode,coreAccountList,1,null);
        		}
        	}else{
        		// 独立核算产品注销时检验方式
        		queryCustomerServiceImpl.logoutCheckMode(customerNo,productObjectCode,coreAccountList,0,null);
        	}
        	
        }
        
		// 注销操作 
		this.logout(x5810bo);
		
		//获取客户产品注销事件上封锁码的封锁码类别、封锁码场景序号
		CoreEvent coreEvent = httpQueryService.queryEvent("BSS.OP.01.0019");
        String blockCodeType = coreEvent.getEffectivenessCodeType();//生效码类别
        Integer blockCodeScene = coreEvent.getEffectivenessCodeScene();//生效码序号
        //查询封锁码表
        CoreEffectivenessCode coreEffectivenessCode =
                httpQueryService.queryEffectivenessCode(operationMode, blockCodeType, blockCodeScene + "");
		eventCommAreaNonFinance.setOperationMode(operationMode);//运营模式
		eventCommAreaNonFinance.setCustomerNo(customerNo);//客户号
		eventCommAreaNonFinance.setEffectivenessCodeType(blockCodeType);//封锁码类别
        eventCommAreaNonFinance.setEffectivenessCodeScene(blockCodeScene);//封锁码场景
        eventCommAreaNonFinance.setEffectivenessCodeScope(coreEffectivenessCode.getEffectivenessCodeScope());//生效码范围
		eventCommAreaNonFinance.setLevelCode(productObjectCode);//层级代码
		eventCommAreaNonFinance.setOperatorId(x5810bo.getOperatorId());//操作员
		eventCommAreaNonFinance.setSceneTriggerObject("P");//场景触发对象-必输
		//上封锁码传递事件号
		eventCommAreaNonFinance.setSpEventNo(spEventNo);
		
		/**
		 * 上封锁码：
		 * 活动触发事件表-客户产品注销事件不存在此表，因为只触发一个事件。
		 * 解决产品注销未上封锁码问题
		 * add by wangxi  2019/7/31
		 */
		eventCommAreaNonFinance.setWhetherProcess("");
		List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
		Map<String, Object> triggerEventParams = new HashMap<String, Object>();
		AuthEventCommAreaNonFinanceBean authEventCommAreaNonFinanceBean = new AuthEventCommAreaNonFinanceBean();
		CachedBeanCopy.copyProperties(eventCommAreaNonFinance,authEventCommAreaNonFinanceBean);
		authEventCommAreaNonFinanceBean.setAuthDataSynFlag("1");
		triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
		eventCommAreaTriggerEventList.add(triggerEventParams);
		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
		eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);
		
		return eventCommAreaNonFinance;
	}

	/**
	 * POC
	 * 产品对象代码-判断客户名下的核算产品
	 * @param x5810bo
	 * @throws Exception
	 */
	private void checkReProductObjectCode(X5810BO x5810bo) throws Exception {
		EventCommArea eventCommArea = new EventCommArea();//调用403构建所需
		// 构件清单
        List<CoreActivityArtifactRel> artifactList = x5810bo.getActivityArtifactList();
        //客户代码
  		String customerNo = x5810bo.getCustomerNo();
  		//产品对象代码
		String productObjectCode = x5810bo.getProductObjectCode();
  		
  		List<CoreAccount> coreAccountList = x5810bo.getCoreAccountList();
		//判断客户当前名下是否多于1个集中核算产品
		CoreProductSqlBuilder coreProductSqlBuilderQList = new CoreProductSqlBuilder();
		if (StringUtil.isNotBlank(customerNo)) {
			coreProductSqlBuilderQList.andCustomerNoEqualTo(customerNo);
		} 
		List<CoreProduct> coreProductList = coreProductDaoImpl.selectListBySqlBuilder(coreProductSqlBuilderQList);
		if (null != coreProductList && coreProductList.size() > 1){
			int i=0;
			for (CoreProduct coreProduct : coreProductList) {
				//增加排除独立核算产品的状态查询，走403构件，如果不为0，则为独立核算产品，跳出，走下一个循环
				String pro = coreProduct.getProductObjectCode();
				String ope = coreProduct.getOperationMode();
				eventCommArea.setEcommOperMode(ope);//运营模式
				eventCommArea.setEcommProdObjId(pro);//产品对象代码
				String repro = getProObjCode(eventCommArea, artifactList);//得到该产品的产品类型
				if(!"0".equals(repro)){
					continue;
				}
				
				String statusCodepro = coreProduct.getStatusCode();//状态：1 正常,8关闭
				if (statusCodepro.equals("1")){
					i++;
				}
			}
			if(i==0){
				throw new BusinessException("CUS-00083", "产品状态已关闭");
			}
			if(i==1){
				// 产品对象代码校验
				queryCustomerServiceImpl.logoutCheckMode(customerNo,productObjectCode,coreAccountList,1,null);
			}
		}else{
			// 产品对象代码校验
			queryCustomerServiceImpl.logoutCheckMode(customerNo,productObjectCode,coreAccountList,1,null);
		}
	}
	
	/**
	 * POC
	 * 套卡对方产品对象代码-判断客户名下的核算产品
	 * @param x5810bo
	 * @throws Exception
	 */
	private void checkReProductCodeSet(X5810BO x5810bo) throws Exception {
		EventCommArea eventCommArea = new EventCommArea();//调用403构建所需
		// 构件清单
        List<CoreActivityArtifactRel> artifactList = x5810bo.getActivityArtifactList();
        //客户代码
  		String customerNo = x5810bo.getCustomerNo();
  		//套卡对方产品对象代码
  		String productCodeSet = x5810bo.getProductCodeSet();
  		
  		List<CoreAccount> coreAccountList = x5810bo.getCoreAccountList();
		//判断客户当前名下是否多于一个集中核算产品
		CoreProductSqlBuilder coreProductSqlBuilderQList = new CoreProductSqlBuilder();
		if (StringUtil.isNotBlank(customerNo)) {
			coreProductSqlBuilderQList.andCustomerNoEqualTo(customerNo);
		} 
		List<CoreProduct> coreProductList = coreProductDaoImpl.selectListBySqlBuilder(coreProductSqlBuilderQList);
		if (null != coreProductList && coreProductList.size() > 1){
			int i=0;
			for (CoreProduct coreProduct : coreProductList) {
				//增加排除独立核算产品的状态查询，走403构件，如果不为0，则为独立核算产品，跳出，走下一个循环。   add by wangxi 2019/6/15
				String pro = coreProduct.getProductObjectCode();
				String ope = coreProduct.getOperationMode();
				eventCommArea.setEcommOperMode(ope);//运营模式
				eventCommArea.setEcommProdObjId(pro);//产品对象代码
				String repro = getProObjCode(eventCommArea, artifactList);//得到该产品的产品类型
				if(!"0".equals(repro)){
					continue;
				}
				
				String statusCodepro = coreProduct.getStatusCode();//状态：1 正常,8关闭
				if (statusCodepro.equals("1")){
					i++;
				}
			}
			if(i==0){
				throw new BusinessException("CUS-00083", "产品状态已关闭");
			}
			if(i==1){
				// 集中核算产品注销时检验方式,检核方式需将筛选的产品对象替换为0。
				queryCustomerServiceImpl.logoutCheckMode(customerNo,productCodeSet,coreAccountList,1,null);
			}
		}else{
			// 集中核算产品注销时检验方式,检核方式需将筛选的产品对象替换为0。
			queryCustomerServiceImpl.logoutCheckMode(customerNo,productCodeSet,coreAccountList,1,null);
		}
	}

	/**
	 * POC
	 * 产品对象代码和套卡对方产品对象都是集中核算产品
	 * 多余两条集中核算产品直接注销,校验两次,传值不同
	 * @param x5810bo
	 * @throws Exception
	 */
	private void checkObjectCodeSet(X5810BO x5810bo) throws Exception {
		EventCommArea eventCommArea = new EventCommArea();//调用403构建所需
		// 构件清单
        List<CoreActivityArtifactRel> artifactList = x5810bo.getActivityArtifactList();
        //客户代码
  		String customerNo = x5810bo.getCustomerNo();
  		//产品对象代码
		String productObjectCode = x5810bo.getProductObjectCode();
		//套卡产品对象代码
		String productCodeSet = x5810bo.getProductCodeSet();
  		
  		List<CoreAccount> coreAccountList = x5810bo.getCoreAccountList();
		//判断客户当前名下是否多于2个集中核算产品
		CoreProductSqlBuilder coreProductSqlBuilderQList = new CoreProductSqlBuilder();
		if (StringUtil.isNotBlank(customerNo)) {
			coreProductSqlBuilderQList.andCustomerNoEqualTo(customerNo);
		} 
		List<CoreProduct> coreProductList = coreProductDaoImpl.selectListBySqlBuilder(coreProductSqlBuilderQList);
		if (null != coreProductList && coreProductList.size() > 2){
			int i=0;
			for (CoreProduct coreProduct : coreProductList) {
				//增加排除独立核算产品的状态查询，走403构件，如果不为0，则为独立核算产品，跳出，走下一个循环
				String pro = coreProduct.getProductObjectCode();
				String ope = coreProduct.getOperationMode();
				eventCommArea.setEcommOperMode(ope);//运营模式
				eventCommArea.setEcommProdObjId(pro);//产品对象代码
				String repro = getProObjCode(eventCommArea, artifactList);//得到该产品的产品类型
				if(!"0".equals(repro)){
					continue;
				}
				
				String statusCodepro = coreProduct.getStatusCode();//状态：1 正常,8关闭
				if (statusCodepro.equals("1")){
					i++;
				}
			}
			if(i==0){
				throw new BusinessException("CUS-00083", "产品状态已关闭");
			}
			if(i==2){
				// 产品对象代码校验
				queryCustomerServiceImpl.logoutCheckMode(customerNo,productObjectCode,coreAccountList,1,null);
				// 套卡对方产品对象代码校验
				queryCustomerServiceImpl.logoutCheckMode(customerNo,productCodeSet,coreAccountList,1,null);
			}
		}else{
			// 产品对象代码校验
			queryCustomerServiceImpl.logoutCheckMode(customerNo,productObjectCode,coreAccountList,1,null);
			// 套卡对方产品对象代码校验
			queryCustomerServiceImpl.logoutCheckMode(customerNo,productCodeSet,coreAccountList,1,null);
		}
	}
	
	/**
	 * 
	 * @Title: logout   
	 * @Description: 注销校验结束后进行的更改状态操作
	 * @param: @param x5810bo
	 * @param: @throws Exception      
	 * @return: void      
	 * @throws
	 */
	private void logout(X5810BO x5810bo) throws Exception {
		//客户代码
		String customerNo = x5810bo.getCustomerNo();
		String productObjectCode = x5810bo.getProductObjectCode();
		String productCodeSet = x5810bo.getProductCodeSet();
		
		//注销后产品状态更新为5-注销
		CoreProductSqlBuilder coreProductSqlBuilderQuery = new CoreProductSqlBuilder();
		CoreProductSqlBuilder coreProductSqlBuilderStr = new CoreProductSqlBuilder();
		if (StringUtil.isNotBlank(customerNo)) {
			coreProductSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
		} 
		if (StringUtil.isNotBlank(productCodeSet)) {
			coreProductSqlBuilderStr.orProductObjectCodeEqualTo(productObjectCode).orProductObjectCodeEqualTo(productCodeSet);
		} else {
			coreProductSqlBuilderStr.andProductObjectCodeEqualTo(productObjectCode);
		}
		
		coreProductSqlBuilderQuery.and(coreProductSqlBuilderStr);
		
		//先查询出对应的产品信息
		List<CoreProduct> coreProductList = coreProductDaoImpl.selectListBySqlBuilder(coreProductSqlBuilderQuery);
		//如果根据查出的产品对象不为空，则修改产品状态
		if (null != coreProductList && coreProductList.size() > 0){
			for (CoreProduct coreProduct : coreProductList) {
				coreProduct.setStatusCode("5");//注销后产品状态更新为5-注销
				//获取系统单元处理日期
				CoreSystemUnit systemUnit = operationModeUtil.getcoreOperationMode(customerNo);
				String currProcessDate = "";
				if(systemUnit!=null){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if("EOD".equals(systemUnit.getSystemOperateState())){
						currProcessDate = systemUnit.getCurrProcessDate();
					}else if("NOR".equals(systemUnit.getSystemOperateState())){
						currProcessDate = systemUnit.getNextProcessDate();
					}else{
						throw new BusinessException("该系统单元为0");
					}
				}else{
					throw new BusinessException("无法获取系统单元");
				}
				coreProduct.setProductCancelDate(currProcessDate);//产品注销日期为系统处理日期
				
				int updateByPrimaryKey = coreProductDaoImpl.updateByPrimaryKey(coreProduct);
				//返回值为0时更新失败，为1时更新成功
				if (updateByPrimaryKey != 1){
					throw new BusinessException("CUS-00083","更新产品状态失败");
				}
			}
		}
		
		//该产品下所有媒介更新为无效，无效原因为CAN(cancel)
		CoreMediaBasicInfoSqlBuilder CoreMediaBasicInfoSqlBuilderQuery = new CoreMediaBasicInfoSqlBuilder();
		CoreMediaBasicInfoSqlBuilder CoreMediaBasicInfoSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
		if (StringUtil.isNotBlank(customerNo)){
			CoreMediaBasicInfoSqlBuilderQuery.andMainCustomerNoEqualTo(customerNo);
		}
		if (StringUtil.isNotBlank(productCodeSet)){
			CoreMediaBasicInfoSqlBuilderStr.orProductObjectCodeEqualTo(productObjectCode).orProductObjectCodeEqualTo(productCodeSet);
		}else{
			CoreMediaBasicInfoSqlBuilderStr.andProductObjectCodeEqualTo(productObjectCode);
		}
		
		CoreMediaBasicInfoSqlBuilderQuery.and(CoreMediaBasicInfoSqlBuilderStr);
		
		//先查出对应的媒介信息
		List<CoreMediaBasicInfo> coreMediaBasicInfoList = coreMediaBasicInfoDaoImpl.selectListBySqlBuilder(CoreMediaBasicInfoSqlBuilderQuery);
		//如果媒介信息结果不为空，则修改媒介状态
		if (null != coreMediaBasicInfoList && coreMediaBasicInfoList.size() > 0){
		    for (CoreMediaBasicInfo coreMediaBasicInfoUpd : coreMediaBasicInfoList) {
		    	coreMediaBasicInfoUpd.setInvalidFlag("N");//是否有效标识 Y有效 N无效
		        coreMediaBasicInfoUpd.setInvalidReason("CAN");
		        int updateByPrimaryKey = coreMediaBasicInfoDaoImpl.updateByPrimaryKey(coreMediaBasicInfoUpd);
		        if (updateByPrimaryKey != 1){
	                throw new BusinessException("CUS-00083","更新媒介状态失败");
	            }
            }
		}
		
	}

	
    /**
     * 根据403构件决定产品对象编码
     * 
     * @param eventCommArea
     * @param artifactList
     * @return
     * @throws Exception
     */
	private String getProObjCode(EventCommArea eventCommArea, List<CoreActivityArtifactRel> artifactList) throws Exception {
        String productOjectCode = "";
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtil.checkArtifactExist(BSC.ARTIFACT_NO_403, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_403, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.PRODUCT_BUSINESS_ACCOUNTS.equals(entry.getKey())) {
                // 单独核算
                productOjectCode = eventCommArea.getEcommProdObjId();
            } else {
                // 集中核算，产品对象置空
                productOjectCode = "0";
            }
        }
        return productOjectCode;
    }

	/**
     * 检查产品对象代码
     * @param eventCommAreaNonFinance
     * @throws Exception
     */
    private CoreProductObject checkDate(String operationMode, String productObjectCode) throws Exception {
   	 CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode,productObjectCode);
   	        if (coreProductObject == null) {
   	            throw new BusinessException("CUS-00014", "产品对象表"); // 产品对象表
   	        }
   	        return coreProductObject;
	}
	
}
