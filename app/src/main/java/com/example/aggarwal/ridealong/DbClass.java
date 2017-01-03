package com.example.aggarwal.ridealong;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Aggarwal on 23-06-2016.
 */
public class DbClass extends SQLiteOpenHelper {
    static String db="ride_along";
    static int ver=3;
    DbClass(Context context)
    {
        super(context, db, null, ver);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users_detail(name varchar,password varchar,emailid varchar unique,contact varchar);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table users_detail");
        onCreate(db);
    }
    public boolean fetchDatabase(String emailid,String pass)
    {
        boolean flag=false;
        String[] args={emailid,pass};
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from users_detail where emailid=? and password=?",args);
        flag=cursor.moveToFirst();
        cursor.close();
        return flag;
    }
    public String fetchName(String emailid)
    {
        String[] args={emailid};
        String name=null;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from users_detail where emailid=?",args);
        if(cursor!=null) {
            cursor.moveToFirst();
            do {
                name = cursor.getString(cursor.getColumnIndex("name"));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return name;
    }
    public DataClass fetchNameByEmail(String emailid)
    {
        String[] args={emailid};
        DataClass obj;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from users_detail where emailid=?",args);
        cursor.moveToFirst();
        do{
            obj=new DataClass(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("contact")));
        }while(cursor.moveToNext());
        cursor.close();
        return obj;
    }
    public boolean add(String name,String email,String password,String number) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("insert into users_detail(name,emailid,password,contact) values('" + name + "','" + email + "','" + password + "','" + number + "');");
        }
        catch (Exception e)
        {
            return false;
        }
        return true;

    }
    public String fetchContact(String emailid)
    {
        String[] args={emailid};
        String name=null;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from users_detail where emailid=?",args);
        cursor.moveToFirst();
        name=cursor.getString(cursor.getColumnIndex("contact"));
        cursor.close();
        return name;
    }
}



