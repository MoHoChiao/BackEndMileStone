package com.netpro.trinity.resource.admin.util;

import java.util.List;

public class JsTreeItem {
	
	private String id;
	private String name;
	private List<JsTreeItem> children;
	private String icon;
	private boolean opened;
	private boolean disabled;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<JsTreeItem> getChildren() {
		return children;
	}
	public void setChildren(List<JsTreeItem> children) {
		this.children = children;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public boolean isOpened() {
		return opened;
	}
	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
}
