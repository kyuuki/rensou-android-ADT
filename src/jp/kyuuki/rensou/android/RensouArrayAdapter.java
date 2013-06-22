package jp.kyuuki.rensou.android;

import java.text.SimpleDateFormat;
import java.util.List;

import jp.kyuuki.rensou.android.model.RensouHistory;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RensouArrayAdapter extends ArrayAdapter<RensouHistory> {
    LayoutInflater mInflater;
    
    // 日付のフォーマット
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public RensouArrayAdapter(Context context, int textViewResourceId, List<RensouHistory> list) {
        super(context, textViewResourceId, list);
        // TODO Auto-generated constructor stub
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.row_rensou, null);
        }
        
        RensouHistory rensouHistory = getItem(position);
        
        TextView sourceText = (TextView) view.findViewById(R.id.sourceText);
        TextView dateTimeText = (TextView) view.findViewById(R.id.dateTimeText);
        TextView rensouText = (TextView) view.findViewById(R.id.rensouText);
        
        // View
        if (rensouHistory.getRensou() != null) {
            sourceText.setText(rensouHistory.getSource().getKeyword());
            if (rensouHistory.getRensou().getCreatedAt() != null) {
                dateTimeText.setText(sdf.format(rensouHistory.getRensou().getCreatedAt()));
            }
            rensouText.setText(rensouHistory.getRensou().getKeyword());
        }
        return view;
    }
}
