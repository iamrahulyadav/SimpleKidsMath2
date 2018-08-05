package com.ozs.simplekidsmath2;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
/*
  Try Again Dialog (for wrong answer)
 */
public class TryAgainDialog extends Dialog  {
    public Activity c;
    public Dialog d;
    public Button yes, no;

    public TryAgainDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        setCancelable(false);
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
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doTryAgain!=null){
                    doTryAgain.OnTryAgain(true);
                }
                dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doTryAgain!=null){
                    doTryAgain.OnTryAgain(false);
                }
                dismiss();
            }
        });
    }
}
