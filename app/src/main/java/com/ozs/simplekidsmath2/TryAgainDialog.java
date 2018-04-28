package com.ozs.simplekidsmath2;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class TryAgainDialog extends Dialog implements View.OnClickListener {
    public Activity c;
    public Dialog d;
    public Button yes, no;

    public TryAgainDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    public interface  OnTryAgainListener {
       void OnTryAgain(boolean isTryAgain);
    }


    public OnTryAgainListener doTryAgain=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tryagain_dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                if (doTryAgain!=null){
                    doTryAgain.OnTryAgain(true);
                }
                break;
            case R.id.btn_no:
                if (doTryAgain!=null){
                    doTryAgain.OnTryAgain(false);
                }
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
