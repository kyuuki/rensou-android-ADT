package jp.kyuuki.rensou.android.model;

import java.util.ArrayList;
import java.util.List;

import jp.kyuuki.rensou.android.R;
import android.content.res.Resources;

public class RensouHistory {
    Rensou rensou;
    Rensou source;
    
    public RensouHistory(Rensou r, Rensou s) {
        this.rensou = r;
        this.source = s;
    }

    public Rensou getRensou() {
        return rensou;
    }

    public Rensou getSource() {
        return source;
    }
    
    public static List<RensouHistory> createDummyRensouHistoryList(Resources resources) {
        String[] keywords = resources.getStringArray(R.array.dummy_list);
        List<RensouHistory> list = new ArrayList<RensouHistory>();
        
        Rensou lastRensou = new Rensou();
        lastRensou.setId(1);
        lastRensou.setKeyword(keywords[0]);
        
        for (int i = 1; i < keywords.length - 1; i++) {
            Rensou r = new Rensou();
            r.setId(1 + i);
            r.setKeyword(keywords[i]);

            RensouHistory rh = new RensouHistory(r, lastRensou);
            list.add(rh);
            
            lastRensou = r;
        }
        
        return list;
    }
}

