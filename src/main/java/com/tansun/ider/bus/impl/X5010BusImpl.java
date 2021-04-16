package com.tansun.ider.bus.impl;

import java.util.List;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5010Bus;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.issue.CoreCustomerDao;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.impl.CoreCustomerAddrDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerAddrSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.model.bo.X5010BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 客户地址新增
 * @author: admin
 */
@Service
public class X5010BusImpl implements X5010Bus {

	@Resource
	private CoreCustomerAddrDaoImpl coreCustomerAddrDaoImpl;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private CoreCustomerDao coreCustomerDao;
//	@Autowired
//	private CoreSystemUnitDao coreSystemUnitDao;
	@Autowired
	private HttpQueryService httpQueryService;
	
	/**
	 * 1. 新增地址，判断地址记录内容是否大于一条，是否小于四条
	 */
	@Override
	public Object busExecute(X5010BO x5010bo) throws Exception {
		
		//判断是否新卡新媒介
		if (StringUtil.isNotBlank(x5010bo.getIsNew())) {
			if ("1".equals(x5010bo.getIsNew())) {
				return x5010bo;
			}
		}
		
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5010bo, eventCommAreaNonFinance);
		String operatorId = x5010bo.getOperatorId();
		List<CoreCustomerAddr> listCoreCustomerAddrs = x5010bo.getCoreCoreCustomerAddrs();
		String customerNo = eventCommAreaNonFinance.getCustomerNo();
		CoreCustomerAddrSqlBuilder coreCustomerAddrSqlBuilder = new CoreCustomerAddrSqlBuilder();
		coreCustomerAddrSqlBuilder.andCustomerNoEqualTo(customerNo);
		List<CoreCustomerAddr> listCoreCustomerAddrs1 = coreCustomerAddrDaoImpl
				.selectListBySqlBuilder(coreCustomerAddrSqlBuilder);
		int i;
		if (listCoreCustomerAddrs1 == null || listCoreCustomerAddrs1.isEmpty()) {
			i = 1;
		} else {
			i = listCoreCustomerAddrs1.size() + 1;
		}
		if (operatorId == null) {
			operatorId = "system";
		}
		
		if (listCoreCustomerAddrs != null && listCoreCustomerAddrs.size() > 0) {
			for (CoreCustomerAddr coreCustomerAddr : listCoreCustomerAddrs) {
				coreCustomerAddr.setCustomerNo(eventCommAreaNonFinance.getCustomerNo());
				coreCustomerAddr.setId(RandomUtil.getUUID());
				if (null == coreCustomerAddr.getType()) {
					coreCustomerAddr.setType(coreCustomerAddr.getType());
				}
				coreCustomerAddr.setSerialNo(i++);
				coreCustomerAddr.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
				coreCustomerAddr.setVersion(1);
				coreCustomerAddrDaoImpl.insert(coreCustomerAddr);
				// 记录非金融日志，内容更新。
				CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();   
				coreCustomerSqlBuilder.andCustomerNoEqualTo(customerNo);
				CoreCustomer coreCustomer = coreCustomerDao.selectBySqlBuilder(coreCustomerSqlBuilder);
				if(null != coreCustomer){
					//通过系统单元编号获取当前日志标识
					String systemUnitNo = coreCustomer.getSystemUnitNo();
//					CoreSystemUnitSqlBuilder coreSystemUnitSqlBuilder = new CoreSystemUnitSqlBuilder();
//					coreSystemUnitSqlBuilder.andSystemUnitNoEqualTo(systemUnitNo);
//					CoreSystemUnit coreSystemUnit = coreSystemUnitDao.selectBySqlBuilder(coreSystemUnitSqlBuilder);
					CoreSystemUnit coreSystemUnit =httpQueryService.querySystemUnit(systemUnitNo);
					String currLogFlag = coreSystemUnit.getCurrLogFlag();
					x5010bo.setCurrLogFlag(currLogFlag);
					nonFinancialLogUtil.createNonFinancialActivityLog(x5010bo.getEventNo(), x5010bo.getActivityNo(),
							ModificationType.ADD.getValue(), null, null, coreCustomerAddr, coreCustomerAddr.getId(),
							x5010bo.getCurrLogFlag(), operatorId, coreCustomerAddr.getCustomerNo(),
							coreCustomerAddr.getType().toString(), null,null);
				}
			}
		}
		for (CoreCustomerAddr coreCustomerAddrs : listCoreCustomerAddrs) {
			if (coreCustomerAddrs.getType() == 1) {
				eventCommAreaNonFinance.setType(coreCustomerAddrs.getType());
				eventCommAreaNonFinance.setContactAddress(coreCustomerAddrs.getContactAddress());
				eventCommAreaNonFinance.setContactPostCode(coreCustomerAddrs.getContactPostCode());
				eventCommAreaNonFinance.setContactMobilePhone(coreCustomerAddrs.getContactMobilePhone());
				eventCommAreaNonFinance.setCity(coreCustomerAddrs.getCity());
			}
		}
		return eventCommAreaNonFinance;
	}

}
