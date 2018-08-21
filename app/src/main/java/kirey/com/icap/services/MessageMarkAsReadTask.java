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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import kirey.com.icap.R;
import kirey.com.icap.model.RestResponseDto;
import kirey.com.icap.utils.ICAPApp;

/**
 * Created by kitanoskan on 12/06/2017.
 */

public class MessageMarkAsReadTask extends AsyncTask<String,Void,String> {

    private static String TAG = "MessageMarkAsReadTask";
    Context context;
    private ProgressDialog dialog;
    //LoginInterface loginInterface;

    public MessageMarkAsReadTask(Activity a) {
        //loginInterface = (LoginInterface) a;
        context = a;

    }


    @Override
    protected String doInBackground(String... params) {
        String result = null;
        try {

            ResponseEntity<RestResponseDto> response = markMessageAsRead(params[0].toString(), params[1].toString());
            ((ICAPApp)context.getApplicationContext()).markMsgAsRead(params[0].toString());
        }
        catch(Exception e){

            if (e instanceof HttpClientErrorException) {
                switch (((HttpClientErrorException) e).getStatusCode()) {

                    case UNAUTHORIZED: result = (((HttpClientErrorException) e).getStatusCode()).toString(); break;
                    case FORBIDDEN:    result = context.getString(R.string.forbidden); break;
                    default: result = context.getString(R.string.generalError);
                }
            }
            else  if(e instanceof ResourceAccessException)
                result =  context.getString(R.string.serverNotAvailable);
            else
                result = context.getString(R.string.generalError);

            Log.e(TAG,e.getMessage());
        }
        return result;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String message) {
        super.onPostExecute(message);
        if(message!=null)
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
        else {
            Intent intentBoadcst = new Intent("sendData");
            context.sendBroadcast(intentBoadcst);
        }
    }

    private ResponseEntity<RestResponseDto> markMessageAsRead(String messageId, String sessionID) {


        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.add("JSESSIONID", sessionID);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);

        String url = context.getString(R.string.base_url)+"/alertRecieved/"+messageId;

        ResponseEntity<RestResponseDto> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, RestResponseDto.class);

        return result;
    }
}
