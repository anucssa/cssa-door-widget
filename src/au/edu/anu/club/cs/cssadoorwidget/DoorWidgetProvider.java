package au.edu.anu.club.cs.cssadoorwidget;

import static au.edu.anu.club.cs.cssadoorwidget.MyLog.logv;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class DoorWidgetProvider extends AppWidgetProvider {
	
	private static final String DOOR_BUTTON = "DOOR_BUTTON";
	
	public DoorWidgetProvider() {
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		logv(this, "update called");
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.door_widget);
		ComponentName cn = new ComponentName(context, DoorWidgetProvider.class);

		rv.setOnClickPendingIntent(R.id.door_button, getPendingSelfIntent(context, DOOR_BUTTON));
		appWidgetManager.updateAppWidget(cn, rv);
		logv(this, "Added button click listener");
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (intent.getAction().equals(DOOR_BUTTON)) {
			logv(this, "button pressed");
			new NetworkTask().execute(context);
		}
	}
	
	private PendingIntent getPendingSelfIntent(Context context, String action) {
		Intent intent = new Intent(context, getClass());
		intent.setAction(action);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}
}
