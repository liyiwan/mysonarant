package com.yizi.iwuse.user.database;

import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.framework.database.DataBaseManager;
import com.yizi.iwuse.user.model.ShoppingItem;

/**		用户数据库相关管理
 * @author hehaodong
 *
 */
public class UserDataManager {

	private DbUtils dbUtils;
	
	public UserDataManager(){
		dbUtils = DataBaseManager.getInstance(AppContext.instance().globalContext).dbUtils;
	}
	
	/**
	 * 	创建购物车表
	 */
	public void creatShoppingTable(){
		try {
			dbUtils.createTableIfNotExist(ShoppingItem.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**		保存购物车物品项，存在改物品则加一，不存在则插入物品
	 * @param shoppingItem
	 */
	public void saveShopping(ShoppingItem shoppingItem){
		try {
			ShoppingItem shoppingItem2 = dbUtils.findById(ShoppingItem.class, shoppingItem.id);
			if(shoppingItem2 != null){
				shoppingItem2.productNum = shoppingItem2.productNum + 1;
				dbUtils.replace(shoppingItem2);
			}else{
				dbUtils.replace(shoppingItem);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**		保存购物车物品项，存在则替换该物品，不存在则插入物品
	 * @param shoppingItem
	 */
	public void saveShoppingNoChange(ShoppingItem shoppingItem){
		try {
			dbUtils.replace(shoppingItem);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**		更改指定用户购物车物品所属
	 * @param userId
	 */
	public void updataShoppingBelong(String userId){
		List<ShoppingItem> shoppingList = getShoppingArray("visitor");
		if(shoppingList == null){
			return;
		}
		for(ShoppingItem shoppingItem : shoppingList){
			shoppingItem.userId = "abc";
			saveShoppingNoChange(shoppingItem);
		}
	}
	
	/**		获取指定用户购物车物品列表
	 * @param userId
	 * @return
	 */
	public List<ShoppingItem> getShoppingArray(String userId){
		try {
			List<ShoppingItem> shoppingList = dbUtils
					.findAll(Selector.from(ShoppingItem.class)
							.where("userId", "=", userId));
			return shoppingList;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**		获取指定用户购物车物品数量
	 * @param userId
	 * @return
	 */
	public int getShoppingNum(String userId){
		try {
			List<ShoppingItem> shoppingList = dbUtils
					.findAll(Selector.from(ShoppingItem.class)
							.where("userId", "=", userId));
			int shoppingNum = 0;
			if(shoppingList == null){
				return 0;
			}
			for(int i=0;i < shoppingList.size();i++){
				ShoppingItem shoppingItem = shoppingList.get(i);
				shoppingNum = shoppingNum + shoppingItem.productNum;
			}
			return shoppingNum;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	
	/**		删除某一购物车物品项
	 * @param shoppingItem
	 */
	public void delShopping(ShoppingItem shoppingItem){
		try {
			dbUtils.delete(shoppingItem);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
