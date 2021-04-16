package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5825Bus;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.CoreMediaBindDao;
import com.tansun.ider.dao.issue.CoreMediaCardInfoDao;
import com.tansun.ider.dao.issue.CoreMediaLabelInfoDao;
import com.tansun.ider.dao.issue.CoreProductFormDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBind;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaLabelInfo;
import com.tansun.ider.dao.issue.entity.CoreProductForm;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBindSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaCardInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaLabelInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductFormSqlBuilder;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.AuthEventCommAreaNonFinanceBean;
import com.tansun.ider.model.bo.X5825BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.service.business.common.Constant;

/**
 * 媒介升降级
 * @author wangxi
 *
 */
@Service
public class X5825BusImpl implements X5825Bus {
    @Autowired
    private HttpQueryService httpQueryService;
    @Autowired
    private CoreMediaBindDao coreMediaBindDao;
    @Autowired
    private CoreCustomerDao coreCustomerDao;
    @Autowired
    private CoreMediaLabelInfoDao coreMediaLabelInfoDao;
    @Autowired
    private CoreProductFormDao coreProductFormDao;
    @Autowired
    private CoreMediaCardInfoDao coreMediaCardInfoDao;
    @Autowired
    private CoreMediaBasicInfoDao coreMediaBasicInfoDao;

	@Override
    public Object busExecute(X5825BO x5825BO) throws Exception {
		//外部识别号
		String externalIdentificationNo = x5825BO.getExternalIdentificationNo();
		//产品对象代码-新
		String productObjectCodeNew = x5825BO.getProductObjectCodeNew();
		//客户号
		String customerNo = x5825BO.getCustomerNo();
		// 操作员Id
        String operatorId = x5825BO.getOperatorId();
		
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if(coreCustomer==null){
            throw new BusinessException("CUS-12000");
        }
        CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
		
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		if(StringUtil.isNotBlank(externalIdentificationNo)){
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
		}
		
		coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		
		// 事件公共公共区
        EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
        // 将参数传递给事件公共区
        CachedBeanCopy.copyProperties(coreMediaBasicInfo,eventCommAreaNonFinance);
        
        // 查询媒介绑定信息，并将其放到公共区
        CoreMediaBindSqlBuilder coreMediaBindSqlBuilder = new CoreMediaBindSqlBuilder();
        coreMediaBindSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
        List<CoreMediaBind> listCoreMediaBinds = coreMediaBindDao.selectListBySqlBuilder(coreMediaBindSqlBuilder);
        if (null != listCoreMediaBinds && !listCoreMediaBinds.isEmpty()) {
            eventCommAreaNonFinance.setListCoreMediaBinds(listCoreMediaBinds);
        }
        // 查询媒介标签信息，并将其放到公共区
        CoreMediaLabelInfoSqlBuilder coreMediaLabelInfoSqlBuilder = new CoreMediaLabelInfoSqlBuilder();
        coreMediaLabelInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
        List<CoreMediaLabelInfo> listCoreMediaLabelInfos = coreMediaLabelInfoDao
                .selectListBySqlBuilder(coreMediaLabelInfoSqlBuilder);
        if (null != listCoreMediaLabelInfos && !listCoreMediaLabelInfos.isEmpty()) {
            eventCommAreaNonFinance.setCoreMediaLabelInfos(listCoreMediaLabelInfos);
        }
        //产品形式信息
        CoreProductFormSqlBuilder coreProductFormSqlBuilder = new CoreProductFormSqlBuilder();
        coreProductFormSqlBuilder.andProductFormEqualTo(coreMediaBasicInfo.getProductForm());
        CoreProductForm coreProductForm = coreProductFormDao.selectBySqlBuilder(coreProductFormSqlBuilder);
        eventCommAreaNonFinance.setProductForm(coreProductForm.getProductForm());
        // 查询出 凸字信息
        CoreMediaCardInfoSqlBuilder coreMediaCardInfoSqlBuilder = new CoreMediaCardInfoSqlBuilder();
        coreMediaCardInfoSqlBuilder.andMediaUnitCodeEqualTo(coreMediaBasicInfo.getMediaUnitCode());
        CoreMediaCardInfo coreMediaCardInfo = coreMediaCardInfoDao.selectBySqlBuilder(coreMediaCardInfoSqlBuilder);
        if (null != coreMediaCardInfo) {
            CachedBeanCopy.copyProperties(coreMediaCardInfo,eventCommAreaNonFinance);
        }
        CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(eventCommAreaNonFinance.getOperationMode(),
                eventCommAreaNonFinance.getMediaObjectCode());
        if (null == coreMediaObject) {
            throw new BusinessException("CUS-00014", "媒介对象表");
        }
        String institutionId = coreMediaBasicInfo.getInstitutionId();
        CoreOrgan coreOrgan = httpQueryService.queryOrgan(institutionId);
        if (coreOrgan == null) {
            throw new BusinessException("CUS-00014", "机构表");
        }
        // 国家码
        eventCommAreaNonFinance.setCountry(coreOrgan.getCountry());
        // 服务代码
        eventCommAreaNonFinance.setServiceCode(coreMediaObject.getServiceCode());
        // 媒介类型
        eventCommAreaNonFinance.setMediaObjectType(coreMediaObject.getMediaObjectType());
        // 转卡标志
        eventCommAreaNonFinance.setInvalidReason(InvalidReasonStatus.CHP.getValue());
        eventCommAreaNonFinance.setInvalidReasonOld(InvalidReasonStatus.CHP.getValue());
        // 转出媒介单元代码
        eventCommAreaNonFinance.setTransferMediaCode(coreMediaBasicInfo.getMediaUnitCode());
        eventCommAreaNonFinance.setAuthDataSynFlag("1");//同步授权标志
        eventCommAreaNonFinance.setCurrLogFlag(coreSystemUnit.getCurrLogFlag());
        eventCommAreaNonFinance.setOperationDate(coreSystemUnit.getNextProcessDate());
        eventCommAreaNonFinance.setIdType(coreCustomer.getIdType());
        eventCommAreaNonFinance.setIdNumber(coreCustomer.getIdNumber());
        eventCommAreaNonFinance.setOperatorId(operatorId);//操作员id
        eventCommAreaNonFinance.setCurrProcessDate(coreSystemUnit.getCurrProcessDate());//当前处理日
        eventCommAreaNonFinance.setNextProcessDate(coreSystemUnit.getNextProcessDate());
        eventCommAreaNonFinance.setProductObjectCode(productObjectCodeNew);//新选择的产品对象代码
        eventCommAreaNonFinance.setExpirationDate(coreMediaBasicInfo.getExpirationDate());
		
/*        //同步授权
        eventCommAreaNonFinance.setWhetherProcess("");
        List<Map<String, Object>> eventCommAreaTriggerEventList = new ArrayList<>();
        Map<String, Object> triggerEventParams = new HashMap<String, Object>();
        AuthEventCommAreaNonFinanceBean authEventCommAreaNonFinanceBean = new AuthEventCommAreaNonFinanceBean();
        CachedBeanCopy.copyProperties(eventCommAreaNonFinance,authEventCommAreaNonFinanceBean);
        authEventCommAreaNonFinanceBean.setAuthDataSynFlag("1");
        authEventCommAreaNonFinanceBean.setInvalidReasonOld("");
        triggerEventParams.put(Constant.KEY_TRIGGER_PARAMS, authEventCommAreaNonFinanceBean);
        eventCommAreaTriggerEventList.add(triggerEventParams);
        eventCommAreaNonFinance.setEventCommAreaTriggerEventList(null);
        eventCommAreaNonFinance.setEventCommAreaTriggerEventList(eventCommAreaTriggerEventList);*/
        
        
		return eventCommAreaNonFinance;
    }

}
