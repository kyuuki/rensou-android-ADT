package jp.kyuuki.rensou.android;

import java.text.SimpleDateFormat;
import java.util.List;

import jp.kyuuki.rensou.android.model.RensouHistory;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RensouArrayAdapter extends ArrayAdapter<RensouHistory> {
    LayoutInflater mInflater;
    
    // 日付のフォーマット
    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // TODO: 多言語化
    
    public RensouArrayAdapter(Context context, int textViewResourceId, List<RensouHistory> list) {
        super(context, textViewResourceId, list);
        // TODO Auto-generated constructor stub
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    // レイアウトファイルに記述してある値を 1 度だけ取得する。取得後は正になるはず。
    static int rowPaddingLeft   = -1;
    static int rowPaddingTop    = -1;
    static int rowPaddingRight  = -1;
    static int rowPaddingBottom = -1;
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.row_rensou, null);
        }
        
        RensouHistory rensouHistory = getItem(position);
        
        RelativeLayout rowRensouLayout = (RelativeLayout) view.findViewById(R.id.rowRensouLayout);
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
            
            // 1 行おきに背景を変える
//            int left   = rowPaddingLeft   > 0 ? rowPaddingLeft   : rowRensouLayout.getPaddingLeft();
//            int top    = rowPaddingTop    > 0 ? rowPaddingTop    : rowRensouLayout.getPaddingTop();
//            int right  = rowPaddingRight  > 0 ? rowPaddingRight  : rowRensouLayout.getPaddingRight();
//            int bottom = rowPaddingBottom > 0 ? rowPaddingBottom : rowRensouLayout.getPaddingBottom();
            // TODO: ↑なぜか値が一定しない。
            float density = getContext().getResources().getDisplayMetrics().density;
            int left   = (int) (density * 60);
            int top    = (int) (density * 30);
            int right  = (int) (density * 60);
            int bottom = (int) (density * 30);
            if (position % 2 == 0) {
                rowRensouLayout.setBackgroundResource(R.drawable.rensou_cell_bg);
                rowRensouLayout.setPadding(left, top, right, bottom);
            } else {
                rowRensouLayout.setBackgroundResource(R.drawable.rensou_cell_bg2);
                rowRensouLayout.setPadding(left + (int) (density * 18), top, right
                        - (int) (density * 18), bottom); // 上の画像が左に吹き出しが付くので通常より右に移動
            }
        }

        return view;
    }
}
