package com.m.log.telecom;

import java.util.List;

import com.common.db.SessionManager;
import com.m.send.LogVO;
import com.m.send.MessageVO;
import com.m.send.Sent;

public class KTSentDao implements Sent {
	
	
	
	public List<MessageVO> getList(LogVO vo) {
		
		if(vo.getMode().equals("SMS")) return getListSMS(vo);
		else  return getListMMS(vo);
	}
	
	private List<MessageVO> getListSMS(LogVO vo) {
		
		SessionManager sm = new SessionManager(sqlMapper.openSession(true));
		return (List)sm.selectList(ns + "select_ktsms_list", vo);
	}
	
	private List<MessageVO> getListMMS(LogVO vo) {
		
		SessionManager sm = new SessionManager(sqlMapper.openSession(true));
		return (List)sm.selectList(ns + "select_ktmms_list", vo);
	}
}
