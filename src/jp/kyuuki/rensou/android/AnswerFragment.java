package jp.kyuuki.rensou.android;

import java.util.ArrayList;

import jp.kyuuki.rensou.android.model.Rensou;
import jp.kyuuki.rensou.android.net.RensouApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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

public class AnswerFragment extends Fragment {
    // Model
    private long mThemeId = -1;

    // View
    private TextView mLastKeywordText;
    private EditText mPostRensouEditText;
    private Button mAnswerButton;

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        
        // 最後の連想取得
        String url = RensouApi.getGetUrlLast();
        mRequestQueue.add(new JsonObjectRequest(Method.GET, url, null, 
            new Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("HTTP", "body is " + response.toString());
                    Rensou rensou = RensouApi.json2Rensou(response);
                    
                    // TODO: mLastKeywordText が作成出来ていないパターンがある？
                    mThemeId = rensou.getId();
                    mLastKeywordText.setText(rensou.getKeyword());
                }
            },
                
            new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                }
            }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_answer, container, false);
        mLastKeywordText = (TextView) v.findViewById(R.id.lastKeywordText);
        mPostRensouEditText = (EditText) v.findViewById(R.id.postRensouEditText);
        mAnswerButton = (Button) v.findViewById(R.id.answerButton);
        mAnswerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = RensouApi.getPostUrl();
                JSONObject json = new JSONObject();
                try {
                    json.put("theme_id", mThemeId);
                    json.put("keyword", mPostRensouEditText.getText());
                } catch (JSONException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }

                mRequestQueue.add(new JsonObjectArrayRequest(Method.POST, url, json,
                    new Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.v("HTTP", "body is " + response);
                            
                            ArrayList<Rensou> list = new ArrayList<Rensou>();
                            for (int i = 0, len = response.length(); i < len; i++) {
                                JSONObject o;
                                Rensou r = new Rensou();
                                try {
                                    o = response.getJSONObject(i);
                                    r.setKeyword(o.getString("keyword"));
                                } catch (JSONException e) {
                                    // TODO 自動生成された catch ブロック
                                    e.printStackTrace();
                                }
                                list.add(r);
                            }
                            
                            Intent intent = new Intent(getActivity(), PostResultActivity.class);
                            intent.putExtra("list", list);
                            startActivity(intent);
                        }
                    },
                    
                    new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                ));
            }
        });
        
        return v;
    }
}
