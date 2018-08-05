package com.ozs.simplekidsmath2;

import android.app.Activity;
import android.app.Dialog;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

/*
 * Child Info Dialog - Information about performance for
 * the child and parent dialog box
 * Standard android dialog inheritance
 */
public class ChildInfoDialog extends Dialog  {
    public Activity c;
    public Dialog d;
    private Button ok,okreset;
    private boolean isResetVisible=false;
    private ChildInfo childInfo=null;
    TextView tvGrandTotal;
    TextView tvGrandCorrect;
    TextView tvGrandError;

    TextView tvAddGood,tvAddBad,tvAddTotal;
    TextView tvSubGood,tvSubBad,tvSubTotal;
    TextView tvMultGood,tvMultBad,tvMultTotal;
    TextView tvDivGood,tvDivBad,tvDivTotal;
    LinearLayout ll_lastscore;
    TextView tvLastScoreReset;

    public ChildInfoDialog(Activity a) {
        super(a);
        this.c = a;
        setCancelable(false);
    }

    public ChildInfoDialog(Activity a,ChildInfo _childInfo) {
        super(a);
        this.c = a;
        childInfo=_childInfo;
        setCancelable(false);
    }

    public void setResetVisible(){
        this.isResetVisible=true;
    }

    public ChildInfo getChildInfo() {
        return childInfo;
    }

    public void setChildInfo(ChildInfo childInfo) {
        this.childInfo = childInfo;
    }

    public interface  OnOKChildInfoListener {
        void OnChildInfo();
        void OnParentResetInfo();
    }

    public OnOKChildInfoListener doOK=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.child_info_dialog);
        ok = findViewById(R.id.btn_okchildinfo);
        okreset=findViewById(R.id.btn_okchildreset);

        if (this.isResetVisible){
            okreset.setVisibility(View.VISIBLE);
        }else{
            okreset.setVisibility(View.GONE);
        }

        if (childInfo==null){
            return;
        }
        // UI Assignments
        tvAddTotal= findViewById(R.id.add_total);
        tvAddGood= findViewById(R.id.add_correct);
        tvAddBad= findViewById(R.id.add_error);
        tvSubTotal= findViewById(R.id.sub_total);
        tvSubGood= findViewById(R.id.sub_correct);
        tvSubBad= findViewById(R.id.sub_error);
        tvMultTotal= findViewById(R.id.mult_total);
        tvMultGood= findViewById(R.id.mult_correct);
        tvMultBad= findViewById(R.id.mult_error);
        tvDivTotal= findViewById(R.id.dev_total);
        tvDivGood= findViewById(R.id.dev_correct);
        tvDivBad= findViewById(R.id.dev_error);
        ll_lastscore=findViewById(R.id.last_score_reset_layout);
        tvLastScoreReset= findViewById(R.id.last_score_reset);

        tvAddTotal.setText(childInfo.getAddTotal().toString());
        tvAddGood.setText(childInfo.getAddGood().toString());
        tvAddBad.setText(childInfo.getAddBad().toString());

        tvSubTotal.setText(childInfo.getSubTotal().toString());
        tvSubGood.setText(childInfo.getSubGood().toString());
        tvSubBad.setText(childInfo.getSubBad().toString());

        tvMultTotal.setText(childInfo.getMultTotal().toString());
        tvMultGood.setText(childInfo.getMultGood().toString());
        tvMultBad.setText(childInfo.getMultBad().toString());

        tvDivTotal.setText(childInfo.getDivTotal().toString());
        tvDivGood.setText(childInfo.getDivGood().toString());
        tvDivBad.setText(childInfo.getDivBad().toString());

        // Last Score Reset Date
        if(childInfo.getLastScoreReset()<=0L){
            ll_lastscore.setVisibility(View.GONE);
        }else{
            ll_lastscore.setVisibility(View.VISIBLE);
            Date date=new Date();
            date.setTime(childInfo.getLastScoreReset());
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            tvLastScoreReset.setText(fmtOut.format(date));
        }
        // Grand Total calculations
        tvGrandTotal=findViewById(R.id.grand_total);
        tvGrandCorrect=findViewById(R.id.grand_correct);
        tvGrandError=findViewById(R.id.grand_error);

        Integer grandTotal= childInfo.getAddTotal()+childInfo.getSubTotal()+
                        childInfo.getMultTotal()+childInfo.getDivTotal();
        tvGrandTotal.setText(grandTotal.toString());
        Integer grandCorrect= childInfo.getAddGood()+childInfo.getSubGood()+
                childInfo.getMultGood()+childInfo.getDivGood();
        tvGrandCorrect.setText(grandCorrect.toString());
        Integer grandError= childInfo.getAddBad()+childInfo.getSubBad()+
                childInfo.getMultBad()+childInfo.getDivBad();
        tvGrandError.setText(grandError.toString());

        // Events Invokers

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doOK!=null){
                    doOK.OnChildInfo();
                }
                dismiss();
            }
        });
        okreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doOK!=null){
                    doOK.OnParentResetInfo();
                }
                dismiss();
            }
        });
    }
}
