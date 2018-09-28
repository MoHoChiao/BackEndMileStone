/**
 * Copyright (C) NetPro Information Service Ltd., 2008.
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
package com.netpro.trinity.resource.admin.util;

/**
 * @author Jet Wu
 *
 */
public class StrUtil {
	public static String getDateTime(int d, int t) {
		StringBuffer sbuf = new StringBuffer();
		
		int Y = d/10000;
		int M = (d%10000)/100;
		int D = (d%100);
		int h = t/10000;
		int m = (t%10000)/100;
		int s = t%100;
		
		sbuf.append(Y).append("-");
		if (M < 10)
			sbuf.append("0");
		
		sbuf.append(M).append("-");
		
		if (D < 10)
			sbuf.append("0");
		
		sbuf.append(D).append(" ");	
		
		if (h < 10)
			sbuf.append("0");
		
		sbuf.append(h).append(":");
		
		if (m < 10)
			sbuf.append("0");
		
		sbuf.append(m).append(":");

		if (s < 10)
			sbuf.append("0");
		
		sbuf.append(s);
		
		return sbuf.toString();
	}
	
	public static String outputXMLText(String str)
	{
		if (str == null)
			return "";
		
		StringBuffer sbuf = new StringBuffer();
		
		for (int i=0; i<str.length(); i++) {
			char ch = str.charAt(i);
			
			if (ch == '<') {
				sbuf.append("&lt;");
			} else if (ch == '>') {
				sbuf.append("&gt;");
			} else if (ch == '"') {
				sbuf.append("&quot;");
			} else if (ch == '\'') {
				sbuf.append("&apos;");
			} else if (ch == '&') {
				sbuf.append("&amp;");
			} else if (ch == '\n') {
				sbuf.append("&#10;");
			} else if (ch == '\r') {
				sbuf.append("&#13;");				
			} else {
				sbuf.append(ch);
			}
		}
		
		return sbuf.toString();
	}
}
