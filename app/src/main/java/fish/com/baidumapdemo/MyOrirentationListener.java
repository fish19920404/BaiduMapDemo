package fish.com.baidumapdemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 方向传感器
 * Created by neil on 2017/6/18 0018.
 */
public class MyOrirentationListener implements SensorEventListener {

    private SensorManager mSensorManager;
    private Context mContext;
    private Sensor mSensor;
    private float lastX;
    private OnOrientationListener mOnOrientationListener;

    // z轴指向自己

    public MyOrirentationListener(Context mContext) {
        this.mContext = mContext;
    }

    // 开始监听
    public void start() {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (null != mSensorManager) {
            // 获得方向传感器
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if (mSensor != null) {
            // SensorManager.SENSOR_DELAY_UI 、还有游戏等等
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        if (null != mSensorManager) {
            mSensorManager.unregisterListener(this);
        }
    }

    // 方向发生变化
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = event.values[SensorManager.DATA_X];
            if (Math.abs(x - lastX) > 1.0) {
                // 通过回调更新
                if (mOnOrientationListener != null) {
                    mOnOrientationListener.onOrientationChange(lastX);
                }
            }
            lastX = x;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnOrientationListener {
        void onOrientationChange(float x);
    }


    public void setmOnOrientationListener(OnOrientationListener mOnOrientationListener) {
        this.mOnOrientationListener = mOnOrientationListener;
    }
}

