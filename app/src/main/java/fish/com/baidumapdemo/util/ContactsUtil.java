package fish.com.baidumapdemo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：utils.app
 * 类描述：获取手机联系人
 * 创建人：Darker
 * 邮箱：1522651962@qq.com
 * 创建时间：16/5/20 上午12:44
 * 修改备注：
 *
 * @version 1.0
 */
public class ContactsUtil {

    public List<ContactModel> contactModelList ;
    /**
     * 获取库Phon表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    /**
     * 得到手机通讯录联系人信息
     **/
    public List<ContactModel> getContacts(Context mContext) {
        contactModelList = new ArrayList<>();
        getPhoneContacts(mContext);
        getSIMContacts(mContext);
        return  contactModelList;
    }

    private  void getPhoneContacts(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);


        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                ContactModel contactModel = new ContactModel();
                contactModel.setName(contactName);
                contactModel.setPhone(phoneNumber);
                contactModelList.add(contactModel);

            }

            phoneCursor.close();
        }

    }

    /**
     * 得到手机SIM卡联系人人信息
     **/
    private  void getSIMContacts(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);

                //Sim卡中没有联系人头像

                ContactModel contactModel = new ContactModel();
                contactModel.setName(contactName);
                contactModel.setPhone(phoneNumber);
                contactModelList.add(contactModel);
            }

            phoneCursor.close();
        }
    }

    public static class ContactModel {
        private String name;
        private String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

}
