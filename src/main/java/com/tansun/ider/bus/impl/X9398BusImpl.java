package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X9398Bus;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreSpecialActivityLog;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X9598BO;
import com.tansun.ider.model.vo.X5982VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.LogService;
import com.tansun.ider.util.CachedBeanCopy;

/**
 * @version:1.0
 * @Description: 分期资产证券化未抛本金处理
 * @author: PanQi
 */
@Service
public class X9398BusImpl implements X9398Bus {
    private static Logger logger = LoggerFactory.getLogger(X9398BusImpl.class);

    @Autowired
    private LogService logService;
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreCustomerDaoImpl coreCustomerDao;
    static final String BSS_RT_02_1002 = "BSS.RT.02.1002";

    @Override
    public Object busExecute(X9598BO x9598BO) throws Exception {
        PageBean<X5982VO> page = new PageBean<>();
        // 客户号
        String customerNo = x9598BO.getCustomerNo();
        // 获取系统单元信息
        CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
        List<CoreSpecialActivityLog> coreSpecialActivityLogLists = x9598BO.getCoreSpecialActivityLogList();
        // 是否有待处理S日志
        if (null == coreSpecialActivityLogLists || coreSpecialActivityLogLists.isEmpty()) {
            return null;
        }
        List<CoreSpecialActivityLog> logList = new ArrayList<>();
        for (CoreSpecialActivityLog log : coreSpecialActivityLogLists) {
            logList.add(generateCreditSpecialActivityLog(x9598BO, log));
        }
        logService.saveSpecialLog(logList, coreSystemUnit);
        return page;
    }

    private CoreSpecialActivityLog generateCreditSpecialActivityLog(X9598BO x9598BO, CoreSpecialActivityLog log)
            throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("活动[{}]金融 S类日志开始 ==>START", log.getActivityNo());
        }
        CoreSpecialActivityLog coreSpecialActivityLog = new CoreSpecialActivityLog();
        CachedBeanCopy.copyProperties(log, coreSpecialActivityLog);
        coreSpecialActivityLog.setId(RandomUtil.getUUID());
        // 事件编号
        coreSpecialActivityLog.setEventNo(x9598BO.getEventNo());
        // 活动编号
        coreSpecialActivityLog.setActivityNo(x9598BO.getActivityNo());
        coreSpecialActivityLog.setSettleEventNo(BSS_RT_02_1002);
        // 事件活动关联表
        List<CoreEventActivityRel> coreEventActivityRelList = httpQueryService
                .queryEventActivityRel(x9598BO.getEventNo());
        Optional<CoreEventActivityRel> first = coreEventActivityRelList.stream()
                .filter(x -> StringUtils.equals(x.getActivityNo(), log.getActivityNo())).findFirst();
        CoreEventActivityRel coreEventActivityRel = new CoreEventActivityRel();
        if (first.isPresent()) {
            coreEventActivityRel = first.get();
        } else {
            // 没有查到的逻辑
            logger.error("未找到事件活动关联表,EventNo{}", log.getEventNo());
        }
        // 会计用途标识
        if (StringUtil.isBlank(coreEventActivityRel.getAccountingUseFlag())) {
            coreSpecialActivityLog.setAccountingUseFlag("N");
        } else {
            coreSpecialActivityLog.setAccountingUseFlag(coreEventActivityRel.getAccountingUseFlag());
        }
        return coreSpecialActivityLog;
    }

}
