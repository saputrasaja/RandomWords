package com.wiwit.acitivity.tab;

import java.util.HashMap;

import com.wiwit.all.R;
import com.wiwit.connection.DataBase;
import com.wiwit.connection.Setting;
import com.wiwit.connection.Word;
import com.wiwit.connection.WordUtil;
import com.wiwit.util.DebugHelper;
import com.wiwit.util.MyApp;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainTab extends TabActivity {
	protected TabHost tabHost;
	protected long transactionID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams attrs = this.getWindow().getAttributes();
		attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		this.getWindow().setAttributes(attrs);
		setContentView(R.layout.tab);
		initFirst();

		/* TabHost will have Tabs */
		tabHost = (TabHost) findViewById(android.R.id.tabhost);

		/*
		 * TabSpec used to create a new tab. By using TabSpec only we can able
		 * to setContent to the tab. By using TabSpec setIndicator() we can set
		 * name to tab.
		 */

		/* tid1 is firstTabSpec Id. Its used to access outside. */
		TabSpec newWordSpec = tabHost.newTabSpec("new");
		TabSpec oldWordSpec = tabHost.newTabSpec("old");
		TabSpec editTabSpec = tabHost.newTabSpec("edit");
		TabSpec infoTabSpec = tabHost.newTabSpec("info");
		TabSpec allTabSpec = tabHost.newTabSpec("all");
		TabSpec delTabSpec = tabHost.newTabSpec("all");

		/* TabSpec setIndicator() is used to set name for the tab. */
		/* TabSpec setContent() is used to set content for a particular tab. */
		newWordSpec.setIndicator("New").setContent(
				new Intent(this, NewWordTab.class));
		oldWordSpec.setIndicator("Old").setContent(
				new Intent(this, OldWordTab.class));
		infoTabSpec.setIndicator("Info").setContent(
				new Intent(this, InfoAppTab.class));
		editTabSpec.setIndicator("Edit").setContent(
				new Intent(this, EditTab.class));
		allTabSpec.setIndicator("All").setContent(
				new Intent(this, AllWordTab.class));
		delTabSpec.setIndicator("Del").setContent(
				new Intent(this, DelWordTab.class));
		/* Add tabSpec to the TabHost to display. */
		tabHost.addTab(infoTabSpec);
		tabHost.addTab(newWordSpec);
		tabHost.addTab(oldWordSpec);
		tabHost.addTab(delTabSpec);
		tabHost.addTab(editTabSpec);
		tabHost.addTab(allTabSpec);
	}

	public void setTransactionID(long l) {
		transactionID = l;
	}

	public void switchTabSpecial(int tab) {
		tabHost.setCurrentTab(tab);
	}

	protected MyApp getAppState() {
		return ((MyApp) this.getApplicationContext());
	}

	protected SQLiteDatabase getSQLite() {
		return getAppState().getSd();
	}

	protected void initFirst() {
		MyApp appState = ((MyApp) this.getApplicationContext());
		appState.setSd(openOrCreateDatabase(DataBase.DATABASE_NAME,
				MODE_WORLD_WRITEABLE, null));
		// START INIT FIRST
		DataBase.initWordFromLocal(getSQLite());
		
		// END INIT
		getAppState().setAllRow(Word.getAllRow(getSQLite()));
		// testUpdate();
		checkGlobalVariable();
	}

	protected void checkGlobalVariable() {
		MyApp appState = ((MyApp) this.getApplicationContext());
		DebugHelper.debug("is AppState != null : " + (appState != null));
		DebugHelper.debug("is SQLite != null : " + (appState.getSd() != null));
		DebugHelper.debug("is allRowe != null : "
				+ (appState.getAllRow() != null));
	}
}
