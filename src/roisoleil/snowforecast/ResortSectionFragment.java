package roisoleil.snowforecast;

import java.util.Locale;

import roisoleil.snowforecast.utils.Constants;
import roisoleil.snowforecast.utils.Utils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

public class ResortSectionFragment extends Fragment {

	protected View headerView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FrameLayout root = new FrameLayout(getActivity());
		inflater.inflate(R.layout.resort, root);
		refresh(false);
		return root;
	}

	public void result(String url, String html, AjaxStatus status) {
		System.out.println("klkfdsnjlsdf");
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
		// headerView.findViewById(R.id.error).setVisibility(View.GONE);
		long expire = 24 * 60 * 60 * 1000;
		if (forced) {
			expire = 10;
		}

		AQuery aQuery = new AQuery(getActivity());
		String url = "http://www.onthesnow.com/app/skireport/area.html?ver=2.2&area="
				+ getActivity().getIntent().getExtras()
						.getString(Constants.AREA_ID)
				+ "&androidid="
				+ Utils.getAndroidId(getActivity())
				+ "&meas=Metric"
				+ "&language="
				+ Locale.getDefault().getLanguage()
				+ "&locale="
				+ Locale.getDefault().getLanguage()
				+ "_"
				+ Locale.getDefault().getCountry()
				+ "&timezone="
				+ "yyyy-MMM-dd HH:mm:ss";
		aQuery.ajax(url, String.class, expire, this, "result");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.favorite, menu);
	}

}
