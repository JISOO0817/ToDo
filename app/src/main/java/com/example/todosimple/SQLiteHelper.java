package com.example.todosimple;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLiteHelper  {

    private static final String dbName = "simpleToDo";
    private static final String tableName = "ToDo";
    private static final int dbVersion = 1;


    private OpenHelper opener;

    private SQLiteDatabase db;
    private Context context;


    public SQLiteHelper(Context context) {

        this.context = context;
        this.opener = new OpenHelper(context,dbName,null,dbVersion);
        db = opener.getWritableDatabase();
    }

    private class OpenHelper extends SQLiteOpenHelper{


        public OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String create = "CREATE TABLE " + tableName + "(" +
                    "seq integer PRIMARY KEY AUTOINCREMENT, " +
                    "maintext text, " +
                    "subtext text, "  +
                    "isdone integer)";
            sqLiteDatabase.execSQL(create);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
            onCreate(sqLiteDatabase);
        }
    }



    //데이터 추가

    public void insertMemo(Memo memo) {

        // "" 와 '" "' 의 차이는 문자열이냐 아니냐의 차이

        String sql = "INSERT INTO " + tableName + " VALUES(NULL, '" + memo.maintext + "','" + memo.subtext + "'," + memo.getIsdone() + ");";
        db.execSQL(sql);
    }



    //데이터 삭제

    public void deleteMemo(int position){

        String sql = "DELETE FROM " + tableName +" WHERE seq =" + position + ";";
        db.execSQL(sql);
    }

    //데이터 수정

    public ArrayList<Memo> selectAll(){
        String sql = "SELECT * FROM " + tableName;

        ArrayList<Memo> list = new ArrayList<>();

        Cursor results = db.rawQuery(sql,null);
        results.moveToFirst();

        while(!results.isAfterLast()){

            Memo memo = new Memo(results.getInt(0),results.getString(1),results.getString(2),results.getInt(3));
            list.add(memo);
            results.moveToNext();
        }

        results.close();
        return list;
    }

}
