package com.ollm.cordova.plugin.custombacknavigation;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.BackEventCompat;
import android.os.Build;
import android.util.Log;

public class CustomBackNavigation extends CordovaPlugin {

	private static final String TAG = "CustomBackNavigation";
	private CallbackContext mainCallbackContext = null;
	private OnBackPressedCallback onBackPressedCallback = null;
	private Boolean enable = false;

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		switch (action) {
			case "register":
				mainCallbackContext = callbackContext;
				registerBackCallback(args.getBoolean(0));
				break;
			case "setEnabled":
				setEnabled(args.getBoolean(0));
				break;
		}

		return true;
	}

	private void registerBackCallback(Boolean enable) {

		if (Build.VERSION.SDK_INT >= 33) { // Android 13+

			onBackPressedCallback = new OnBackPressedCallback(enable) {

				@Override
				public void handleOnBackStarted(BackEventCompat backEvent) {

					Log.d(TAG, "handleOnBackStarted");

					sendBackEvent("started", backEvent);

				}

				@Override
				public void handleOnBackProgressed(BackEventCompat backEvent) {

					Log.d(TAG, "handleOnBackProgressed");

					sendBackEvent("progress", backEvent);

				}

				@Override
				public void handleOnBackPressed() {

					Log.d(TAG, "handleOnBackPressed");

					sendBasicEvent("pressed");

				}

				@Override
				public void handleOnBackCancelled() {

					Log.d(TAG, "handleOnBackCancelled");

					sendBasicEvent("cancelled");

				}
			};

			this.cordova.getActivity().getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
		}
	}

	private void setEnabled(Boolean enable) {

		Log.d(TAG, "setEnabled");

		if (onBackPressedCallback != null) {
			onBackPressedCallback.setEnabled(enable);
		}

	}

	private void sendBasicEvent(String type) {

		Log.d(TAG, "sendBasicEvent");

		JSONObject json = new JSONObject();

		try {

			json.put("type", type);

		} catch (JSONException e) {

			PluginResult result = new PluginResult(PluginResult.Status.JSON_EXCEPTION);
			result.setKeepCallback(true);
			mainCallbackContext.sendPluginResult(result);
			return;

		}

		sendPluginResult(json);

	}

	private void sendBackEvent(String type, BackEventCompat backEvent) {

		JSONObject json = new JSONObject();

		try {

			json.put("type", type);
			json.put("progress", backEvent.getProgress());
			json.put("swipeEdge", backEvent.getSwipeEdge());
			json.put("touchX", backEvent.getTouchX());
			json.put("touchY", backEvent.getTouchY());

		} catch (JSONException e) {

			PluginResult result = new PluginResult(PluginResult.Status.JSON_EXCEPTION);
			result.setKeepCallback(true);
			mainCallbackContext.sendPluginResult(result);
			return;

		}

		sendPluginResult(json);

	}

	private void sendPluginResult(JSONObject json) {

		if (mainCallbackContext != null) {
			PluginResult result = new PluginResult(PluginResult.Status.OK, json);
			result.setKeepCallback(true);
			mainCallbackContext.sendPluginResult(result);
		}

	}

}