package com.mycompany.myvalet;

import java.util.List; 
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.*;


public class SpinnerActivity extends Activity implements OnItemSelectedListener {
   
    
	
    public void onItemSelected(AdapterView<?> parent, View view,
							   int pos, long id) {
        // An item was selected. You can retrieve the selected item using
         parent.getItemAtPosition(pos);
		String item = parent.getItemAtPosition(pos).toString();
		
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
