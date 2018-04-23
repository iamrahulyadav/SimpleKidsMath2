package com.ozs.simplekidsmath2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final Integer MAX_CHILDEREN = 6;
    public static final Boolean TRACE_FLAG = false;
    public static final String CHILD_MODE = "CHILD_MODE";
    public static final String CHILD_MODE_VALUE_ADD = "ADD";
    public static final String CHILD_MODE_VALUE_CHG = "CHG";
    public static final String CHILD_MODE_ID = "CHILD_MODE_ID";

    protected ChildrenList m_clist;
    protected ChildrenListPresentor m_clistPresentor;
    protected Child m_selectedChild=null;

    public static final  int ADD_CHILD_REQUEST=1;
    public static final  int OPTION_REQUEST=2;

    public static final String FIRST_PARAM="firstp";
    public static final String SECOND_PARAM="secondp";
    public static final String OPERATOR_PARAM="+";

    ImageView ivgood;
    ImageView ivbad;
    TextView tv1;
    TextView tv2;
    TextView tvop;
    EditText etResult;
    Thread   myThread = null;
    Date     Startdt  = new Date();
    Long     waitedSec=0L;
    boolean  bmyThreadStop=false;
    long     utcOffset=0;
    int      animResource=R.anim.push_left_in;
    ImageView aniView;
    ImageView oppView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState!=null)
        {
            tv1.setText(savedInstanceState.getString(FIRST_PARAM,"0"));
            tv2.setText(savedInstanceState.getString(SECOND_PARAM,"0"));
            tvop.setText(savedInstanceState.getString(OPERATOR_PARAM,"+"));
        }

        /* ------------------------------------------ */
        /* Permissions                                */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
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
        /* ------------------------------------------ */
        Init();
    }

    public void Init(){
        m_clist=ChildrenList.getInstance();
        m_clist.setContext(this);
        m_clist.LoadData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FindViews();
        m_clistPresentor=new ChildrenListPresentor(this);
        CreateCList();

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

        ImageButton ibGen=(ImageButton) findViewById(R.id.imageButtonGen);
        ibGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitRound();
            }
        });

        EditText etResult= findViewById(R.id.editTextRes);


        etResult.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId & EditorInfo.IME_MASK_ACTION) != 0) {
                    CheckResults();
                    return true;
                }
                else {
                    return false;
                }
            }
        });


        etResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*
                if (!answerWaitFlag){
                    return;
                }
                if (inAnswerDelay) {
                    return;
                }

                final Handler handler = new Handler();
                inAnswerDelay=true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        CheckResults();
                    }
                }, 1000);
                */
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        InitRound();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode==KeyEvent.KEYCODE_ENTER)
        {
           CheckResults();
        }
        return true;
    }

    protected void CreateCList(){
        m_clist=ChildrenList.getInstance();
        m_clist.setContext(this);
        m_clist.LoadData();

    }

    protected void FindViews()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        ImageButton ib= hView.findViewById(R.id.imageButton);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenEditChild();
            }
        });

        tv1=findViewById(R.id.textView1stArg);
        tv2=findViewById(R.id.textView2ndArg);
        tvop=findViewById(R.id.textViewSign);
        etResult=findViewById(R.id.editTextRes);

        ivgood= findViewById(R.id.imageButtonGood);
        ivbad = findViewById(R.id.imageButtonBad);
        ivgood.setVisibility(View.GONE);
        ivbad.setVisibility(View.GONE);
        tvop.setText("+");

    }


    public void InitRound()
    {
        ivgood.setVisibility(View.GONE);
        ivbad.setVisibility(View.GONE);

        m_clist=ChildrenList.getInstance();
        m_clist.setContext(this);
        m_selectedChild=null;
        m_selectedChild=m_clist.getSelectedChild();
        int nOp=InitRoundHelperAction();


        m_clist.setContext(this);
        Child child = m_clist.getSelectedChild();
        if (child==null){
            child = new Child("dummy");
        }


        Random r = new Random(new Date().getTime());

        Integer n1start=m_selectedChild.getMinparam();
        Integer n1end=m_selectedChild.getMaxparam();
        int norm=n1end-n1start;
        norm=Math.abs(norm);
        // Normalize the result

        Integer n1=r.nextInt(norm);
        Integer n2=r.nextInt(norm);
        // Move the result back to range
        n1=n1start+n1;
        n2=n1start+n2;

        String strOp="";
        switch(nOp){
            case 1:
                strOp="+";
                break;
            case 2:
                strOp="-";
                break;
            case 3:
                strOp="*";
                break;
            case 4:
                strOp="/";
                break;
            default:
                strOp="+";
        }

        // Case of subtracion
        if (nOp==2)
        {
            tvop.setText("-");
            if (!child.getAllowMinusResult()) {
                if (n2 > n1) {
                    int nt = n1;
                    n1 = n2;
                    n2 = nt;
                }
            }
        }
        // case of /
        if (nOp==4) {

            if (n2 > n1) {
                int nt = n1;
                n1 = n2;
                n2 = nt;
            }
            n2=(n2==0)?1:n2;
            int res = n1 / n2;
            if (n1 % n2 !=0) {
               n1=Math.abs(n1);
               n2=Math.abs(n2);
                if (n2 > n1) {
                    int nt = n1;
                    n1 = n2;
                    n2 = nt;
                }
                n1=n1-(n1 % n2);
            }

        }

        tvop.setText(strOp);
        tv1.setText(n1.toString());
        tv2.setText(n2.toString());
        etResult.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etResult, InputMethodManager.SHOW_IMPLICIT);


        //calac UTC Offset

        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        utcOffset = tz.getOffset(now.getTime());

        // Start Thread
        Startdt  = new Date();
        waitedSec=0L;
        bmyThreadStop=false;
        Runnable myRunnableThread = new CountDownRunner();
        myThread= new Thread(myRunnableThread);
        myThread.start();
    }

    protected int InitRoundHelperAction(){
        int[]  arOp={0,0,0,0};
        arOp[0]=(m_selectedChild.getAdd())?1:0;
        arOp[1]=(m_selectedChild.getSub())?1:0;
        arOp[2]=(m_selectedChild.getMult())?1:0;
        arOp[3]=(m_selectedChild.getDiv())?1:0;

        int num=0;
        // How Many "1"
        for(int i=0;i<4;i++){
            if (arOp[i]==1){
                num++;
            }
        }
        if (num==0) {
            return 0;
        }
        if (num==1){
            for(int i=0;i<4;i++){
                if (arOp[i]==1){
                    return (i+1);
                }
            }
        }
        Random r = new Random(new Date().getTime());
        Integer n1=r.nextInt(num);
        int num2 = -1;
        for(int i=0;i<4;i++)
        {
            if (arOp[i]==1){
                num2++;
            }
            if (num2>=n1){
                return (i+1);
            }
        }
        return 0;

    }

    protected void CheckResults(){


        if (myThread!=null)
        {
            bmyThreadStop=true;
        }
        int n1=Integer.parseInt(tv1.getText().toString());
        int n2=Integer.parseInt(tv2.getText().toString());

        try {
            int n3 = Integer.parseInt(etResult.getText().toString());
            View view = MainActivity.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            if (tvop.getText().toString()=="+")
            {

                if (n1 + n2 == n3) {
                        GoodSign();
                }
                else
                {
                        BadSign();
                }
            }
            else if (tvop.getText().toString()=="-") {
                if (n1 - n2 == n3) {
                        GoodSign();
                }
                else
                {
                        BadSign();
                }
            }else if (tvop.getText().toString()=="*") {
                if (n1 * n2 == n3) {
                        GoodSign();
                }
                else
                {
                        BadSign();
                }
            }else {
                if (n1 / n2 == n3) {
                        GoodSign();
                }
                else
                {
                        BadSign();
                }
            }
        }
        catch(Exception ex1)
        {
        }
        finally {
            //load an animation from XML
            Animation animation1 = AnimationUtils.loadAnimation(this,animResource);
            animation1.setDuration(1500);

         animation1.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                // Bail out to next screen
                // --> Move to next questio NextPrevQ("Next");
                ivgood.setVisibility(View.GONE);
                ivbad.setVisibility(View.GONE);
                InitRound();
            }
        });

            aniView.startAnimation(animation1);
            animation1.start();
            /*
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    ivgood.setVisibility(View.GONE);
                    ivbad.setVisibility(View.GONE);
                    InitRound();
                }
            }, 2000);
            */
        }
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    TextView txtCurrentTime = findViewById(R.id.textseconds);
                    Date dt = new Date();
                    Long diff1=(dt.getTime() - Startdt.getTime());
                    Long diff2= diff1 / 1000;
                    waitedSec=diff2;
                    txtCurrentTime.setText(diff2.toString());
                }catch (Exception e) {}
            }
        });
    }
    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while((!Thread.currentThread().isInterrupted())&&(!bmyThreadStop)){
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }
    // Display Good result Sign
    protected void GoodSign(){
        // Display animation

        startAnimation(true);
    }

    // Display Bad Result Sign
    protected void BadSign(){

        startAnimation(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )) {

            // Select Current Child
            Child child = m_clist.getSelectedChild();
            if (child != null) {
                SwitchChild(child);
            }
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
        CreateCList();

        if(m_clist.GetItemsSize()<=0)
        {
            //Bail out ->NO Items
            return;
        }
        Integer startCustomMenu=m_clist.GetMinMenuId();
        Integer endCustomMenu=m_clist.GetMaxMenuId();

        // Remove OLD Menus:
        for(int i=122;i<=122+MAX_CHILDEREN-1;i++) {
            menu.removeItem(i);
        }
        // Add Childrens (up to 5)
        int j=startCustomMenu;

        Child[] children=m_clist.getChildrenArray();
        if (children!=null)
        {
            for(int i=0;((i<children.length)&&(i<MAX_CHILDEREN));i++){
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
                Init();
                // Select Current Child
                Child child = m_clist.getSelectedChild();
                if (child != null) {
                    SwitchChild(child);
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Missing Permissions")
                        .setMessage("Application Can not work without the requested permissions, Quit ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CAMERA,
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        },
                                        11);

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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
        // getMenuInflater().inflate(R.menu.main, menu);
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
    /*
    Open Edit Child Activity
     */
    protected void OpenEditChild(){
        Child child=m_clist.getSelectedChild();
        Intent foo=new Intent(MainActivity.this,ChildMaintActivity.class);
        foo.putExtra(CHILD_MODE,CHILD_MODE_VALUE_CHG);
        foo.putExtra(CHILD_MODE_ID,child.getChildId());
        startActivityForResult(foo, ADD_CHILD_REQUEST);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == 121) {
            if (m_clist.GetItemsSize()<=MAX_CHILDEREN) {
                Intent foo = new Intent(MainActivity.this, ChildMaintActivity.class);
                foo.putExtra(CHILD_MODE, CHILD_MODE_VALUE_ADD);
                foo.putExtra(CHILD_MODE_ID, 0);
                startActivityForResult(foo, ADD_CHILD_REQUEST);
            }
            else
            {
                Toast.makeText(this,getString(R.string.lbl_no_children_exeeded),Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.nav_manage) {
            Intent foo=new Intent(MainActivity.this,OptionsActivity.class);
            startActivityForResult(foo, OPTION_REQUEST);


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
        TextView nav_ver  = hView.findViewById(R.id.ver);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            nav_ver.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        nav_user.setText(child.getName());
        ImageButton ib= hView.findViewById(R.id.imageButton);
        ImageView ibMain= findViewById(R.id.imageViewMain);

        if (ib==null) {
            return;
        }
        m_clistPresentor.AssignImageToImageButton(ib,child.getImgName());
        m_clistPresentor.AssignImageToImageView(ibMain,child.getImgName());

        CreateCList();
        m_clist.setSelectedChild(child);
        // Leave the drawer open
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == ADD_CHILD_REQUEST) {
           CreateCList();
           SetCustomMenu();
           // Select Current Child
           Child child=m_clist.getSelectedChild();
           if (child!=null){
               SwitchChild(child);
           }
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

    public void startAnimation(boolean isGoodAnswer)
    {

        if (isGoodAnswer)
        {
            aniView=findViewById(R.id.imageButtonGood);
            oppView=findViewById(R.id.imageButtonBad);
        }
        else
        {
            aniView=findViewById(R.id.imageButtonBad);
            oppView=findViewById(R.id.imageButtonGood);
        }
        oppView.setVisibility(View.INVISIBLE);
        oppView.setVisibility(View.GONE);

        animResource=R.anim.push_left_in;
        Random rndGen=new Random();
        int nRandInt=rndGen.nextInt(63);
        nRandInt = nRandInt % 7;
        switch(nRandInt)
        {
            case 0:
                animResource=R.anim.fadein;
                break;
            case 1:
                animResource=R.anim.fadeout;
                break;
            case 2:
                animResource=R.anim.push_left_in;
                break;
            case 3:
                animResource=R.anim.rotation;
                break;
            case 4:
                animResource=R.anim.push_right_out;
                break;
            case 5:
                animResource=R.anim.push_right_in;
                break;
            case 6:
                animResource=R.anim.push_left_out;
                break;
            default:
                animResource=R.anim.push_left_in;
                break;
        }

        //load an animation from XML
        //Animation animation1 = AnimationUtils.loadAnimation(this,animResource);
        //animation1.setAnimationListener(this);
        //animation1.setDuration(2000);

        /* animation1.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                // Bail out to next screen
                // --> Move to next questio NextPrevQ("Next");
                ivgood.setVisibility(View.GONE);
                ivbad.setVisibility(View.GONE);
            }
        });
        */
        aniView.setVisibility(View.VISIBLE);
        //aniView.startAnimation(animation1);
        //animation1.start();


    }
}
