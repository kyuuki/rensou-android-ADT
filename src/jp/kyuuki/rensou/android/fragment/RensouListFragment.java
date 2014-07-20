package jp.kyuuki.rensou.android.fragment;

import java.util.ArrayList;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.adapter.RensouArrayAdapter;
import jp.kyuuki.rensou.android.model.Rensou;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 連想リストフラグメント。
 */
public class RensouListFragment extends Fragment {
    private static final String TAG = RensouListFragment.class.getName();

    // 引数のキー
    public static final String BUNDLE_LIST = "list";
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate(): " + this.hashCode());

        // このフラグメントは回転しても作り直さない
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView(): " + this.hashCode());
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

        RensouArrayAdapter adapter = new RensouArrayAdapter(getActivity(), R.layout.row_rensou, list);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setScrollingCacheEnabled(false);  // Xperia ray などでスクロール時に背景が白くなるのに対処

        return v;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart(): " + this.hashCode());
        // Fragment のページビューを取ると Activity と混乱するので、このアプリでは取らない。
//        EasyTracker.getInstance().setContext(getActivity());
//        EasyTracker.getTracker().sendView("RensouListFragment");
    }
    
    @Override
    public void onStop() {
        Log.v(TAG, "onStop(): " + this.hashCode());
        super.onStart();
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Rensou> getSerializableBundleList(Bundle bundle) {
        return (ArrayList<Rensou>) bundle.getSerializable(BUNDLE_LIST);
    }
}
