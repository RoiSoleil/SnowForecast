package roisoleil.snowforecast;

import java.io.IOException;

import roisoleil.snowforecast.adapter.RegionAdapter;
import roisoleil.snowforecast.adapter.SorryAdapter;
import roisoleil.snowforecast.utils.AreaDataBaseHelper;
import roisoleil.snowforecast.utils.Constants;
import roisoleil.snowforecast.utils.ExpandableListFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class RegionSectionFragment extends ExpandableListFragment implements
		IRefreshable {

	protected View headerView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		if (getExpandableListAdapter() == null) {
			headerView = getActivity().getLayoutInflater().inflate(
					R.layout.error, null);
			getExpandableListView().addHeaderView(headerView);
		}
		refresh(false);
	}

	public void result(SQLiteDatabase sqliteDatabase) {
		if (sqliteDatabase != null) {
			setListAdapter(new RegionAdapter(getActivity(), sqliteDatabase));
			setListShown(true);
		} else {
			headerView.findViewById(R.id.error).setVisibility(View.VISIBLE);
			((TextView) headerView.findViewById(R.id.statusValue))
					.setText("-1");
			((TextView) headerView.findViewById(R.id.messageValue))
					.setText("Can't load dataBase.");
			setListAdapter(new SorryAdapter(getActivity()));
			setListShown(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_search:
			search();
			break;
		}
		return true;
	}

	public void refresh(boolean forced) {
		headerView.findViewById(R.id.error).setVisibility(View.GONE);
		AreaDataBaseHelper dataBaseHelper = new AreaDataBaseHelper(
				getActivity());
		SQLiteDatabase sqliteDatabase = null;
		try {
			dataBaseHelper.createDataBase();
			sqliteDatabase = dataBaseHelper.getReadableDatabase();
		} catch (IOException e) {
		}
		result(sqliteDatabase);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.region, menu);
	}

	private void search() {
		// TODO
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		Object data = arg1.getTag();
		if (data instanceof String) {
			String areaId = (String) data;
			String resortName = AreaDataBaseHelper.getResortName(getActivity(),
					areaId);
			if (areaId != null && resortName != null) {
				Intent intent = new Intent(getActivity(), ResortActivity.class);
				intent.putExtra(Constants.AREA_ID, areaId);
				intent.putExtra(Constants.AREA_NAME, resortName);
				getActivity().startActivity(intent);
			}
		}
		return super.onChildClick(arg0, arg1, arg2, arg3, arg4);
	}

}
