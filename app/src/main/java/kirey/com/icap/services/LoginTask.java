package kirey.com.icap.services;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import kirey.com.icap.R;
import kirey.com.icap.activities.LoginInterface;
import kirey.com.icap.model.RestResponseDto;
import kirey.com.icap.model.UserData;
import kirey.com.icap.model.UserDetailsDto;
import kirey.com.icap.utils.ICAPApp;
import kirey.com.icap.utils.ICAPCookieStore;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by kitanoskan on 19/05/2017.
 */

public class LoginTask extends AsyncTask<String,Void,String> {

    private static String TAG = "LoginTask";
    Context context;
    //private ProgressDialog dialog;
    private String userToken;
    LoginInterface loginInterface;

    public LoginTask(Activity a) {
        loginInterface = (LoginInterface) a;
        context = a;
    }

    @Override
    protected String doInBackground(String... params) {

        String result = null;
        ResponseEntity<RestResponseDto> response;

        try {
            response = loginUser(params[0].toString(), params[1].toString(), FirebaseInstanceId.getInstance().getToken());
            userToken = (String) ((LinkedHashMap) response.getBody().getData()).get("sessionId");

            UserData.getInstance().setUsername(((LinkedHashMap) response.getBody().getData()).get("username").toString());
            UserData.getInstance().setMail(((LinkedHashMap) response.getBody().getData()).get("email").toString());
            UserData.getInstance().setFirstName(((LinkedHashMap) response.getBody().getData()).get("firstName").toString());
            UserData.getInstance().setLastName(((LinkedHashMap) response.getBody().getData()).get("lastName").toString());

            ((ICAPApp) context.getApplicationContext()).storeUserData(UserData.getInstance());
        }
        catch(Exception e){
            HttpStatus status = null;

            if (e instanceof HttpClientErrorException) {
                status = ((HttpClientErrorException) e).getStatusCode();
                switch (status) {

                    case UNAUTHORIZED: result = context.getString(R.string.credentialsIncorrect); break;
                    case FORBIDDEN:    result = context.getString(R.string.forbidden); break;
                    default: result = context.getString(R.string.generalError);
                }
            }
            else
                if(e instanceof ResourceAccessException)
                    result = context.getString(R.string.serverNotAvailable);
                else
                    result = context.getString(R.string.generalError);
            loginInterface.dissmisLoading();
            Log.e(TAG,e.getMessage());
        }
       return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loginInterface.showLoading();
    }

    @Override
    protected void onPostExecute(String resultMsg) {
        super.onPostExecute(resultMsg);

        //in case of error
        if(resultMsg != null) {
            loginInterface.dissmisLoading();
            Toast.makeText(context, resultMsg, Toast.LENGTH_LONG).show();
        }else {
            //update usertoken
            ((ICAPApp)this.context.getApplicationContext()).setUserToken(userToken);
            loginInterface.subscribe(FirebaseInstanceId.getInstance().getToken(), userToken);

        }
    }

    private ResponseEntity<RestResponseDto> loginUser(String user, String pass, String fbToken) throws Exception {

        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        String url = context.getString(R.string.base_url)+"/authentication";

        HttpHeaders headers = new HttpHeaders();
        headers.add("fbToken", fbToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
       // headers.add("JSESSIONID", "");


        UserDetailsDto userDetailsDto = new UserDetailsDto(user, pass);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(userDetailsDto,headers);

        ResponseEntity<RestResponseDto> result = restTemplate.postForEntity(url, requestEntity, RestResponseDto.class);

        //String cookieString = ((ICAPApp) this.context.getApplicationContext()).getCookieMgr().getCookieStore().getCookies().get(0).getValue();
        //((ICAPApp) this.context.getApplicationContext()).setUserToken(cookieString);

        return result;
    }

}
