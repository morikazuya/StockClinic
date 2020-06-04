package com.km.android.stockclinic.trade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ___DBAdapter {
    private final static String DB_NAME = "stockClinic.db";
    private final static String DB_TABLE = "stock";
    private final static int DB_VERSION = 1;

    /**
     * DBのカラム名
     */
    public final static String _id = "_id";
    public final static String stock_hold = "stockHold";
    public final static String trade_type = "tradeType";
    public final static String trade_date = "tradeDate";
    public final static String trade_quantity = "tradeQuantity";
    public final static String trade_price = "tradePrice";
    public final static String trade_fee = "tradeFee";

    private SQLiteDatabase db;
//    private DBHelper dbHelper;
    protected Context context;

    // コンストラクタ
    public ___DBAdapter(Context context) {
        this.context = context;
//        dbHelper = new DBHelper(this.context);
    }

//    /**
//     * DBの読み書き
//     * openDB()
//     *
//     * @return this 自身のオブジェクト
//     */
//    public ___DBAdapter openDB() {
//        db = dbHelper.getWritableDatabase();
//        return this;
//    }
//
//    /**
//     * DBの読み込み 今回は未使用
//     * readDB()
//     *
//     * @return this 自身のオブジェクト
//     */
//    public ___DBAdapter readDB() {
//        db = dbHelper.getReadableDatabase();
//        return this;
//    }
//
//    /**
//     * DBを閉じる
//     * closeDB()
//     */
//    public void closeDB() {
//        db.close();
//        db = null;
//    }

    /**
     * DBのレコードへ登録
     * saveDB()
     *
     * @param stockHold
     * @param tradeType
     * @param tradeDate
     * @param tradeQuantity
     * @param tradePrice
     * @param tradeFee
     */
    public void saveDB(String stockHold, String tradeType, String tradeDate, int tradeQuantity, double tradePrice, double tradeFee) {

        db.beginTransaction();          // トランザクション開始

        try {
            ContentValues values = new ContentValues();     // ContentValuesでデータを設定していく
            values.put(stock_hold, stockHold);
            values.put(trade_type, tradeType);
            values.put(trade_date, tradeDate);
            values.put(trade_quantity, tradeQuantity);
            values.put(trade_price, tradePrice);
            values.put(trade_fee, tradeFee);

            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues
            db.insert(DB_TABLE, null, values);      // レコードへ登録

            db.setTransactionSuccessful();      // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                // トランザクションの終了
        }
    }

    /**
     * DBのデータを取得
     * getDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */
    public Cursor getDB(String[] columns) {

        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db.query(DB_TABLE, columns, null, null, null, null, null);
    }

    /**
     * DBの検索したデータを取得
     * searchDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @param column  String 選択条件に使うカラム名
     * @param name    String[]
     * @return DBの検索したデータ
     */
    public Cursor searchDB(String[] columns, String column, String[] name) {
        return db.query(DB_TABLE, columns, column + " like ?", name, null, null, null);
    }

    /**
     * DBのレコードを全削除
     * allDelete()
     */
    public void allDelete() {

        db.beginTransaction();                      // トランザクション開始
        try {
            // deleteメソッド DBのレコードを削除
            // 第1引数：テーブル名
            // 第2引数：削除する条件式 nullの場合は全レコードを削除
            // 第3引数：第2引数で?を使用した場合に使用
            db.delete(DB_TABLE, null, null);        // DBのレコードを全削除
            db.setTransactionSuccessful();          // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                    // トランザクションの終了
        }
    }

    /**
     * DBのレコードの単一削除
     * selectDelete()
     *
     * @param position String
     */
    public void selectDelete(String position) {

        db.beginTransaction();                      // トランザクション開始
        try {
            db.delete(DB_TABLE, _id + "=?", new String[]{position});
            db.setTransactionSuccessful();          // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                    // トランザクションの終了
        }
    }

//    /**
//     * データベースの生成やアップグレードを管理するSQLiteOpenHelperを継承したクラス
//     * DBHelper
//     */
//    private static class DBHelper extends SQLiteOpenHelper {
//
//        // コンストラクタ
//        public DBHelper(Context context) {
//            //第1引数：コンテキスト
//            //第2引数：DB名
//            //第3引数：factory nullでよい
//            //第4引数：DBのバージョン
//            super(context, DB_NAME, null, DB_VERSION);
//        }
//        /**
//         * DB生成時に呼ばれる
//         * onCreate()
//         *
//         * @param db SQLiteDatabase
//         */
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//
//            //テーブルを作成するSQL文の定義 ※スペースに気を付ける
//            String createTbl = "CREATE TABLE " + DB_TABLE + " (" +
//                    "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
//                    "stock_hold TEXT NOT NULL , " +
//                    "trade_type TEXT NOT NULL , " +
//                    "trade_date DATE NOT NULL , " +
//                    "trade_quantity INTEGER NOT NULL , " +
//                    "trade_price DOUBLE NOT NULL , " +
//                    "trade_fee DOUBLE NOT NULL)";
//
//            db.execSQL(createTbl);      //SQL文の実行
//        }
//
//        /**
//         * DBアップグレード(バージョンアップ)時に呼ばれる
//         *
//         * @param db         SQLiteDatabase
//         * @param oldVersion int 古いバージョン
//         * @param newVersion int 新しいバージョン
//         */
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            // DBからテーブル削除
//            db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
//            // テーブル生成
//            onCreate(db);
//        }
//    }
}




