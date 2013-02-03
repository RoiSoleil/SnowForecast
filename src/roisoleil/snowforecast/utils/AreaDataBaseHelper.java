package roisoleil.snowforecast.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class AreaDataBaseHelper extends SQLiteOpenHelper {

	private final static String DB_PATH = "/data/data/roisoleil.snowforecast/databases/";

	private final static String DB_NAME = "areas";

	private final static String TABLE_NAME = "all_areas";

	private SQLiteDatabase sqliteDataBase;

	private final Context context;

	public AreaDataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context;
	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
		} else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = context.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		sqliteDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if (sqliteDataBase != null)
			sqliteDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public static Cursor fetchAllCountries(SQLiteDatabase sqliteDatabase) {
		Cursor cursor = null;
		if (sqliteDatabase != null) {
			cursor = sqliteDatabase.query(true, TABLE_NAME,
					new String[] { "resort_country" }, null, null, null, null,
					null, null);
			if (cursor != null) {
				cursor.moveToFirst();
			}
		}
		return cursor;
	}

	public static Cursor fetchResortByCountry(SQLiteDatabase sqliteDatabase,
			String country) {
		Cursor cursor = null;
		if (sqliteDatabase != null && country != null) {
			cursor = sqliteDatabase.query(true, TABLE_NAME, new String[] {
					"id", "resort_name", "resort_region", "resort_id" },
					"resort_country='" + country + "'", null, null, null, null,
					null);
			if (cursor != null) {
				cursor.moveToFirst();
			}
		}
		return cursor;
	}

	public static List<String> fetchResort(SQLiteDatabase sqliteDatabase) {
		List<String> countries = new ArrayList<String>();
		if (sqliteDatabase != null) {
			Cursor cursor = null;
			cursor = sqliteDatabase.query(true, TABLE_NAME,
					new String[] { "resort_country" }, null, null, null, null,
					null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					countries.add(cursor.getString(0));
				}
			}
		}
		return countries;
	}

	public static String getResortName(Activity activity, String areaId) {
		String resortName = null;
		if (activity != null && areaId != null) {
			AreaDataBaseHelper dataBaseHelper = new AreaDataBaseHelper(activity);
			SQLiteDatabase sqliteDatabase = null;
			try {
				dataBaseHelper.createDataBase();
				sqliteDatabase = dataBaseHelper.getReadableDatabase();
			} catch (IOException e) {
			}
			if (sqliteDatabase != null) {
				Cursor cursor = sqliteDatabase.query(true, TABLE_NAME,
						new String[] { "resort_name" }, "resort_id='" + areaId
								+ "'", null, null, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					resortName = cursor.getString(0);
				}
			}
		}
		return resortName;
	}
}