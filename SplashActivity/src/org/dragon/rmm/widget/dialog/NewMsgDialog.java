package org.dragon.rmm.widget.dialog;

import org.dragon.rmm.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class NewMsgDialog extends AlertDialog {

    public ImageView nmdCloseBtn;
    public TextView nmdMsgContent;
    Context context;

    public NewMsgDialog(Context context) {
        super(context);
        this.context = context;
    }

    public NewMsgDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.new_msg_dialog);
        initViews();
    }

    private void initViews() {
        nmdCloseBtn = (ImageView) findViewById(R.id.nmd_close_btn);
        nmdMsgContent = (TextView) findViewById(R.id.nmd_msg_content_textview);
    }
}
