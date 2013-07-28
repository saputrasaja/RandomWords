package com.wiwit.util;

import android.app.Application;

public class MyApp extends Application{

	private String test;
	
	public MyApp() {
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}
