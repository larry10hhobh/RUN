package com.larry.shugo.listener;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * 定位监听器
 */
public class MyLocationListener implements BDLocationListener {

    public BDLocation bdLocation = null;  //处理结果

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                ||bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {

            this.bdLocation = bdLocation;

        } else {
            this.bdLocation = null;
        }
    }

    public BDLocation getBdLocation() {
        return bdLocation;
    }

    public void setBdLocation(BDLocation bdLocation) {
        this.bdLocation = bdLocation;
    }
}
