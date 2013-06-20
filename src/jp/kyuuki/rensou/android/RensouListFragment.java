package jp.kyuuki.rensou.android;

import java.util.ArrayList;
import java.util.List;

import jp.kyuuki.rensou.android.model.Rensou;
import jp.kyuuki.rensou.android.model.RensouHistory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RensouListFragment extends Fragment {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rensou_list, container, false);

        // 引数処理
        Bundle bundle = getArguments();
        ArrayList<Rensou> list;
        if (bundle != null) {
            list = (ArrayList<Rensou>) bundle.getSerializable("list");
        } else {
            list = new ArrayList<Rensou>();
        }
        
        // 連想履歴作成
        // TODO: id を見てもうちょっと効率よく
        List<RensouHistory> rensouHistoryArray = new ArrayList<RensouHistory>();
        Rensou to = null;
        for (int i = 0, len = list.size(); i < len; i++) {
            Rensou r = (Rensou) list.get(i);
            if (to != null) {
                RensouHistory rh = new RensouHistory(to, r);
                rensouHistoryArray.add(rh);
            }
            to = r;
        }

        RensouArrayAdapter adapter = new RensouArrayAdapter(getActivity(), R.layout.row_rensou, rensouHistoryArray);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        return v;
    }
}
