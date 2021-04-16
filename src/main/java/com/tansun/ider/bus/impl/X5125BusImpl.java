package com.tansun.ider.bus.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tansun.ider.util.CachedBeanCopy;
import org.springframework.stereotype.Service;

import com.tansun.framework.util.ReflexUtil;
import com.tansun.framework.util.StringUtil;
import com.tansun.ider.bus.X5125Bus;
import com.tansun.ider.dao.issue.entity.CoreCustomer;
import com.tansun.ider.dao.issue.entity.CoreMediaBasicInfo;
import com.tansun.ider.dao.issue.impl.CoreCustomerDaoImpl;
import com.tansun.ider.dao.issue.impl.CoreMediaBasicInfoDaoImpl;
import com.tansun.ider.dao.issue.sqlbuilder.CoreCustomerSqlBuilder;
import com.tansun.ider.dao.issue.sqlbuilder.CoreMediaBasicInfoSqlBuilder;
import com.tansun.ider.framwork.commun.PageBean;
import com.tansun.ider.model.bo.X5125BO;
import com.tansun.ider.model.vo.X5125VO;
import com.tansun.ider.service.business.EventCommAreaNonFinance;
import com.tansun.ider.util.MapTransformUtils;

/**
 * @version:1.0
 * @Description: 媒介资料查询
 * @author: admin
 */
@Service
public class X5125BusImpl implements X5125Bus {

	@Resource
	private CoreCustomerDaoImpl coreCustomerDaoImpl;
	@Resource
	private CoreMediaBasicInfoDaoImpl coreMediaBasicInfoDaoImpl;

	@Override
	public Object busExecute(X5125BO x5125bo) throws Exception {
		// 获取公共区
		EventCommAreaNonFinance eventCommAreaNonFinance = new EventCommAreaNonFinance();
		// 将参数传递给事件公共区
		CachedBeanCopy.copyProperties(x5125bo, eventCommAreaNonFinance);
		// 身份证号
		String idNumber = eventCommAreaNonFinance.getIdNumber();
		// 外部识别号
		String externalIdentificationNo = eventCommAreaNonFinance.getExternalIdentificationNo();
		// 如果接口输入证件号码，查询客户基本信息表得到客户代码、客户姓名等客户信息；
		// 如果接口输入外部识别号，根据外部识别号查询“媒介基本信息表”
		// 获得证件号码，根据证件号码查询客户基本信息表得到客户代码、客户姓名等客户信息。
		PageBean<X5125VO> page = new PageBean<>();
		CoreMediaBasicInfoSqlBuilder coreMediaBasicInfoSqlBuilder = new CoreMediaBasicInfoSqlBuilder();
		CoreCustomer coreCustomer = null;
		if (StringUtil.isNotBlank(idNumber)) {
			CoreCustomerSqlBuilder coreCustomerSqlBuilder = new CoreCustomerSqlBuilder();
			coreCustomerSqlBuilder.andIdNumberEqualTo(idNumber);
			coreCustomer = coreCustomerDaoImpl.selectBySqlBuilder(coreCustomerSqlBuilder);
			if (coreCustomer != null) {
				coreMediaBasicInfoSqlBuilder.andMainCustomerNoEqualTo(coreCustomer.getCustomerNo());
			}
		}
		@SuppressWarnings("unused")
		CoreMediaBasicInfo coreMediaBasicInfo = null;
		if (StringUtil.isNotBlank(externalIdentificationNo)) {
			coreMediaBasicInfoSqlBuilder.andExternalIdentificationNoEqualTo(externalIdentificationNo);
		}
		int totalCount = coreMediaBasicInfoDaoImpl.countBySqlBuilder(coreMediaBasicInfoSqlBuilder);
		page.setTotalCount(totalCount);
		if (null != x5125bo.getPageSize() && null != x5125bo.getIndexNo()) {
			coreMediaBasicInfoSqlBuilder.orderByMediaUnitCode(false);
			coreMediaBasicInfoSqlBuilder.setPageSize(x5125bo.getPageSize());
			coreMediaBasicInfoSqlBuilder.setIndexNo(x5125bo.getIndexNo());
			page.setPageSize(x5125bo.getPageSize());
			page.setIndexNo(x5125bo.getIndexNo());
		}

		if (totalCount > 0) {
			List<CoreMediaBasicInfo> list = coreMediaBasicInfoDaoImpl
					.selectListBySqlBuilder(coreMediaBasicInfoSqlBuilder);
			List<X5125VO> listVo = new ArrayList<X5125VO>();
			for (CoreMediaBasicInfo coreMediaBasicInfo2 : list) {
				X5125VO x5125VO = new X5125VO();
				Map<String, Object> map = MapTransformUtils.objectToMap(coreMediaBasicInfo2);
				ReflexUtil.setFieldsValues(x5125VO, map);
				x5125VO.setCustomerNo(coreMediaBasicInfo2.getMainCustomerNo());
				if(coreCustomer!=null){
					x5125VO.setCustomerName(coreCustomer.getCustomerName());
				}else{
					CoreCustomerSqlBuilder customerSqlBuilder = new CoreCustomerSqlBuilder();
					customerSqlBuilder.andCustomerNoEqualTo(coreMediaBasicInfo2.getMainCustomerNo());
					CoreCustomer customer = coreCustomerDaoImpl.selectBySqlBuilder(customerSqlBuilder);
					x5125VO.setCustomerName(customer == null ? "":customer.getCustomerName());
				}
				listVo.add(x5125VO);
			}
			page.setRows(listVo);
		}
		return page;
	}

}
