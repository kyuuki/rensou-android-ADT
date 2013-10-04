package jp.kyuuki.rensou.android.model;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    long id;
    
    public User(long id) {
        this.id = id;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    // 永続化
    private static final String PREFERENCE_NAME = "Rensou";
    private static final String KEY_MY_USER_ID = "MyUserId";
    
    public static User getMyUser(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        long myUserId = settings.getLong(KEY_MY_USER_ID, -1);
        
        if (myUserId < 0) {
            return null;
        } else {
            return new User(myUserId);
        }
    }
    
    public void saveMyUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putLong(KEY_MY_USER_ID, this.getId());
        edit.commit();
    }
}
