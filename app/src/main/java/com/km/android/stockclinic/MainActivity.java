package com.km.android.stockclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.km.android.stockclinic.trade.PieChartActivity;
import com.km.android.stockclinic.trade.PortfolioActivity;
import com.km.android.stockclinic.trade.StockListActivity;
import com.km.android.stockclinic.trade.TradeListActivity;

public class MainActivity extends AppCompatActivity {
    Button btnStock;
    Button btnPortfolio;
    Button btnPieChart;
    Button btnTrade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnStock = findViewById(R.id.btnStock);
        btnStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StockListActivity.class);
                startActivity(intent);
            }
        });

//        btnPortfolio = findViewById(R.id.btnPortfolio);
//        btnPortfolio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, PortfolioActivity.class);
//                startActivity(intent);
//            }
//        });

        btnPieChart = findViewById(R.id.btnPieChart);
        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PieChartActivity.class);
                startActivity(intent);
            }
        });

        btnTrade = findViewById(R.id.btnTrade);
        btnTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TradeListActivity.class);
                startActivity(intent);
            }
        });
    }

}
