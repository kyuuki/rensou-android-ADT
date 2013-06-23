package jp.kyuuki.rensou.android;

import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

/**
 * 全画面共通。
 */
public abstract class CommonActivity extends FragmentActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_about:
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
