package com.common.util;

import com.oreilly.servlet.multipart.Part;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.FilePart;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.common.VbyP;
import com.common.util.SLibrary;
import com.common.util.UnixtimeFileRenamePolicy;

public class UploadMultipart {
	
	int maxSize = 1024 * 1024;
	String Encoding = "euc-kr";
	String [] checkContentType = null;
	String contentType = null;
	String realFileName = null;//�������ϸ�
	String uploadedFileName = null;//�ߺ��˻��� ���ε�� ���ϸ�
	StringBuffer errorMsg = new StringBuffer();
	long fileSize = 0;
	String addFileName = null;//�����̸� ���� ������ ���� �̸�
	HashMap<String, String> hm = null;
	
	
	public void setCheckContentType(String[] str) {this.checkContentType = str;}
	public void setUnixTimeFile(String addString) {this.addFileName = addString;}
	
	public String getContentType() { return this.contentType; }	
	public long getFileByteSize() { return this.fileSize; }	
	public String getRealFileName(){ return this.realFileName; }
	public String getUploadedFileName(){ return this.uploadedFileName; }	
	public String getErrorMsg(){ if ( this.errorMsg.toString() != null ) return this.errorMsg.toString(); else return ""; }	
	public HashMap<String, String> getParameter(){ return this.hm;}
	
	public boolean Upload(HttpServletRequest request , String savePath , int maxFileSize , String encoding ) {
		
		boolean rslt = false;
		try {
			
			MultipartParser mp = new MultipartParser(request, maxFileSize);
			mp.setEncoding(encoding);

			File dir =new File(savePath); 
			Part part;
			
			hm = new HashMap<String, String>();
			
			int countPart = 0;
			while ((part = mp.readNextPart()) != null) {
				
				if (part.isFile()){
					
					FilePart filePart = (FilePart) part; 
					filePart.setRenamePolicy(new UnixtimeFileRenamePolicy());           

					realFileName = filePart.getFileName(); 
					
					if ( realFileName != null ){
						
						contentType = filePart.getContentType().toString();

						if( this.checkContentType()){
							
							fileSize = filePart.writeTo(dir);
							uploadedFileName = filePart.getFileName();   //�ߺ�ó���� ���� �̸�
							
							rslt = true;
						}
						else {
							errorMsg.append(" ���ε� ������ ���� ���°�  �ƴմϴ�. \\r\\n\\   - ���� ���� ���� : [" + contentType + "] \\r\\n   - ���� ���� ���� : ["+SLibrary.join( this.checkContentType," | ")+"]");
						}
					}
					else {
						errorMsg.append(" realFileName is null ");
					}
					countPart++;
				}
				else if(part.isParam()){
					ParamPart paramPart = (ParamPart)part;
					hm.put( SLibrary.IfNull(paramPart.getName()), SLibrary.IfNull(paramPart.getStringValue()) );
					
				}
				else{
					errorMsg.append(" part.isFile() is false ");
				}
			}

			if (countPart == 0) {
				errorMsg.append(" parameter count is zero ");
			}
			
		}
		catch(IOException eio) {  rslt = false; errorMsg.append(this.fileSizeErrorHangle( eio.getMessage())); }
		catch(Exception e){
			VbyP.errorLog("UploadMultipart.java upload error : "+e.toString());
			rslt = false; 
		}
		
		return rslt;
	}
	
	private boolean checkContentType() {
		
		boolean rslt = false;
		
		if (checkContentType == null) {
			
			rslt = true;
		}
		else {
			
			int count = this.checkContentType.length;
			for (int i = 0; i < count; i++) {
				if ( !rslt && contentType.equals(this.checkContentType[i]) ) rslt = true;
			}
		}
		
		return rslt;
	}
	
	private String fileSizeErrorHangle(String str) {
		
		StringBuffer buf = new StringBuffer();
		if (SLibrary.searchPattern(str, "Posted content length of.*")) {
			String pre = "Posted content length of";
			String end = "exceeds limit of";
			
			String temp = str;
			temp = SLibrary.replaceAll(temp, pre, "");
			temp = SLibrary.replaceAll(temp, end, "/");
			buf.append("���ε�뷮�� �ʰ� �Ͽ����ϴ�. ( ��������ũ��/�������ũ�� )\\r\\n   - ");
			buf.append(temp+" byte");
		}else {
			
			buf.append(str);
		}
		
		return buf.toString();
	}

	
	
	
}
