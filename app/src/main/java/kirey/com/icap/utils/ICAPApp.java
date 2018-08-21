package kirey.com.icap.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kirey.com.icap.model.RecievedMessage;
import kirey.com.icap.model.UserData;

import static kirey.com.icap.services.ProcessNotificationIntentService.fromJson;


/**
 * Created by kitanoskan on 30/05/2017.
 */

public class ICAPApp extends Application {

    //private String username = "unknown";
    private String messageRecieved = "No messages";
    //private String companyName = "unknown";
    public static final int MAX_MESSAGES_STORAGE_SIZE = 100;

    private CookieManager cookieMgr;

    /*
    public String getUsername(){ return username; }
    public void setUsername(String user) { username = user; }

    public void setCompanyName(String nameComp){ companyName = nameComp;}
    public String getCompanyName(){return companyName;}
    */

    //public String getMessageRecieved(){ return messageRecieved; }
    //public void setMessageRecieved(String msg) { messageRecieved = msg; }

    @Override
    public void onCreate() {
        super.onCreate();
        cookieMgr = new CookieManager(new ICAPCookieStore(getApplicationContext()), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieMgr);
    }

    public CookieManager getCookieMgr() {
        return cookieMgr;
    }

    /**
     * Method whish stores userData to shared preferences
     * @param userData
     * @return
     */
    public void storeUserData(UserData userData){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("KGRIPref", 0);

        Gson gson = new Gson();
        String userDataJson = gson.toJson(userData);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserData", userDataJson);
        editor.commit();
    }

    /**
     * Load user data from Shared Preferences
     * @return
     */
    public UserData loadUserData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("KGRIPref", 0);
        String userDataJson = pref.getString("UserData", "");

        Gson gson = new Gson();
        UserData userData = gson.fromJson(userDataJson, UserData.class);

        UserData.getInstance().setLastName(userData.getLastName());
        UserData.getInstance().setFirstName(userData.getFirstName());
        UserData.getInstance().setMail(userData.getMail());
        UserData.getInstance().setUsername(userData.getUsername());

        return userData;
    }

    /**
     * method for getting userToken from shared preferences
     * @return
     */
    public String getUserToken(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ICAPPref", 0);
        String userToken = pref.getString("sessionId", null);
        return userToken;
    }

    /**
     * method for setting userToken in shared preferences
     * @return
     */
    public void setUserToken(String token){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("ICAPPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("sessionId", token);
        editor.commit();
    }

    /**
     * method for updating  userToken in shared preferences
     * @return
     */
    public void updateUserToken(String token){
        if(!token.equals(getUserToken()))
            setUserToken(token);
    }

    /**
     * set message status read to true
     * @param msgId
     */
    public void markMsgAsRead(String msgId){
        List<RecievedMessage> messages = getStoredMessages();
        for (RecievedMessage msg : messages) {
           if(msg.getMessageId() != null)
               if(msg.getMessageId().equals(msgId)) {
                   msg.setRead(true);
                   break;
               }
        }

        storeMessages(messages);

    }

    /**
     * method storing messages in shared preferences
     * @param messages
     */
    public void storeMessages(List<RecievedMessage> messages) {

        Gson gson = new Gson();

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());


        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("recievedMessages", gson.toJson(messages));
        prefsEditor.commit();

    }

    /**
     * gets stored mesages from shared preferences
     * @return
     *
     */
    public List<RecievedMessage> getStoredMessages(){
        List<RecievedMessage> savedMessages = new ArrayList<RecievedMessage>();

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        String storedMesgs = appSharedPrefs.getString("recievedMessages",null);

        if(storedMesgs != null)
            //get stored messages
            savedMessages = (ArrayList<RecievedMessage>) fromJson(storedMesgs,
                    new TypeToken<ArrayList<RecievedMessage>>() {
                    }.getType());

        return savedMessages;
    }

    public void removeAllMessagesFromStorage(){

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());


        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("recievedMessages", null);
        prefsEditor.commit();
    }

    /**
     * sets choosen level alert in shared preferences
     * @param level
     */
    public void setAlertLevel(int level){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putInt("alertLevel", level);
        prefsEditor.commit();
    }

    /**
     * reads current alert level from shared preferences
     * @return
     */
    public int getAlertLevel(){

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        return appSharedPrefs.getInt("alertLevel",1);
    }

    /**
     * Method gives corresponding int value for selected alert
     * @param title
     * @return
     */
    public int getLevelForAlert(String title) {

        if(title.toLowerCase().contains("red"))
            return 3;
        else  if(title.toLowerCase().contains("orange"))
            return 2;
        else  if(title.toLowerCase().contains("yellow"))
            return 1;

        return 0;
    }

    /**
     * Mehod sets new storage size
     * if new storage size is smaller than old one, older messages are deleted
     * @param newSize
     */
    public void setMaxSizeMessageStorage(int newSize){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        if(getMaxSizMessageStorage() > newSize){
            //we delete older messages

            List<RecievedMessage> messagesFromStorage = getStoredMessages();

            if(messagesFromStorage.size() > newSize)
            {
                Collections.sort(messagesFromStorage, new Comparator<RecievedMessage>() {
                    @Override
                    public int compare(RecievedMessage r1, RecievedMessage r2) {
                        return Long.valueOf(r1.getMessageTimestamp()).compareTo(Long.valueOf(r2.getMessageTimestamp()));
                    }
                });

                List<RecievedMessage> newMessagesList = new ArrayList<RecievedMessage>();

                for (int i = 0; i < newSize; i++) {
                    newMessagesList.add(messagesFromStorage.get(i));
                }

                storeMessages(newMessagesList);
            }
        }

        //set new size
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putInt("msgStorageSize", newSize);
        prefsEditor.commit();



    }

    /**
     * Method returns storage size
     * Defoult storage size is 10
     * @return
     */
    public int getMaxSizMessageStorage(){

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        return appSharedPrefs.getInt("msgStorageSize",10);
    }

    public void deleteMessageFromStorage(int id){
        List<RecievedMessage> messagesFromStorage = getStoredMessages();

        for(int i=0; i<messagesFromStorage.size(); i++)
        {
            if(messagesFromStorage.get(i).getId() == id)
                messagesFromStorage.remove(i);
        }

        storeMessages(messagesFromStorage);
    }
}
