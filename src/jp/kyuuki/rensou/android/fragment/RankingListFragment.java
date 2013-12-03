package jp.kyuuki.rensou.android.fragment;

import java.util.List;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.adapter.RankingArrayAdapter;
import jp.kyuuki.rensou.android.model.Rank;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * ランキングリストフラグメント。
 * 
 * - 自前で通信して、データ取得まで行う (この中で閉じる)。
 */
public class RankingListFragment extends Fragment {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ranking_list, container, false);

        List<Rank> ranking = null;
        
        // TODO: ダミーデータ
        // DUMMY
        try { ranking = Rank.createDummyRanking(getResources()); } catch (Exception e) {};

        RankingArrayAdapter adapter = new RankingArrayAdapter(getActivity(), R.layout.row_ranking, ranking);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setScrollingCacheEnabled(false);  // Xperia ray などでスクロール時に背景が白くなるのに対処

        return v;
    }
}
