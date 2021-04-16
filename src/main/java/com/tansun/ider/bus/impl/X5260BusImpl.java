package com.tansun.ider.bus.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5260Bus;
import com.tansun.ider.dao.issue.CoreAccountBalanceObjectDao;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreBalanceUnitDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreAccountBalanceObject;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountBalanceObjectSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5260BO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;

/**
 * @Desc:统一利率
 * @Author yuanyanjiao
 * @Date 2018年9月28日 上午9:57:18
 */
@Service
public class X5260BusImpl implements X5260Bus {

    @Resource
    private CoreAccountDao coreAccountDao;
    @Resource
    private CoreAccountBalanceObjectDao coreAccountBalanceObjectDao;
    @Resource
    private CoreBalanceUnitDao coreBalanceUnitDao;
    
    @Transactional
    public Object busExecute(X5260BO x5260bo) throws Exception {
        updateInterestRate(x5260bo);
        return "OK";
    }

    /**
     * 更新利率信息
     */
    private void updateInterestRate(X5260BO x5260bo) throws Exception {
        CoreAccountBalanceObjectSqlBuilder coreAccountBalanceObjectSqlBuilder = new CoreAccountBalanceObjectSqlBuilder();
        coreAccountBalanceObjectSqlBuilder.andAccountIdEqualTo(x5260bo.getAccountId());
        coreAccountBalanceObjectSqlBuilder.andCurrencyCodeEqualTo(x5260bo.getCurrencyCode());
        List<CoreAccountBalanceObject> list = coreAccountBalanceObjectDao.selectListBySqlBuilder(coreAccountBalanceObjectSqlBuilder);
        if(list!=null && list.size()>0){
            //查询账户基本信息获取业务类型
            CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
            coreAccountSqlBuilder.andAccountIdEqualTo(x5260bo.getAccountId());
            coreAccountSqlBuilder.andCurrencyCodeEqualTo(x5260bo.getCurrencyCode());
            CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
            CoreAccountBalanceObjectSqlBuilder balanceObjectSqlBuilder = null;
            CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilder = null;
            CoreBalanceUnitSqlBuilder balanceUnitSqlBuilder = null;
            for(CoreAccountBalanceObject obj:list){
                //余额对象代码
                EventCommArea eventCommArea=new EventCommArea();
                eventCommArea.setEcommOperMode(coreAccount.getOperationMode());
                eventCommArea.setEcommBcoCode(obj.getBalanceObjectCode());
                eventCommArea.setEcommBusineseType(coreAccount.getBusinessTypeCode());
                String bcuDimension = queryBcuDimension(BSC.ARTIFACT_NO_808, eventCommArea);
                if(StringUtil.isNotBlank(bcuDimension)){
                    //更新账户余额对象信息
                    balanceObjectSqlBuilder = new CoreAccountBalanceObjectSqlBuilder();
                    balanceObjectSqlBuilder.andIdEqualTo(obj.getId());
                    balanceObjectSqlBuilder.andVersionEqualTo(obj.getVersion());
                    obj.setInterestRate(x5260bo.getInterestRate());
                    obj.setVersion(obj.getVersion()+1);
                    int result = coreAccountBalanceObjectDao.updateBySqlBuilderSelective(obj, balanceObjectSqlBuilder);
                    if (result != 1) {
                        throw new BusinessException("COR-10006");
                    }
                    //更新余额单元信息
                    coreBalanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
                    coreBalanceUnitSqlBuilder.andAccountIdEqualTo(x5260bo.getAccountId());
                    coreBalanceUnitSqlBuilder.andCurrencyCodeEqualTo(x5260bo.getCurrencyCode());
                    coreBalanceUnitSqlBuilder.andBalanceObjectCodeEqualTo(obj.getBalanceObjectCode());
                    List<CoreBalanceUnit> units = coreBalanceUnitDao.selectListBySqlBuilder(coreBalanceUnitSqlBuilder);
                    if(units!=null && units.size()>0){
                        for(CoreBalanceUnit coreBalanceUnit:units){
                            balanceUnitSqlBuilder = new CoreBalanceUnitSqlBuilder();
                            balanceUnitSqlBuilder.andIdEqualTo(coreBalanceUnit.getId());
                            balanceUnitSqlBuilder.andVersionEqualTo(coreBalanceUnit.getVersion());
                            coreBalanceUnit.setVersion(coreBalanceUnit.getVersion()+1);
                            coreBalanceUnit.setLastInterestRate(coreBalanceUnit.getAnnualInterestRate());
                            coreBalanceUnit.setAnnualInterestRate(x5260bo.getInterestRate());
                            coreBalanceUnit.setLastInterestRateExpirDate(DateUtil.format(null, DateUtil.FORMAT_DATE));
                            int count = coreBalanceUnitDao.updateBySqlBuilderSelective(coreBalanceUnit, balanceUnitSqlBuilder);
                            if(count!=1){
                                throw new BusinessException("COR-10006");
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 查询808元件
     */
    public String queryBcuDimension(String artifactNo, EventCommArea eventCommArea) throws Exception {
        // 获取元件信息
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> resultMap = artService.getElementByArtifact(artifactNo, eventCommArea);
        Iterator<Map.Entry<String, String>> it = resultMap.entrySet().iterator();
        // 元件编码
        String bcuDimension = null;
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String[] pcdKey = key.split("_");
            String currentKey = pcdKey[0];
            if(Constant.BCU_CYCLE_RATE.equals(currentKey)){
                bcuDimension = currentKey;
            }
        }
        return bcuDimension;
    }
}
