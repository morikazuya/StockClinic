package com.km.android.stockclinic.trade;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StockDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "stockClinic.db";
    private final static String DB_TABLE = "stock";
    private static final int DB_VERSION = 1;

    //コンストラクタ
    StockDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TABLE +
                "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "stock_hold TEXT NOT NULL , " +
                "trade_type TEXT NOT NULL , " +
                "trade_date DATE NOT NULL , " +
                "trade_quantity INTEGER NOT NULL , " +
                "trade_price DOUBLE NOT NULL , " +
                "trade_fee DOUBLE NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }
}
