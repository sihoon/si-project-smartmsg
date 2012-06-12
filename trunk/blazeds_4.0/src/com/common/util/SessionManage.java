/*
 *@(#)SessionMangement.java 1.0, 2009. 11. 5.
 *
 *Copyright(c) 2009 ehancast All rights reserved
 */
package com.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;


public class SessionManage {
	
	static SessionManage sm = null;
	
	public static SessionManage getInstance(){
		
		if (sm == null) 
			sm = new SessionManage();
		return sm;
	}
	
	public SessionManage(){}
	
	/**
	 * HashMap �����͸� �޾� key, value ���� ����(HttpSession)�� �����Ѵ�.
	 * @param session
	 * @param hm
	 * @param interval
	 * @return  boolean
	 */
	public boolean create(HttpSession session, HashMap<String, String> hm, int interval) {
		
		boolean b = false;
		try {
			Set<Map.Entry<String, String>> set = hm.entrySet();
			for(Map.Entry<String, String> me : set) {
	
			  session.setAttribute(me.getKey(), me.getValue());
			}
			b = true;
			session.setMaxInactiveInterval(interval);
		}catch(Exception e) {}
		
		return b;
	}
	
	/**
	 * ������� ��� session �� HashMap<String, String>�� ��ȯ�Ѵ�.
	 * @param session
	 * @return
	 */
	public HashMap<String, String> getSession(HttpSession session) {
		
		HashMap<String, String> hm = new HashMap<String, String>();
		Enumeration<String> e = session.getAttributeNames();
		
		String temp = null;
		while(e.hasMoreElements()) {
			
			temp = e.nextElement();
			hm.put(temp, (String)session.getAttribute(temp));
		}
		
		return hm;
	}
	
	/**
	 * Ư�� ���ǰ��� ��ȯ�Ѵ�.
	 * @param session
	 * @param name
	 * @return
	 */
	public String getSession(HttpSession session, String name) {
		
		String rslt = (String)session.getAttribute(name);
		if (rslt != null)
			return rslt;
		else 
			return "";
	}
	
	/**
	 * Ư�� ���ǰ��� �ִ��� Ȯ���Ѵ�.
	 * @param session
	 * @param sessionName
	 * @return
	 */
	public boolean bCheck(HttpSession session, String sessionName) {
		
		if (session.getAttribute(sessionName) == null)
			return false;
		else
			return true;
	}
	
	/**
	 * ������� ������ ���� �Ѵ�.
	 * @param session
	 */
	public void remove(HttpSession session) {
		
		session.invalidate();
	}
}
