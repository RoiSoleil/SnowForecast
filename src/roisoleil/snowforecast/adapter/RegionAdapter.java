package roisoleil.snowforecast.adapter;

import roisoleil.snowforecast.R;
import roisoleil.snowforecast.utils.AreaDataBaseHelper;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.SimpleCursorTreeAdapter;

public class RegionAdapter extends SimpleCursorTreeAdapter {

	protected Activity activity;

	protected SQLiteDatabase sqliteDatabase;

	public RegionAdapter(Activity activity, SQLiteDatabase sqliteDatabase) {
		super(activity, AreaDataBaseHelper.fetchAllCountries(sqliteDatabase),
				R.layout.icountry, R.layout.icountry,
				new String[] { "resort_country" },
				new int[] { R.id.resortCountry }, R.layout.preview_lite,
				R.layout.preview_lite, new String[] { "resort_name",
						"resort_region" }, new int[] { R.id.resortName,
						R.id.regionName });
		this.activity = activity;
		this.sqliteDatabase = sqliteDatabase;
	}

	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor) {
		return AreaDataBaseHelper.fetchResortByCountry(sqliteDatabase,
				groupCursor.getString(0));
	}

	@Override
	public long getGroupId(int groupPosition) {
		return (long) groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return (long) childPosition;
	}

	@Override
	protected void bindChildView(View view, Context context, Cursor cursor,
			boolean isLastChild) {
		super.bindChildView(view, context, cursor, isLastChild);
		int index = cursor.getColumnIndex("resort_id");
		if (index != -1) {
			String areaId = cursor.getString(index);
			view.setTag(areaId);
			view.findViewById(R.id.favorite).setTag(areaId);
		}
	}
}
