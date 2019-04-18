package com.example.honghanh.bookstoremanager.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SqlHelper extends SQLiteOpenHelper {



    public SqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void queryDataToBookstore(){
        SQLiteDatabase database = getWritableDatabase();
        //database.execSQL("DROP TABLE IF EXISTS BOOKSTORE");
        database.execSQL("CREATE TABLE IF NOT EXISTS BOOKSTORE(Id CHAR PRIMARY KEY, " +
                " name NVARCHAR, address NVARCHAR, telephone CHAR, email CHAR, overview NVARCHAR, image CHAR, bigImage CHAR, " +
                "likebookstore INTEGER)");
    }

    /*----------------------------------- INSERT DATA INTO TABLE -----------------------------------*/
    public void insertToBookstoreTable(String id, String name, String address, String telephone, String email,
                                       String overview, String avt, String bigPoster){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO BOOKSTORE VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, id);
        statement.bindString(2, name);
        statement.bindString(3, address);
        statement.bindString(4, telephone);
        statement.bindString(5, email);
        statement.bindString(6, overview);
        statement.bindString(7, avt);
        statement.bindString(8, bigPoster);

        statement.executeInsert();

    }

    public void updateOneValue(String id, int likebookstore) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE BOOKSTORE SET likebookstore = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindDouble(1, (double)likebookstore);
        statement.bindString(2, id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }


}
