package jp.kyuuki.rensou.android;

import jp.kyuuki.rensou.android.model.Rensou;
import jp.kyuuki.rensou.android.net.RensouApi;

import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AnswerFragment extends Fragment {
    private RequestQueue mRequestQueue;
    
    private TextView mLastKeywordText;
    private Button mAnswerButton;
    
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
        mAnswerButton = (Button) v.findViewById(R.id.answerButton);
        mAnswerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = RensouApi.getPostUrl();
                mRequestQueue.add(new JsonObjectRequest(Method.POST, url, null,
                    new Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v("HTTP", "body is " + response.toString());
                        }
                    },
                    
                    new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                        }
                    }));
            }
        });
        
        return v;
    }
}
