package kirey.com.icap.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import kirey.com.icap.R;
import kirey.com.icap.activities.LoginInterface;
import kirey.com.icap.activities.MainActivity;
import kirey.com.icap.model.RestResponseDto;
import kirey.com.icap.utils.ICAPApp;

/**
 * Created by kitanoskan on 23/05/2017.
 */

public class SubscribeTask extends AsyncTask<String,Void,String> {

    private static String TAG = "SubscribeTask";
    Context context;
    private ProgressDialog dialog;
    LoginInterface loginInterface;

    public SubscribeTask(Activity a) {
        loginInterface = (LoginInterface) a;
        context = a;

    }

    @Override
    protected String doInBackground(String... params) {

        String result = null;
        try {
            subscribe(params[0].toString(), params[1].toString());
        }
        catch(Exception e){
            Log.e(TAG, "ERROR DURING SUBSCRIBE: " + e.toString());

            HttpStatus status = null;
            if(e instanceof HttpClientErrorException)
                status = ((HttpClientErrorException) e).getStatusCode();
                switch (status) {
                    case UNAUTHORIZED:
                        //case when user's mobile token is changed for some reason;
                        // user is loggedout and should be redirected to login screen
                        return status.toString() + context.getString( R.string.subscribeError);
                    case FORBIDDEN:
                        ((ICAPApp)this.context.getApplicationContext()).setUserToken(null);
                        return (context.getString(R.string.noPermission));
                    default:
                        return (context.getString(R.string.generalError));
            }
        }

        return result;
    }


    @Override
    protected void onPreExecute() {
        loginInterface.showLoading();
    }

    @Override
    protected void onPostExecute(String message) {
        super.onPostExecute(message);

         loginInterface.dissmisLoading();
         if(message==null) {
             //loginInterface.getUserDetails(((ICAPApp)this.context.getApplicationContext()).getUserToken()); //sada nema getUserDetails, ali ako bude bilo moze da se vrati ovo
             context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
             ((Activity)context).finish();
         }
         else Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }

    private ResponseEntity<RestResponseDto> subscribe(String fbToken, String sessionID) throws Exception {

        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.add("JSESSIONID", sessionID);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);

        //TODO later change topid to be dynamic
        String url = context.getString(R.string.base_url)+"/subscribeToTopic/"+fbToken+"/"+"ICAP";

        ResponseEntity<RestResponseDto> result = restTemplate.exchange(url, HttpMethod.POST, requestEntity, RestResponseDto.class);

        return result;
    }
}
