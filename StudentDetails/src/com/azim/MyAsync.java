package com.azim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

@SuppressLint("NewApi")
public class MyAsync extends AsyncTask<String, Integer, String> {

	public static String NETORK_FAILURE_REASON = null;
	public static int CURRENT_NETWORK = ConnectivityManager.TYPE_WIFI;
	Context _tt;
	private ProgressDialog dialog;
	public static final int APK_CONNECTION_TIMEOUT = 15000;
	public static final int APK_WAIT_DATA_TIMEOUT = 30000;
	private Handler _handler;

	public MyAsync(Context ctx, Handler requestHandler) {
		// TODO Auto-generated constructor stub
		_tt = ctx;
		_handler = requestHandler;
		dialog = new ProgressDialog(ctx);
	}

	// I will put all my download code here
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		if (isInternetAvailable(_tt)) {

			byte[] result = null;
			String str = "";
			HttpEntity httpEntity = null;
			try {
				Log.d("service request  request is ", params[0]);
				HttpGet httpGet = new HttpGet(params[0]);
				HttpParams httpParameters = new BasicHttpParams();
				// Set the timeout in milliseconds until a connection is
				// established.
				// The default value is zero, that means the timeout is not
				// used.
				// int timeoutConnection = 3000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						APK_CONNECTION_TIMEOUT);
				// Set the default socket timeout (SO_TIMEOUT)
				// in milliseconds which is the timeout for waiting for data.
				// int timeoutSocket = 5000;
				HttpConnectionParams.setSoTimeout(httpParameters,
						APK_WAIT_DATA_TIMEOUT);

				DefaultHttpClient httpClient = new DefaultHttpClient(
						httpParameters);
				HttpResponse response = httpClient.execute(httpGet);

				httpEntity = response.getEntity();
				str = getResponseBody(httpEntity);
				Log.d("Login response code ", response.getStatusLine()
						.getStatusCode() + "");
			}

			catch (NullPointerException e) {

				Log.d(" NullPointerException ", "Problem with the Services ");
				return "Nullpointerexception";
				// return false;
			} catch (SocketTimeoutException e) {

				Log.d("  SocketTimeoutException ", "Problem with the Services ");
				return "SocketTimeoutException";
				// return false;
			} catch (ConnectTimeoutException e) {

				Log.d("  connectiontimeout ", "Problem with the Services ");
				// return false;
				return "connectiontimeout";

			}

			catch (UnknownHostException e) {
				Log.d("Pals_Service  UnknownHostException ",
						"Problem with the Services ");
				// return false;
				return "UnknownHostException";

			}

			catch (UnsupportedEncodingException e) {

				Log.d("Pals_Service  unsupportedencodedexception ",
						"Problem with the Services ");
				// return false;
				return "unsupportedencodedexception";
			} catch (IOException e) {
				Log.d("Pals_Service  IOException ",
						"Problem with the Services ");
				// return false;
				return "IOException";
			} catch (IllegalStateException e) {
				Log.d("  IllegalStateException ", "Problem with the Services ");
				// return false;
				return "  IllegalStateException ";
			} catch (Exception e) {
				Log.d("Pals_Service  normalexception ",
						"Problem with the Services ");
				// return false;
				return "Other Exception ";
			}

			return str;

		} else {

			Log.v("$$$$$$$$$$$$", "No internet connection");

			return ("No Internet connection on your device");

		}

	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog.dismiss();

		Message msg = Message.obtain();
		msg.arg1 = 999;
		try {
			Bundle bundle = new Bundle();
			bundle.putString("user_message", result);
			msg.setData(bundle);
			_handler.sendMessage(msg);
		} catch (Exception e) {

		}

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();

		try {
			dialog.setMessage("Please wait, data is downloading...");
			dialog.show();
		} catch (Exception e) {

		}

	}

	public static boolean isInternetAvailable(Context context) {
		try {
			ConnectivityManager conMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final android.net.NetworkInfo wifi = conMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			final android.net.NetworkInfo mobile = conMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NETORK_FAILURE_REASON = null;
			if (wifi.getState() == NetworkInfo.State.CONNECTED
					|| wifi.getState() == NetworkInfo.State.CONNECTING) {
				CURRENT_NETWORK = ConnectivityManager.TYPE_WIFI;
				return true;

			} else if (mobile.getState() == NetworkInfo.State.CONNECTED
					|| mobile.getState() == NetworkInfo.State.CONNECTING) {
				CURRENT_NETWORK = ConnectivityManager.TYPE_MOBILE;
				return true;
			} else if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
					|| conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
				return true;

			} else if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
					|| conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {
				return true;

			} else if (mobile.getState() == NetworkInfo.State.DISCONNECTED
					|| mobile.getState() == NetworkInfo.State.DISCONNECTING) {

				NETORK_FAILURE_REASON = mobile.getReason();
				CURRENT_NETWORK = ConnectivityManager.TYPE_MOBILE;
			} else if (wifi.getState() == NetworkInfo.State.DISCONNECTED
					|| wifi.getState() == NetworkInfo.State.DISCONNECTING) {
				CURRENT_NETWORK = ConnectivityManager.TYPE_WIFI;
				NETORK_FAILURE_REASON = wifi.getReason();

			} else if (mobile.getState() == NetworkInfo.State.DISCONNECTED
					|| mobile.getState() == NetworkInfo.State.DISCONNECTING) {

				NETORK_FAILURE_REASON = mobile.getReason();
				CURRENT_NETWORK = ConnectivityManager.TYPE_MOBILE;
			} else if (wifi.getState() == NetworkInfo.State.DISCONNECTED
					|| wifi.getState() == NetworkInfo.State.DISCONNECTING) {
				CURRENT_NETWORK = ConnectivityManager.TYPE_WIFI;
				NETORK_FAILURE_REASON = wifi.getReason();

			}
		} catch (NullPointerException e) {
			return true;
		} catch (Exception e) {
			return true;
		}

		return false;
	}

	public String getResponseBody(final HttpEntity entity) throws IOException,
			ParseException {

		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		InputStream instream = entity.getContent();

		if (instream == null) {
			return "";
		}

		Log.d("Content length ", String.valueOf(entity.getContentLength()));

		if (entity.getContentLength() > Integer.MAX_VALUE) {
			Log.d("Content length ", " Exception ");
			throw new IllegalArgumentException(

			"HTTP entity too large to be buffered in memory");

		}

		StringBuilder buffer = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				instream, HTTP.UTF_8));

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

		} finally {
			instream.close();
			reader.close();
		}

		return buffer.toString();

	}

}
