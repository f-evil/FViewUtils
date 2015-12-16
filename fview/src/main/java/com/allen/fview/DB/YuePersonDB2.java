package com.allen.fview.DB;

import android.content.Context;

import com.allen.fview.Bean.DateOwner;
import com.allen.fview.Utils.StringUtil;
import com.allen.fview.Utils.XLog;

public class YuePersonDB2 {
	private Context context;
	private DBHelper helper;
	public DBOperator dbOperator;

	public YuePersonDB2(final Context context) {
		this.dbOperator = new DBOperator(context);
		this.context = context;
		this.helper = new DBHelper(context);
	}


	public boolean isExistrefOwnerId(String refOwnerId) {

		if (StringUtil.isEmpty(refOwnerId)) {
			XLog.e("FView BD", "参数不能为空！");
			return false;
		}

		String sql = "select * from tblYuePerson where refOwnerId = ?";

		int count = this.dbOperator.getCount(sql, new String[]{refOwnerId});

		if (count > 0)
			return true;

		return false;
	}

	public void add(String refOwnerId, DateOwner person) {

		String sql;
		String[] params;
		if (!isExistrefOwnerId(refOwnerId)) {
			sql = "insert into tblYuePerson (refOwnerId,aliasName,companyName,imgUrl,regName) values(?,?,?,?,?)";
			params = new String[]{refOwnerId, person.getAliasName(), person.getCompanyName(), person.getImgUrl(),
					person.getRegName()};
		} else {
			sql = "update tblYuePerson set refOwnerId=?,aliasName=?,companyName=?,imgUrl=?,regName=? where " +
					"refOwnerId= ?";
			params = new String[]{refOwnerId, person.getAliasName(), person.getCompanyName(), person.getImgUrl(),
					person.getRegName(), refOwnerId};
		}

		this.dbOperator.operator(sql, params);

	}

}
