package blogspot.justsimpleinfo.com.simpleloginwithphpandmysql;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button mRegisterBtn;
    Button mLoginBtn;

    EditText mNameEditText;
    EditText mUserEditText;
    EditText mPasswordEditText;
    EditText mPasswordEditTextValidation;

    String mName;
    String mUsername;
    String mPassword;
    String mPasswordValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegisterBtn = this.findViewById(R.id.register_button);
        mLoginBtn = this.findViewById(R.id.login_button);

        mNameEditText = this.findViewById(R.id.input_name);
        mUserEditText = this.findViewById(R.id.input_username);
        mPasswordEditText = this.findViewById(R.id.input_password);
        mPasswordEditTextValidation = this.findViewById(R.id.input_password_validation);


        mRegisterBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        mName = mNameEditText.getText().toString().trim();
        mUsername = mUserEditText.getText().toString().trim();
        mPassword = mPasswordEditText.getText().toString().trim();
        mPasswordValidation = mPasswordEditTextValidation.getText().toString();

        switch (v.getId()){
            case R.id.register_button:


                if(mName.length() <=0){

                    Toast.makeText(this, "Name is require "+mName, Toast.LENGTH_SHORT).show();
                    return;

                }

                if(mUsername.length() <=0){

                    Toast.makeText(this, "Username is require", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mPassword.length() <=0){
                    Toast.makeText(this, "Password is require", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!mPassword.equals(mPasswordValidation)){
                    Toast.makeText(this, "Password not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                new ServerConnectionUtils(this,ServerConnectionUtils.REGISTER_ACTION,mName,mUsername,mPassword).execute();
                break;
            case R.id.login_button:
                Intent loginIntent = new Intent(this.getApplicationContext(),LoginActivity.class);
                startActivity(loginIntent);
                this.finish();
                break;
            default:
        }

    }
}
