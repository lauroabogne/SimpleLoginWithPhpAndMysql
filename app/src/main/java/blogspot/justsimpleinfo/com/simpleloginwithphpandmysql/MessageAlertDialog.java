package blogspot.justsimpleinfo.com.simpleloginwithphpandmysql;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;

/**
 * Created by Lauro-PC on 6/24/2018.
 */

public class MessageAlertDialog extends AlertDialog.Builder {

    AlertDialog mAlertDialog;
    String mMessage;
    boolean mSuccess;
    Context mContext;

    DialogInterface.OnClickListener mOnClickListener;

    public MessageAlertDialog(Context context) {
        super(context);
        mContext = context;
    }

    public MessageAlertDialog(Context context,String message,boolean success,DialogInterface.OnClickListener onClickListener) {
        super(context);
        mContext = context;
        mMessage = message;
        mSuccess = success;
        mOnClickListener = onClickListener;
        this.init();
    }

    private void init(){

        if(mSuccess){
            this.setTitle("Message");
            this.setMessage(mMessage);
        }else{

            this.setTitle("Error Message");

            this.setMessage(Html.fromHtml("<font color='red'>"+mMessage+"</font>"));

        }


        this.setPositiveButton("Close", mOnClickListener);

    }
    @Override
    public AlertDialog show() {

        mAlertDialog = super.show();

        return mAlertDialog;
    }
}
