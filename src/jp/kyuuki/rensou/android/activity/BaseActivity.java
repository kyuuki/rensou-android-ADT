package jp.kyuuki.rensou.android.activity;

import jp.kyuuki.rensou.android.Analysis;
import jp.kyuuki.rensou.android.R;
import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * 全画面共通。
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStart();
        EasyTracker.getInstance().activityStop(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_about:
            EasyTracker.getTracker().sendEvent(Analysis.GA_EC_UI_ACTION, Analysis.GA_EA_MENU_ABOUT, null, null);

            PackageManager pm = this.getPackageManager();
            String versionName = "";
            PackageInfo packageInfo;
            try {
                packageInfo = pm.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
                versionName = getString(R.string.version) + " " + packageInfo.versionName;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
                versionName = "";
            }
            
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            AlertDialog ad = ab.create();
            ad.setTitle(getString(R.string.action_about));
            ad.setMessage(getString(R.string.app_name) + " " + versionName);
            ad.show();
            break;
        }
        
        return true;
    }
}
