package com.ozs.simplekidsmath2;

import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
/*
 *  Child Options Activity
 */
public class OptionsActivity extends AppCompatActivity {

    private static final String STATE_MIN="STATE_MIN";
    private static final String STATE_MAX="STATE_MAX";
    private static final String STATE_MINMULT="STATE_MINMULT";
    private static final String STATE_MAXMULT="STATE_MAXMULT";
    private static final String STATE_MINDIV="STATE_MINDIV";
    private static final String STATE_MAXDIV="STATE_MAXDIV";

    private static final String STATE_ISADD="STATE_ISADD";
    private static final String STATE_ISSUB="STATE_ISSUB";
    private static final String STATE_ISMULT="STATE_ISMULT";
    private static final String STATE_ISDIV="STATE_ISDIV";


    ChildrenList m_clist;
    Child selectedChild;
    EditText etMinValue;
    EditText etMaxValue;
    EditText etMinValueMult;
    EditText etMaxValueMult;
    EditText etMinValueDiv;
    EditText etMaxValueDiv;
    CheckBox cbAddOp;
    CheckBox cbSubOp;
    CheckBox cbMultOp;
    CheckBox cbDivOp;
    CheckBox cbAllowMinusRes;
    TextView tvError2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);


        m_clist=ChildrenList.getInstance();
        m_clist.setContext(this);
        selectedChild=m_clist.getSelectedChild();

        FindViews();
        UpdateUIFromPreferences();

    }
    /*
      Assign Views
     */
    protected void FindViews()
    {
        etMinValue=findViewById(R.id.editTextMin);
        etMaxValue=findViewById(R.id.editTextMax);
        etMinValueMult=findViewById(R.id.editTextMinMult);
        etMaxValueMult=findViewById(R.id.editTextMaxMult);
        etMinValueDiv=findViewById(R.id.editTextMinDiv);
        etMaxValueDiv=findViewById(R.id.editTextMaxDiv);

        cbAddOp=findViewById(R.id.checkBoxAdd);
        cbSubOp=findViewById(R.id.checkBoxSub);
        cbMultOp=findViewById(R.id.checkBoxMult);
        cbDivOp=findViewById(R.id.checkBoxDiv);
        cbAllowMinusRes=findViewById(R.id.checkAllowMinus);
        tvError2=findViewById(R.id.optionsErrorMsg2);

    }
    /*
      onResume - Standard android event
     */
    @Override
    protected void onResume() {
        super.onResume();

        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    /*
      Menu Options setting
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  MenuInflater inflater = getMenuInflater();
      //  inflater.inflate(R.menu.main, menu);
        return true;
    }
    /*
      Menu Options Results
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId()==android.R.id.home) {
           if (SavePreferences(true)){
               finish();
           }

        }
        return true;
    }
    /*
      Save Preferences on "Back" Pressed
     */
    @Override
    public void onBackPressed()
    {
        SavePreferences(true);
    }
    /*
       Handle "Back"
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // OnKeyPressed is not supported below android 5
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                SavePreferences(true);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    /*
      Save State
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(STATE_MIN,etMinValue.getText().toString());
        outState.putString(STATE_MAX,etMaxValue.getText().toString());
        outState.putString(STATE_MINMULT,etMinValueMult.getText().toString());
        outState.putString(STATE_MAXMULT,etMaxValueMult.getText().toString());
        outState.putString(STATE_MINDIV,etMinValueDiv.getText().toString());
        outState.putString(STATE_MAXDIV,etMaxValueDiv.getText().toString());
        outState.putBoolean(STATE_ISADD,cbAddOp.isChecked());
        outState.putBoolean(STATE_ISSUB,cbSubOp.isChecked());
        outState.putBoolean(STATE_ISMULT,cbMultOp.isChecked());
        outState.putBoolean(STATE_ISDIV,cbDivOp.isChecked());
    }
    /*
      Restore State
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(STATE_MIN)){
            etMinValue.setText(savedInstanceState.getString(STATE_MIN));
        }
        if (savedInstanceState.containsKey(STATE_MAX)){
            etMaxValue.setText(savedInstanceState.getString(STATE_MAX));
        }
        if (savedInstanceState.containsKey(STATE_MINMULT)){
            etMinValueMult.setText(savedInstanceState.getString(STATE_MINMULT));
        }
        if (savedInstanceState.containsKey(STATE_MAXMULT)){
            etMaxValueMult.setText(savedInstanceState.getString(STATE_MAXMULT));
        }
        if (savedInstanceState.containsKey(STATE_MINDIV)){
            etMinValueDiv.setText(savedInstanceState.getString(STATE_MINDIV));
        }
        if (savedInstanceState.containsKey(STATE_MAXDIV)){
            etMaxValueDiv.setText(savedInstanceState.getString(STATE_MAXDIV));
        }

        if (savedInstanceState.containsKey(STATE_ISADD)){
            cbAddOp.setChecked(savedInstanceState.getBoolean(STATE_ISADD));
        }
        if (savedInstanceState.containsKey(STATE_ISSUB)){
            cbSubOp.setChecked(savedInstanceState.getBoolean(STATE_ISSUB));
        }
        if (savedInstanceState.containsKey(STATE_ISMULT)){
            cbMultOp.setChecked(savedInstanceState.getBoolean(STATE_ISMULT));
        }
        if (savedInstanceState.containsKey(STATE_ISDIV)){
            cbDivOp.setChecked(savedInstanceState.getBoolean(STATE_ISDIV));
        }
    }
    /*
       Update UI With the current preferences
     */
    private void UpdateUIFromPreferences()
    {
        m_clist.setContext(this);

        etMinValue.setText(selectedChild.getMinparam().toString());
        etMaxValue.setText(selectedChild.getMaxparam().toString());
        etMinValueMult.setText(selectedChild.getMinparammult().toString());
        etMaxValueMult.setText(selectedChild.getMaxparammult().toString());
        etMinValueDiv.setText(selectedChild.getMinparamdiv().toString());
        etMaxValueDiv.setText(selectedChild.getMaxparamdiv().toString());


        cbAddOp.setChecked(selectedChild.getAdd());
        cbSubOp.setChecked(selectedChild.getSub());
        cbMultOp.setChecked(selectedChild.getMult());
        cbDivOp.setChecked(selectedChild.getDiv());
        cbAllowMinusRes.setChecked(selectedChild.getAllowMinusResult());
        tvError2.setText("");

    }
    /*
       Validate input
     */
    private Boolean Validate() {
        String strMin=etMinValue.getText().toString();
        String strMax=etMaxValue.getText().toString();
        String strMinMult=etMinValueMult.getText().toString();
        String strMaxMult=etMaxValueMult.getText().toString();
        String strMinDiv=etMinValueDiv.getText().toString();
        String strMaxDiv=etMaxValueDiv.getText().toString();

        Integer n1,n2,n3,n4,n5,n6;

        try {
            n1 = Integer.parseInt(strMin);
            n2 = Integer.parseInt(strMax);
            n3 = Integer.parseInt(strMinMult);
            n4 = Integer.parseInt(strMaxMult);
            n5 = Integer.parseInt(strMinDiv);
            n6 = Integer.parseInt(strMaxDiv);

        }catch (NumberFormatException e) {
            tvError2.setText(R.string.option_error_invalidint);
            return false;
        }
        if (!cbAllowMinusRes.isChecked())
        {
            if ((n1<0)||(n2<0))
            {
                tvError2.setText(R.string.option_error_minusparams);
                return false;
            }
        }

        if (!cbAllowMinusRes.isChecked())
        {
            if (n1>n2)
            {
                tvError2.setText(R.string.option_error_fromltto);
                return false;
            }
        }
        if (n3>n4)
        {
            tvError2.setText(R.string.option_error_fromltto);
            return false;
        }
        if (n5>n6)
        {
            tvError2.setText(R.string.option_error_fromltto);
            return false;
        }
        return true;
    }
    /*
      Save Preferences
     */
    private Boolean SavePreferences(Boolean IsFinish)
    {
        m_clist.setContext(this);

        if (!Validate()){
            return false;
        }

        String strMin=etMinValue.getText().toString();
        String strMax=etMaxValue.getText().toString();
        String strMinMult=etMinValueMult.getText().toString();
        String strMaxMult=etMaxValueMult.getText().toString();
        String strMinDiv=etMinValueDiv.getText().toString();
        String strMaxDiv=etMaxValueDiv.getText().toString();

        Integer nMin=Integer.parseInt(strMin);
        Integer nMax=Integer.parseInt(strMax);
        Integer nMinMult=Integer.parseInt(strMinMult);
        Integer nMaxMult=Integer.parseInt(strMaxMult);
        Integer nMinDiv=Integer.parseInt(strMinDiv);
        Integer nMaxDiv=Integer.parseInt(strMaxDiv);

        selectedChild.setMinparam(nMin);
        selectedChild.setMaxparam(nMax);
        selectedChild.setMinparammult(nMinMult);
        selectedChild.setMaxparammult(nMaxMult);
        selectedChild.setMinparamdiv(nMinDiv);
        selectedChild.setMaxparamdiv(nMaxDiv);

        selectedChild.setAdd(cbAddOp.isChecked());
        selectedChild.setSub(cbSubOp.isChecked());
        selectedChild.setMult(cbMultOp.isChecked());
        selectedChild.setDiv(cbDivOp.isChecked());

        m_clist.SaveData();
        if (IsFinish){
            finish();
        }
        return true;
    }
}

