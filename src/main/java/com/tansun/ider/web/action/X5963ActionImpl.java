package com.tansun.ider.web.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tansun.ider.bus.X5963Bus;
import com.tansun.ider.framwork.api.ActionService;

/**
 * 根据系统单元 中的当前日志标识，删除G类，S类，D类，非金融的日志，和交易登记表
 * @author lianhuan
 * 2019年4月9日
 */
@Service("X5963")
public class X5963ActionImpl implements ActionService {

	@Autowired
	private X5963Bus x5963Bus;

	/**
	 * X5963 客户基本信息新增活动设计
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Object execute(Map<String, Object> map) throws Exception {
		return x5963Bus.busExecute();
	}
}
