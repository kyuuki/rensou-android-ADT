package jp.kyuuki.rensou.android.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.net.RensouApi;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;

/**
 * ランク。
 */
public class Rank implements Serializable {
    private static final long serialVersionUID = -6029400733997760777L;

    private int rank;
    private RensouHistory rensouHistory;

    public Rank(int rank, RensouHistory rh) {
        this.rank = rank;
        this.rensouHistory = rh;
    }
    
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public RensouHistory getRensouHistory() {
        return rensouHistory;
    }

    public void setRensouHistory(RensouHistory rensouHistory) {
        this.rensouHistory = rensouHistory;
    }

    public static List<Rank> createDummyRanking(Resources resources) throws JSONException, IOException {
        // JSON 読み込み
        InputStream is = resources.openRawResource(R.raw.dummy_ranking);
        JSONArray array = new JSONArray(IOUtils.toString(is));

        List<Rank> ranking = new ArrayList<Rank>();
        
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            
            Rensou r = new Rensou();
            r.setKeyword(o.getString("keyword"));
            r.setCreatedAt(RensouApi.parseDate(o.getString("created_at")));
            r.setFavorite(o.getInt("favorite"));
            Rensou s = new Rensou();
            s.setKeyword(o.getString("old_keyword"));
            
            RensouHistory rh = new RensouHistory(r, s);
            Rank rank = new Rank(i + 1, rh);
            ranking.add(rank);
        }
        
        return ranking;
    }
}
