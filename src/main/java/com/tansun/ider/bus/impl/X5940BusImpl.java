package com.tansun.ider.bus.impl;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.bus.X5940Bus;
import com.tansun.ider.dao.beta.CoreSecondRecogHistDao;
import com.tansun.ider.dao.beta.CoreSecondRecogParamDao;
import com.tansun.ider.dao.beta.entity.CoreSecondRecogHist;
import com.tansun.ider.dao.beta.entity.CoreSecondRecogParam;
import com.tansun.ider.dao.beta.entity.CoreSecondRecogParamKey;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5940BO;

/**
 * 交易二次识别参数删除
 * 
 * @author lianhuan 2018年9月14日
 */
@Service
public class X5940BusImpl implements X5940Bus {
    @Resource
    private CoreSecondRecogParamDao coreSecondRecogParamDao;
    @Resource
    private CoreSecondRecogHistDao coreSecondRecogHistDao;

    @Override
    public Object busExecute(X5940BO x5940bo) throws Exception {
        String secondRecogId = x5940bo.getSecondRecogId();
        CoreSecondRecogParamKey coreSecondRecogParamKey = new CoreSecondRecogParamKey(secondRecogId);
        CoreSecondRecogParam coreSecondRecogParam = coreSecondRecogParamDao.selectByPrimaryKey(coreSecondRecogParamKey);
        if (coreSecondRecogParam == null) {
            throw new BusinessException("CUS-00014", "二次识别参数id=" + secondRecogId);
        }
        // 存入二次识别参数历史表中
        CoreSecondRecogHist coreSecondRecogHist = new CoreSecondRecogHist();
        CachedBeanCopy.copyProperties(coreSecondRecogParam, coreSecondRecogHist);
        coreSecondRecogHist.setId(RandomUtil.getUUID());
        coreSecondRecogHist.setVersion(1);

        coreSecondRecogHistDao.insert(coreSecondRecogHist);
        // 从二次识别参数表中删除
        coreSecondRecogParamDao.deleteByPrimaryKey(coreSecondRecogParamKey);
        return null;
    }

}
