package kirey.com.icap.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

import kirey.com.icap.R;
import kirey.com.icap.activities.LoginActivity;
import kirey.com.icap.activities.MainActivity;
import kirey.com.icap.model.RestResponseDto;
import kirey.com.icap.model.UserData;
import kirey.com.icap.utils.ICAPApp;

/**
 * Created by kitanoskan on 30/05/2017.
 */

public class GetUserDetailsTask extends AsyncTask<String,Void,String> {

    private String TAG = this.getClass().getName();
    Context context;

    public GetUserDetailsTask(Activity a) {
        context = a;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            ResponseEntity<RestResponseDto> result = checkUserDetails(params[0]);
            if (result.getStatusCode() == HttpStatus.OK) {

                UserData.getInstance().setMail(((LinkedHashMap) result.getBody().getData()).get("mail").toString());
                UserData.getInstance().setUsername(((LinkedHashMap) result.getBody().getData()).get("username").toString());

                ((ICAPApp) context.getApplicationContext()).storeUserData(UserData.getInstance());

                return "LOGGED_YES";
            }
        } catch (Exception e) {
            if (e instanceof HttpClientErrorException) {
                HttpStatus status = null;
                status = ((HttpClientErrorException) e).getStatusCode();
                switch (status) {
                    case UNAUTHORIZED:
                        //case when user's mobile token is changed for some reason;
                        // user is loggedout and should be redirected to login screen
                        return (null);    //context.getString(R.string.getUserDataFailed);
                    case FORBIDDEN:
                        ((ICAPApp)this.context.getApplicationContext()).setUserToken(null);
                        return context.getString(R.string.noPermission);
                    default:
                        return context.getString(R.string.generalError);
                }
            }
            if(e instanceof ResourceAccessException)
                return "Server not availabe";
            else return context.getString(R.string.generalError);
        }
        return ("An error occured during login");
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //redirect user to main activity if token is valid
        //otherwise reditect it to login activity
        if(result != null && result.equals("LOGGED_YES"))
            context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        else {
            if(result != null)
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        ((Activity)context).finish();
    }

    public ResponseEntity<RestResponseDto> checkUserDetails(String sessionID){
        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.add("JSESSIONID", sessionID);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);

        //TODO later change topic to be dynamic
        String url = context.getString(R.string.base_url)+"/userDetails";

        ResponseEntity<RestResponseDto> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, RestResponseDto.class);
        return result;
    }
}
