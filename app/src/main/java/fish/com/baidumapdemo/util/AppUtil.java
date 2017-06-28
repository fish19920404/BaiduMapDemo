package fish.com.baidumapdemo.util;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;

/**
 * 项目名称：com.utils.app
 * 类描述：app Utils 获取应用信息工具类
 * 创建人：Darker
 * 邮箱：1522651962@qq.com
 * 创建时间：15/9/18 上午9:52
 * 修改备注：
 *
 * @version 1.0
 */
public class AppUtil {

    /**
     * 获取包名
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appName = null;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appName = packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;

    }

    /**
     * 获取应用版本
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        String appVersion = null;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    /**
     * 发送通知
     *
     * @param mContext
     * @param logo
     * @param title
     * @param content
     * @param activity
     */
    public static void sendNotification(Context mContext, int logo, String title, String content, Class activity) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) mContext
                .getSystemService(ns);
        // 定义通知栏展现的内容信息
        long when = System.currentTimeMillis();
        @SuppressWarnings("deprecation") Notification notification = new Notification(logo, title, when);
        // 定义下拉通知栏时要展现的内容信息
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        Bundle bundle = new Bundle();
        Intent notificationIntent = new Intent(mContext, activity);
        bundle.putString("control", "noti");
        notificationIntent.putExtras(bundle);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                notificationIntent, 0);
//        notification.setLatestEventInfo(context, contentTitle, contentText,
//                contentIntent);
        // 用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(1, notification);

    }

    /**
     * 开启震动  ,记得在manifast文件中添加权限
     *
     * @param context
     * @param time
     */
    public static void startVibrator(Context context, int time) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(time);
    }

    /**
     * 开启音乐
     *
     * @param context
     * @param resource
     */
    public static void startMusic(Context context, int resource) {
        MediaPlayer mp = MediaPlayer.create(context, resource);
        mp.start();
    }

    /**打电话
     * @param phone
     */
    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(intent);

    }
    public static void shareText(Context context, String content){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,content);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
    public static void sendMessage(Context context, String mobile, String str){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", mobile);
        //短信内容在这里
//        intent.putExtra("sms_body", "学车找教练，驾始平台见,http://xxcvip.com/");
        intent.putExtra("sms_body", str);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);
    }

}
