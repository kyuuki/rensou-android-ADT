package jp.kyuuki.rensou.android.adapter;

import java.util.Date;
import java.util.List;

import jp.kyuuki.rensou.android.R;
import jp.kyuuki.rensou.android.common.RensouUtils;
import jp.kyuuki.rensou.android.model.Rank;
import jp.kyuuki.rensou.android.model.Rensou;
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
        Rensou rensou = rank.getRensou();
        
        ImageView rankImage = (ImageView) view.findViewById(R.id.rankImage);
        TextView rensouText = (TextView) view.findViewById(R.id.rensouText);
        TextView dateTimeText = (TextView) view.findViewById(R.id.dateTimeText);
        TextView favoriteText = (TextView) view.findViewById(R.id.favoriteText);
        
        // View
        if (rensou != null) {
            // ランク画像
            rankImage.setImageResource(rankImageResource[position]);  // 配列のチェックはしない。ここに渡すデータ数と画像数はあわせておくこと前提。

            // 連想結果表示テキスト
            rensouText.setText(Html.fromHtml(RensouUtils.rensouToHtml(rensou, getContext())));

            // 投稿日付
            java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
            java.text.DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());

            if (rensou.getCreatedAt() != null) {
                Date d = rensou.getCreatedAt();
                dateTimeText.setText(dateFormat.format(d) + " " + timeFormat.format(d));
            }

            // いいね！数
            favoriteText.setText(getContext().getString(R.string.ranking_like_count, rensou.getFavorite()));
        }

        return view;
    }
}
