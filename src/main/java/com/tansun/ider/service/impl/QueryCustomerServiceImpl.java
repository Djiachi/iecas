package com.tansun.ider.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.dao.auth.entity.AuthCreditUnit;
import com.tansun.ider.dao.beta.CoreSystemUnitDao;
import com.tansun.ider.dao.beta.entity.CoreOrgan;
import com.tansun.ider.dao.beta.entity.CoreUser;
import com.tansun.ider.dao.issue.CoreCustomerContrlViewDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreBalanceUnit;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerContrlView;
import com.tansun.ider.dao.issue.entity.CoreInstallmentTransAcct;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreProduct;
import com.tansun.ider.dao.issue.impl.CoreAccountDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreBalanceUnitDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreInstallmentTransAcctDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreProductDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreBalanceUnitSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerContrlViewSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreInstallmentTransAcctSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreProductSqlBuilder;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.QueryCustomerService;
import com.tansun.ider.service.business.common.Constant;

/**
 * 查询客户信息公共参数
 * 
 * @author wt
 * @date 2018年11月21日上午09:20
 */
@Service
public class QueryCustomerServiceImpl implements QueryCustomerService {
	
	private static String BSS_AD_01_9001 = "BSS.AD.01.9001";
	private static String BSS_AD_01_0001 = "BSS.AD.01.0001";
	private static String BSS_AD_01_0003 = "BSS.AD.01.0003";
	
	@Resource
	private CoreCustomerDao coreCustomerDao;
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Resource
	private CoreSystemUnitDao coreSystemUnitDao;
	@Autowired
	private CoreCustomerContrlViewDao coreCustomerContrlViewDao;
	@Autowired
    private HttpQueryService httpQueryService;
	@Autowired
	private CoreAccountDaoImpl coreAccountDaoImpl; //账户基本信息表
	@Autowired
	private CoreBalanceUnitDaoImpl coreBalanceUnitDaoImpl;	//余额单元表
	@Autowired
	private CoreInstallmentTransAcctDaoImpl coreInstallmentTransAcctDaoImpl; //分期交易账户信息表
	@Autowired
	private CoreProductDaoImpl coreProductDaoImpl; //产品单元基本信息
	@Autowired
	private CoreCustomerDaoImpl coreCustomerDaoImpl; //客户基本信息
	
	@Override
	public String queryCoreMediaBasicInfo(String idNumber, String externalIdentificationNo) throws Exception {
		String customerNo = null;
		if (StringUtil.isNotBlank(idNumber)) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilderStr = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilderStr.andIdNumberEqualTo(idNumber);
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilderStr);
			if (coreCustomer != null) {
				customerNo = coreCustomer.getCustomerNo();
			} else {
				throw new BusinessException("CUS-00014", "客户基本信息");
			}
		} else if (StringUtil.isNotBlank(externalIdentificationNo)) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilderStr = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilderStr.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			coreMediaBasicInfoSqlBuilderStr.andInvalidFlagEqualTo("Y");
			CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao
					.selectBySqlBuilder(coreMediaBasicInfoSqlBuilderStr);
			if (coreMediaBasicInfo != null) {
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
			} else {
				throw new BusinessException("CUS-00124");//无法定位媒介基本信息或该媒介已失效！
			}
		}
		return customerNo;
	}

	@Override
	public CoreCustomer queryCoreCustomer(String idNumber, String externalIdentificationNo) throws Exception {
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
			// 有效期标识Y
			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
			coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			if (coreMediaBasicInfo != null) {
				coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
			} else {
				throw new BusinessException("CUS-00014", "媒介单元基本信息");
			}
		}

		if (StringUtil.isNotBlank(idNumber)) {
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
		}
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		if (coreCustomer == null) {
			throw new BusinessException("CUS-00014", "客户基本信息");
		}
		return coreCustomer;
	}

	@Override
	public List<CoreMediaBasicInfo> queryCoreMediaBasicInfoList(String externalIdentificationNo, String queryFlag)
			throws Exception {
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		if (StringUtil.isNotBlank(queryFlag)) {
			coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
		}
		List<CoreMediaBasicInfo> listCoreMediaBasicInfo = coreMediaBasicInfoDao
				.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		return listCoreMediaBasicInfo;
	}

	@Override
	public List<CoreCustomerContrlView> queryCoreCustomerContrlView(String customerNo, String contrlStartDate,
			String contrlEndDate) throws Exception {
		CoreCustomerContrlViewSqlBuilder coreCustomerContrlViewSqlBuilder = new CoreCustomerContrlViewSqlBuilder();
		coreCustomerContrlViewSqlBuilder.andCustomerNoEqualTo(customerNo);
		if (StringUtil.isNotBlank(contrlStartDate)) {
			coreCustomerContrlViewSqlBuilder.andContrlStartDateGreaterThanOrEqualTo(contrlStartDate);
		}
		if (StringUtil.isNotBlank(contrlEndDate)) {
			coreCustomerContrlViewSqlBuilder.andContrlEndDateLessThanOrEqualTo(contrlEndDate);
		}
		List<CoreCustomerContrlView> listCoreCustomerContrlView = coreCustomerContrlViewDao
				.selectListBySqlBuilder(coreCustomerContrlViewSqlBuilder);
		return listCoreCustomerContrlView;
	}

	@Override
    public String queryCustomerCorporationEntity(String organization) throws Exception {
        CoreOrgan organ = httpQueryService.queryOrgan(organization);
        if (organ == null) {
            throw new BusinessException("CUS-00014", "机构" + organization);
        }
        String corporationEntityNo = organ.getCorporationEntityNo();
        if (StringUtil.isBlank(corporationEntityNo)) {
            throw new BusinessException("CUS-00014", "机构" + organization + "的法人实体编号");
        }
        return corporationEntityNo;
    }

    @Override
    public String queryOrganByUserName(String userName) throws Exception {
        CoreUser coreUser = httpQueryService.queryCoreUser(userName);
        if (coreUser == null) {
            throw new BusinessException("CUS-00014", "登陆用户" + userName);
        }
        String organization = coreUser.getOrganization();
        if (StringUtil.isBlank(organization)) {
            throw new BusinessException("CUS-00014", "登陆用户" + userName + "的机构号");
        }
        return organization;
    }

    @Override
    public boolean checkCorporationEntity(Map<String, Object> bodyMap,String eventId) throws Exception {
        String userName = "";
        String externalIdentificationNo = "";
        String idNumber = "";
        String idType = "";
        if (bodyMap.containsKey("operatorId")) {
            userName = (String) bodyMap.get("operatorId");
        }
        if (bodyMap.containsKey("externalIdentificationNo")) {
            externalIdentificationNo = (String) bodyMap.get("externalIdentificationNo");
        }
        if (bodyMap.containsKey("idNumber")) {
            idNumber = (String) bodyMap.get("idNumber");
        }
        if (bodyMap.containsKey("idType")) {
        	idType = (String) bodyMap.get("idType");
        }
        if ((StringUtil.isBlank(externalIdentificationNo) && StringUtil.isBlank(idNumber) && StringUtil.isBlank(idType))
                || StringUtil.isBlank(userName) || !eventId.equals(BSS_AD_01_0001) || !eventId.equals(BSS_AD_01_9001) ||
                !eventId.equals(BSS_AD_01_0003) ) {
            return true;
        }
        Object object = this.queryCustomer(idType,idNumber, externalIdentificationNo);
        CoreCustomer coreCustomer = null;
        String InstitutionId = "";
		if(object instanceof CoreCustomer){
			coreCustomer = (CoreCustomer)object;
			InstitutionId = coreCustomer.getInstitutionId();
		}else if(object instanceof CoreMediaBasicInfo){
			CoreMediaBasicInfo coreMediaBasicInfo = (CoreMediaBasicInfo)object;
			InstitutionId = coreMediaBasicInfo .getInstitutionId();
		}
        String cusOrgNo = InstitutionId;
        String corporationEntityCustomer = this.queryCustomerCorporationEntity(cusOrgNo);
        String userOrgNo = this.queryOrganByUserName(userName);
        String corporationEntityUser = this.queryCustomerCorporationEntity(userOrgNo);
        if (corporationEntityCustomer.equals(corporationEntityUser)) {
            return true;
        }
        return false;
    }

	@Override
	public CoreMediaBasicInfo queryCoreMediaBasicInfoForExt(String externalIdentificationNo) throws Exception {
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		coreMediaBasicInfoSqlBuilder.andInvalidFlagEqualTo("Y");
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		return coreMediaBasicInfo;
	}

	/**
	 * 根据外部识别号查询 媒介表,获取媒介对象(支持无效卡)
	 */
	@Override
	public List<CoreMediaBasicInfo> queryCoreMediaBasicInfoForExInvalid(String externalIdentificationNo) throws Exception {
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		List<CoreMediaBasicInfo> coreMediaBasicInfoList = coreMediaBasicInfoDao.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		return coreMediaBasicInfoList;
	}
	
	@Override
	public String queryCustomerNo(String idType, String idNumber, String externalIdentificationNo) throws Exception {
		String customerNo = "";
		if (StringUtil.isNotBlank(idType) && StringUtil.isNotBlank(idNumber)) {
			CoreCustomer coreCustomer = null;
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
			coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (null != coreCustomer) {
				customerNo = coreCustomer.getCustomerNo();
			} else {
				throw new BusinessException("CUS-00014", "客户");
			}
		} else if (StringUtil.isNotBlank(externalIdentificationNo)) {
			CoreMediaBasicInfo coreMediaBasicInfo = null;
			coreMediaBasicInfo = this.queryCoreMediaBasicInfoForExt(externalIdentificationNo);
			if (null != coreMediaBasicInfo) {
				customerNo = coreMediaBasicInfo.getMainCustomerNo();
			} else {
				throw new BusinessException("CUS-00014", "客户");
			}
		}
		if (StringUtil.isBlank(customerNo)) {
			throw new BusinessException("COR-10048");
		}
		return customerNo;
	}

	@Override
	public Object queryCustomer(String idType, String idNumber, String externalIdentificationNo) throws Exception {
		if (StringUtil.isNotBlank(idType) && StringUtil.isNotBlank(idNumber)) {
			CoreCustomer coreCustomer = null;
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
			coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (null != coreCustomer) {
				return coreCustomer;
			} else {
				throw new BusinessException("CUS-00014", "客户基本");
			}
		} else if (StringUtil.isNotBlank(externalIdentificationNo)) {
			CoreMediaBasicInfo coreMediaBasicInfo = null;
			coreMediaBasicInfo = this.queryCoreMediaBasicInfoForExt(externalIdentificationNo);
			if (null != coreMediaBasicInfo) {
				return coreMediaBasicInfo;
			} else {
				throw new BusinessException("CUS-00124");//无法定位媒介基本信息或该媒介已失效！
			}
		} else {
			throw new BusinessException("COR-10048");
		}
	}
	
	/**
	 * 根据证件类型、证件号，或者外部识别号查询客户基本信息(支持无效卡)
	 */
	@Override
	public Object queryCustomerInvalid(String idType, String idNumber, String externalIdentificationNo) throws Exception {
		if (StringUtil.isNotBlank(idType) && StringUtil.isNotBlank(idNumber)) {
			CoreCustomer coreCustomer = null;
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomerSqlBuilder.andIdTypeEqualTo(idType);
			coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (null != coreCustomer) {
				return coreCustomer;
			} else {
				throw new BusinessException("CUS-00014", "客户基本");
			}
		} else if (StringUtil.isNotBlank(externalIdentificationNo)) {
			List<CoreMediaBasicInfo> coreMediaBasicInfoList = null;
			CoreMediaBasicInfo coreMediaBasicInfo = null;
			coreMediaBasicInfoList = this.queryCoreMediaBasicInfoForExInvalid(externalIdentificationNo);
			if (null != coreMediaBasicInfoList && coreMediaBasicInfoList.size() > 0) {
				//因为只是想要取客户号的值去查询客户资料，所以这里取第一条记录，解决数据库定位多余一条问题   add by  wangxi 2019/7/8
				coreMediaBasicInfo = (CoreMediaBasicInfo) coreMediaBasicInfoList.get(0);
				return coreMediaBasicInfo;
			} else {
				throw new BusinessException("CUS-00124");//无法定位媒介基本信息或该媒介已失效！
			}
		} else {
			throw new BusinessException("COR-10048");
		}
	}

	@Override
	public CoreCustomer queryCustomer(String customerNo) throws Exception {
		CoreCustomer coreCustomer = null;
		if (StringUtil.isNotBlank(customerNo)) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
			coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		}else {
			throw new BusinessException("CUS-00078");
		}
		return coreCustomer;
	}

	@Override
	public Map<String,String> checkProductLogout(String customerNo,String productObjectCode) throws Exception{
		
		Map<String,String> map=new HashMap<String,String>();
		
		//因为在调用“根据403构件决定产品对象编码”方法所需传送运营模式参数，所以需要根据客户代码和产品对象代码去查询账户信息表中运营模式[operation_mode]
		CoreAccountSqlBuilder coreAccountSqlBuilderQuery = new CoreAccountSqlBuilder();
		if (StringUtil.isNotBlank(customerNo) && StringUtil.isNotBlank(productObjectCode)) {
			coreAccountSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
			coreAccountSqlBuilderQuery.andProductObjectCodeEqualTo(productObjectCode);
		} 
		List<CoreAccount> coreAccountList = coreAccountDaoImpl.selectListBySqlBuilder(coreAccountSqlBuilderQuery);
		
		map= this.logoutCheckMode(customerNo,productObjectCode,coreAccountList,1,0);
		return map;
	}
	
	/**
	 * @param newFlag 
	 * @param i 
	 * @param coreAccount2 
	 * 注销时检验方式
	 * @Title: logoutCheckMode   
	 * @Description: 注销时满足以下校验方式即可进行注销操作
	 * @param: @param x5810bo
	 * @param: @throws Exception      
	 * @return: void      
	 * @throws
	 */
	public Map<String,String> logoutCheckMode(String customerNo,String productObjectCode, List<CoreAccount> coreAccountList, int flag, Integer newFlag) throws Exception {
		Map<String,String> map =new HashMap<String,String>();
		//集中核算产品筛选时将产品对象替换为0
//		if(flag == 1){
//			productObjectCode = "0";
//		}
		
		//0. 注销功能开始时，先根据客户代码和产品对象代码去查询账户信息表中账户代码[account_id]
		if(null != coreAccountList && coreAccountList.size() > 0){
			
			for (CoreAccount coreAccount : coreAccountList) {
				String accountId = coreAccount.getAccountId();

				//1.查出账户代码后根据账户代码去查询余额，得出余额单元表集合后循环判断该客户所有余额[balance]是否都为0，不为0的不能进行注销操作
				CoreBalanceUnitSqlBuilder coreBalanceUnitSqlBuilderQuery = new CoreBalanceUnitSqlBuilder();
				if (StringUtil.isNotBlank(accountId)) {
					coreBalanceUnitSqlBuilderQuery.andAccountIdEqualTo(accountId);
				} 
				List<CoreBalanceUnit> coreBalanceUnitList = coreBalanceUnitDaoImpl.selectListBySqlBuilder(coreBalanceUnitSqlBuilderQuery);
				if (null != coreBalanceUnitList && coreBalanceUnitList.size() > 0) {
					for (CoreBalanceUnit coreBalanceUnit : coreBalanceUnitList) {
						BigDecimal balance = coreBalanceUnit.getBalance();
						if(balance.compareTo(BigDecimal.ZERO) != 0){ //所有余额单元余额必须为0，不为0则抛错
							if(newFlag==null){
								throw new BusinessException("CUS-00083", "当前余额不为0");
							}else{
								map.put("0001", "当前余额不为0不允许注销");
								map.put(Constant.ALLOWCANCEL,"N");
								return  map;
							}
						}
					}
				}
				
				//2.从账户基本信息表内得到数据 账户状态，判断是否 为 D-登记未释放的争议账户，不为D的才可以进行注销操作
				String statusCodeacc = coreAccount.getStatusCode();
				if (statusCodeacc.equals("D")){
					if(newFlag==null){
						throw new BusinessException("CUS-00083", "存在争议未释放账户");
					}else{
						map.put("0002", "存在争议未释放账户不允许注销");
						map.put(Constant.ALLOWCANCEL,"N");
						return  map;
					}
				}
				
				//3.根据账户代码去查询分期信息表中数据，判断该产品下分期账户中未抛金额和未抛费用都为0，不为0的不能进行注销操作
				CoreInstallmentTransAcctSqlBuilder coreInstallmentTransAcctSqlBuilderQuery = new CoreInstallmentTransAcctSqlBuilder();
				if (StringUtil.isNotBlank(accountId)) {
					coreInstallmentTransAcctSqlBuilderQuery.andAccountIdEqualTo(accountId);
				} 
				CoreInstallmentTransAcct coreInstallmentTransAcct = coreInstallmentTransAcctDaoImpl.selectBySqlBuilder(coreInstallmentTransAcctSqlBuilderQuery);
				if(coreInstallmentTransAcct!=null){
					//剩余本金-未抛金额
					BigDecimal remainPrincipalAmount = coreInstallmentTransAcct.getRemainPrincipalAmount();
					//剩余手续费-未抛费用
					BigDecimal remainFeeAmount = coreInstallmentTransAcct.getRemainFeeAmount();
					
					if ((remainPrincipalAmount.compareTo(BigDecimal.ZERO) != 0) && (remainFeeAmount.compareTo(BigDecimal.ZERO) != 0)){
						
						if(newFlag==null){
							throw new BusinessException("CUS-00083", "未抛金额或未抛费用不为0");
						}else{
							map.put("0003", "未抛金额或未抛费用不为0不允许注销");
							map.put(Constant.ALLOWCANCEL,"N");
							return  map;
						}
					}
				}
			}
		}
		
		//运营模式
		CoreCustomerSqlBuilder coreCustomerSqlBuilderQuery = new CoreCustomerSqlBuilder();
        if (StringUtil.isNotBlank(customerNo)) {
            coreCustomerSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
        } 
        CoreCustomer coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilderQuery);
        String operationMode = "";
        if(null != coreCustomer){
            operationMode = coreCustomer.getOperationMode();
        }
		
		
		//4.该产品下所有实时余额账户当前未达授权金额都为0 [授权客户额度单元信息表auth.auth_credit_unit 判断3个字段值的余额为0]  
		List<AuthCreditUnit>  authCreditUnitList = httpQueryService.queryAuthCreditUnitList(customerNo, productObjectCode, operationMode);
		if (null != authCreditUnitList && authCreditUnitList.size() > 0){
			for (AuthCreditUnit authCreditUnit : authCreditUnitList) {
				BigDecimal outstandingAmtFixed = authCreditUnit.getOutstandingAmtFixed();//未对消授权金额(固额)
				BigDecimal outstandingAmtTemp = authCreditUnit.getOutstandingAmtTemp();//未对消授权金额(临额)
				BigDecimal outstandingAmtTolerance = authCreditUnit.getOutstandingAmtTolerance();//未对消授权金额(容差)
				
				if (BigDecimal.ZERO.compareTo(outstandingAmtFixed) != 0 
						&& BigDecimal.ZERO.compareTo(outstandingAmtTemp) != 0 
							&& BigDecimal.ZERO.compareTo(outstandingAmtTolerance) != 0 ){
					if(newFlag==null){
						throw new BusinessException("CUS-00083", "未达授权金额不为0");
					}else{
						map.put("0004", "未达授权金额不为0不允许注销");
						map.put(Constant.ALLOWCANCEL,"N");
						return  map;
					}
				}
			}
		}
		
		if(newFlag==null){
			//5.如果该产品当前状态为[8-关闭]时不允许
			CoreProductSqlBuilder coreProductSqlBuilderQuery = new CoreProductSqlBuilder();
			if (StringUtil.isNotBlank(customerNo)) {
				coreProductSqlBuilderQuery.andCustomerNoEqualTo(customerNo);
				coreProductSqlBuilderQuery.andProductObjectCodeEqualTo(productObjectCode);
			} 
			CoreProduct coreProduct = coreProductDaoImpl.selectBySqlBuilder(coreProductSqlBuilderQuery);
			if(null != coreProduct){
				String statusCodepro = coreProduct.getStatusCode();
				if (statusCodepro.equals("8") && statusCodepro.equals("5")){
					throw new BusinessException("CUS-00083", "产品当前状态为关闭或注销状态");
				}
			}
		}else {
			map.put("0000", "允许注销");
			map.put(Constant.ALLOWCANCEL,"Y");
		}
		return map;
	}

}
