package com.tansun.ider.service.impl;

import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.entity.CoreSpecialActivityLog;
import com.tansun.ider.dao.issue.entity.CoreSpecialActivityLogB;
import com.tansun.ider.enums.SystemLogStatus;
import com.tansun.ider.model.CommLog;
import com.tansun.ider.service.CommonLog;
import com.tansun.ider.service.LogService;
import com.tansun.ider.util.CachedBeanCopy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: PanQi
 * @Date: 2020/1/9
 * @updater:
 * @description:
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private CommonLog commonLog;

    @Override
    public void saveSpecialLog(List<CoreSpecialActivityLog> specialActivityLogList, CoreSystemUnit coreSystemUnit)
            throws Exception {
        CommLog commLog = new CommLog();
        String currLogFlag = coreSystemUnit.getCurrLogFlag();
        if (StringUtils.equals(coreSystemUnit.getCurrLogFlag(), SystemLogStatus.A.getValue())) {

        } else if (StringUtils.equals(coreSystemUnit.getCurrLogFlag(), SystemLogStatus.B.getValue())) {

        }
        if (currLogFlag.equals(SystemLogStatus.A.getValue())) {
            commLog.setCoreSpecialActivityLogList(specialActivityLogList);
        } else if (currLogFlag.equals(SystemLogStatus.B.getValue())) {
            // 插入B表中
            List<CoreSpecialActivityLogB> specialActivityLogBList = specialActivityLogList.stream().parallel()
                    .map(log -> {
                        CoreSpecialActivityLogB b = new CoreSpecialActivityLogB();
                        CachedBeanCopy.copyProperties(log, b);
                        return b;
                    }).collect(Collectors.toList());
            commLog.setCoreSpecialActivityLogBList(specialActivityLogBList);
        }
        commonLog.logInsert(commLog);
    }
}
