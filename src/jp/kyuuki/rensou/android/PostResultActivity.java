package jp.kyuuki.rensou.android;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import jp.kyuuki.rensou.android.model.Rensou;

/**
 * 投稿結果画面。
 */
public class PostResultActivity extends FragmentActivity {
    
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

        /*
         * フラグメント差し替え
         */
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        RensouListFragment fragment = new RensouListFragment();
 
        // フラグメントに渡す値をセット
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", list);
        fragment.setArguments(bundle);

        t.replace(R.id.rensouListFragment, fragment);
        t.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
