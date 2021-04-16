package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.bus.X5961Bus;
import com.tansun.ider.dao.issue.CoreCustomerWaiveFeeInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerWaiveFeeInfo;
import com.tansun.ider.model.bo.X5961BO;
/**
 * publicservice工程插入发卡数据库统一接口
 * @author lianhuan
 * 2019年3月12日
 */
@Service
public class X5961BusImpl implements X5961Bus {

	@Autowired
    private CoreCustomerWaiveFeeInfoDao coreCustomerWaiveFeeInfoDao;
	
    @Override
    public Object insertCoreCustomerWaiveFeeInfo(X5961BO x5961bo) throws Exception {
    	CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfo = new CoreCustomerWaiveFeeInfo();
		CachedBeanCopy.copyProperties(x5961bo, coreCustomerWaiveFeeInfo);
    	int i = coreCustomerWaiveFeeInfoDao.insert(coreCustomerWaiveFeeInfo);
        return i;
    }
    
}
