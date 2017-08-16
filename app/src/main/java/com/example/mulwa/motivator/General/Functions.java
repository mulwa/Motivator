package com.example.mulwa.motivator.General;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by mulwa on 8/13/17.
 */

public class Functions {

    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

}
