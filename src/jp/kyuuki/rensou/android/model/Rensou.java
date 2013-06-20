package jp.kyuuki.rensou.android.model;

import java.io.Serializable;
import java.util.Date;

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
}
