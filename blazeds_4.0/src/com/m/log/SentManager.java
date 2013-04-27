package com.m.log;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.common.VbyP;
import com.common.db.PreparedExecuteQueryManager;
import com.m.send.LogVO;

public class SentManager implements ISent {
	
	static ISent sent = new SentManager();
	public static ISent getInstance() {
		return sent;
	}
	private SentManager(){}

	@Override
	public ArrayList<LogVO> getList(Connection conn, String user_id, String yyyymm) {
		
		ArrayList<HashMap<String, String>> al = null;
		
		String SQL = VbyP.getSQL( "sent_list" );
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		pq.setPrepared(conn, SQL);
		pq.setString(1, user_id);
		pq.setString(2, yyyymm);
		al = pq.ExecuteQueryArrayList();
		
		return parseVO(al);
	}

	@Override
	public int updateLog(Connection conn, LogVO slvo) {
		
		//UPDATE sent_log SET line = ?, mode = ?, method = ?, message = ?, user_ip = ?, timeSend = ?, timeWrite = ?, ynDel = ?, delType = ?, timeDel = ? where idx=?
		int rsltCount = 0;
		String SQL = VbyP.getSQL("sent_update");
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		pq.setPrepared(conn, SQL);
		pq.setString(1, slvo.getLine());
		pq.setString(2, slvo.getMode());
		pq.setString(3, slvo.getMethod());
		pq.setString(4, slvo.getMessage());
		pq.setString(5, slvo.getUser_ip());
		pq.setString(6, slvo.getTimeSend());
		pq.setString(7, slvo.getTimeWrite());
		pq.setString(8, slvo.getYnDel());
		pq.setString(9, slvo.getDelType());
		pq.setString(10, slvo.getTimeDel());
		pq.setInt(11, slvo.getIdx());
		
		rsltCount = pq.executeUpdate();			
		
		return rsltCount;
	}
	
	@Override
	public ArrayList<LogVO> getListAll(Connection conn, String dtStart, String dtEnd) {
		
		ArrayList<LogVO> al = new ArrayList<LogVO>();
		ArrayList<HashMap<String, String>> temp = null;
		
		String SQL = VbyP.getSQL("sent_list");
		PreparedExecuteQueryManager pq = new PreparedExecuteQueryManager();
		
		pq.setPrepared(conn, SQL);
		pq.setString(1, dtStart);
		pq.setString(2, dtEnd);
		
		temp = pq.ExecuteQueryArrayList();
		
		if (temp != null && temp.size() > 0) {
			int cnt = temp.size();
			for (int i = 0; i < cnt; i++) {
				al.add(new LogVO(temp.get(i)));
			}
		}
		return al;
		
	}
	
	private ArrayList<LogVO> parseVO(ArrayList<HashMap<String, String>> al) {
		
		ArrayList<LogVO> result = new ArrayList<LogVO>();
		
		if (al != null) {
			int cnt = al.size();
			for (int i = 0; i < cnt; i++) {
				result.add( new LogVO(al.get(i)) );
			}
		}
		
		return result;
	}

}
