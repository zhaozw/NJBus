package com.renyu.nj_tran.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.amap.api.location.core.CoordinateConvert;
import com.amap.api.location.core.GeoPoint;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.renyu.nj_tran.model.LineModel;
import com.renyu.nj_tran.model.StationModel;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by renyu on 15/8/25.
 */
public class DBUtils {

    public static void test(Context context) {

        SQLiteDatabase.loadLibs(context);

        File file=new File("/data/data/"+context.getPackageName()+"/"+ParamUtils.dbName);
        if (file.exists()) {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getPath(), "1qaz2wsxnjbc", null);
            //Cursor cs=db.rawQuery("select * from sqlite_master where type=\"table\";", null);
//            Cursor cs=db.rawQuery("pragma table_info ('ibus_station') ", null);
//            Cursor cs=db.rawQuery("pragma table_info ('ibus_line') ", null);
//            Cursor cs=db.rawQuery("select * from ibus_station", null);
//            Cursor cs=db.rawQuery("pragma table_info ('ibus_line_stations') ", null);
//            Cursor cs=db.rawQuery("select * from ibus_line_stations", null);
//            Cursor cs=db.rawQuery("select * from ibus_line", null);
//            Cursor cs=db.rawQuery("pragma table_info ('ibus_line_updown') ", null);
            Cursor cs=db.rawQuery("select * from ibus_line_updown", null);
            cs.moveToFirst();
            for (int i=0;i<cs.getCount();i++) {
                cs.moveToPosition(i);
//                System.out.print(cs.getString(cs.getColumnIndex("st_id")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("weiba_id")) +" ");
//                System.out.print(cs.getString(cs.getColumnIndex("st_name"))+" ");
//                System.out.print(cs.getString(cs.getColumnIndex("st_lat"))+" ");
//                System.out.print(cs.getString(cs.getColumnIndex("st_lon"))+" ");
//                System.out.print(cs.getString(cs.getColumnIndex("st_real_lat"))+" ");
//                System.out.print(cs.getString(cs.getColumnIndex("st_real_lon"))+" ");
//                System.out.print(cs.getString(cs.getColumnIndex("st_status"))+" ");
//                System.out.print(cs.getString(cs.getColumnIndex("st_side"))+" ");
//                System.out.print(cs.getString(cs.getColumnIndex("is_true"))+" ");
//                System.out.print(cs.getString(cs.getColumnIndex("update_time"))+" ");

//                System.out.print(cs.getString(cs.getColumnIndex("line_id")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("weiba_id")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_code")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_name")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_interval")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_status")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_ticket")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("organ_name")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("ltd")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("update_time")) + " ");


//                System.out.print(cs.getString(cs.getColumnIndex("line_station_id")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_id"))+ " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_code"))+ " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_interval"))+ " ");
//                System.out.print(cs.getString(cs.getColumnIndex("updown_type"))+ " ");
//                System.out.print(cs.getString(cs.getColumnIndex("st_id"))+ " ");
//                System.out.print(cs.getString(cs.getColumnIndex("st_name"))+ " ");
//                System.out.print(cs.getString(cs.getColumnIndex("st_stop_level"))+ " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_st_status"))+ " ");
//                System.out.print(cs.getString(cs.getColumnIndex("update_time"))+ " ");
//                System.out.print(cs.getString(cs.getColumnIndex("is_true"))+ " ");

                System.out.print(cs.getString(cs.getColumnIndex("line_id")) + " ");
                System.out.print(cs.getString(cs.getColumnIndex("line_code"))+ " ");
                System.out.print(cs.getString(cs.getColumnIndex("line_interval"))+ " ");
                System.out.print(cs.getString(cs.getColumnIndex("updown_type"))+ " ");
                System.out.print(cs.getString(cs.getColumnIndex("st_start_id"))+ " ");
                System.out.print(cs.getString(cs.getColumnIndex("st_end_id"))+ " ");
                System.out.print(cs.getString(cs.getColumnIndex("bus_start"))+ " ");
                System.out.print(cs.getString(cs.getColumnIndex("bus_end"))+ " ");
                System.out.print(cs.getString(cs.getColumnIndex("updown_status"))+ " ");
                System.out.print(cs.getString(cs.getColumnIndex("update_time"))+ " ");

//                System.out.println(cs.getString(1));
                System.out.println("\n");

            }
            cs.close();
            db.close();
        }
    }

    /**
     * 获取数据库实例对象
     * @param context
     * @return
     */
    private static SQLiteDatabase getDBInstance(Context context) {
        File file=new File("/data/data/"+context.getPackageName()+"/"+ParamUtils.dbName);
        if (file.exists()) {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file.getPath(), "1qaz2wsxnjbc", null);
            return db;
        }
        else {
            return null;
        }
    }

    /**
     * 当前附近车站信息
     * @param context
     * @param currentLocation
     * @return
     */
    public static ArrayList<StationModel> getNearByStation(Context context, LatLng currentLocation) {
        SQLiteDatabase db = getDBInstance(context);
        ArrayList<StationModel> stationModels=new ArrayList<>();
        db.beginTransaction();
        try {
            Cursor cs=db.rawQuery("select * from ibus_station", null);
            cs.moveToFirst();
            for (int i=0;i<cs.getCount();i++) {
                cs.moveToPosition(i);
                GeoPoint geoPoint=CoordinateConvert.fromGpsToAMap(Double.parseDouble(cs.getString(cs.getColumnIndex("st_real_lat"))), Double.parseDouble(cs.getString(cs.getColumnIndex("st_real_lon"))));
                float distance=AMapUtils.calculateLineDistance(currentLocation, new LatLng((double) geoPoint.getLatitudeE6()/1E6, (double) geoPoint.getLongitudeE6()/1E6));
                if (Math.abs(distance)<1000) {
                    StationModel model=new StationModel();
                    model.setSt_id(cs.getInt(cs.getColumnIndex("st_id")));
                    model.setSt_name(cs.getString(cs.getColumnIndex("st_name")));
                    model.setSt_real_lat(cs.getDouble(cs.getColumnIndex("st_real_lat")));
                    model.setSt_real_lon(cs.getDouble(cs.getColumnIndex("st_real_lon")));
                    stationModels.add(model);

//                    System.out.print(cs.getString(cs.getColumnIndex("st_id")) + " ");
//                    System.out.print(cs.getString(cs.getColumnIndex("weiba_id")) +" ");
//                    System.out.print(cs.getString(cs.getColumnIndex("st_name"))+" ");
//                    System.out.print(cs.getString(cs.getColumnIndex("st_lat"))+" ");
//                    System.out.print(cs.getString(cs.getColumnIndex("st_lon"))+" ");
//                    System.out.print(cs.getString(cs.getColumnIndex("st_real_lat"))+" ");
//                    System.out.print(cs.getString(cs.getColumnIndex("st_real_lon"))+" ");
//                    System.out.print(cs.getString(cs.getColumnIndex("st_status"))+" ");
//                    System.out.print(cs.getString(cs.getColumnIndex("st_side"))+" ");
//                    System.out.print(cs.getString(cs.getColumnIndex("is_true"))+" ");
//                    System.out.print(cs.getString(cs.getColumnIndex("update_time"))+" ");
//                    System.out.println("\n");
                }
            }
            cs.close();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        for (int i=0;i<stationModels.size();i++) {
            ArrayList<Integer> lineId=getLineId(db, stationModels.get(i).getSt_id());
            stationModels.get(i).setLineModels(getLineInfo(db, lineId));
        }
        db.close();
        return stationModels;
    }

    /**
     * 根据站点id查询线路id
     * @param db
     * @param st_id
     * @return
     */
    public static ArrayList<Integer> getLineId(SQLiteDatabase db, int st_id) {
        ArrayList<Integer> lineIds=new ArrayList<>();
        Cursor cs=db.rawQuery("select * from ibus_line_stations where st_id="+st_id, null);
        cs.moveToFirst();
        for (int i=0;i<cs.getCount();i++) {
            cs.moveToPosition(i);
            lineIds.add(cs.getInt(cs.getColumnIndex("line_id")));

//            System.out.print(cs.getString(cs.getColumnIndex("line_station_id")) + " ");
//            System.out.print(cs.getString(cs.getColumnIndex("line_id"))+ " ");
//            System.out.print(cs.getString(cs.getColumnIndex("line_code"))+ " ");
//            System.out.print(cs.getString(cs.getColumnIndex("line_interval"))+ " ");
//            System.out.print(cs.getString(cs.getColumnIndex("updown_type"))+ " ");
//            System.out.print(cs.getString(cs.getColumnIndex("st_id"))+ " ");
//            System.out.print(cs.getString(cs.getColumnIndex("st_name"))+ " ");
//            System.out.print(cs.getString(cs.getColumnIndex("st_stop_level"))+ " ");
//            System.out.print(cs.getString(cs.getColumnIndex("line_st_status"))+ " ");
//            System.out.print(cs.getString(cs.getColumnIndex("update_time"))+ " ");
//            System.out.print(cs.getString(cs.getColumnIndex("is_true")) + " ");
//            System.out.println("\n");
        }
        cs.close();
        return lineIds;
    }

    /**
     * 获取当前线路所有站点信息
     * @param line_id
     * @param context
     * @return
     */
    public static ArrayList<StationModel> getLineInfo(int line_id, int updown_type, Context context) {
        ArrayList<StationModel> models=new ArrayList<>();
        SQLiteDatabase db = getDBInstance(context);
        Cursor cs_line=db.rawQuery("select * from ibus_line_stations where line_id="+line_id+" and updown_type="+updown_type+" order by st_stop_level asc", null);
        cs_line.moveToFirst();
        ArrayList<Integer> temps=new ArrayList<>();
        for (int i=0;i<cs_line.getCount();i++) {
            cs_line.moveToPosition(i);
            temps.add(cs_line.getInt(cs_line.getColumnIndex("st_id")));
        }
        cs_line.close();
        for (int i=0;i < temps.size();i++) {
            Cursor cs=db.rawQuery("select * from ibus_station where st_id="+temps.get(i), null);
            cs.moveToFirst();
            if (cs.getCount()>0) {
                cs.moveToPosition(0);
                StationModel model=new StationModel();
                model.setSt_name(cs.getString(cs.getColumnIndex("st_name")));
                model.setSt_id(cs.getInt(cs.getColumnIndex("st_id")));
                model.setSt_real_lon(cs.getDouble(cs.getColumnIndex("st_real_lon")));
                model.setSt_real_lat(cs.getDouble(cs.getColumnIndex("st_real_lat")));
                model.setSt_pos(i);
                models.add(model);
            }
            cs.close();
        }
        db.close();
        return models;
    }

    /**
     * 查询具体线路信息
     * @param db
     * @param line_ids
     * @return
     */
    public static ArrayList<LineModel> getLineInfo(SQLiteDatabase db, ArrayList<Integer> line_ids) {
        ArrayList<LineModel> models=new ArrayList<>();
        for (int j=0;j<line_ids.size();j++) {
            Cursor cs=db.rawQuery("select * from ibus_line where line_id="+line_ids.get(j), null);
            cs.moveToFirst();
            for (int i=0;i<cs.getCount();i++) {
                cs.moveToPosition(i);
                LineModel model=new LineModel();
                model.setLine_id(cs.getInt(cs.getColumnIndex("line_id")));
                model.setLine_code(cs.getInt(cs.getColumnIndex("line_code")));
                model.setLine_name(cs.getString(cs.getColumnIndex("line_name")));
                models.add(model);

//                System.out.print(cs.getString(cs.getColumnIndex("line_id")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("weiba_id")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_code")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_name")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_interval")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_status")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("line_ticket")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("organ_name")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("ltd")) + " ");
//                System.out.print(cs.getString(cs.getColumnIndex("update_time")) + " ");
//                System.out.println("\n");
            }
            cs.close();
        }
        return models;
    }

    /**
     * 获取当前线路正反方向
     * @param line_name
     * @param line_id
     * @param line_code
     * @param context
     * @return
     */
    public static ArrayList<LineModel> getLineModelDetail(String line_name, int line_id, int line_code, Context context) {
        ArrayList<LineModel> models=new ArrayList<>();
        SQLiteDatabase db = getDBInstance(context);
        Cursor cs=null;
        if (line_code==0) {
            cs=db.rawQuery("select * from ibus_line_updown where line_id="+line_id, null);
        }
        else {
            cs=db.rawQuery("select * from ibus_line_updown where line_id="+line_id+" and line_code="+line_code, null);
        }
        cs.moveToFirst();
        for (int i=0;i<cs.getCount();i++) {
            cs.moveToPosition(i);
            LineModel model=new LineModel();
            model.setLine_name(line_name);
            model.setLine_id(line_id);
            model.setLine_code(line_code);
            model.setBus_end(cs.getString(cs.getColumnIndex("bus_end")));
            model.setBus_start(cs.getString(cs.getColumnIndex("bus_start")));
            model.setSt_end_id(cs.getInt(cs.getColumnIndex("st_end_id")));
            model.setSt_start_id(cs.getInt(cs.getColumnIndex("st_start_id")));
            model.setUpdown_type(cs.getInt(cs.getColumnIndex("updown_type")));
            models.add(model);
        }
        cs.close();
        db.close();
        return models;
    }

    public synchronized static String getStationName(int st_id, Context context) {
        String stationName="";
        SQLiteDatabase db = getDBInstance(context);
        if (db==null) {
            return null;
        }
        Cursor cs=db.rawQuery("select * from ibus_station where st_id="+st_id, null);
        cs.moveToFirst();
        if (cs.getCount()>0) {
            cs.moveToPosition(0);
            stationName=cs.getString(cs.getColumnIndex("st_name"));
        }
        cs.close();
        db.close();
        return stationName;
    }

    /**
     * 判断是否添加到收藏
     * @param context
     * @param line_id
     * @return
     */
    public static synchronized boolean isFav(Context context, int line_id, int updown_type) {
        android.database.sqlite.SQLiteDatabase db=LocalSqliteHelper.getInstance(context).getReadableDatabase();
        Cursor cs=db.query(LocalSqliteHelper.TABLE_NAME_FAV, null, LocalSqliteHelper.LINE_ID + "=? and " + LocalSqliteHelper.UPDOWN_TYPE + "=?", new String[]{"" + line_id, "" + updown_type}, null, null, null);
        cs.moveToFirst();
        int count=cs.getCount();
        cs.close();
        db.close();
        if (count>0) {
            return true;
        }
        return false;
    }

    public static synchronized void addFav(Context context, int line_id, int updown_type, String line_name) {
        android.database.sqlite.SQLiteDatabase db=LocalSqliteHelper.getInstance(context).getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(LocalSqliteHelper.LINE_ID, line_id);
        cv.put(LocalSqliteHelper.UPDOWN_TYPE, updown_type);
        cv.put(LocalSqliteHelper.LINE_NAME, line_name);
        db.insert(LocalSqliteHelper.TABLE_NAME_FAV, null, cv);
        db.close();
    }

    public static synchronized void removeFav(Context context, int line_id, int updown_type) {
        android.database.sqlite.SQLiteDatabase db=LocalSqliteHelper.getInstance(context).getReadableDatabase();
        db.delete(LocalSqliteHelper.TABLE_NAME_FAV, LocalSqliteHelper.LINE_ID + "=? and " + LocalSqliteHelper.UPDOWN_TYPE + "=?", new String[]{"" + line_id, "" + updown_type});
        db.close();
    }

    public static synchronized ArrayList<LineModel> getFav(Context context) {
        ArrayList<LineModel> models=new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db=LocalSqliteHelper.getInstance(context).getWritableDatabase();
        Cursor cs=db.query(LocalSqliteHelper.TABLE_NAME_FAV, null, null, null, null, null, LocalSqliteHelper.ID+" desc");
        cs.moveToFirst();
        for (int i=0;i<cs.getCount();i++) {
            cs.moveToPosition(i);
            LineModel model=new LineModel();
            model.setLine_id(cs.getInt(cs.getColumnIndex(LocalSqliteHelper.LINE_ID)));
            model.setUpdown_type(cs.getInt(cs.getColumnIndex(LocalSqliteHelper.UPDOWN_TYPE)));
            model.setLine_name(cs.getString(cs.getColumnIndex(LocalSqliteHelper.LINE_NAME)));
            models.add(model);
        }
        cs.close();
        db.close();
        return models;
    }

    /**
     * 搜搜结果
     * @param context
     * @param search
     * @return
     */
    public static synchronized ArrayList<LineModel> getSearchResult(Context context, String search) {
        ArrayList<LineModel> models=new ArrayList<LineModel>();
        SQLiteDatabase db = getDBInstance(context);
        String sql="select * from ibus_line where line_name like '%"+search+"%'";
        Cursor cs=db.rawQuery(sql, null);
        cs.moveToFirst();
        for (int i=0;i<cs.getCount();i++) {
            cs.moveToPosition(i);
            LineModel model=new LineModel();
            model.setLine_id(cs.getInt(cs.getColumnIndex("line_id")));
            model.setLine_code(cs.getInt(cs.getColumnIndex("line_code")));
            model.setLine_name(cs.getString(cs.getColumnIndex("line_name")));
            models.add(model);
        }
        cs.close();
        db.close();
        return models;
    }
}
