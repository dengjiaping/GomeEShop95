//package com.gome.ecmall.home.suitebuy;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.view.Gravity;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//import com.gome.eshopnew.R;
//
//public class SuiteBuyLookDialog extends Dialog {
//
//	public SuiteBuyLookDialog(Context context, int theme) {
//		super(context, R.style.Style_filter_dialog);
//
//	}
//
//	public SuiteBuyLookDialog(Context context) {
//		super(context, R.style.Style_filter_dialog);
//
//	}
//
//	protected void onCreate(android.os.Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		WindowManager.LayoutParams lp = getWindow().getAttributes();
//		lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//		lp.gravity = Gravity.BOTTOM;
//		lp.width = WindowManager.LayoutParams.FILL_PARENT;
//		lp.height = 50;
//		lp.dimAmount = 0.3f;
//		setContentView(R.layout.suite_buy_look_more_dialog);
//		TextView noticeImage = (TextView) findViewById(R.id.suite_notice_imageView);
//		noticeImage.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (SuiteBuyLookDialog.this != null
//						&& SuiteBuyLookDialog.this.isShowing()) {
//					SuiteBuyLookDialog.this.dismiss();
//				}
//			}
//		});
//	};
//
//	public static SuiteBuyLookDialog show(Context context) {
//		SuiteBuyLookDialog dialog = new SuiteBuyLookDialog(context);
//		dialog.show();
//		return dialog;
//	}
//
// }
