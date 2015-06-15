package com.yizi.iwuse.product.model;

/**		单品对象
 * @author hehaodong
 *
 */
public class ProductItem {

	/***标题***/
	public String title;
	/***主题属性，图片或视频***/
	public String property;
	/***主题视频或图片路径***/
	public int themeUrl;
	/***主题类目***/
	public String kind;

	public ProductItem(String title, int themeUrl,String kind, String property) {
		this.title = title;
		this.property = property;
		this.themeUrl = themeUrl;
		this.kind = kind;
	}
}
