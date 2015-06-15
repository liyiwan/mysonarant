package com.yizi.iwuse.common.base;

/****
 * 清理出之前加载过的View，防止同一个Child被多个父项引用
 * 
 * @author zhangxiying
 *
 */
public interface IActivity {
	public void removeAllView();
}
