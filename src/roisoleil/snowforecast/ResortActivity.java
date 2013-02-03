package roisoleil.snowforecast;

import java.util.ArrayList;
import java.util.List;

import roisoleil.snowforecast.utils.Constants;
import roisoleil.snowforecast.utils.Utils;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;

public class ResortActivity extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resort);

		String areaName = getIntent().getExtras()
				.getString(Constants.AREA_NAME);
		setTitle(areaName);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_resort, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public void onFavoriteClick(final View view) {
		Object data = view.getTag();
		if (data instanceof String) {
			Utils.addOrRemoveToFavorites(this, ((String) data));
			int count = mSectionsPagerAdapter.getCount();
			for (int i = 0; i < count; i++) {
				Fragment fragment = mSectionsPagerAdapter.getItem(i);
				if (fragment instanceof IRefreshable) {
					((IRefreshable) fragment).refresh(true);
				}
			}
		}
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		List<Fragment> fragments = new ArrayList<Fragment>();

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments.add(new ResortSectionFragment());
			fragments.add(new WeatherSectionFragment());
			fragments.add(new WebcamSectionFragment());
			fragments.add(new TweetSectionFragment());
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = fragments.get(position);
				break;
			case 1:
				fragment = fragments.get(position);
				break;
			case 2:
				fragment = fragments.get(position);
				break;
			case 3:
				fragment = fragments.get(position);
				break;
			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_resort_section1).toUpperCase();
			case 1:
				return getString(R.string.title_resort_section2).toUpperCase();
			case 2:
				return getString(R.string.title_resort_section3).toUpperCase();
			case 3:
				return getString(R.string.title_resort_section4).toUpperCase();
			}
			return null;
		}
	}

}
