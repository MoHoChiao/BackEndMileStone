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

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author KF
 * 
 */
public class XMLConfigUtil {

	public static <T> String objectToXML(T obj) throws Exception{
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter sw = new StringWriter();
		marshaller.marshal(obj, sw);
		return sw.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T xmlToObject(String xml, Class<T> objectClass)
			throws Exception {
		JAXBContext context = JAXBContext.newInstance(objectClass);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(new ByteArrayInputStream(xml
				.getBytes("utf-8")));
	}
}
