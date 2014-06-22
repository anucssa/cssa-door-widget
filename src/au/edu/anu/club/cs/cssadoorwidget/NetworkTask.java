package au.edu.anu.club.cs.cssadoorwidget;

import static au.edu.anu.club.cs.cssadoorwidget.MyLog.logv;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.Toast;

public class NetworkTask extends AsyncTask<NetworkTask.NetworkTaskParams, Void, Void> { 
	
	public static class NetworkTaskParams {
		public Context context;
		public boolean makeToast;
		public NetworkTaskParams(Context context, boolean makeToast) {
			this.context = context;
			this.makeToast = makeToast;
		}
	}

	private enum Result {OPEN, CLOSED, NO_INTERNET, OTHER_ERROR}
	private Result result = null;
	private NetworkTaskParams p;

	private static final String DOOR_URI = "http://cssadoor.herokuapp.com/";
	private static final String OPEN_STRING = "open";
	private static final int TRANSITION_LENGTH_MILLIS = 500;

	@Override
	protected Void doInBackground(NetworkTaskParams... argv) {
		p = argv[0];
		if (isOnline(p.context)) {
			try {
				if (isDoorOpen()) {
					result = Result.OPEN;
				} else {
					result = Result.CLOSED;
				}
			} catch (DoorAccessException e) {
				e.printStackTrace();
				result = Result.OTHER_ERROR;
			}
		} else {
			result = Result.NO_INTERNET;
		}
		return null;
	}

	private boolean isOnline(Context context) {
		ConnectivityManager cm =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	private boolean isDoorOpen() throws DoorAccessException {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(DOOR_URI);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			String door_text = client.execute(request, responseHandler);
			//logv(this, door_text);
			return door_text.contains(OPEN_STRING);
		} catch (ClientProtocolException e) {
			throw new DoorAccessException(e);
		} catch (IOException e) {
			throw new DoorAccessException(e);
		}
	}

	@Override
	public void onPostExecute(Void v) {
		RemoteViews rv = new RemoteViews(p.context.getPackageName(), R.layout.door_widget);
		//TransitionDrawable td = (TransitionDrawable) context.getResources().getDrawable(R.drawable.door_transition);
		switch (result) {
		case OPEN:
			//td.startTransition(TRANSITION_LENGTH_MILLIS);
			rv.setImageViewResource(R.id.door_button, R.drawable.door_open_selector);
			report(R.string.toast_door_open);
			break;
		case CLOSED:
			//td.reverseTransition(TRANSITION_LENGTH_MILLIS);
			rv.setImageViewResource(R.id.door_button, R.drawable.door_closed_selector);
			report(R.string.toast_door_closed);
			break;
		case NO_INTERNET:
			report(R.string.toast_no_internet);
			break;
		case OTHER_ERROR:
			report(R.string.toast_update_failed);
			break;
		}
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(p.context);
		ComponentName watchWidget = new ComponentName(p.context, DoorWidgetProvider.class);
		appWidgetManager.updateAppWidget(watchWidget, rv);
	}

	private void report(int strId) {
		logv(this, p.context.getResources().getString(strId));
		if (p.makeToast) Toast.makeText(p.context, strId, Toast.LENGTH_SHORT).show();
	}
}

