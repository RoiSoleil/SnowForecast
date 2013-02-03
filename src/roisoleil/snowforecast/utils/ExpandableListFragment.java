package roisoleil.snowforecast.utils;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ExpandableListFragment extends Fragment implements
		OnCreateContextMenuListener, ExpandableListView.OnChildClickListener,
		ExpandableListView.OnGroupCollapseListener,
		ExpandableListView.OnGroupExpandListener {

	static final int INTERNAL_EMPTY_ID = 0x00ff0001;
	static final int INTERNAL_LIST_CONTAINER_ID = 0x00ff0003;

	final private Handler mHandler = new Handler();

	final private Runnable mRequestFocus = new Runnable() {
		public void run() {
			mList.focusableViewAvailable(mList);
		}
	};

	final private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			onListItemClick((ListView) parent, v, position, id);
		}
	};

	ExpandableListAdapter mAdapter;
	ExpandableListView mList;
	View mEmptyView;
	TextView mStandardEmptyView;
	View mListContainer;
	boolean mSetEmptyText;
	boolean mListShown;
	boolean mFinishedStart = false;

	public ExpandableListFragment() {
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FrameLayout root = new FrameLayout(getActivity());

		FrameLayout lframe = new FrameLayout(getActivity());
		lframe.setId(INTERNAL_LIST_CONTAINER_ID);

		TextView tv = new TextView(getActivity());
		tv.setId(INTERNAL_EMPTY_ID);
		tv.setGravity(Gravity.CENTER);
		lframe.addView(tv, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));

		ExpandableListView lv = new ExpandableListView(getActivity());
		lv.setId(android.R.id.list);
		lv.setDrawSelectorOnTop(false);
		lframe.addView(lv, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));

		root.addView(lframe, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));

		ListView.LayoutParams lp = new ListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		root.setLayoutParams(lp);

		return root;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ensureList();
	}

	@Override
	public void onDestroyView() {
		mHandler.removeCallbacks(mRequestFocus);
		mList = null;
		super.onDestroyView();
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
	}

	public void setListAdapter(ExpandableListAdapter adapter) {
		boolean hadAdapter = mAdapter != null;
		mAdapter = adapter;
		if (mList != null) {
			mList.setAdapter(adapter);
			if (!mListShown && !hadAdapter) {
				// The list was hidden, and previously didn't have an
				// adapter. It is now time to show it.
				setListShown(true, getView().getWindowToken() != null);
			}
		}
	}

	public void setSelection(int position) {
		ensureList();
		mList.setSelection(position);
	}

	public long getSelectedPosition() {
		ensureList();
		return mList.getSelectedPosition();
	}

	public long getSelectedId() {
		ensureList();
		return mList.getSelectedId();
	}

	public ExpandableListView getExpandableListView() {
		ensureList();
		return mList;
	}

	public void setEmptyText(CharSequence text) {
		ensureList();
		if (mStandardEmptyView == null) {
			throw new IllegalStateException(
					"Can't be used with a custom content view");
		}
		mStandardEmptyView.setText(text);
		if (!mSetEmptyText) {
			mList.setEmptyView(mStandardEmptyView);
			mSetEmptyText = true;
		}
	}

	public void setListShown(boolean shown) {
		setListShown(shown, true);
	}

	public void setListShownNoAnimation(boolean shown) {
		setListShown(shown, false);
	}

	private void setListShown(boolean shown, boolean animate) {
		ensureList();
		if (mListShown == shown) {
			return;
		}
		mListShown = shown;
		if (mListContainer != null) {
			if (shown) {
				if (animate) {
					mListContainer.startAnimation(AnimationUtils.loadAnimation(
							getActivity(), android.R.anim.fade_in));
				}
				mListContainer.setVisibility(View.VISIBLE);
			} else {
				if (animate) {
					mListContainer.startAnimation(AnimationUtils.loadAnimation(
							getActivity(), android.R.anim.fade_out));
				}
				mListContainer.setVisibility(View.GONE);
			}
		}
	}

	public ExpandableListAdapter getExpandableListAdapter() {
		return mAdapter;
	}

	private void ensureList() {
		if (mList != null) {
			return;
		}
		View root = getView();
		if (root == null) {
			throw new IllegalStateException("Content view not yet created");
		}
		if (root instanceof ExpandableListView) {
			mList = (ExpandableListView) root;
		} else {
			mStandardEmptyView = (TextView) root
					.findViewById(INTERNAL_EMPTY_ID);
			if (mStandardEmptyView == null) {
				mEmptyView = root.findViewById(android.R.id.empty);
			}
			mListContainer = root.findViewById(INTERNAL_LIST_CONTAINER_ID);
			View rawListView = root.findViewById(android.R.id.list);
			if (!(rawListView instanceof ExpandableListView)) {
				if (rawListView == null) {
					throw new RuntimeException(
							"Your content must have a ExpandableListView whose id attribute is "
									+ "'android.R.id.list'");
				}
				throw new RuntimeException(
						"Content has view with id attribute 'android.R.id.list' "
								+ "that is not a ExpandableListView class");
			}
			mList = (ExpandableListView) rawListView;
			if (mEmptyView != null) {
				mList.setEmptyView(mEmptyView);
			}
		}
		mListShown = true;
		mList.setOnItemClickListener(mOnClickListener);
		if (mAdapter != null) {
			setListAdapter(mAdapter);
		} else {
			// We are starting without an adapter, so assume we won't
			// have our data right away and start with the progress indicator.
			setListShown(false, false);
		}
		mHandler.post(mRequestFocus);
	}

	@Override
	public void onGroupExpand(int arg0) {
	}

	@Override
	public void onGroupCollapse(int arg0) {
	}

	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
	}

	public void onContentChanged() {
		View emptyView = getView().findViewById(android.R.id.empty);
		mList = (ExpandableListView) getView().findViewById(android.R.id.list);
		if (mList == null) {
			throw new RuntimeException(
					"Your content must have a ExpandableListView whose id attribute is "
							+ "'android.R.id.list'");
		}
		if (emptyView != null) {
			mList.setEmptyView(emptyView);
		}
		mList.setOnChildClickListener(this);
		mList.setOnGroupExpandListener(this);
		mList.setOnGroupCollapseListener(this);

		if (mFinishedStart) {
			setListAdapter(mAdapter);
		}
		mFinishedStart = true;
	}
}