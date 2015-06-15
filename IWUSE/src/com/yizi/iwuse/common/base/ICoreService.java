package com.yizi.iwuse.common.base;

/***
 * Service抽象接口
 * 
 * @author zhangxiying
 *
 */
public interface ICoreService {

	/****
	 * 初始化每个event需要进行的一些状态初始化
	 * 
	 * @return
	 */
	public boolean initState();
}
