package com.km.android.stockclinic.trade;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.km.android.stockclinic.R;

import java.util.Calendar;

public class AddTradeActivity extends AppCompatActivity {
    int _id = -1;

    EditText _stock_hold;
    Spinner _trade_type;
    EditText _trade_date;
    EditText _trade_quantity;
    EditText _trade_price;
    EditText _trade_fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trade);

        //戻るボタン
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        _trade_date = findViewById(R.id.tradeDate);
        _trade_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTradeActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                _trade_date.setText(String.format("%d/%02d/%02d", year, month+1, dayOfMonth));
                            }
                        },
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DATE)
                );
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSave(View view) {
        _stock_hold = findViewById(R.id.stockHold);
        String stockHold = _stock_hold.getText().toString();

        _trade_type = findViewById(R.id.tradeType);
        String tradeType = (String) _trade_type.getSelectedItem();

//        _trade_date = findViewById(R.id.tradeDate);
        String tradeDate =  _trade_date.getText().toString();

        _trade_quantity = findViewById(R.id.tradeQuantity);
        String tradeQuantity = _trade_quantity.getText().toString();

        _trade_price = findViewById(R.id.tradePrice);
        String tradePrice = _trade_price.getText().toString();

        _trade_fee = findViewById(R.id.tradeFee);
        String tradeFee = _trade_fee.getText().toString();


        StockDatabaseHelper helper = new StockDatabaseHelper(AddTradeActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            String sqlInsert = "INSERT INTO stock (stock_hold, trade_type, trade_date, trade_quantity, trade_price, trade_fee) VALUES (?, ?, ?, ?, ?, ?)";
            SQLiteStatement stmt = db.compileStatement(sqlInsert);

            //stmt.bindLong(1, _id);
            stmt.bindString(1, stockHold);
            stmt.bindString(2, tradeType);
            stmt.bindString(3, String.valueOf(tradeDate));
            stmt.bindString(4, tradeQuantity);
            stmt.bindString(5, tradePrice);
            stmt.bindString(6, tradeFee);

            stmt.executeInsert();
        } finally {
            db.close();
        }
        finish();
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _id = position;
        }
    }
}
