package au.edu.anu.club.cs.cssadoorwidget;

import android.util.Log;

public class MyLog {
	public static void logv(Object owner, String message) {
		Log.v(owner.getClass().getSimpleName(), message);
	}
	public static void logd(Object owner, String message) {
		Log.d(owner.getClass().getSimpleName(), message);
	}
	public static void loge(Object owner, String message) {
		Log.e(owner.getClass().getSimpleName(), message);
	}
	public static void logw(Object owner, String message) {
		Log.w(owner.getClass().getSimpleName(), message);
	}
	public static void logi(Object owner, String message) {
		Log.i(owner.getClass().getSimpleName(), message);
	}
}
