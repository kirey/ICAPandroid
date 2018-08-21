package kirey.com.icap.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import kirey.com.icap.R;


/**
 * Created by kitanoskan on 04/12/2017.
 */

public class AboutActivity extends AppCompatActivity {

    String TAG = this.getClass().getName();
    private TextView aboutTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutTextView = (TextView) findViewById(R.id.aboutTextView);

        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
            aboutTextView.setText(getString(R.string.aboutText)+" "+info.versionName+"_"+info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }



    }

}
