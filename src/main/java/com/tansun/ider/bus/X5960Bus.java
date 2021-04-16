package com.tansun.ider.bus;

import com.tansun.ider.model.bo.X5960BO;

public interface X5960Bus {
    public Object queryOlTransPostLog(X5960BO x5960bo) throws Exception;

    public Object queryOlTransPostLogB(X5960BO x5960bo) throws Exception;

    public Object queryAccount(X5960BO x5960bo) throws Exception;

    public Object queryCustomer(X5960BO x5960bo) throws Exception;

    public Object queryMediaBasicInfo(X5960BO x5960bo) throws Exception;

    public Object queryCustomerWaiveFeeInfo(X5960BO x5960bo) throws Exception;

    public Object queryCustomerContrlView(X5960BO x5960bo) throws Exception;

    public Object queryProductForm(X5960BO x5960bo) throws Exception;

    public Object queryGeneralActivityLog(X5960BO x5960bo) throws Exception;

    public Object querySpecialActivityLogB(X5960BO x5960bo) throws Exception;

    public Object queryGeneralActivityLogB(X5960BO x5960bo) throws Exception;

    public Object queryTransHist(X5960BO x5960bo) throws Exception;

    public Object querySpecialActLogHist(X5960BO x5960bo) throws Exception;

    public Object queryCustomerBusinessType(X5960BO x5960bo) throws Exception;

    public Object queryCustomerElement(X5960BO x5960bo) throws Exception;
    
    public Object queryCustomerBillDay(X5960BO x5960bo) throws Exception;
}
