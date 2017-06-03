package com.mycompany.myvalet;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.BufferedReader; 
import java.io.DataInputStream; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.util.Calendar;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
	//String namedb = String.valueOf(new Calendar(calender.getInstance().get(Calendar.YEAR)));
    private static final String DATABASE_NAME = "myvalet.db";

    // Contacts table name
    private static final String TABLE_NAME = "transactions";

    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_DATE= "date";
    private static final String KEY_TRANSACTION = "transaction_type";
	private static final String KEY_CATAGORY = "catagory";
	private static final String KEY_DISCRIPTION = "discription";
	private static final String KEY_AMOUNT = "amount";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
		
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + "  TEXT ,"
			+ KEY_TRANSACTION + " TEXT," + KEY_CATAGORY+" TEXT,"
			+ KEY_DISCRIPTION + " TEXT," + KEY_AMOUNT + " FLOAT" +  ")";
        	db.execSQL(CREATE_TRANSACTION_TABLE);
		//Log.d("Dtabase Handler ", "Created DB..");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, contact.getDate()); 
        values.put(KEY_TRANSACTION, contact.getTransaction()); 
		values.put(KEY_CATAGORY, contact.getCatagory());
		values.put(KEY_DISCRIPTION, contact.getDiscription());
		values.put(KEY_AMOUNT, contact.getAmount());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,KEY_DATE, KEY_TRANSACTION,
									 KEY_CATAGORY, KEY_DISCRIPTION, KEY_AMOUNT}, KEY_ID + "=?",
								 new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
									  cursor.getString(1), cursor.getString(2),
									   cursor.getString(3), cursor.getString(4),
									   cursor.getFloat(5));
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
	     List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
    
		Log.d("Getting all contacts","Getting all contacts");
		
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
				contact.setTransaction(cursor.getString(2));
				contact.setCatagory(cursor.getString(3));
				contact.setDiscription(cursor.getString(4));
                contact.setAmount(cursor.getFloat(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, contact.getDate());
        values.put(KEY_TRANSACTION, contact.getTransaction());
		values.put(KEY_CATAGORY,contact.getCatagory());
		values.put(KEY_DISCRIPTION, contact.getDiscription());
		values.put(KEY_AMOUNT, contact.getAmount());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
						 new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_NAME, KEY_ID + " = ?",
			//  new String[] { String.valueOf(contact.getID()) });
	    db.delete(TABLE_NAME, KEY_ID + " = ?",
				  new String[] { String.valueOf( contact.getID())});
		
	    //db.execSQL("DELETE FROM SQLITE_SEQUENCE  WHERE NAME = '"+TABLE_NAME+"'");
		
		//db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
		
		db.execSQL("DROP TABLE IF EXISTS test2");
		db.execSQL("CREATE TABLE test2 AS SELECT  *  FROM "+ TABLE_NAME);
		db.execSQL("DELETE FROM " + TABLE_NAME);
		db.execSQL("DELETE FROM sqlite_sequence WHERE name=' "+TABLE_NAME+"'");
		//db.execSQL("INSERT INTO " + TABLE_NAME+ " (value) SELECT value FROM test2");
		db.execSQL("INSERT INTO " + TABLE_NAME+ " (_id, date, transaction_type, catagory,discription, amount ) SELECT * FROM test2");
		db.execSQL("DROP TABLE test2");
		
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }
	
	// ====Geting sum of credit
	public float getSumOfCredit(){
	
		float credit=0;
        String creditQuery = "SELECT   SUM( amount )   FROM " + TABLE_NAME 
			+ " WHERE transaction_type = 'Credit'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(creditQuery,null);
		if(c.moveToFirst()){
			credit = c.getFloat(0);
		}
		return credit;
		
	}
	// ====Geting sum of debit
	public float getSumOfDebit(){

		float debit=0;
        String creditQuery = "SELECT   SUM( amount )   FROM " + TABLE_NAME 
			+ " WHERE transaction_type = 'Debit'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(creditQuery,null);
		if(c.moveToFirst()){
			debit = c.getFloat(0);
		}
		db.close();
		return debit;

	}
	//====="Getting monthly statement===="==
	public float getMonthlyCredit(int month, int year){

		String m = String.valueOf(month ).length()  == 1 ? "0"+String.valueOf(month):String.valueOf(month)  ;
		String y = String.valueOf(year);
		float credit = 0;
        String creditQuery = "SELECT  SUM(amount )  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+y+"-"+m+"' AND  transaction_type = 'Credit'";
		//String creditQuery = "SELECT  SUM(amount )  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '2017-01' AND  transaction_type = 'Credit'";
		
		//String creditQuery = "SELECT   SUM( amount )   FROM " + TABLE_NAME 
			//+ " WHERE transaction_type = 'Credit'";
		//String creditQuery = "SELECT  amount   FROM "+TABLE_NAME+" WHERE date= '25-12-2017' ";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(creditQuery,null);
		if(c.moveToFirst()){
			credit = c.getFloat(0);
		}
		db.close();
		return credit;

	}
	//=====Getting monthly credit details===================================
	public float getMonthlyDetails(int month, int year, String transaction, String catagory){

		String trans = transaction;
		String cat = catagory;
		String m = String.valueOf(month ).length()  == 1 ? "0"+String.valueOf(month):String.valueOf(month)  ;
		String y = String.valueOf(year);
		float detail_amt = 0;
        String detailQuery = "SELECT  SUM(amount )  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+y+"-"+m+"' AND  transaction_type = '" + trans + "' AND catagory = '" + cat + "'";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(detailQuery,null);
		if(c.moveToFirst()){
			detail_amt = c.getFloat(0);
		}
		db.close();
		return detail_amt;
	}
	
	//======Geting monthly debit=============================================
	public float getMonthlyDebit(int month, int year){

		String m = String.valueOf(month ).length()  == 1 ? "0"+String.valueOf(month):String.valueOf(month)  ;
		String y = String.valueOf(year);
		float debit = 0;
        String debitQuery = "SELECT  SUM(amount)  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+y+"-"+m+"' AND  transaction_type = 'Debit'";
		//String creditQuery = "SELECT   SUM( amount )   FROM " + TABLE_NAME 
		//+ " WHERE transaction_type = 'Credit'";
		//String creditQuery = "SELECT  amount   FROM "+TABLE_NAME+" WHERE date= '25-12-2017' ";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(debitQuery,null);
		if(c.moveToFirst()){
			debit = c.getFloat(0);
		}
		db.close();
		return debit;

	}
	//=== Getting a particular mounth account======================
	public List<Contact  > getSelectedMonthAccount(int month, int year){
		String m = String.valueOf(month ).length()  == 1 ? "0"+String.valueOf(month):String.valueOf(month)  ;
		String y = String.valueOf(year);
		List<Contact> accountList = new ArrayList<Contact>();
		String selectQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+y+"-"+m+"' ";
		
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

		Log.d("Getting all contacts","Getting all contacts");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
				contact.setTransaction(cursor.getString(2));
				contact.setCatagory(cursor.getString(3));
				contact.setDiscription(cursor.getString(4));
                contact.setAmount(cursor.getFloat(5));
                // Adding contact to list
                accountList.add(contact);
            } while (cursor.moveToNext());
        }
		return accountList;
	}
	//====Catagory details==="=====================================
	
	public float getCatagoryDetails(int month,int year, String catagory){
		String m = String.valueOf(month ).length()  == 1 ? "0"+String.valueOf(month):String.valueOf(month)  ;
		String y = String.valueOf(year);
		
		float s=0;
		String catagoryQuery = "SELECT  SUM(amount )  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+y+"-"+m+"' AND  transaction_type = 'Debit' AND catagory = '"+catagory+"' ";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(catagoryQuery,null);
		if(c.moveToFirst()){
			s = c.getFloat(0);
			Log.d("Catagory Food:", String.valueOf(s));
			
		}
		db.close();
		return s;
	}
	// =======coustom search =============================
	
	
	public List<Contact> getSearchResult(int year, 
	                                     int month, 
										 String credit, 
										 String debit, 
										 String catagory, 
										 String discription, 
										 String relation,
										 String amount){
		List<Contact> contactList = new ArrayList<Contact>();
		String m = String.valueOf(month ).length()  == 1 ? "0"+String.valueOf(month):String.valueOf(month)  ;
		String y = String.valueOf(year);
		String searchQuery = "";
		/*
		searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y', date) = '"+ y +"' ";
		if(month != -1){
			searchQuery = searchQuery.concat( "-"+m+"' ");
		}
		if(!credit.equals("") ){
			searchQuery = searchQuery.concat(
		}
		*/
        if(year != 0 & month == -1 & credit.equals("") & debit.equals("") & catagory.equals("") & discription.equals("") & relation.equals("") & amount.equals("")){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y', date) = '"+ y +"' ";
		}else if(year != 0 & month == -1 & credit.equals("Credit") & debit.equals("") & catagory.equals("") & discription.equals("")  & relation.equals("") & amount.equals("")){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y', date) = '"+ y +"' AND  transaction_type= 'Credit' ";
		}else if(year != 0 & month == -1 & credit.equals("") & debit.equals("Debit") & catagory.equals("") & discription.equals("")   & relation.equals("") & amount.equals("")  ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y', date) = '"+ y +"' AND  transaction_type= 'Debit' ";
		}else if(year != 0 & month == -1 & credit.equals("") & debit.equals("") & !catagory.equals("") & discription.equals("")  & relation.equals("") & amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y', date) = '"+ y +"' AND  catagory = '"+ catagory +"'";
		}else if(year != 0 & month == -1 & credit.equals("") & debit.equals("") & catagory.equals("") & !discription.equals("")  & relation.equals("") & amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y', date) = '"+ y +"' AND  discription = '"+ discription +"'";
		}else if(year != 0 & month == -1 & credit.equals("") & debit.equals("") & catagory.equals("") & discription.equals("")  & !relation.equals("") & !amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y', date) = '"+ y +"' AND  amount "+ relation +"  "+ amount ;
		}else if(year != 0 & month == -1 & credit.equals("Credit") & debit.equals("") & catagory.equals("") & discription.equals("")  & !relation.equals("") & !amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y', date) = '"+ y +"'  AND   transaction_type = 'Credit' AND  amount "+ relation +"  "+ amount ;
		}else if(year != 0 & month == -1 & credit.equals("") & debit.equals("Debit") & catagory.equals("") & discription.equals("")  & !relation.equals("") & !amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y', date) = '"+ y +"'  AND   transaction_type = 'Debit' AND  amount "+ relation +"  "+ amount ;
		}
		else if(year != 0 & month != -1 & credit.equals("") & debit.equals("") & catagory.equals("") & discription.equals("")  & relation.equals("") & amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"' ";
		}else if(year != 0 & month != -1 & credit.equals("Credit") & debit.equals("") & catagory.equals("") & discription.equals("")  & relation.equals("") & amount.equals("")  ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"' AND  transaction_type = 'Credit'";
		}else if(year != 0 & month != -1 & credit.equals("") & debit.equals("Debit") & catagory.equals("") & discription.equals("") & relation.equals("") & amount.equals("")  ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"' AND  transaction_type = 'Debit'";
		}else if(year != 0 & month != -1 & credit.equals("Credit") & debit.equals("") & !catagory.equals("") & discription.equals("")  & relation.equals("") & amount.equals("")  ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"' AND  transaction_type = 'Credit' AND  catagory = '"+ catagory +"'" ;
		}else if(year != 0 & month != -1 & credit.equals("") & debit.equals("Debit") & !catagory.equals("") & discription.equals("") & relation.equals("") & amount.equals("")  ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"' AND  transaction_type = 'Debit'  AND  catagory = '"+ catagory +"'"  ;
		}
		else if(year != 0 & month != -1 & credit.equals("Credit") & debit.equals("") & catagory.equals("") & discription.equals("")  & !relation.equals("") & !amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"'  AND   transaction_type = 'Credit' AND  amount "+ relation +"  "+ amount ;
		}else if(year != 0 & month != -1 & credit.equals("") & debit.equals("Debit") & catagory.equals("") & discription.equals("")  & !relation.equals("") & !amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"'  AND   transaction_type = 'Debit' AND  amount "+ relation +"  "+ amount ;
		}else if(year != 0 & month != -1 & credit.equals("Credit") & debit.equals("") & catagory.equals("") & discription.equals("")  & !relation.equals("") & !amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"'  AND   transaction_type = 'Credit'   AND  catagory = '"+ catagory +"'  AND  amount "+ relation +"  "+ amount ;
		}else if(year != 0 & month != -1 & credit.equals("") & debit.equals("Debit") & !catagory.equals("") & discription.equals("")  & !relation.equals("") & !amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"'  AND   transaction_type = 'Debit'  AND  catagory = '"+ catagory +"' AND  amount "+ relation +"  "+ amount ;
		}else if(year != 0 & month != -1 & credit.equals("Credit") & debit.equals("Debit") & !catagory.equals("") & discription.equals("")  & !relation.equals("") & !amount.equals("")   ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"'  AND  catagory = '"+ catagory +"' AND  amount "+ relation +"  "+ amount ;
		}
		else if(year != 0 & month != -1 & credit.equals("") & debit.equals("") & !catagory.equals("") & discription.equals("")  & relation.equals("") & amount.equals("") ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"' AND  catagory = '"+ catagory +"'";
		}else if(year != 0 & month != -1 & credit.equals("") & debit.equals("") & catagory.equals("") & !discription.equals("") & relation.equals("") & amount.equals("")    ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"' AND  discription = '"+ discription + "'";
		}else if(year != 0 & month != -1 & credit.equals("Credit") & debit.equals("Debit") & catagory.equals("") & !discription.equals("")  & relation.equals("") & amount.equals("")  ){
			searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+ y +"-"+m+"'   ";
		}
        
		
				
       // String searchQuery = "SELECT  *  FROM "+TABLE_NAME+" WHERE strftime('%Y-%m', date) = '"+y+"-"+m+"' AND  transaction_type = 'Debit'";
		
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(searchQuery, null);

		//Log.d("Getting all contacts","Getting all contacts");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDate(cursor.getString(1));
				contact.setTransaction(cursor.getString(2));
				contact.setCatagory(cursor.getString(3));
				contact.setDiscription(cursor.getString(4));
                contact.setAmount(cursor.getFloat(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
      }
	  public void deletetable(){
		  SQLiteDatabase db = this.getWritableDatabase();
		  db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		  //db.delete(TABLE_NAME,null,null);
		  String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				  + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + "  TEXT ,"
				  + KEY_TRANSACTION + " TEXT," + KEY_CATAGORY+" TEXT,"
				  + KEY_DISCRIPTION + " TEXT," + KEY_AMOUNT + " FLOAT" +  ")";
		  db.execSQL(CREATE_TRANSACTION_TABLE);
		  db.close();
		  
	  }
}
