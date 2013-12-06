package jp.kyuuki.rensou.android.adapter;

import java.util.Date;
import java.util.List;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.common.RensouUtils;
import jp.kyuuki.rensou.android.model.Rensou;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RensouArrayAdapter extends ArrayAdapter<Rensou> {
    LayoutInflater mInflater;
    
    // 日付のフォーマット → 多言語化により Andorid 標準の表示形式にあわせることにした。
    //@SuppressLint("SimpleDateFormat")
    //static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public RensouArrayAdapter(Context context, int textViewResourceId, List<Rensou> list) {
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
        
        Rensou rensou = getItem(position);
        
        RelativeLayout rowRensouLayout = (RelativeLayout) view.findViewById(R.id.rowRensouLayout);
        TextView dateTimeText = (TextView) view.findViewById(R.id.dateTimeText);
        TextView rensouText = (TextView) view.findViewById(R.id.rensouText);
        ImageView spamImage = (ImageView) view.findViewById(R.id.spamImage);
        ImageView likeImage = (ImageView) view.findViewById(R.id.likeImage);
        TextView favoriteCountText = (TextView) view.findViewById(R.id.favoriteCountText);
        
        // View
        if (rensou.getOldKeyword() != null) {
            // 連想結果表示テキスト
            rensouText.setText(Html.fromHtml(RensouUtils.rensouToHtml(rensou, getContext())));

            // 投稿日付
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
            java.text.DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());

            if (rensou.getCreatedAt() != null) {
                //dateTimeText.setText(sdf.format(rensouHistory.getRensou().getCreatedAt()));
                Date d = rensou.getCreatedAt();
                dateTimeText.setText(dateFormat.format(d) + " " + timeFormat.format(d));
            }
            
            // いいね！数
            favoriteCountText.setText(getContext().getString(R.string.list_rensou_like_count, rensou.getFavorite()));

            // 1 行おきに背景を変える
//            int left   = rowPaddingLeft   > 0 ? rowPaddingLeft   : rowRensouLayout.getPaddingLeft();
//            int top    = rowPaddingTop    > 0 ? rowPaddingTop    : rowRensouLayout.getPaddingTop();
//            int right  = rowPaddingRight  > 0 ? rowPaddingRight  : rowRensouLayout.getPaddingRight();
//            int bottom = rowPaddingBottom > 0 ? rowPaddingBottom : rowRensouLayout.getPaddingBottom();
            // TODO: ↑なぜか値が一定しない。
            float density = getContext().getResources().getDisplayMetrics().density;
            int left   = (int) (density * 30);
            int top    = (int) (density * 10);
            int right  = (int) (density * 40);
            int bottom = (int) (density * 15);
            if (position % 2 == 0) {
                rowRensouLayout.setBackgroundResource(R.drawable.rensou_cell_bg);
                rowRensouLayout.setPadding(left, top, right, bottom);
            } else {
                rowRensouLayout.setBackgroundResource(R.drawable.rensou_cell_bg2);
                rowRensouLayout.setPadding(left + (int) (density * 10), top, right
                        - (int) (density * 10), bottom); // 上の画像が左に吹き出しが付くので通常より右に移動
            }
        }

        return view;
    }
}
