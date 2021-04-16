package com.tansun.ider.bus.impl;

import java.util.HashMap;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X4055Bus;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X4055BO;
import com.tansun.ider.model.bo.X5035BO;
import com.tansun.ider.model.bo.X5755BO;
import com.tansun.ider.web.WSC;
/**
 * 公务卡注销停用查询
 * @Description:TODO()   
 * @author: sunyaoyao
 * @date:   2019年5月22日 上午9:45:09   
 *
 */
@Service
public class X4055BusImpl implements X4055Bus{
	@Override
	public Object busExecute(X4055BO x4055bo) throws Exception {
		String externalIdentificationNo = x4055bo.getExternalIdentificationNo();
		String budgetOrgCode = x4055bo.getIdNumber();
		if(StringUtil.isNotBlank(externalIdentificationNo)){
			return exNoBusExecute(x4055bo);
		}else if(StringUtil.isNotBlank(budgetOrgCode)){
			return budgetOrgCodeExecute(x4055bo);
		}else{
			throw new BusinessException("获取参数为空！");
		}
	}
	
	/**
	 * 5155逻辑
	 * @Description: TODO()   
	 * @param: @param x4055bo
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: Object      
	 * @throws
	 */
	private Object budgetOrgCodeExecute(X4055BO x4055bo) throws Exception{
		// 5.4 调用活动
        ActionService coreService = (ActionService) SpringUtil.getBean("X5155");
        // 内部方法转换为JSON后进行传递
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        X5755BO x5755BO = new X5755BO();
        CachedBeanCopy.copyProperties(x4055bo, x5755BO);
        x5755BO.setTansferFind("1");
        paramsMap.put(WSC.EVENT_PUBLIC_DATA_AREA_KEY, JSON.toJSONString(x5755BO));
        return coreService.execute(paramsMap);
	}
	/**
	 * 5305逻辑
	 * @Description: TODO()   
	 * @param: @param x4055bo
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: Object      
	 * @throws
	 */
	private Object exNoBusExecute(X4055BO x4055bo) throws Exception{
		if(x4055bo.getPageSize()==null){
			x4055bo.setPageSize(10);
		}
		if(x4055bo.getIndexNo()==null){
			x4055bo.setIndexNo(0);
		}
		// 5.4 调用活动
        ActionService coreService = (ActionService) SpringUtil.getBean("X5305");
        // 内部方法转换为JSON后进行传递
        HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        X5035BO x5035BO = new X5035BO();
        CachedBeanCopy.copyProperties(x4055bo, x5035BO);
        paramsMap.put(WSC.EVENT_PUBLIC_DATA_AREA_KEY, JSON.toJSONString(x5035BO));
        //提供无效卡入口
        if(x4055bo.getEffective()!=null){
        	paramsMap.put(WSC.EVENT_ID, "BSS.IQ.01.0017-");//查询无效卡
        }
        paramsMap.put(WSC.EVENT_ID, "BSS.IQ.01.0007-");//查询有效卡
        return coreService.execute(paramsMap);
	}
}
