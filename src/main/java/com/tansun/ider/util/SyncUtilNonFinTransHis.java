package com.tansun.ider.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tansun.ider.dao.issue.CoreNmnyHistDao;
import com.tansun.ider.dao.issue.CoreNmnyLogBDao;
import com.tansun.ider.dao.issue.CoreNmnyLogDao;

@Service
public class SyncUtilNonFinTransHis {
    private static Object lock = new Object();
    private Map<String, Object> mapResult = new HashMap<String, Object>();

    public Map<String, Object> getMapResult() {
        return mapResult;
    }

    public void putOne(String key, Object object) {
        synchronized (lock) {
            mapResult.put(key, object);
        }
    }

    public Map<String, Object> syncHisData(CoreNmnyLogDao coreNmnyLogDao,
    		CoreNmnyLogBDao coreNmnyLogBDao,CoreNmnyHistDao coreNmnyHistDao)
            throws Exception {
        List<Thread> list = new ArrayList<Thread>();
        NonFinTransHisSyncThreadA nonFinTransHisSyncThreadA = new NonFinTransHisSyncThreadA(this, coreNmnyLogDao, coreNmnyHistDao);
        NonFinTransHisSyncThreadB nonFinTransHisSyncThreadB = new NonFinTransHisSyncThreadB(this, coreNmnyLogBDao, coreNmnyHistDao);
        Thread tA = new Thread(nonFinTransHisSyncThreadA);
        Thread tB = new Thread(nonFinTransHisSyncThreadB);
        list.add(tA);
        list.add(tB);
        for (Thread thread : list) {
            thread.setDaemon(true);
            thread.start();
        }
        // 子线程处理并返回结果
        for (Thread thread : list) {
            thread.join();
        }
        return mapResult;
    }
}
