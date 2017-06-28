package fish.com.baidumapdemo;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.daasuu.bl.BubbleLayout;

import java.util.List;

import fish.com.baidumapdemo.util.LogUtil;

/**
 * Created by neil on 2017/6/18 .
 * <p >LocationClient 进行定位的一些设置
 * LocationClientOption 进行定位的一些设置
 * BDLocationLister 定位监听
 * BDLocation \ MyLocationData
 * <p/>
 * 自定义图标: BitmapDescriptor
 * 引入方向传感器: SensorManager --Sensor
 * BDLocationLister 对方向进行设置
 * 定位模式 : MyLocationConfiguration config = new MyLocationConfiguration(mLocationMode, true, mIconLocation);
 * mBaiduMap.setMyLocationConfiguration(config);
 * 添加覆盖物及图层、覆盖物的点击处理:BitmapDescriptor、Marker、OverlayOptions、
 * Infrowindow
 */
public class MainActivity extends AppCompatActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstIn = true;
    private Context mContext;

    // 定位相关
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
    private double mJindu;
    private double mWeidu;
    // 自定义定位图标
    private BitmapDescriptor mIconLocation;
    private MyOrirentationListener myOrirentationListener;
    private float mCurrentX; // 记录当前的位置
    private MyLocationConfiguration.LocationMode mLocationMode; // 定位模式

    // 覆盖物相关
    private BitmapDescriptor mMarker; // 标记
    private RelativeLayout layoutMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        mContext = this;
        setContentView(R.layout.activity_main);
        initView();

        // 初始化定位
        initLocation();

        // 添加覆盖物
        initMarker();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Bundle extraInfo = marker.getExtraInfo();
                Info info = (Info) extraInfo.getSerializable("info");
                LogUtil.e("点击marker" + info.getName());
                ImageView ivBg = (ImageView) layoutMarker.findViewById(R.id.iv_imageView);
                TextView tvName = (TextView) layoutMarker.findViewById(R.id.tv_name);
                TextView tvContent = (TextView) layoutMarker.findViewById(R.id.tv_content);
                TextView tvZan = (TextView) layoutMarker.findViewById(R.id.tv_zan);
                ivBg.setImageResource(info.getImgId());
                tvName.setText(info.getName());
                tvContent.setText(info.getDistance());
                tvZan.setText(info.getZan() + "");

                InfoWindow inforWindow;
//                TextView tv = new TextView(getApplicationContext());
//                tv.setBackgroundResource(R.drawable.img_border1);
//                tv.setPadding(30, 20, 30, 50);
//                tv.setText(info.getName());
//                final LatLng la = marker.getPosition();
//                // 将地图上的点转为实际坐标
//                Point p = mBaiduMap.getProjection().toScreenLocation(la);
//                LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);


                // 自定义inforWindow
                // 网上的一个第三方气泡库，不用这个气泡库，自己创建TextView也一样效果
                final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(mContext).inflate(R.layout.marker_pop_layout, null, false);
                TextView tv = (TextView) bubbleLayout.findViewById(R.id.popUpText);
                tv.setText(info.getName());
                // 设置气泡的位置为中间 3是偏移量，自己调整的
                TextPaint paint = tv.getPaint();
                float textLength = paint.measureText(info.getName());
                bubbleLayout.setArrowPosition(textLength / 2 + bubbleLayout.getArrowWidth() / 2 + 3);
                bubbleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        mBaiduMap.hideInfoWindow(); //隐藏气泡
                    }
                });

                final LatLng la = marker.getPosition();
//                // 将地图上的点转为实际坐标
                Point p = mBaiduMap.getProjection().toScreenLocation(la);
                LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);

                // 为弹出的InfoWindow添加点击事件
                inforWindow = new InfoWindow(BitmapDescriptorFactory.fromView(bubbleLayout), ll, -47, new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        LogUtil.e("点击覆盖物");
                        mBaiduMap.hideInfoWindow();
                    }
                });
                mBaiduMap.showInfoWindow(inforWindow);
                layoutMarker.setVisibility(View.VISIBLE);
                return true;
            }
        });

        // 地图点击时
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LogUtil.e("点击地图");
                layoutMarker.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }


    private void showLocation(final Marker marker) {  //显示气泡
        Bundle extraInfo = marker.getExtraInfo();
        Info info = (Info) extraInfo.getSerializable("info");
        // 网上的一个第三方气泡库，不用这个气泡库，自己创建TextView也一样效果
        final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(mContext).inflate(R.layout.marker_pop_layout, null, false);
        TextView tv = (TextView) bubbleLayout.findViewById(R.id.popUpText);
        tv.setText(marker.getTitle());
        // 设置气泡的位置为中间 3是偏移量，自己调整的
        TextPaint paint = tv.getPaint();
        float textLength = paint.measureText(info.getName());
        bubbleLayout.setArrowPosition(textLength / 2 + bubbleLayout.getArrowWidth() / 2 + 3);
        bubbleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mBaiduMap.hideInfoWindow(); //隐藏气泡
            }
        });
        final LatLng ll = marker.getPosition();
        Point p = mBaiduMap.getProjection().toScreenLocation(ll);
        LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
        InfoWindow mInfoWindow = new InfoWindow(bubbleLayout, llInfo, -47);
        mBaiduMap.showInfoWindow(mInfoWindow); //显示气泡
    }


    private void initView() {
        mMapView = (MapView) findViewById(R.id.id_bmapView);

        mBaiduMap = mMapView.getMap();
        // 设置地图方法比例 15.f 保持500m左右
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(mapStatusUpdate);

    }

    // 定位相关
    private void initLocation() {
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        mLocationClient = new LocationClient(this);
        mMyLocationListener = new MyLocationListener();
        // 注册
        mLocationClient.registerLocationListener(mMyLocationListener);
        // 设置配置
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option); // 设定一些属性
        // 初始化定位图标
        mIconLocation = BitmapDescriptorFactory.fromResource(R.mipmap.myinvite);
        myOrirentationListener = new MyOrirentationListener(this);
        myOrirentationListener.setmOnOrientationListener(new MyOrirentationListener.OnOrientationListener() {
            @Override
            public void onOrientationChange(float x) {
                mCurrentX = x;
            }
        });
    }

    private void initMarker() {
        mMarker = BitmapDescriptorFactory.fromResource(R.mipmap.icon_openmap_mark);
        layoutMarker = (RelativeLayout) findViewById(R.id.layout_marker);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
            //开始方向传感器
            myOrirentationListener.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 停止方向传感器
        myOrirentationListener.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_map_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;

            case R.id.id_map_site:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.id_map_trafic:
                if (mBaiduMap.isTrafficEnabled()) {
                    mBaiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通off");
                } else {
                    mBaiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通on");
                }
                break;
            case R.id.id_map_location: // 回到我的位置
                centerMyLocation();
                break;

            case R.id.id_map_mode_common: // 定位模式(普通)
                mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
                break;

            case R.id.id_map_mode_follow: // 定位模式(跟随)
                mLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                break;

            case R.id.id_map_mode_compass: // 定位模式(罗盘)
                mLocationMode = MyLocationConfiguration.LocationMode.COMPASS;
                break;

            case R.id.id_add_voerlay: // 添加覆盖物
                addOverLays(Info.infos);
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 定位成功后的回调
            MyLocationData data = new MyLocationData.Builder()//
                    .direction(mCurrentX)// 方向的值(结合方向传感器)
                    .accuracy(location.getRadius())//
                    .latitude(location.getLatitude())// 精度
                    .longitude(location.getLongitude()) // 纬度
                    .build();
            LogUtil.d("定位信息：" + JSON.toJSONString(data));
            mBaiduMap.setMyLocationData(data);
            // 定位完成后，设置自定义图标
            // 定位模式 MyLocationConfiguration.LocationMode.NORMAL 有三种
            MyLocationConfiguration config = new MyLocationConfiguration(mLocationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfiguration(config);
            // 更新经度和纬度
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mJindu = location.getLatitude();
            mWeidu = location.getLongitude();

            // 第一次用户定位成功设置为用户所在点
            if (isFirstIn) {
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                Toast.makeText(mContext, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                isFirstIn = false;
            }
        }
    }

    private void centerMyLocation() {
        LatLng latLng = new LatLng(mJindu, mWeidu);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }


    /**
     * 添加覆盖物
     *
     * @param infos
     */
    private void addOverLays(List<Info> infos) {
        // 清楚图层
        mBaiduMap.clear();
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        for (Info info : infos) {
            // 经纬度
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            // 图标
            options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
            // 添加
            marker = (Marker) mBaiduMap.addOverlay(options);
            Bundle arg = new Bundle();
            arg.putSerializable("info", info);
            marker.setExtraInfo(arg);
        }

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

}
