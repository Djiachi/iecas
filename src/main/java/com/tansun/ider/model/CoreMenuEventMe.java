package com.tansun.ider.model;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreMenuEvent;

public class CoreMenuEventMe  extends CoreMenuEvent  implements Serializable {
	private static final long serialVersionUID = -161123581321345589L;
	
	// 子菜单,子事件
	private List<CoreMenuEvent> childMenuEvent;
	
	public List<CoreMenuEvent> getChildMenuEvent() {
		return childMenuEvent;
	}
	
	public void setChildMenuEvent(List<CoreMenuEvent> childMenuEvent) {
		this.childMenuEvent = childMenuEvent;
	}
	
}
