package jp.kyuuki.rensou.android.adapter;

import java.util.Date;
import java.util.List;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.model.Rank;
import jp.kyuuki.rensou.android.model.RensouHistory;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RankingArrayAdapter extends ArrayAdapter<Rank> {
    private static final int[] rankImageResource = {
        R.drawable.rank1_icon,
        R.drawable.rank2_icon,
        R.drawable.rank3_icon,
        R.drawable.rank4_icon,
        R.drawable.rank5_icon,
        R.drawable.rank6_icon,
        R.drawable.rank7_icon,
        R.drawable.rank8_icon,
        R.drawable.rank9_icon,
        R.drawable.rank10_icon
    };
    
    LayoutInflater mInflater;
    
    public RankingArrayAdapter(Context context, int textViewResourceId, List<Rank> list) {
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
            view = mInflater.inflate(R.layout.row_ranking, null);
        }
        
        Rank rank = getItem(position);
        RensouHistory rensouHistory = rank.getRensouHistory();
        
        ImageView rankImage = (ImageView) view.findViewById(R.id.rankImage);
        TextView rensouText = (TextView) view.findViewById(R.id.rensouText);
        TextView dateTimeText = (TextView) view.findViewById(R.id.dateTimeText);
        TextView favoriteTextView = (TextView) view.findViewById(R.id.favoriteText);
        
        // View
        if (rensouHistory.getRensou() != null) {
            // ランク画像
            rankImage.setImageResource(rankImageResource[position]);  // 配列のチェックはしない。ここに渡すデータ数と画像数はあわせておくこと前提。

            // 連想結果表示テキスト
            // TODO 共通化
            StringBuffer buf = new StringBuffer();
            buf.append("<font color='#ff0000'>");
            buf.append(rensouHistory.getSource().getKeyword());
            buf.append("</font>");
            buf.append(" ");
            buf.append("<small>");
            buf.append(getContext().getString(R.string.list_rensou_related));
            buf.append("</small> ");
            buf.append(" ");
            buf.append("<font color='#ff0000'>");
            buf.append(rensouHistory.getRensou().getKeyword());
            buf.append("</font> ");
            rensouText.setText(Html.fromHtml(buf.toString()));

            // 投稿日付
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
            java.text.DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());

            if (rensouHistory.getRensou().getCreatedAt() != null) {
                Date d = rensouHistory.getRensou().getCreatedAt();
                dateTimeText.setText(dateFormat.format(d) + " " + timeFormat.format(d));
            }

            // いいね！数
            favoriteTextView.setText(getContext().getString(R.string.ranking_like_count, rensouHistory.getRensou().getFavorite()));
        }

        return view;
    }
}
