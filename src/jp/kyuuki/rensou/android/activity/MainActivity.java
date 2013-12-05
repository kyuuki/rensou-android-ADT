package jp.kyuuki.rensou.android.activity;

import jp.kyuuki.rensou.android.Config;
import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.fragment.DummyFragment;
import jp.kyuuki.rensou.android.fragment.PostRensouFragment;
import jp.kyuuki.rensou.android.model.User;
import jp.kyuuki.rensou.android.net.InitialData;
import jp.kyuuki.rensou.android.net.RensouApi;
import jp.kyuuki.rensou.android.net.VolleyUtils;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

    // http://idios.hatenablog.com/entry/2012/07/07/235137
    enum State {
        INITIAL,
        GETTING_INITIAL_DATA {
            @Override
            public void run(MainActivity activity) {
                activity.getInitialData();
            }
            
            @Override
            public State accept(MainActivity activity) {
                User user = User.getMyUser(activity);
                if (user == null) {
                    // 初回起動時のみ
                    return State.REGISTORING_USER;
                } else {
                    return State.READY;
                }
            }
        },
        REGISTORING_USER {
            @Override
            public void run(MainActivity activity) {
                activity.registerUser();
            }
            
            @Override
            public State accept(MainActivity activity) {
                return State.READY;
            }
        },
        READY {
            @Override
            public void run(MainActivity activity) {
                activity.startPostRensouFragment();
            }
        };
        
        public void run(MainActivity activity) {
            throw new IllegalStateException();
        }
        
        public State accept(MainActivity activity) {
            throw new IllegalStateException();
        }
    }
    
    private State state = State.INITIAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 自動でソフトキーボードが出るのを防ぐ。
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

            // 静的に XML に書いておくといろいろ不具合が起こるので動的に生成。詳細不明。要調査。
            Fragment newFragment = new DummyFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.mainFragment, newFragment);
            ft.commit();

            state = State.GETTING_INITIAL_DATA;
            state.run(this);
        }
    }

    String BUNDLE_KEY_STATE = "STATE";
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        // http://qiita.com/amay077/items/097f54b7dee586fadc99
        outState.putInt(BUNDLE_KEY_STATE, state.ordinal());
    };

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        
        int index = savedInstanceState.getInt(BUNDLE_KEY_STATE);
        this.state = State.values()[index];
    };
    
    private void getInitialData() {
        String url = Config.INITIAL_DATA_URL;

        JsonObjectRequest request = new JsonObjectRequest(Method.GET, url, null,

            new Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("HTTP", "body is " + response.toString());
                    
                    InitialData data = InitialData.createInitialData(response);
                    
                    String message = null;
                    String apiBaseUrl = null;
                    if (data != null) {
                        message = data.getMessege();
                        apiBaseUrl = data.getApiBaseUrl();
                    }

                    // 初期データに API ベース URL が入っていたら、デフォルトを上書き。
                    if (apiBaseUrl != null) {
                        RensouApi.BASE_URL = apiBaseUrl;
                    }
                    
                    if (message != null) {
                        // お知らせダイアログ表示
                        InitialDialogFragment dailog = InitialDialogFragment.newInstance(MainActivity.this.getString(R.string.app_name), message);
                        dailog.show(getSupportFragmentManager(), "dialog");  // TODO: 第二引数は何？
                    } else {
                        state = state.accept(MainActivity.this);
                        state.run(MainActivity.this);
                    }
                }
            },

            new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // 初期データが取得できなくても、何もなかったかのようにふるまう
                    state = state.accept(MainActivity.this);
                    state.run(MainActivity.this);
                }
            });

        mRequestQueue.add(request);
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
                    
                    state = state.accept(MainActivity.this);
                    state.run(MainActivity.this);
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
    
    /*
     * お知らせダイアログ
     */
    public static class InitialDialogFragment extends DialogFragment {

        public static InitialDialogFragment newInstance(String title, String message) {
            InitialDialogFragment fragment = new InitialDialogFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("message", message);
            fragment.setArguments(args);
            return fragment;
        }  

        @Override 
        public Dialog onCreateDialog(Bundle savedInstanceState) {  
            String title = getArguments().getString("title");  
            String message = getArguments().getString("message");  
  
            return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity activity = (MainActivity) getActivity();
                        activity.state = activity.state.accept(activity);
                        activity.state.run(activity);
                    }})
                .create();  
        }  
    } 
}
