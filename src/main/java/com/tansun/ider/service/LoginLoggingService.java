package com.tansun.ider.service;

import com.tansun.ider.dao.beta.entity.CoreUser;

public interface LoginLoggingService {

	/**
	 * 登录日志
	* @Description: 
	* @param userId
	* @param coreUser 
	* @param bool boolean类型 true为登陆成功，false为登录失败
	* @throws Exception
	 */
	public void loggingCoreUserLoginInfo(String userId, CoreUser coreUser, boolean bool)
			throws Exception;

	/**
	 * 登出日志
	* @Description: 
	* @param coreUser 当前登录用户信息
	* @throws Exception
	 */
	public void loggingCoreUserLoginOutInfo(CoreUser coreUser) throws Exception;
}
