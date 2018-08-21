package kirey.com.icap.activities;

import android.app.ProgressDialog;

/**
 * Created by kitanoskan on 24/05/2017.
 */

public interface LoginInterface {
    public void subscribe(String fbToken, String userToken);
    public void getUserDetails(String userToken);
    public ProgressDialog getDialog();
    public void setDialog(ProgressDialog dialog);
    public void dissmisLoading();
    public void showLoading();
}
