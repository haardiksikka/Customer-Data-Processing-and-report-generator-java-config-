package com.viva.customer_processing.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileNumberValidator {
	
	public static boolean isValid(String s) {

		Pattern p = Pattern.compile("[0-9]{10}");

		Matcher m = p.matcher(s);
		return (m.find() && m.group().equals(s));
	}


}
