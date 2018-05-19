package com.ozs.simplekidsmath2;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ChildInfoDialog extends Dialog implements View.OnClickListener {
    public Activity c;
    public Dialog d;
    private Button ok,okreset;
    private boolean isResetVisible=false;
    private ChildInfo childInfo=null;
    TextView tvTotal,tvGood,tvBad;

    TextView tvAddGood,tvAddBad,tvAddTotal;
    TextView tvSubGood,tvSubBad,tvSubTotal;
    TextView tvMultGood,tvMultBad,tvMultTotal;
    TextView tvDivGood,tvDivBad,tvDivTotal;

    public ChildInfoDialog(Activity a) {
        super(a);
        this.c = a;
    }

    public ChildInfoDialog(Activity a,ChildInfo _childInfo) {
        super(a);
        this.c = a;
        childInfo=_childInfo;
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
        ok.setOnClickListener(this);
        okreset.setOnClickListener(this);

        if (this.isResetVisible){
            okreset.setVisibility(View.VISIBLE);
        }else{
            okreset.setVisibility(View.GONE);
        }

        if (childInfo==null){
            return;
        }

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_okchildinfo:
                if (doOK!=null){
                    doOK.OnChildInfo();
                }
                break;
            case R.id.btn_okchildreset:
                if (doOK!=null){
                    doOK.OnParentResetInfo();
                }
                break;
            default:
                break;
        }
        dismiss();
    }
}
