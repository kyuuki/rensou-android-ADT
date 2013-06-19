package jp.kyuuki.rensou.android;

import jp.kyuuki.rensou.android.model.Rensou;
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

        RensouArrayAdapter adapter = new RensouArrayAdapter(getActivity(), R.layout.row_rensou);
        Rensou r;

        for (int i = 0; i < 10; i++) {
            r = new Rensou();
            r.setKeyword("しりとり");
            adapter.add(r);
            
            r = new Rensou();
            r.setKeyword("りんご");
            adapter.add(r);
    
            r = new Rensou();
            r.setKeyword("ごりら");
            adapter.add(r);
    
            r = new Rensou();
            r.setKeyword("らっこ");
            adapter.add(r);
    
            r = new Rensou();
            r.setKeyword("こいし");
            adapter.add(r);
        }

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        return v;
    }
}
