package au.edu.anu.club.cs.cssadoorwidget;

import static au.edu.anu.club.cs.cssadoorwidget.MyLog.logv;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		logv(this, "onReceive called with: " + intent.getAction());
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			setAlarm(context, true);
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			setAlarm(context, false);
		}
	}
	
	private void setAlarm(Context context, boolean value) {
		Intent intent = new Intent(context, DoorWidgetProvider.class);
		intent.setAction(DoorWidgetProvider.UPDATE_ACTION);
		PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		if (value) alarm.setRepeating(AlarmManager.RTC, 0, 
				DoorWidgetProvider.UPDATE_INTERVAL_MILLIS, pending);
		else alarm.cancel(pending);
		
		logv(this, "Alarm: " + (value ? "on" : "off"));
	}

}
