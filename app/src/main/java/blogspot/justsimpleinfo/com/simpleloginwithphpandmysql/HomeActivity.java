package blogspot.justsimpleinfo.com.simpleloginwithphpandmysql;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mUserNameTextView;
    Button mLogoutButton;
    SharedPreferences mSharedPreferences;
    String mName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = this.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME,MODE_PRIVATE);
        mName = mSharedPreferences.getString(Constant.NAME,null);

        setContentView(R.layout.activity_home);

        mUserNameTextView = this.findViewById(R.id.username_textview);

        mLogoutButton = this.findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(this);

        if(mName !=null){

            mUserNameTextView.setText(mName);
        }
    }

    @Override
    public void onClick(View view) {

        /**
         * logout
         */
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Constant.IS_LOGIN, false);
        editor.putString(Constant.NAME,"");
        editor.commit();


        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivity(loginIntent);
        this.finish();


    }
}
