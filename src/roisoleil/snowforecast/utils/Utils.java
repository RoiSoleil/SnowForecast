package roisoleil.snowforecast.utils;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings.Secure;

public class Utils {

	public static Set<String> getFavorites(Activity activity) {
		Set<String> favorites = new HashSet<String>();
		if (activity != null) {
			SharedPreferences sharedPreferences = activity
					.getSharedPreferences(Constants.PREFERENCES,
							Context.MODE_PRIVATE);
			if (!sharedPreferences.contains(Constants.FAVORITES)) {
				favorites = new HashSet<String>();
				Editor editor = sharedPreferences.edit();
				editor.putStringSet(Constants.FAVORITES, favorites);
				editor.commit();
			} else {
				favorites = sharedPreferences.getStringSet(Constants.FAVORITES,
						new HashSet<String>());
			}
		}
		return favorites;
	}

	public static void addOrRemoveToFavorites(Activity activity, String areaId) {
		if (activity != null && areaId != null) {
			Set<String> favorites = getFavorites(activity);
			if (favorites.contains(areaId)) {
				favorites.remove(areaId);
			} else {
				favorites.add(areaId);
			}
			SharedPreferences sharedPreferences = activity
					.getSharedPreferences(Constants.PREFERENCES,
							Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putStringSet(Constants.FAVORITES, favorites);
			editor.commit();
		}
	}

	public static boolean isInFavorites(Activity activity, String areaId) {
		if (activity != null && areaId != null) {
			Set<String> favorites = getFavorites(activity);
			return favorites.contains(areaId);
		}
		return false;
	}

	public static String getAndroidId(Activity activity) {
		String androidId = Constants.EMPTY;
		if (activity != null) {
			androidId = Secure.getString(activity.getContentResolver(),
					Secure.ANDROID_ID);
		}
		return androidId;
	}

	public static Location getLocation(Activity activity) {
		Location location = null;
		if (activity != null) {
			LocationManager locationManager = (LocationManager) activity
					.getSystemService(Context.LOCATION_SERVICE);
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
	}

}
