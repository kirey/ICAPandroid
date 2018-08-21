package kirey.com.icap.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kirey.com.icap.R;
import kirey.com.icap.model.RecievedMessage;
import kirey.com.icap.model.UserData;
import kirey.com.icap.services.LogoutTask;
import kirey.com.icap.services.MessageMarkAsReadTask;
import kirey.com.icap.utils.ICAPApp;
import kirey.com.icap.utils.MessageListAdapter;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    private Button subscribeGroup1Btn, subscribeGroup2Btn, subscribeGroup3Btn, loginBtn;
    private TextView msgTextView, usernameTextView, firstNameTextView, lastNameTextView, emailTextView;
    private LinearLayout deleteSelectionLinLay;
    private ListView messagesListView;
    private List<RecievedMessage> storedMessages;
    MessageBroadcastReciver broadCastReciver;
    MessageListAdapter listAdapter;
    boolean deleteMode =  false;
    MenuItem deleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.v("TOKEN", FirebaseInstanceId.getInstance().getToken());

        final String userToken = ((ICAPApp)this.getApplicationContext()).getUserToken();

        firstNameTextView = (TextView) findViewById(R.id.firstNameTextView);
        lastNameTextView = (TextView) findViewById(R.id.lastNameTextView);
        msgTextView = (TextView) findViewById(R.id.msgTextView);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        messagesListView = (ListView) findViewById(android.R.id.list);
        emailTextView = (TextView) findViewById(R.id.emailTextView);


        storedMessages = ((ICAPApp)this.getApplicationContext()).getStoredMessages();
        //sort desc
        Collections.sort(storedMessages, new Comparator<RecievedMessage>() {
            @Override
            public int compare(RecievedMessage r1, RecievedMessage r2) {
                return Long.valueOf(r1.getMessageTimestamp()).compareTo(Long.valueOf(r2.getMessageTimestamp()));
            }
        });

        //load userdata
        UserData userData = ((ICAPApp) this.getApplicationContext()).loadUserData();

        listAdapter = new MessageListAdapter(this,R.layout.message_list_item, storedMessages);
        messagesListView.setAdapter(listAdapter);
        emailTextView.setText(UserData.getInstance().getMail());
        usernameTextView.setText(UserData.getInstance().getUsername());
        firstNameTextView.setText(UserData.getInstance().getFirstName());
        lastNameTextView.setText(UserData.getInstance().getLastName());

        messagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView c = (TextView) v.findViewById(R.id.titleItemTextView);
                //if(!listAdapter.getItem(position).isRead())
                    //new MessageMarkAsReadTask(MainActivity.this).execute(listAdapter.getItem(position).getMessageId(), userToken);
            }
        });

        messagesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                //show delete all button in menu
                deleteAll.setVisible(true);

                //show delete option for each list item
                listAdapter.showDeleteSection(true);
                deleteMode = true;
                refreshMessages();
                return true;
            }
        });


        //handle notification message if any
       /* if (getIntent().getExtras() != null) {
            String data = (String) getIntent().getExtras().get("messageText");
            if(data != null)
                refreshTextMessage(data);
        }*/

        //refreshTextMessage(((KgriApp) this.getApplicationContext()).getMessageRecieved());
        refreshMessages();
    }

    private class MessageBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshMessages();
        }
    }

    private void refreshMessages() {

        storedMessages = ((ICAPApp)this.getApplicationContext()).getStoredMessages();

        if(storedMessages.size() == 0)
            msgTextView.setVisibility(View.VISIBLE);

        //refresh list of messages
        else {
            msgTextView.setVisibility(View.GONE);

            Collections.sort(storedMessages, new Comparator<RecievedMessage>() {
                @Override
                public int compare(RecievedMessage r1, RecievedMessage r2) {
                    return Long.valueOf(r2.getMessageTimestamp()).compareTo(Long.valueOf(r1.getMessageTimestamp()));
                }
            });

        }
        listAdapter.refresh(storedMessages);
    }



    @Override
    protected void onStop() {
        super.onStop();
        if(broadCastReciver!=null)
            unregisterReceiver(broadCastReciver);
    }

    protected void onResume() {
        super.onResume();
        refreshMessages();
        broadCastReciver = new MessageBroadcastReciver();
        registerReceiver(broadCastReciver, new IntentFilter("sendData"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                new LogoutTask(this).execute();
                return true;

            case R.id.action_settings:

                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;

            case R.id.action_delete:

                ArrayList<RecievedMessage> listForDelete= listAdapter.getCheckedItems();
                for (RecievedMessage msg:listForDelete) {
                    ((ICAPApp)this.getApplicationContext()).deleteMessageFromStorage(msg.getId());
                }
                listAdapter.showDeleteSection(false);
                deleteMode = false;
                deleteAll.setVisible(false);
                refreshMessages();
                return true;

            case R.id.action_about:

                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_action_bar, menu);
        deleteAll = menu.findItem(R.id.action_delete);
        return true;
    }

    @Override
    public void onBackPressed() {

        if(deleteMode) {
            listAdapter.showDeleteSection(false);
            listAdapter.uncheckAll();
            deleteMode = false;
            deleteAll.setVisible(false);
            refreshMessages();
        }
        else super.onBackPressed();
    }

}
