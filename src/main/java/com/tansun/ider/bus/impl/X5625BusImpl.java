package com.tansun.ider.bus.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5625Bus;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgram;
import com.tansun.ider.dao.beta.entity.CoreBusinessProgramScope;
import com.tansun.ider.dao.beta.entity.CoreProductBusinessScope;
import com.tansun.ider.dao.beta.entity.CoreProductObject;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreAccountDao;
import com.tansun.ider.dao.issue.CoreCustomerBillDayDao;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerBillDay;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.sqlbuilder.CoreAccountSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerBillDaySqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.enums.ProductType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5625BO;
import com.tansun.ider.model.vo.X5625VO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommArea;

@Service
public class X5625BusImpl implements X5625Bus{

	@Resource
	private CoreCustomerDao coreCustomerDao;
	@Resource
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Resource
	private HttpQueryService httpQueryService;
	@Resource
	private CoreCustomerBillDayDao coreCustomerBillDayDao;
	@Resource
	private CoreAccountDao coreAccountDao;
	@Override
	public Object busExecute(X5625BO x5625bo) throws Exception {
		if(StringUtil.isEmpty(x5625bo.getEcommEntryId())){
			throw new BusinessException("CUS-00058");
		}
		//外部识别号码—>主客户代码+运营模式
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(x5625bo.getEcommEntryId());
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if(coreMediaBasicInfo==null){
			throw new BusinessException("CUS-00018");
		}
	
		//主客户代码->客户证件号+证件类型
		CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
		coreCustomerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
		CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
		if(coreCustomer==null){
			throw new BusinessException("CUS-00005");
		}
		//产品对象代码+运营模式->业务项目代码 
		List<CoreProductBusinessScope> coreProductBusinessScope = httpQueryService.queryProductBusinessScope(coreMediaBasicInfo.getProductObjectCode(),coreMediaBasicInfo.getOperationMode());
		if(coreProductBusinessScope.size()==0){
			throw new BusinessException("CUS-00059");
		}
		//查询产品信息
		CoreProductObject  coreProductObject  = httpQueryService.queryProductObject(coreMediaBasicInfo.getOperationMode(), coreMediaBasicInfo.getProductObjectCode());
		if(coreProductObject==null){
			throw new BusinessException("CUS-00006");
		}
		//业务项目代码+客户号->业务所属类型
/*		CoreAccountSqlBuilder coreAccountSqlBuilder = new CoreAccountSqlBuilder();
		coreAccountSqlBuilder.andBusinessProgramNoEqualTo(coreProductBusinessScope.get(0).getBusinessProgramNo());
		coreAccountSqlBuilder.andCustomerNoEqualTo(coreCustomer.getCustomerNo());
		CoreAccount coreAccount = coreAccountDao.selectBySqlBuilder(coreAccountSqlBuilder);
		if(coreAccount==null){
			throw new BusinessException("CUS-00006");
		}*/
		 // 查询业务项目范围
        List<CoreBusinessProgramScope> CoreBusinessProgramScope = httpQueryService.queryBusinessProgramScope(coreProductBusinessScope.get(0).getBusinessProgramNo(),coreMediaBasicInfo.getOperationMode());
        if(CoreBusinessProgramScope.size()==0){
			throw new BusinessException("CUS-00063");
		}
		//业务项目代码+主客户代码->账单日等
        String businessProgramNo = coreProductBusinessScope.get(0).getBusinessProgramNo();
    	checkProductType(coreMediaBasicInfo.getOperationMode(),businessProgramNo);
		CoreCustomerBillDaySqlBuilder coreCustomerBillDaySqlBuilder = new CoreCustomerBillDaySqlBuilder();
		coreCustomerBillDaySqlBuilder.andBusinessProgramNoEqualTo(coreProductBusinessScope.get(0).getBusinessProgramNo());
		coreCustomerBillDaySqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo.getMainCustomerNo());
		CoreCustomerBillDay coreCustomerBillDay = coreCustomerBillDayDao.selectBySqlBuilder(coreCustomerBillDaySqlBuilder);
		if(coreCustomerBillDay==null){
			throw new BusinessException("CUS-00060");
		}
		//填装返回
		X5625VO x5625vo = new X5625VO();
		x5625vo.setIdNumber(coreCustomer.getIdNumber());
		x5625vo.setIdType(coreCustomer.getIdType());
		x5625vo.setEcommEntryId(x5625bo.getEcommEntryId());
		x5625vo.setEcommProdObjId(coreMediaBasicInfo.getProductObjectCode());
		x5625vo.setEcommBusineseType(CoreBusinessProgramScope.get(0).getBusinessTypeCode());
		x5625vo.setEcommBusinessProgramCode(coreProductBusinessScope.get(0).getBusinessProgramNo());
		x5625vo.setEcommCustId(coreCustomer.getCustomerNo());
		x5625vo.setEcommCustName(coreCustomer.getCustomerName());
		x5625vo.setProductDesc(coreProductObject.getProductDesc());
		//如果账单日为0则调用paramService
		if(coreCustomerBillDay.getBillDay()==0){
			CoreSystemUnit coreSystemUnit = httpQueryService.querySystemUnit(coreCustomer.getSystemUnitNo());
			if(coreSystemUnit==null){
				throw new BusinessException("CUS-00061");
			}
			//如果是“01、02”之类的只给1、2
			String nextProcessDate = coreSystemUnit.getNextProcessDate();
			if(nextProcessDate.substring(nextProcessDate.length()-2).startsWith("0")){
				x5625vo.setRepayDay(Integer.parseInt(nextProcessDate.substring(nextProcessDate.length()-1)));
			}else{
				String billDay = coreSystemUnit.getNextProcessDate().substring(nextProcessDate.length()-2);
				//如果是29、30、31给28
				if("29".equals(billDay)||"30".equals(billDay)||"31".equals(billDay)){
					x5625vo.setRepayDay(28);
				}else{
					//正常直接装填
					x5625vo.setRepayDay(Integer.parseInt(billDay));
				}
			}
		}else{
			//不为零直接装填
			x5625vo.setRepayDay(coreCustomerBillDay.getBillDay());
		}
		return x5625vo;
	}
	
    /**
     * 验证产品是否是信贷产品
     * 
     * @param ecommOperMode
     * @param ecommProdObjId
     * @return
     */
    public void checkProductType(String ecommOperMode,String businessProgramNo) throws Exception{
    	CoreBusinessProgram businessProgram = httpQueryService.queryBusinessProgram(ecommOperMode, businessProgramNo);
    	if(businessProgram == null){
    		//TODO产品不存在
    		throw new BusinessException("COR-10013");
    	}
    	if(!ProductType.LOAN.getValue().equals(businessProgram.getProgramType())){
    		throw new BusinessException("COR-12017");
    	}
    }

}
