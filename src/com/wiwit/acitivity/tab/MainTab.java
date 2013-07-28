package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.util.MyApp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainTab extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);
        MyApp appState = ((MyApp)this.getApplicationContext());
        appState.setTest("hai");
        /* TabHost will have Tabs */
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        /* TabSpec used to create a new tab. 
         * By using TabSpec only we can able to setContent to the tab.
         * By using TabSpec setIndicator() we can set name to tab. */
        
        /* tid1 is firstTabSpec Id. Its used to access outside. */
        TabSpec newWordSpec = tabHost.newTabSpec("new");
        TabSpec oldWordSpec = tabHost.newTabSpec("old");
        TabSpec editTabSpec = tabHost.newTabSpec("old");
        TabSpec infoTabSpec = tabHost.newTabSpec("old");
        
        /* TabSpec setIndicator() is used to set name for the tab. */
        /* TabSpec setContent() is used to set content for a particular tab. */
        newWordSpec.setIndicator("New Word").setContent(new Intent(this,NewWordTab.class));
        oldWordSpec.setIndicator("Old Word").setContent(new Intent(this,OldWordTab.class));
        editTabSpec.setIndicator("Edit Word").setContent(new Intent(this,OldWordTab.class));
        infoTabSpec.setIndicator("Info").setContent(new Intent(this,InfoAppTab.class));
        
        /* Add tabSpec to the TabHost to display. */
        tabHost.addTab(infoTabSpec);
        tabHost.addTab(newWordSpec);
        tabHost.addTab(oldWordSpec);
        tabHost.addTab(editTabSpec);
        
        
    }
}
