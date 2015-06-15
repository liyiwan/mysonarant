package com.yizi.iwuse.common.widget;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.yizi.iwuse.R;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.general.MainHomeActivity;

/*****
 * 视频播放控件
 * 
 * @author zhangxiying
 *
 */
@SuppressLint("NewApi")
public class VideoWidget extends View implements OnCompletionListener,OnErrorListener,OnInfoListener,    
OnPreparedListener, OnSeekCompleteListener,OnVideoSizeChangedListener,SurfaceHolder.Callback {

	private static final String TAG = "VideoWidget";
	/**当前手机屏幕对象**/
	private Display currDisplay;    
    /**视频播放**/
	private SurfaceView surfaceView;   
	
	private Button btn_vdowidget_enterwuse;
    
	private SurfaceHolder holder;    
    
	private MediaPlayer player;    
    /**最终使用的宽高***/
	private int vWidth,vHeight;
	
	/****
	 * 
	 * 
	 * @param mContext
	 * @param parentView
	 * @param videoPath
	 */
	public VideoWidget(final Activity mContext, ViewGroup parentView, final String videoPath) {
		super(mContext);
		View view = LayoutInflater.from(mContext).inflate(R.layout.widget_video, parentView);
		surfaceView = (SurfaceView) view.findViewById(R.id.vdovi_videotools);
		
		btn_vdowidget_enterwuse = (Button) view.findViewById(R.id.btn_vdowidget_enterwuse);
		holder = surfaceView.getHolder();
		holder.addCallback(this);    
		
        //下面开始实例化MediaPlayer对象    
//        player = new MediaPlayer();
        try {   
        	if(player == null){
        		currDisplay = mContext.getWindowManager().getDefaultDisplay();  
        		player = MediaPlayer.create(mContext, Uri.parse(videoPath));
        		player.setOnCompletionListener(this);    
        		player.setOnErrorListener(this);    
        		player.setOnInfoListener(this);    
        		player.setOnPreparedListener(this);    
        		player.setOnSeekCompleteListener(this);    
        		player.setOnVideoSizeChangedListener(this);
        	}else{
        		
        	}
//        	player.setDataSource(mContext, Uri.parse(videoPath));
            //然后，我们取得当前Display对象    
        } catch (IllegalArgumentException e) {    
            ILog.e(TAG, e);
        } catch (IllegalStateException e) {    
        	ILog.e(TAG, e);
        }/* catch (IOException e) {    
        	ILog.e(TAG, e);
        } */
        
        btn_vdowidget_enterwuse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				player.release();
				Intent mIntent = new Intent(mContext, MainHomeActivity.class);
				mContext.startActivity(mIntent);
				mContext.finish();
			}
		});
        
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        try {
        	// 当SurfaceView中的Surface被创建的时候被调用    
        	//在这里我们指定MediaPlayer在当前的Surface中进行播放    
        	player.setDisplay(holder);    
        	//在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了    
        	//player.prepareAsync();
//			player.prepare();
//        	player.start();
		} catch (IllegalStateException e) {
			 ILog.e(TAG, e);
		}/* catch (IOException e) {
			 ILog.e(TAG, e);
		}*/
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// TODO
		
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO
	}

	@SuppressLint("NewApi")
	@Override
	public void onPrepared(MediaPlayer mp) {
		// 当prepare完成后，该方法触发，在这里我们播放视频    

		//首先取得video的宽和高    
        vWidth = mp.getVideoWidth();    
        vHeight = mp.getVideoHeight();    
        Point size = new Point();
        currDisplay.getSize(size);   
        
        if(vWidth > size.x || vHeight > size.y){    
            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放    
            float wRatio = (float)vWidth/(float)size.x;    
            float hRatio = (float)vHeight/(float)size.y;    
            
            //选择大的一个进行缩放 
            float ratio = Math.max(wRatio, hRatio);
            
            vWidth = (int)Math.ceil((float)vWidth/ratio);    
            vHeight = (int)Math.ceil((float)vHeight/ratio);    
                
        }else{
        	vWidth = size.x;    
            vHeight = size.y;   
        }
        //设置surfaceView的布局参数    
        surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(vWidth, vHeight));    
        //然后开始播放视频    
        player.start();    
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// 当一些特定信息出现或者警告时触发    
        switch(what){    
	        case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:    
	            break;    
	        case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:      
	            break;    
	        case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:    
	            break;    
	        case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:     
	            break;    
        }    
        return false;  
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		switch (what) {    
	        case MediaPlayer.MEDIA_ERROR_SERVER_DIED:    
	            ILog.v(TAG, "MEDIA_ERROR_SERVER_DIED");    
	            break;    
	        case MediaPlayer.MEDIA_ERROR_UNKNOWN:    
	        	ILog.v(TAG, "MEDIA_ERROR_UNKNOWN");    
	            break;    
	        default:    
	            break;    
        }
		mp.release();
        return false;   
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		//循环播放   
		mp.start();   
	}

	public MediaPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MediaPlayer player) {
		this.player = player;
	}
	
}
