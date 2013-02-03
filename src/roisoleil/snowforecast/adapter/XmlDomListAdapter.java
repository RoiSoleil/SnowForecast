package roisoleil.snowforecast.adapter;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.androidquery.util.XmlDom;

public abstract class XmlDomListAdapter implements ListAdapter {

	protected Activity activity;

	protected XmlDom xmlDom;

	public XmlDomListAdapter(Activity activity, XmlDom xmlDom) {
		this.activity = activity;
		this.xmlDom = xmlDom;
	}
	
}
