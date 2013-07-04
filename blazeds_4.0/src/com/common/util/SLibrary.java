package com.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.MessageFormat;
import java.util.Calendar;
import java.io.Reader;

public class SLibrary {

	public final static String remarksAll = "#";
	public final static String remarksFrom = "//";
	public final static String CRLF = "\r\n";

	/**
	 * Formater
	 */
	public static DecimalFormat fmtBy = new DecimalFormat("##############0");
	public static DecimalFormat fmtBy0 = new DecimalFormat(
			"###,###,###,###,##0");
	public static DecimalFormat fmtBy1 = new DecimalFormat(
			"###,###,###,###,##0.0");
	public static DecimalFormat fmtBy2 = new DecimalFormat("###,###,###,##0.00");

	/**
	 * KSC5601 -> 8859_1
	 * 
	 * @param ko
	 *            - ���ڿ�
	 * @return String
	 */
	public static String to8859_1(String ko) {
		if (ko == null) {
			return null;
		}
		try {
			return new String(ko.getBytes("KSC5601"), "8859_1");
		} catch (Exception e) {
			return ko;
		}
	}

	/**
	 * 8859_1 -> KSC5601
	 * 
	 * @param ko
	 *            - ���ڿ�
	 * @return String
	 */
	public static String toKSC5601(String en) {
		if (en == null) {
			return null;
		}
		try {
			// ���
			// en = new String(en.getBytes("8859_1"), "KSC5601");
			// �����
			return en;
		} catch (Exception e) {
			// return en;
		}
		return en;

	}

	/**
	 * �־��� ���ڿ��� null�� ��� ""; �����Ѵ�.
	 * 
	 * @param str
	 * 
	 * @return "" �Ǵ� str
	 */
	public static String IfNull(String str) {

		if (str == null)
			return "";
		else
			return str;
	}

	/**
	 * �־��� ���ڿ��� null�� ��� ""; �����Ѵ�.
	 * 
	 * @param str
	 * @return "" �Ǵ� str
	 */
	public static String IfNullDB(String str) {

		if (str == null || str.equals("null"))
			return "";
		else
			return str;
	}

	/**
	 * �־��� ���ڿ��� null�� ��� ""; flase�� �����Ѵ�.
	 * 
	 * @param str
	 * 
	 * @return boolean
	 */
	public static boolean isNull(String str) {

		if (str == null || str.trim().equals(""))
			return true;
		else
			return false;
	}
	
	public static String getDateAddSecond(String format, String orgDate, int sec){
		return getDateTimeString( format, ((getTime(orgDate, format)/1000)+sec)*1000 );
	}

	public static String getDateTimeString() {
		return getDateTimeString("yyyy-MM-dd HH:mm:ss");
	}

	public static String getDateTimeString(String format) {

		return getDateTimeString(format, System.currentTimeMillis());
	}

	public static String getUnixtimeStringSecond() {

		String unix = Long.toString(System.currentTimeMillis());
		return unix.substring(0, unix.length() - 3);
	}

	/**
	 * �־��� ��˿� ��� ������ �����͸� ��˵� ���ڿ��� �����Ѵ�.
	 * 
	 * @param date
	 *            ����� ���ڰ�
	 * 
	 * @param format
	 *            ������ ������ ��� ���ڿ�
	 * 
	 * @return ��˵� ���� ��
	 */
	public static String getDateTimeString(String format, long date) {

		return getDateTimeString(format, new java.util.Date(date));
	}

	/**
	 * �־��� ��˿� ��� ������ �����͸� ��˵� ���ڿ��� �����Ѵ�.
	 * 
	 * @param date
	 *            ����� ���ڰ�
	 * 
	 * @param format
	 *            ������ ������ ��� ���ڿ�
	 * 
	 * @return ��˵� ���� ��
	 */
	public static String getDateTimeString(String format, java.util.Date date) {

		SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
		StringBuffer ret = new StringBuffer();
		df.format(date, ret, new FieldPosition(0));
		return ret.toString();
	}

	/**
	 * String�� ���� Time; return �Ѵ�.
	 * 
	 * @param data
	 *            ���ڿ�
	 * 
	 * @return yyyy-MM-dd���� unixTime long��
	 */
	public static long getTime(String dateString) {

		return getTime(dateString, "yyyy-MM-dd");
	}

	/**
	 * String�� ���� Time; return �Ѵ�.
	 * 
	 * @param data
	 *            ���ڿ�
	 * @param format
	 *            ������ ������ ��� ���ڿ�
	 * @return unixTime long��
	 */
	public static long getTime(String dateString, String format) {

		SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
		try {
			Date d = df.parse(dateString);
			return d.getTime();
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * String�� ���� Time; return �Ѵ�.
	 * 
	 * @param data
	 *            ���ڿ�
	 * @param format
	 *            ������ ������ ��� ���ڿ�
	 * @return unixTime String��
	 */
	public static String getTimeSecond(String dateString, String format) {

		String rslt = "";
		SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
		try {
			Date d = df.parse(dateString);
			rslt = Long.toString(d.getTime());
		} catch (ParseException e) {
		}

		return rslt.substring(0, rslt.length() - 3);
	}

	/**
	 * String�� ���� Time; ��d format8�� return �Ѵ�.
	 * 
	 * @param data
	 *            ���ڿ� yyyyMMddhhmmss
	 * @param format
	 *            ������ ������ ��� ���ڿ�
	 * @return unixTime long��
	 */
	public static String getDateTimeString(String dateString, String format) {

		long dLong = 0;

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault());
		try {
			Date d = df.parse(dateString);
			dLong = d.getTime();
		} catch (ParseException e) {
			dLong = 0;
		}
		return getDateTimeString(format, dLong);
	}

	/**
	 * String�� ���� Time; ��d format8�� return �Ѵ�.
	 * 
	 * @param data
	 *            ���ڿ� yyyy-MM-dd hh:mm:ss
	 * @param format
	 *            ������ ������ ��� ���ڿ�
	 * @return unixTime long��
	 */
	public static String getDateTimeStringStandard(String dateString,
			String format) {

		long dLong = 0;

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		try {
			Date d = df.parse(dateString);
			dLong = d.getTime();
		} catch (ParseException e) {
			dLong = 0;
		}
		return getDateTimeString(format, dLong);
	}

	/**
	 * String�� ���� Time; ��d format8�� return �Ѵ�.
	 * 
	 * @param data
	 *            ���ڿ� yyyyMMddhhmmss
	 * @param format
	 *            ������ ������ ��� ���ڿ�
	 * @return unixTime long��
	 */
	public static String getDateTimeString(String dateString, String format,
			String parmFormat) {

		long dLong = 0;
		System.out.println(dateString+"/"+format+"/"+parmFormat);
		SimpleDateFormat df = new SimpleDateFormat(parmFormat,
				Locale.getDefault());
		try {
			Date d = df.parse(dateString);
			dLong = d.getTime();
			System.out.println(dLong);
		} catch (ParseException e) {
			dLong = 0;
		}
		return getDateTimeString(format, dLong);
	}

	/**
	 * ���� ����8�� n�� ���� ��¥ ��ȯ
	 * 
	 * @param n
	 *            - ��� ��
	 * @param format
	 * @return String
	 */
	public static String diffOfMonth(int n, String format) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, n);
		return SLibrary.getDateTimeString(format, cal.getTime());
	}

	/**
	 * ���� ����8�� n�� ���� ��¥ ��ȯ
	 * 
	 * @param n
	 *            - ��� ��
	 * @param format
	 * @return String
	 */
	public static String diffOfDay(int n, String format) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, n);
		return SLibrary.getDateTimeString(format, cal.getTime());
	}

	/**
	 * ���� ����8�� n�� ���� ��¥ ��ȯ
	 * 
	 * @param n
	 *            - ��� ��
	 * @param format
	 * @return String
	 */
	public static String diffOfHour(int n, String format) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, n);
		return SLibrary.getDateTimeString(format, cal.getTime());
	}

	/**
	 * �ش� �� ���� ����; ���Ѵ�.
	 */
	public static int getLastDate(int year, int month) {

		Calendar calendar = Calendar.getInstance();

		month = month - 1;
		calendar.set(year, month, 1);
		int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		return lastDayOfMonth;
	}

	/**
	 * �ش� �� ���� ����; ���Ѵ�.
	 */
	public static int getLastDate(String pyear, String pmonth) {

		int year = Integer.parseInt(pyear);
		int month = Integer.parseInt(pmonth);

		Calendar calendar = Calendar.getInstance();

		month = month - 1;
		calendar.set(year, month, 1);
		int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		return lastDayOfMonth;
	}

	/**
	 * ���ڿ�; �8��ʿ��� ���ٿ� ��d�� ���̸� �����
	 * 
	 * @param src
	 *            ���� ���ڿ�
	 * 
	 * @param length
	 *            ���Ϲ�; ����
	 * 
	 * @return ���� ���ڿ�
	 */
	public static String padR(String src, int length) {
		return padR(src, length, " ");
	}

	/**
	 * ���ڿ�; �8��ʿ��� ���ٿ� ��d�� ���̸� �����
	 * 
	 * @param src
	 *            ���� ���ڿ�
	 * 
	 * @param unit
	 *            ���ٿ��� ��� ���ڿ�
	 * 
	 * @param length
	 *            ���Ϲ�; ����
	 * 
	 * @return ���� ���ڿ�
	 */
	public static String padR(String src, int length, String unit) {

		StringBuffer ret = src == null ? new StringBuffer() : new StringBuffer(
				src);
		for (; ret.length() < length;) {
			ret.append(unit);
		}

		if (ret.length() > length)
			return ret.toString().substring(0, length);

		return ret.toString();
	}

	/**
	 * ���ڿ�; ���ʿ��� ���ٿ� ��d�� ���̸� �����
	 * 
	 * @param src
	 *            ���� ���ڿ�
	 * 
	 * @param length
	 *            ���Ϲ�; ����
	 * 
	 * @return ���� ���ڿ�
	 */
	public static String padL(String src, int length) {
		return padL(src, length, " ");
	}

	/**
	 * ���ڿ�; ���ʿ��� ���ٿ� ��d�� ���̸� �����
	 * 
	 * @param src
	 *            ���� ���ڿ�
	 * 
	 * @param unit
	 *            ���ٿ��� ��� ���ڿ�
	 * 
	 * @param length
	 *            ���Ϲ�; ����
	 * 
	 * @return ���� ���ڿ�
	 */
	public static String padL(String src, int length, String unit) {
		// StringBuffer ret = src == null ? new StringBuffer() : new
		// StringBuffer(src);
		StringBuffer ret = new StringBuffer();
		for (; ret.length() + src.length() < length;) {
			ret.append(unit);
		}

		ret.insert(ret.length(), src);

		if (ret.length() > length) {
			return length <= 0 ? ret.toString() : ret.toString().substring(
					ret.length() - length, length);
		}

		return ret.toString();
	}

	/**
	 * ���� ���ڿ��� ���� ��d�� �κ�; ã�� �����ϴ� ���ڿ��� ��� ��ġ�Ѵ�.
	 * 
	 * @param originalString
	 *            ���� ���ڿ�
	 * 
	 * @param findString
	 *            ã�Ƴ� ���ڿ�
	 * 
	 * @param replacString
	 *            ��ġ�� ���ڿ�
	 * 
	 * @return ���� ���ڿ�
	 */

	public static String replaceAll(String originalString, String findString,
			String replacString) {

		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		while ((e = originalString.indexOf(findString, s)) >= 0) {
			result.append(originalString.substring(s, e));
			result.append(replacString);
			s = e + findString.length();
		}

		result.append(originalString.substring(s));
		return result.toString();
	}

	/**
	 * ����ڿ�(strTarget)���� Ưd���ڿ�(strSearch); ã�� ��d���ڿ�(strReplace �迭 Object[])��
	 * ġȯ�Ͽ� ������ ���ڿ�; ��ȯ�Ѵ�.
	 * 
	 * @param strTarget
	 *            ����ڿ�
	 * 
	 * @param strSearch
	 *            �������� Ưd���ڿ�
	 * 
	 * @param strReplace
	 *            ���� ��Ű�� ��d���ڿ� �迭 Object[]
	 * 
	 * @param isWhere
	 *            v�ǹ���; ��Ÿ���� ��Ʈ��. ��, ��ҹ��� ������ ���. ��) where
	 * 
	 * @return ����Ϸ�� ���ڿ�
	 */
	public static String replaceArrayStringWhere(String strTarget,
			String[] strSearch, Object[] strReplace, String isWhere) {

		if (isWhere == null) {
			return replaceArrayString(strTarget, strSearch, strReplace);
		}

		String result = null;

		String strCheck = new String(strTarget);
		StringBuffer strBuf = new StringBuffer();
		int i = 0;

		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strSearch[i]);
			if (begin == -1) {
				strBuf.append(strCheck);
				break;
			} else {
				int end = begin + strSearch[i].length();
				strBuf.append(strCheck.substring(0, begin));
				if (strReplace[i] instanceof String) {
					// v���� ���� v�ǹ��ΰ��
					if (strSearch[i].toUpperCase().startsWith(
							isWhere.toUpperCase())) {
						strBuf.append(strReplace[i]);
					}
					// v�ǰ��� ���
					else {
						strBuf.append("'" + strReplace[i] + "'");
					}
				} else {
					strBuf.append(strReplace[i]);
				}
				strCheck = strCheck.substring(end);
			}
			if (i < strSearch.length - 1)
				i++;
		}
		result = strBuf.toString();

		return result;
	}

	/**
	 * ����ڿ�(strTarget)���� Ưd���ڿ�(strSearch); ã�� ��d���ڿ�(strReplace �迭 Object[])��
	 * ġȯ�Ͽ� ������ ���ڿ�; ��ȯ�Ѵ�.
	 * 
	 * @param strTarget
	 *            ����ڿ�
	 * 
	 * @param strSearch
	 *            �������� Ưd���ڿ�
	 * 
	 * @param strReplace
	 *            ���� ��Ű�� ��d���ڿ� �迭 Object[]
	 * 
	 * @return ����Ϸ�� ���ڿ�
	 */
	public static String replaceArrayString(String strTarget,
			String[] strSearch, Object[] strReplace) {
		String result = null;

		String strCheck = new String(strTarget);
		StringBuffer strBuf = new StringBuffer();
		int i = 0;

		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strSearch[i]);
			if (begin == -1) {
				strBuf.append(strCheck);
				break;
			} else {
				int end = begin + strSearch[i].length();
				strBuf.append(strCheck.substring(0, begin));
				if (strReplace[i] instanceof String) {
					strBuf.append("'" + strReplace[i] + "'");
				} else {
					strBuf.append(strReplace[i]);
				}
				strCheck = strCheck.substring(end);
			}
			if (i < strSearch.length - 1)
				i++;
		}
		result = strBuf.toString();

		return result;
	}

	/**
	 * ����; �˻��Ͽ� Ưd ���ڿ��� �����Ѵ�
	 * 
	 * @param strTarget
	 *            ����ڿ�
	 * 
	 * @param pattern
	 *            ����
	 * 
	 * @param pattern
	 *            ���湮�ڿ�
	 * 
	 * @return ����Ϸ�� ���ڿ�
	 */
	public static String replacePattern(String strTarget, String pattern,
			String replaceString) {

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(strTarget);
		return m.replaceAll(replaceString);
	}

	/**
	 * ���ϰ˻�
	 * 
	 * @param strTarget
	 *            ����ڿ�
	 * 
	 * @param pattern
	 *            ����
	 * 
	 * @return ��ġ ���
	 */
	public static boolean searchPattern(String strTarget, String pattern) {

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(strTarget);
		return m.matches();

	}

	/**
	 * ����ڿ�(strTarget)���� Ưd���ڿ�(?); ã�� ��d���ڿ�(strReplace �迭 Object[])�� ġȯ�Ͽ� ������
	 * ���ڿ�; ��ȯ�Ѵ�.
	 * 
	 * @param strTarget
	 *            ����ڿ�
	 * 
	 * @param strReplace
	 *            ���� ��Ű�� ��d���ڿ� �迭 Object[]
	 * 
	 * @return ����Ϸ�� ���ڿ�
	 */
	public static String setStmt(String strTarget, Object[] strReplace) {
		String result = null;

		String strCheck = new String(strTarget);
		StringBuffer strBuf = new StringBuffer();
		int i = 0;

		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf("?");
			if (begin == -1) {
				strBuf.append(strCheck);
				break;
			} else {
				int end = begin + 1;
				strBuf.append(strCheck.substring(0, begin));
				if (strReplace[i] instanceof String) {
					strBuf.append("'" + strReplace[i] + "'");
				} else {
					strBuf.append(strReplace[i]);
				}
				strCheck = strCheck.substring(end);
			}
			if (i < strReplace.length - 1)
				i++;
		}
		result = strBuf.toString();

		return result;
	}

	/**
	 * �Խù����� �ִ� ������ ����
	 * 
	 * @return int
	 * 
	 * @param allPage
	 *            int
	 * 
	 * @param list_num
	 *            int
	 */
	public static int getMaxNum(int allPage, int list_num) {
		if ((allPage % list_num) == 0) {
			return allPage / list_num;
		} else {
			return allPage / list_num + 1;
		}
	}

	public static String readAll(InputStream in) throws IOException {

		return readAll(in, null);
	}

	public static String readAll(InputStream in, String cr) throws IOException {

		if (cr == null)
			cr = CRLF;

		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		int pos;
		String line;
		StringBuffer sbuf = new StringBuffer();
		while ((line = bin.readLine()) != null) {
			line = line.trim();
			if (line.startsWith(remarksAll))
				continue;

			pos = line.indexOf(remarksFrom);
			if (pos == 0)
				continue;
			else if (pos != -1)
				line = line.substring(0, pos);

			sbuf.append(line);
			sbuf.append(cr);
		}

		return sbuf.toString();
	}

	/**
	 * üũ�ڽ� Y/N return
	 * 
	 * @param: String check
	 * 
	 * @return: String
	 */
	public static String getisCheck(String check) {
		String reStr = "";
		if (check != null) {
			if (check.equals("-1"))
				reStr = "Y";
			else
				reStr = "N";
		} else {
			reStr = "";
		}
		return reStr;
	}

	/**
	 * 1��üũ
	 * 
	 * @param: String strNm
	 * 
	 * @return: boolean
	 */
	public static boolean checkDay(String strNm) {
		if (strNm == null || strNm.length() != 8)
			return true;
		int p_year = Integer.parseInt(strNm.substring(0, 4));
		if ((p_year % 4) == 0) {
			if ((p_year % 100) == 0 && (p_year % 400) != 0)
				return true;
			return false;
		} else
			return true;
	}

	/**
	 * �Ѵ��� ��¥ ���
	 * 
	 * @param: String strNm
	 * 
	 * @return: String
	 */
	public static String beforeDay(String strNm) {
		if (strNm != null && strNm.length() == 8) {
			int[] monStr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

			String reYear = strNm.substring(0, 4);
			int reYear_a = Integer.parseInt(reYear) - 1;

			int reMon = Integer.parseInt(strNm.substring(4, 6));
			String reMon_s = zeroFill(reMon + "", 2);
			String reMon_sa = zeroFill((reMon - 1) + "", 2);

			int reDay = Integer.parseInt(strNm.substring(6, 8));

			String reDay_sa = zeroFill((reDay + 1) + "", 2);

			if (reMon == 1) {
				if ("31".equals(strNm.substring(6, 8))) {
					return reYear + "0101";
				} else {
					return reYear_a + "12" + reDay_sa;
				}
			} else if (monStr[reMon - 2] <= (reDay)) {
				return reYear + reMon_s + "01";
			} else {
				return reYear + reMon_sa + reDay_sa;
			}
		}
		return "";
	}

	/**
	 * �Ѵ��� ��¥ ���
	 * 
	 * @param: String strNm
	 * 
	 * @return: String
	 */
	public static String afterDay(String strNm) {
		if (strNm != null && strNm.length() == 8) {
			int[] monStr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

			String reYear = strNm.substring(0, 4);
			int reYear_a = Integer.parseInt(reYear) + 1;

			int reMon = Integer.parseInt(strNm.substring(4, 6));
			String reMon_s = zeroFill(reMon + "", 2);
			String reMon_sa = zeroFill((reMon + 1) + "", 2);

			int reDay = Integer.parseInt(strNm.substring(6, 8));

			String reDay_sa = zeroFill((reDay - 1) + "", 2);

			if (reMon == 12) {
				if ("01".equals(strNm.substring(6, 8))) {
					return reYear + "1231";
				} else {
					return reYear_a + "01" + reDay_sa;
				}
			} else if (monStr[reMon] <= (reDay)) {
				return reYear + reMon_sa + monStr[reMon];
			} else if (reDay == 1) {
				return reYear + reMon_s + monStr[reMon - 1];
			} else {
				return reYear + reMon_sa + reDay_sa;
			}
		}
		return "";
	}

	/**
	 * '1' ==> size + str(i) = '000001'
	 * 
	 * @param: String strNm
	 * 
	 * @return: String
	 */
	public static String zeroFill(String strNm, int size) {
		String reStr = "";
		int NmSize;

		NmSize = strNm.length();

		for (int i = 0; size - NmSize > i; i++) {
			reStr = "0" + reStr;
		}
		reStr = reStr + strNm;

		return reStr;
	}

	/**
	 * '000001' ==> '1'
	 * 
	 * @param: String strNm
	 * 
	 * @return: String
	 */
	public static String cutFill(String strNm) {
		String reStr;
		reStr = Integer.parseInt(strNm) + "";

		return reStr;
	}

	/**
	 * ��¥, ��,�ݾ��� mask�� remove �� ���ڸ� return[=������]
	 * 
	 * @return java.lang.String
	 * 
	 * @param s
	 *            java.lang.String
	 */
	public static String removeMask(String s) {
		String format = "-0123456789";

		if (s == null || s.equals("")) {
			return "";
		}

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == '-' && s.indexOf(c) > 0)
				continue;

			if (format.indexOf(c) > -1)
				buf.append(c);
		}

		return buf.toString();

	}

	/**
	 * ���ڿ� ���� ',' �� 3�ڸ����� ����(���ڿ��� ����) �ۼ� ��¥: <2004.09.15>
	 * 
	 * @param: <|>
	 * 
	 * @return:
	 */
	public static String addComma(String src) {

		String resultValue = "";
		int count = 0;
		int pos = 3;

		if (src == null || src.length() == 0)
			return "0";

		String[] data = SLibrary.split(src, ".", true);

		if (data.length > 0) {
			for (int i = data[0].length() - 1; i >= 0; i--) {
				resultValue = data[0].charAt(i) + resultValue;
				count++;
				if (count == pos && i != 0) {
					resultValue = "," + resultValue;
					count = 0;
				}
			}
		}

		if (data.length == 2)
			resultValue += "." + data[1];

		return resultValue;
	}

	/**
	 * ���ڿ� ���� ',' �� 3�ڸ����� ����(���ڿ��� ����) �ۼ� ��¥: <2004.09.15>
	 * 
	 * @param: <|>
	 * 
	 * @return:
	 */
	public static String addComma(int num) {

		String src = Integer.toString(num);
		String resultValue = "";
		int count = 0;
		int pos = 3;

		if (src == null || src.length() == 0)
			return resultValue;

		String[] data = SLibrary.split(src, ".", true);

		if (data.length > 0) {
			for (int i = data[0].length() - 1; i >= 0; i--) {
				resultValue = data[0].charAt(i) + resultValue;
				count++;
				if (count == pos && i != 0) {
					resultValue = "," + resultValue;
					count = 0;
				}
			}
		}

		if (data.length == 2)
			resultValue += "." + data[1];

		return resultValue;
	}

	/**
	 * �Է¹�: String���� Comma�� ��f�Ѵ�.
	 * 
	 * @return String Comma�� ��f�� String
	 * 
	 * @param value
	 *            String Comma�� �ִ� String
	 */
	public static String commaRemove(String value) {
		String newValue = "";
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) != ',')
				newValue += String.valueOf(value.charAt(i));
		}
		return newValue;
	}

	/**
	 * ����ڿ�(strTarget)���� ���й��ڿ�(strDelim); ����8�� ���ڿ�; �и��Ͽ� �� �и��� ���ڿ�; �迭�� �Ҵ��Ͽ�
	 * ��ȯ�Ѵ�.
	 * 
	 * @param strTarget
	 *            �и� ��� ���ڿ�
	 * @param strDelim
	 *            ���н�ų ���ڿ��μ� ��� ���ڿ����� ���Ե��� �ʴ´�.
	 * @param bContainNull
	 *            ���еǾ��� ���ڿ��� ��鹮�ڿ��� ���Կ���. true : ����, false : �������� ��=.
	 * @return �и��� ���ڿ�; ���� �迭�� �ݳ��Ͽ� ��ȯ�Ѵ�.
	 */

	public static String[] split(String strTarget, String strDelim,
			boolean bContainNull) {
		// StringTokenizer�� �����ڰ� ����8�� ��ø�Ǿ� ��; ��� ��� ���ڿ�; ��ȯ���� ��=.
		// ��� �Ʒ��� ���� �ۼ���.
		int index = 0;
		String[] resultStrArray = new String[search(strTarget, strDelim) + 1];
		String strCheck = new String(strTarget);

		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strDelim);
			if (begin == -1) {
				resultStrArray[index] = strCheck;
				break;
			} else {
				int end = begin + strDelim.length();
				if (bContainNull) {
					resultStrArray[index++] = strCheck.substring(0, begin);
				}
				strCheck = strCheck.substring(end);
				if (strCheck.length() == 0 && bContainNull) {
					resultStrArray[index] = strCheck;
					break;
				}
			}
		}
		return resultStrArray;
	}

	/**
	 * ����ڿ�(strTarget)���� ��d���ڿ�(strSearch)�� �˻�� Ƚ��, ��d���ڿ��� ��8�� 0 ; ��ȯ�Ѵ�.
	 * 
	 * @param strTarget
	 *            ����ڿ�
	 * 
	 * @param strSearch
	 *            �˻��� ���ڿ�
	 * 
	 * @return ��d���ڿ��� �˻�Ǿ�8�� �˻�� Ƚ��, �˻���� �ʾ�8�� 0 ; ��ȯ�Ѵ�.
	 */
	public static int search(String strTarget, String strSearch) {
		int result = 0;
		String strCheck = new String(strTarget);

		for (int i = 0; i < strTarget.length();) {
			int loc = strCheck.indexOf(strSearch);
			if (loc == -1) {
				break;
			} else {
				result++;
				i = loc + strSearch.length();
				strCheck = strCheck.substring(i);
			}
		}
		return result;
	}

	// string int�� ��ȯ
	public static int intValue(String str) {

		try {
			if (str == null || str.length() == 0)
				return 0;

			return Integer.parseInt(str);
		} catch (NumberFormatException nf) {
			return 0;
		}
	}

	/**
	 * ó�� ��� Check
	 */
	public static boolean isResult(int[] r) {
		if (r == null || r.length == 0)
			return false;
		else {
			for (int i = 0; i < r.length; i++)
				if (r[i] == 0)
					return false;
		}
		return true;
	}

	/**
	 * ���ڹ迭; ���� �迭�� ��ȭ
	 * 
	 * @param String
	 *            [] ���� �迭
	 * @return int [] d�� �迭
	 */
	public static int[] changeIntArrayToStringArray(String[] strArray) {

		int[] rslt = null;
		if (strArray != null) {

			rslt = new int[strArray.length];
			int cnt = strArray.length;
			for (int i = 0; i < cnt; i++) {

				rslt[i] = SLibrary.intValue(strArray[i]);
			}
		}
		return rslt;
	}

	/**
	 * ���ڹ迭; ���� �迭�� ��ȭ
	 * 
	 * @param String
	 *            [] ���� �迭
	 * @return int [] d�� �迭
	 */
	public static int[] changeIntArrayToStringArray(String[] strArray,
			int returnlength) {

		int[] rslt = null;
		if (strArray != null) {

			rslt = new int[returnlength];
			int cnt = strArray.length;
			for (int i = 0; i < cnt; i++) {

				rslt[i] = SLibrary.intValue(strArray[i]);
			}
		}
		return rslt;
	}

	/**
	 * Object[]�� ��鹮�ڰ� f�ŵ� �迭; �����Ѵ�.
	 */
	public static Object[] getNotBlankArray(Object[] obj) {

		List<Object> list = new ArrayList<Object>();

		for (int i = 0; i < list.size(); i++) {

			if (obj[i] instanceof String) {
				if (!obj[i].equals(""))
					list.add(obj[i]);
			} else {
				list.add(obj[i]);
			}
		}

		return list.toArray();
	}

	/**
	 * �ڹٽ�ũ��Ʈ ���â ����� ��ũ��Ʈ @param alert - ��� ���� @param script - ���� script
	 * 
	 * @return str : script ���ڿ�
	 */
	public static String alertScript(String alert, String script) {

		if (alert == null || alert.trim().equals(""))
			return "<script type=\"text/javascript\" language=\"javascript\"> "
					+ script + "</script>";
		else
			return "<script type=\"text/javascript\" language=\"javascript\"> alert(\""
					+ alert + "\"); " + script + "</script>";
	}

	/**
	 * ���ڿ�; ���ڷ� ��ȯ
	 */
	public static int parseInt(String str) {

		int rslt = 0;
		try {
			rslt = Integer.parseInt(str);
		} catch (Exception e) {
		}

		return rslt;
	}

	/**
	 * ���ڿ�; ���ڷ� ��ȯ
	 */
	public static long parseLong(String str) {

		long rslt = 0;
		try {
			rslt = Long.parseLong(str);
		} catch (Exception e) {
		}

		return rslt;
	}

	/**
	 * ���ڿ��� Object�迭; Format�Ͽ� ��ȯ�Ѵ�.
	 * */
	public static String messageFormat(String pattern, Object[] obj) {

		return MessageFormat.format(pattern, obj);
	}

	/**
	 * �����ڷ� ������ ù �迭�� �ߺ�; f��&d�� �� return
	 */
	public static String[] removeDupList(String list, String strSplit) {

		String[] phoneList = list.split("\\r\\n");
		StringTokenizer st;
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		String phone = null;
		String name = null;
		StringBuffer result = new StringBuffer();
		StringBuffer dupList = new StringBuffer();
		boolean b = false;

		String[] arrResult = new String[2];

		for (int i = 0; i < phoneList.length; i++) {
			st = new StringTokenizer(phoneList[i], strSplit);
			b = false;

			phone = "";
			name = "";

			while (st.hasMoreTokens()) {
				if (b == false) {
					phone = st.nextToken();
					b = true;
				} else {
					name += st.nextToken();
				}
			}
			if (hashTable.containsKey(phone)) {
				dupList.append(" + " + phone + " " + name + "\\r\\n");
			} else {
				hashTable.put(phone, name);
				result.append(phone + " " + name + "\\r\\n");
			}
		}

		/*
		 * Enumeration keys = hashTable.keys();
		 * 
		 * while (keys.hasMoreElements()) {
		 * 
		 * key = (String) keys.nextElement(); result.append(key + " " + (String)
		 * hashTable.get(key) + "\\r\\n");
		 * 
		 * }
		 */
		arrResult[0] = result.toString();
		arrResult[1] = dupList.toString();
		return arrResult;
	}

	/**
	 * �����ڷ� ������ ù �迭�� �ߺ�; f��&d�� �� return
	 */
	public static String[] removeCheckDeletList(String list,
			Hashtable<String, String> hs) {

		String[] phoneList = list.split("\\r\\n");
		StringTokenizer st;
		String phone = null;
		String name = null;
		// String key = null;
		StringBuffer result = new StringBuffer();
		StringBuffer noList = new StringBuffer();
		boolean b = false;

		String[] arrResult = new String[2];

		for (int i = 0; i < phoneList.length; i++) {
			st = new StringTokenizer(phoneList[i]);
			b = false;

			phone = "";
			name = "";

			while (st.hasMoreTokens()) {
				if (b == false) {
					phone = st.nextToken();
					b = true;
				} else {
					name += st.nextToken();
				}
			}
			if (hs.containsKey(phone)) {
				noList.append(" + " + phone + " " + name + "\\r\\n");
			} else {
				result.append(phone + " " + name + "\\r\\n");
			}
		}

		/*
		 * Enumeration keys = hashTable.keys();
		 * 
		 * while (keys.hasMoreElements()) {
		 * 
		 * key = (String) keys.nextElement(); result.append(key + " " + (String)
		 * hashTable.get(key) + "\\r\\n");
		 * 
		 * }
		 */
		arrResult[0] = result.toString();
		arrResult[1] = noList.toString();
		return arrResult;
	}

	/**
	 * �⺻ �����ڷ� ������ ù �迭�� �ߺ�; f��&d�� �� return
	 */
	public static String[] removeDupList(String list) {

		String[] phoneList = list.split("\\r\\n");
		StringTokenizer st;
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		String phone = null;
		String name = null;
		// String key = null;
		StringBuffer result = new StringBuffer();
		StringBuffer dupList = new StringBuffer();
		boolean b = false;

		String[] arrResult = new String[2];

		for (int i = 0; i < phoneList.length; i++) {
			st = new StringTokenizer(phoneList[i]);
			b = false;

			phone = "";
			name = "";

			while (st.hasMoreTokens()) {
				if (b == false) {
					phone = st.nextToken();
					b = true;
				} else {
					name += st.nextToken();
				}
			}
			if (hashTable.containsKey(phone)) {
				dupList.append(" + " + phone + " " + name + "\\r\\n");
			} else {
				hashTable.put(phone, name);
				result.append(phone + " " + name + "\\r\\n");
			}
		}

		/*
		 * Enumeration keys = hashTable.keys();
		 * 
		 * while (keys.hasMoreElements()) {
		 * 
		 * key = (String) keys.nextElement(); result.append(key + " " + (String)
		 * hashTable.get(key) + "\\r\\n");
		 * 
		 * }
		 */
		arrResult[0] = result.toString();
		arrResult[1] = dupList.toString();
		return arrResult;
	}

	/**
	 * ���ڿ��� byte�� ���Ѵ�.
	 * 
	 * @param strSource
	 * @return
	 */
	public static int getByte(String strSource) {

		int byteSize = 0;
		char[] charArray = strSource.toCharArray();
		for (int i = 0; i < strSource.length(); i++) {

			if (charArray[i] < 256)
				byteSize += 1;
			else
				byteSize += 2;
		}

		return byteSize;
	}

	/**
	 * String; ��d�� ���̸�ŭ�� ����� �� �ֵ��� �ϸ�, ���� �Ϻκи��� ��µǴ� ��쿡�� ��d�� postfix ���ڿ�; ����
	 * �߰��Ѵ�.
	 * 
	 * cutByte��: ���ڿ��� byte ���̸� �ǹ��Ѵ�. �ѱ۰� ���� 2byte character�� 2�� ����ϸ�, ���� �� �ݰ�
	 * ��ȣ�� 1�� ����Ѵ�.
	 * 
	 * @param strSource
	 *            ��ȯ�ϰ��� �ϴ� �� ���ڿ�. null�� ��� ��鹮�ڿ��� ��ȯ�ȴ�.
	 * @param cutByte
	 *            ��ȯ�� �� ����(postfix ���ڿ��� ���� ����). �ݵ�� strPostfix���ڿ��� byteũ�� �̻���
	 *            ���ڸ� �Է��ؾ� �Ѵ�. �׷��� ��: ��� �� ���ڿ�; �״�� ��ȯ�Ѵ�.
	 * @param bTrim
	 *            �� ���ڿ��� �յڿ� ��鹮�ڰ� ��;��� trim; �������� ��d�Ѵ�.
	 * @param strPostfix
	 *            ���ڿ��� �߸���� �̸� ǥ���ϱ� '�� ���ڿ�. null�� ��� "..."�� �߰��ȴ�.
	 * @return ��ȯ�� ��� ���ڿ�; return �Ѵ�. ��, strSource�� null�� ��� ��鹮�ڿ��� ��ȯ�Ǹ�
	 *         cutByte�� strPostfix���ڿ��� byteũ�� �̸��� ���ڰ� �4� ��� �� ���ڿ�; �״�� ��ȯ�Ѵ�.
	 */
	public static String cutBytes(String strSource, int cutByte, boolean bTrim,
			String strPostfix) {

		if (strSource == null)
			return "";

		int postfixSize = 0;
		for (int i = 0; i < strPostfix.length(); i++) {
			if (strPostfix.charAt(i) < 256)
				postfixSize += 1;
			else
				postfixSize += 2;
		}

		if (postfixSize > cutByte)
			return strSource;

		if (bTrim)
			strSource = strSource.trim();
		char[] charArray = strSource.toCharArray();

		int strIndex = 0;
		int byteLength = 0;
		for (; strIndex < strSource.length(); strIndex++) {

			int byteSize = 0;
			if (charArray[strIndex] < 256) {
				// 1byte character �̸�
				byteSize = 1;
			} else {
				// 2byte character �̸�
				byteSize = 2;
			}

			if ((byteLength + byteSize) > cutByte - postfixSize) {
				break;
			}

			byteLength = byteLength += byteSize;
		}

		if (strIndex == strSource.length())
			strPostfix = "";
		else {
			if ((byteLength + postfixSize) < cutByte)
				strPostfix = " " + strPostfix;
		}

		return strSource.substring(0, strIndex) + strPostfix;
	}

	/**
	 * String; ��d�� ���̸�ŭ�� ����� �� �ֵ��� �ϸ�, ���� �Ϻκи��� ��µǴ� ��쿡�� ��d�� postfix ���ڿ�; ����
	 * �߰��Ѵ�.
	 * 
	 * cutByte��: ���ڿ��� byte ���̸� �ǹ��Ѵ�. �ѱ۰� ���� 2byte character�� 2�� ����ϸ�, ���� �� �ݰ�
	 * ��ȣ�� 1�� ����Ѵ�.
	 * 
	 * @param strSource
	 *            ��ȯ�ϰ��� �ϴ� �� ���ڿ�. null�� ��� ��鹮�ڿ��� ��ȯ�ȴ�.
	 * @param cutByte
	 *            ��ȯ�� �� ����(postfix ���ڿ��� ���� ����). �ݵ�� strPostfix���ڿ��� byteũ�� �̻���
	 *            ���ڸ� �Է��ؾ� �Ѵ�. �׷��� ��: ��� �� ���ڿ�; �״�� ��ȯ�Ѵ�.
	 * @param bTrim
	 *            �� ���ڿ��� �յڿ� ��鹮�ڰ� ��;��� trim; �������� ��d�Ѵ�.
	 * @param strPostfix
	 *            ���ڿ��� �߸���� �̸� ǥ���ϱ� '�� ���ڿ�. null�� ��� "..."�� �߰��ȴ�.
	 * @return ��ȯ�� ��� ���ڿ��� ������ ���ڿ�
	 */
	public static int cutBytesIndex(String strSource, int cutByte, boolean bTrim) {

		int strIndex = 0;

		if (strSource == null)
			return strIndex;

		if (bTrim)
			strSource = strSource.trim();
		char[] charArray = strSource.toCharArray();

		int byteLength = 0;
		for (; strIndex < strSource.length(); strIndex++) {

			int byteSize = 0;
			if (charArray[strIndex] < 256) {
				// 1byte character �̸�
				byteSize = 1;
			} else {
				// 2byte character �̸�
				byteSize = 2;
			}

			if ((byteLength + byteSize) > cutByte) {
				break;
			}

			byteLength = byteLength += byteSize;
		}

		return strIndex;
	}

	/**
	 * String; ��d�� ���� ��'�� ������ ��ȯ�Ѵ�.
	 * 
	 * cutByte��: ���ڿ��� byte ���̸� �ǹ��Ѵ�. �ѱ۰� ���� 2byte character�� 2�� ����ϸ�, ���� �� �ݰ�
	 * ��ȣ�� 1�� ����Ѵ�.
	 * 
	 * @param strSource
	 *            ��ȯ�ϰ��� �ϴ� �� ���ڿ�. null�� ��� ��鹮�ڿ��� ��ȯ�ȴ�.
	 * @param cutByte
	 *            ��ȯ�� �� ����(postfix ���ڿ��� ���� ����). �ݵ�� strPostfix���ڿ��� byteũ�� �̻���
	 *            ���ڸ� �Է��ؾ� �Ѵ�. �׷��� ��: ��� �� ���ڿ�; �״�� ��ȯ�Ѵ�.
	 * @param bTrim
	 *            �� ���ڿ��� �յڿ� ��鹮�ڰ� ��;��� trim; �������� ��d�Ѵ�.
	 * @return ��ȯ�� ��� ���ڿ�; return �Ѵ�. ��, strSource�� null�� ��� ��鹮�ڿ��� ��ȯ�Ǹ�
	 *         cutByte�� strPostfix���ڿ��� byteũ�� �̸��� ���ڰ� �4� ��� �� ���ڿ�; �״�� ��ȯ�Ѵ�.
	 */
	public static String[] cutBytesGetArray(String strSource, int cutByte,
			boolean bTrim) {

		String[] arrRslt = null;
		int totalSize = SLibrary.getByte(strSource);
		int cnt = (int) Math.ceil(totalSize / cutByte);// �ø�
		cnt++;// ������ byte�� '��

		arrRslt = new String[cnt];

		int pre = 0;
		int cur = 0;
		for (int i = 0; i < cnt; i++) {

			cur = SLibrary.cutBytesIndex(strSource, cutByte, bTrim);
			arrRslt[i] = strSource.substring(pre, cur);
			pre = cur;

		}

		return arrRslt;
	}

	/**
	 * �޴����ȣ �˻�
	 */
	public static boolean checkHp(String str) {

		String hp = SLibrary.replaceAll(str.trim(), "-", "");
		return SLibrary.searchPattern(hp, "(0\\d{2})(\\d{3,4})(\\d{4})");
	}

	/**
	 * FAX �˻�
	 */
	public static boolean checkFax(String str) {

		String hp = SLibrary.replaceAll(str.trim(), "-", "");
		return SLibrary.searchPattern(hp, "(0\\d{1,3})(\\d{3,4})(\\d{4})");
	}

	/**
	 * Email �˻�
	 */
	public static boolean checkEmail(String str) {

		String pattern = "(^[_0-9a-zA-Z-\\.]+[_0-9a-zA-Z-\\.]*@[_0-9a-zA-Z-]+\\.[a-zA-Z]+[a-zA-Z\\.]+([a-zA-Z]+)*$)";
		return SLibrary.searchPattern(str, pattern);
	}

	/**
	 * String [] ; string '','','' �� ��ȯ
	 * 
	 * @param temp
	 *            - ���ڹ迭
	 * @return String
	 */
	public static String inQuery(String[] temp) {

		String in = "";
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				if (i == temp.length - 1) {
					in += "'" + temp[i] + "'";
				} else {
					in += "'" + temp[i] + "',";
				}
			}
		}
		return in;
	}

	/**
	 * int [] ; string 1,23,1 �� ��ȯ
	 * 
	 * @param temp
	 *            - ���ڹ迭
	 * @return String
	 */
	public static String inQuery(int[] temp) {

		String in = "";
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				if (i == temp.length - 1) {
					in += Integer.toString(temp[i]);
				} else {
					in += Integer.toString(temp[i]) + ",";
				}
			}
		}
		return in;
	}

	/**
	 * ���ڸ� ���ڿ� '0'; �ٿ� �ش�
	 * 
	 * @param str
	 *            - ���ڿ�
	 */
	public static String addTwoSizeNumber(String str) {
		int num = Integer.parseInt(str);
		if (num < 10) {
			return "0" + Integer.toString(num);
		} else {
			return str;
		}

	}

	/**
	 * ���ڸ� ���ڿ� '0'; �ٿ� �ش�
	 * 
	 * @param str
	 *            - ���ڿ�
	 */
	public static String addTwoSizeNumber(int num) {
		if (num < 10) {
			return "0" + Integer.toString(num);
		} else {
			return Integer.toString(num);
		}

	}

	/**
	 * ������ �� ���ڿ�; ��ȯ�Ѵ�. ����
	 */
	public static String pgString(String sql) {

		String preString = "SELECT * FROM ( SELECT A.*, ROWNUM RNUM , MAX(ROWNUM) OVER(ORDER BY ROWNUM DESC) TOTALCNT FROM ( ";
		String lastString = " ) A  ) WHERE RNUM BETWEEN (TOTALCNT +1)-? AND (TOTALCNT +1)-? ORDER BY RNUM DESC";

		return preString + sql + lastString;
	}

	/**
	 * ������ �� ���ڿ�; ��ȯ�Ѵ�. d��
	 */
	public static String pgStringASC(String sql) {

		String preString = "SELECT * FROM ( SELECT A.*, ROWNUM RNUM , MAX(ROWNUM) OVER(ORDER BY ROWNUM DESC) TOTALCNT FROM ( ";
		String lastString = " ) A  ) WHERE RNUM BETWEEN ? AND ? ORDER BY RNUM ASC";

		return preString + sql + lastString;
	}

	/**
	 * ��ü�Ǽ� �� ���ڿ�; ��ȯ�Ѵ�.
	 */
	public static String countQueryString(String sql) {

		String preString = "SELECT count(*) as cnt FROM (  ";
		String lastString = " ) ";

		return preString + sql + lastString;
	}

	/**
	 * ���ڿ� �迭���� �ش� ���ڰ� �ִ��� Ȯ���Ѵ�
	 */
	public static boolean isArrayValue(String[] arr, String findString) {

		boolean b = false;
		int cnt = 0;
		if (arr != null) {
			cnt = arr.length;
			for (int i = 0; i < cnt; i++) {
				if (arr[i].equals(findString)) {
					b = true;
					break;
				}

			}
		}

		return b;

	}

	/**
	 * �迭; �޾� ����� ���ڿ��� �����Ѵ�. �̶� �� ������Ʈ ���̿� ���й��ڿ�; �߰��Ѵ�.<br>
	 * 
	 * @param aobj
	 *            ���ڿ��� ���� �迭
	 * @param s
	 *            �� ������Ʈ�� ���� ���ڿ�
	 * @return ����� ���ڿ�
	 * 
	 *         <code>
	 * String[] source = new String[] {"AAA","BBB","CCC"};<br>
	 * String result = TextUtil.join(source,"+");<br>
	 * </code> <code>result</code>�� <code>"AAABBBCCC"</code>�� ����� �ȴ�.
	 */
	public static String join(Object aobj[], String s) {
		StringBuffer stringbuffer = new StringBuffer();
		int i = aobj.length;
		if (i > 0) {
			stringbuffer.append(aobj[0].toString());
		}
		for (int j = 1; j < i; j++) {
			stringbuffer.append(s);
			stringbuffer.append(aobj[j].toString());
		}

		return stringbuffer.toString();
	}

	/**
	 * 2009-08-01 12:00:0.0 �ڿ� ���ڸ� ��f
	 */
	public static String yyyymmddhhmiss(String strDate) {

		if (strDate == null || strDate.length() < 20)
			return strDate;
		else
			return strDate.substring(0, strDate.length() - 2);
	}

	/**
	 * Clob Data type -> String type
	 * 
	 * @param re
	 * @return
	 * @throws IOException
	 */
	public static String getClobString(Reader re) throws IOException {

		StringBuffer data = new StringBuffer();
		char[] buf = new char[1024];

		int cnt = 0;
		if (re != null) {
			while ((cnt = re.read(buf)) != -1) {
				data.append(buf, 0, cnt);
			}
		}

		return data.toString();

	}

	/**
	 * html; �ؽ�Ʈ�� ���̵��� �����Ѵ�.
	 * 
	 * @param str
	 * @return
	 */
	public static String textToHtml(String str) {

		return str.replaceAll("<", "&lt").replaceAll("</", "&lt/");

	}

	/*
	 * public static void loadingBegin(String msg, javax.servlet.jsp.JspWriter
	 * out) throws Exception{ out.print(alertScript("",
	 * "parent.loadingBegin(\""+msg+"\");")); out.flush(); }
	 * 
	 * public static void loadingEnd(javax.servlet.jsp.JspWriter out) throws
	 * Exception{ out.print(alertScript("", "parent.loadingEnd();"));
	 * out.flush(); }
	 */
	public static String IfNull(HashMap<String, String> hm, String key) {

		if (hm.containsKey(key))
			return hm.get(key);
		else
			return "";
	}
	
	public static String IfNull(String[] arr, int index) {

		if (arr != null && arr.length > index)
			return SLibrary.IfNull( arr[index] );
		else
			return "";
	}

	/**
	 * HashMap ; <option> �ױ׷� ��ȯ�Ѵ�.
	 * 
	 * @param rs
	 * @param selectedValue
	 * @return
	 * @throws Exception
	 */
	public static String getSelectTag(HashMap<String, String> hm,
			String selectedValue) {

		StringBuffer strBuffer = new StringBuffer();
		String tmp = "";
		String selected = "";
		Iterator<String> keys = hm.keySet().iterator();

		while (keys.hasNext()) {
			tmp = keys.next();
			selected = hm.get(tmp).equals(selectedValue) ? "selected" : "";
			strBuffer.append("<option value=\"" + tmp + "\" " + selected + ">"
					+ hm.get(tmp) + "</option>");
		}

		return strBuffer.toString();
	}

	public static String propertiesHangle(String s) {

		String rslt = "";
		try {
			if (s != null)
				rslt = new String(s.getBytes("UTF-8"), "euc-kr");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.toString());
		}

		return rslt;
	}

	public static String getExcelColumnTitle(int index) {

		int base = (int) (char) 'A';
		int div = (int) (char) 'Z' - base + 1;
		StringBuffer buf = new StringBuffer();

		if ((index - 1) >= 0) {

			// twoLength String
			if (index - 1 >= div) {

				buf.append(new Character((char) (base
						+ (int) ((index - 1) / div) - 1)).toString());
				buf.append(new Character(
						(char) (base + (int) ((index - 1) % div))).toString());
			} else {
				buf.append(new Character((char) (base + index - 1)).toString());
			}
		}

		return buf.toString();
	}

	public static String getPhone(String phone) {
		String number = phone.replaceAll("[^\\d]*", "");
		boolean b = Pattern
				.matches("^0[17][016789]-?\\d{3,4}-?\\d{4}$", number);
		return b ? number : null;
	}

	public static Boolean isFile(String path) {
		
		Boolean b = false;
		File f = new File(path);
		if (f.exists())	b = true;
		return b;
	}
	
	public static int mysqlTotalCount(Connection conn) {
		
		Statement stmt = null;
		ResultSet rs = null;
		int rslt = 0;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT FOUND_ROWS()");
			while (rs.next()){ rslt = rs.getInt(1); }
			rs.close();
			stmt.close();
		} catch (Exception e) {	}
		
		return rslt;
	}
	
	
	public static int pattenCnt(String str, String patten) {
		
		Pattern p = Pattern.compile(patten);
		Matcher m = p.matcher(str);
		int count = 0;
		for( int i = 0; m.find(i); i = m.end())
			count++;
		
		return count;
	}
	
	 public static void fileMake(String makeFileName) {
	  File f1 = new File(makeFileName);
	  try {
	   f1.createNewFile();
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
	 }
	 
	 //파일을 삭제하는 메소드
	 public static void fileDelete(String deleteFileName) {
	  File I = new File(deleteFileName);
	  I.delete();
	 }
	 
	 //파일을 복사하는 메소드
	 public static void fileCopy(String inFileName, String outFileName) {
	  try {
	   FileInputStream fis = new FileInputStream(inFileName);
	   FileOutputStream fos = new FileOutputStream(outFileName);
	   
	   int data = 0;
	   while((data=fis.read())!=-1) {
	    fos.write(data);
	   }
	   fis.close();
	   fos.close();
	   
	  } catch (IOException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	 }
	 
	 //파일을 이동하는 메소드
	 public static void fileMove(String inFileName, String outFileName) {
	  try {
	   FileInputStream fis = new FileInputStream(inFileName);
	   FileOutputStream fos = new FileOutputStream(outFileName);
	   
	   int data = 0;
	   while((data=fis.read())!=-1) {
	    fos.write(data);
	   }
	   fis.close();
	   fos.close();
	   
	   //복사한뒤 원본파일을 삭제함
	   fileDelete(inFileName);
	   
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
	 }
	 
	 //디렉토리의 파일 리스트를 읽는 메소드
	 public static List<File> getDirFileList(String dirPath)
	 {
	  // 디렉토리 파일 리스트
	  List<File> dirFileList = null;
	  
	  // 파일 목록을 요청한 디렉토리를 가지고 파일 객체를 생성함
	  File dir = new File(dirPath);
	  
	  // 디렉토리가 존재한다면
	  if (dir.exists())
	  {
	   // 파일 목록을 구함
	   File[] files = dir.listFiles();
	   
	   // 파일 배열을 파일 리스트로 변화함 
	   dirFileList = Arrays.asList(files);
	  }
	  
	  return dirFileList;
	 }

}
