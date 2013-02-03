package roisoleil.snowforecast.adapter;

import roisoleil.snowforecast.R;
import android.app.Activity;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ListAdapter;

public class SorryAdapter implements ListAdapter, ExpandableListAdapter {

	private final static Long LONG = Long.valueOf(1234567890);

	private Activity activity;

	public SorryAdapter(Activity activity) {
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int arg0) {
		return LONG;
	}

	@Override
	public long getItemId(int arg0) {
		return LONG;
	}

	@Override
	public int getItemViewType(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		return activity.getLayoutInflater().inflate(R.layout.sorry, null);
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return false;
	}

	@Override
	public int getGroupCount() {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		return null;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		return 0;
	}

}
