package jp.kyuuki.rensou.android.activity;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.fragment.RankingListFragment;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

/**
 * ランキング画面。
 */
public class RankingActivity extends BaseActivity {
    private static final String TAG = RankingActivity.class.getName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // アクションバー
        setDisplayHomeAsUpEnabled(true);

        /*
         * フラグメント差し替え
         */
        // http://www.garunimo.com/program/p17.xhtml
        if (savedInstanceState == null) {
            Fragment fragment = new RankingListFragment();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction t = fm.beginTransaction();

            t.replace(R.id.rensouListFragment, fragment);

            t.commit();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @SuppressLint("NewApi")
    private void setDisplayHomeAsUpEnabled(boolean b) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(b);
        }
    }
}
