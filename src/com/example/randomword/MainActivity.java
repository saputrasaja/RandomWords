package com.example.randomword;

import com.wiwit.connection.DataBase;
import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import com.wiwit.all.R;
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		SQLiteDatabase sd = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
//		DataBase.initAll(sd);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
