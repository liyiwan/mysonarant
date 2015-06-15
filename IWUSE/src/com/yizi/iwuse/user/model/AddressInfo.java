package com.yizi.iwuse.user.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.yizi.iwuse.framework.model.BaseModel;

/***
 * 地址信息表
 * 
 * @author zhangxiying
 *
 */
@Table(name = "tab_address")
public class AddressInfo extends BaseModel{
	/****编码****/
	@Column(column = "code")
	public String code;
	/****父ID****/
	@Column(column = "parentId")
    public int parentId;
	/****名称****/
	@Column(column = "name")
    public String name;
	/****级别****/
	@Column(column = "level")
    public int level;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
