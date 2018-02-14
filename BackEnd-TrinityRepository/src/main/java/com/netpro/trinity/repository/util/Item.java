/**
 * Copyright (C) NetPro Information Service Ltd., 2010.
 * All rights reserved.
 * 
 * This software is covered by the license agreement between
 * the end user and NetPro Information Service Ltd., and may be 
 * used and copied only in accordance with the terms of the 
 * said agreement.
 * 
 * NetPro Information Service Ltd. assumes no responsibility or 
 * liability for any errors or inaccuracies in this software, 
 * or any consequential, incidental or indirect damage arising
 * out of the use of the software.
 */

package com.netpro.trinity.repository.util;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author KF
 * 
 */
@XmlRootElement
@XmlType(name="item", propOrder={"props", "items"})
public class Item{	
	
	private String name;
	
	private String type;
	
	private String value;
	
	private List<Prop> props;
	
	private Map<String, String> propsMap;
	
	private List<Item> items;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@XmlAttribute
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}	
	
	@XmlElement(name="prop")
	public List<Prop> getProps() {
		if(this.props != null) {
			return this.props;
		}else {
			List<Prop> list = null;
			if(propsMap != null) {
				list = new LinkedList<Prop>();
				for (Entry<String, String> entry : propsMap.entrySet()) {
					Prop prop = new Prop();
					prop.setId(entry.getKey());
					prop.setValue(entry.getValue());
					list.add(prop);
				}
			}		
			return list;
		}
	}	

	public void setProps(List<Prop> props) {
		this.props = props;
		Map<String, String> map = null;
		if (props != null && props.size() > 0) {
			map = new LinkedHashMap<String, String>();
			for(Prop prop : props) {
				map.put(prop.getId(), prop.getValue());
			}
		}
		this.propsMap = map;
	}

	public String getProp(String key) {
		if(propsMap == null) {
			setProps(getProps());
		}
		return propsMap == null ? null : propsMap.get(key);
	}
	
	public void putProp(String key, String value) {
		if(propsMap == null) {
			setProps(getProps());
			if (propsMap == null)
				propsMap = new LinkedHashMap<String, String>();
		}
		propsMap.put(key, value);
		
		List<Prop> list = new LinkedList<Prop>();
		for (Entry<String, String> entry : propsMap.entrySet()) {
			Prop prop = new Prop();
			prop.setId(entry.getKey());
			prop.setValue(entry.getValue());
			list.add(prop);
		}
		props = list;
	}

	@XmlElement(name="item")
	public List<Item> getItems() {
		if (items == null) {
			items = new LinkedList<Item>();
		}
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Deprecated
	public Item getNext(String name){
		return getNext(name, null);
	}
	
	public Item getChild(String name){
		return getNext(name, null);
	}
	
//	public Item getChild(String name){
//		return getNext(name, null);
//	}
	
	public List<Item> getChildren(String name){
		List<Item> ret = new LinkedList<Item>();
		
		if (items == null)
			return ret;
		
		for(Item item : items){
			if(name.equals(item.getName()))
				ret.add(item);
		}
		return ret;
	}
	
	public List<Item> getChildren(Item example){
		List<Item> ret = new LinkedList<Item>();
		for(Item item:items){
			if(item.match(example))
					ret.add(item);
		}
		return ret;
	}
	
	public boolean match(Item example){
		return (example.name == null || example.name.equals(name))
		&& (example.type == null || example.type.equals(type))
		&& (example.value == null || example.value.equals(value));
	}
	
	public Item removeItem(Item item){
		List<Item> list = new LinkedList<Item>();
		Item removeItem = null;
		for (Item it : getItems()){
			if (it.equals(item)){
				removeItem = it;
			} else {
				list.add(it);
			}
		}
		items = list;
		return removeItem;
	}
	
	@Deprecated
	public Item getNext(String name, Item from){
		if(items == null)
			return null;
		Item begin = from;
		for(Item item:items){
			if(begin == null && name.equals(item.getName()))
				return item;
			if(begin == item){
				begin = null;
			}
		}
		return null;
	}
	
	@Deprecated
	public Item getNext(Item from){
		if(items == null)
			return null;		
		Item begin = from;
		for(Item item: items){
			if(begin == null)
				return item;
			if(begin == item){
				begin = null;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		try {
			return XMLConfigUtil.objectToXML(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Item valueOf(String xml){
		try {
			return XMLConfigUtil.xmlToObject(xml, Item.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
