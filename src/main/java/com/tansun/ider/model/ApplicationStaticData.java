package com.tansun.ider.model;

public class ApplicationStaticData {

	private static boolean isOpenValidator = true;
	private static boolean isUseSessionCache = false;
	private static boolean isRecordTransLog = false;
	private static String notRecordTransCodeLog = "";
	private static boolean isLoginSystem = true;

	public static String getNotRecordTransCodeLog() {
		return notRecordTransCodeLog;
	}

	public static void setNotRecordTransCodeLog(String notRecordTransCodeLog) {
		ApplicationStaticData.notRecordTransCodeLog = notRecordTransCodeLog;
	}

	public static boolean isOpenValidator() {
		return isOpenValidator;
	}

	public static boolean isRecordTransLog() {
		return isRecordTransLog;
	}

	public static void setRecordTransLog(boolean isRecordTransLog) {
		ApplicationStaticData.isRecordTransLog = isRecordTransLog;
	}

	public static void setOpenValidator(boolean isOpenValidator) {
		ApplicationStaticData.isOpenValidator = isOpenValidator;
	}

	public static boolean isUseSessionCache() {
		return isUseSessionCache;
	}

	public static void setUseSessionCache(boolean isUseSessionCache) {
		ApplicationStaticData.isUseSessionCache = isUseSessionCache;
	}

	public static boolean isLoginSystem() {
		return isLoginSystem;
	}

	public static void setLoginSystem(boolean isLoginSystem) {
		ApplicationStaticData.isLoginSystem = isLoginSystem;
	}

}
