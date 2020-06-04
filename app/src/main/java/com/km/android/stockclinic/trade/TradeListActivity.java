package com.km.android.stockclinic.trade;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.km.android.stockclinic.R;

import java.util.ArrayList;
import java.util.List;

public class TradeListActivity extends AppCompatActivity {
    StockDatabaseHelper helper = new StockDatabaseHelper(TradeListActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_list);

        //戻るボタン
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //DB一覧表示
        ListView lvTrade = findViewById(R.id.lvTrade);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query("stock", null, null, null, null, null, null);
        String[] from = {"stock_hold", "trade_type", "trade_date", "trade_quantity", "trade_price"};
        int[] to = {R.id.tvStockHold, R.id.tvTradeType, R.id.tvTradeDate, R.id.tvTradeQuantity, R.id.tvTradePrice};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(TradeListActivity.this, R.layout.trade_list_item, c, from, to, 0);
        lvTrade.setAdapter(adapter);


        lvTrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TradeListActivity.this);
                builder.setTitle("編集");
                builder.setMessage("操作を選んでください");
                builder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<ListViewItem> items = new ArrayList<>();
                        ListViewItem listViewItem;
                        SQLiteDatabase db = helper.getWritableDatabase();
                        Cursor cursor = db.query("stock", null, null, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            do {
                                listViewItem = new ListViewItem(
                                        cursor.getInt(0),
                                        cursor.getString(1),
                                        cursor.getString(2),
                                        cursor.getString(3),
                                        cursor.getInt(4),
                                        cursor.getDouble(5),
                                        cursor.getDouble(6));

                                Log.d("取得したCursor(ID):", String.valueOf(cursor.getInt(0)));
                                Log.d("取得したCursor(保有銘柄):", cursor.getString(1));
                                Log.d("取得したCursor(取引タイプ):", cursor.getString(2));
                                Log.d("取得したCursor(取引日):", cursor.getString(3));
                                Log.d("取得したCursor(量):", cursor.getString(4));
                                Log.d("取得したCursor(価格):", cursor.getString(5));
                                Log.d("取得したCursor(手数料):", cursor.getString(6));

                                items.add(listViewItem);

                            } while (cursor.moveToNext());
                        }
                        ListViewItem listItem = items.get(position);
                        int listId = listItem.getId();
                        Log.d("Long click : ", String.valueOf(listId));
                        Cursor c = db.rawQuery("DELETE FROM stock WHERE _id = " + String.valueOf(listId), null);
                        c.moveToFirst();
                        //todo
//                        adapter.notifyDataSetChanged();
                    }
                });
//                builder.setNegativeButton("編集", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
                builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trade_menu_option_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.add_trade:
                Intent intent = new Intent(TradeListActivity.this, AddTradeActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}