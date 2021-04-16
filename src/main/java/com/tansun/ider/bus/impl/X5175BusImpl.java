package com.tansun.ider.bus.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.ider.bus.X5175Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.CoreMediaBasicInfoDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.entity.CoreMediaBind;
import com.tansun.ider.dao.issue.impl.CoreMediaBindDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBindSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X5175BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 新媒介绑定
 * @author: admin
 */
@Service
public class X5175BusImpl implements X5175Bus {

	@Autowired
	private CoreMediaBindDaoImpl coreMediaBindDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
	@Autowired
	private CoreMediaBasicInfoDao coreMediaBasicInfoDao;
	@Autowired
	private HttpQueryService httpQueryService;

	@Override
	public Object busExecute(X5175BO x5175bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		String operatorId = x5175bo.getOperatorId();
		@SuppressWarnings("unused")
		List<CoreCustomerAddr> listCoreCustomerAddrs = x5175bo.getCoreCoreCustomerAddrs();
		CachedBeanCopy.copyProperties(x5175bo, eventCommAreaNonFinance);
		String mediaObjectCode = eventCommAreaNonFinance.getMediaObjectCode();
		String mediaUnitCode = eventCommAreaNonFinance.getMediaUnitCode();
		String bindId = eventCommAreaNonFinance.getBindId();

		CoreMediaBindSqlBuilder coreMediaBindSqlBuilder = new CoreMediaBindSqlBuilder();
		coreMediaBindSqlBuilder.andMediaObjectCodeEqualTo(mediaObjectCode);
		coreMediaBindSqlBuilder.andMediaUnitCodeEqualTo(mediaUnitCode);
		List<CoreMediaBind> listCoreMediaBinds = coreMediaBindDaoImpl.selectListBySqlBuilder(coreMediaBindSqlBuilder);
		
		if (operatorId == null) {
			operatorId = "system";
		}

		CoreMediaBind coreMediaBinds = null;

		if (listCoreMediaBinds.size() > 0) {

			Collections.sort(listCoreMediaBinds, new Comparator<CoreMediaBind>() {

				@Override
				public int compare(CoreMediaBind o1, CoreMediaBind o2) {
					// 按照顺序号排序
					return o1.getSerialNo() - o2.getSerialNo();
				}
			});
			coreMediaBinds = listCoreMediaBinds.get(listCoreMediaBinds.size() - 1);
		}
		CoreMediaBind coreMediaBind = new CoreMediaBind();
		coreMediaBind.setBindDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
		coreMediaBind.setBindId(bindId);
		coreMediaBind.setId(RandomUtil.getUUID());
		coreMediaBind.setMediaObjectCode(mediaObjectCode);
		coreMediaBind.setMediaUnitCode(mediaUnitCode);
		if (coreMediaBinds != null) {
			coreMediaBind.setSerialNo(coreMediaBinds.getSerialNo() + 1);
		} else {
			coreMediaBind.setSerialNo(1);
		}
		coreMediaBind.setVersion(1);
		@SuppressWarnings("unused")
		int result = coreMediaBindDaoImpl.insert(coreMediaBind);
		
		CoreMediaBasicInfo coreMediaBasicInfoStr = new CoreMediaBasicInfo();
		CachedBeanCopy.copyProperties(eventCommAreaNonFinance, coreMediaBasicInfoStr);
		CachedBeanCopy.copyProperties(coreMediaBasicInfoStr, x5175bo);
		
		//媒介单元基本信息表获取客户代码
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(x5175bo.getExternalIdentificationNo());
		CoreMediaBasicInfo coreMediaBasicInfo = coreMediaBasicInfoDao.selectBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		if(null != coreMediaBasicInfo){
			//通过客户号获取系统单元编号
			String customerNo = coreMediaBasicInfo.getMainCustomerNo();
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();   
			coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
			CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
			if(null != coreCustomer){
				//通过系统单元编号获取当前日志标识
				String systemUnitNo = coreCustomer.getSystemUnitNo();
//				CoreSystemUnitSqlBuilder coreSystemUnitSqlBuilder = new CoreSystemUnitSqlBuilder();
//				coreSystemUnitSqlBuilder.andSystemUnitNoEqualTo(systemUnitNo);
//				CoreSystemUnit coreSystemUnit = coreSystemUnitDao.selectBySqlBuilder(coreSystemUnitSqlBuilder);
				CoreSystemUnit coreSystemUnit =	httpQueryService.querySystemUnit(systemUnitNo);
				String currLogFlag = coreSystemUnit.getCurrLogFlag();
				x5175bo.setCurrLogFlag(currLogFlag);
				
				// 记录非金融日志，内容更新。
				nonFinancialLogUtil.createNonFinancialActivityLog(x5175bo.getEventNo(), x5175bo.getActivityNo(),
						ModificationType.ADD.getValue(), null, null, coreMediaBind, coreMediaBind.getId(),
						x5175bo.getCurrLogFlag(), operatorId, customerNo,
						coreMediaBind.getMediaUnitCode(), null,null);
			}
			
		}
		
		return eventCommAreaNonFinance;
	}

}
