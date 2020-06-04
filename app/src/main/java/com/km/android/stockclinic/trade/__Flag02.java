package com.km.android.stockclinic.trade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.km.android.stockclinic.R;

import java.util.ArrayList;

public class __Flag02 extends Fragment  {

    PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.__frag02_layout, container, false);
        pieChart = view.findViewById(R.id.pie_chart);

        PieDataSet pieDataSet = new PieDataSet(pieChartDataset(), "data");
//        PieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueLineColor(R.color.colorAccent);
//        PieDataSet.setValueTextSize(14f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("data");
        pieChart.animate();

        return inflater.inflate(R.layout.__frag02_layout, container,false);
    }

    private ArrayList<PieEntry> pieChartDataset() {
        ArrayList<PieEntry> dataset = new ArrayList<PieEntry>();

        dataset.add(new PieEntry(0, 40));
        dataset.add(new PieEntry(1, 10));
        dataset.add(new PieEntry(2, 15));
        dataset.add(new PieEntry(3, 12));

        return dataset;
    }
}