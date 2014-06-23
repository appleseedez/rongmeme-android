/*
 * 官网地站:http://www.ShareSDK.cn
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 ShareSDK.cn. All rights reserved.
 */

package org.dragon.rmm.ui.widget;

import static cn.sharesdk.framework.utils.BitmapHelper.blur;
import static cn.sharesdk.framework.utils.BitmapHelper.captureView;
import static cn.sharesdk.framework.utils.R.dipToPx;
import static cn.sharesdk.framework.utils.R.getBitmapRes;
import static cn.sharesdk.framework.utils.R.getScreenWidth;
import static cn.sharesdk.framework.utils.R.getStringRes;

import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoComment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.onekeyshare.PicViewer;

import com.android.volley.VolleyError;

/** 执行图文分享的页面，此页面不支持微信平台的分享 */
public class ShareComment extends Dialog implements OnClickListener, TextWatcher {

	private static final int MAX_TEXT_COUNT = 140;
	private static final int DIM_COLOR = 0x7f323232;
	private RelativeLayout rlPage;
	private TitleLayout llTitle;
	private LinearLayout llBody;
	private RelativeLayout rlThumb;
	// 文本编辑框
	private EditText etContent;
	// 字数计算器
	private TextView tvCounter;
	// 别针图片
	private ImageView ivPin;
	// 输入区域的图片
	private ImageView ivImage;
	private Bitmap image;
	private View tmpBgView;
	private Drawable background;

	private Context activity;

	private long storeid, orderid;
	private float level;

	public ShareComment(Context context, long storeid, long orderid, float level) {
		super(context);
		activity = context;
		this.storeid = storeid;
		this.orderid = orderid;
		this.level = level;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		genBackground();
		setContentView(getPageView());
		onTextChanged(etContent.getText(), 0, etContent.length(), 0);
		android.view.WindowManager.LayoutParams parames = getWindow().getAttributes();
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		int displayWidth = display.getWidth();
		parames.width = displayWidth;
		parames.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		parames.dimAmount = 0.25f;
		getWindow().setAttributes(parames);
	}

	public void setBackGround(View bgView) {
		tmpBgView = bgView;
	}

	private RelativeLayout getPageView() {
		rlPage = new RelativeLayout(getContext());
		rlPage.setBackgroundColor(0xc0323232);
		int dp_8 = dipToPx(getContext(), 8);
		int width = getScreenWidth(getContext()) - dp_8 * 2;
		RelativeLayout.LayoutParams lpDialog = new RelativeLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		lpDialog.topMargin = dp_8;
		lpDialog.bottomMargin = dp_8;
		lpDialog.addRule(RelativeLayout.CENTER_IN_PARENT);
		rlPage.setLayoutParams(lpDialog);
		rlPage.addView(getPageTitle());
		rlPage.addView(getPageBody());
		rlPage.addView(getImagePin());
		return rlPage;
	}

	// 标题栏
	private TitleLayout getPageTitle() {
		llTitle = new TitleLayout(getContext());
		llTitle.setId(1);
		llTitle.getBtnBack().setOnClickListener(this);
		int resId = getStringRes(activity, "multi_share");
		if (resId > 0) {
			llTitle.getTvTitle().setText(resId);
		}
		llTitle.getBtnRight().setVisibility(View.VISIBLE);
		resId = getStringRes(activity, "share");
		if (resId > 0) {
			llTitle.getBtnRight().setText(resId);
		}
		llTitle.getBtnRight().setOnClickListener(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		llTitle.setLayoutParams(lp);
		return llTitle;
	}

	// 页面主体
	private LinearLayout getPageBody() {
		llBody = new LinearLayout(getContext());
		llBody.setId(2);
		int resId = getBitmapRes(activity, "edittext_back");
		if (resId > 0) {
			llBody.setBackgroundResource(resId);
		}
		llBody.setOrientation(LinearLayout.VERTICAL);
		RelativeLayout.LayoutParams lpBody = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpBody.addRule(RelativeLayout.ALIGN_LEFT, llTitle.getId());
		lpBody.addRule(RelativeLayout.BELOW, llTitle.getId());
		lpBody.addRule(RelativeLayout.ALIGN_RIGHT, llTitle.getId());
		int dp_3 = dipToPx(getContext(), 3);
		lpBody.setMargins(dp_3, dp_3, dp_3, dp_3);
		llBody.setLayoutParams(lpBody);

		llBody.addView(getMainBody());
		return llBody;
	}

	private LinearLayout getMainBody() {
		LinearLayout llMainBody = new LinearLayout(getContext());
		llMainBody.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lpMain = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lpMain.weight = 1;
		int dp_4 = dipToPx(getContext(), 4);
		lpMain.setMargins(dp_4, dp_4, dp_4, dp_4);
		llMainBody.setLayoutParams(lpMain);

		LinearLayout llContent = new LinearLayout(getContext());
		LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lpContent.weight = 1;
		llMainBody.addView(llContent, lpContent);

		// 文字输入区域
		etContent = new EditText(getContext());
		etContent.setGravity(Gravity.LEFT | Gravity.TOP);
		etContent.setBackgroundDrawable(null);
		etContent.setText("这家店确实不错");
		etContent.addTextChangedListener(this);
		LinearLayout.LayoutParams lpEt = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpEt.weight = 1;
		etContent.setLayoutParams(lpEt);
		llContent.addView(etContent);

		llContent.addView(getThumbView());
		llMainBody.addView(getBodyBottom());

		return llMainBody;
	}

	// 输入区域的图片
	private RelativeLayout getThumbView() {
		rlThumb = new RelativeLayout(getContext());
		rlThumb.setId(1);
		int dp_82 = dipToPx(getContext(), 82);
		int dp_98 = dipToPx(getContext(), 98);
		LinearLayout.LayoutParams lpThumb = new LinearLayout.LayoutParams(dp_82, dp_98);
		rlThumb.setLayoutParams(lpThumb);

		ivImage = new ImageView(getContext());
		int resId = getBitmapRes(activity, "btn_back_nor");
		if (resId > 0) {
			ivImage.setBackgroundResource(resId);
		}
		ivImage.setScaleType(ScaleType.CENTER_INSIDE);
		ivImage.setImageBitmap(image);

		int dp_4 = dipToPx(getContext(), 4);
		ivImage.setPadding(dp_4, dp_4, dp_4, dp_4);
		int dp_74 = dipToPx(getContext(), 74);
		RelativeLayout.LayoutParams lpImage = new RelativeLayout.LayoutParams(dp_74, dp_74);
		int dp_16 = dipToPx(getContext(), 16);
		int dp_8 = dipToPx(getContext(), 8);
		lpImage.setMargins(0, dp_16, dp_8, 0);
		ivImage.setLayoutParams(lpImage);
		ivImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (image != null && !image.isRecycled()) {
					PicViewer pv = new PicViewer();
					pv.setImageBitmap(image);
					pv.show(activity, null);
				}
			}

		});
		rlThumb.addView(ivImage);

		Button btn = new Button(getContext());
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 取消分享图片
				rlThumb.setVisibility(View.GONE);
				ivPin.setVisibility(View.GONE);
			}
		});
		resId = getBitmapRes(activity, "img_cancel");
		if (resId > 0) {
			btn.setBackgroundResource(resId);
		}
		int dp_20 = dipToPx(getContext(), 20);
		RelativeLayout.LayoutParams lpBtn = new RelativeLayout.LayoutParams(dp_20, dp_20);
		lpBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lpBtn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		btn.setLayoutParams(lpBtn);
		rlThumb.addView(btn);

		rlThumb.setVisibility(View.GONE);
		return rlThumb;
	}

	private LinearLayout getBodyBottom() {
		LinearLayout llBottom = new LinearLayout(getContext());
		llBottom.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// 字数统计
		tvCounter = new TextView(getContext());
		tvCounter.setText(String.valueOf(MAX_TEXT_COUNT));
		tvCounter.setTextColor(0xffcfcfcf);
		tvCounter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		tvCounter.setTypeface(Typeface.DEFAULT_BOLD);
		LinearLayout.LayoutParams lpCounter = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpCounter.gravity = Gravity.CENTER_VERTICAL;
		tvCounter.setLayoutParams(lpCounter);
		llBottom.addView(tvCounter);

		return llBottom;
	}

	// 别针图片
	private ImageView getImagePin() {
		ivPin = new ImageView(getContext());
		int resId = getBitmapRes(activity, "pin");
		if (resId > 0) {
			ivPin.setImageResource(resId);
		}
		int dp_80 = dipToPx(getContext(), 80);
		int dp_36 = dipToPx(getContext(), 36);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(dp_80, dp_36);
		lp.topMargin = dipToPx(getContext(), 6);
		lp.addRule(RelativeLayout.ALIGN_TOP, llBody.getId());
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		ivPin.setLayoutParams(lp);
		ivPin.setVisibility(View.GONE);

		return ivPin;
	}

	private void genBackground() {
		background = new ColorDrawable(DIM_COLOR);
		if (tmpBgView != null) {
			try {
				Bitmap bgBm = captureView(tmpBgView, tmpBgView.getWidth(), tmpBgView.getHeight());
				bgBm = blur(bgBm, 20, 8);
				BitmapDrawable blurBm = new BitmapDrawable(activity.getResources(), bgBm);
				background = new LayerDrawable(new Drawable[] { blurBm, background });
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public void onClick(View v) {
		if (v.equals(llTitle.getBtnBack())) {
			dismiss();
			return;
		}
		if (v instanceof TextView) {
			String content = etContent.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				Toast.makeText(activity, "评论内容不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			InfoComment info = new InfoComment(storeid, orderid, level, content);
			ApiServer.getInstance(activity).comment(info, new ResponseListener() {

				@Override
				public void success(ApiMethod api, String response) {
				}

				@Override
				public void fail(ApiMethod api, VolleyError error) {

				}
			});
		}
		dismiss();
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		int remain = MAX_TEXT_COUNT - etContent.length();
		tvCounter.setText(String.valueOf(remain));
		tvCounter.setTextColor(remain > 0 ? 0xffcfcfcf : 0xffff0000);
	}

	public void afterTextChanged(Editable s) {

	}
}
