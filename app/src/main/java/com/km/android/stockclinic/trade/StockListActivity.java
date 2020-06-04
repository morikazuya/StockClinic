package com.km.android.stockclinic.trade;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.km.android.stockclinic.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockListActivity extends AppCompatActivity {
    StockDatabaseHelper helper = new StockDatabaseHelper(StockListActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        //戻るボタン
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //DBから銘柄番号取得
        ListView lvHoldStock = findViewById(R.id.lvHoldStock);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM stock LIMIT 5", null );
        c.moveToFirst();

        //Map作成ループ
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        int idxStock;
        int i = 0;
        //Mapのキー
//        String[] from = new String[c.getCount()];

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Map map = new HashMap();

                    //銘柄番号取得
                    idxStock = c.getColumnIndex("stock_hold");
                    String stock_code = c.getString(idxStock);
                    Log.d("stock_code", stock_code);

//                    //現在の株価取得
//                    StockPriceReceiver receiver = new StockPriceReceiver();
//                    receiver.execute(stock_code);
//                    String getPrice = "0";
//                    Log.d("getPrice", getPrice);
//
//                    map.put(stock_code, getPrice);
                    map.put("name", stock_code);
                    data.add(map);

//                    from[i] = stock_code;
//                    Log.d("from", from[i]);
//                    i++;
                } while (c.moveToNext()); {

                }
            }
        }

        String[] from = {"name"};
        int[] to = {android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(StockListActivity.this, data, android.R.layout.simple_expandable_list_item_1, from, to);
        lvHoldStock.setAdapter(adapter);
        lvHoldStock.setOnItemClickListener(new ListItemClickListener());
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            String stockName = item.get("name");
            Log.d("stockName", stockName);
            TextView tvStockName = findViewById(R.id.tvStockName);
            tvStockName.setText(stockName + "の株価： ");
            TextView tvStockPrice = findViewById(R.id.tvStockPrice);
            StockPriceReceiver receiver = new StockPriceReceiver(tvStockPrice);
            receiver.execute(stockName);

//            try {
//                Thread.sleep(13000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
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



    private class StockPriceReceiver extends AsyncTask<String, String, String> {
        private TextView _tvStockPrice;

        public StockPriceReceiver(TextView tvStockPrice) {
            _tvStockPrice = tvStockPrice;
        }

        @Override
        protected String doInBackground(String... params) {
            String code = params[0] + ".T";
            String api_key = "571N3L9KQIUW1S8Y";

            String urlStr = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + code + "&interval=5min&apikey=" + api_key;
            String result = "";

            HttpURLConnection con = null;
            InputStream is = null;
            try {
                URL url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                is = con.getInputStream();
                result = is2String(is);
            } catch (MalformedURLException ex) {

            } catch (IOException e) {

            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {

                    }
                }
            }
            return result;
        }

        @Override
        public void onPostExecute(String result) {
            String lastRefreshed = "";
            String getPrice = "";

            try {
                JSONObject rootJSON = new JSONObject(result);
                JSONObject metaData = rootJSON.getJSONObject("Meta Data");
                lastRefreshed = metaData.getString("3. Last Refreshed");
                Log.d("lastRefreshed", lastRefreshed);

                JSONObject timeSeries5min = rootJSON.getJSONObject("Time Series (Daily)");
                JSONObject timeNow = timeSeries5min.getJSONObject(lastRefreshed);
                getPrice = timeNow.getString("5. adjusted close");
                Log.d("getPrice", getPrice);


            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            _tvStockPrice.setText(getPrice);
        }

        private String is2String(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            char[] b = new char[1024];
            int line;
            while (0 <= (line = reader.read(b))) {
                sb.append(b, 0, line);
            }
            return sb.toString();
        }
    }
}
