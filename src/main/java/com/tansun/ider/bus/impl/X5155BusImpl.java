package com.tansun.ider.bus.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5155Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreBudgetOrgAddInfoDao;
import com.tansun.ider.dao.issue.CoreBudgetOrgCustRelDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgAddInfo;
import com.tansun.ider.dao.issue.entity.CoreBudgetOrgCustRel;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgAddInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBudgetOrgCustRelSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ResponseType;
import com.tansun.ider.framwork.api.ActionService;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.X4045VO;
import com.tansun.ider.model.bo.X4045BO;
import com.tansun.ider.model.bo.X5755BO;
import com.tansun.ider.model.vo.X4055VO;
import com.tansun.ider.model.vo.X5155VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.impl.HttpQueryServiceByGnsImpl;
import com.tansun.ider.service.impl.HttpQueryServiceImpl;
import com.tansun.ider.util.CardUtil;
import com.tansun.ider.web.WSC;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 单位公务卡额度查询
 * 
 * @author liuyanxi 2019年5月11日
 */
@Service
public class X5155BusImpl implements X5155Bus {
	@Resource
	private CoreBudgetOrgCustRelDao coreBudgetOrgCustRelDao;
	@Resource
	private CoreBudgetOrgAddInfoDao coreBudgetOrgAddInfoDao;
	@Resource
	private HttpQueryServiceByGnsImpl httpQueryServiceByGnsImpl;
	@Resource
	private HttpQueryServiceImpl httpQueryServiceImpl;
	@Resource
	private CoreCustomerDao coreCustomerDao;
	@Value("${spring.application.name}")
	private String applicationName;
	@Value("${global.target.service.url.nofn}")
	private String nofnUrl;
	@Value("${gns.global.target.service.url.nofn}")
	private String gnsnofnUrl;
	
    @Override
    public Object busExecute(X5755BO x5755bo) throws Exception {
    	
    	String budgetOrgCode = x5755bo.getIdNumber();
        CoreCustomerSqlBuilder coreCustomerSqlBuilder  = new CoreCustomerSqlBuilder();
        coreCustomerSqlBuilder.andIdNumberEqualTo(budgetOrgCode);
        coreCustomerSqlBuilder.andIdTypeEqualTo("7");
        CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
        if(coreCustomer == null){
        	//TODO 客户信息不存在
        	throw new BusinessException("CUS-00005");
        }
        //查询预算单位信息
        CoreBudgetOrgAddInfoSqlBuilder coreBudgetOrgAddInfoSqlBuilder = new CoreBudgetOrgAddInfoSqlBuilder();
        coreBudgetOrgAddInfoSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
        CoreBudgetOrgAddInfo coreBudgetOrgAddInfo = coreBudgetOrgAddInfoDao.selectBySqlBuilder(coreBudgetOrgAddInfoSqlBuilder);
        if(coreBudgetOrgAddInfo == null){
        	//TODO 预算单位信息不存在
        	throw new BusinessException("COR-12043");
        }
        //按币种缩小金额
        CardUtil cardUtil = SpringUtil.getBean(CardUtil.class);
    	int currencyDecimal = cardUtil.getCurrencyDecimal("156");
    	coreBudgetOrgAddInfo.setOrgAllQuota(CurrencyConversionUtil.reduce(coreBudgetOrgAddInfo.getOrgAllQuota(), currencyDecimal));
    	coreBudgetOrgAddInfo.setPersonMaxQuota(CurrencyConversionUtil.reduce(coreBudgetOrgAddInfo.getPersonMaxQuota(), currencyDecimal));
    	coreBudgetOrgAddInfo.setOrgRestQuota(CurrencyConversionUtil.reduce(coreBudgetOrgAddInfo.getOrgRestQuota(), currencyDecimal));
        PageBean<X4055VO> page = new PageBean<X4055VO>();
        X5155VO x5155VO  = new X5155VO();
        CachedBeanCopy.copyProperties(coreBudgetOrgAddInfo, x5155VO);
        x5155VO.setCustomerName(coreCustomer.getCustomerName());
        x5155VO.setCustomerType(coreCustomer.getCustomerType());
        x5155VO.setIdNumber(coreCustomer.getIdNumber());
        x5155VO.setIdType(coreCustomer.getIdType());
        page.setObj(x5155VO);
    	CoreBudgetOrgCustRelSqlBuilder coreBudgetOrgCustRelSqlBuilder = new CoreBudgetOrgCustRelSqlBuilder();
    	coreBudgetOrgCustRelSqlBuilder.andBudgetOrgCodeEqualTo(budgetOrgCode);
    	coreBudgetOrgCustRelSqlBuilder.setIndexNo(x5755bo.getIndexNo());
    	//coreBudgetOrgCustRelSqlBuilder.setPageSize(x5755bo.getPageSize());
    	int count = coreBudgetOrgCustRelDao.countBySqlBuilder(coreBudgetOrgCustRelSqlBuilder);
        page.setPageSize(x5755bo.getPageSize());
        List<CoreBudgetOrgCustRel> lists = null;
        List<X4055VO> x4055voList = new ArrayList<>();
        X4045VO x4045vo = null;
        boolean flag = false;
        if(count > 0){
        	lists = coreBudgetOrgCustRelDao.selectListBySqlBuilder(coreBudgetOrgCustRelSqlBuilder);
        	Iterator<CoreBudgetOrgCustRel> it = lists.iterator();
        	while(it.hasNext()){
        		CoreBudgetOrgCustRel coreBudgetOrgCustRel = it.next();
        		// 判断是否在当前片区
        		if("ider-nofn".equals(applicationName)){
        			flag = true;
        		}
        		else{
        			String gnsArea = applicationName.substring(applicationName.length()-3, applicationName.length());
        			if(gnsArea.equals(coreBudgetOrgCustRel.getCustomerArea()) || StringUtil.isBlank(coreBudgetOrgCustRel.getCustomerArea())){
        				flag = true;
        			}
        		}
        		if (flag) {
        			// 查找当前片区
        			ActionService coreService = (ActionService) SpringUtil.getBean("X4045");
        			// 内部方法转换为JSON后进行传递
        			HashMap<String, Object> paramsMap = new HashMap<String, Object>();
        			X4045BO x4045bo = new X4045BO();
        			x4045bo.setIdType(coreBudgetOrgCustRel.getIdType());
        			x4045bo.setIdNumber(coreBudgetOrgCustRel.getIdNumber());
        			x4045bo.setExternalIdentificationNo(coreBudgetOrgCustRel.getExternalIdentificationNo());
        			paramsMap.put(WSC.EVENT_PUBLIC_DATA_AREA_KEY, JSON.toJSONString(x4045bo, SerializerFeature.DisableCircularReferenceDetect));
        			x4045vo = (X4045VO) coreService.execute(paramsMap);
        			
        		}else{
        			// 调用gns接口
        			x4045vo = httpQueryServiceByGnsImpl.queryMediaInfoByGns(gnsnofnUrl, coreBudgetOrgCustRel.getExternalIdentificationNo(),coreBudgetOrgCustRel.getIdNumber(),coreBudgetOrgCustRel.getIdType());
        		}
        		List<CoreProductBusinessScope> list = httpQueryServiceImpl.queryProductBusinessScope(x4045vo.getProductObjectCode(), x4045vo.getOperationMode());
        		CoreBusinessProgram coreBusinessProgram = httpQueryServiceImpl.queryBusinessProgram(x4045vo.getOperationMode(), list.get(0).getBusinessProgramNo());
        		//sunyaoyao改，如果是4055调用，全部放进去
        		if("1".equals(x5755bo.getTansferFind())){
        			CoreProductObject coreProductObject = httpQueryServiceImpl.queryProductObject(x4045vo.getOperationMode(), x4045vo.getProductObjectCode());
        			X4055VO x4055vo = new X4055VO();
        			x4055vo.setCustomerName(x4045vo.getCustomerName());
        			x4055vo.setExternalIdentificationNo(x4045vo.getExternalIdentificationNo());
        			x4055vo.setOfficialCardType(coreProductObject.getProductDesc());
        			x4055vo.setIdNumber(coreBudgetOrgCustRel.getIdNumber());
        			x4055vo.setIdType(coreBudgetOrgCustRel.getIdType());
            		x4055vo.setCustomerArea(coreBudgetOrgCustRel.getCustomerArea());
            		x4055vo.setMediaUnitCode(x4045vo.getMediaUnitCode());
            		x4055vo.setInvalidFlag(x4045vo.getInvalidFlag());
        			x4055voList.add(x4055vo);
        		}
        		// 移除个人承责
        		else if (ResponseType.RESPONSE_TYPE_CMP.getValue().equals(coreBusinessProgram.getResponseType())) {
        			//continue;
        			// 重新组装参数
        			CoreProductObject coreProductObject = httpQueryServiceImpl.queryProductObject(x4045vo.getOperationMode(), x4045vo.getProductObjectCode());
        			X4055VO x4055vo = new X4055VO();
        			x4055vo.setCustomerName(x4045vo.getCustomerName());
        			x4055vo.setExternalIdentificationNo(x4045vo.getExternalIdentificationNo());
        			x4055vo.setOfficialCardType(coreProductObject.getProductDesc());
        			x4055vo.setIdNumber(coreBudgetOrgCustRel.getIdNumber());
        			x4055vo.setIdType(coreBudgetOrgCustRel.getIdType());
            		x4055vo.setCustomerArea(coreBudgetOrgCustRel.getCustomerArea());
            		x4055vo.setMediaUnitCode(x4045vo.getMediaUnitCode());
            		x4055vo.setInvalidFlag(x4045vo.getInvalidFlag());
        			x4055voList.add(x4055vo);
        		}/*else{
        			it.remove();
        		}*/
        	}
    	}
        page.setTotalCount(x4055voList.size());
        page.setRows(x4055voList);
        
    	return page;
    }
}
