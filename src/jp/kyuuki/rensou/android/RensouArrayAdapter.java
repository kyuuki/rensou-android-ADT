package jp.kyuuki.rensou.android;

import java.util.Date;

import jp.kyuuki.rensou.android.model.Rensou;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RensouArrayAdapter extends ArrayAdapter<Rensou> {
    LayoutInflater mInflater;
    
    public RensouArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.row_rensou, null);
        }
        
        Rensou rensou = getItem(position);
        
        TextView rensouText = (TextView) view.findViewById(R.id.rensouText);
        TextView dateTimeText = (TextView) view.findViewById(R.id.dateTimeText);
        
        // View
        rensouText.setText(rensou.getKeyword());
        dateTimeText.setText(new Date().toString());
        
        return view;
    }
}
