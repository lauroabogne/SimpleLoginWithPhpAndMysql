package blogspot.justsimpleinfo.com.simpleloginwithphpandmysql;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Lauro-PC on 6/24/2018.
 */

public class ServerConnectionUtils extends AsyncTask<String,String,Boolean> implements DialogInterface.OnClickListener {


    final static int REGISTER_ACTION = 1;
    final static int LOGIN_ACIONT = 2;

    Context mContext;


    int mAction;
    String mName;
    String mUsername;
    String mPassword;

    String mServerErrorMessage = "";
    String mServerMessage = "";
    boolean mIsSuccess = false;

    AlertDialog mMessageAlertDialog;
    LoadingDialog mLoadingDialog;
    SharedPreferences mSharedPreferences;


    ServerConnectionUtils(Context context,int action,String name,String username,String password){

        mContext = context;
        mAction = action;
        mName = name;
        mUsername = username;
        mPassword = password;

        mSharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        mLoadingDialog = new LoadingDialog(context,"Registering... Please wait...");
    }

    ServerConnectionUtils(Context context,int action,String username,String password){
        mContext = context;
        mAction = action;
        mUsername = username;
        mPassword = password;
        mSharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        mLoadingDialog = new LoadingDialog(context,"Checking... Please wait...");
    }


    @Override
    protected void onPreExecute() {

        mLoadingDialog.show();

    }

    @Override
    protected Boolean doInBackground(String... strings) {

        if(mAction == REGISTER_ACTION){

            return register();

        }else{

            return login();

        }

    }

    @Override
    protected void onPostExecute(Boolean success) {

        mLoadingDialog.mAlertDialog.dismiss();

        if(success){
            mMessageAlertDialog = new MessageAlertDialog(mContext,mServerMessage,true,this).show();

        }else{

            mMessageAlertDialog =  new MessageAlertDialog(mContext,mServerErrorMessage,false,this).show();
        }

    }

    /**
     * registering function
     * @return
     */
    private boolean register(){

        OkHttpClient client = new OkHttpClient();


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", mName)
                .addFormDataPart("username", mUsername)
                .addFormDataPart("password", mPassword)
                .build();

        Request request = new Request.Builder()
                .url(Constant.BASE_URL + Constant.REGISTER_URL)
                .post(requestBody)
                .build();

        try {

            Response response = client.newCall(request).execute();

            /**
             * get response body
             */
            String serverResponse = response.body().string();

            /**
             * convert response string to json object
             */
            JSONObject serverResponseJSON = new JSONObject(serverResponse);

            boolean isSuccessful = serverResponseJSON.getBoolean("success");

            if(isSuccessful){

                mIsSuccess = true;
                mServerMessage = serverResponseJSON.getString("message");

                /**
                 * save to shared pref
                 */
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.commit();

            }else{

                mIsSuccess = false;
                mServerErrorMessage = serverResponseJSON.getString("error_message");
            }


        } catch (IOException e) {
            e.printStackTrace();
            mServerErrorMessage = e.getMessage();
            mIsSuccess = false;
            return  mIsSuccess;

        } catch (JSONException e) {
            e.printStackTrace();
            mServerErrorMessage = e.getMessage();
            mIsSuccess = false;
            return  mIsSuccess;
        }


        return  mIsSuccess;
    }

    /**
     * login function
     * @return
     */
    private boolean login(){

        OkHttpClient client = new OkHttpClient();


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", mUsername)
                .addFormDataPart("password", mPassword)
                .build();

        Request request = new Request.Builder()
                .url(Constant.BASE_URL + Constant.LOGIN_URL)
                .post(requestBody)
                .build();

        try {

            Response response = client.newCall(request).execute();
            String serverResponse = response.body().string();
           JSONObject serverResponseJSON = new JSONObject(serverResponse);



            boolean isSuccessful = serverResponseJSON.getBoolean("success");

            if(isSuccessful){

                mIsSuccess = true;
                mServerMessage = serverResponseJSON.getString("message");
                /**
                 * add to shared preference
                 */
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean(Constant.IS_LOGIN, true);
                editor.putString(Constant.NAME,serverResponseJSON.getJSONObject(Constant.USER_INFO).getString(Constant.NAME));

                editor.commit();

            }else{

                mIsSuccess = false;
                mServerErrorMessage = serverResponseJSON.getString("error_message");
            }


        } catch (IOException e) {
            e.printStackTrace();
            mServerErrorMessage = e.getMessage();
            mIsSuccess = false;
            return  mIsSuccess;

        } catch (JSONException e) {
            e.printStackTrace();
            mServerErrorMessage = e.getMessage();
            mIsSuccess = false;
            return  mIsSuccess;
        }


        return  mIsSuccess;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        mMessageAlertDialog.dismiss();

        if(mIsSuccess && mAction == REGISTER_ACTION){


            RegisterActivity registerActivity = (RegisterActivity) mContext;

            Intent intent = new Intent(mContext,LoginActivity.class);
            registerActivity.startActivity(intent);

            registerActivity.finish();

        }else if(mIsSuccess && mAction == LOGIN_ACIONT){

            LoginActivity mainActivity = (LoginActivity) mContext;

            Intent intent = new Intent(mContext,HomeActivity.class);
            mainActivity.startActivity(intent);

            mainActivity.finish();
        }


    }


    /**
     * Loading dialog
     */
    class LoadingDialog extends AlertDialog.Builder{

        ProgressBar mProgressBar;
        Context mContext;
        AlertDialog mAlertDialog;

        LinearLayout mLoadingLayout;

        LoadingDialog(Context context,String message) {
            super(context);

            mContext = context;
            this.setCancelable(false);


            mLoadingLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.progress_layout,null,false);
            init(message);

        }

        private void init(String message){

            TextView loadingTextView = mLoadingLayout.findViewById(R.id.loading_textview);
            loadingTextView.setText(message);
            this.setView(mLoadingLayout);
        }

        @Override
        public AlertDialog show() {
            mAlertDialog = super.show();

            return mAlertDialog;
        }
    }
}
