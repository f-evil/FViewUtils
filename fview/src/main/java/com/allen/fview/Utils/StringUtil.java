package com.allen.fview.Utils;

/**
 * 字符工具类
 * @author fyj
 * @version 创建时间：2015-7-4 下午2:15:11
 */
public class StringUtil {
	/**
	 * 判断字符串是否为空
	 * @param str	输入字符
	 * @return		是否为空
	 */
	public static boolean isEmpty(String str){
		return (null == str || "".equals(str.trim()) || "null".equals(str));
	}

	/**
	 * 截取最后一个字符@c后面的字符串
	 * @param str	输入字符串
	 * @param c		判断字符字符
	 * @return		截取后的字符串
	 */
	public static String lastStringAfter(String str, char c) {
		return str.substring(str.lastIndexOf(c) + 1);
	}
}
