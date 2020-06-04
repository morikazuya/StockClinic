package com.km.android.stockclinic.trade;

import android.util.Log;

public class ListViewItem {
    protected int _id;
    protected String stockHold;
    protected String tradeType;
    protected String tradeDate;
    protected int tradeQuantity;
    protected double tradePrice;
    protected double tradeFee;


    //コンストラクタ
    public ListViewItem(int _id, String stockHold, String tradeType, String tradeDate, int tradeQuantity, double tradePrice, double tradeFee) {
        this._id = _id;
        this.stockHold = stockHold;
        this.tradeType = tradeType;
        this.tradeDate = tradeDate;
        this.tradeQuantity = tradeQuantity;
        this.tradePrice = tradePrice;
        this.tradeFee = tradeFee;
    }

    //セッター、ゲッター
    public int getId() {
        Log.d("取得したID：", String.valueOf(_id));
        return _id;
    }

    public String getStockHold() {
        return stockHold;
    }

    public String getTradeType() {
        return tradeType;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public int getTradeQuantity() {
        return tradeQuantity;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public double getTradeFee() {
        return tradeFee;
    }

}
