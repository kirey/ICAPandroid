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

        //handle notification message if any and set as read
        /*if (getIntent().getExtras() != null) {
            String data = (String) getIntent().getExtras().get("messageText");
            String messageId = (String) getIntent().getExtras().get("google.message_id");
            if(messageId != null)
                new MessageMarkAsReadTask(this).execute(messageId, userToken);

        }*/

        //if userToken not exists hold welcome screen for a while
        //and then redirect it to login screen
        if(userToken == null)
            displayWelcomeScreen();
        else
           //check if token is valid
        //TODO check internet connection
         //   new GetUserDetailsTask(this).execute(userToken);
        {
            Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void displayWelcomeScreen(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your login main activity
                Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, WELCOME_TIME_OUT);
    }
}
