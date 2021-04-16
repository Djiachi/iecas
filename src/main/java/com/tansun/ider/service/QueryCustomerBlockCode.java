package com.tansun.ider.service;

import java.util.List;

import com.tansun.ider.model.CustomerContrlViewBean;

public interface QueryCustomerBlockCode {

	 /**
     * 
     * @param customerNo        客户号
     * @param controlProjectNo  管控项目
     * @param contrlStartDate   开始日期
     * @param contrlEndDate     结束日期
     * @param dateFormat        日期格式类型 如果传递 null,则默认设置 yyyy-MM-dd
     * @return                  返回利息时间计算区间
     * @throws Exception
     */
    public List<CustomerContrlViewBean> queryCoreCustomerContrlView(String customerNo,String controlProjectNo, String contrlStartDate,
            String contrlEndDate,String dateFormat) throws Exception;

}
