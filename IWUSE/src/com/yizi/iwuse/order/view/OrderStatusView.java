package com.yizi.iwuse.order.view;

import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * 状态流程图view
 * @author zhangxiying
 *
 */
public class OrderStatusView extends View {

	private Paint mPaintLinePink;
    private Paint mCircleFill;
    private Paint mPaintTextBlack;
    private Paint mPaintTextGray;
    private Paint mPaintLineGray;
    private Paint mCircleStroke;

    private float mLeft = 35;// 直线距离左边的距离
    private float mRight = 35;// 直线距离右边的距离
    private float mLineHeight = 25;// 直线Y轴的距离
    private float circleSize = 18;// 圆点的大小
    private float mPiceWidth;
    private float mLineBold = 3;// 直线的粗细程度
    private float mdivider = 35;// 直线和文字的间隔距离
    private int textSize = 12;// 文字大小

    private boolean isInitData = false;

    private int screenHeight;
    private int screenWidth;
    private float radio;//
    
    private Context mContext;

    private int mBlackIndex = 0;// 黑色字体的个数

    private static String[] order_wait_pay = {
            "确认订单", "付款", "待发货", "发货中","收货"
    };
    private static String[] order_cancel = {
            "确认订单", "付款", "已取消"
    };
    private static String[] order_waiting = {
            "确认订单", "已发货", "待发货", "未开始"
    };
    private static String[] order_logitics = {
            "确认订单", "已发货", "待发货", "发货中"
    };
    private static String[] order_done = {
            "确认订单", "已发货", "已完成"
    };
    private String[] mShowState = {};

    public OrderStatusView(Context context) {
        super(context);
        mContext = context;
    }

    public void setInfo(Display display, String mState) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        //display.getMetrics(dm);
        radio = dm.scaledDensity;
        init();
        getShowState(mState);
        this.screenHeight = 150;
        this.screenWidth = AppContext.instance().disPlay[0];
        postInvalidate();
    }

    private void init() {
        if (!isInitData) {
            mLeft = mLeft * radio;
            mRight = mRight * radio;
            mLineHeight = mLineHeight * radio;
            circleSize = circleSize * radio;
            mLineBold = mLineBold * radio;
            mdivider = mdivider * radio;
            textSize = (int) (textSize * radio);
            isInitData = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);// 重写onDraw方法

        mPaintTextBlack = new Paint();
        mPaintTextBlack.setStyle(Paint.Style.FILL);
        mPaintTextBlack.setAntiAlias(true);// 去锯齿
        mPaintTextBlack.setColor(Color.parseColor("#111111"));// 颜色
        mPaintTextBlack.setTextSize(textSize);
        mPaintTextBlack.setTextAlign(Paint.Align.CENTER);

        mPaintTextGray = new Paint();
        mPaintTextGray.setStyle(Paint.Style.FILL);
        mPaintTextGray.setAntiAlias(true);// 去锯齿
        mPaintTextGray.setColor(Color.parseColor("#dadada"));// 颜色
        mPaintTextGray.setTextSize(textSize);
        mPaintTextGray.setTextAlign(Paint.Align.CENTER);

        mPaintLinePink = new Paint();
        mPaintLinePink.setStyle(Paint.Style.FILL);
        mPaintLinePink.setAntiAlias(true);// 去锯齿
        mPaintLinePink.setColor(mContext.getResources().getColor(R.color.main_color));// 颜色
        mPaintLinePink.setTextSize(textSize);
        mPaintLinePink.setTextAlign(Paint.Align.CENTER);

        mPaintLineGray = new Paint();
        mPaintLineGray.setStyle(Paint.Style.FILL);
        mPaintLineGray.setAntiAlias(true);// 去锯齿
        mPaintLineGray.setColor(Color.parseColor("#dadada"));// 颜色
        mPaintLineGray.setTextSize(textSize);
        mPaintLineGray.setTextAlign(Paint.Align.CENTER);

        mCircleFill = new Paint();
        mCircleFill.setStyle(Paint.Style.FILL);
        mCircleFill.setAntiAlias(true);// 去锯齿
        mCircleFill.setColor(mContext.getResources().getColor(R.color.main_color));// 颜色
        mCircleFill.setStrokeWidth(6);
        

        mCircleStroke = new Paint();
        mCircleStroke.setStyle(Paint.Style.STROKE);
        mCircleStroke.setAntiAlias(true);// 去锯齿
        mCircleStroke.setColor(Color.parseColor("#dadada"));// 颜色
        mCircleStroke.setStrokeWidth(6);

        mPiceWidth = (screenWidth - mLeft - mRight) / (mShowState.length - 1);
        for (int i = 0; i < mShowState.length; i++) {
            if (i < mBlackIndex) {
                canvas.drawText(mShowState[i], mLeft + i * mPiceWidth, mLineHeight + mdivider,
                        mPaintTextBlack);
                canvas.drawCircle(mLeft + i * mPiceWidth, mLineHeight, circleSize-4, mCircleFill);
                canvas.drawCircle(mLeft + i * mPiceWidth, mLineHeight, circleSize, mCircleStroke);
                if (i != 0) {
                    canvas.drawRect(mLeft + (i - 1) * mPiceWidth + circleSize-6, mLineHeight
                            - mLineBold / 2,
                            mLeft + (i) * mPiceWidth - circleSize+6, mLineHeight + mLineBold / 2,
                            mPaintLinePink);
                }
            } else {
                canvas.drawText(mShowState[i], mLeft + i * mPiceWidth, mLineHeight + mdivider,
                        mPaintTextGray);
                canvas.drawCircle(mLeft + i * mPiceWidth, mLineHeight, circleSize, mCircleStroke);
                canvas.drawRect(mLeft + (i - 1) * mPiceWidth + circleSize, mLineHeight - mLineBold
                        / 2, mLeft + (i) * mPiceWidth - circleSize, mLineHeight + mLineBold / 2,
                        mPaintLineGray);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(screenWidth, screenHeight);
    }

    private void getShowState(String mState) {
        if ("10".equals(mState)) {// 待支付
            mShowState = order_wait_pay;
            mBlackIndex = 2;
        } else if ("0".equals(mState)) {// 订单待确认
            mShowState = order_wait_pay;
            mBlackIndex = 1;
        } else if ("1".equals(mState)) {// 代发货
            mShowState = order_wait_pay;
            mBlackIndex = 3;
        } else if ("2".equals(mState)) {// 发货中
            mShowState = order_logitics;
            mBlackIndex = 4;
        } else if ("3".equals(mState)) {// 已完成
            mShowState = order_done;
            mBlackIndex = 3;
        } else {// 已取消
            mShowState = order_cancel;
            mBlackIndex = 3;
        }
    }
}
