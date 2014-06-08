package org.dragon.rmm.ui;

import org.dragon.rmm.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class ActMain extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View parent = getLayoutInflater().inflate(R.layout.act_main, null);
		setContentView(parent);

		int[] ids = { R.id.menu_launcher_1, R.id.menu_launcher_2, R.id.menu_launcher_3, R.id.menu_launcher_4, R.id.menu_launcher_5 };
		for (int i = 0; i < ids.length; i++) {
			parent.findViewById(ids[i]).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.menu_launcher_1: // 钟点工
			break;
		case R.id.menu_launcher_2:// 我的预约
			break;
		case R.id.menu_launcher_3:// 干洗
			break;
		case R.id.menu_launcher_4:// 保洁
			break;
		case R.id.menu_launcher_5:// 商铺
			startActivity(new Intent(this, ActShop.class));
			break;
		}
	}

	/**
	 * 捕获键盘上的返回键，避免返回后到注册登陆界面去，用户确定退出就直接退出系统
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示");
			// 设置对话框消息
			isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton(DialogInterface.BUTTON_POSITIVE, "确定", backKeyDownlistener);
			isExit.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", backKeyDownlistener);
			// 显示对话框
			isExit.show();

		}

		return false;

	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener backKeyDownlistener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

}
