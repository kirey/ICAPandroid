package kirey.com.icap.services;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import kirey.com.icap.utils.ICAPApp;

/**
 * Created by kitanoskan on 07/06/2017.
 */

public class AlertBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //get alert level determinate by user
        int alertLevel = ((ICAPApp)context.getApplicationContext()).getAlertLevel();

        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            //String title = (String) bundle.get("gcm.notification.title");
            //int currentAlertLevel = ((ICAPApp)context.getApplicationContext()).getLevelForAlert(title);

            //if(currentAlertLevel >= alertLevel) {
                ComponentName comp = new ComponentName(context.getPackageName(), ProcessNotificationIntentService.class.getName());
                context.startService(intent.setComponent(comp));
            //}
           // else {
               // abortBroadcast();
            //}
        }
    }
}
