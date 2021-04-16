package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5340Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.nonfinance.mapper.DisputeAccountMapper;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5340BO;
import com.tansun.ider.model.vo.X5340VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;

/**
 * 争议账户信息查询
 * 
 * @author huangyayun
 * @date 2018年8月13日
 */
@Service
public class X5340BusImpl implements X5340Bus {
	@Resource
	private CoreAccountDao coreAccountDao;// 客户基本账户表
	// @Resource
	// private CoreCustomerDao coreCustomerDao;// 客户号
	@Autowired
	private QueryCustomerService queryCustomerService;
	@Resource
	private HttpQueryService httpQueryService;
	@Resource
	private DisputeAccountMapper disputeAccountMapper;

	@Override
	public Object busExecute(X5340BO x5340Bo) throws Exception {

		String customerNo = null;
		Object object = queryCustomerService.queryCustomer(x5340Bo.getIdType(), x5340Bo.getIdNumber(),
				x5340Bo.getExternalIdentificationNo());
		if (object instanceof CoreCustomer) {
			CoreCustomer coreCustomer = (CoreCustomer) object;
			customerNo = coreCustomer.getCustomerNo();
		} else if (object instanceof CoreMediaBasicInfo) {
			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo) object;
			customerNo = coreMediaBasicInfo.getMainCustomerNo();
		}
		// if
		// (StringUtil.isNotEmpty(x5340Bo.getIdNumber())&&StringUtil.isNotEmpty(x5340Bo.getIdType()))
		// {
		// CoreCustomer coreCustomer =
		// queryCustomerService.queryCoreCustomer(x5340Bo.getIdType(),x5340Bo.getIdNumber());
		// CoreCustomerSqlBuilder coreCustomerSqlBuilder = new
		// CoreCustomerSqlBuilder();
		// coreCustomerSqlBuilder.
		// andIdNumberEqualTo(x5340Bo.getIdNumber());
		// CoreCustomer coreCustomer =
		// coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		// if (coreCustomer != null) {
		// customerNo = coreCustomer.getCustomerNo();
		// }else{
		// throw new BusinessException("CUS-00014", "客户基本");
		// }
		// customerNo = coreCustomer.getCustomerNo();
		// }
		x5340Bo.setCustomerNo(customerNo);
		PageBean<X5340VO> page = new PageBean<>();
		if (null != x5340Bo.getPageSize() && null != x5340Bo.getIndexNo()) {
			x5340Bo.setPageSize(x5340Bo.getPageSize());
			x5340Bo.setIndexNo(x5340Bo.getIndexNo());
			page.setPageSize(x5340Bo.getPageSize());
			page.setIndexNo(x5340Bo.getIndexNo());
		}
		int totalCount = disputeAccountMapper.selectDisputeAccountConut(x5340Bo);
		page.setTotalCount(totalCount);
		if (totalCount > 0) {
			Integer decimalPlaces = null;
			List<X5340VO> x5340VOList = disputeAccountMapper.selectDisputeAccount(x5340Bo);
			for (X5340VO x5340VO : x5340VOList) {
				// 金额转换
				if (decimalPlaces == null) {
					CoreCurrency coreCurrency = httpQueryService.queryCurrency(x5340VO.getCurrencyCode());
					decimalPlaces = coreCurrency.getDecimalPosition();
					//获取币种
					x5340VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
				}
				//获取客户运营模式
                String operationMode = "";
                if (StringUtil.isNotBlank(customerNo)) {
                	CoreCustomer coreCustomer = httpQueryService.queryCustomer(null, customerNo);
                	if(null != coreCustomer){
                		operationMode = coreCustomer.getOperationMode();
                	}
                }
                //获取业务项目描述
                if (StringUtil.isNotBlank(x5340VO.getBusinessProgramNo()) && StringUtil.isNotBlank(operationMode)) {
                	CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode, x5340VO.getBusinessProgramNo());
                	if(null != coreBusinessProgram){
                		x5340VO.setProgramDesc(coreBusinessProgram.getProgramDesc());
                	}
                }
                //获取所属机构名称
                if (StringUtil.isNotBlank(x5340VO.getOrganNo())){
                	CoreOrgan coreOrgan = httpQueryService.queryOrgan(x5340VO.getOrganNo());
                	if(null != coreOrgan){
                		x5340VO.setOrganName(coreOrgan.getOrganName());
                	}
                }
                //获取所属业务类型描述
                if (StringUtil.isNotBlank(x5340VO.getBusinessTypeCode()) && StringUtil.isNotBlank(operationMode)) {
                	CoreBusinessType coreBusinessType = httpQueryService.queryBusinessType(operationMode, x5340VO.getBusinessTypeCode());
                	if(null != coreBusinessType){
                		x5340VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
                	}
                }
                //获取新建日期
                if (StringUtil.isNotBlank(x5340VO.getAccountId()) && StringUtil.isNotBlank(x5340VO.getCurrencyCode())) {
                	CoreAccount coreAccount =  httpQueryService.queryAccount(x5340VO.getAccountId(), x5340VO.getCurrencyCode());
                	if(null != coreAccount){
                		x5340VO.setCreateDate(coreAccount.getCreateDate());
                	}
                }
                //获取产品描述
                if (StringUtil.isNotBlank(x5340VO.getProductObjectCode()) && StringUtil.isNotBlank(operationMode)) {
                	CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode, x5340VO.getProductObjectCode());
                	if(null != coreProductObject){
                		x5340VO.setProductDesc(coreProductObject.getProductDesc());
                	}
                }
                
				amountConversion(x5340VO, decimalPlaces);
				decimalPlaces = null;
			}
			page.setRows(x5340VOList);
		}
		return page;
	}

	/**
	 * 金额转换
	 * 
	 * @author huangyayun
	 * @date 2018年8月13日
	 */
	private void amountConversion(X5340VO x5340VO, Integer decimalPlaces) throws Exception {
		if (x5340VO.getDisputeAmount() != null) {
			BigDecimal disputeAmount = CurrencyConversionUtil.reduce(x5340VO.getDisputeAmount(), decimalPlaces);
			x5340VO.setDisputeAmount(disputeAmount);
		}
		if (x5340VO.getBlanceAmount() != null) {
			BigDecimal blanceAmount = CurrencyConversionUtil.reduce(x5340VO.getBlanceAmount(), decimalPlaces);
			x5340VO.setBlanceAmount(blanceAmount);
		}
		if (x5340VO.getOverpayFreezeAmount() != null) {
			BigDecimal overpayFreezeAmount = CurrencyConversionUtil.reduce(x5340VO.getOverpayFreezeAmount(),
					decimalPlaces);
			x5340VO.setOverpayFreezeAmount(overpayFreezeAmount);
		}
		if (x5340VO.getInterestAmt() != null) {
			BigDecimal interestAmt = CurrencyConversionUtil.reduce(x5340VO.getInterestAmt(), decimalPlaces);
			x5340VO.setInterestAmt(interestAmt);
		}
	}
}
