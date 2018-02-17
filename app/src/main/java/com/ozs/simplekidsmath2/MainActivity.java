package com.ozs.simplekidsmath2;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Boolean TRACE_FLAG = true;
    public static String CHILD_MODE = "CHILD_MODE";
    public static String CHILD_MODE_VALUE_ADD = "ADD";
    public static String CHILD_MODE_VALUE_CHG = "CHG";
    public static String CHILD_MODE_ID = "CHILD_MODE_ID";

    protected ChildrenList m_clist;

    public static final  int ADD_CHILD_REQUEST=1;
    public static final  int OPTION_REQUEST=2;


    public static final int MAX_NUM=20;
    public static final String FIRST_PARAM="firstp";
    public static final String SECOND_PARAM="secondp";
    public static final String OPERATOR_PARAM="+";


    ImageView iv;
    TextView tv1;
    TextView tv2;
    TextView tvop;
    EditText etResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        m_clist=ChildrenList.getInstance();
        m_clist.setContext(this);
        m_clist.LoadData();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        menu.add(R.id.referrer_group, 121, Menu.NONE, "Add Child");
        SetCustomMenu();

       /*
        menu.add(R.id.referrer_group, 124, Menu.NONE, "Child 2");
        menu.add(R.id.referrer_group, 125, Menu.NONE, "Child 3");
        */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    11);
            return;
        }


        FindViews();

        if (savedInstanceState!=null)
        {
            tv1.setText(savedInstanceState.getString(FIRST_PARAM,"0"));
            tv2.setText(savedInstanceState.getString(SECOND_PARAM,"0"));
            tvop.setText(savedInstanceState.getString(OPERATOR_PARAM,"+"));
        }

        ImageButton ibGen=(ImageButton) findViewById(R.id.imageButtonGen);
        ibGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitRound();
            }
        });


        EditText etResult=(EditText) findViewById(R.id.editTextRes);
        etResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int n1=Integer.parseInt(tv1.getText().toString());
                int n2=Integer.parseInt(tv2.getText().toString());
                try {
                    int n3 = Integer.parseInt(charSequence.toString());
                    if (tvop.getText().toString()=="+")
                    {
                        if (n1 + n2 == n3) {

                            View view = MainActivity.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            ImageView iv = (ImageView) findViewById(R.id.imageButtonGood);
                            iv.setVisibility(View.VISIBLE);
                        }

                    }
                    else
                    {
                        if (n1 - n2 == n3) {
                            View view = MainActivity.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            ImageView iv = (ImageView) findViewById(R.id.imageButtonGood);
                            iv.setVisibility(View.VISIBLE);
                        }
                    }

                }
                catch(Exception ex1)
                {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        InitRound();
    }


    protected void FindViews()
    {
        tv1=findViewById(R.id.textView1stArg);
        tv2=findViewById(R.id.textView2ndArg);
        tvop=findViewById(R.id.textViewSign);
        etResult=findViewById(R.id.editTextRes);

        iv=(ImageView) findViewById(R.id.imageButtonGood);
        iv.setVisibility(View.INVISIBLE);
        tvop.setText("+");

    }


    public void InitRound()
    {
        iv.setVisibility(View.INVISIBLE);
        Random r = new Random(new Date().getTime());

        Integer n1=r.nextInt(10);;
        Integer n2=r.nextInt(10);;

        if (new Date().getTime() % 2 ==0 )
        {
            tvop.setText("+");
        }
        else
        {
            tvop.setText("-");
            if (n2 > n1)
            {
                int nt=n1;
                n1=n2;
                n2=nt;
            }
        }

        tv1.setText(n1.toString());
        tv2.setText(n2.toString());
        etResult.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etResult, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void onResume() {
        super.onResume();


        // Select Current Child
        Child child=m_clist.getSelectedChild();
        if (child!=null){
            SwitchChild(child);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    protected void SetCustomMenu(){
        // Create Children List


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        m_clist=ChildrenList.getInstance();
        m_clist.setContext(this);

        if(m_clist.GetItemsSize()<=0)
        {
            //Bail out ->NO Items
            return;
        }
        Integer startCustomMenu=m_clist.GetMinMenuId();
        Integer endCustomMenu=m_clist.GetMaxMenuId();

        // Remove OLD Menus:
        for(int i=startCustomMenu;i<=endCustomMenu;i++) {
            menu.removeItem(i);
        }
        // Add Childrens (up to 5)
        int j=startCustomMenu;

        Child[] children=m_clist.getChildrenArray();
        if (children!=null)
        {
            for(int i=0;((i<children.length)&&(i<4));i++){
                j++;
                menu.add(R.id.referrer_group, children[i].getMenuId(), Menu.NONE, children[i].getName());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==11)
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    ){
                return;

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }

            return;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == 121) {
            Intent foo=new Intent(MainActivity.this,ChildMaintActivity.class);
            foo.putExtra(CHILD_MODE,CHILD_MODE_VALUE_ADD);
            foo.putExtra(CHILD_MODE_ID,0);
            startActivityForResult(foo, ADD_CHILD_REQUEST);

        } else if (id == R.id.nav_manage) {
            Intent foo=new Intent(MainActivity.this,OptionsActivity.class);
            startActivityForResult(foo, OPTION_REQUEST);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else {
            Integer startCustomMenu=m_clist.GetMinMenuId();
            Integer endCustomMenu=m_clist.GetMaxMenuId();

            Child child=m_clist.GetChildByMenuId(id);
            if (child!=null)
            {
                // Switch Child
                SwitchChild(child);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void SwitchChild(Child child) {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.userName);
        nav_user.setText(child.getName());
        ImageView iv= hView.findViewById(R.id.imageView);

        if (iv==null) {
            return;
        }
        File root = new File(getApplicationInfo().dataDir + File.separator + "pics" + File.separator);
        File sdImageMainDirectory = new File(root + File.separator+child.getImgName());

        if (sdImageMainDirectory.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(sdImageMainDirectory.getAbsolutePath());
            if (myBitmap!=null) {
                iv.setImageBitmap(myBitmap);
            }
        }
        m_clist = ChildrenList.getInstance();
        m_clist.setContext(this);
        m_clist.setSelectedChild(child);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == ADD_CHILD_REQUEST) {
           SetCustomMenu();
       }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString(FIRST_PARAM,tv1.getText().toString());
        outState.putString(SECOND_PARAM,tv1.getText().toString());
        outState.putString(OPERATOR_PARAM,"+");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        tv1.setText(savedInstanceState.getString(FIRST_PARAM,"0"));
        tv2.setText(savedInstanceState.getString(SECOND_PARAM,"0"));
        tvop.setText(savedInstanceState.getString(OPERATOR_PARAM,"+"));

        super.onRestoreInstanceState(savedInstanceState);
    }
}
