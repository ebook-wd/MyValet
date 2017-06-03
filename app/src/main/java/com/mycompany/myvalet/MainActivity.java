package com.mycompany.myvalet;

import static com.mycompany.myvalet.Constants.FIRST_COLUMN;
import static com.mycompany.myvalet.Constants.FOURTH_COLUMN;
import static com.mycompany.myvalet.Constants.SECOND_COLUMN;
import static com.mycompany.myvalet.Constants.THIRD_COLUMN;

import android.app.*;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.app.ListActivity;
import android.R.color;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.Window;
import android.content.Context;
import android.view.View.OnFocusChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.graphics.Color;
import java.text.SimpleDateFormat;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.text.*;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.*;
import java.io.BufferedReader; 
import java.io.DataInputStream; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.OutputStreamWriter;
import java.io.IOException; 
import java.io.InputStreamReader;
import android.widget.ExpandableListView.*; 

//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
public class MainActivity extends Activity 
{
	//Define the constants and variables----------------------
	//int pos;
	int edit = 0;
	int search =0;
	int pos =0;
	int item_id ;
	int month=0, year =0;
	String viewitem="";
	String search_month="";
	String search_catagory="";
	String amount_relation = "";
	String search_amount = "";
	String chart_type = "";
	Button addbtn;
	String catagory, trantype, discrip,amt;
	String search_credit ="";
	String search_debit ="";
	String search_discription = "";
	String relation = "";
	
	Float amount, credit, debit, balance;
	DatePicker simpleDatePicker;
	private Spinner spinner;
	private Spinner spinner1;
	EditText editDate ;
	EditText editDiscription ;
	EditText editAmount ;
	EditText edititem;
	LayoutInflater layoutinflater;
	ArrayAdapter<String> adapter ;
	private ArrayList<HashMap<String, String>> list;
	final DatabaseHandler db = new DatabaseHandler(this);
	final Context context = this;
	
	public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
	//-------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		//=======Cheking the user selected settings============"==================8===============
		SharedPreferences prefs = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE); 
		String name = prefs.getString("theme", "Light");//"No name defined" is the default value.
		//int idName = prefs.getInt("idName", 0); //0 is the default value.
	    if(name.equals("Dark")){
			//=====Setting the user selected theme================================================
			setTheme(R.style.DarkTheme);
		}
		
		//setTheme(R.style.DarkTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		//===hides the virtual key board at the begining of activity================="===================
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//-----------------------------------------------------------------------------------------------
		final TextView creditView = (TextView)findViewById(R.id.creditView);
		TextView debitView = (TextView)findViewById(R.id.debitView);
		TextView balanceView = (TextView)findViewById(R.id.balanceView);
		/*
		//===Adding header to list view=================================
		ListView listView2=(ListView)findViewById(R.id.accountView);
		layoutinflater = getLayoutInflater();
		ViewGroup header = (ViewGroup)layoutinflater.inflate(R.layout.item_header,listView2,false);
		listView2.addHeaderView(header);
		//==========================================================
		*/
		//displayResultList();
		
		final EditText editDate = (EditText)findViewById(R.id.editDate);
		editDate.setFocusable(false);
		final EditText editDiscription = (EditText)findViewById(R.id.editDiscription);
		final EditText editAmount = (EditText)findViewById(R.id.editAmount);
		//final ListView listView = (ListView)findViewById(R.id.accountView);
		final Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
		
		
		//=======On click display calender=================================
		editDate.setOnClickListener(new View.OnClickListener() {
		//editDate.setOnTouchListener(new View.OnTouchListener(){
		    @Override
		public void onClick(View v) {
   	
		//public boolean onTouch(View v, MotionEvent event) {
					
		    // calender class's instance and get current date , month and year from calender
		    final Calendar c = Calendar.getInstance();
			int mYear = c.get(Calendar.YEAR); // current year
			int mMonth = c.get(Calendar.MONTH); // current month
			int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
			
			// date picker dialog
			DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
            new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
            // set day of month , month and year value in the edit text
            // editDate.setText(dayOfMonth + "-"
		        //+ (monthOfYear + 1) + "-" + year);
				Date yourDate= new Date(year-1900, monthOfYear , dayOfMonth);
			    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
				String d = "";
				d = dateFormatter.format(yourDate);
				editDate.setText(d);
				Log.d("Date picker",d);
				//editDate.setText(year+ "-" + ((String.valueOf(monthOfYear + 1).length()  == 1 ? "0"+String.valueOf(monthOfYear+1):String.valueOf(monthOfYear+1)) ) + "-" + dayOfMonth);
				//editDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth );
				}
           }, mYear, mMonth, mDay);
		   datePickerDialog.show();
	    }
	});
	
	final RadioGroup radiogroup =  (RadioGroup) findViewById(R.id.radioGroup);
	radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
	{
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId)
		{
			switch(checkedId)
			{
                case R.id.radio_credit:
					addItemsOnSpinner("Credit");
					//Toast.makeText(MainActivity.this,"Credit clicked",Toast.LENGTH_LONG).show();
                    break;
                case R.id.radio_debit:           
					addItemsOnSpinner("Debit");
					//Toast.makeText(MainActivity.this,"Debit clicked",Toast.LENGTH_LONG).show();
					break;
                
			}
		}
	});
	
	addItemsOnSpinner("Credit");
	//addListenerOnSpinnerItemSelection();
	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		catagory = parent.getItemAtPosition(position).toString();
		//Toast.makeText(MainActivity.this,"Catagory:"+catagory,Toast.LENGTH_LONG).show();
		}
		public void onNothingSelected(AdapterView<?> parent) {
		}
	});
	Button cancelbtn = (Button)findViewById(R.id.maincancelButton);
	cancelbtn.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			editDate.setText("");
			editDiscription.setText("");
			editAmount.setText("");
			if(search==1){
				addbtn=(Button)findViewById(R.id.button);
				addbtn.setText("Add");
			}
		}
	});
	
    addbtn=(Button)findViewById(R.id.button);
	addbtn.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
		String date = editDate.getText().toString();
		String[] parts = date.split("-");
		String year = parts[0];
		String month = parts[1];
		
		//=====Hiding virtual keyboard================================================================
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		//---------------------------------------------------------------------------------
	    String discrip = editDiscription.getText().toString();
		//RadioGroup radiogroup =  (RadioGroup) findViewById(R.id.radioGroup);
		if(radiogroup.getCheckedRadioButtonId()!=-1){
		    int id= radiogroup.getCheckedRadioButtonId();
			View radioButton = radiogroup.findViewById(id);
			int radioId = radiogroup.indexOfChild(radioButton);
			RadioButton btn = (RadioButton) radiogroup.getChildAt(radioId);
			String selection = (String) btn.getText();
			trantype =selection;
			//Toast.makeText(MainActivity.this,"Date"+ selection,Toast.LENGTH_LONG).show();
		}
		
	    amt = editAmount.getText().toString();
		if(date.equals("")){
			Toast.makeText(MainActivity.this,"Date can not be empty",Toast.LENGTH_LONG).show();
			
		}else if(amt.equals("")){
			Toast.makeText(MainActivity.this,"Amount can not be empty",Toast.LENGTH_LONG).show();
		}else{
			//String.format("%.2f",Float.parseFloat(amt));
			Float amount= Float.parseFloat(amt);
			Log.d("Positio", String.valueOf(pos));
			Log.d("Edit:", String.valueOf(edit));
			if(date==""){
				Toast.makeText(MainActivity.this,"Date can not be empty",Toast.LENGTH_LONG).show();
			}else{
				if(edit == 0){
					db.addContact(new Contact(date, trantype, catagory, discrip, amount));
				}else{
					int update =db.updateContact(new Contact(item_id, date, trantype, catagory, discrip, amount));
					Log.d("Updating:", String.valueOf(update));
					edit = 0;
					addbtn.setText("Add");
				}
			}
			// Reading all contacts
			//Log.d("Reading: ", "Reading all contacts.."); 
			displayResultList(Integer.valueOf(month), Integer.valueOf(year));
			clear();
			//startActivity(new Intent(MainActivity.this, AccountDisplayActivity.class));
		    }
	    }
		
		public void clear()
		{
			editDate.setText("");
			editDiscription.setText("");
			editAmount.setText("");
		}
    });
  }
  
	//======Adding menu======="===========""===============
	public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu, menu);
        return true;
    }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_view:
				view_item(item);
				return true;
			case R.id.menu_chart:
				//chart_item(item);
				return true;
			case R.id.line_chart:
				chart_item(item);
				return true;
			case R.id.bar_chart:
				chart_item(item);
				return true;
			case R.id.pi_chart:
				chart_item(item);
				return true;
			case R.id.menu_edit:
				edit_item(item);
				return true;
			case R.id.menu_statement:
				//Toast.makeText(MainActivity.this, "ADD!", Toast.LENGTH_SHORT).show();
				menu_statement(item);
				return true;
	        case R.id.menu_search:
				menu_search(item);
				return true;
			case R.id.menu_export:
				export(item);
				return true;
			case R.id.menu_settings:
				menu_settings(item);
				return true;		
			case R.id.menu_help:
				menu_help(item);
				return true;
			case R.id.about:
				menu_about(item);
				//startActivity(new Intent(this, About.class));
				return true;
			default:
				return false;
		}
	}
	public void view_item(MenuItem item){
	
		final Dialog dialog1 = new Dialog(context);
		dialog1.setContentView(R.layout.view_dialog);
		dialog1.setTitle("View");
		final EditText selectyear = (EditText)dialog1.findViewById(R.id.viewdialogEditText);
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR); // current year
		selectyear.setText(String.valueOf(mYear));
		
		Spinner spinner1 = (Spinner) dialog1.findViewById(R.id.viewdialogSpinner);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
																			  R.array.view_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
		spinner1.setAdapter(adapter1);
		
		//addListenerOnSpinnerItemSelection();
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			    viewitem = parent.getItemAtPosition(position).toString();
				//Toast.makeText(MainActivity.this,"Spinner Item:"+ viewitem ,Toast.LENGTH_LONG).show();
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		Button viewbutton = (Button)dialog1.findViewById(R.id.viewdialogButton);
		viewbutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//year = 2017;
				String y = selectyear.getText().toString();
				year = Integer.parseInt(y);
				//Toast.makeText(MainActivity.this,"Spinner Item:"+ viewitem ,Toast.LENGTH_LONG).show();
				
				if(viewitem.equals("JANUARY")){
					month = 1;
				}else if( viewitem.equals("FEBRUARY")){
					month = 2  ;
				}else if( viewitem.equals("MARCH")){
					month = 3 ;
				}else if( viewitem.equals( "APRIL")){
					month = 4  ;
				}else if( viewitem.equals( "MAY")){
					month = 5 ;
				}else if( viewitem.equals( "JUNE")){
					month = 6 ;
				}else if( viewitem.equals( "JULY")){
					month = 7   ;
				}else if( viewitem.equals( "AUGUST")){
					month = 8   ;
				}else if( viewitem.equals( "SEPEMBER")){
					month =  9  ;
				}else if( viewitem.equals( "OCTOBER")){
					month =  10  ;
				}else if( viewitem.equals( "NOVEMBER")){
					month =  11  ;
				}else if( viewitem.equals( "DECEMBER")){
					month =  12  ;
				}else{
					
				}
				displayResultList(month,year);
				dialog1.dismiss();
			}
		});
		dialog1.show();
		
		Button viewcancelbutton = (Button)dialog1.findViewById(R.id.viewdialogcancelButton);
		viewcancelbutton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog1.dismiss();
				}
		});
					
	}
	
	public void chart_item(MenuItem item){
		if(item.getItemId() == R.id.line_chart){
			//Toast.makeText(MainActivity.this,"Item :"+"chart bar",Toast.LENGTH_LONG).show();
			chart_type = "Line_Chart";
		}else if(item.getItemId() == R.id.bar_chart){
			chart_type = "Bar_Chart";
		}else if(item.getItemId() == R.id.pi_chart){
			chart_type = "Pi_Chart";
		}
		//Toast.makeText(MainActivity.this,"Item :"+ item.getItemId(),Toast.LENGTH_LONG).show();
		final Dialog dialog1 = new Dialog(context);
		dialog1.setContentView(R.layout.view_dialog);

		
		final EditText selectyear = (EditText)dialog1.findViewById(R.id.viewdialogEditText);
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR); // current year
		selectyear.setText(String.valueOf(mYear));

		Spinner spinner1 = (Spinner) dialog1.findViewById(R.id.viewdialogSpinner);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
																			  R.array.view_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
		spinner1.setAdapter(adapter1);

		//addListenerOnSpinnerItemSelection();
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					viewitem = parent.getItemAtPosition(position).toString();
					//Toast.makeText(MainActivity.this,"Spinner Item:"+ viewitem ,Toast.LENGTH_LONG).show();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		Button viewbutton = (Button)dialog1.findViewById(R.id.viewdialogButton);
		viewbutton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//year = 2017;
					String y = selectyear.getText().toString();
					year = Integer.parseInt(y);
					//Toast.makeText(MainActivity.this,"Spinner Item:"+ viewitem ,Toast.LENGTH_LONG).show();

					if(viewitem.equals("JANUARY")){
						month = 1;
					}else if( viewitem.equals("FEBRUARY")){
						month = 2  ;
					}else if( viewitem.equals("MARCH")){
						month = 3 ;
					}else if( viewitem.equals( "APRIL")){
						month = 4  ;
					}else if( viewitem.equals( "MAY")){
						month = 5 ;
					}else if( viewitem.equals( "JUNE")){
						month = 6 ;
					}else if( viewitem.equals( "JULY")){
						month = 7   ;
					}else if( viewitem.equals( "AUGUST")){
						month = 8   ;
					}else if( viewitem.equals( "SEPEMBER")){
						month =  9  ;
					}else if( viewitem.equals( "OCTOBER")){
						month =  10  ;
					}else if( viewitem.equals( "NOVEMBER")){
						month =  11  ;
					}else if( viewitem.equals( "DECEMBER")){
						month =  12  ;
					}else{

					}
					//displayResultList(month,year);
					Intent i = new Intent(MainActivity.this, PlotChart.class);
					i.putExtra("MONTH",month);
					i.putExtra("YEAR",year);
					if(chart_type.equals("Line_Chart")){
						i.putExtra("CHART","linechart");
					}else if(chart_type.equals("Bar_Chart")){
						i.putExtra("CHART","barchart");
					}else if(chart_type.equals("Pi_Chart")){
						i.putExtra("CHART","pichart");
					}
					
					startActivity(i);
					
					dialog1.dismiss();
				}
			});
		dialog1.show();

		Button viewcancelbutton = (Button)dialog1.findViewById(R.id.viewdialogcancelButton);
		viewcancelbutton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog1.dismiss();
				}
			});
		
		//Intent i = new Intent(MainActivity.this, PlotChart.class);
		//startActivity(i);
	}
	
	public void edit_item(MenuItem item){
		final Dialog dialog2 = new Dialog(this);
		dialog2.setContentView(R.layout.edit_menu_dialog);
		dialog2.setTitle("Edit");
		//dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);//NO TITLE :)
		
		Button editButton1 = (Button) dialog2.findViewById(R.id.editmeudialogButton);
		editButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//setContentView(R.layout.edit_menu_dialog);
					
				EditText edititem = ( EditText)dialog2.findViewById(R.id.editmenudialogEditText);
				String str = edititem.getText().toString();
				if(str.equals("")){
					Toast.makeText(getApplicationContext(), "Item Id cannot be empty", Toast.LENGTH_SHORT).show();
				    dialog2.dismiss();
				}else{
					item_id = Integer.valueOf(str);
					//Log.d("item-id", "button clicked");

					EditText editDate = (EditText)findViewById(R.id.editDate);
					editDate.setFocusable(false);
					EditText editDiscription = (EditText)findViewById(R.id.editDiscription);
					EditText editAmount = (EditText)findViewById(R.id.editAmount);
					//final ListView listView = (ListView)findViewById(R.id.accountView);
					final Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
					//Toast.makeText(getApplicationContext(), "You clicked on Edit", Toast.LENGTH_SHORT).show();
					Contact con =    db.getContact( item_id);
					editDate.setText(con.getDate());
					RadioGroup radiogroup =  (RadioGroup) findViewById(R.id.radioGroup);
					RadioButton radiobutton1 =(RadioButton)radiogroup.findViewById(R.id.radio_credit);
					RadioButton radiobutton2 =(RadioButton) radiogroup.findViewById(R.id.radio_debit);
					//Toast.makeText(MainActivity.this, "Transaction Type:"+ con.getTransaction()  , Toast.LENGTH_SHORT).show();
					if(con.getTransaction().equals("Credit")){
						//radiogroup.check(R.id.radio_credit);
						radiobutton1.setChecked(true);
						//Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
						ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.credit_category_array, android.R.layout.simple_spinner_item);
						int selected = adapter.getPosition(con.getCatagory());
						spinner.setSelection(selected);
					}else if(con.getTransaction().equals("Debit")){
						//radiogroup.check(R.id.radio_debit);
						radiobutton2.setChecked(true);
						//radiobutton1.setChecked(false);
						//Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
						ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.debit_category_array, android.R.layout.simple_spinner_item);
						int selected = adapter.getPosition(con.getCatagory());
						spinner.setSelection(selected);
					}else{

					}
					editDiscription.setText(con.getDiscription());
					editAmount.setText(String.valueOf(con.getAmount()));
					//Log.d("Edit", con.getDiscription().toString());
					Button b1 =(Button)findViewById(R.id.button);
					b1.setText("Update");
					edit = 1;
					dialog2.dismiss();
				}
				
			}
		});
		dialog2.show();
		
		Button editcancelbutton = (Button)dialog2.findViewById(R.id.editmenudialogcancelButton);
		editcancelbutton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog2.dismiss();
				}
			});
	}
		
	public void export(MenuItem item){
		
		openDialog();
	
	}
	public void menu_statement(MenuItem item){

		final Dialog dialog3 = new Dialog(this);
		dialog3.setContentView(R.layout.view_statement);
		dialog3.setTitle("Statement ");
		final EditText viewyear = ( EditText)dialog3.findViewById(R.id.statementmenudialogEditText);
		Calendar c = Calendar.getInstance();
		int thisYear = c.get(Calendar.YEAR); // current year
		viewyear.setText(String.valueOf(thisYear));
		Button showbtn = (Button) dialog3.findViewById(R.id.showButton);
		
		showbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				year = Integer.valueOf(viewyear.getText().toString());
				//startActivity(new Intent(MainActivity.this, AccountDisplayActivity.class));
				Intent i = new Intent(MainActivity.this,AccountDisplayActivity.class);
				i.putExtra("KEY","Statement");
				i.putExtra("YEAR",year);
				startActivity(i);
				dialog3.dismiss();
			}
		});		
		dialog3.show();
		
		Button cancelbutton = (Button)dialog3.findViewById(R.id.statementmenudialogcancelButton);
		cancelbutton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog3.dismiss();
				}
			});
	}
	public void menu_search(MenuItem item){
    
		final Dialog dialog5 = new Dialog(context);
		dialog5.setContentView(R.layout.search);
		dialog5.setTitle("Search ");
		final EditText search_year = (EditText) dialog5.findViewById(R.id.searchYear);
		Calendar c = Calendar.getInstance();
		int thisYear = c.get(Calendar.YEAR); // current year
		search_year.setText(String.valueOf(thisYear));
		int thisMonth = c.get(Calendar.MONTH); // current year
		//search_month.setText(String.valueOf(thisYear));
		String month_str ="";
		if(thisMonth == 0){
			month_str ="JANUARY";
		}else if(thisMonth == 1){
			month_str ="FEBRUARY";
		}else if(thisMonth == 2){
			month_str ="MARCH";
		}else if(thisMonth == 3){
			month_str ="APRIL";
		}else if(thisMonth == 4){
			month_str ="MAY";
		}else if(thisMonth == 5 ){
			month_str ="JUNE";
		}else if(thisMonth == 6){
			month_str ="JULY";
		}else if(thisMonth == 7){
			month_str ="AUGUST";
		}else if(thisMonth == 8){
			month_str ="SEPTEMBER";
		}else if(thisMonth == 9){
			month_str ="OCTOBER";
		}else if(thisMonth == 10){
			month_str ="NOVEMBER";
		}else if(thisMonth == 11){
			month_str ="DECEMBER";
		}
		
		Spinner month_spinner = (Spinner) dialog5.findViewById(R.id.searchMonthSpinner);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
											 R.array.month_search_array, android.R.layout.simple_spinner_item);
		
		// Specify the layout to use when the list of choices appears
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
		month_spinner.setAdapter(adapter1);
		//int select = adapter1.getPosition(month_str);
		//month_spinner.setSelection(select);
		
       
		
		
		//addListenerOnSpinnerItemSelection();
		month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				search_month = parent.getItemAtPosition(position).toString();
				//Toast.makeText(MainActivity.this,"Spinner Item:"+ viewitem ,Toast.LENGTH_LONG).show();
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		Spinner catagory_spinner = (Spinner) dialog5.findViewById(R.id.searchCatagorySpinner);
		ArrayAdapter<CharSequence> catgoryAdaptet = ArrayAdapter.createFromResource(this,
																					R.array.category_search_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
		catgoryAdaptet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
		catagory_spinner.setAdapter(catgoryAdaptet);

		//addListenerOnSpinnerItemSelection();
		catagory_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					search_catagory = parent.getItemAtPosition(position).toString();
					//Toast.makeText(MainActivity.this,"Spinner Item:"+ viewitem ,Toast.LENGTH_LONG).show();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
			
	    Spinner amountSpinner = (Spinner) dialog5.findViewById(R.id.searchAmountSpinner);
		ArrayAdapter<CharSequence> amountAdapter = ArrayAdapter.createFromResource(this,R.array.amount_array, android.R.layout.simple_spinner_item);
		amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		amountSpinner.setAdapter(amountAdapter);
		amountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				amount_relation = parent.getItemAtPosition(position).toString();
				//Toast.makeText(MainActivity.this,"Spinner Item:"+ viewitem ,Toast.LENGTH_LONG).show();
				if(amount_relation.equals("Equal")){
					relation = "=";
				}else if(amount_relation.equals("Less Than")){
					relation = "<";
				}else if(amount_relation.equals("Greater Than")){
					relation = ">";
				}
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		dialog5.show();
		
		Button cancelbutton = (Button)dialog5.findViewById(R.id.searchdialogCancelButton);
		cancelbutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog5.dismiss();
			}
		});
		
		Button searchbutton = (Button)dialog5.findViewById(R.id.searchButton);
		searchbutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String year_str = search_year.getText().toString();
				if(!year_str.equals("")){
					year = Integer.valueOf(year_str);

					if(search_month.equals("JANUARY")){
							month = 1;
					}else if( search_month.equals("FEBRUARY")){
							month = 2  ;
					}else if( search_month.equals("MARCH")){
							month = 3 ;
					}else if( search_month.equals( "APRIL")){
							month = 4  ;
					}else if( search_month.equals( "MAY")){
							month = 5 ;
					}else if( search_month.equals( "JUNE")){
							month = 6 ;
					}else if( search_month.equals( "JULY")){
							month = 7   ;
					}else if( search_month.equals( "AUGUST")){
							month = 8   ;
					}else if( search_month.equals( "SEPEMBER")){
							month =  9  ;
					}else if( search_month.equals( "OCTOBER")){
							month =  10  ;
					}else if( search_month.equals( "NOVEMBER")){
							month =  11  ;
					}else if( search_month.equals( "DECEMBER")){
							month =  12  ;
					}else{
							month = -1;
					}
						//month = Integer.valueOf(search_month);
					CheckBox creditCheckBox = (CheckBox) dialog5.findViewById(R.id.creditCheckBox);
					if(creditCheckBox.isChecked()){
							search_credit ="Credit";
					}else{
							search_credit = "";
					}
					CheckBox debitCheckBox = (CheckBox) dialog5.findViewById(R.id.debitCheckBox);
					if(debitCheckBox.isChecked()){
							search_debit ="Debit";
					}else{
							search_debit = "";
					}

					EditText searchDiscription = (EditText) dialog5.findViewById(R.id.searchDiscription);
					search_discription = searchDiscription.getText().toString();	
					EditText searchAmount = (EditText) dialog5.findViewById(R.id.searchAmount);
					search_amount = searchAmount.getText().toString();	
						
						
					setContentView(R.layout.main);
					ListView listView2=(ListView)findViewById(R.id.accountView);
					list=new ArrayList<HashMap<String,String>>();

					//	List<Contact> contacts = db.getAllContacts();   
					List<Contact> contacts = db.getSearchResult(year, month,
						                                              search_credit, 
																	  search_debit, 
																	  search_catagory,  
																	  search_discription ,
																	  relation, 
																	  search_amount);
					for (Contact cn : contacts) {
						String str="";
						//float f = Float.parseFloat(cn.getAmount().toString());
						amt = String.format("%.2f",Float.parseFloat(cn.getAmount().toString()));
						String trans = cn.getTransaction().trim();
						if(trans.equals("Credit"))
						{
								str = "+" + amt;
						}
						else{
								str = "-" + amt;
						}

						HashMap<String,String> temp = new HashMap<String, String>();
						temp.put(FIRST_COLUMN, String.valueOf(cn.getID()));
						temp.put(SECOND_COLUMN, cn.getDate());
						temp.put(THIRD_COLUMN, cn.getDiscription());
						temp.put(FOURTH_COLUMN, str);
						list.add(temp);
					}
					ListViewAdapters adapter2 = new ListViewAdapters(MainActivity.this, list);
					listView2.setAdapter(adapter2);

					dialog5.dismiss();
						
				}else{
						Toast.makeText(MainActivity.this,"Enter year in yyyy format",Toast.LENGTH_LONG).show();
						
				}
			}
		});
	}
	public void menu_settings( MenuItem item ){
		
		final Dialog dialog4 = new Dialog(this);
		dialog4.setContentView(R.layout.theme_dialog);
		dialog4.setTitle("Settings ");
		
		dialog4.show();
		Button okbtn = (Button) dialog4.findViewById(R.id.themeDialogButton);
		okbtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
					
				RadioGroup themeradiogroup =  (RadioGroup) dialog4.findViewById(R.id.themeRadioGroup);
				if(themeradiogroup.getCheckedRadioButtonId()!=-1){
					int id= themeradiogroup.getCheckedRadioButtonId();
					View radioButton = themeradiogroup.findViewById(id);
					int radioId = themeradiogroup.indexOfChild(radioButton);
					RadioButton btn = (RadioButton) themeradiogroup.getChildAt(radioId);
					String selection = (String) btn.getText();
					trantype =selection;
					//Toast.makeText(MainActivity.this,"Theme :"+ selection,Toast.LENGTH_LONG).show();
					Toast.makeText(MainActivity.this,"Restart the app for new settings to take effect",Toast.LENGTH_LONG).show();
						
					SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE).edit();
					editor.putString("theme", selection);
					//editor.putInt("idName", 12);
					editor.commit();
				}
					
				dialog4.dismiss();
			}
		});
	}
		
	
	public void menu_help(MenuItem item){
		Intent i = new Intent(MainActivity.this,AccountDisplayActivity.class);
		i.putExtra("KEY","Help");
		startActivity(i);
	}
	public void menu_about(MenuItem item){

		final AlertDialog alertDialog = new AlertDialog.Builder(
			MainActivity.this).create();

		// Setting Dialog Title
		alertDialog.setTitle("About MyValet");
		// Setting Dialog Message
		alertDialog.setMessage("Welcome to MyValet. This is a simple application "
		+"for saving personal money transactios"
		+"\n"
		+"\n"
		+"Created by : William Doyle"
		+"\n"
		+"\n"
		+"Report bugs at myvalet.github.com");
		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.account);
		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
					//Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
					alertDialog.dismiss();
				}
			});
		alertDialog.show();
	}
	//=======""===8=========================================

	public void addItemsOnSpinner(String catagory) {
		final Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
				
		if(catagory.equals("Credit")){
			ArrayAdapter<CharSequence> credit_adapter;
			credit_adapter = ArrayAdapter.createFromResource(this,R.array.credit_category_array, android.R.layout.simple_spinner_item);
			credit_adapter.setNotifyOnChange(true);
			// Specify the layout to use when the list of choices appears
			credit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(credit_adapter);
			
		}else if(catagory.equals("Debit")){
			ArrayAdapter<CharSequence> debit_adapter;
			// Create an ArrayAdapter using the string array and a default spinner layout
			debit_adapter = ArrayAdapter.createFromResource(this,R.array.debit_category_array, android.R.layout.simple_spinner_item);
			debit_adapter.setNotifyOnChange(true);
			// Specify the layout to use when the list of choices appears
			debit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(debit_adapter);
			
		}
       																			      
	}
	public void addItemsOnViewSpinner() {
		final Spinner spinner1 = (Spinner) findViewById(R.id.viewdialogSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
																			 R.array.view_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
		spinner1.setAdapter(adapter1);
	}
	public void addListenerOnSpinnerItemSelection() {
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			catagory = parent.getItemAtPosition(position).toString();
		}
		public void onNothingSelected(AdapterView<?> parent) {
		}
	});
	
    }
	private void displayResultList(int month, int year) {
		/*
		ArrayAdapter<String> adapter =new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, result);
		//ArrayAdapter<String> adapter =new ArrayAdapter<String>(MainActivity.this,  android.R.layout.list_row, R.id.list_row_name, result);
		ListView listView = (ListView)findViewById(R.id.accountView);
		//layoutinflater = getLayoutInflater();
		//ViewGroup header = (ViewGroup)layoutinflater.inflate(R.layout.item_header,listView,false);
		//listView.addHeaderView(header);
		listView.setAdapter(adapter);
		**/
		//===== coloumn list view===========================
		ListView listView2=(ListView)findViewById(R.id.accountView);
		list=new ArrayList<HashMap<String,String>>();

		//	List<Contact> contacts = db.getAllContacts();   
		List<Contact> contacts = db.getSelectedMonthAccount(Integer.valueOf(month), Integer.valueOf(year));   
		for (Contact cn : contacts) {
			String str="";
			//float f = Float.parseFloat(cn.getAmount().toString());
			amt = String.format("%.2f",Float.parseFloat(cn.getAmount().toString()));
			String trans = cn.getTransaction().trim();
			if(trans.equals("Credit")){
				str = "+" + amt;
			}
			else{
				str = "-" + amt;
			}

			HashMap<String,String> temp = new HashMap<String, String>();
			temp.put(FIRST_COLUMN, String.valueOf(cn.getID()));
			temp.put(SECOND_COLUMN, cn.getDate());
			temp.put(THIRD_COLUMN, cn.getDiscription());
			temp.put(FOURTH_COLUMN, str);
			list.add(temp);
		}
		ListViewAdapters adapter2=new ListViewAdapters(this, list);
		listView2.setAdapter(adapter2);
		
		float c = db.getMonthlyCredit(month, year);
		float d = db.getMonthlyDebit(month, year);
        Log.d("credit",String.valueOf(db.getSumOfCredit()));
		TextView creditView = (TextView)findViewById(R.id.creditView);
		creditView.setText("+"+String.valueOf(c));
		TextView debitView = (TextView)findViewById(R.id.debitView);
		debitView.setText("-"+String.valueOf(d));
		balance = c - d;

		TextView balanceView = (TextView)findViewById(R.id.balanceView);
		String str="";
		if(balance > 0.0){
			balanceView.setTextColor(Color.parseColor("#7FF218"));
			str = "+"+String.valueOf(balance);
		}else{
			balanceView.setTextColor(Color.parseColor("#F88245"));
			str = "-"+String.valueOf(balance);
		}
		balanceView.setText(str);
		//balance = credit - debit;
		//Toast.makeText(MainActivity.this, "Balance: "+String.valueOf(credit), Toast.LENGTH_SHORT).show();  
		
		listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id){
				final Dialog dialog2 = new Dialog(context);
				dialog2.setContentView(R.layout.edit_menu_dialog);
				//dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);//NO TITLE :)

				Button editButton1 = (Button) dialog2.findViewById(R.id.editmeudialogButton);
				editButton1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//setContentView(R.layout.edit_menu_dialog);

						EditText edititem = ( EditText)dialog2.findViewById(R.id.editmenudialogEditText);
						item_id = Integer.valueOf(edititem.getText().toString());
						//Log.d("item-id", "button clicked");

						EditText editDate = (EditText)findViewById(R.id.editDate);
						editDate.setFocusable(false);
						EditText editDiscription = (EditText)findViewById(R.id.editDiscription);
						EditText editAmount = (EditText)findViewById(R.id.editAmount);
						//final ListView listView = (ListView)findViewById(R.id.accountView);
						final Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
						//Toast.makeText(getApplicationContext(), "You clicked on Edit", Toast.LENGTH_SHORT).show();
						Contact con =    db.getContact( item_id);
						editDate.setText(con.getDate());
						RadioGroup radiogroup =  (RadioGroup) findViewById(R.id.radioGroup);
						RadioButton radiobutton1 =(RadioButton)radiogroup.findViewById(R.id.radio_credit);
						RadioButton radiobutton2 =(RadioButton) radiogroup.findViewById(R.id.radio_debit);
						//Toast.makeText(MainActivity.this, "Transaction Type:"+ con.getTransaction()  , Toast.LENGTH_SHORT).show();
						if(con.getTransaction().equals("Credit")){
							//radiogroup.check(R.id.radio_credit);
							radiobutton1.setChecked(true);
							//radiobutton2.setChecked(false);
							//Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
							ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
																 R.array.credit_category_array, android.R.layout.simple_spinner_item);
							int selected = adapter.getPosition(con.getCatagory());
							spinner.setSelection(selected);
							
						}else if(con.getTransaction().equals("Debit")){
							//radiogroup.check(R.id.radio_debit);
							radiobutton2.setChecked(true);
							//radiobutton1.setChecked(false);
						    //Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
							ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
																 R.array.debit_category_array, android.R.layout.simple_spinner_item);
							int selected = adapter.getPosition(con.getCatagory());
							spinner.setSelection(selected);
							
						}else{

						}
						
						editDiscription.setText(con.getDiscription());
						editAmount.setText(String.valueOf(con.getAmount()));
						//Log.d("Edit", con.getDiscription().toString());
						Button b1 =(Button)findViewById(R.id.button);
						b1.setText("Update");
						edit = 1;

						dialog2.dismiss();
					}
				});
			dialog2.show();

			Button editcancelbutton = (Button)dialog2.findViewById(R.id.editmenudialogcancelButton);
			editcancelbutton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog2.dismiss();
				}
			});
		}
	});	
  }
  private void openDialog(){
       LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
       View subView = inflater.inflate(R.layout.export_dialog, null);
       final CheckBox yesCheckBox = (CheckBox)subView.findViewById(R.id.yesCheckBox);
	   final CheckBox noCheckBox = (CheckBox)subView.findViewById(R.id.noCheckBox);
	    
	   yesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
		 @Override
		 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		 {
			 if ( isChecked ){
			   noCheckBox.setChecked(false);
			 }

		  }
		});
	    noCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
		  @Override
		  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
		    if ( isChecked ){
			  yesCheckBox.setChecked(false);
			}
	      }
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog");
        builder.setMessage("Do you want to delete all datas after export?");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
					//textInfo.setText(subEditText.getText().toString());
		        if(yesCheckBox.isChecked()){
					//Toast.makeText(MainActivity.this, "All datas will be permanently lost!", Toast.LENGTH_LONG).show();
				    export();
					//=====Deleting the datas from table==============
					db.deletetable();
					  
			    }else if(noCheckBox.isChecked()){
					//Toast.makeText(MainActivity.this, "All datas will be retained!", Toast.LENGTH_LONG).show();
					export();
				}
			}
		});

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
			}
		});

        builder.show();
    }
	public void export(){
		
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but all we need
			//  to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		String data="";

		try {
			File newFolder = new File(Environment.getExternalStorageDirectory(), "MyValet");
			if (!newFolder.exists()) {
				newFolder.mkdir();
			}
			try {
				File file = new File(newFolder, "MyValet" + ".csv");
				file.createNewFile();
				FileOutputStream fOut = new FileOutputStream(file);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				List<Contact> contacts = db.getAllContacts();   
				for (Contact cn : contacts) {
					data = cn.getID()+","+ cn.getDate()+","+cn.getTransaction()
						+","+cn.getCatagory()+",\""+cn.getDiscription()+"\","+cn.getAmount()+"\n";
					myOutWriter.append(data);
				}
				myOutWriter.close();

				fOut.flush();
				fOut.close();

			} catch (Exception ex) {
				//System.out.println("ex: " + ex);
				Log.d("MyValet","ex:",ex);
			}
		} catch (Exception e) {
			System.out.println("e: " + e);
		}
		Toast.makeText(MainActivity.this,"Savrd to internal storage folder MyValet as MyValet.csv",Toast.LENGTH_LONG).show();
	}
}
		
	

