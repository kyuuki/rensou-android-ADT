package jp.kyuuki.rensou.android.model;

import java.util.HashSet;

import android.content.Context;
import android.util.Log;

/**
 * 自分のいいね！履歴。
 *
 * - 自分がいいね！した履歴をアプリ側で持っておく。
 */
public class MyLikes {
    HashSet<Long> history;

    private static MyLikes instance;  // メモリ不足で初期化されて null になる可能性あり
    
    private MyLikes(Context context) {
        // TODO: 永続化
        this.history = new HashSet<Long>();
    }
    
    public static MyLikes getInstance(Context context) {
        if (instance == null) {
            instance = new MyLikes(context);
        }
        
        return instance;
    }
    
    public boolean isLike(long rensouId) {
        return history.contains(rensouId);
    }
    
    public void like(long rensouId) {
        history.add(rensouId);
        Log.v("kyuuki", "likes = " + history);
    }
    
    public void unlike(long rensouId) {
        history.remove(rensouId);
        Log.v("kyuuki", "likes = " + history);
    }
}
