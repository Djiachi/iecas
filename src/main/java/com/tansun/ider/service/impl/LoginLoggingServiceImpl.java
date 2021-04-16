package com.tansun.ider.service.impl;

import org.springframework.stereotype.Service;

import com.tansun.ider.dao.beta.entity.CoreUser;
import com.tansun.ider.service.LoginLoggingService;

@Service
public class LoginLoggingServiceImpl implements LoginLoggingService {

	@Override
	public void loggingCoreUserLoginInfo(String userId, CoreUser coreUser, boolean bool) throws Exception {
		
	}

	@Override
	public void loggingCoreUserLoginOutInfo(CoreUser coreUser) throws Exception {
		
	}

}
