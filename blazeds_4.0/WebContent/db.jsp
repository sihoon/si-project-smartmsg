<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR" import="java.sql.* , com.common.*, com.common.properties.*"%>
	<%@page import="java.text.DecimalFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>DB���� Ȯ��</title>
</head>
<body>
<%
out.println( request.getContextPath()+"@@@" );
ServletContext context = session.getServletContext();
String realContextPath = context.getRealPath(request.getContextPath());
out.println(realContextPath);
/*
	ReadPropertiesAble rp = ReadProperties.getInstance();
	
	String str = rp.getPropertiesFileValue("filtering.properties","filteringMessage");
	String newStr = new String(str.getBytes("8859_1"), "KSC5601");
	out.println( newStr);
*/
//	out.println(VbyP.getValue("adminPhones"));

//UserInformationVO uvo = null;
	Connection conn = null;
	conn = VbyP.getDB();
	if (conn != null) {
		out.println("ok");
		conn.close();
	}
	else
		out.println("no");
	/*
	conn = VbyP.getDB("sms1");
	if (conn != null) {
		out.println("jdbc/sms1ok");
		conn.close();
	}
	else
		out.println("jdbc/sms1no");
	*/

%>
</body>
</html>