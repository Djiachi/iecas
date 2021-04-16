package com.tansun.ider.service;

import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.entity.CoreSpecialActivityLog;

import java.util.List;

/**
 * @Author: PanQi
 * @Date: 2020/1/9
 * @updater:
 * @description: 日志公共服务
 */
public interface LogService {
    /**
     * 保存S日志
     * @param specialActivityLogList
     * @param coreSystemUnit
     * @throws Exception
     */
     void saveSpecialLog(List<CoreSpecialActivityLog> specialActivityLogList, CoreSystemUnit coreSystemUnit)throws Exception ;
}
