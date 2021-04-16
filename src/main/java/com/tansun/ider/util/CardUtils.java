package com.tansun.ider.util;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.CurrencyConversionUtil;
import com.tansun.framework.util.SpringUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.enums.PcdType;
import com.tansun.ider.service.CommonInterfaceForArtService;
import com.tansun.ider.service.business.EventCommArea;
import com.tansun.ider.service.business.common.Constant;

/**
 * 卡服务微服务工程下工具类
 * 
 * @author admin
 *
 */
@Service
public class CardUtils {

	private static Logger logger = LoggerFactory.getLogger(CardUtils.class);

    /**
     * 获取币种对应小数位
     * 
     * @param currency
     * @return
     * @throws Exception
     */
   /* public int getCurrencyDecimal(String currency) throws Exception {
        CoreCurrencySqlBuilder coreCurrencySqlBuilder = new CoreCurrencySqlBuilder();
        coreCurrencySqlBuilder.andReferredEqualTo(currency);
        CoreCurrency coreCurrency = coreCurrencyDao.selectBySqlBuilder(coreCurrencySqlBuilder);
        if (null != coreCurrency) {
            return Integer.parseInt(coreCurrency.getDecimalPlaces());
        } else {
            throw new BusinessException("COR-10001");
        }
    }*/

    /**
     * 计算中间变量
     * 
     * @param eventCommArea
     *            公共区域
     * @param currencyDecimal
     *            币种对应小数位
     * @param artifactList
     *            构件列表
     * @return
     * @throws Exception
     */
   /* public static Map<String, Object> queryDayFactory(EventCommArea eventCommArea, int currencyDecimal,
            List<CoreActivityArtifactRel> artifactList) throws Exception {
        // 验证该活动是否配置构件信息
        Boolean checkResult = checkArtifactExist(BSC.ARTIFACT_NO_911, artifactList);
        if (!checkResult) {
            throw new BusinessException("COR-10002");
        }
        Boolean result = checkArtifactExist(BSC.ARTIFACT_NO_T01, artifactList);
        if (!result) {
            throw new BusinessException("COR-10002");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>(2);
        BigDecimal dayFactory = BigDecimal.ZERO;
        int middleCurrencyDecimal = currencyDecimal;
        // 获取利息累计元件
        // 获取元件信息
        CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
        Map<String, Object> resultMap = artService.getElementByArtifact(BSC.ARTIFACT_NO_911, eventCommArea);
        Iterator<Map.Entry<String, Object>> it = resultMap.entrySet().iterator();
        String intAccrualMethod = null;
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if (Constant.YEAR_BASE_365.equals(entry.getKey())) {
                intAccrualMethod = entry.getKey();
            }
        }
        if (Constant.YEAR_BASE_365.equals(intAccrualMethod)) {
            // 获取小数位
            Map<String, Object> resultMapForTechnical = artService.getElementByArtifact(BSC.ARTIFACT_NO_T01,
                    eventCommArea);
            Iterator<Map.Entry<String, Object>> iterator = resultMapForTechnical.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (Constant.DAY_FACTOR_SEVEN.equals(entry.getKey())) {
                    dayFactory = new BigDecimal(DayFactory.DAYFACTORY_SEVEN.getValue());
                } else if (Constant.DAY_FACTOR_EIGHT.equals(entry.getKey())) {
                    dayFactory = new BigDecimal(DayFactory.DAYFACTORY_EIGHT.getValue());
                } else if (Constant.DAY_FACTOR_NINE.equals(entry.getKey())) {
                    dayFactory = new BigDecimal(DayFactory.DAYFACTORY_NINE.getValue());
                } else if (Constant.MIDDLE_CURRENCY_TWO.equals(entry.getKey())) {
                    middleCurrencyDecimal = currencyDecimal + 2;
                } else if (Constant.MIDDLE_CURRENCY_FOUR.equals(entry.getKey())) {
                    middleCurrencyDecimal = currencyDecimal + 4;
                }
            }
        }
        paramMap.put("dayFactory", dayFactory);
        paramMap.put("middleCurrencyDecimal", middleCurrencyDecimal);
        return paramMap;
    }*/

    /**
     * 验证构件是否存在
     * 
     * @param artifactNo
     *            构件编码
     * @param artifactList
     *            构件List
     * @return
     */
    public static Boolean checkArtifactExist(String artifactNo, List<CoreActivityArtifactRel> artifactList) {
        Boolean flag = Boolean.FALSE;
        if (null != artifactList && !artifactList.isEmpty()) {
            for (CoreActivityArtifactRel coreActivityArtifactRel : artifactList) {
                if (coreActivityArtifactRel.getArtifactNo().equals(artifactNo)) {
                    flag = Boolean.TRUE;
                }
            }
        }
        return flag;
    }

    /**
     * 根据【媒介单元代码】从【制卡信息】、【媒介基本信息】表中获取记录，写入【制卡文件记录】表中
     * @param media_unit_code 媒介单元代码
     * @throws Exception 
     */
    /**
     * 获取产品PCD利率值
     * 
     * @param pcdType
     *            数值类型
     * @param pcdValue
     *            数值
     * @param pcdPoint
     *            小数位
     * @return
     * @throws Exception
     */
    public static BigDecimal getProductPcdValue(String pcdType, String pcdValue, String pcdPoint) throws Exception {
        BigDecimal finalPcdValue = BigDecimal.ZERO;
        if (PcdType.D.getValue().equals(pcdType)) {
            // D：数值型
            // 默认小数位为0
            int point = 0;
            if (StringUtil.isNotBlank(pcdPoint) && StringUtil.isNotEmpty(pcdPoint)) {
                point = Integer.parseInt(pcdPoint);
            }
            finalPcdValue = CurrencyConversionUtil.reduce(new BigDecimal(pcdValue), point);
        } else if (PcdType.P.getValue().equals(pcdType)) {
            // 百分比
            finalPcdValue = CurrencyConversionUtil.reduce(new BigDecimal(pcdValue), 2);
        }
        return finalPcdValue;
    }
    
	/**
	 * 
	 * @Description: 查询是否可以转卡
	 * @param x5070bo
	 * @param coreMediaBasicInfo2
	 * @return
	 * @throws Exception
	 */
	// 根据媒介对象号调用通用程序“活动获取元件”，取得当前媒介生效元件
	// 若元件编号为“302AAA0100”，则将“媒介类型是否可转卡”赋值为Y，控制挂失可转卡，转卡媒介查询“是否转卡”不允许选“是”，选择“是”报错；
	// 若元件编号为“302AAA0101”，则将“媒介类型是否可转卡”赋值为N ，控制挂失不可转卡，转卡媒介查询“是否转卡”可选“是”。
	public static String getTransferCard(EventCommArea eventCommArea,
			List<CoreActivityArtifactRel> artifactList, CoreMediaBasicInfo coreMediaBasicInfo2) throws Exception {
		CommonInterfaceForArtService artService = SpringUtil.getBean(CommonInterfaceForArtService.class);
		// 运营模式
		eventCommArea.setEcommOperMode(coreMediaBasicInfo2.getOperationMode());
		eventCommArea.setEcommMediaObjId(coreMediaBasicInfo2.getMediaObjectCode());
		String transferCard = "";
		for (CoreActivityArtifactRel dto : artifactList) {
			// 6.1 通过构件获取元件，公共程序处理CoreArtifactKey
			Map<String, String> elePcdResultMap = artService.getElementByArtifact(dto.getArtifactNo(), eventCommArea);
			// 6.2 应对POC演示选取必要元件
			Iterator<Map.Entry<String, String>> it = elePcdResultMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				if (Constant.TRANSFER_CARD_FLAG.equals(entry.getKey())) {
					// 是否可转卡标志
					transferCard = "Y";
				} else if (Constant.TRANSFER_CARD_TYPE.equals(entry.getKey())) {
					// 是否可转卡标志
					transferCard = "N";
				}
				if (logger.isDebugEnabled()) {
					logger.debug("元件编号={},  and PCD信息={} ", entry.getKey(), entry.getValue());
				}
			}
		}

		return transferCard;
	}
    
}
