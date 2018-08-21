package kirey.com.icap.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

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

import java.io.IOException;

import kirey.com.icap.R;
import kirey.com.icap.activities.LoginActivity;
import kirey.com.icap.model.RestResponseDto;
import kirey.com.icap.utils.ICAPApp;

/**
 * Created by kitanoskan on 20/06/2017.
 */

public class LogoutTask extends AsyncTask<Void,Void,String> {

    private static String TAG = "GetUserDetailsTask";
    Context context;

    public LogoutTask(Activity a) {
        context = a;
    }


    @Override
    protected String doInBackground(Void... params) {
        String message = null;

        try {
            ResponseEntity<RestResponseDto> result = logout();
            if (result.getStatusCode() == HttpStatus.OK) {
                logoutUser();
            }
        } catch (Exception e) {
            if (e instanceof HttpClientErrorException) {
                message = context.getString(R.string.errorLogout);
            }
            else  if(e instanceof ResourceAccessException)
                message = context.getString(R.string.serverNotAvailable);
            else message = context.getString(R.string.generalError);
        }

        return message;
    }

    private void logoutUser() throws IOException {

        ((ICAPApp)this.context.getApplicationContext()).setUserToken(null);
        FirebaseInstanceId.getInstance().deleteInstanceId();

        SharedPreferences pref = context.getSharedPreferences("KGRIPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", null);
        editor.commit();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s == null){
            context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            ((Activity)context).finish();
        }else
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private ResponseEntity<RestResponseDto> logout() {

        RestTemplate restTemplate = new RestTemplate(true);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Mobile-Auth-Token", ((ICAPApp)this.context.getApplicationContext()).getUserToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);

        //TODO later change topid to be dynamic
        String url = context.getString(R.string.base_url)+"/logout";

        ResponseEntity<RestResponseDto> result = restTemplate.exchange(url, HttpMethod.POST, requestEntity, RestResponseDto.class);

        return result;

    }
}
