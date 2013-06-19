package jp.kyuuki.rensou.android.model;

/**
 * 連想。
 */
public class Rensou {
    private long id;
    private String keyword;
    private boolean favorite;
    private boolean spam;

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
}
