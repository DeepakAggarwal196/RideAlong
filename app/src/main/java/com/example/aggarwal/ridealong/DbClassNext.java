package com.example.aggarwal.ridealong;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aggarwal on 08-07-2016.
 */
public class DbClassNext extends SQLiteOpenHelper {
    static String db="ride_along_1";
    static int ver=1;
    DbClassNext(Context context)
    {
        super(context, db, null, ver);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ride_detail(name varchar,contact varchar,source varchar,destination varchar,dates date,time varchar,car varchar,capacity varchar);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table ride_detail");
        onCreate(db);
    }
    public boolean add(String name,String contact,String source,String destination,String date,String time,String car,String capacity) {
        SQLiteDatabase db = getWritableDatabase();
        try {
//            db.execSQL("insert into ride_detail(name,contact,source,destination,dates,time,car,capacity) values('" + name + "','" + contact + "','" + source + "','" + destination+ "',to_date('" + date + "','DD/MM/YYYY')"+",'"+time+"','"+car+"','"+capacity+"');");
            db.execSQL("insert into ride_detail(name,contact,source,destination,dates,time,car,capacity) values('" + name + "','" + contact + "','" + source + "','" + destination+ "','" + date +"','"+time+"','"+car+"','"+capacity+"');");
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    public boolean checkDatabase(String src, String des, String date)
    {
        boolean flag=false;
        String[] args={src,des,date};
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from ride_detail where source=? and destination=? and dates=?",args);
        flag=cursor.moveToFirst();
        cursor.close();
        return flag;
    }
    public Cursor fetchDatabase(String src, String des, String date)
    {
        boolean flag=false;
        String[] args={src,des,date};
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from ride_detail where source=? and destination=? and dates=?",args);
        flag=cursor.moveToFirst();
        return cursor;
    }
}
