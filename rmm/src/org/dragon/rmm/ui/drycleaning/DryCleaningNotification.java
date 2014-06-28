package org.dragon.rmm.ui.drycleaning;

import org.dragon.rmm.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DryCleaningNotification extends Dialog implements android.view.View.OnClickListener {

	private String mContent;
	
	public DryCleaningNotification(Context context, String content) {
		super(context);
		mContent = content;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_dry_cleaning_notification);
		
		Button close = (Button) findViewById(R.id.close);
		close.setOnClickListener(this);
		
		if(mContent != null) {
			TextView content = (TextView) findViewById(R.id.notification);
			content.setText(mContent);
		}
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.close) {
			dismiss();
		}
	}
}
