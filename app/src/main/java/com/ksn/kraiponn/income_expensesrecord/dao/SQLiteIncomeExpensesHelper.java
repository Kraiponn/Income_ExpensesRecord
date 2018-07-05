package com.ksn.kraiponn.income_expensesrecord.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteIncomeExpensesHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "income_expenses_db";
    private static SQLiteIncomeExpensesHelper helper;

    private SQLiteIncomeExpensesHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    public static synchronized SQLiteIncomeExpensesHelper newInstance(Context context) {
        if (helper == null) {
            helper = new SQLiteIncomeExpensesHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE user_list(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_name TEXT, " +
                "password TEXT) ";
        db.execSQL(sql);

        String sql2 = "CREATE TABLE income_expenses(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date INTEGER, " +
                "month INTEGER, " +
                "year INTEGER, " +
                "title TEXT, " +
                "type TEXT, " +
                "amount INTEGER) ";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
