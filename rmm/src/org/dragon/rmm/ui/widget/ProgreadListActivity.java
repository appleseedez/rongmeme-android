package org.dragon.rmm.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ResponseListener;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;

/**
 * <pre>
 * An activity that implements several basic functionalities used in common scenarios
 * 
 * 1. List some items of small views
 * 2. Progress bar
 * 3. Basic page header components and its click handler
 * 4. Receive asynchronous resources
 * </pre>
 * 
 * @author Bots
 */
public abstract class ProgreadListActivity<T> extends ListActivity implements OnClickListener, ResponseListener {
	
	private SimpleAdapter mSimpleAdapter;
	private List<HashMap<String, Object>> mDataSet;
	private List<T> mDataSource;
	
	int[] mElements;
	String[] mFields;
	
	private View mProgressBar;
	
	private int mLayoutResId    = -1;
	private int mSubLayoutResId = -1;
	
	private int mNavigatorTitleLabel     = -1;
	private int mNavigatorForwardIcon   = -1;
	private int mNavigatorBackwardIcon  = -1;
	
	public ProgreadListActivity(int layout, int subLayout) {
		mLayoutResId = layout;
		mSubLayoutResId = subLayout;
	}
	
	protected void setBackwardIcon(int backward) {
		mNavigatorBackwardIcon = backward;
	}
	
	protected void setForwardIcon(int forward) {
		mNavigatorForwardIcon = forward;
	}
	
	protected void setTitleLabel(int label) {
		mNavigatorTitleLabel  = label;
	}
	
	protected void setFields(String[] fields) {
		mFields = fields;
	}
	
	protected void setElements(int[] elements) {
		mElements = elements;
	}
	
	public T getData(long index) {
		T data = null;
		if(mDataSource != null) {
			data = mDataSource.get((int)index);
		}
		return data;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		if(mLayoutResId == -1) {
			throw new RuntimeException("miss layout resource id");
		}
		
		View layout = getLayoutInflater().inflate(mLayoutResId, null);
		setContentView(layout);
		
		mProgressBar = layout.findViewById(android.R.id.progress);

		Button backward = (Button) layout.findViewById(R.id.navigator_backward);
		if(backward == null) {
			throw new RuntimeException("not include page header definition in layout xml file");
		}
		
		if(mNavigatorBackwardIcon != -1) {
			backward.setBackgroundResource(mNavigatorBackwardIcon);
		}
		
		backward.setOnClickListener(this);

		if(mNavigatorTitleLabel != -1) {
			TextView label = (TextView) layout.findViewById(R.id.navigator_title);
			label.setText(mNavigatorTitleLabel);
		}
		
		if(mNavigatorForwardIcon != -1) {
			Button forward = (Button) layout.findViewById(R.id.navigator_forward);
			forward.setBackgroundResource(mNavigatorForwardIcon);
			forward.setOnClickListener(this);
		}

		mSimpleAdapter = initSimpleAdapter(getBaseContext());
		setListAdapter(mSimpleAdapter);
		
//		getListView().setDivider(getResources().getDrawable(R.drawable.line));
		
		updateInternalDataSource();
	}

	SimpleAdapter initSimpleAdapter(Context context) {

		mDataSet = new ArrayList<HashMap<String, Object>>();
		mDataSource = new ArrayList<T>();

		updateDataSetInternally();
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(context, mDataSet, mSubLayoutResId, mFields, mElements);

		ListBinder listAdapter = new ListBinder();
		simpleAdapter.setViewBinder(listAdapter);

		return simpleAdapter;
	}

	private void updateDataSetInternally() {
		mDataSet.clear();
		int size = mDataSource.size();
		for (int index = 0; index < size; index++) {
			HashMap<String, Object> mapping = new HashMap<String, Object>();
			T data = mDataSource.get(index);
			updateDataSet(data, mapping);
			mDataSet.add(mapping);
		}
	}
	
	/**
	 * Establish relationship between data set and view elements
	 * 
	 * @param data what to display
	 * @param mapping where to display
	 */
	protected abstract void updateDataSet(T data, HashMap<String, Object> mapping);

	private void updateInternalDataSource() {
		mProgressBar.setVisibility(View.VISIBLE);
		requestDataSource();
	}
	
	/**
	 * trigger a transaction that accesses network or data storage for retrieving data
	 */
	protected abstract void requestDataSource();
	
	/**
	 * update own data source after retrieving data
	 * 
	 * @param bundle data retrieved
	 * @param dataSource data source that needs to fulfill
	 */
	protected abstract void updateDataSource(ApiMethod which, String bundle, List<T> dataSource);
	
	protected boolean updateViewValue(int id, View view, Object data) {
		return false;
	}
	
	class ListBinder implements SimpleAdapter.ViewBinder {
		@Override
		public boolean setViewValue(View view, Object data, String textRepresentation) {
			return updateViewValue(view.getId(), view, data);
		}
	}
	
	protected void onBackwordBtnClick() {
		finish();
	}
	
	protected void onForwardBtnClick() {}
	
	protected void updateViewLayout() {
		updateDataSetInternally();
		mSimpleAdapter.notifyDataSetChanged();
	}

	protected void onClick(int id, View view) {}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.navigator_backward:
			onBackwordBtnClick();
			break;
		case R.id.navigator_forward:
			onForwardBtnClick();
			break;
		default:
			onClick(view.getId(), view);
			break;
		}
	}

	@Override
	public void success(ApiMethod api, String response) {
		mProgressBar.setVisibility(View.GONE);

		updateDataSource(api, response, mDataSource);
		updateDataSetInternally();
		
		mSimpleAdapter.notifyDataSetChanged();
	}

	@Override
	public void fail(ApiMethod api, VolleyError error) {
	}
}
