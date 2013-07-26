package jp.kyuuki.rensou.android.activity;

import java.util.ArrayList;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.fragment.RensouListFragment;
import jp.kyuuki.rensou.android.model.Rensou;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

/**
 * 投稿結果画面。
 */
public class PostResultActivity extends BaseActivity {
    
    // 引数のキー
    public static final String INTENT_EXTRA_LIST = "list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_result);

        // 引数処理
        Intent intent = getIntent();
        @SuppressWarnings("unchecked")
        ArrayList<Rensou> list = (ArrayList<Rensou>) intent.getSerializableExtra(INTENT_EXTRA_LIST);

        // アクションバー
        setDisplayHomeAsUpEnabled(true);

        /*
         * フラグメント差し替え
         */
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        RensouListFragment fragment = new RensouListFragment();
 
        // フラグメントに渡す値をセット
        Bundle bundle = new Bundle();
        bundle.putSerializable(RensouListFragment.BUNDLE_LIST, list);
        fragment.setArguments(bundle);

        t.replace(R.id.root, fragment);
        t.commit();
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

    
    @SuppressLint("NewApi")
    private void setDisplayHomeAsUpEnabled(boolean b) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(b);
        }
    }
}
