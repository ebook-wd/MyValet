package com.mycompany.myvalet;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import java.io.BufferedReader;
import 	java.io.InputStreamReader;
import 	java.io.IOException;

import java.io.BufferedReader; 
import java.io.DataInputStream; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.OutputStreamWriter;
import java.io.InputStreamReader; 
import android.os.Environment;

public class AccountDisplayActivity extends Activity
{
	private WebView webView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from new_activity.xml
		setContentView(R.layout.display);
		String s= getIntent().getStringExtra("KEY");
		int year = getIntent().getIntExtra("YEAR", 0);
		if(s.equals("Statement")){
			int i=0;
			String html ="";
			String htmlf ="";
			String str1 ="";
			String str2 ="";
			String str3 ="";
			String str4 ="";
			String str5 ="";
			String str6 ="";
			String str7 ="";
			String str8 ="";
			String str9 ="";
			String str10 ="";
			String str11 ="";
			String str12 ="";
			String str13 ="";
			
			float yCredit =  0;
			float yDebit = 0 ;
			float yBalance = 0 ;
			DatabaseHandler db = new DatabaseHandler(this);

			List<Float> CREDIT = new ArrayList<>();
			List<Float> CREDIT_BILLS = new ArrayList<>();
			List<Float> CREDIT_SALARY = new ArrayList<>();
			List<Float> CREDIT_OTHERS = new ArrayList<>();
			
			List<Float> DEBIT = new ArrayList<>();
			List<Float> DEBIT_BILLS = new ArrayList<>();
			List<Float> DEBIT_DRESS = new ArrayList<>();
			List<Float> DEBIT_ENTERTAINMENT = new ArrayList<>();
			List<Float> DEBIT_FOOD = new ArrayList<>();
			List<Float> DEBIT_GROCERY = new ArrayList<>();
			List<Float> DEBIT_LOAN = new ArrayList<>();
			List<Float> DEBIT_OTHERS = new ArrayList<>();
			
			List<Float> BALANCE = new ArrayList<>();

			for( i=0;i<=11; i++){

				CREDIT.add(db.getMonthlyCredit(i+1, year));
				CREDIT_BILLS.add(db.getMonthlyDetails(i+1, year, "Credit", "Bills"));
				CREDIT_SALARY.add(db.getMonthlyDetails(i+1, year, "Credit", "Salary"));			
				CREDIT_OTHERS.add(db.getMonthlyDetails(i+1,year, "Credit", "Others"));
				DEBIT.add(db.getMonthlyDebit(i+1, year));
				DEBIT_BILLS.add(db.getMonthlyDetails(i+1, year, "Debit", "Bills"));			
				DEBIT_DRESS.add(db.getMonthlyDetails(i+1, year, "Debit", "Dress"));			
				DEBIT_ENTERTAINMENT.add(db.getMonthlyDetails(i+1, year, "Debit", "Entertainment"));			
				DEBIT_FOOD.add(db.getMonthlyDetails(i+1, year, "Debit", "Food"));			
				DEBIT_GROCERY.add(db.getMonthlyDetails(i+1, year, "Debit", "Grocery"));			
				DEBIT_LOAN.add(db.getMonthlyDetails(i+1, year, "Debit", "Loan"));			
				DEBIT_OTHERS.add(db.getMonthlyDetails(i+1, year, "Debit", "Others"));	
				
				BALANCE.add( CREDIT.get(i)- DEBIT.get(i));
			}
			
			for( i=0;i<=11; i++){
				yCredit =  yCredit + CREDIT.get(i);
				yDebit = yDebit +DEBIT.get(i) ;
				yBalance = yBalance + BALANCE.get(i);
			}

			webView = (WebView) findViewById(R.id.webView1);
			webView.getSettings().setJavaScriptEnabled(true);
			//webView.loadUrl("https://www.google.co.in");

			//String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
			//webView.loadData(customHtml, "text/html", "UTF-8");
			
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(
					new InputStreamReader(getAssets().open("statement.html"), "UTF-8")); 

				// do reading, usually loop until end of file reading 
				
				while ((htmlf = reader.readLine()) != null) {
					//process line
					html = html.concat(htmlf);
					
				}
			} catch (IOException e) {
				//log the exception
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						//log the exception
					}
				}
			}
			for(i=0;i<=11;i++){
				str1 = "$CREDIT["+i+"]$";
				str2 = "$DEBIT["+i+"]$";
				str3 = "$BALANCE["+i+"]$";
				str4 = "$CREDIT_BILLS["+i+"]$";
				str5 = "$CREDIT_SALARY["+i+"]$";
				str6 = "$CREDIT_OTHERS["+i+"]$";
				
				str7 = "$DEBIT_BILLS["+i+"]$";
				str8 = "$DEBIT_DRESS["+i+"]$";
				str9 = "$DEBIT_ENTERTAINMENT["+i+"]$";
				str10 = "$DEBIT_FOOD["+i+"]$";
				str11 = "$DEBIT_GROCERY["+i+"]$";
				str12 = "$DEBIT_LOAN["+i+"]$";
				str13 = "$DEBIT_OTHERS["+i+"]$";
				
				html = html.replace(str1, CREDIT.get(i).toString());
				html = html.replace(str4, CREDIT_BILLS.get(i).toString());
				html = html.replace(str5, CREDIT_SALARY.get(i).toString());
				html = html.replace(str6, CREDIT_OTHERS.get(i).toString());
				
				html = html.replace(str2, DEBIT.get(i).toString());
				html = html.replace(str7, DEBIT_BILLS.get(i).toString());
				html = html.replace(str8, DEBIT_DRESS.get(i).toString());
				html = html.replace(str9, DEBIT_ENTERTAINMENT.get(i).toString());
				html = html.replace(str10, DEBIT_FOOD.get(i).toString());
				html = html.replace(str11, DEBIT_GROCERY.get(i).toString());
				html = html.replace(str12, DEBIT_LOAN.get(i).toString());
				html = html.replace(str13, DEBIT_OTHERS.get(i).toString());
				html = html.replace(str3, BALANCE.get(i).toString());
				
			}
			
			html = html.replace("$yCredit$",String.valueOf( yCredit));
			html = html.replace("$yDebit$", String.valueOf(yDebit));
			html = html.replace("$yBalance$", String.valueOf(yBalance));
			
			//String filesPath = getFilesDir().getAbsolutePath();
			String filePath = "file:///MyValet/statement.html";
		    // webView.loadUrl("file:///android_asset/statement.html");
			webView.loadDataWithBaseURL(null,html,"text/html","UTF-8","about:blank");
			//webView.loadData(html, "text/html", "UTF-8");
			}else if(s.equals("Help")){
			webView = (WebView) findViewById(R.id.webView1);
			webView.getSettings().setJavaScriptEnabled(true);
			
			webView.loadUrl("file:///android_asset/help.html");
		}
	}
  
}

/*
function toggle_by_class(cls, on) {
    var lst = document.getElementsByClassName(cls);
    for(var i = 0; i < lst.length; ++i) {
        lst[i].style.display = on ? '' : 'none';
    }
}
*/
