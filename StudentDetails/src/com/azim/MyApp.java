package com.azim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MyApp extends Activity implements OnClickListener, LocationListener {
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	EditText editRollno, editName, editMarks;
	Button btnAdd, btnDelete, btnModify, btnView, btnViewAll, btnShowInfo, btn_async, Button1;
	SQLiteDatabase db;
	static final int PICK_CONTACT_REQUEST = 1; // The request code
	
	private LocationManager locationManager;
	Location location; // location
	double latitude; // latitude
	double longitude; // longitude
	//longitude and lattitude

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, // 3
																						// sec
				10, this);
		if (locationManager != null) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				System.out.println(latitude + "please show the output" + longitude);
			}
		}
		editRollno = (EditText) findViewById(R.id.editRollno);
		editName = (EditText) findViewById(R.id.editName);
		editMarks = (EditText) findViewById(R.id.editMarks);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnModify = (Button) findViewById(R.id.btnModify);
		btnView = (Button) findViewById(R.id.btnView);
		btnViewAll = (Button) findViewById(R.id.btnViewAll);
		btnShowInfo = (Button) findViewById(R.id.btnShowInfo);
		Button1 = (Button) findViewById(R.id.button1);
		Button1.setOnClickListener(this);

		btnAdd.setOnClickListener(this);

		btn_async = (Button) findViewById(R.id.btn_async);
		btn_async.setOnClickListener(this);

		btnDelete.setOnClickListener(this);
		btnModify.setOnClickListener(this);
		btnView.setOnClickListener(this);
		btnViewAll.setOnClickListener(this);
		btnShowInfo.setOnClickListener(this);
		db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
	}

	@SuppressLint("NewApi")
	public void onClick(View view) {
		if (view == btnAdd) {

			if (editRollno.getText().toString().trim().length() == 0
					|| editName.getText().toString().trim().length() == 0
					|| editMarks.getText().toString().trim().length() == 0) {
				showMessage("Error", "Please enter all values");
				return;
			}
			db.execSQL("INSERT INTO student VALUES('" + editRollno.getText() + "','" + editName.getText() + "','"
					+ editMarks.getText() + "');");
			showMessage("Success", "Record added");
			clearText();

		}
		if (view == btnDelete) {
			if (editRollno.getText().toString().trim().length() == 0) {
				showMessage("Error", "Please enter Rollno");
				return;
			}
			Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollno.getText() + "'", null);
			if (c.moveToFirst()) {
				db.execSQL("DELETE FROM student WHERE rollno='" + editRollno.getText() + "'");
				showMessage("Success", "Record Deleted");
			} else {
				showMessage("Error", "Invalid Rollno");
			}
			clearText();
		}
		if (view == btnModify) {
			if (editRollno.getText().toString().trim().length() == 0) {
				showMessage("Error", "Please enter Rollno");
				return;
			}
			Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollno.getText() + "'", null);
			if (c.moveToFirst()) {
				db.execSQL("UPDATE student SET name='" + editName.getText() + "',marks='" + editMarks.getText()
						+ "' WHERE rollno='" + editRollno.getText() + "'");
				showMessage("Success", "Record Modified");
			} else {
				showMessage("Error", "Rollno# Not found");
			}
			clearText();
		}
		if (view == btnView) {
			if (editRollno.getText().toString().trim().length() == 0) {
				showMessage("Error", "Please enter Rollno");
				return;
			}
			Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollno.getText() + "'", null);
			if (c.moveToFirst()) {
				editName.setText(c.getString(1));
				editMarks.setText(c.getString(2));
			} else {
				showMessage("Error", "Rollno# Not found");
				clearText();
			}
		}
		if (view == btnViewAll) {
			Cursor c = db.rawQuery("SELECT * FROM student", null);
			if (c.getCount() == 0) {
				showMessage("Error", "No records found");
				return;
			}
			StringBuffer buffer = new StringBuffer();
			while (c.moveToNext()) {
				buffer.append("Rollno: " + c.getString(0) + "\n");
				buffer.append("Name: " + c.getString(1) + "\n");
				buffer.append("Marks: " + c.getString(2) + "\n\n");
			}
			showMessage("Student Details", buffer.toString());
		}
		if (view == btnShowInfo) {
			showMessage("Student Management Application", "Demo class");
		}

		if (view == Button1) {
			// Intent intent = new Intent(MyApp.this,
			// ServiceTest.class);
			// startActivity(intent);

			//Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			//startActivityForResult(intent,0);
			
			{
			    Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
			    pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
			    startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
			}
		}

		if (view == btn_async) {
			System.out.println("Download url ");

			// My handler
			// Handler requestHandler = new Handler() {

			// public void handleMessage(Message message) {

			// if (message.arg1 == 99)
			// {
			// String user_message =
			// message.getData().getString("user_message");
			// Log.d("Hi ", user_message);
		}
		// }
		// };

		// MyAsync dd = new MyAsync(MyApp.this, requestHandler);
		// dd.execute("http://www.pcworld.com/index.rss");

		PraneethTestAsync pp = new PraneethTestAsync(this);
		pp.execute("");

	}
	// }

	private void pickContact() {
		// TODO Auto-generated method stub
		
	}

	public void showMessage(String title, String message) {
		Builder builder = new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}

	public void clearText() {
		editRollno.setText("");
		editName.setText("");
		editMarks.setText("");
		editRollno.requestFocus();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}