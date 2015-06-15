package com.yizi.iwuse.common.widget;

import com.yizi.iwuse.product.adapter.FirstItemMaxAdapter.ViewHolder;
import com.yizi.iwuse.product.service.events.VideoPlay;

import de.greenrobot.event.EventBus;

public class VideoThread extends Thread {

	private ViewHolder viewHolder;
	
	public VideoThread(ViewHolder viewHolder){
		this.viewHolder = viewHolder;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(2000);
			showVideo();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized  void showVideo(){
//    	VideoEvent videoEvent = new VideoEvent();
//    	videoEvent.setViewHolder(viewHolder);
    	EventBus.getDefault().post(new VideoPlay());
	}

}
