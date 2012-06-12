/**
 *  Class Name, Ŭ���� �̸�
 *  Class Description, Ŭ���� ����
 *  Version Information, ���� ����
 *  Make date, �ۼ���
 *  Author, �ۼ���
 *  Modify lists, ��������
 *  Copyright, ���۱� ����
 */
package com.common.db;

import java.util.Vector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import com.common.VbyP;

/**
 * mathod to change the owner name
 * 
 * @param filename
 *            �����ϰ��� �ϴ� ���ο� Owner���� parameter�� �Ѵ�.
 * @return return�Ǵ� ���� �����ϴ�.
 * @see #ConnectionPool
 * @see #ExecuteQueryManager
 */
public class ExecuteQueryManager {


	/**
	 * update insert Query ó�� ��ü
	 * 
	 * @param sql -
	 *            Query
	 * @return true : ����
	 */
	public boolean ExecuteUpdate(String sql) {
		boolean bCheck = false;

		Connection conn = VbyP.getDB();

		try {

			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			bCheck = true;
			stmt.close();
		} catch (Exception e) {
			
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>2" + sql);
			bCheck = false;
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ es.toString() + "=>2" + sql);
				bCheck = false;
			}			
		}

		return bCheck;
	}

	/**
	 * �ް� ���� connection�� ���� update insert Query ó��
	 * 
	 * @param conn -
	 *            Connection
	 * @param sql -
	 *            query
	 * @return true : ����
	 */
	public boolean ExecuteUpdateConn(Connection conn, String sql) {
		boolean bCheck = false;
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			try {
				bCheck = true;
				stmt.close();
			} catch (Exception e) {
				VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
			}
		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		return bCheck;
	}

	/**
	 * �ŰԺ��� sql �� ���� nColumnCount �� ��ŭ�� ���ڿ� �迭�� Vector�� ��ȯ
	 * 
	 * @param sql -
	 *            Query
	 * @param nColumncount -
	 *            �˻� Į�� ��
	 * @return Vector -> String[nColumncount]
	 */
	public Vector<String[]> ExecuteQuery(String sql, int nColumnCount) {
		Vector<String[]> vtList = new Vector<String[]>();
		Connection conn = VbyP.getDB();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			String[] strTemp = null; // �˻� ���� �ӽ� �����
			int i;
			int j;

			while (rs.next()) {
				strTemp = new String[nColumnCount];

				for (i = 0; i < nColumnCount; i++) {
					j = i + 1;
					strTemp[i] = rs.getString(j);
					if (strTemp[i] == null || strTemp[i].equals("null")) strTemp[i] = "";
				}

				vtList.add(strTemp);
			}
			
			rs.close();
			stmt.close();
		} catch (Exception e) {
			
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ es.toString() + "=>2" + sql);
			}
		}

		return vtList;
	}
	
	

	/**
	 * �ŰԺ��� sql �� ���� nColumnCount �� ��ŭ�� ���ڿ� �迭�� Vector�� ��ȯ
	 * 
	 * @param sql -
	 *            Query
	 * @param nColumncount -
	 *            �˻� Į�� ��
	 * @param conn -
	 *            DB Connection
	 * @return Vector -> String[nColumncount]
	 */
	public Vector<String[]> ExecuteQuery(String sql, int nColumnCount, Connection conn) {
		
		Vector<String[]> vtList = new Vector<String[]>();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			String[] strTemp = null; // �˻� ���� �ӽ� �����
			int i;
			int j;

			while (rs.next()) {
				strTemp = new String[nColumnCount];

				for (i = 0; i < nColumnCount; i++) {
					j = i + 1;
					strTemp[i] = rs.getString(j);
					if (strTemp[i] == null || strTemp[i].equals("null")) strTemp[i] = "";
				}

				vtList.add(strTemp);
			}
			
			rs.close();
			stmt.close();
			
		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		return vtList;
	}

	/**
	 * �ŰԺ��� sql �� ���� nColumnCount �� ��ŭ�� ���ڿ� �迭�� ��ȯ
	 * 
	 * @param sql -
	 *            Query
	 * @param nColumncount -
	 *            �˻� Į�� ��
	 * @return String[nColumncount]
	 */
	public String[] ExecuteQueryCols(String sql, int nColumnCount) {
		
		String[] strTemp = new String[nColumnCount]; // �˻� ���� �����
		Connection conn = VbyP.getDB();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			
			rs = stmt.executeQuery(sql);

			int i;
			int j;

			while (rs.next()) {
				for (i = 0; i < nColumnCount; i++) {
					j = i + 1;
					strTemp[i] = rs.getString(j);
					if (strTemp[i] == null || strTemp[i].equals("null")) strTemp[i] = "";
				}
			}
			
			rs.close();
			stmt.close();

		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ es.toString() + "=>2" + sql);
			}
		}

		return strTemp;
	}

	/**
	 * sql ����� ���ڿ� �迭�� ��ȯ
	 * 
	 * @param sql -
	 *            Query
	 * @return String[]
	 */
	public String[] ExecuteQuery(String sql) {
		
		String[] strTemp = null; // �˻� ���� �ӽ� �����
		Connection conn = VbyP.getDB();
		try {

			Statement stmt = conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			// ù��° �Ż������� ������, �ι�°�� Ŀ���̵�����, ����°�� Ŀ���� ����Ű�� ���� �������ɿ���

			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			int i = 0;
			rs.last();
			strTemp = new String[rs.getRow()];
			rs.beforeFirst();

			while (rs.next()) {
				strTemp[i] = rs.getString(1);
				if (strTemp[i] == null || strTemp[i].equals("null")) strTemp[i] = "";
				i++;
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "22222=>" + sql);
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ es.toString() + "=>2" + sql);
			}
		}

		return strTemp;
	}

	/**
	 * ���� ��� ���� ��ȯ
	 * 
	 * @param sql -
	 *            Query
	 * @return int
	 */
	public int ExecuteQueryNum(String sql) {
		
		int num = 0;
		Connection conn = VbyP.getDB();
		try {

			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				num = rs.getInt(1);
			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ es.toString() + "=>2" + sql);
			}
		}
		return num;
	}

	/**
	 * ���� ��� ���� ��ȯ
	 * 
	 * @param sql -
	 *            Query
	 * @return int
	 */
	public int ExecuteQueryNum(String sql, Connection conn) {
		
		int num = 0;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				num = rs.getInt(1);
			}

			rs.close();
			stmt.close();
			
		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		
		return num;
	}

	/**
	 * ���� ��� ���ڿ� ��ȯ
	 * 
	 * @param sql -
	 *            Query
	 * @return String
	 */
	public String ExecuteQueryString(String sql) {
		
		String str = null;
		Connection conn = VbyP.getDB();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				
				str = rs.getString(1);
			}

			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException es) {
				VbyP.errorLog("ExecuteQueryManager"+ "conn.close() Error !! "
						+ es.toString());
			}

		}
		if (str == null || str.equals("null")) str = "";
		return str;
	}

	/**
	 * ���� ��� ���ڿ� ��ȯ
	 * 
	 * @param sql -
	 *            Query
	 * @return String
	 */
	public String ExecuteQueryString(String sql, Connection conn) {
		
		String str = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				str = rs.getString(1);
			}
			
			rs.close();
			stmt.close();

		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		if (str == null || str.equals("") || str.equals("null")) str = "";
		return str;
	}

	/**
	 * ���� ��� ���ڿ� ��ȯ
	 * 
	 * @param sql -
	 *            Query
	 * @return String
	 */
	public String ExecuteQueryStringConn(String sql, Connection conn) {
		
		String str = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				str = rs.getString(1);
			}
			rs.close();
			stmt.close();

		} catch (Exception e) {
			VbyP.errorLog("ExecuteQueryManager"+ e.toString() + "=>" + sql);
		}
		if (str == null || str.equals("null")) str = "";
		return str;
	}
};
