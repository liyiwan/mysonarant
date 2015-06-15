package com.yizi.iwuse.user.view;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.yizi.iwuse.AppContext;
import com.yizi.iwuse.R;
import com.yizi.iwuse.common.utils.ILog;
import com.yizi.iwuse.common.widget.wheel.OnWheelChangedListener;
import com.yizi.iwuse.common.widget.wheel.WheelView;
import com.yizi.iwuse.common.widget.wheel.adapters.ArrayWheelAdapter;
import com.yizi.iwuse.constants.GeneralConst;
import com.yizi.iwuse.general.service.XmlParserHandler;
import com.yizi.iwuse.general.service.events.AddressEditEvent;
import com.yizi.iwuse.user.model.CityModel;
import com.yizi.iwuse.user.model.DistrictModel;
import com.yizi.iwuse.user.model.ProvinceModel;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

/***
 * 地址选择弹出框
 * 
 * @author zhangxiying
 *
 */
public class AddrSelectPopupWindow extends PopupWindow {
	private static final String TAG = "AddrSelectPopupWindow";
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private Button btn_cancel;
	private Button btn_reset;
	private Button btn_confirm;
	
	private Context mContext;
	private View mMenuView;
	
	/**所有省*/
	private String[] mProvinceDatas;
	/**key - 省 value - 市*/
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/** key - 市 values - 区*/
	private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	
	/** key - 区 values - 邮编*/
	private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 

	/** 当前省的名称*/
	private String mCurrentProviceName;
	/** 当前市的名称*/
	private String mCurrentCityName;
	/** 当前区的名称*/
	private String mCurrentDistrictName ="";
	
	/**当前区的邮政编码*/
	private String mCurrentZipCode ="";
	 
	private static AddrSelectPopupWindow mAddrSelectPopupWindow;
	private  AddrSelectPopupWindow(Context mContext) {
		super(mContext);
		this.mContext = mContext;
		mMenuView = LayoutInflater.from(mContext).inflate(R.layout.layout_popup_addrselect, null);
		this.setContentView(mMenuView);
		initPopup();
		initView();
		initData();
	}
	/***
	 * 单例弹出框
	 * @param mContext
	 * @return
	 */
	public static AddrSelectPopupWindow getInstance(Context mContext){
		synchronized (AddrSelectPopupWindow.class) {
			if (mAddrSelectPopupWindow == null) {
				mAddrSelectPopupWindow = new AddrSelectPopupWindow(mContext);
			}
		}
		return mAddrSelectPopupWindow;
	} 
	/***
	 * 初始化弹出框显示
	 * 
	 */
	private void initPopup(){
		//设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.FILL_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimBottom);  
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        mMenuView.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = mMenuView.findViewById(R.id.lay_operate).getTop();  
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height){  
                       dismiss();  
                    }  
                }                 
                return true;  
            }  
        });
	}
	
	private void initView(){
		mViewProvince = (WheelView) mMenuView.findViewById(R.id.id_province);
		mViewCity = (WheelView) mMenuView.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) mMenuView.findViewById(R.id.id_district);
		btn_cancel = (Button)mMenuView.findViewById(R.id.btn_cancel);
		btn_reset = (Button)mMenuView.findViewById(R.id.btn_reset);
		btn_confirm = (Button)mMenuView.findViewById(R.id.btn_confirm);
		// 添加change事件
    	mViewProvince.addChangingListener(wheelChangeListener);
    	// 添加change事件
    	mViewCity.addChangingListener(wheelChangeListener);
    	// 添加change事件
    	mViewDistrict.addChangingListener(wheelChangeListener);
    	// 添加onclick事件
    	btn_cancel.setOnClickListener(mClickListener);
    	btn_reset.setOnClickListener(mClickListener);
    	btn_confirm.setOnClickListener(mClickListener);
	}
	
	private void initData(){
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}
	
	private OnWheelChangedListener wheelChangeListener = new OnWheelChangedListener() {
		
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			switch(wheel.getId()){
			case R.id.id_province:
				updateCities();
				break;
			case R.id.id_city:
				updateAreas();
				break;
			case R.id.id_district:
				mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
				mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
				break;
			}
		}
	};
	
	/***
	 * 按键点击事件
	 */
	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 switch(v.getId()){
			 	case R.id.btn_cancel:
			 		 dismiss();
			 		 break;
			 	case R.id.btn_reset:
			 		 initData();
			 	     break;
			 	case R.id.btn_confirm:
			 		AddressEditEvent addrEvent = new AddressEditEvent();
			 		addrEvent.eventType  = GeneralConst.EVENTTYPE_EDITADDR;
			 		addrEvent.address = mCurrentProviceName+mCurrentCityName+mCurrentDistrictName;
			 		EventBus.getDefault().post(addrEvent);
			 		dismiss();
			 		break;
			 }
		}
	};
	
	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mContext, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] {""};
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}
	
	/**
	 * 解析省市区的XML数据
	 */
	
    protected void initProvinceDatas()
	{
		List<ProvinceModel> provinceList = null;
    	AssetManager asset = mContext.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = //AppContext.instance().userService.getBaseAddressList();
							handler.getDataList();
			//*/ 初始化默认选中的省、市、区
			if (provinceList!= null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList!= null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			//*/
			mProvinceDatas = new String[provinceList.size()];
        	for (int i=0; i< provinceList.size(); i++) {
        		// 遍历所有省的数据
        		mProvinceDatas[i] = provinceList.get(i).getName();
        		List<CityModel> cityList = provinceList.get(i).getCityList();
        		String[] cityNames = new String[cityList.size()];
        		for (int j=0; j< cityList.size(); j++) {
        			// 遍历省下面的所有市的数据
        			cityNames[j] = cityList.get(j).getName();
        			List<DistrictModel> districtList = cityList.get(j).getDistrictList();
        			String[] distrinctNameArray = new String[districtList.size()];
        			DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
        			for (int k=0; k<districtList.size(); k++) {
        				// 遍历市下面所有区/县的数据
        				DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
        				// 区/县对于的邮编，保存到mZipcodeDatasMap
        				mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
        				distrinctArray[k] = districtModel;
        				distrinctNameArray[k] = districtModel.getName();
        			}
        			// 市-区/县的数据，保存到mDistrictDatasMap
        			mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
        		}
        		// 省-市的数据，保存到mCitisDatasMap
        		mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
        	}
        } catch (Throwable e) {  
            ILog.e(TAG, e);
        } finally {
        	
        } 
	}
	
}
