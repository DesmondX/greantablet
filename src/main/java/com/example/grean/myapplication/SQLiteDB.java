package com.example.grean.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/9/26.
 */
public class SQLiteDB extends SQLiteOpenHelper {

    public static final String CREATE_DB =  "create table data ( "+
            "id integer primary key autoincrement, "+
            "name text)";

    private Context mContext;


    public SQLiteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DB);
        Toast.makeText(mContext, "the table has been created", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
