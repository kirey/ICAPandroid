package kirey.com.icap.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;


import kirey.com.icap.R;
import kirey.com.icap.services.GetUserDetailsTask;
import kirey.com.icap.utils.ICAPApp;

/**
 * Created by kitanoskan on 19/05/2017.
 */

public class WelcomeActivity extends Activity {

    // Splash screen timer
    private static int WELCOME_TIME_OUT = 4000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        final String userToken = ((ICAPApp)this.getApplicationContext()).getUserToken();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                if(userToken != null){
                    Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    // Start your login main activity
                    Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, WELCOME_TIME_OUT);
    }
}
