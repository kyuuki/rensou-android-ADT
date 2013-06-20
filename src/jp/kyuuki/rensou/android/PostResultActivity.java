package jp.kyuuki.rensou.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;

import jp.kyuuki.rensou.android.model.Rensou;

/**
 * 投稿結果画面。
 */
public class PostResultActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_result);

        // 引数処理
        Intent intent = getIntent();
        ArrayList<Rensou> list = (ArrayList<Rensou>) intent.getSerializableExtra("list");
        
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        RensouListFragment fragment = new RensouListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", list);
        // フラグメントに渡す値をセット
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
