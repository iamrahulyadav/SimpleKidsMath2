package com.ozs.simplekidsmath2;

import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

public class OptionsActivity extends AppCompatActivity {

    ChildrenList m_clist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    @Override
    protected void onResume() {
        super.onResume();

        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed()
    {
        SavePreferences(true);
    }

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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    private void UpdateUIFromPreferences()
    {
        m_clist=ChildrenList.getInstance();
        m_clist.setContext(this);
    }

    private void SavePreferences(Boolean IsFinish){

    }

}
