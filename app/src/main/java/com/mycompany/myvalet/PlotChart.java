package com.mycompany.myvalet;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.XAxis;
import android.app.*;
import android.app.Activity;

import android.graphics.Color;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
public class PlotChart extends Activity
{
	//BarChart chart ;
	HorizontalBarChart chart;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;
	DatabaseHandler db;
	float food_value =0;
	float entertainment_value =0;
	float bills_value =0;
	float grocery_value =0;
	float dress_value =0;
	float loan_value =0;
	float others_value = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		String chart_type = getIntent().getStringExtra("CHART");
		if(chart_type.equals("barchart")){
			
			setContentView(R.layout.chart);
			//chart = (BarChart) findViewById(R.id.bar_chart);
			chart = (HorizontalBarChart     ) findViewById(R.id.bar_chart);
			BARENTRY = new ArrayList<>();
			BarEntryLabels = new ArrayList<String>();
			getDataFromDB();
			AddValues();
			Bardataset = new BarDataSet(BARENTRY, "Expenses");
			BARDATA = new BarData(BarEntryLabels, Bardataset);
			Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
			chart.setData(BARDATA);
			//chart.getXAxis().setValueFormatter(new LabelValueFormatter(data));
			chart.animateY(3000);
			
		}else if(chart_type.equals("pichart")){
			
			setContentView(R.layout.pi_chart);
			PieChart pieChart = (PieChart) findViewById(R.id.piechart);
			pieChart.setUsePercentValues(true);
			getDataFromDB();
			
			// IMPORTANT: In a PieChart, no values (Entry) should have the same
			// xIndex (even if from different DataSets), since no values can be
			// drawn above each other.
			ArrayList<Entry> yvalues = new ArrayList<Entry>();
			ArrayList<String> xVals = new ArrayList<String>();
			if(food_value!=0){
				yvalues.add(new Entry(food_value, 0));
				xVals.add("Food");
			}
			if(entertainment_value!=0){
				yvalues.add(new Entry(entertainment_value, 1));
				xVals.add("Entertainment");
			}
			if(loan_value!=0){
				yvalues.add(new Entry(loan_value, 2));
				xVals.add("Loan");
			}
			if(	bills_value!=0){
				yvalues.add(new Entry(bills_value, 3));
				xVals.add("Bills");
			}		
			if(grocery_value!=0){
				yvalues.add(new Entry(grocery_value, 4));
				xVals.add("Grocery");
			}
			if(dress_value!=0){
				yvalues.add(new Entry(dress_value, 5));
				xVals.add("Dress");
			}
			if(others_value!=0){
				yvalues.add(new Entry(others_value, 6));
				xVals.add("Others");
			}
				
			PieDataSet dataSet = new PieDataSet(yvalues, "Catagory");
            dataSet.setSliceSpace(3);
			dataSet.setSelectionShift(6);
		
			PieData data = new PieData(xVals, dataSet);
			data.setValueFormatter(new PercentFormatter());
			pieChart.setData(data);
			
			pieChart.setDescription("Expenses");

			pieChart.setDrawHoleEnabled(true);
			pieChart.setTransparentCircleRadius(25f);
			pieChart.setHoleRadius(25f);

			dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
			//dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
			//dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
			//dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
			//dataSet.setColors(ColorTemplate.PASTEL_COLORS);
			
			//dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
			data.setValueTextSize(13f);
			data.setValueTextColor(Color.DKGRAY);
			//pieChart.setOnChartValueSelectedListener(this);

			pieChart.animateXY(1400, 1400);
			
		}else if(chart_type.equals("linechart")){
			
			setContentView(R.layout.line_chart);
	        LineChart mChart =  (LineChart) findViewById(R.id.linechart);
			getDataFromDB();
			//mChart = (LineChart) findViewById(R.id.chart);
            // add data
			ArrayList<  Entry > yVals = new ArrayList<Entry>();
			yVals.add(new Entry(food_value, 0));
			yVals.add(new Entry( entertainment_value , 1));
			yVals.add(new Entry(loan_value, 2));
			yVals.add(new Entry(bills_value, 3));
			yVals.add(new Entry(grocery_value, 4));
			yVals.add(new Entry(dress_value, 5));
			yVals.add(new Entry(others_value, 6));
			
			
			ArrayList<String> xVals = new ArrayList<>();
			xVals.add("Food");
			xVals.add("Entertainment");
			xVals.add("Loan");
			xVals.add("Bills");
			xVals.add("Grocery");
			xVals.add("Dress");
			xVals.add("Others");
			
			LineDataSet set1;

           // create a dataset and give it a type
			set1 = new LineDataSet(yVals, "DataSet 1");
			set1.setFillAlpha(110);

			set1.setColor(Color.BLUE);
			set1.setCircleColor(Color.BLACK);
			set1.setLineWidth(2f);
			set1.setCircleRadius(4f);
			set1.setDrawCircleHole(true);
			set1.setValueTextSize(9f);
			set1.setDrawFilled(true);

			//ArrayList<ILineDataSet> dataSets = new ArrayList<>();
			//dataSets.add(set1);
			LineData data = new LineData(xVals, set1);
           // LineData data = new LineData(xVals, dataSets);
			//LineDataSet set = new LineDataSet(null, null);
			//LineData data = new LineData();
			//data.addDataSet(set);

			mChart.setData(data);
			mChart.invalidate(); 
		}
    }
	
	public void getDataFromDB(){
		int month=0;
		int year = 0;
		month = getIntent().getIntExtra("MONTH", 0);
		year = getIntent().getIntExtra("YEAR", 0);
		
		db = new DatabaseHandler(this);
		food_value = db.getCatagoryDetails(month, year, "Food");
		entertainment_value = db.getCatagoryDetails(month, year, "Entertainment");
		loan_value = db.getCatagoryDetails(month, year, "Loan");
		grocery_value = db.getCatagoryDetails(month, year, "Grocery");
		bills_value = db.getCatagoryDetails(month, year, "Bills");
	    dress_value = db.getCatagoryDetails(month, year, "Dress");
		others_value = db.getCatagoryDetails(month, year, "Others");
	}

    public void AddValues(){

		BARENTRY.add(new BarEntry(food_value, 0));
		BarEntryLabels.add("Food");
		BARENTRY.add(new BarEntry(entertainment_value, 1));
		BarEntryLabels.add("Entertainment");
		BARENTRY.add(new BarEntry(loan_value, 2));
		BarEntryLabels.add("Loan");
		BARENTRY.add(new BarEntry(bills_value, 3));
		BarEntryLabels.add("Bills");
		BARENTRY.add(new BarEntry(grocery_value, 4));
		BarEntryLabels.add("Grocery");
		BARENTRY.add(new BarEntry(dress_value, 5));
		BarEntryLabels.add("Dress");
		BARENTRY.add(new BarEntry(others_value, 6));
		BarEntryLabels.add("Others");
        /*
		if(food_value!=0){
			BARENTRY.add(new BarEntry(food_value, 0));
			BarEntryLabels.add("Food");
		}
		if(entertainment_value!=0){
			BARENTRY.add(new BarEntry(entertainment_value, 1));
			BarEntryLabels.add("Entertainment");
		}
		if(loan_value!=0){
			BARENTRY.add(new BarEntry(loan_value, 2));
			BarEntryLabels.add("Loan");
		}
		if(bills_value!=0){
			BARENTRY.add(new BarEntry(bills_value, 3));		
			BarEntryLabels.add("Bills");
		}
		if(grocery_value!=0){
			BARENTRY.add(new BarEntry(grocery_value, 4));
			BarEntryLabels.add("Grocery");
		}
		if(dress_value!=0){
			BARENTRY.add(new BarEntry(dress_value, 5));
			BarEntryLabels.add("Dress");
		}
		if(others_value!=0){
			BARENTRY.add(new BarEntry(others_value, 6));
			BarEntryLabels.add("Others");
		}
		*/
    }

    
}
