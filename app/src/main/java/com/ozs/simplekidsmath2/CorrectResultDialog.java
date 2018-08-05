package com.ozs.simplekidsmath2;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
/*
 *  Correct Result Dialog
 */
public class CorrectResultDialog extends Dialog {
    public Activity c;
    public Dialog d;
    private Button closeBtn;
    private String factor1,factorOp, factor2,factorResult;

    public interface  OnCloseCorrectResultInfoListener {
        void OnCloseCorrectResult();
    }
    public OnCloseCorrectResultInfoListener closeListener=null;

    public CorrectResultDialog(Activity a) {
        super(a);
        this.c = a;
        setCancelable(false);

    }

    public CorrectResultDialog(Activity a,String _factor1,String _factorOp, String _factor2, String _factorResult) {
        super(a);
        this.c = a;
        this.factor1=_factor1;
        this.factor2=_factor2;
        this.factorOp=_factorOp;
        this.factorResult=_factorResult;
        setCancelable(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.correctresult_dialog);

        TextView tvf1=findViewById(R.id.textresfactor1);
        TextView tvfop=findViewById(R.id.textresfactorop);
        TextView tvf2=findViewById(R.id.textresfactor2);
        TextView tvfeq=findViewById(R.id.textresfactoreq);
        TextView tvfres=findViewById(R.id.textresval);

        tvf1.setText(this.factor1);
        tvfop.setText(this.factorOp);
        tvf2.setText(this.factor2);
        tvfeq.setText("=");
        tvfres.setText(this.factorResult);

        closeBtn = findViewById(R.id.btn_showresult_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closeListener!=null)
                {
                    closeListener.OnCloseCorrectResult();
                }
            }
        });

    }
}
