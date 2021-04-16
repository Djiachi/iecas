package com.tansun.ider.bus.impl;

import org.springframework.stereotype.Service;

import com.tansun.ider.bus.X5735Bus;
import com.tansun.ider.model.bo.X5735BO;
/**
 * 	附加表信息查询
 * 
 * @author yanyingzhao 2019年1月24日
 */
@Service
public class X5735BusImpl implements X5735Bus {
	
	
    @Override
    public Object busExecute(X5735BO x5735bo) throws Exception {
    	@SuppressWarnings("unused")
		String globalSerialNumber = x5735bo.getGlobalSerialNumber();
/*    	ClrClearingAdditionalSqlBuilder clrClearingAdditionalSqlBuilder = new ClrClearingAdditionalSqlBuilder();
    	 if (StringUtil.isNotBlank(globalSerialNumber)) {
    		 clrClearingAdditionalSqlBuilder.andGlobalSerialNumberEqualTo(globalSerialNumber);
    		 }
    	 List<ClrClearingAdditional> list = clrClearingAdditionalDao.selectListBySqlBuilder(clrClearingAdditionalSqlBuilder);*/
    	 return null;
    }

}



