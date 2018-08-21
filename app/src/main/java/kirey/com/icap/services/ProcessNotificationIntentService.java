package kirey.com.icap.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kirey.com.icap.model.RecievedMessage;
import kirey.com.icap.utils.ICAPApp;

/**
 * Created by kitanoskan on 08/06/2017.
 */

public class ProcessNotificationIntentService extends IntentService {

    public ProcessNotificationIntentService(){
        super("processNotificationIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ProcessNotificationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        Bundle extras = intent.getExtras();

        RecievedMessage msgToStore = new RecievedMessage();
        msgToStore.setMessageText((String) extras.get("messageText"));
        msgToStore.setNotificationIcon((String) extras.get("gcm.notification.icon"));
        //msgToStore.setId(Integer.valueOf((String)extras.get("id")));
       // msgToStore.setLocation((String) extras.get("location"));
        //msgToStore.setMessageTimestamp((Long) extras.get("google.sent_time"));
        msgToStore.setMessageTimestamp(Long.parseLong((String) extras.get("gcm.notification.timeSent")));
        msgToStore.setMessageTitle((String) extras.get("gcm.notification.title"));
        //msgToStore.setTargetTimestamp((String) extras.get("targetTimestamp"));//(Long.valueOf((String) extras.get("targetTimestamp")).longValue());
        //msgToStore.setMessageId((String) extras.get("google.message_id"));
        //msgToStore.setAddress((String) extras.get("address"));


        //storeMessage(msgToStore);
        List<RecievedMessage> savedMessages = new ArrayList<RecievedMessage>();
        Gson gson = new Gson();

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        String storedMesgs = appSharedPrefs.getString("recievedMessages",null);

        if(storedMesgs != null)
            //get stored messages
            savedMessages = (ArrayList<RecievedMessage>) fromJson(storedMesgs,
                    new TypeToken<ArrayList<RecievedMessage>>() {
                    }.getType());


        if (savedMessages.size()<((ICAPApp)getApplicationContext()).getMaxSizMessageStorage()) {

            savedMessages.add(msgToStore);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putString("recievedMessages", gson.toJson(savedMessages));
            prefsEditor.commit();
        }
        // else remove oldest and add new
        else  {
            savedMessages.remove(0);
            savedMessages.add(msgToStore);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            prefsEditor.putString("recievedMessages", gson.toJson(savedMessages));
            prefsEditor.commit();
        }

        Intent intentBoadcst = new Intent("sendData");
        sendBroadcast(intentBoadcst);
    }

    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }

    /*public void updateList(){
        //display message to user in list
        Intent intent = new Intent("sendData");
        sendBroadcast(intent);

    }*/
}
