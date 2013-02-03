package roisoleil.snowforecast;

import java.util.Locale;

import roisoleil.snowforecast.adapter.PreviewAdapter;
import roisoleil.snowforecast.adapter.SorryAdapter;
import roisoleil.snowforecast.utils.AreaDataBaseHelper;
import roisoleil.snowforecast.utils.Constants;
import roisoleil.snowforecast.utils.Utils;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;

public class NearMeSectionFragment extends ListFragment implements IRefreshable {

	protected View headerView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		if (getListAdapter() == null) {
			headerView = getActivity().getLayoutInflater().inflate(
					R.layout.error, null);
			getListView().addHeaderView(headerView);
		}
		refresh(false);
	}

	public void result(String url, XmlDom xmlDom, AjaxStatus status) {
		if (200 == status.getCode() && xmlDom != null) {
			setListAdapter(new PreviewAdapter(getActivity(), xmlDom));
			setListShown(true);
		} else {
			headerView.findViewById(R.id.error).setVisibility(View.VISIBLE);
			((TextView) headerView.findViewById(R.id.statusValue)).setText(""
					+ status.getCode());
			((TextView) headerView.findViewById(R.id.messageValue))
					.setText(status.getMessage());
			setListAdapter(new SorryAdapter(getActivity()));
			setListShown(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_refresh:
			refresh(true);
			break;
		}
		return true;
	}

	public void refresh(boolean forced) {
		headerView.findViewById(R.id.error).setVisibility(View.GONE);
		setListShown(false);
		long expire = 24 * 60 * 60 * 1000;
		if (forced) {
			expire = 10;
		}
		double lat = 47.548489;
		double lon = -122.154526;
		Location location = Utils.getLocation(getActivity());
		if (location != null) {
			lat = location.getLatitude();
			lon = location.getLongitude();
		}
		AQuery aQuery = new AQuery(getActivity());
		String url = "http://www.onthesnow.com/app/skireport/nearbyareas.html?ver=2.2&lat="
				+ lat
				+ "&lon="
				+ lon
				+ "&androidid="
				+ Utils.getAndroidId(getActivity())
				+ "&language="
				+ Locale.getDefault().getLanguage()
				+ "&locale="
				+ Locale.getDefault().getLanguage()
				+ "_"
				+ Locale.getDefault().getCountry();
		aQuery.ajax(url, XmlDom.class, expire, this, "result");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.favorite, menu);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Object data = v.getTag();
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
	}
}
