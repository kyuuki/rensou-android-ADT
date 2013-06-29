package jp.kyuuki.rensou.android.model;

import java.io.Serializable;
import java.util.Date;

import jp.kyuuki.rensou.android.common.Utils;

/**
 * 連想。
 */
public class Rensou implements Serializable {
    private static final long serialVersionUID = 3403855697655211867L;

    private long id;
    private String keyword;
    private boolean favorite;
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
    public boolean isFavorite() {
        return favorite;
    }
    public void setFavorite(boolean favorite) {
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
}
