/**  

* @Title: X5820Bus.java

* @Function

* @Description:  

* @author baiyu

* @date 2019年5月15日  

* @version R04.00 

*/
package com.tansun.ider.bus.impl;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.bus.X5820Bus;
import com.tansun.ider.dao.issue.CoreUnifiedNoticeDao;
import com.tansun.ider.dao.issue.entity.CoreUnifiedNotice;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5820BO;

/**
 * 
 * @ClassName: X5820Bus
 * 
 * @Function:
 * 
 * @Description:
 * 
 * @author baiyu
 * 
 * @date 2019年5月15日
 * 
 * @version R04.00
 * 
 */
@Service
public class X5820BusImpl implements X5820Bus {
    @Autowired
    private CoreUnifiedNoticeDao coreUnifiedNoticeDao;
    private static final Integer DEFAULT_RESEND_TIME = 2;
    private static final String SEND_STATUS_SUCCESS = "S";
    private static final String SEND_STATUS_FAILURE = "F";

    /*
     * (non-Javadoc)
     * 
     * MethodName: busExecute
     * 
     * Description:
     * 
     * @param x5820bo
     * 
     * @return
     * 
     * @throws Exception
     * 
     * @see
     * com.tansun.ider.bus.X5820Bus#busExecute(com.tansun.ider.model.bo.X5820BO)
     * 
     */
    @Override
    public Object busExecute(X5820BO x5820bo) throws Exception {
        // TODO Auto-generated method stub
        CoreUnifiedNotice coreUnifiedNotice = new CoreUnifiedNotice();
        coreUnifiedNotice.setId(RandomUtil.getUUID());
        CachedBeanCopy.copyProperties(x5820bo, coreUnifiedNotice);
        coreUnifiedNotice.setResendTime(DEFAULT_RESEND_TIME);
        if (sendMessage()) {
            coreUnifiedNotice.setSendStatus(SEND_STATUS_SUCCESS);

        } else {
            coreUnifiedNotice.setSendStatus(SEND_STATUS_FAILURE);
        }
        coreUnifiedNotice.setTransDate(DateUtil.format(null, DateUtil.FORMAT_DATE));
        coreUnifiedNotice.setTransTime(DateUtil.format(null, DateUtil.FORMAT_TIME));
        coreUnifiedNotice.setFinalSendDate(DateUtil.format(null, DateUtil.FORMAT_DATE));
        coreUnifiedNotice.setFinalSendTime(DateUtil.format(null, DateUtil.FORMAT_TIME));
        int result = coreUnifiedNoticeDao.insert(coreUnifiedNotice);
        if (result < 1){
            throw new BusinessException("COR-10032");
        }
        return x5820bo;
    }

    /**
     * 
     * 
     * @MethodName: sendMessage
     * 
     * @Description: 发送短信 调用短信平台接口，发送成功返回true，调用失败返回fasle；
     * 
     * @return
     */
    private boolean sendMessage() {

        // TODO Auto-generated method stub

        return true;
    }
}
