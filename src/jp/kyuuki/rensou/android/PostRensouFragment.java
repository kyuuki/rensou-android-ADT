package jp.kyuuki.rensou.android;

import java.util.ArrayList;

import jp.kyuuki.rensou.android.model.Rensou;
import jp.kyuuki.rensou.android.net.RensouApi;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 連想投稿部品。
 */
public class PostRensouFragment extends Fragment {
    // Model
    private long mThemeId = -1;

    // View
    private TextView mLatestKeywordText;
    private EditText mPostRensouEditText;
    private Button mAnswerButton;

    ProgressDialog progressDialog;  // 通信中ダイアログ

    // 通信
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_rensou, container, false);

        mLatestKeywordText = (TextView) v.findViewById(R.id.latestKeywordText);
        mPostRensouEditText = (EditText) v.findViewById(R.id.postRensouEditText);
        mAnswerButton = (Button) v.findViewById(R.id.postButton);
        mAnswerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyTracker.getTracker().sendEvent(Analysis.GA_EC_UI_ACTION, Analysis.GA_EA_BUTTON_POST, null, null);
                
                // 入力チェック
                String keyword = mPostRensouEditText.getText().toString();
                if (Rensou.validateKeyword(keyword) == false) {
                    EasyTracker.getTracker().sendEvent(Analysis.GA_EC_ERROR, Analysis.GA_EA_POST_VALIDATE, null, null);
                    Toast.makeText(getActivity(), getString(R.string.post_rensou_error_validate_keyword), Toast.LENGTH_LONG).show();
                    return;
                }
                
                String url = RensouApi.getPostUrl();
                JSONObject json = RensouApi.makeRensouJson(mThemeId, keyword);
                
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.post_rensou_posting));
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                mRequestQueue.add(new JsonObjectArrayRequest(Method.POST, url, json,
                    new Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // タイミングによってはダイアログがない場合があるかも
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            progressDialog = null;

                            Log.v("HTTP", "body is " + response);

                            ArrayList<Rensou> list = RensouApi.json2Rensous(response);

                            Intent intent = new Intent(getActivity(), PostResultActivity.class);
                            intent.putExtra("list", list);
                            startActivity(intent);
                        }
                    },

                    new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // タイミングによってはダイアログがない場合があるかも
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            progressDialog = null;
                            
                            if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                                // 投稿が被った可能性が高い。
                                EasyTracker.getTracker().sendEvent(Analysis.GA_EC_ERROR, Analysis.GA_EA_POST_CONFLICT, null, null);
                                Toast.makeText(getActivity(), getString(R.string.post_rensou_error_transaction), Toast.LENGTH_LONG).show();
                                mPostRensouEditText.setText("");
                                getLatestRensou();
                            } else {
                                EasyTracker.getTracker().sendEvent(Analysis.GA_EC_ERROR, Analysis.GA_EA_POST_ERROR, null, null);
                                Toast.makeText(getActivity(), getString(R.string.error_communication), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                ));
            }
        });
        
        return v;
    }
    
    @Override
    public void onResume () {
        super.onResume();

        // 前回入力した投稿削除
        mPostRensouEditText.setText("");

        // 最新の連想取得
        getLatestRensou();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        // Fragment のページビューを取ると Activity と混乱するので、このアプリでは取らない。
//        EasyTracker.getInstance().setContext(getActivity());
//        EasyTracker.getTracker().sendView("PostRensouFragment");
    }
    
    // 最後の連想取得
    private void getLatestRensou() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.post_rensou_reading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        String url = RensouApi.getGetUrlLast();
        mRequestQueue.add(new JsonObjectRequest(Method.GET, url, null, 
            new Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // タイミングによってはダイアログがない場合があるようだ (1 回ヌルポで落ちた)
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    progressDialog = null;

                    Log.v("HTTP", "body is " + response.toString());
                    Rensou rensou = RensouApi.json2Rensou(response);
                    
                    // TODO: mLastKeywordText が作成出来ていないパターンがある？
                    mThemeId = rensou.getId();
                    mLatestKeywordText.setText(rensou.getKeyword());
                }
            },
                
            new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // タイミングによってはダイアログがない場合があるかも
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    progressDialog = null;

                    EasyTracker.getTracker().sendEvent(Analysis.GA_EC_ERROR, Analysis.GA_EA_GET_LAST_KEYWORD, null, null);
                    Toast.makeText(getActivity(), getString(R.string.error_communication), Toast.LENGTH_LONG).show();
                    // TODO: 通信エラーの時はどうする？
                }
            }));
    }
}
