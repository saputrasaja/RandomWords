package com.wiwit.util;

import android.util.Log;

public class DebugHelper {

	public final String DEBUG_NAME = "RandomWords";
	protected String className;
	protected String msg;

	public DebugHelper(Object o, String msg) {
		if (o != null) {
			this.className = getClassName(o);
		}
		this.msg = msg;
	}

	protected String getDebugName() {
		return DEBUG_NAME;
	}

	protected String getClassName(Object o) {
		return o.getClass().getSimpleName();
	}

	// still use less now
	protected String getMethodName(Object o) {
		return o.getClass().getEnclosingMethod().getName();
	}

	protected String generateMessage() {
		String result = "";
		if (this.className != null)
			result = result + className;
		if (this.msg != null)
			result = result + " : " + msg;
		return result;
	}

	protected void doDebug() {
		Log.d(getDebugName(), generateMessage());
	}

	protected void doException(Exception e) {
		Log.wtf(getDebugName(), generateMessage());
		e.printStackTrace();
	}

	public static void debug(Object msg) {
		DebugHelper db = new DebugHelper(null, String.valueOf(msg));
		db.doDebug();
	}

	public static void debug(Object object, String msg) {
		DebugHelper db = new DebugHelper(object, msg);
		db.doDebug();
	}

	public static void exception(Object o, Exception e) {
		DebugHelper db = new DebugHelper(o, null);
		db.doException(e);
	}
	
	public static void exception(Exception e) {
		DebugHelper db = new DebugHelper(null, null);
		db.doException(e);
	}
}
