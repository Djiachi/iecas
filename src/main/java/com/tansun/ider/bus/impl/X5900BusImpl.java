package com.tansun.ider.bus.impl;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5900Bus;
import com.tansun.ider.dao.beta.entity.CoreHoliday;
import com.tansun.ider.dao.beta.entity.CoreSystemUnit;
import com.tansun.ider.dao.beta.sqlbuilder.CoreSystemUnitSqlBuilder;
import com.tansun.ider.enums.HolidayStatusType;
import com.tansun.ider.enums.SystemOperateState;
import com.tansun.ider.framwork.exception.BusinessException;
import com.tansun.ider.model.bo.X5900BO;
import com.tansun.ider.service.BetaCommonParamService;
import com.tansun.ider.service.HttpQueryService;
import com.tansun.ider.service.business.common.Constant;
import com.tansun.ider.util.MapTransformUtils;
import com.tansun.ider.web.WSC;

/**
 * @version:1.0
 * @Description: 切日
 * @author: admin
 */
@Service
public class X5900BusImpl implements X5900Bus {

	/**
	 * 记录日志
	 */
	private static Logger logger = LoggerFactory.getLogger(X5900BusImpl.class);

	// @Autowired
	// private CoreSystemUnitDao coreSystemUnitDao;
	// @Autowired
	// private CoreHolidayDao coreHolidayDao;
	@Autowired
	private HttpQueryService httpQueryService;
	@Autowired
	private BetaCommonParamService betaCommonParamService;

	@Override
	public Object busExecute(X5900BO x5900bo) throws Exception {
		// CoreSystemUnitSqlBuilder coreSystemUnitSqlBuilder = new
		// CoreSystemUnitSqlBuilder();
		// 100
		// coreSystemUnitSqlBuilder.andSystemUnitNoNotEqualTo("100");
		// B01
		// coreSystemUnitSqlBuilder.andSystemUnitNoEqualTo("100");
		// List<CoreSystemUnit> coreSystemUnitList = null;
		Map<String, String> paramsSystemUnitMap = new HashMap<String, String>();
		try {
			paramsSystemUnitMap.put(Constant.REQUEST_TYPE_STR, Constant.REQUEST_TYPE);
			paramsSystemUnitMap.put(Constant.RESULT_TYPE_STR, Constant.REQUEST_TYPE);
			paramsSystemUnitMap.put(WSC.REDIS_KEY, "ALL");
			// 给总控的参数中增加一个字段"requestType", 0, 为内部请求，1为外部请求，当为外部请求时即是发卡或授权请求时
			List<CoreSystemUnit> coreSystemUnitList = betaCommonParamService
					.queryCoreSystemUnitList(paramsSystemUnitMap);

			for (CoreSystemUnit coreSystemUnit : coreSystemUnitList) {
//				if (!coreSystemUnit.getSystemUnitNo().equals("101")) {
//					continue;
//				}
				//  上一处理日期 = 当前处理日期
				coreSystemUnit.setLastProcessDate(coreSystemUnit.getCurrProcessDate());
				// 下一处理日期
				String operationDate = coreSystemUnit.getNextProcessDate();
				
				String weekForDays = getNextProcessDate(operationDate);
//				String currProcessDateNow = coreSystemUnit.getCurrProcessDate();
				// 当前处理日期
				try {
					boolean flag_1 = false;
					String tomorrowStr = coreSystemUnit.getNextProcessDate();
					CoreHoliday coreHoliday = null;
					// 判断明天是星期几，然后根据星期几在获取第几位。
//					int weekForDay = dayForWeek(operationDate);
//					int weekForDayLast = 0;
//					if (weekForDay == 1) {
//						weekForDayLast = 0;
//					} else {
//						weekForDayLast = weekForDay - 1;
//					}
					// 系统单元表“系统处理日标识"
					do {
						int weekForDay = dayForWeek(weekForDays);
						int weekForDayLast = 0;
						if (weekForDay == 1) {
							weekForDayLast = 0;
						} else {
							weekForDayLast = weekForDay - 1;
						}
						String systemProcessDayFlag = coreSystemUnit.getSystemProcessDayFlag().substring(weekForDayLast,
								weekForDay);
						String month = weekForDays.substring(5, 7);
						String year = weekForDays.substring(0, 4);
						if (HolidayStatusType.O.getValue().equals(systemProcessDayFlag)) {
							boolean flag_C = true;
							do {
								coreHoliday = httpQueryService.queryHoliday(coreSystemUnit.getHolidayNo(), year + month,
										HolidayStatusType.Y.getValue());
								if (null == coreHoliday) {
									flag_C = false;
									flag_1 = false;
								} else {
									// flag_C = true;
									Map<String, Object> map = MapTransformUtils.objectToMap(coreHoliday);
									String dd = weekForDays.substring(8, 10);
									String value = getMapValue(map, "holidayDate" + dd);
									if (StringUtil.isNotBlank(value) && HolidayStatusType.C.getValue().equals(value)) {
										flag_1 = true;
										flag_C = false;
										// 则跳过该日期,继续增加一天并检查
										String month_C = weekForDays.substring(5, 7);
										String year_C = weekForDays.substring(0, 4);
										weekForDays = getNextProcessDate(weekForDays);
//										tomorrowStr = getNextProcessDate(tomorrowStr);
										if (!year.equals(year_C)) {
											year = year_C;
										}
										if (!month.equals(month_C)) {
											month = month_C;
										}
									} else {
										// 如果未找到记录，或者未设置例外的，则将该日期作为下一处理日期；
										flag_C = false;
										flag_1 = false;
									}
								}
							} while (flag_C);
						} else if (HolidayStatusType.C.getValue().equals(systemProcessDayFlag)) {
							boolean flag_O = true;
							do {
								// 非处理日-非处理日批次标识
								String nonProDayBatchFlag = coreSystemUnit.getNonProDayBatchFlag();
								if (HolidayStatusType.NN.getValue().equals(nonProDayBatchFlag)) {
									coreHoliday = httpQueryService.queryHoliday(coreSystemUnit.getHolidayNo(),
											year + month, HolidayStatusType.Y.getValue());
									if (null == coreHoliday) {
										flag_O = false;
										flag_1 = true;
										String month_O = weekForDays.substring(5, 7);
										String year_O = weekForDays.substring(0, 4);
										weekForDays = getNextProcessDate(weekForDays);
//										tomorrowStr = getNextProcessDate(tomorrowStr);
//										String month_O = tomorrowStr.substring(5, 7);
										if (!month.equals(month_O)) {
											flag_O = false;
											month = month_O;
										}
//										String year_O = tomorrowStr.substring(0, 4);
										if (!year.equals(year_O)) {
											year = year_O;
										}
									} else {
										// 非处理日批次标识为“N-不执行批次”时，再检查如果该日期在假日表中设置了例外（O-处理日），则将该日期作为下一处理日；否则跳过该日期；
										Map<String, Object> map = MapTransformUtils.objectToMap(coreHoliday);
										String dd = weekForDays.substring(8, 10);
//										String month_O = weekForDays.substring(5, 7);
//										String year_O = weekForDays.substring(0, 4);
										String value = getMapValue(map, "holidayDate" + dd);
										if (StringUtil.isBlank(value)) {
											flag_O = false;
											flag_1 = true;
											String month_O = weekForDays.substring(5, 7);
											String year_O = weekForDays.substring(0, 4);
											weekForDays = getNextProcessDate(weekForDays);
											tomorrowStr = getNextProcessDate(tomorrowStr);
//											String month_O = tomorrowStr.substring(5, 7);
											if (!month.equals(month_O)) {
												flag_O = false;
												month = month_O;
											}
//											String year_O = tomorrowStr.substring(0, 4);
											if (!year.equals(year_O)) {
												year = year_O;
											}
										}else {
											if (HolidayStatusType.O.getValue().equals(value)) {
												flag_O = false;
												flag_1 = false;
											} else {
												flag_1 = true;
												flag_O = false;
												String month_O = weekForDays.substring(5, 7);
												String year_O = weekForDays.substring(0, 4);
												weekForDays = getNextProcessDate(weekForDays);
//												tomorrowStr = getNextProcessDate(tomorrowStr);
//												String month_O = tomorrowStr.substring(5, 7);
												if (!month.equals(month_O)) {
													flag_O = false;
													month = month_O;
												}
//												String year_O = tomorrowStr.substring(0, 4);
												if (!year.equals(year_O)) {
													year = year_O;
												}
											}
										}
									}
								} else {
									// 非处理日批次标识为“Y-正常执行批次”时，将该日期作为下一处理日期
									flag_O = false;
									flag_1 = false;
								}
							} while (flag_O);
						} else {
							flag_1 = false;
						}
					} while (flag_1);
					tomorrowStr = weekForDays;
					String lastProcessDate = coreSystemUnit.getCurrProcessDate();

					String nextProcessDate = coreSystemUnit.getNextProcessDate();
					// 下一处理日期
					coreSystemUnit.setNextProcessDate(tomorrowStr);
					// 系统运行状态
					coreSystemUnit.setSystemOperateState(SystemOperateState.EOD.getValue());
					// 营业日期
//					coreSystemUnit.setOperationDate(tomorrowStr);
					// 当前处理日期
					coreSystemUnit.setCurrProcessDate(nextProcessDate);
					String currLogFlag = coreSystemUnit.getCurrLogFlag();
					String nextLogFlag = coreSystemUnit.getNextLogFlag();
					coreSystemUnit.setCurrLogFlag(nextLogFlag);
					coreSystemUnit.setNextLogFlag(currLogFlag);
					coreSystemUnit.setVersion(coreSystemUnit.getVersion() + 1);
					CoreSystemUnitSqlBuilder coreSystemUnitSqlBuilderStr = new CoreSystemUnitSqlBuilder();
					coreSystemUnitSqlBuilderStr.andIdEqualTo(coreSystemUnit.getId());
					// CoreSystemUnit CoreSystemUnitNew = coreSystemUnitDao
					// .selectBySqlBuilder(coreSystemUnitSqlBuilderStr);
					CoreSystemUnit CoreSystemUnitNew = httpQueryService
							.querySystemUnit(coreSystemUnit.getSystemUnitNo());
					if (null != CoreSystemUnitNew) {
						// coreSystemUnitSqlBuilderStr.andVersionEqualTo(CoreSystemUnitNew.getVersion());
						// int result =
						// coreSystemUnitDao.updateBySqlBuilderSelective(coreSystemUnit,
						// coreSystemUnitSqlBuilderStr);
						Map<String, String> paramsSystemUnitMap1 = new HashMap<String, String>();
						paramsSystemUnitMap1.put("lastProcessDate", lastProcessDate);
						paramsSystemUnitMap1.put("tomorrowStr", tomorrowStr);
						paramsSystemUnitMap1.put("systemOperateState", SystemOperateState.EOD.getValue());
						paramsSystemUnitMap1.put("tomorrowStr", tomorrowStr);
						paramsSystemUnitMap1.put("nextProcessDate", nextProcessDate);
						paramsSystemUnitMap1.put("nextLogFlag", CoreSystemUnitNew.getCurrLogFlag());
						paramsSystemUnitMap1.put("currLogFlag", CoreSystemUnitNew.getNextLogFlag());
						paramsSystemUnitMap1.put(Constant.REQUEST_TYPE_STR, Constant.RESULT_TYPE);
						paramsSystemUnitMap1.put("systemUnitNo", CoreSystemUnitNew.getSystemUnitNo());
						paramsSystemUnitMap1.put("operatorId", "system");
						int result = betaCommonParamService.updateCoreSystemUnit(paramsSystemUnitMap1);
						if (result != 1) {
							throw new BusinessException("CUS-00012");
						}
						logger.info("切日处理  成功 successed" + coreSystemUnit.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ok";
	}

	public String getNextProcessDate(String operationDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tomorrowStr = null;
		Calendar c = Calendar.getInstance();
		Date date;
		date = sdf.parse(operationDate);
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
		Date tomorrow = c.getTime();
		tomorrowStr = sdf.format(tomorrow);
		return tomorrowStr;
	}

	/**
	 * 
	 * @Description: 获取当前日期对应的参数字段值
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String getMapValue(@SuppressWarnings("rawtypes") Map map, String key) throws Exception {
		String value = map.get(key) == null ? "" : map.get(key).toString();
		return value;
	}

	/**
	 * Obj 对象转换成Map
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new TreeMap<String, Object>();
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			map.put(field.getName(), field.get(obj));
		}
		return map;
	}

	/**
	 * 
	 * @Description: 判断当前日期是星期几
	 * @param pTime
	 * @return
	 * @throws Exception
	 */
	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	public static void main(String[] args) throws Exception {
		// int weekForDay = dayForWeek("2018-12-03");
		// int weekForDayLast =0;
		// if (weekForDay==1) {
		// weekForDayLast = 0;
		// }else {
		// weekForDayLast = weekForDay-1;
		// }
		//
		// String systemProcessDayFlag = "1O3OOC7".substring(weekForDayLast,
		// weekForDay);
		String month = "2020-03-06".substring(5, 7);
		String year = "2020-03-06".substring(0, 4);
		System.out.println(year + month);
	}

}
