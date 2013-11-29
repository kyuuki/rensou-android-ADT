package jp.kyuuki.rensou.android.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.content.res.Resources;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.common.Utils;

/**
 * 連想。
 */
public class Rensou implements Serializable {
    private static final long serialVersionUID = 3403855697655211867L;

    private long id;
    private String keyword;
    private int favorite;
    private boolean spam;
    private Date createdAt;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public int getFavorite() {
        return favorite;
    }
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
    public boolean isSpam() {
        return spam;
    }
    public void setSpam(boolean spam) {
        this.spam = spam;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    // 入力チェック
    // null 不可
    public static boolean validateKeyword(String keyword) {
        if (Utils.isForbiddenOnlySpace(keyword)) {
            return false;
        }
        
        int length = keyword.length();
        if (length == 0 || length > 13) {
            return false;
        }
        
        return true;
    }
    
    public static Rensou createDummyRensou(Resources resources) {
        Rensou r = new Rensou();
        r.setId(1);
        r.setKeyword(resources.getString(R.string.dummy_keyword));
        
        return r;
    }
    
    public static ArrayList<Rensou> createDummyRensouList(Resources resources) {
        String[] keywords = resources.getStringArray(R.array.dummy_list);
        ArrayList<Rensou> list = new ArrayList<Rensou>();
        
        for (int i = 0; i < keywords.length - 1; i++) {
            Rensou r = new Rensou();
            r.setId(1 + i);
            r.setKeyword(keywords[i]);

            list.add(r);
        }
        
        return list;
    }
}
