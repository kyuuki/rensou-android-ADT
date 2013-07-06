package jp.kyuuki.rensou.android.fragment;

import java.util.ArrayList;
import java.util.List;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.RensouArrayAdapter;
import jp.kyuuki.rensou.android.model.Rensou;
import jp.kyuuki.rensou.android.model.RensouHistory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 連想リストかけら。
 */
public class RensouListFragment extends Fragment {
    // 引数のキー
    public static final String BUNDLE_LIST = "list";
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rensou_list, container, false);

        // 引数処理
        Bundle bundle = getArguments();
        if (bundle == null) {
            Log.e("rensou", "Invalid arguments");
            return v;
        }
        
        ArrayList<Rensou> list = getSerializableBundleList(bundle);
        if (list == null) {
            Log.e("rensou", "Invalid bundle list");
            return v;
        }
        
        // 連想リスト → 連想履歴リスト作成
        List<RensouHistory> rensouHistoryList = makeResnouHistoryList(list);

        RensouArrayAdapter adapter = new RensouArrayAdapter(getActivity(), R.layout.row_rensou, rensouHistoryList);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setScrollingCacheEnabled(false);  // Xperia ray などでスクロール時に背景が白くなるのに対処

        return v;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        // Fragment のページビューを取ると Activity と混乱するので、このアプリでは取らない。
//        EasyTracker.getInstance().setContext(getActivity());
//        EasyTracker.getTracker().sendView("RensouListFragment");
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Rensou> getSerializableBundleList(Bundle bundle) {
        return (ArrayList<Rensou>) bundle.getSerializable(BUNDLE_LIST);
    }
    
    // 連想リスト → 連想履歴リスト
    private List<RensouHistory> makeResnouHistoryList(List<Rensou> list) {
        List<RensouHistory> rensouHistoryList = new ArrayList<RensouHistory>();
        Rensou to = null;
        for (int i = 0, len = list.size(); i < len; i++) {
            Rensou r = (Rensou) list.get(i);

            if (to == null) {
                // 1 つ目は元ネタがわからないので連想履歴は作成しない。
                to = r;
                continue;
            }
        
            RensouHistory rh = new RensouHistory(to, r);
            rensouHistoryList.add(rh);
            to = r;  // 次の連想履歴用に保持
        }
        
        return rensouHistoryList;
    }
}
