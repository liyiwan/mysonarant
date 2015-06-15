package com.yizi.iwuse.common.widget;

import com.yizi.iwuse.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**		自定义dialog
 * @author hehaodong
 *
 */
public class CustomDialog extends Dialog {

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public CustomDialog(Context context) {
		super(context);
	}

	public static class Builder {

		private Context context;
		private String title;
		private String message;
		private int positiveButtonTextId = -1;
		private int negativeButtonTextId = -1;
		private int layoutId = -1;

		private DialogInterface.OnClickListener positiveButtonClickListener,
				negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}


		/**		设置消息内容
		 * @param message
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**		设置layout样式文件
		 * @param layoutId
		 * @return
		 */
		public Builder setLinearLayout(int layoutId) {
			this.layoutId = layoutId;
			return this;
		}

		/**
		 * 设置标题
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}


		/**
		 * 设置确定按钮和监听
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonTextId,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonTextId = positiveButtonTextId;
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * 设置取消按钮和监听
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(int negativeButtonTextId,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonTextId = negativeButtonTextId;
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * 创建dialog
		 */
		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CustomDialog dialog = new CustomDialog(context,
					R.style.DialogStyle);
			View layout;
			if(layoutId != -1){
				layout = inflater.inflate(layoutId, null);
			}else{
				layout = inflater.inflate(R.layout.custom_dialog, null);
			}
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			if (title != null) {
				((TextView) layout.findViewById(R.id.tv_title)).setText(title);
			} else {
				((TextView) layout.findViewById(R.id.tv_title))
						.setVisibility(View.GONE);
			}
			((TextView) layout.findViewById(R.id.tv_title)).setText(title);
			if (message != null) {
				((TextView) layout.findViewById(R.id.tv_message))
						.setText(message);
			} else {
				((TextView) layout.findViewById(R.id.tv_message))
						.setVisibility(View.GONE);
			}

			if (positiveButtonTextId != -1 || negativeButtonTextId != -1) {
				layout.findViewById(R.id.ll_bottom_btn).setVisibility(
						View.VISIBLE);
			}

			if (positiveButtonTextId != -1) {
				((Button) layout.findViewById(R.id.btn_positive))
						.setText(positiveButtonTextId);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.btn_positive))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.btn_positive).setVisibility(View.GONE);
			}
			if (negativeButtonTextId != -1) {
				((Button) layout.findViewById(R.id.btn_negative))
						.setText(negativeButtonTextId);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.btn_negative))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.btn_negative).setVisibility(View.GONE);
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}

}