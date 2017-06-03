package com.mycompany.myvalet;

public class Contact {

    //private variables
    int _id;
    String _date;
    String _transaction;
	String _catagory;
	String _discription;
	Float _amount;

    // Empty constructor
    public Contact(){

    }
    // constructor
    public Contact(int id, String date, String transaction, 
	          String catagory, String discription, Float amount){
        this._id = id;
        this._date = date;
        this._transaction = transaction;
		this._catagory = catagory;
		this._discription = discription;
		this._amount = amount;
		
    }

    // constructor
    public Contact(String date, String transaction, 
				   String catagory, String discription, Float amount){
        this._date = date;
        this._transaction = transaction;
		this._catagory = catagory;
		this._discription = discription;
		this._amount = amount;

    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting date
    public String getDate(){
        return this._date;
    }

    // setting date
    public void setDate(String date){
        this._date = date;
    }
	
	//getting transaction
    public String getTransaction(){
        return this._transaction;
    }

    // setting transaction
    public void setTransaction(String transaction){
        this._transaction = transaction;
    }

    // getting catagory
    public String getCatagory(){
        return this._catagory;
    }

    // setting catagory
    public void setCatagory(String catagory){
        this._catagory = catagory;
    }
	
	// getting discription
    public String getDiscription(){
        return this._discription
		;
    }

    // setting discription
    public void setDiscription(String discription){
        this._discription = discription;
    }
	// getting amount
    public Float getAmount(){
        return this._amount;
    }

    // setting amount
    public void setAmount(Float amount){
        this._amount = amount;
    }
}
