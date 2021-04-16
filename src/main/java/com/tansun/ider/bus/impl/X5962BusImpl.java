package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5962Bus;
import com.tansun.ider.dao.issue.CoreCustomerWaiveFeeInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomerWaiveFeeInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerWaiveFeeInfoSqlBuilder;
import com.tansun.ider.model.bo.X5962BO;
import com.tansun.ider.util.ClassUtil;
/**
 * publicservice工程修改发卡数据库统一接口
 * @author lianhuan
 * 2019年3月12日
 */
@Service
public class X5962BusImpl implements X5962Bus {
	
	@Autowired
    private CoreCustomerWaiveFeeInfoDao coreCustomerWaiveFeeInfoDao;

    @Override
    public Object updateCoreCustomerWaiveFeeInfo(X5962BO x5962bo) throws Exception {
    	
		CoreCustomerWaiveFeeInfoSqlBuilder coreCustomerWaiveFeeInfoSqlBuilder = new CoreCustomerWaiveFeeInfoSqlBuilder();
		coreCustomerWaiveFeeInfoSqlBuilder.andCustomerNoEqualTo(x5962bo.getCustomerNo());
		coreCustomerWaiveFeeInfoSqlBuilder.andCurrencyCodeEqualTo(x5962bo.getCurrencyCode());
		coreCustomerWaiveFeeInfoSqlBuilder.andFeeItemNoEqualTo(x5962bo.getFeeItemNo());
		CoreCustomerWaiveFeeInfoSqlBuilder tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
        if (StringUtil.isNotBlank(x5962bo.getInstanCode1())) {
            coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode1EqualTo(x5962bo.getInstanCode1());
        }else{
            tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
            tempSqlBui.orInstanCode1EqualTo("");
            tempSqlBui.orInstanCode1IsNull();
            coreCustomerWaiveFeeInfoSqlBuilder.and(tempSqlBui);
        }
        if (StringUtil.isNotBlank(x5962bo.getInstanCode2())) {
            coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode2EqualTo(x5962bo.getInstanCode2());
        }else{
            tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
            tempSqlBui.orInstanCode2EqualTo("");
            tempSqlBui.orInstanCode2IsNull();
            coreCustomerWaiveFeeInfoSqlBuilder.and(tempSqlBui);
        }
        if (StringUtil.isNotBlank(x5962bo.getInstanCode3())) {
            coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode3EqualTo(x5962bo.getInstanCode3());
        }else{
            tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
            tempSqlBui.orInstanCode3EqualTo("");
            tempSqlBui.orInstanCode3IsNull();
            coreCustomerWaiveFeeInfoSqlBuilder.and(tempSqlBui);
        }
        if (StringUtil.isNotBlank(x5962bo.getInstanCode4())) {
            coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode4EqualTo(x5962bo.getInstanCode4());
        }else{
            tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
            tempSqlBui.orInstanCode4EqualTo("");
            tempSqlBui.orInstanCode4IsNull();
            coreCustomerWaiveFeeInfoSqlBuilder.and(tempSqlBui);
        }
        if (StringUtil.isNotBlank(x5962bo.getInstanCode5())) {
            coreCustomerWaiveFeeInfoSqlBuilder.andInstanCode5EqualTo(x5962bo.getInstanCode5());
        }else{
            tempSqlBui = new CoreCustomerWaiveFeeInfoSqlBuilder();
            tempSqlBui.orInstanCode5EqualTo("");
            tempSqlBui.orInstanCode5IsNull();
            coreCustomerWaiveFeeInfoSqlBuilder.and(tempSqlBui);
        }
		CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfoNew = coreCustomerWaiveFeeInfoDao.selectBySqlBuilder(coreCustomerWaiveFeeInfoSqlBuilder);
		CoreCustomerWaiveFeeInfo coreCustomerWaiveFeeInfoOld = new CoreCustomerWaiveFeeInfo();
		CachedBeanCopy.copyProperties(coreCustomerWaiveFeeInfoNew, coreCustomerWaiveFeeInfoOld);
		CachedBeanCopy.copyProperties(x5962bo, coreCustomerWaiveFeeInfoNew);

		coreCustomerWaiveFeeInfoSqlBuilder.andVersionEqualTo(coreCustomerWaiveFeeInfoNew.getVersion());
		coreCustomerWaiveFeeInfoNew.setVersion(coreCustomerWaiveFeeInfoNew.getVersion() + 1);
		coreCustomerWaiveFeeInfoNew = (CoreCustomerWaiveFeeInfo) ClassUtil.getReflectObjectTransString(coreCustomerWaiveFeeInfoNew);
		int i  = coreCustomerWaiveFeeInfoDao.updateBySqlBuilderSelective(coreCustomerWaiveFeeInfoNew, coreCustomerWaiveFeeInfoSqlBuilder);
		return "OK";
    }
}
