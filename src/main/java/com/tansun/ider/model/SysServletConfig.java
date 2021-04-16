package com.tansun.ider.model;

import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class SysServletConfig{
	public Locale locale = Locale.SIMPLIFIED_CHINESE;
	public boolean isOpenValidator = true;
	public boolean isLoginServlet = true;
	public String webformBeanPackage = "webbean";
	public String logincode = "";
	public String logoutcode = "";
	public String noLoginTransCode = "";
	private HashMap<String, String> noLoginCodeHashMap = new HashMap<String, String>();
	public String communAdapter;
	public String transCodeKey;
	public String clientTokenkey;
	public String channelNoKey;
	public String branchNoKey;
	public String userIdKey;
	public String userNumberKey;
	public String legalPerNumKey;
	public String workingPath;
	public String uploadpath = "/tmp"; // 文件上传目录
	public String savefile = "false";
	public long maxsize = 500 * 1024 * 1024; // 文件上传最大尺寸
	public DiskFileItemFactory factory = new DiskFileItemFactory();
	public ServletFileUpload upload = new ServletFileUpload(factory);
	public String servletName;

	public HashMap<String, String> getNoLoginCodeHashMap() {
		return noLoginCodeHashMap;
	}

	public void setNoLoginCodeHashMap(HashMap<String, String> noLoginCodeHashMap) {
		this.noLoginCodeHashMap = noLoginCodeHashMap;
	}

	public boolean isOpenValidator() {
		return isOpenValidator;
	}

	public void setOpenValidator(boolean isOpenValidator) {
		this.isOpenValidator = isOpenValidator;
	}

	public boolean isLoginServlet() {
		return isLoginServlet;
	}

	public void setLoginServlet(boolean isLoginServlet) {
		this.isLoginServlet = isLoginServlet;
	}

	public String getWebformBeanPackage() {
		return webformBeanPackage;
	}

	public void setWebformBeanPackage(String webformBeanPackage) {
		this.webformBeanPackage = webformBeanPackage;
	}

	public String getNoLoginTransCode() {
		return noLoginTransCode;
	}

	public void setNoLoginTransCode(String noLoginTransCode) {
		this.noLoginTransCode = noLoginTransCode;
		if (this.noLoginTransCode != null && !"".equals(this.noLoginTransCode)) {
			String[] nologincodes = this.noLoginTransCode.split(",");
			HashMap<String, String> noLoginCodeHashMap = new HashMap<String, String>();
			for (String string : nologincodes) {
				if (string != null && !"".equals(string)){
					noLoginCodeHashMap.put(string, "");
				}
			}
			setNoLoginCodeHashMap(noLoginCodeHashMap);
		}

	}

	public String getCommunAdapter() {
		return communAdapter;
	}

	public void setCommunAdapter(String communAdapter) {
		this.communAdapter = communAdapter;
	}

	public String getTransCodeKey() {
		return transCodeKey;
	}

	public void setTransCodeKey(String transCodeKey) {
		this.transCodeKey = transCodeKey;
	}

	public String getChannelNoKey() {
		return channelNoKey;
	}

	public void setChannelNoKey(String channelNoKey) {
		this.channelNoKey = channelNoKey;
	}

	public String getBranchNoKey() {
		return branchNoKey;
	}

	public void setBranchNoKey(String branchNoKey) {
		this.branchNoKey = branchNoKey;
	}

	public String getUserIdKey() {
		return userIdKey;
	}

	public void setUserIdKey(String userIdKey) {
		this.userIdKey = userIdKey;
	}

	public String getUserNumberKey() {
		return userNumberKey;
	}

	public void setUserNumberKey(String userNumberKey) {
		this.userNumberKey = userNumberKey;
	}

	public String getLegalPerNumKey() {
		return legalPerNumKey;
	}

	public void setLegalPerNumKey(String legalPerNumKey) {
		this.legalPerNumKey = legalPerNumKey;
	}

}
