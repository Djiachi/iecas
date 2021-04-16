package com.tansun.ider.service;

import java.util.List;
import java.util.Map;

import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlView;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;

/**
 * 查询客户信息公共参数
 *
 * @author wt
 * @date 2018年11月21日上午09:22
 */
public interface QueryCustomerService {

    public List<CoreMediaBasicInfo> queryCoreMediaBasicInfoList(String externalIdentificationNo, String queryFlag)
            throws Exception;

    /**
     * @Description: 根据身份证号、或者外部识别号，媒介单元基本信息
     * @param idNumber
     *            身份证号
     * @param externalIdentificationNo
     *            外部识别号
     * @return String 客户号
     * @throws Exception
     */
    public String queryCoreMediaBasicInfo(String idNumber, String externalIdentificationNo) throws Exception;

    /**
     *
     * @Description: 根据身份证号、或者外部识别号，查询客户基本信息
     * @param idNumber
     *            身份证号
     * @param externalIdentificationNo
     *            外部识别号
     * @return CoreCustomer 客户基本信息
     * @throws Exception
     */
    public CoreCustomer queryCoreCustomer(String idNumber, String externalIdentificationNo) throws Exception;

    /**
     *
     * @Description: 查询客户封锁码视图表，根据开始结束日期查询
     * @param customerNo
     * @param contrlStartDate
     * @param contrlEndDate
     * @return
     * @throws Exception
     */
    public List<CoreCustomerContrlView> queryCoreCustomerContrlView(String customerNo, String contrlStartDate,
                                                                    String contrlEndDate) throws Exception;

    /**
     * 根据机构号查询所属法人
     *
     * @param organization
     * @return
     * @throws Exception
     */
    public String queryCustomerCorporationEntity(String organization) throws Exception;

    /**
     * 根据用户名查询机构号
     *
     * @param userName
     * @return
     * @throws Exception
     */
    public String queryOrganByUserName(String userName) throws Exception;

    /**
     * 增加校验查询客户所属机构的法人是否与登录用户所属机构的法人相同
     *
     * @param bodyMap
     * @return
     * @throws Exception
     */
    public boolean checkCorporationEntity(Map<String, Object> bodyMap,String eventId) throws Exception;

    /**
     * 根据外部识别号查询 媒介表,获取媒介对象
     * 内部已存在空报错
     */
    public CoreMediaBasicInfo queryCoreMediaBasicInfoForExt(String externalIdentificationNo) throws Exception;


    /**@author sunyaoyao改
     * 根据证件类型、证件号，或者外部识别号查询客户代码
     * 内部已存在空报错
     */
    public String queryCustomerNo(String idType,String idNumber,String externalIdentificationNo) throws Exception;

    /**
     * @author sunyaoyao改
     * 根据证件类型、证件号，或者外部识别号查询客户基本信息
     * 内部已存在空报错
     */
    public Object queryCustomer(String idType,String idNumber,String externalIdentificationNo) throws Exception;
    /**
     * @author wangxi改
     * 根据证件类型、证件号，或者外部识别号查询客户基本信息(支持无效卡)
     * 内部已存在空报错
     */
    public Object queryCustomerInvalid(String idType,String idNumber,String externalIdentificationNo) throws Exception;
    /**
     * 根据外部识别号查询 媒介表,获取媒介对象(支持无效卡)
     * 内部已存在空报错
     */
    public List<CoreMediaBasicInfo> queryCoreMediaBasicInfoForExInvalid(String externalIdentificationNo) throws Exception;

    public CoreCustomer queryCustomer(String customerNo) throws Exception;

    /**
     * 客户产品注销校验接口
     */
    public Map<String,String> checkProductLogout(String customerNo,String productObjectCode) throws Exception;

    public Map<String,String> logoutCheckMode(String customerNo,String productObjectCode, List<CoreAccount> coreAccountList, int flag, Integer newFlag) throws Exception;
}
