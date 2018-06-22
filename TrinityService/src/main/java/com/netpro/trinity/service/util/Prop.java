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

package com.netpro.trinity.service.util;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author KF
 * 
 */
@XmlType(name="prop")
public class Prop {
	
	private String id;	
	
	private String value;

	@XmlAttribute
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@XmlValue
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Item [id=" + id + ", value=" + value + "]";
	}
}
