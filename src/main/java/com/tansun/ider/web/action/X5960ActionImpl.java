package com.tansun.ider.web.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.tansun.ider.bus.X5960Bus;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreCustomerBusinessType;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlView;
import com.tansun.ider.dao.issue.entity.CoreCustomerElement;
import com.tansun.ider.dao.issue.entity.CoreCustomerWaiveFeeInfo;
import com.tansun.ider.dao.issue.entity.CoreGeneralActivityLog;
import com.tansun.ider.dao.issue.entity.CoreGeneralActivityLogB;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreOlTransPostLog;
import com.tansun.ider.dao.issue.entity.CoreOlTransPostLogB;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.entity.CoreSpecialActLogHist;
import com.tansun.ider.dao.issue.entity.CoreSpecialActivityLogB;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.model.bo.X5960BO;
import com.tansun.ider.web.WSC;

/**
 * publicservice工程中，查询发卡表，统一接口
 * 
 * @author lianhuan 2019年3月7日
 */
@Service("X5960")
public class X5960ActionImpl implements ActionService {
    public static String tableName = "Core";
    @Autowired
    private X5960Bus x5960Bus;

    @Override
    public Object execute(Map<String, Object> map) throws Exception {
        Object object = null;
        X5960BO x5960BO = JSON.parseObject((String) map.get(WSC.EVENT_PUBLIC_DATA_AREA_KEY), X5960BO.class, Feature.DisableCircularReferenceDetect);
        if (CoreOlTransPostLog.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryOlTransPostLog(x5960BO);
        } else if (CoreOlTransPostLogB.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryOlTransPostLogB(x5960BO);
        } else if (CoreAccount.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryAccount(x5960BO);
        } else if (CoreCustomer.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryCustomer(x5960BO);
        } else if (CoreMediaBasicInfo.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryMediaBasicInfo(x5960BO);
        } else if (CoreCustomerWaiveFeeInfo.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryCustomerWaiveFeeInfo(x5960BO);
        } else if (CoreCustomerContrlView.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryCustomerContrlView(x5960BO);
        } else if (CoreProductForm.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryProductForm(x5960BO);
        } else if (CoreGeneralActivityLog.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryGeneralActivityLog(x5960BO);
        }  else if (CoreSpecialActivityLogB.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.querySpecialActivityLogB(x5960BO);
        } else if (CoreGeneralActivityLogB.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryGeneralActivityLogB(x5960BO);
        } else if (CoreTransHist.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryTransHist(x5960BO);
        } else if (CoreSpecialActLogHist.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.querySpecialActLogHist(x5960BO);
        } else if (CoreCustomerBusinessType.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryCustomerBusinessType(x5960BO);
        } else if (CoreCustomerElement.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryCustomerElement(x5960BO);
        } else if (CoreCustomerBillDay.class.getName().equals(x5960BO.getQueryTableName())) {
            object = x5960Bus.queryCustomerBillDay(x5960BO);
        }
        return object;
    }

}
