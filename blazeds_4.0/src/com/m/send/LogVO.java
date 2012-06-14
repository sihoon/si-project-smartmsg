package com.m.send;

import java.io.Serializable;

public class LogVO implements Serializable {
	
	static final long serialVersionUID = 1222179582713735628L;
	
	int idx = 0;
	String user_id = "";
	String line = "";
	String mode = "";
	String method = "";
	String message = "";
	String user_ip = "";
	String timeSend = "";
	String timeWrite = "";
	String ynDel = "N";
	String delType = "";
	String timeDel = "";
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}
	public String getTimeSend() {
		return timeSend;
	}
	public void setTimeSend(String timeSend) {
		this.timeSend = timeSend;
	}
	public String getTimeWrite() {
		return timeWrite;
	}
	public void setTimeWrite(String timeWrite) {
		this.timeWrite = timeWrite;
	}
	public String getYnDel() {
		return ynDel;
	}
	public void setYnDel(String ynDel) {
		this.ynDel = ynDel;
	}
	public String getDelType() {
		return delType;
	}
	public void setDelType(String delType) {
		this.delType = delType;
	}
	public String getTimeDel() {
		return timeDel;
	}
	public void setTimeDel(String timeDel) {
		this.timeDel = timeDel;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	

}
