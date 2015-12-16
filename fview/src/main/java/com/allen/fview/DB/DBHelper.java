package com.allen.fview.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.allen.fview.Utils.XLog;

/**
 * BD更新方式
 */
public class DBHelper extends SQLiteOpenHelper {
	public static final String USER_DBNAME = "yilian2.db";
	private static final int DATABASE_VERSION = 3;

	private static final String CREATE_YUE_TIME_LINE = "create table tblYueTimeLine(" +
			"_y_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"categoryNote text," +
			"createdAt text," +
			"desc text," +
			")";

	private static final String CREATE_YUE_PERSON = "create table tblYuePerson(" +
			"_y_id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"refOwnerId text," +
			")";

	public DBHelper(Context context) {
		super(context, USER_DBNAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		XLog.e("Bsn", "DataBase onCreate ...");
		try {
			db.execSQL("CREATE table IF NOT EXISTS tblChatList"
					+ " (_B_id INTEGER PRIMARY KEY AUTOINCREMENT, img TEXT, chatId TEXT,  chatName TEXT, assisToId " +
					"TEXT, chatType TEXT,createTime TEXT,UserId TEXT)");

			db.execSQL("CREATE table IF NOT EXISTS cusTomer"
					+ " (_Cu_id INTEGER PRIMARY KEY AUTOINCREMENT, IsDual TEXT, TagName TEXT,  RefOwnerUserId TEXT, " +
					"CustomerId TEXT, ImgUrl TEXT,RefOwnerUserName TEXT,CompanyName TEXT,companyId TEXT,RefUserId " +
					"TEXT,UserGrade TEXT,AliasName TEXT,TagId TEXT,CustomerName TEXT)");

			db.execSQL("CREATE table IF NOT EXISTS tblChat"
					+ " (_C_id INTEGER PRIMARY KEY AUTOINCREMENT, userId TEXT, nick TEXT, headIcon TEXT)");

			db.execSQL("CREATE table IF NOT EXISTS tblUserImg"
					+ " (_U_id INTEGER PRIMARY KEY AUTOINCREMENT, UserName TEXT, UserImage TEXT, UserId TEXT)");

			db.execSQL("CREATE table IF NOT EXISTS tblSetting"
					+ " (_S_id INTEGER PRIMARY KEY AUTOINCREMENT, _Name TEXT, _Value TEXT)");

			db.execSQL("CREATE table IF NOT EXISTS tblChatMsg"
					+ " (_M_id INTEGER PRIMARY KEY AUTOINCREMENT, chatId TEXT, userToId TEXT,  message TEXT, userId " +
					"TEXT, chatType TEXT,status TEXT,msgType TEXT,msgSize TEXT,msgDate TEXT,msgTitle TEXT," +
					"RefBusinessId TEXT,VedioStaus TEXT,uid TEXT)");

			db.execSQL(CREATE_YUE_TIME_LINE);
			db.execSQL(CREATE_YUE_PERSON);

		} catch (Exception ex) {
			XLog.e("onCreate->", ex.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		XLog.e("Bsn", "DataBase onUpgrade ...version" + newVersion);
		switch (oldVersion) {
			case 2:
				db.execSQL(CREATE_YUE_TIME_LINE);
				db.execSQL(CREATE_YUE_PERSON);
		}

	}
}
