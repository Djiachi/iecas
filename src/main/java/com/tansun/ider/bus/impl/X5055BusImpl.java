package com.tansun.ider.bus.impl;

import java.util.Date;
import java.util.Map;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.DateUtil;
import com.tansun.framework.util.RandomUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5055Bus;
import com.tansun.ider.dao.beta.entity.CoreMediaObject;
import com.tansun.ider.dao.issue.CoreCustomerAddrDao;
import com.tansun.ider.dao.issue.CoreMediaCardInfoDao;
import com.tansun.ider.dao.issue.entity.CoreMediaCardInfo;
import com.tansun.ider.enums.InvalidReasonStatus;
import com.tansun.ider.enums.ModificationType;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5055BO;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.MapTransformUtils;
import com.tansun.ider.util.NonFinancialLogUtil;

/**
 * @version:1.0
 * @Description: 媒介制卡信息新建
 * @author: admin
 */
@Service
public class X5055BusImpl implements X5055Bus {

	@Autowired
	private CoreMediaCardInfoDao coreMediaCardInfoDao;
	@Autowired
	private NonFinancialLogUtil nonFinancialLogUtil;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private CoreCustomerAddrDao coreCustomerAddrDao;
	public static final String YU_ZHI_KA="yuzhika";
	@Override
	public Object busExecute(X5055BO x5055bo) throws Exception {
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5055bo, eventCommAreaNonFinance);
		String invalidReason = eventCommAreaNonFinance.getInvalidReason();
		String operatorId = x5055bo.getOperatorId();
		// 新发卡
		if (operatorId == null) {
			operatorId = "system";
		}
		if (null == invalidReason) {
			@SuppressWarnings("unused")
			Map<String, Object> map = MapTransformUtils.objectToMap(eventCommAreaNonFinance);
			/**
			 * 获取媒介对象代码 通过媒介对象代码查询出是实体卡还是虚拟卡,根据不同的标志查询 媒介类型:R实体卡,V虚拟卡 新增
			 */
//			CoreMediaObjectSqlBuilder coreMediaObjectSqlBuilder = new CoreMediaObjectSqlBuilder();
//			coreMediaObjectSqlBuilder.andMediaObjectCodeEqualTo(x5055bo.getMediaObjectCode());
//			CoreMediaObject coreMediaObject = coreMediaObjectDaoImpl.selectBySqlBuilder(coreMediaObjectSqlBuilder);
			
			CoreMediaObject coreMediaObject = httpQueryService.queryMediaObject(eventCommAreaNonFinance.getOperationMode(), eventCommAreaNonFinance.getMediaObjectCode());
			
			if (!"V".equals(coreMediaObject.getMediaObjectType())&&!YU_ZHI_KA.equals(eventCommAreaNonFinance.getEmbosserName1())) {
				CoreMediaCardInfo coreMediaCardInfo = new CoreMediaCardInfo();
				coreMediaCardInfo.setMediaUnitCode(eventCommAreaNonFinance.getMediaUnitCode());
				coreMediaCardInfo.setId(RandomUtil.getUUID());
				coreMediaCardInfo.setProductionCode("1");
				// 媒介制卡信息
				coreMediaCardInfo.setProductionDate(eventCommAreaNonFinance.getOperationDate());
				coreMediaCardInfo.setEmbosserName1(eventCommAreaNonFinance.getEmbosserName1());
				coreMediaCardInfo.setFormatCode(eventCommAreaNonFinance.getFormatCode());
				coreMediaCardInfo.setVersion(1);
				coreMediaCardInfo.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
				@SuppressWarnings("unused")
				int i = coreMediaCardInfoDao.insert(coreMediaCardInfo);
				nonFinancialLogUtil.createNonFinancialActivityLog(x5055bo.getEventNo(), x5055bo.getActivityNo(),
						ModificationType.ADD.getValue(), null, null, coreMediaCardInfo, coreMediaCardInfo.getId(),
						x5055bo.getCurrLogFlag(), operatorId, x5055bo.getCustomerNo(),
						coreMediaCardInfo.getMediaUnitCode(), null,null);
			}
			
			//增加校验：保存媒介制卡信息时，增加对卡版代码字段的判断，如果媒介对象为虚拟卡，无需输入卡版代码	add by wangxi 20190507 
			if("V".equals(coreMediaObject.getMediaObjectType())){
				String formatCode = eventCommAreaNonFinance.getFormatCode();
				if(StringUtil.isNotBlank(formatCode)){
					throw new BusinessException("CUS-00088");
				}
				
			}
			
			eventCommAreaNonFinance.setServiceCode(coreMediaObject.getServiceCode());
		} else if (InvalidReasonStatus.TRF.getValue().equals(invalidReason)
				|| InvalidReasonStatus.BRK.getValue().equals(invalidReason)
				|| InvalidReasonStatus.EXP.getValue().equals(invalidReason)
				|| InvalidReasonStatus.PNA.getValue().equals(invalidReason)
				|| InvalidReasonStatus.CHP.getValue().equals(invalidReason)) {
			CoreMediaCardInfo coreMediaCardInfo = new CoreMediaCardInfo();
			CachedBeanCopy.copyProperties(eventCommAreaNonFinance, coreMediaCardInfo);
			coreMediaCardInfo.setId(RandomUtil.getUUID());
			//productionCode制卡请求 0：无请求 1：新发卡制卡 2：到期续卡制卡 3：毁损补发制卡 4：挂失换卡制卡 5：提前续卡制卡 6: 升降级制卡
			if (InvalidReasonStatus.TRF.getValue().equals(invalidReason)) {
				coreMediaCardInfo.setProductionCode("4");//4：挂失换卡制卡 
			} else if (InvalidReasonStatus.BRK.getValue().equals(invalidReason)) {
				coreMediaCardInfo.setProductionCode("3");//3：毁损补发制卡
			} else if (InvalidReasonStatus.EXP.getValue().equals(invalidReason)) {
				coreMediaCardInfo.setProductionCode("2");// 2：到期续卡制卡
			}else if(InvalidReasonStatus.PNA.getValue().equals(invalidReason)){
				coreMediaCardInfo.setProductionCode("5");//5：提前续卡制卡
				coreMediaCardInfo.setProductionDate(eventCommAreaNonFinance.getOperationDate());
			}else if(InvalidReasonStatus.CHP.getValue().equals(invalidReason)){
                coreMediaCardInfo.setProductionCode("6");//6: 升降级制卡
            }
			coreMediaCardInfo.setGmtCreate(DateUtil.format(new Date(), "YYYY-MM-dd"));//修复gmt_create值不正确bug DD换dd add by wangxi 2019/7/11
			coreMediaCardInfo.setVersion(1);
			coreMediaCardInfo.setEmbosserName1(eventCommAreaNonFinance.getEmbosserName1());
			coreMediaCardInfoDao.insert(coreMediaCardInfo);
			nonFinancialLogUtil.createNonFinancialActivityLog(x5055bo.getEventNo(), x5055bo.getActivityNo(),
					ModificationType.ADD.getValue(), null, null, coreMediaCardInfo, coreMediaCardInfo.getId(),
					x5055bo.getCurrLogFlag(), operatorId, x5055bo.getCustomerNo(), coreMediaCardInfo.getMediaUnitCode(),
					null,null);
		}
		eventCommAreaNonFinance.setAuthDataSynFlag("1");
		return eventCommAreaNonFinance;
	}
	  @Override
	    public int createExecute(X5055BO x5055bo) throws Exception {
	        CoreMediaCardInfo coreMediaCardInfo = new CoreMediaCardInfo();
	        CachedBeanCopy.copyProperties(x5055bo, coreMediaCardInfo);
	        coreMediaCardInfo.setId(RandomUtil.getUUID());
	        coreMediaCardInfo.setVersion(1);
	        coreMediaCardInfo.setGmtCreate(DateUtil.format(null, DateUtil.FORMAT_DATETIME));
	        return coreMediaCardInfoDao.insert(coreMediaCardInfo);
	    }
}
