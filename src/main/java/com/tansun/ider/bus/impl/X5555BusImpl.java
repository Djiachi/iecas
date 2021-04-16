package com.tansun.ider.bus.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5555Bus;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreBalanceObject;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessType;
import com.tansun.ider.dao.beta.entity.CoreCurrency;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreTransHist;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreTransHistDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreTransHistSqlBuilder;
import com.tansun.ider.enums.LendingD;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.BSC;
import com.tansun.ider.model.bo.X5555BO;
import com.tansun.ider.model.vo.X5555VO;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.CachedBeanCopy;
import com.tansun.ider.util.CardUtil;

/**
 * @version:1.0
 * @Description: 利息历史查询
 * @author: wt
 */
@Service
public class X5555BusImpl implements X5555Bus {

	@Resource
	private CoreTransHistDaoImpl coreTransHistDaoImpl;
	@Resource
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Resource
	private HttpQueryService httpQueryService;
	@Autowired
    private QueryCustomerService queryCustomerService;

	@Override
	public Object busExecute(X5555BO x5555bo) throws Exception {
		PageBean<X5555VO> page = new PageBean<>();
		String idNumber = x5555bo.getIdNumber();
		String idType = x5555bo.getIdType();
		String externalIdentificationNo = x5555bo.getExternalIdentificationNo();
		String customerNo = x5555bo.getCustomerNo();
		Object object = queryCustomerService.queryCustomer(idType, idNumber, externalIdentificationNo);
		String operationMode = "";
		String productObjectCode = "";
        if(object instanceof CoreCustomer){
			CoreCustomer coreCustomer = (CoreCustomer)object;
			customerNo = coreCustomer.getCustomerNo();
		    operationMode = coreCustomer.getOperationMode();
		}else if(object instanceof CoreMediaBasicInfo){
			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			customerNo = coreMediaBasicInfo.getMainCustomerNo();
			operationMode = coreMediaBasicInfo.getOperationMode();
			productObjectCode = coreMediaBasicInfo.getProductObjectCode();
		}
//		if (StringUtil.isNotBlank(idNumber)) {
//			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
//			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
//			CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
//			if (null == coreCustomer) {
//				throw new BusinessException("CUS-00005");
//			}
//			customerNo = coreCustomer.getCustomerNo();
//		} else if (StringUtil.isNotBlank(externalIdentificationNo)) {
//			// 查询媒介基本信息
//			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
//			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
//			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
//					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
//			if (coreMediaBasicInfo == null) {
//				throw new BusinessException("CUS-00005");
//			}
//			customerNo = coreMediaBasicInfo.getMainCustomerNo();
//		}
		CoreTransHistSqlBuilder coreTransHistSqlBuilder = new CoreTransHistSqlBuilder();
		if (StringUtil.isNotBlank(customerNo)) {
			coreTransHistSqlBuilder.andCustomerNoEqualTo(customerNo);
		}
		String reproductObjectCode = "";
		String productDesc = "";
		//查询产品对象代码
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			EventCommArea eventCommArea = new EventCommArea();
		    eventCommArea.setEcommOperMode(operationMode);//运营模式
		    eventCommArea.setEcommProdObjId(productObjectCode);//产品对象代码
	        List<CoreActivityArtifactRel> artifactList = x5555bo.getActivityArtifactList();
	        if (null != artifactList && !artifactList.isEmpty()) {
			reproductObjectCode = this.getProObjCode(eventCommArea, artifactList);
			 //集中核算
			 if("0".equals(reproductObjectCode)){
				 coreTransHistSqlBuilder.andProductObjectCodeEqualTo("0");
				 productDesc = "";
             }else{//反之，正常进行赋值操作
            	 coreTransHistSqlBuilder.andProductObjectCodeEqualTo(productObjectCode);
            	 CoreProductObject coreProductObject = httpQueryService.queryProductObject(operationMode, reproductObjectCode);
            	 if (null != coreProductObject) {
            		 productDesc = coreProductObject.getProductDesc();
				}
             }
			 List<CoreProductBusinessScope> coreProductBusinessScopeList =httpQueryService.queryProductBusinessScope(productObjectCode, operationMode);
			 if (null != coreProductBusinessScopeList && !coreProductBusinessScopeList.isEmpty()) {
				 CoreTransHistSqlBuilder coreTransHistSqlBuilder1 = new CoreTransHistSqlBuilder();
				 for (CoreProductBusinessScope coreProductBusinessScope : coreProductBusinessScopeList) {
					String businessProgramNo = coreProductBusinessScope.getBusinessProgramNo();
					if (StringUtil.isNotBlank(businessProgramNo)) {
						coreTransHistSqlBuilder1.orBusinessProgramCodeEqualTo(businessProgramNo);
					}
				}
				coreTransHistSqlBuilder.and(coreTransHistSqlBuilder1);
			 }
	        }
		}
		// 查询活动编号 X8040
		coreTransHistSqlBuilder.andActivityNoEqualTo("X8040")
				.and(new CoreTransHistSqlBuilder().orEventNoLikeBoth("PT.12").orEventNoLikeBoth("PT.13"));
		coreTransHistSqlBuilder.orderByOccurrDate(true);
		coreTransHistSqlBuilder.orderByOccurrTime(true);
		int totalCount = coreTransHistDaoImpl.countBySqlBuilder(coreTransHistSqlBuilder);
		page.setTotalCount(totalCount);
		if (null != x5555bo.getPageSize() && null != x5555bo.getIndexNo()) {
			coreTransHistSqlBuilder.setPageSize(x5555bo.getPageSize());
			coreTransHistSqlBuilder.setIndexNo(x5555bo.getIndexNo());
			page.setPageSize(x5555bo.getPageSize());
			page.setIndexNo(x5555bo.getIndexNo());
		}
		if (totalCount > 0) {
			List<CoreTransHist> list = coreTransHistDaoImpl.selectListBySqlBuilder(coreTransHistSqlBuilder);
			List<X5555VO> listX5555VO = new ArrayList<>();
			for (CoreTransHist coreTransHist : list) {
				X5555VO X5555VO = new X5555VO();
				if (StringUtil.isNotBlank(externalIdentificationNo)) {
					X5555VO.setProductObjectCode(reproductObjectCode);
					X5555VO.setProductDesc(productDesc);
				}
				// 金额转换及获取币种描述
				amountConversion(coreTransHist,X5555VO);
				CachedBeanCopy.copyProperties(coreTransHist, X5555VO);
				// 业务项目代码
				String businessTypeCode = coreTransHist.getBusinessTypeCode();
				if (StringUtil.isNotBlank(businessTypeCode)) {
					CoreBusinessType coreBusinessType =  httpQueryService.queryBusinessType(operationMode, businessTypeCode);
					if (null != coreBusinessType) {
						X5555VO.setBusinessDesc(coreBusinessType.getBusinessDesc());
					}
				}
				//获取余额对象描述
				if (StringUtil.isNotBlank(X5555VO.getBalanceObjectCode())) {
					CoreBalanceObject coreBalanceObject = httpQueryService.queryBalanceObject(operationMode, X5555VO.getBalanceObjectCode());
					if(null != coreBalanceObject){
						X5555VO.setObjectDesc(coreBalanceObject.getObjectDesc());
					}
				}
				//获取业务项目描述
				if (StringUtil.isNotBlank(X5555VO.getBusinessProgramCode())) {
					CoreBusinessProgram coreBusinessProgram = httpQueryService.queryBusinessProgram(operationMode, X5555VO.getBusinessProgramCode());
					if(null != coreBusinessProgram){
						X5555VO.setProgramDesc(coreBusinessProgram.getProgramDesc());
					}
				}
				//获取入账币种描述
				if (StringUtil.isNotBlank(X5555VO.getPostingCurrencyCode())) {
					CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreTransHist.getPostingCurrencyCode());
					if(null != coreCurrency){
						X5555VO.setPostingCurrencyDesc(coreCurrency.getCurrencyDesc());
					}
				}
				//获取交易币种
				if (StringUtil.isNotBlank(X5555VO.getTransCurrCde())) {
					CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreTransHist.getTransCurrCde());
					if(null != coreCurrency){
						X5555VO.setTransCurrDesc(coreCurrency.getCurrencyDesc());
					}
				}				
				listX5555VO.add(X5555VO);
			}
			page.setRows(listX5555VO);
		}
		return page;
	}

	/**
	 * 转换币种，交易金额
	 */
	private void amountConversion(CoreTransHist coreTransHist,X5555VO X5555VO) throws Exception {
		// 实际入账金额
		CoreCurrency coreCurrency = httpQueryService.queryCurrency(coreTransHist.getCurrencyCode());
		if(null != coreCurrency){
			X5555VO.setCurrencyDesc(coreCurrency.getCurrencyDesc());
		}
		int decimalPlaces = coreCurrency.getDecimalPosition();
		if (coreTransHist.getActualPostingAmount() != null
				&& !coreTransHist.getActualPostingAmount().toString().equals("0")) {
			BigDecimal actualPostingAmount = CurrencyConversionUtil.reduce(coreTransHist.getActualPostingAmount(),
					decimalPlaces);
			coreTransHist.setActualPostingAmount(actualPostingAmount);
		}
		// 清算分配金额
		if (coreTransHist.getSettleDistriAmount() != null
				&& !coreTransHist.getSettleDistriAmount().toString().equals("0")) {
			CoreCurrency coreCurrencySettleDistriAmount = httpQueryService
					.queryCurrency(coreTransHist.getSettleDistriCurrency());
			int decimalPlacesSettleDistriAmount = coreCurrencySettleDistriAmount.getDecimalPosition();
			BigDecimal settleDistriAmount = CurrencyConversionUtil.reduce(coreTransHist.getSettleDistriAmount(),
					decimalPlacesSettleDistriAmount);
			coreTransHist.setSettleDistriAmount(settleDistriAmount);
		}
		// 溢缴款冻结金额 overpay_frz_amount
		if (coreTransHist.getOverpayFrzAmount() != null
				&& !coreTransHist.getOverpayFrzAmount().toString().equals("0")) {
			CoreCurrency coreCurrencySettleDistriAmount = httpQueryService
					.queryCurrency(coreTransHist.getOverpayFrzCurrCode());
			int decimalPlacesOverpayFrzAmount = coreCurrencySettleDistriAmount.getDecimalPosition();
			BigDecimal overpayFrzAmount = CurrencyConversionUtil.reduce(coreTransHist.getOverpayFrzAmount(),
					decimalPlacesOverpayFrzAmount);
			coreTransHist.setOverpayFrzAmount(overpayFrzAmount);
		}
		// 清算金额 settlement_amount
		if (coreTransHist.getSettlementAmount() != null
				&& !coreTransHist.getSettlementAmount().toString().equals("0")) {
			CoreCurrency coreCurrencySettlementAmount = httpQueryService
					.queryCurrency(coreTransHist.getSettlementCurrencyCode());
			int decimalPlacesSettlementAmountt = coreCurrencySettlementAmount.getDecimalPosition();
			BigDecimal settlementAmount = CurrencyConversionUtil.reduce(coreTransHist.getSettlementAmount(),
					decimalPlacesSettlementAmountt);
			coreTransHist.setSettlementAmount(settlementAmount);
		}
		// 入账币种
		if (coreTransHist.getPostingAmount() != null && !coreTransHist.getPostingAmount().toString().equals("0")) {
			CoreCurrency coreCurrencyPostingAmount = httpQueryService.queryCurrency(coreTransHist.getPostingCurrencyCode());
			int decimalPlacesPostingAmount = coreCurrencyPostingAmount.getDecimalPosition();
			BigDecimal postingAmount = CurrencyConversionUtil.reduce(coreTransHist.getPostingAmount(),
					decimalPlacesPostingAmount);
			if (LendingD.C.getValue().equals(coreTransHist.getLoanSign())) {
				postingAmount = postingAmount.multiply(new BigDecimal(-1));
				coreTransHist.setPostingAmount(postingAmount);
			}else {
				coreTransHist.setPostingAmount(postingAmount);
			}
		}
		//交易金额
		if (coreTransHist.getTransAmount() != null && !coreTransHist.getTransAmount().toString().equals("0")) {
			CoreCurrency coreCurrencyTransAmount = httpQueryService.queryCurrency(coreTransHist.getTransCurrCde());
			int decimalPlacesTransAmount = coreCurrencyTransAmount.getDecimalPosition();
			BigDecimal transAmount = CurrencyConversionUtil.reduce(coreTransHist.getTransAmount(),
					decimalPlacesTransAmount);
			coreTransHist.setTransAmount(transAmount);
		}
	}
	
	
	private String getProObjCode(EventCommArea eventCommArea, List<CoreActivityArtifactRel> artifactList) throws Exception {
        String productOjectCode = "";
        // 验证该活动是否配置构件信息
        Boolean checkResult = CardUtil.checkArtifactExist(BSC.ARTIFACT_NO_403, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, String> elePcdResultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_403, eventCommArea);
        Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (Constant.PRODUCT_BUSINESS_ACCOUNTS.equals(entry.getKey())) {
                // 单独核算
                productOjectCode = eventCommArea.getEcommProdObjId();
            } else {
                // 集中核算，产品对象置空
                productOjectCode = "0";
            }
        }
        return productOjectCode;
    }
	
	
	public static void main(String[] args) {
		BigDecimal decimal = new BigDecimal(123);
		decimal = decimal.multiply(new BigDecimal(-1));
		System.out.println(decimal);
	}

}
