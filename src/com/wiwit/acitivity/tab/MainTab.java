package com.wiwit.acitivity.tab;

import java.util.HashMap;

import com.wiwit.all.R;
import com.wiwit.connection.DataBase;
import com.wiwit.connection.Word;
import com.wiwit.connection.WordUtil;
import com.wiwit.util.DebugHelper;
import com.wiwit.util.MyApp;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainTab extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		initFirst();

		/* TabHost will have Tabs */
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

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

		/* TabSpec setIndicator() is used to set name for the tab. */
		/* TabSpec setContent() is used to set content for a particular tab. */
		newWordSpec.setIndicator("New Word").setContent(
				new Intent(this, NewWordTab.class));
		oldWordSpec.setIndicator("Old Word").setContent(
				new Intent(this, OldWordTab.class));
		editTabSpec.setIndicator("Edit Word").setContent(
				new Intent(this, OldWordTab.class));
		infoTabSpec.setIndicator("Info").setContent(
				new Intent(this, InfoAppTab.class));

		/* Add tabSpec to the TabHost to display. */
		tabHost.addTab(infoTabSpec);
		tabHost.addTab(newWordSpec);
		tabHost.addTab(oldWordSpec);
		tabHost.addTab(editTabSpec);
	}

	protected MyApp getAppState() {
		return ((MyApp) this.getApplicationContext());
	}

	protected SQLiteDatabase getSQLite() {
		return getAppState().getSd();
	}

	protected void initFirstlyLauncth() {
		DataBase.initWordFromLocal(getSQLite());
	}

	protected void initFirst() {
		MyApp appState = ((MyApp) this.getApplicationContext());
		appState.setSd(openOrCreateDatabase(DataBase.DATABASE_NAME,
				MODE_PRIVATE, null));
		initFirstlyLauncth();
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

	protected void testUpdate() {
		HashMap<String, Word> wordMap = getAppState().getAllRow();
		DebugHelper.debug("indonesian : "
				+ wordMap.get("accuse").getIndonesianWord());
		DebugHelper
				.debug("english : " + wordMap.get("accuse").getEnglishWord());
		DebugHelper.debug("state : " + wordMap.get("accuse").getState());
		DebugHelper.debug("isHaveReadNew : "
				+ wordMap.get("accuse").isHaveReadNew());
		DebugHelper.debug("isHaveReadOld : "
				+ wordMap.get("accuse").isHaveReadOld());

		Word w = wordMap.get("accuse");
		w.setIndonesianWord("tuduh");
		w.setState(WordUtil.NEW.toString());
		w.setHaveReadNew(true);
		w.setHaveReadOld(true);
		w.update(getSQLite(), "accuse");

		getAppState().setAllRow(Word.getAllRow(getSQLite()));
		wordMap = getAppState().getAllRow();
		DebugHelper.debug(wordMap.get("accuse").getIndonesianWord());
		DebugHelper.debug(wordMap.get("accuse").getEnglishWord());
		DebugHelper.debug(wordMap.get("accuse").getState());
		DebugHelper.debug(wordMap.get("accuse").isHaveReadNew());
		DebugHelper.debug(wordMap.get("accuse").isHaveReadOld());
	}
}
