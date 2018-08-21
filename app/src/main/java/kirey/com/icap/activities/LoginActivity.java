package kirey.com.icap.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import kirey.com.icap.R;
import kirey.com.icap.model.UserData;
import kirey.com.icap.services.GetUserDetailsTask;
import kirey.com.icap.services.LoginTask;
import kirey.com.icap.services.SubscribeTask;
import kirey.com.icap.utils.ICAPApp;

/**
 * Created by kitanoskan on 19/05/2017.
 */

public class LoginActivity extends AppCompatActivity implements LoginInterface{

    String TAG = this.getClass().getName();
    private Button loginBtn;
    private EditText usernameInputText, passInputText;
    private RelativeLayout parentLay;
    private String username, password;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(parentLay.getWindowToken(), 0);

                username =  usernameInputText.getText().toString();
                password = passInputText.getText().toString();

                if(username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty())
                {
                    Toast.makeText(LoginActivity.this, getString(R.string.enterAllFields), Toast.LENGTH_LONG).show();
                }else {
                    //check if the same user as previous is loging now
                    //if another user is logging now than remove messages of previous user
                    if (!username.equals(UserData.getInstance().getUsername()))
                        ((ICAPApp) LoginActivity.this.getApplicationContext()).removeAllMessagesFromStorage();
                    login(username, password);
                }

            }
        });
    }

    private void initViews() {
        loginBtn = (Button) findViewById(R.id.loginBtn);
        usernameInputText = (EditText) findViewById(R.id.usernameInput);
        passInputText = (EditText) findViewById(R.id.passInput);
        parentLay = (RelativeLayout) findViewById(R.id.parentRelLay);
    }

    public void login(String username, String password) {
        new LoginTask(this).execute(username, password);
    }

    @Override
    public void subscribe(String fbToken, String userToken) {
        new SubscribeTask(this).execute(fbToken, userToken);
    }

    @Override
    public void getUserDetails(String userToken) {
        new GetUserDetailsTask(this).execute(userToken);
    }


    @Override
    public ProgressDialog getDialog() {
        return dialog;
    }

    @Override
    public void setDialog(ProgressDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void dissmisLoading() {
        if(dialog != null  && dialog.isShowing())
            this.dialog.dismiss();
    }

    @Override
    public void showLoading() {
        if(dialog == null) {
           dialog =  new ProgressDialog(this);
            dialog.setMessage("Logging...");
           this.dialog.show();
        }
        else
            if (!dialog.isShowing())
                this.dialog.show();

    }
}
