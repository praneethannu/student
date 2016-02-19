package com.azim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBrodcast extends BroadcastReceiver {

	
	       @Override
	          public void onReceive(Context context, Intent intent) {
	             if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
	                Log.e("", "ACTION_DATE_CHANGED received");
	             }
	         }
	    
		
		
			

		

	}


