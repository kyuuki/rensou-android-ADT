package jp.kyuuki.rensou.android.activity;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import jp.kyuuki.rensou.android.R;
import android.os.Bundle;
import android.view.WindowManager.LayoutParams;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 自動でソフトキーボードが出るのを防ぐ
        // http://y-anz-m.blogspot.jp/2010/05/android_17.html
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        
        setContentView(R.layout.activity_main);
        
        // AdView をリソースとしてルックアップしてリクエストを読み込む。
        // https://developers.google.com/mobile-ads-sdk/docs/android/banner_xml?hl=ja
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest();
        //adRequest.addTestDevice("6E5BC489C3B529363F063C3C74151BC7");
        adView.loadAd(adRequest);
    }
}
