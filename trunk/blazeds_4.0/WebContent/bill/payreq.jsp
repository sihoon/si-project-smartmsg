<%@page import="com.m.member.UserSession"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page contentType="text/html;charset=EUC-KR" %>
<%@ page import="java.io.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="lgdacom.XPayClient.XPayClient"%>
<%
	UserSession us = (UserSession)session.getAttribute("user_id");
	String user_id = us.getUser_id();
	if (SLibrary.isNull(us.getUser_id())) {
		out.println(SLibrary.alertScript("�α��� �� �̿� �ϼ���.", ""));
	}
    /*
     * [���� ������û ������(STEP2-1)]
     *
     * ���������������� �⺻ �Ķ���͸� ���õǾ� ������, ������ �ʿ��Ͻ� �Ķ���ʹ� �����޴����� �����Ͻþ� �߰� �Ͻñ� �ٶ��ϴ�.
     */

    /*
     * 1. �⺻���� ������û ���� ����
     *
     * �⺻������ �����Ͽ� �ֽñ� �ٶ��ϴ�.(�Ķ���� ���޽� POST�� ����ϼ���)
     */
    String CST_PLATFORM         = "test";//request.getParameter("CST_PLATFORM");                 //LG���÷��� �������� ����(test:�׽�Ʈ, service:����)
    String CST_MID              = "fd_adsoft2";//request.getParameter("CST_MID");                      //LG���÷������� ���� �߱޹����� �������̵� �Է��ϼ���.
    String LGD_MID              = ("test".equals(CST_PLATFORM.trim())?"t":"")+CST_MID;  //�׽�Ʈ ���̵�� 't'�� �����ϰ� �Է��ϼ���.
                                                                                        //�������̵�(�ڵ�����)
    String LGD_OID              = user_id+"_"+SLibrary.getUnixtimeStringSecond();//request.getParameter("LGD_OID");                      //�ֹ���ȣ(�������� ����ũ�� �ֹ���ȣ�� �Է��ϼ���)
    String LGD_AMOUNT           = request.getParameter("LGD_AMOUNT");                   //�����ݾ�("," �� ������ �����ݾ��� �Է��ϼ���)
    String LGD_MERTKEY          = "be40663ff0756bbc25c90073def17e74";													//����MertKey(mertkey�� ���������� -> ������� -> ���������������� Ȯ���ϽǼ� �ֽ��ϴ�)
	String LGD_BUYER            = user_id;//request.getParameter("LGD_BUYER");                    //�����ڸ�
    String LGD_PRODUCTINFO      = "���ڳ�Ʈ";//request.getParameter("LGD_PRODUCTINFO");              //��ǰ��
    String LGD_BUYEREMAIL       = "";//request.getParameter("LGD_BUYEREMAIL");               //������ �̸���
    String LGD_TIMESTAMP        = SLibrary.getUnixtimeStringSecond();//request.getParameter("LGD_TIMESTAMP");                //Ÿ�ӽ�����
    String LGD_CUSTOM_SKIN      = "red";                                                //�������� ����â ��Ų(red, blue, cyan, green, yellow)
    String LGD_BUYERID          = request.getParameter("LGD_BUYERID");       			//������ ���̵�
    String LGD_BUYERIP          = request.getParameter("LGD_BUYERIP");       			//������IP

    /*
     * �������(������) ���� ������ �Ͻô� ��� �Ʒ� LGD_CASNOTEURL �� �����Ͽ� �ֽñ� �ٶ��ϴ�. 
     */    
    String LGD_CASNOTEURL		= "http://����URL/cas_noteurl.jsp";    

    /*
     *************************************************
     * 2. MD5 �ؽ���ȣȭ (�������� ������) - BEGIN
     *
     * MD5 �ؽ���ȣȭ�� �ŷ� �������� �������� ����Դϴ�.
     *************************************************
     *
     * �ؽ� ��ȣȭ ����( LGD_MID + LGD_OID + LGD_AMOUNT + LGD_TIMESTAMP + LGD_MERTKEY )
     * LGD_MID          : �������̵�
     * LGD_OID          : �ֹ���ȣ
     * LGD_AMOUNT       : �ݾ�
     * LGD_TIMESTAMP    : Ÿ�ӽ�����
     * LGD_MERTKEY      : ����MertKey (mertkey�� ���������� -> ������� -> ���������������� Ȯ���ϽǼ� �ֽ��ϴ�)
     *
     * MD5 �ؽ������� ��ȣȭ ������ ����
     * LG���÷������� �߱��� ����Ű(MertKey)�� ȯ�漳�� ����(lgdacom/conf/mall.conf)�� �ݵ�� �Է��Ͽ� �ֽñ� �ٶ��ϴ�.
     */
    StringBuffer sb = new StringBuffer();
    sb.append(LGD_MID);
    sb.append(LGD_OID);
    sb.append(LGD_AMOUNT);
    sb.append(LGD_TIMESTAMP);
    sb.append(LGD_MERTKEY);

    byte[] bNoti = sb.toString().getBytes();
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] digest = md.digest(bNoti);

    StringBuffer strBuf = new StringBuffer();
    for (int i=0 ; i < digest.length ; i++) {
        int c = digest[i] & 0xff;
        if (c <= 15){
            strBuf.append("0");
        }
        strBuf.append(Integer.toHexString(c));
    }

    String LGD_HASHDATA = strBuf.toString();
    String LGD_CUSTOM_PROCESSTYPE = "TWOTR";
    /*
     *************************************************
     * 2. MD5 �ؽ���ȣȭ (�������� ������) - END
     *************************************************
     */
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>LG���÷��� eCredit���� �����׽�Ʈ</title>
<script type="text/javascript">
/*
 * �������� ������û�� PAYKEY�� �޾Ƽ� �������� ��û.
 */
function doPay_ActiveX(){
    ret = xpay_check(document.getElementById('LGD_PAYINFO'), '<%= CST_PLATFORM %>');

    if (ret=="00"){     //ActiveX �ε� ����
        var LGD_RESPCODE        = dpop.getData('LGD_RESPCODE');       //����ڵ�
        var LGD_RESPMSG         = dpop.getData('LGD_RESPMSG');        //����޼���

        if( "0000" == LGD_RESPCODE ) { //��������
            var LGD_PAYKEY      = dpop.getData('LGD_PAYKEY');         //LG���÷��� ����KEY
            var msg = "������� : " + LGD_RESPMSG + "\n";
            msg += "LGD_PAYKEY : " + LGD_PAYKEY +"\n\n";
            document.getElementById('LGD_PAYKEY').value = LGD_PAYKEY;
            alert(msg);
            document.getElementById('LGD_PAYINFO').submit();
        } else { //��������
            alert("������ �����Ͽ����ϴ�. " + LGD_RESPMSG);
            /*
             * �������� ȭ�� ó��
             */
        }
    } else {
        alert("LG U+ ���ڰ����� ���� ActiveX Control��  ��ġ���� �ʾҽ��ϴ�.");
        /*
         * �������� ȭ�� ó��
         */
    }      
}

function isActiveXOK(){
	if(lgdacom_atx_flag == true){
    	document.getElementById('LGD_BUTTON1').style.display='none';
        document.getElementById('LGD_BUTTON2').style.display='';
        doPay_ActiveX();
	}else{
		document.getElementById('LGD_BUTTON1').style.display='';
        document.getElementById('LGD_BUTTON2').style.display='none';	
	}
}
</script>
</head>

<body onload="isActiveXOK();">
<div id="LGD_ACTIVEX_DIV"/> <!-- ActiveX ��ġ �ȳ� Layer �Դϴ�. �������� ������. -->
<form method="post" id="LGD_PAYINFO" action="payres.jsp">
<table>
    <tr>
        <td>������ �̸� </td>
        <td><%= LGD_BUYER %></td>
    </tr>
    <tr>
        <td>������ IP </td>
        <td><%= LGD_BUYERIP %></td>
    </tr>
    <tr>
        <td>������ ID </td>
        <td><%= LGD_BUYERID %></td>
    </tr>    
    <tr>
        <td>��ǰ���� </td>
        <td><%= LGD_PRODUCTINFO %></td>
    </tr>
    <tr>
        <td>�����ݾ� </td>
        <td><%= LGD_AMOUNT %></td>
    </tr>
    <tr>
        <td>������ �̸��� </td>
        <td><%= LGD_BUYEREMAIL %></td>
    </tr>
    <tr>
        <td>�ֹ���ȣ </td>
        <td><%= LGD_OID %></td>
    </tr>
    <tr>
        <td colspan="2">* �߰� �� ������û �Ķ���ʹ� �޴����� �����Ͻñ� �ٶ��ϴ�.</td>
    </tr>
    <tr>
        <td colspan="2"></td>
    </tr>    
    <tr>
        <td colspan="2">
		<div id="LGD_BUTTON1">������ ���� ����� �ٿ� ���̰ų�, ����� ��ġ���� �ʾҽ��ϴ�. </div>
		<div id="LGD_BUTTON2" style="display:none"><input type="button" value="������û" onclick="doPay_ActiveX();"/> </div>        
        </td>
    </tr>    
</table>
<br>

<input type="hidden" name="CST_PLATFORM"                value="<%= CST_PLATFORM %>">                   <!-- �׽�Ʈ, ���� ���� -->
<input type="hidden" name="CST_MID"                     value="<%= CST_MID %>">                        <!-- �������̵� -->
<input type="hidden" name="LGD_MID"                     value="<%= LGD_MID %>">                        <!-- �������̵� -->
<input type="hidden" name="LGD_OID"                     value="<%= LGD_OID %>">                        <!-- �ֹ���ȣ -->
<input type="hidden" name="LGD_BUYER"                   value="<%= LGD_BUYER %>">                      <!-- ������ -->
<input type="hidden" name="LGD_PRODUCTINFO"             value="<%= LGD_PRODUCTINFO %>">                <!-- ��ǰ���� -->
<input type="hidden" name="LGD_AMOUNT"                  value="<%= LGD_AMOUNT %>">                     <!-- �����ݾ� -->
<input type="hidden" name="LGD_BUYEREMAIL"              value="<%= LGD_BUYEREMAIL %>">                 <!-- ������ �̸��� -->
<input type="hidden" name="LGD_CUSTOM_SKIN"             value="<%= LGD_CUSTOM_SKIN %>">                <!-- ����â SKIN -->
<input type="hidden" name="LGD_CUSTOM_PROCESSTYPE"      value="<%= LGD_CUSTOM_PROCESSTYPE %>">         <!-- Ʈ����� ó����� -->
<input type="hidden" name="LGD_TIMESTAMP"               value="<%= LGD_TIMESTAMP %>">                  <!-- Ÿ�ӽ����� -->
<input type="hidden" name="LGD_HASHDATA"                value="<%= LGD_HASHDATA %>">                   <!-- MD5 �ؽ���ȣ�� -->
<input type="hidden" name="LGD_PAYKEY"                  id="LGD_PAYKEY">   							   <!-- LG���÷��� PAYKEY(������ �ڵ�����)-->
<input type="hidden" name="LGD_VERSION"         		value="JSP_XPay_1.0">
<input type="hidden" name="LGD_BUYERIP"                 value="<%= LGD_BUYERIP %>">           			<!-- ������IP -->
<input type="hidden" name="LGD_BUYERID"                 value="<%= LGD_BUYERID %>">           			<!-- ������ID -->

<!-- �������(������) ���������� �Ͻô� ���  �Ҵ�/�Ա� ����� �뺸�ޱ� ���� �ݵ�� LGD_CASNOTEURL ������ LG ���÷����� �����ؾ� �մϴ� . -->
<input type="hidden" name="LGD_CASNOTEURL"          	value="<%= LGD_CASNOTEURL %>" >                 <!-- ������� NOTEURL -->

</form>
</body>
<!--  xpay.js�� �ݵ�� body �ؿ� �νñ� �ٶ��ϴ�. -->
<!--  UTF-8 ���ڵ� ��� �ô� xpay.js ��� xpay_utf-8.js ��  ȣ���Ͻñ� �ٶ��ϴ�.-->
<script language="javascript" src="<%=request.getScheme()%>://xpay.uplus.co.kr<%="test".equals(CST_PLATFORM)?(request.getScheme().equals("https")?":7443":":7080"):""%>/xpay/js/xpay_utf-8.js" type="text/javascript">
</script>
</html>
