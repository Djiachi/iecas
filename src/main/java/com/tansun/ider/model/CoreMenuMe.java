package com.tansun.ider.model;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreMenu;

/**
 * @version:1.0
* @Description: 
* @author: admin
 */
public class CoreMenuMe extends CoreMenu implements Serializable {
	private static final long serialVersionUID = -161123581321345589L;
	
	// 子菜单
	private List<CoreMenuMe> childMenus;
	
	public List<CoreMenuMe> getChildMenus() {
		return childMenus;
	}
	
	public void setChildMenus(List<CoreMenuMe> childMenus) {
		this.childMenus = childMenus;
	}
	
}