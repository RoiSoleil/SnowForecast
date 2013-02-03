package roisoleil.snowforecast;

import java.util.Locale;
import java.util.Set;

import roisoleil.snowforecast.adapter.PreviewAdapter;
import roisoleil.snowforecast.adapter.SorryAdapter;
import roisoleil.snowforecast.utils.Constants;
import roisoleil.snowforecast.utils.Utils;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;

public class WebcamSectionFragment extends ListFragment {

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
			PreviewAdapter favoriteAdapter = new PreviewAdapter(getActivity(),
					xmlDom);
			setListAdapter(favoriteAdapter);
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

	private void refresh(boolean forced) {
		headerView.findViewById(R.id.error).setVisibility(View.GONE);
		setListShown(false);
		long expire = 24 * 60 * 60 * 1000;
		if (forced) {
			expire = 10;
		}

		AQuery aQuery = new AQuery(getActivity());
		String url = "http://www.onthesnow.com/app/skireport/areas.html?ver=2.2&areas="
				+ getFavoritesToString()
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

	private String getFavoritesToString() {
		String toString = "";
		Set<String> favorites = Utils.getFavorites(getActivity());
		if (favorites != null && !favorites.isEmpty()) {
			for (String favorite : favorites) {
				toString += favorite + Constants.COMMA;
			}
		}
		if (toString.endsWith(Constants.COMMA)) {
			toString.substring(0, toString.length() - Constants.COMMA.length());
		}
		return toString;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.favorite, menu);
	}

}
