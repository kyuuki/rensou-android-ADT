package jp.kyuuki.rensou.android.fragment;

import java.util.List;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.adapter.RankingArrayAdapter;
import jp.kyuuki.rensou.android.common.Logger;
import jp.kyuuki.rensou.android.model.Rank;
import jp.kyuuki.rensou.android.net.RensouApi;
import jp.kyuuki.rensou.android.net.VolleyUtils;
import jp.kyuuki.rensou.android.view.TouchChanger;

import org.json.JSONArray;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectArrayRequest;

/**
 * ランキングリスト フラグメント。
 * 
 * - 自前で通信して、データ取得まで行う (この中で閉じる)。
 */
public class RankingListFragment extends Fragment {
    // Model
    List<Rank> mRankList = null;  // 表示中のランキング

    // View
    private ListView mList;  // リスト表示
    final TouchChanger changer = new TouchChanger();

    ProgressDialogFragment progressDialog = null;  // 通信中ダイアログ

    // 通信
    private RequestQueue mRequestQueue;

    RankingArrayAdapter mAdapter = null;  // mRankList と連動

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // このフラグメントは回転しても作り直さない
        setRetainInstance(true);

        mRequestQueue = VolleyUtils.getRequestQueue(getActivity().getApplicationContext());

        if (mRankList == null) {
            // 受信時に View ができていない可能性はない？
            String url = RensouApi.getGetUrlRensousRanking();
            mRequestQueue.add(new JsonObjectArrayRequest(Method.GET, url, null,
                    new PostUrlRensousRankingListener(),
                    new PostUrlRensousRankingListener()));

            progressDialog = ProgressDialogFragment.newInstance();
            progressDialog.show(getFragmentManager(), "dialog");  // TODO: 第 2 引数は？
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ranking_list, container, false);

        mList = (ListView) v.findViewById(R.id.listView);
        mList.setScrollingCacheEnabled(false);  // Xperia ray などでスクロール時に背景が白くなるのに対処
        mList.setAdapter(mAdapter);  // 既存のアダプタを再利用
        mList.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changer.setOnTouch(event);
                return false;
            }
        });

        return v;
    }

    /*
     * GET rensous/ranking 受信後の動作。
     */
    private class PostUrlRensousRankingListener implements Listener<JSONArray>, ErrorListener {

        @Override
        public void onResponse(JSONArray response) {
            progressDialog.dismiss();
            progressDialog = null;
            
            Logger.v("HTTP", "body is " + response);

            mRankList = RensouApi.json2Ranking(response);
            // DUMMY
            //try { mRankList = Rank.createDummyRanking(getResources()); } catch (Exception e) {};

            assert (getActivity() != null);  // Activity から切り離されて Fragment が再作成が行われないようにしておくこと。

            mAdapter = new RankingArrayAdapter(getActivity(), R.layout.row_ranking, mRankList);
            mList.setAdapter(mAdapter);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            // null の場合ある？
            progressDialog.dismiss();
            progressDialog = null;

            Toast.makeText(getActivity(), getString(R.string.error_communication), Toast.LENGTH_LONG).show();
        }
    }
    
    /*
     * 通信中ダイアログ
     */
    // http://your-v.sblo.jp/article/52030295.html
    // public static にしておく必要がある
    public static class ProgressDialogFragment extends DialogFragment {
        // 引数ありのコンストラクタを作ってはダメ
        public static ProgressDialogFragment newInstance() {
            ProgressDialogFragment fragment = new ProgressDialogFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.post_rensou_reading));
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            return dialog;
        }
    }
}
