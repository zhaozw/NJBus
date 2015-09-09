package com.renyu.nj_tran.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by renyu on 15/9/6.
 */
public class LocalSqliteHelper extends SQLiteOpenHelper {

    final static String DBNAME="njbus";
    final static int DBVERSION=1;

    static LocalSqliteHelper helper=null;

    final static String ID="_id";
    final static String TABLE_NAME_FAV="table_fav";
    final static String LINE_ID="line_id";
    final static String LINE_NAME="line_name";
    final static String UPDOWN_TYPE="updown_type";

    public LocalSqliteHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    public synchronized static LocalSqliteHelper getInstance(Context context) {
        if (helper==null) {
            synchronized (LocalSqliteHelper.class) {
                helper=new LocalSqliteHelper(context.getApplicationContext());
            }
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table if not exists "+TABLE_NAME_FAV+"("+ID+" integer not null primary key autoincrement, "+LINE_ID+" integer not null, "+UPDOWN_TYPE+" integer not null, "+LINE_NAME+" text not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
