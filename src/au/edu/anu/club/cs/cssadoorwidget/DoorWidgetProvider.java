package au.edu.anu.club.cs.cssadoorwidget;

import static au.edu.anu.club.cs.cssadoorwidget.MyLog.logv;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class DoorWidgetProvider extends AppWidgetProvider {
	
	private static final String DOOR_BUTTON = "DOOR_BUTTON";
	protected static final long UPDATE_INTERVAL_MILLIS = 30 * 1000;
	protected static final String UPDATE_ACTION = "UPDATE_ALARM";
	
	public DoorWidgetProvider() {
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.door_widget);
		ComponentName cn = new ComponentName(context, DoorWidgetProvider.class);

		rv.setOnClickPendingIntent(R.id.door_button, getPendingSelfIntent(context, DOOR_BUTTON));
		appWidgetManager.updateAppWidget(cn, rv);
		logv(this, "Added button click listener");
		
		setAlarm(context, true);
		
		context.startService(new Intent(context, ScreenService.class));
		logv(this, "Service created"); 
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//logv(this, "onReceive called with: " + intent.getAction());
		if (intent.getAction().equals(DOOR_BUTTON)) {
			logv(this, "button pressed");
			new NetworkTask().execute(context);
		} else if (intent.getAction().equals(UPDATE_ACTION)) {
			logv(this, "alarm update called");
			new NetworkTask().execute(context);
		}
		super.onReceive(context, intent);
	}
	
	@Override
	public void onDisabled(Context context) {
		setAlarm(context, false);
		context.stopService(new Intent(context, ScreenService.class));
		logv(this, "Widget disabled, alarm turned off");
	}
	
	private PendingIntent getPendingSelfIntent(Context context, String action) {
		Intent intent = new Intent(context, getClass());
		intent.setAction(action);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}

	private void setAlarm(Context context, boolean value) {
		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (value) alarm.setRepeating(AlarmManager.RTC, 0, UPDATE_INTERVAL_MILLIS, 
				getPendingSelfIntent(context, UPDATE_ACTION));
		else alarm.cancel(getPendingSelfIntent(context, UPDATE_ACTION));
		logv(this, "Alarm: " + (value ? "on" : "off"));
	}

}
