package com.yizi.iwuse.common.utils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.constants.PublicConst;
import com.yizi.iwuse.general.WelcomeActivity;

/**
 * 静态方法集成类 有转换MD5密码，解析成16进制，
 */
public class IWuseUtil {
	/** The logging tag used by this class with ILog. */
	protected static final String TAG = "IWuseUtil";
	
	/**重复快速点击事件**/
	private static long lastClickTime;

	/**
	 * 转换成MD5密码
	 * 
	 * @param source
	 *            要转换的字符串
	 * 
	 * @return MD5密码
	 */
	public static String getMD5(byte[] source) {
		String s = null;
		// 用来将字节转换成 16 进制表示的字符

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			// MD5 的计算结果是一个 128 位的长整数，
			byte tmp[] = md.digest();
			// 用字节表示就是 16 个字节

			// 每个字节用 16 进制表示的话，使用两个字符，
			char str[] = new char[16 * 2];
			// 所以表示成 16 进制需要 32 个字符

			// 表示转换结果中对应的字符位置
			int k = 0;
			// 从第一个字节开始，对 MD5 的每一个字节

			for (int i = 0; i < 16; i++) {
				// 转换成 16 进制字符的转换

				// 取第 i 个字节

				byte byte0 = tmp[i];
				// 取字节中高 4 位的数字转换,
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				// >>> 为逻辑右移，将符号位一起右移

				// 取字节中低 4 位的数字转换
				str[k++] = hexDigits[byte0 & 0xf];
			}
			// 换后的结果转换为字符串

			s = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 解析成16进制的字符串
	 * 
	 * @param strInt10
	 *            准备解析的十进制数d
	 * @return 16进制的字符串
	 */
	public static String create16Int(int strInt10) {
		String int16 = Integer.toHexString(strInt10);
		return "0x" + int16.toUpperCase();
	}

	/**
	 * Boolean类型转化
	 * 
	 * @param value
	 *            需要转化值
	 * 
	 * @param defValue
	 *            默认值
	 * 
	 * @return 返回转换值
	 */
	public static Boolean parseBoolean(Object value, Object defValue) {
		try {
			return Boolean.parseBoolean(value.toString());
		} catch (Exception e) {
			Log.e("NumberFormatException", "Boolean format Exception...");
			return parseBoolean(defValue, 0);
		}
	}

	/**
	 * Int数字转换
	 * 
	 * @param value
	 *            需要转化值
	 * 
	 * @param defValue
	 *            默认值
	 * 
	 * @return 返回转换值
	 */
	public static Integer parseInt(Object value, Object defValue) {
		if (null == value) {
			return parseInt(defValue, 0);
		}

		try {
			return Integer.parseInt(value.toString());
		} catch (Exception e) {
			Log.e("NumberFormatException", "Integer format Exception...");
			return parseInt(defValue, 0);
		}
	}

	/**
	 * Int数字转换
	 * 
	 * @param value
	 *            需要转化值
	 * 
	 * @return 返回转换值
	 */
	public static Integer parseInt(Object value) {
		return parseInt(value.toString(), 0);
	}

	/**
	 * 判断字符串是否为纯数字
	 * 
	 * @param str
	 * @return true-纯数字 false-非纯数字
	 */
	public static boolean isNumString(String str) {
		boolean isNum = false;

		if (null == str) {
			ILog.e(TAG, "the str is null .");
			return isNum;
		}

		try {
			new Integer(str);
			isNum = true;
		} catch (NumberFormatException e) {
			ILog.e(TAG, e);
		}

		return isNum;
	}

	/**
	 * @param info
	 *            androidManifest.xml里面的版本号
	 * @param ver
	 *            版本号头 如：V1
	 * @return 版本号 :V1.001
	 */
	public static String getVersion(int version, String ver) {
		NumberFormat formatter = new DecimalFormat("000");
		String number = ver + "." + formatter.format(version);
		return number.toLowerCase();
	}
	
	/*****
	 * 格式化时间
	 * 
	 * @param confTime
	 * @param template
	 * @return
	 */
	public static Date dateFormat(String confTime,String template){
		// 构造时间格式器
		if (confTime == null || confTime.length() < 14) {
			return null;
		} else {
			DateFormat format1 = new SimpleDateFormat(template);
			//DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			// 定义时间对象
			Date confDate = null;
			try {
				// 格式化时间

				confDate = format1.parse(confTime);
			} catch (ParseException e) {
				ILog.e(TAG, e);
			}
			return confDate;
		}
	}

	/**
	 * 格式化时间变量
	 * 
	 * 
	 * 
	 * @param 传递过来的时间变量
	 *            (String类型)yyyyMMddHHmmss
	 * @return 返回指定的类型.yyyy.MM.dd HH:mm
	 */
	public static String dateFormat(String confTime) {
		// 构造时间格式器
		if (confTime == null || confTime.length() < 14) {
			return "";
		} else {
			//DateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			// 定义时间对象
			Date confDate = null;
			try {
				// 格式化时间

				confDate = format2.parse(confTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// String confTime_YM= confTime.substring(0, 14);
			// 返回
			return format2.format(confDate);
		}
	}

	/**
	 * 根据地区获取不同的全球化数字
	 * 
	 * @return 全球化后的数字字符串
	 */
	public static String formatNum(int num) {
		StringBuilder returnStr = new StringBuilder(String.valueOf(num));
		// int strSize = returnStr.length();
		// // 获取国家
		// int state = ApplicationInfo.getInstance().deviceInfo.state;
		// // 获取数字分隔符
		// char devideSign = EnumGlobal.getNumDivideSign(state);
		// // 根据全球化设置不同的分隔
		// for (int i = 3; i < strSize; i += 3)
		// {
		// returnStr.insert(strSize - i, devideSign);
		// }
		return returnStr.toString();
	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 *            时间 (String类型)yyyyMMddHHmmss
	 * @return 格式化后的时间字符串
	 */
	public static String formatTime(String time, String formatString) {
		// 构造时间格式器
		if (time == null || time.length() < 14) {
			return "";
		} else {
			// 先统一用英文格式进行格式化，避免出现异常
			DateFormat format1 = new SimpleDateFormat(formatString, Locale.US);
			// 根据不同地区格式化成不同的时间格式。
			// DateFormat format2 =
			// EnumGlobal.getDateFormat(AppContext.instance().deviceServ.deviceInfo.nation);
			DateFormat format2 = DateFormat.getDateInstance(DateFormat.DEFAULT,
					AppContext.instance().getCurrentLocale());
			// 定义时间对象
			Date confDate = null;
			try {
				// 格式化时间
				confDate = format1.parse(time);
			} catch (ParseException e) {
				ILog.e(TAG, e);
			}
			// 返回
			return format2.format(confDate);
		}
	}

	/**
	 * 根据以指定分隔符隔开的字符串列表，返回ArrayList列表
	 * 
	 * @param ids
	 * @return
	 */
	public static ArrayList<Integer> change2List(String ids, String flag) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		String[] idArray = ids.split(flag);

		for (int i = 0; i < idArray.length; i++) {
			result.add(Integer.valueOf(idArray[i]));
		}
		return result;

	}

	/**
	 * 将List数据转换为数组，组装对应的格式给终端或者主机
	 * 
	 * @param list
	 * @return
	 */
	public static Integer[] change2Array(List<Integer> list) {
		Integer[] result = new Integer[list.size()];
		for (int k = 0; k < list.size(); k++) {
			result[k] = list.get(k);
		}

		return result;
	}

	/***
	 * 创建自定义对话框
	 * 
	 * @param mContext
	 * @param message
	 * @param id
	 * @return
	 */
	// public static IWuseDialog createHintDlg(Context mContext, String message,
	// String id)
	// {
	// final IWuseDialog dialog = new IWuseDialog(mContext, id);
	// dialog.setContentView(IWuseDialog.ENUM_DIALOG_VIEW.ONE_BUTTON_VIEW);
	// dialog.setTitle(mContext.getString(R.string.exception));
	// dialog.setText(message);
	// dialog.setFirstBTText(mContext.getString(R.string.alert_dialog_ok));
	// dialog.setFirstBTListener(new OnClickGernelListener()
	// {
	// @Override
	// public void onNormalClick(View v)
	// {
	// dialog.dismiss();
	// }
	// });
	// return dialog;
	//
	// }

	// /**
	// * 创建可以改变标题的对话框
	// * @param mContext
	// * @param title 对话框标题
	// * @param message
	// * @param id
	// * @return
	// */
	// public static IWuseDialog createHintDlg(Context mContext, String title,
	// String message, String id)
	// {
	// final IWuseDialog dialog = new IWuseDialog(mContext, id);
	// dialog.setContentView(IWuseDialog.ENUM_DIALOG_VIEW.ONE_BUTTON_VIEW);
	// dialog.setTitle(title);
	// dialog.setText(message);
	// dialog.setFirstBTText(mContext.getString(R.string.alert_dialog_ok));
	// dialog.setFirstBTListener(new OnClickGernelListener()
	// {
	// @Override
	// public void onNormalClick(View v)
	// {
	// dialog.dismiss();
	// }
	// });
	// return dialog;
	//
	// }

	// /**
	// * 创建一个有确定和取消的对话框
	// *
	// * @param mContext
	// * @param message 显示的字符串
	// * @return
	// */
	// public static IWuseDialog createOkCancelDlg(Context mContext, String
	// message, String id)
	// {
	// final IWuseDialog dialog = new IWuseDialog(mContext, id);
	// dialog.setContentView(IWuseDialog.ENUM_DIALOG_VIEW.TWO_BUTTON_VIEW);
	// // DTS2011123005431 2012-1-30 xkf48808 修改错把"确认"当作对话框标题的问题 modify start
	// // dialog.setTitle(R.string.confirm);
	// dialog.setTitle("");
	// // DTS2011123005431 2012-1-30 xkf48808 修改错把"确认"当作对话框标题的问题 modify end
	// dialog.setText(message);
	// dialog.setFirstBTText(mContext.getString(R.string.confirm));
	// dialog.setFirstBTListener(new OnClickGernelListener()
	// {
	// @Override
	// public void onNormalClick(View v)
	// {
	// dialog.dismiss();
	// }
	// });
	//
	// dialog.setSecondBTText(mContext.getString(R.string.cancel));
	// dialog.setSecondBTListener(new OnClickGernelListener()
	// {
	// @Override
	// public void onNormalClick(View v)
	// {
	// dialog.dismiss();
	// }
	// });
	// return dialog;
	//
	// }

	/**
	 * 二分查找 注意：二分查找只是针对有序排列的各种数组或集合 创建等待对话框
	 * 
	 * @param mContext
	 * @param resId
	 *            显示信息的资源id
	 * @return HIDDialog
	 */
	// public static IWuseDialog createWaitingDlg(Context mContext, int resId,
	// String id)
	// {
	// return createWaitingDlg(mContext, mContext.getString(resId), id);
	// }

	/**
	 * 创建等待对话框
	 * 
	 * @param mContext
	 * @param message
	 *            显示信息
	 * @return HIDDialog
	 */
	// public static IWuseDialog createWaitingDlg(Context mContext, String
	// message, String id)
	// {
	// IWuseDialog WaitingDialog = new IWuseDialog(mContext,
	// R.style.dialog_wait, id);
	// if (null == message)
	// {
	// message = mContext.getString(R.string.ui_please_wait);
	// }
	//
	// WaitingDialog.setContentView(R.layout.ui_waiting,
	// (int) mContext.getResources().getDimension(R.dimen.wait_dialog_width),
	// (int) mContext.getResources()
	// .getDimension(R.dimen.wait_dialog_height));
	//
	// // 必须放在设置 view之后,自定义的view是不能设置该方法的，注释掉
	// ((TextView)
	// WaitingDialog.findViewById(R.id.dialog_text)).setText(message);
	// Window win = WaitingDialog.getWindow();
	// WindowManager.LayoutParams lp = win.getAttributes();
	//
	// lp.y = lp.y +
	// mContext.getResources().getDimensionPixelOffset(R.dimen.toast_gravity_yoffset);
	// win.setAttributes(lp);
	// ILog.v(TAG, "create waite dialog...");
	// return WaitingDialog;
	// }

	/**
	 * 超长信息提醒对话框
	 * 
	 * @param mContext
	 * @param message
	 *            显示信息
	 * @return HIDDialog
	 */
	// public static IWuseDialog createLongMsgDlg(Context mContext, String
	// message, String id)
	// {
	// IWuseDialog.dismissDialog(id);
	// IWuseDialog WaitingDialog = new IWuseDialog(mContext,
	// R.style.dialog_wait, id);
	// WaitingDialog.setContentView(R.layout.ui_long_msg_dlg,
	// (int) mContext.getResources().getDimension(R.dimen.wait_dialog_width),
	// (int) mContext.getResources()
	// .getDimension(R.dimen.wait_dialog_height));
	//
	// // 必须放在设置 view之后,自定义的view是不能设置该方法的，注释掉
	// ((TextView)
	// WaitingDialog.findViewById(R.id.dialog_text)).setText(message);
	// Window win = WaitingDialog.getWindow();
	// WindowManager.LayoutParams lp = win.getAttributes();
	//
	// lp.y = lp.y +
	// mContext.getResources().getDimensionPixelOffset(R.dimen.toast_gravity_yoffset);
	// win.setAttributes(lp);
	// ILog.v(TAG, "create waite dialog...");
	// return WaitingDialog;
	// }

	/**
	 * 二分查找 注意：二分查找只是针对有序排列的各种数组或集合 如果没有找到，返回序号-1
	 * 
	 * @param offset
	 *            从开头第几个开始查找
	 * @param target
	 *            查找的目标
	 * @param siteList
	 *            列表
	 * @return
	 */
	// public static int binarySearchSite(int offset, char target, int
	// specialSiteIndex, List<?> siteList)
	// {
	// int front = offset;
	// int tail = siteList.size() - 1;
	// int lastOfList = tail;
	// int targetIndex = -1;
	// boolean findFlag = false;
	// ILog.v(TAG, "target:" + target);
	//
	// // 当列表中只有一个会场直接返回
	// if (front <= tail)
	// {
	// // 如果索引小于第一个，则跳到第一个
	// if (siteList.get(offset).pinyin.charAt(0) > target)
	// {
	// targetIndex = front;
	// findFlag = true;
	// }
	// // 如果索引大于最后一个，则跳到最后一个
	// if (siteList.get(tail).pinyin.charAt(0) < target)
	// {
	// targetIndex = tail;
	// findFlag = true;
	// }
	// if (!findFlag)
	// {
	// // 判断子数组是否能再次二分
	// while (front < tail)
	// {
	// // 获取子数组的中间位置，并依据此中间位置进行二分
	// int middle = (front + tail) / 2;
	// int curIndex = siteList.get(middle).pinyin.charAt(0);
	//
	// // 找到一样的
	// if (curIndex == target)
	// {
	// // 如果中间那个找到了，还要看看前面一个是咋样,如果前一个不是同样的或是前一个已经到了列表的第一个，直接返回
	// if ((middle - 1 <= 0)
	// || (middle - 1 >= 0 && siteList.get(middle - 1).pinyin.charAt(0) !=
	// target))
	// {
	// targetIndex = middle;
	// findFlag = true;
	// break;
	// }
	// else
	// {
	// // 如果前一个也是
	// tail = middle - 1;
	// }
	// }
	// else if (curIndex > target)
	// {
	// tail = middle - 1;
	// }
	// else
	// {
	// front = middle + 1;
	// }
	// }
	//
	// if (siteList.get(front).pinyin.charAt(0) == target)
	// {
	// targetIndex = front;
	// findFlag = true;
	// }
	// }
	//
	// // 如果找不到，则前移
	// if (!findFlag)
	// {
	// if (siteList.get(tail).pinyin.charAt(0) > target)
	// {
	// targetIndex = tail;
	// }
	// else if (siteList.get(front).pinyin.charAt(0) > target)
	// {
	// targetIndex = front;
	// }
	// else
	// {
	// targetIndex = front + 1;
	// }
	// findFlag = true;
	// }
	//
	// if (findFlag)
	// {
	// // 如果对应的会场在列表中的位置<=主席端广播会场或非主席端广播会场的位置，则需要前移
	// if (specialSiteIndex != -1)
	// {
	// if (targetIndex < specialSiteIndex)
	// {
	// targetIndex++;
	// }
	// else if (targetIndex == specialSiteIndex && specialSiteIndex !=
	// lastOfList)
	// {
	// targetIndex++;
	// }
	// }
	// if (targetIndex <= lastOfList)
	// {
	// return targetIndex;
	// }
	// }
	// }
	// return -1;
	// }

	/**
	 * 截断字符,并根据配置文件中的layout宽度，自动变换显示的字体大小，从而使得截断后的信息能够完整的显示出来
	 * 
	 * @param singleLineEngWidth
	 *            每一行的英文物理宽度
	 * @param text
	 *            需要截断的文字
	 * @param container
	 *            显示text的容器
	 * @param maxWidth
	 *            容器layout的宽度值
	 * @param rawTextSize
	 *            初始配置中设置的字体大小
	 * @param isMultiPic
	 *            初始配置中设置的字体大小
	 */
	public static String formatStrTwoLine(int singleLineEngWidth, String text,
			TextView container, float maxWidth, int rawTextSize,
			boolean isMultiPic) {
		String rawString;

		if (container == null || text == null) {
			return "";
		}

		Paint tvPaint = container.getPaint();
		// 将container的layout宽度值变大，从而使得能够容下所有字符同时使得显示的姿态大小尽可能的大，1.7这个系数是多次试验后得出的结果
		maxWidth *= 1.7;
		rawString = formatStrTwoLine(singleLineEngWidth, text);
		container.setTextSize(rawTextSize);
		// 保证根据所选的字体大小得到的将显示字符串的宽度值应小于container的layout宽度上限
		while (tvPaint.measureText(rawString) > maxWidth && rawTextSize > 0) {
			rawTextSize--;// 递减字体大小
			container.setTextSize(rawTextSize);
		}

		return rawString;
	}

	/**
	 * 截断字符
	 * 
	 * @param singleLineEngWidth
	 *            每一行的英文物理宽度
	 * @param text
	 *            需要截断的文字
	 */
	public static String formatStrTwoLine(int singleLineEngWidth, String text) {
		if (null == text) {
			return "";
		}
		final int EN_WIDTH = 1;
		final int CH_WIDTH = 2;

		StringBuilder returnStr = new StringBuilder();
		String remainText = new String(text);
		// 截断除最后一行的每一行
		int length = 0;
		for (int i = 0; i < remainText.length(); i++) {
			char checkingChar = remainText.charAt(i);
			// 中文
			if (String.valueOf(checkingChar).getBytes().length > 1) {
				length += CH_WIDTH;
			}
			// 英文
			else {
				length += EN_WIDTH;
			}
		}
		// 如果总长度小于指定一行的英文物理宽度，直接返回字符串
		if (length <= singleLineEngWidth) {
			return text;
		}

		// 如果指定的一行物理宽度太小，直接返回字符转
		if (singleLineEngWidth < 5) // 指定行宽的最小值5
		{
			return text;
		}
		// 如果原始字串的总长度在指定的宽度的1行到2行之间
		else if (length > singleLineEngWidth
				&& length <= (singleLineEngWidth * 2)) {
			length = 0;
			for (int i = 0; i < (singleLineEngWidth * 2); i++) {
				char checkingChar = remainText.charAt(i);
				// 中文
				if (String.valueOf(checkingChar).getBytes().length > 1) {
					length += CH_WIDTH;
				}
				// 英文
				else {
					length += EN_WIDTH;
				}
				if (length >= singleLineEngWidth) {
					returnStr.append(remainText.substring(0, i + 1));
					returnStr.append(PublicConst.SPLIT_LINE);
					remainText = remainText.substring(i + 1);
					return returnStr + remainText;
				}
			}
		} else
		// 如果大于2行的宽度
		{
			length = 0;
			for (int i = 0; i < (singleLineEngWidth * 2); i++) {
				char checkingChar = remainText.charAt(i);
				// 中文
				if (String.valueOf(checkingChar).getBytes().length > 1) {
					length += CH_WIDTH;
				}
				// 英文
				else {
					length += EN_WIDTH;
				}
				if (length >= singleLineEngWidth) {
					returnStr.append(remainText.substring(0, i + 1));
					returnStr.append(PublicConst.SPLIT_LINE);
					remainText = remainText.substring(i + 1);
					break;
				}
			}
			// 半行的位置，英文长度的中间位置，如果不是偶数，加1凑成再算
			int halSigleLineEngWidth = (singleLineEngWidth / 2) % 2 == 0 ? singleLineEngWidth / 2
					: singleLineEngWidth / 2 + 1;
			length = 0;
			for (int i = 0; i < halSigleLineEngWidth - 1; i++) {
				char checkingChar = remainText.charAt(i);
				if (String.valueOf(checkingChar).getBytes().length > 1) {
					length += CH_WIDTH;
				}
				// 英文
				else {
					length += EN_WIDTH;
				}
				if (length >= halSigleLineEngWidth - 2) {
					returnStr.append(remainText.substring(0, i));
					break;
				}
			}

			length = 0;
			for (int i = 0; i < halSigleLineEngWidth - 1; i++) {
				char checkingChar = remainText.charAt(remainText.length() - i
						- 1);
				if (String.valueOf(checkingChar).getBytes().length > 1) {
					length += CH_WIDTH;
				}
				// 英文
				else {
					length += EN_WIDTH;
				}
				if (length >= halSigleLineEngWidth - 2) {
					returnStr.append("...");
					returnStr.append(remainText.substring(remainText.length()
							- i - 1));
					break;
				}
			}
		}

		return returnStr.toString();
	}

	public static String formatStrOneLine(int singleLineEngWidth, String text) {
		if (null == text) {
			return "";
		}

		final int EN_WIDTH = 1;
		final int CH_WIDTH = 2;
		StringBuilder returnStr = new StringBuilder();
		String remainText = new String(text);
		// 截断除最后一行的每一行
		int length = 0;
		for (int i = 0; i < remainText.length(); i++) {
			char checkingChar = remainText.charAt(i);
			// 中文
			if (String.valueOf(checkingChar).getBytes().length > 1) {
				length += CH_WIDTH;
			}
			// 英文
			else {
				length += EN_WIDTH;
			}
		}
		// 如果总长度小于指定一行的英文物理宽度，直接返回字符串
		if (length <= singleLineEngWidth) {
			return text;
		}

		// 如果指定的一行物理宽度太小，直接返回字符转
		if (singleLineEngWidth < 5) // 指定行宽的最小值5
		{
			return text;
		}

		// 半行的位置，英文长度的中间位置，如果不是偶数，加1凑成再算
		int halSigleLineEngWidth = (singleLineEngWidth / 2) % 2 == 0 ? singleLineEngWidth / 2
				: singleLineEngWidth / 2 + 1;

		length = 0;
		for (int i = 0; i < halSigleLineEngWidth - 1; i++) {
			char checkingChar = remainText.charAt(i);
			if (String.valueOf(checkingChar).getBytes().length > 1) {
				length += CH_WIDTH;
			}
			// 英文
			else {
				length += EN_WIDTH;
			}
			if (length >= halSigleLineEngWidth - 2) {
				returnStr.append(remainText.substring(0, i));
				break;
			}
		}

		length = 0;
		for (int i = 0; i < halSigleLineEngWidth - 1; i++) {
			char checkingChar = remainText.charAt(remainText.length() - i - 1);
			if (String.valueOf(checkingChar).getBytes().length > 1) {
				length += CH_WIDTH;
			}
			// 英文
			else {
				length += EN_WIDTH;
			}
			if (length >= halSigleLineEngWidth - 2) {
				returnStr.append("...");
				returnStr.append(remainText.substring(remainText.length() - i
						- 1));
				break;
			}
		}

		return returnStr.toString();
	}

	/**
	 * 根据给定的容器和最大宽度自匹配字符长度，中间省略
	 * 
	 * @param Container
	 * @param text
	 * @param maxLenght
	 *            ：显示的最大长度
	 * @param maxWidth
	 *            ：textView的最大宽度
	 * @return
	 */
	public static String formatWrapContainer(TextView Container, String text,
			int maxLenght, int maxWidth) {
		if (Container == null || text == null) {
			return "";
		}

		Paint tvPaint = Container.getPaint();
		String adaptText;

		int maxLenghtOfText = maxLenght;

		adaptText = IWuseUtil.formatStrOneLine(maxLenghtOfText, text); // 请求发言列表长度为36个英文字符

		while (tvPaint.measureText(adaptText) > maxWidth && maxLenghtOfText > 0) {
			maxLenghtOfText -= 2;// 递减长度
			adaptText = IWuseUtil.formatStrOneLine(maxLenghtOfText, text);

		}
		return adaptText;
	}

	/**
	 * 防止view被多方引用，无法被GC回收
	 * 
	 * @param view
	 */
	public static void unbindDrawables(View view) {
		if (null == view) {
			return;
		}

		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}

			// 屏蔽掉不支持removeAllViews的view的异常
			try {
				((ViewGroup) view).removeAllViews();
			} catch (Exception e) {

			}
		}
	}

	public static boolean isPopWindowExist(Context mContext) {
		WindowManager mWindowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		// mWindowManager.
		return false;
	}

	/**
	 * 用来日志跟踪
	 * 
	 * @param develope
	 */
	public static void strickmode() {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			// StrictMode.ThreadPolicy policy = new
			// StrictMode.ThreadPolicy.Builder().permitAll().build();
			// StrictMode.setThreadPolicy(policy);
		}
	}

	/**
	 * 在出现异常之后重启整个HID
	 * 
	 * @param activity
	 */
	public static synchronized void restartIWuse(Context activity) {
		ILog.i(TAG, "iwuse restarted.");
		if (null != activity) {
			AlarmManager mgr = (AlarmManager) activity
					.getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,
					WelcomeActivity.pdIntent);
			System.exit(0);
		}
	}

	public static void reConnectIWuse(Context context) {
		if (null != context) {
			Intent intent = new Intent(context, WelcomeActivity.class);
			context.startActivity(intent);
		}
	}

	/**
	 * 比例HID版本，判断是否需要升级
	 * 
	 * @param t1
	 * @param t2
	 * @return 0为相等，不需要升级；负数为ver1小于ver2，需要升级；正数为ver1大于ver2，不需要升级
	 */
	public static int upgradeVersionCompare(String ver1, String ver2) {
		try {
			ver1 = ver1.toUpperCase().trim();
			ver2 = ver2.toUpperCase().trim();
			if (ver1.contains(PublicConst.VER_PRE)) {
				ver1 = ver1.substring(PublicConst.VER_PRE.length()).trim();
			}
			if (ver2.contains(PublicConst.VER_PRE)) {
				ver2 = ver2.substring(PublicConst.VER_PRE.length()).trim();
			}
			ILog.d("ver1:", ver1);
			ILog.d("ver2:", ver2);
			return ver1.compareTo(ver2);
		} catch (Exception e) {
			ILog.e(TAG, e);
			return 0;
		}
	}

	/**
	 * 判断是版本是否为标准版本命名方式<br>
	 * 标准的方式为：IWuse V100R001C01B100
	 * 
	 * @param version
	 * @return true-标准、false-非标准
	 */
	public static boolean isStandardVersionName(String version) {
		if (version == null) {
			ILog.e(TAG, "version String is null . ");
			return false;
		}

		version = version.toUpperCase().trim();

		if (version.contains(PublicConst.VER_PRE)) {
			return true;
		}

		return false;
	}

	/**
	 * 获取当前的上下文环境
	 * 
	 * @return
	 */
	public static Context getCurrentContext(Context context) {
		// 当前的所有上下文
		String mMainHomeActivity = "MainHomeActivity";

		String mWelcomeActivity = "WelcomeActivity";

		ActivityManager am = null;

		if (am == null) {
			am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
		}

		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String topActivityName = cn.getClassName();

		if (topActivityName != null) {
			// if (topActivityName.contains(mMainHomeActivity))
			// {
			// return MainHomeActivity.getApp();
			// }
			if (topActivityName.contains(mWelcomeActivity)) {
				return context;// WelcomeActivity.getApp();
			}
		}
		return context;
	}
	
	/***
	 * 测试用，获取测试引导图片
	 * 
	 * @param id
	 * @return
	 */
	public static View getImageView(Context context,int id) {  
        ImageView imgView = new ImageView(context);  
        imgView.setId(id);  
        imgView.setImageResource(id); 
        imgView.setScaleType(ScaleType.FIT_XY);
        int bmpW = BitmapFactory.decodeResource(context.getResources(), id).getWidth();// 获取图片宽度
        int[] display = AppContext.instance().disPlay;
        int screenW = display[0];// 获取分辨率宽度
        float offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imgView.setImageMatrix(matrix);
        
        return imgView;  
    }
	
	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	public static int getStatusBarHeight(Context context) {
		Class c = null;
		Object bj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			bj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(bj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/*****
	 * 防止快速点击,事件间隔为 500ms
	 * 
	 * @return
	 */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < PublicConst.CLICKINTERVALTIME) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
    
    public static boolean editTextIsNull(EditText editText){
    	String editStr = editText.getText().toString().trim();
    	if("".equals(editStr)){
    		return true;
    	}else{
    		return false;
    	}
    }
	
}
