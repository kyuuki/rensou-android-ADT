package jp.kyuuki.rensou.android.model;

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
}

