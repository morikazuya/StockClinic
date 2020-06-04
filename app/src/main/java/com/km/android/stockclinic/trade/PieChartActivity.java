package com.km.android.stockclinic.trade;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.util.List;

public class PieChartActivity extends AppCompatActivity {

StockDatabaseHelper helper = new StockDatabaseHelper(PieChartActivity.this);
    List<String> stock = new ArrayList<>();
    List<Float> price = new ArrayList<Float>();
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        //戻るボタン
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //チャートの値準備
        //株価
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM stock LIMIT 5", null );
        c.moveToFirst();

        int idxStock;
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    //銘柄番号取得
                    idxStock = c.getColumnIndex("stock_hold");
                    String number = c.getString(idxStock);
                    stock.add(number);

                    PieChartActivity.StockPriceReceiver receiver = new StockPriceReceiver();
                    receiver.execute(number);

//                    try {
//                        Thread.sleep(13000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                } while (c.moveToNext()); {

                }
            }
        }
        c.close();
        db.close();
        setupPieChart();
    }

    private void setupPieChart() {
        //PieEntriesのリストを作成する:
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < price.size(); i++) {
            pieEntries.add(new PieEntry(price.get(i), stock.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "stock_code");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(14f);
        PieData data = new PieData(dataSet);

        //PieChartを取得する:
        PieChart piechart = findViewById(R.id.pie_chart);
        piechart.setData(data);
        piechart.invalidate();
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

        public StockPriceReceiver() {

        }

        @Override
        protected String doInBackground(String... value) {
            //株価取得
            String code = value[0] + ".T";
            Log.d("code", code);
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
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("result", result);
            return result;
        }


        @Override
        public void onPostExecute(String result) {
            try {
                String lastRefreshed = "";
                Float getPrice = 0f;

                JSONObject rootJSON = new JSONObject(result);
                JSONObject metaData = rootJSON.getJSONObject("Meta Data");
                lastRefreshed = metaData.getString("3. Last Refreshed");
                Log.d("lastRefreshed", lastRefreshed);

                JSONObject timeSeries5min = rootJSON.getJSONObject("Time Series (Daily)");
                JSONObject timeNow = timeSeries5min.getJSONObject(lastRefreshed);
                getPrice = Float.valueOf(timeNow.getInt("5. adjusted close"));
                Log.d("getPrice", String.valueOf(getPrice));


                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM stock LIMIT 5", null );
                c.moveToFirst();
                int quantity = c.getColumnIndex("trade_quantity");
                Float quantityValue = c.getFloat(quantity);
                Log.d("quantityValue", String.valueOf(quantityValue));
                Float marketValue = getPrice * quantityValue;
                Log.d("marketValue", String.valueOf(marketValue));

                price.add(marketValue);
                c.close();
                db.close();
                setupPieChart();

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
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
