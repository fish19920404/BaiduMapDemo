package fish.com.baidumapdemo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neil on 2017/6/18 0018.
 */
public class Info  implements Serializable{

    private double latitude; // 经度
    private double longitude; // 纬度
    private int imgId; // 图片
    private String name ; // 名称
    private String distance; // 距离
    private int zan;

    public static List<Info> infos = new ArrayList<>();

    static {
        infos.add(new Info(30.595339,114.271474,R.mipmap.con2,"张三","距离200米",145));
        infos.add(new Info(30.595139,114.273474,R.mipmap.con3,"李四","距离200米",264));
        infos.add(new Info(30.595239,114.273971,R.mipmap.con1,"王五","距离200米",2110));
        infos.add(new Info(30.595339,114.263971,R.mipmap.con4,"李刘","距离200米",2011));
    }

    public Info(double latitude, double longitude, int imgId, String name, String distance, int zan) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgId = imgId;
        this.name = name;
        this.distance = distance;
        this.zan = zan;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }
}
