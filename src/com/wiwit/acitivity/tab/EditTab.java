package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.util.MyApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class EditTab extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_word_layout);
	}

	protected MyApp getAppState() {
		return ((MyApp) this.getApplicationContext());
	}

	protected SQLiteDatabase getSQLite() {
		return getAppState().getSd();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								EditTab.this.finish();
							}
						}).setNegativeButton("No", null).show();
	}
}
