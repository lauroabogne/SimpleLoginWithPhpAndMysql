package blogspot.justsimpleinfo.com.simpleloginwithphpandmysql;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    Button mLoginBtn;
    Button mGoToRegisterBtn;
    EditText mLoginUserNameInput;
    EditText mLoginPasswordInput;
    android.app.AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.checkIfAlreadyLogin();

        setContentView(R.layout.activity_login);

        mLoginBtn = findViewById(R.id.login_button);
        mGoToRegisterBtn = findViewById(R.id.go_to_register_button);
        mLoginUserNameInput = findViewById(R.id.login_username_input);
        mLoginPasswordInput = findViewById(R.id.login_password_input);

        mLoginBtn.setOnClickListener(this);
        mGoToRegisterBtn.setOnClickListener(this);

    }

    private void checkIfAlreadyLogin(){
        SharedPreferences sharedPreferences  =this.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME,MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean(Constant.IS_LOGIN,false);

        if(isLogin){

            Intent homeIntent = new Intent(this,HomeActivity.class);
            startActivity(homeIntent);
            finish();

        }

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.go_to_register_button:

                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);

                break;
            case R.id.login_button:
                this.login();
                break;
            default:

        };



    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        mAlertDialog.dismiss();

    }

    private void login(){
        String username = mLoginUserNameInput.getText().toString().trim();
        String password = mLoginPasswordInput.getText().toString().trim();

        if(username.length() <=0){

            mAlertDialog = new MessageAlertDialog(this,"Username require",false,this).show();

            return;
        }

        if(password.length() <=0){

            mAlertDialog = new MessageAlertDialog(this,"Password require",false,this).show();

            return;
        }

        new ServerConnectionUtils(this,ServerConnectionUtils.LOGIN_ACIONT,username,password).execute();
    }
}
