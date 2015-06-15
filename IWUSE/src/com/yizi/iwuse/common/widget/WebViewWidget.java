package com.yizi.iwuse.common.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/****
 * 重写的WebView组件
 * 
 * @author zhangxiying
 *
 */
public class WebViewWidget extends WebView {
	private Context mContext;
	
	public WebViewWidget(Context context) {
		super(context);
		mContext = context;
		init();
	}
	
	public WebViewWidget(Context context,AttributeSet attrs) {
        super(context,attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
		init();
    }
	
	/****
	 * 对webView做一些特殊的处理
	 * 
	 */
	private void init() {
		//1.先不要自动加载图片，等页面finish后再发起图片加载
	    if(Build.VERSION.SDK_INT >= 19) {
	        this.getSettings().setLoadsImagesAutomatically(true);
	    } else {
	        this.getSettings().setLoadsImagesAutomatically(false);
	    }
	    
	    //2.禁用长按事件
	    this.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				return true;
			}
	    });
	    //3.设置支持Javascript
	    this.getSettings().setJavaScriptEnabled(true); 
	    //4.触摸焦点起作用
	    requestFocus();
	    
	    //5.设置默认
	    setWebChromeClient(new MyWebChromeClient());
	    setWebViewClient(new MyWebViewClient());
	}
	

	/***
	 * 当我们做类似上拉加载下一页这样的功能的时候，页面初始的时候需要知道当前WebView是否存在纵向滚动条，
	 * 如果有则不加载下一页，如果没有则加载下一页直到其出现纵向滚动条
	 * 
	 * @return
	 */
	public boolean existVerticalScrollbar () {
	    return computeVerticalScrollRange() > computeVerticalScrollExtent();
	}
	
	/***
	 * 如果你的多个WebView是放在ViewPager里一个个加载出来的，那么就会遇到这样的问题。
	 * ViewPager首屏WebView的创建是在前台，点击时没有问题；而其他非首屏的WebView是在后台创建
	 * 滑动到它后点击页面会出现如下错误日志：
	 * webcoreglue﹕ Should not happen: no rect-based-test nodes found
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
	        onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
	    }
	    return super.onTouchEvent(ev);
	}
	
	

	class MyWebChromeClient extends WebChromeClient{  
		  
	    @Override  
	    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {   
    		//message就是wave函数里alert的字符串，这样你就可以在android客户端里对这个数据进行处理  
            result.confirm();          
    		return true;    
        }
	    
	}
	
	class MyWebViewClient extends WebViewClient{
		
		@Override
		public void onPageFinished(WebView view, String url) {
		    if(!view.getSettings().getLoadsImagesAutomatically()){
		    	view.getSettings().setLoadsImagesAutomatically(true);
		    }
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		
	}
}
