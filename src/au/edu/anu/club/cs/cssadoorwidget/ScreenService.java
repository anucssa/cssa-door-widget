package au.edu.anu.club.cs.cssadoorwidget;

import static au.edu.anu.club.cs.cssadoorwidget.MyLog.*;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class ScreenService extends Service {
	
	private ScreenReceiver screenReceiver;
	
	@Override
	public void onCreate() {
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		
		screenReceiver = new ScreenReceiver();
		registerReceiver(screenReceiver, filter);
		logv(this, "ScreenReceiver registered");
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(screenReceiver);
		logv(this, "ScreenReceiver unregistered");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
