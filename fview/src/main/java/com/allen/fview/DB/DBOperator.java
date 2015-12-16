package com.allen.fview.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.allen.fview.Utils.XLog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用反射的数据库操作类
 * fyj
 */
public class DBOperator {

	private DBHelper helper;
	private SQLiteDatabase db = null;

	public DBOperator(Context context) {
		helper = new DBHelper(context);
	}

	/**
	 * 使用反射，获取执行影响的记录数
	 * 该方法用于执行insert,delete,update操作
	 */
	public int execute(String sql, Object[] params) {

		Class<?> classType = null;
		Method mth = null;

		int res = 0;
		try {
			classType = Class.forName("android.database.sqlite.SQLiteDatabase");
			mth = classType.getDeclaredMethod("executeSql", new Class[]{String.class, Object[].class});
			mth.setAccessible(true);

			db = helper.getWritableDatabase();

			res = (Integer) mth.invoke(db, sql, params);

		} catch (Exception e) {
			XLog.e("FView BD", e.toString());
		}

		return res;
	}

	/**
	 * 该方法用于执行insert,delete,update操作
	 */
	public void operator(String sql, Object[] params) {

		synchronized (DBOperator.class) {
			try {
				db = helper.getWritableDatabase();
				if (params != null)
					db.execSQL(sql, params);
				else
					db.execSQL(sql);
			} catch (Exception e) {
				XLog.e("FView BD", e.toString());
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}

	}

	/**
	 * 批量处理
	 */
	public void batchOperator(String sql, List<Object[]> paramsList) {
		synchronized (DBOperator.class) {

			try {
				db = helper.getWritableDatabase();

				db.beginTransaction();

				for (Object[] params : paramsList) {
					db.execSQL(sql, params);
				}

				db.setTransactionSuccessful();
			} catch (Exception e) {
				XLog.e("FView BD", e.toString());
			} finally {
				if (db != null) {
					db.endTransaction();
					db.close();
				}
			}
		}
	}

	public List<Map> query(String sql, String[] params, String[] columns) {
		List<Map> resList = new ArrayList<Map>();

		synchronized (DBOperator.class) {
			Cursor c = null;

			try {
				db = helper.getReadableDatabase();

				c = db.rawQuery(sql, params);
				Map<String, Object> map = null;

				while (c.moveToNext()) {
					map = new HashMap<String, Object>();

					for (String colName : columns) {
						map.put(colName, c.getString(c.getColumnIndex(colName)));
					}
					resList.add(map);
				}
			} catch (Exception e) {
				XLog.e("FView BD", e.toString());
			} finally {
				if (c != null)
					c.close();

				if (db != null)
					db.close();
			}

			return resList;
		}

	}

	public int getCount(String sql, String[] params) {
		int count = 0;
		synchronized (DBOperator.class) {
			Cursor c = null;
			try {
				db = helper.getReadableDatabase();
				c = db.rawQuery(sql, params);

				count = c.getCount();

			} catch (Exception e) {
				XLog.e("FView BD", e.toString());
			} finally {
				if (c != null)
					c.close();

				if (db != null)
					db.close();
			}

			return count;
		}
	}

}
