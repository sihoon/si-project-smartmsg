package com.m;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

import com.common.VbyP;
import com.common.db.SessionFactory;
import com.common.db.SessionManager;
import com.common.util.SLibrary;
import com.m.admin.vo.MemberVO;
import com.m.billing.Billing;
import com.m.billing.BillingVO;
import com.m.common.BooleanAndDescriptionVO;
import com.m.common.PointManager;
import com.m.point.Point;
import com.m.point.PointDAO;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;

public class MasterDS {
	
	private final String SESSION_ADMIN = "admin_id";
	SqlSessionFactory sqlMapper = SessionFactory.getSqlSession();
	String ns = "com.query.MapMaster.";
	
	public BooleanAndDescriptionVO login(String user_id, String password) {

		BooleanAndDescriptionVO rvo = new BooleanAndDescriptionVO();

		try {
			rvo.setbResult(false);
			if ( SLibrary.isNull(user_id) )	rvo.setstrDescription("아이디를 입력하세요.");
			else if ( SLibrary.isNull(password) ) rvo.setstrDescription("비밀번호를 입력하세요.");
			else {
				
				if (VbyP.getValue("admin.id").equals(user_id) && VbyP.getValue("admin.pw").equals(password)) {
					FlexSession session =  FlexContext.getFlexSession();
					session.setAttribute(SESSION_ADMIN, user_id);
					VbyP.accessLog(user_id+" Admin Login");
					rvo.setbResult(true);
				} else {
					rvo.setstrDescription("않되요.");
				}
				
			}
		}catch (Exception e) {VbyP.errorLog(e.toString());}
		
		return rvo;
	}
	
	public List<MemberVO> getMember_pagedFiltered(String user_id, String hp, int startIndex, int numItems) {
		
		MemberVO mvo = new MemberVO();
		if (!SLibrary.isNull(user_id)) mvo.setUser_id("%"+user_id+"%");
		if (!SLibrary.isNull(hp)) mvo.setHp("%"+hp+"%");
		
		mvo.setStart(startIndex);
		mvo.setEnd(numItems);
		
		SessionManager sm = new SessionManager(sqlMapper.openSession(true));
		return setRowNum((List)sm.selectList(ns + "select_member_list_page", mvo), startIndex);

	}
	
	public Integer getMember_countFiltered(String user_id, String hp) {
		
		MemberVO mvo = new MemberVO();
		if (!SLibrary.isNull(user_id)) mvo.setUser_id("%"+user_id+"%");
		if (!SLibrary.isNull(hp)) mvo.setHp("%"+hp+"%");
		
		SessionManager sm = new SessionManager(sqlMapper.openSession(true));
		return (Integer)sm.selectOne(ns + "select_member_list_page_count", mvo);

	}
	
	public BooleanAndDescriptionVO setMember(MemberVO mvo) {
		
		BooleanAndDescriptionVO bvo = new BooleanAndDescriptionVO();
		SessionManager sm = new SessionManager(sqlMapper.openSession(true));
		int rslt = sm.update(ns + "update_member", mvo);
		
		if (rslt > 0) bvo.setbResult(true);
		else bvo.setbResult(false);
		
		return bvo;

	}
	
	
	public BooleanAndDescriptionVO setCharge(String user_id, int amount, int point) {
		
		VbyP.accessLog("setCharge : user_id"+user_id+" amount="+amount+" point="+point);
		
		Billing billing = Billing.getInstance();
		BooleanAndDescriptionVO bavo = new BooleanAndDescriptionVO();
		
		MemberVO mvo = getMember(user_id); 
		
		BillingVO bvo = new BillingVO();
		bvo.setUser_id(user_id);
		bvo.setAdmin_id("SI");
		bvo.setAmount( amount );
		bvo.setMemo("");
		bvo.setMethod("Admin");
		bvo.setOrder_no("");
		bvo.setRemain_point( mvo.getPoint() + point );
		bvo.setTimeWrite(SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
		bvo.setUnit_cost(Integer.toString(billing.getCost(amount)));
		
		SessionManager sm = new SessionManager(sqlMapper.openSession(true));
		int insertCnt = sm.insert(ns +"insert_billing", bvo);
		
		if (insertCnt > 0) {
			Point pdao = new PointDAO();
			int rslt = pdao.setPoint(mvo, 3, point * PointManager.DEFULT_POINT);
			if (rslt != 1) {
				bavo.setbResult(false);
				bavo.setstrDescription("pointdao fail");
			}
				
		}
		
		return bavo;

	}
	
	public BooleanAndDescriptionVO setChargeAuto(String user_id, int amount) {
		
		VbyP.accessLog("setCharge : user_id"+user_id+" amount="+amount);
		
		Billing billing = Billing.getInstance();
		BooleanAndDescriptionVO bavo = new BooleanAndDescriptionVO();
		
		MemberVO mvo = getMember(user_id);
		
		int point = billing.getPoint(amount);
		
		BillingVO bvo = new BillingVO();
		bvo.setUser_id(user_id);
		bvo.setAdmin_id("SI");
		bvo.setAmount( amount );
		bvo.setMemo("");
		bvo.setMethod("Admin");
		bvo.setOrder_no("");
		bvo.setPoint(point);
		bvo.setRemain_point( mvo.getPoint() + point );
		bvo.setTimeWrite(SLibrary.getDateTimeString("yyyy-MM-dd HH:mm:ss"));
		bvo.setUnit_cost(Integer.toString(billing.getCost(amount)));
		
		SessionManager sm = new SessionManager(sqlMapper.openSession(true));
		int insertCnt = sm.insert(ns +"insert_billing", bvo);
		
		if (insertCnt > 0) {
			Point pdao = new PointDAO();
			int rslt = pdao.setPoint(mvo, 3, point * PointManager.DEFULT_POINT);
			if (rslt != 1) {
				bavo.setbResult(false);
				bavo.setstrDescription("pointdao fail");
			} else {
				bavo.setbResult(true);
			}
				
		}
		
		return bavo;

	}
	
	List<MemberVO> setRowNum(List<MemberVO> lvo, int start) {
		if (lvo != null) {
			int cnt = lvo.size();
			for (int i = 0; i < cnt; i++) {
				lvo.get(i).setRownum(start);
				start++;
			}
		}
		return lvo;
	}
	
	MemberVO getMember(String user_id) {
		
		SessionManager sm = new SessionManager(sqlMapper.openSession(true));
		// get MemberVO
		MemberVO param = new MemberVO();
		param.setUser_id(user_id);
		return (MemberVO)sm.selectOne(ns + "select_member", param);
	}

}