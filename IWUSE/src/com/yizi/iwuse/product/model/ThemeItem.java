package com.yizi.iwuse.product.model;

/**		主题对象
 * @author hehaodong
 *
 */
public class ThemeItem {

	/***标题***/
	public String title;
	/***主题属性，图片或视频***/
	public String property;
	/***图片ID***/
	public int picUrl;
	/***视频路径***/
	public String videoUrl;
	/***主题类目***/
	public String kind;

	public ThemeItem(String title, int picUrl,String kind, String property, String videoUrl) {
		this.title = title;
		this.property = property;
		this.picUrl = picUrl;
		this.kind = kind;
		this.videoUrl = videoUrl;
	}
	
}
