package jp.kyuuki.rensou.android.activity;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.fragment.DummyFragment;
import jp.kyuuki.rensou.android.fragment.PostRensouFragment;
import jp.kyuuki.rensou.android.model.User;
import jp.kyuuki.rensou.android.net.RensouApi;
import jp.kyuuki.rensou.android.net.VolleyUtils;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MainActivity extends BaseActivity {

    // 通信
    public RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 自動でソフトキーボードが出るのを防ぐ
        // http://y-anz-m.blogspot.jp/2010/05/android_17.html
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        
        setContentView(R.layout.activity_main);
        
        // AdView をリソースとしてルックアップしてリクエストを読み込む。
        // https://developers.google.com/mobile-ads-sdk/docs/android/banner_xml?hl=ja
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest();
        //adRequest.addTestDevice("6E5BC489C3B529363F063C3C74151BC7");
        adView.loadAd(adRequest);

        // onCreate で作っちゃっていい？null に戻っちゃうことない？
        mRequestQueue = VolleyUtils.getRequestQueue(this);

        if (savedInstanceState == null) {
            // アプリ起動時のみ

            // 静的に XML に書いておくといろいろ不具合が起こるので動的に生成。
            Fragment newFragment = new DummyFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.mainFragment, newFragment);
            ft.commit();

            User user = User.getMyUser(this);
            if (user == null) {
                // 初回起動時のみ
                registerUser();
            } else {
                startPostRensouFragment();
            }
        }
    }

    private void registerUser() {
        String url = RensouApi.getPostUrlRegisterUser();
        JSONObject json = RensouApi.makeRegisterUserJson();

        JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, json,
            new Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("HTTP", "body is " + response.toString());
                    User user = RensouApi.json2User(response);
                    user.saveMyUser(MainActivity.this);  // 永続化
                    startPostRensouFragment();
                }
            },

            new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, getString(R.string.error_communication), Toast.LENGTH_LONG).show();
                    // TODO: 通信エラーの時はどうする？
                }
            });

        mRequestQueue.add(request);
    }

    private void startPostRensouFragment() {
        Fragment newFragment = new PostRensouFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragment, newFragment);
        ft.commit();
    }
}
