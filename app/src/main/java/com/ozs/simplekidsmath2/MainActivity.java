package com.ozs.simplekidsmath2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    public static final Integer MAX_Q_TO_INFO = 10;
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
    boolean   inAnimation=false;
    Integer   TryAgainCounter=0;
    // Last Ex
    String   lastParam1,lastParam2,lastOp;



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
                    if (! inAnimation) {
                        CheckResults();
                    }
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

        CheckBounderies();
        InitRound();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode==KeyEvent.KEYCODE_ENTER)
        {
           if (! inAnimation) {
               CheckResults();
           }
           return true;
        }
        return super.onKeyDown(keyCode, event);
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

    /*
    Init Round
     */
    public void InitRound()
    {

        TryAgainCounter=0;
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


        Random r1 = new Random(new Date().getTime());

        Integer n1start;
        Integer n1end;


        switch(nOp){
            case 1:
                n1start=m_selectedChild.getMinparam();
                n1end=m_selectedChild.getMaxparam();
                break;
            case 2:
                n1start=m_selectedChild.getMinparam();
                n1end=m_selectedChild.getMaxparam();
                break;
            case 3:
                n1start=m_selectedChild.getMinparammult();
                n1end=m_selectedChild.getMaxparammult();
                break;
            case 4:
                n1start=m_selectedChild.getMinparamdiv();
                n1end=m_selectedChild.getMaxparamdiv();
                break;
            default:
                n1start=m_selectedChild.getMinparam();
                n1end=m_selectedChild.getMaxparam();
        }

        int norm1=n1end-n1start;
        norm1=Math.abs(norm1);
        // Normalize the result

        Integer n1=r1.nextInt(norm1);
        Integer n2=r1.nextInt(norm1);
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
                strOp="X";
                break;
            case 4:
                strOp="\u00F7";
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

        lastParam1=n1.toString();
        lastParam2=n2.toString();
        lastOp=strOp;
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

    /*
    Init Round After Retry
     */
    public void InitRoundRetry() {


        tvop.setText(lastOp);
        tv1.setText(lastParam1);
        tv2.setText(lastParam2);
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
    /*

     */
    protected void CheckResults(){

        TryAgainCounter=0;

        if (myThread!=null)
        {
            bmyThreadStop=true;
        }
        inAnimation=true;
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
                if (m_selectedChild!=null){
                    m_selectedChild.setAddNo(m_selectedChild.getAddNo()+1);
                    m_selectedChild.setAddNo2(m_selectedChild.getAddNo2()+1);
                }

                if (n1 + n2 == n3) {
                    if (m_selectedChild!=null){
                        m_selectedChild.setAddG(m_selectedChild.getAddG()+1 );
                        m_selectedChild.setAddG2(m_selectedChild.getAddG2()+1);
                        m_selectedChild.setAddTime(m_selectedChild.getAddTime() + waitedSec);
                        m_selectedChild.setAddTime2(m_selectedChild.getAddTime2() + waitedSec);
                    }
                    GoodSign();
                }
                else
                {
                        BadSign();
                }
            }
            else if (tvop.getText().toString()=="-") {

                if (m_selectedChild!=null){
                    m_selectedChild.setSubNo(m_selectedChild.getSubNo()+1);
                    m_selectedChild.setSubNo2(m_selectedChild.getSubNo2()+1);
                }

                if (n1 - n2 == n3) {
                    if (m_selectedChild!=null){
                        m_selectedChild.setSubG(m_selectedChild.getSubG()+1);
                        m_selectedChild.setSubG2(m_selectedChild.getSubG2()+1);
                        m_selectedChild.setSubTime(m_selectedChild.getSubTime() + waitedSec);
                        m_selectedChild.setSubTime2(m_selectedChild.getSubTime2() + waitedSec);

                    }
                        GoodSign();
                }
                else
                {
                        BadSign();
                }
            }else if (tvop.getText().toString()=="X") {
                if (m_selectedChild!=null){
                    m_selectedChild.setMultNo(m_selectedChild.getMultNo()+1);
                    m_selectedChild.setMultNo2(m_selectedChild.getMultNo2()+1);
                }

                if (n1 * n2 == n3) {
                    if (m_selectedChild!=null){
                        m_selectedChild.setMultG(m_selectedChild.getMultG()+1);
                        m_selectedChild.setMultG2(m_selectedChild.getMultG2()+1);
                        m_selectedChild.setMultTime(m_selectedChild.getMultTime() + waitedSec);
                        m_selectedChild.setMultTime2(m_selectedChild.getMultTime2() + waitedSec);

                    }
                    GoodSign();
                }
                else
                {
                        BadSign();
                }
            }else {
                if (m_selectedChild!=null){
                    m_selectedChild.setDivNo(m_selectedChild.getDivNo()+1);
                    m_selectedChild.setDivNo2(m_selectedChild.getDivNo2()+1);
                }

                if (n1 / n2 == n3) {
                    if (m_selectedChild!=null){
                        m_selectedChild.setDivG(m_selectedChild.getDivG()+1);
                        m_selectedChild.setDivG2(m_selectedChild.getDivG2()+1);
                        m_selectedChild.setDivTime(m_selectedChild.getDivTime() + waitedSec);
                        m_selectedChild.setDivTime2(m_selectedChild.getDivTime2() + waitedSec);
                    }
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
            ChildrenList.getInstance().SaveData();
            //load an animation from XML
            final Animation animation1 = AnimationUtils.loadAnimation(this, animResource);
            animation1.setDuration(1500);


            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) {
                    inAnimation = true;
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
                    inAnimation = false;

                    /*****************
                     * Start Try again Dialog
                     */

                    if (TryAgainCounter==0){

                            // Child Info Dialog
                            boolean fInitRound=true;

                            if (m_selectedChild!=null) {

                                int total=m_selectedChild.getAddNo()+
                                          m_selectedChild.getSubNo()+
                                          m_selectedChild.getMultNo()+
                                          m_selectedChild.getDivNo();

                                if (total>=MAX_Q_TO_INFO) {
                                    fInitRound=false;
                                    final ChildInfoDialog cid = new ChildInfoDialog(MainActivity.this,
                                            AssignChildInfo());

                                    cid.doOK = new ChildInfoDialog.OnOKChildInfoListener() {
                                        @Override
                                        public void OnChildInfo() {
                                            ResetChildCounters();
                                            InitRound();
                                            cid.dismiss();
                                        }
                                        @Override
                                        public void OnParentResetInfo() {
                                            ResetChildCounters();
                                            InitRound();
                                            cid.dismiss();
                                        }
                                    };
                                    cid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    cid.show();
                                }
                            }
                        if (fInitRound) {
                            InitRound();
                        }
                    }
                    if (TryAgainCounter==1) {
                        final TryAgainDialog tad = new TryAgainDialog(MainActivity.this);
                        tad.doTryAgain = new TryAgainDialog.OnTryAgainListener() {
                            @Override
                            public void OnTryAgain(boolean isTryAgain) {
                                if (isTryAgain) {
                                    InitRoundRetry();

                                } else {
                                    InitRound();
                                    tad.dismiss();
                                }
                            }
                        };
                        tad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TryAgainCounter=0;
                        tad.show();
                    }
                    /*****************
                     * End Try again Dialog
                     */
                     //animation1.cancel();
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
        ChildrenList.getInstance().SaveData();
        startAnimation(true);
    }

    // Display Bad Result Sign
    protected void BadSign(){
        ChildrenList.getInstance().SaveData();
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
            ivgood.setVisibility(View.GONE);
            ivbad.setVisibility(View.GONE);

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

        } else if (id == R.id.nav_parent) {
                //--------------------------------
                // Show Parent Dialog
                //--------------------------------
                ShowParentDialog();

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
            TryAgainCounter=0;
            aniView=ivgood;
            oppView=ivbad;
        }
        else
        {
            TryAgainCounter=1;
            aniView=ivbad;
            oppView=ivgood;
        }
        oppView.setVisibility(View.GONE);

        animResource=R.anim.push_left_in;
        Random rndGen=new Random();
        int nRandInt=rndGen.nextInt(90);
        nRandInt = nRandInt % 3;
        switch(nRandInt)
        {
            case 0:
                animResource=R.anim.push_left_out;
                break;
            case 1:
                animResource=R.anim.push_right_out;
                break;
            case 2:
                animResource=R.anim.push_left_out;
                break;
            default:
                animResource=R.anim.push_right_out;
                break;
        }
        aniView.setVisibility(View.VISIBLE);
    }

    protected ChildInfo AssignChildInfo(){
        if (m_selectedChild==null) {
            return null;
        }
        ChildInfo ret=new ChildInfo();

        ret.setAddGood(m_selectedChild.getAddG());
        ret.setAddTotal(m_selectedChild.getAddNo());
        ret.setSubGood(m_selectedChild.getSubG());
        ret.setSubTotal(m_selectedChild.getSubNo());
        ret.setMultGood(m_selectedChild.getMultG());
        ret.setMultTotal(m_selectedChild.getMultNo());
        ret.setDivGood(m_selectedChild.getDivG());
        ret.setDivTotal(m_selectedChild.getDivNo());
        return ret;
    }

    protected ChildInfo AssignParentInfo(){
        if (m_selectedChild==null) {
            return null;
        }
        ChildInfo ret=new ChildInfo();

        ret.setAddGood(m_selectedChild.getAddG2());
        ret.setAddTotal(m_selectedChild.getAddNo2());
        ret.setSubGood(m_selectedChild.getSubG2());
        ret.setSubTotal(m_selectedChild.getSubNo2());
        ret.setMultGood(m_selectedChild.getMultG2());
        ret.setMultTotal(m_selectedChild.getMultNo2());
        ret.setDivGood(m_selectedChild.getDivG2());
        ret.setDivTotal(m_selectedChild.getDivNo2());
        ret.setLastScoreReset(m_selectedChild.getLastScoreReset());
        return ret;
    }

    protected void ResetChildCounters(){
        if (m_selectedChild==null) {
            return;
        }

        m_selectedChild.setAddG(0);
        m_selectedChild.setAddTime(0L);
        m_selectedChild.setAddNo(0);

        m_selectedChild.setSubG(0);
        m_selectedChild.setSubTime(0L);
        m_selectedChild.setSubNo(0);

        m_selectedChild.setMultG(0);
        m_selectedChild.setMultTime(0L);
        m_selectedChild.setMultNo(0);

        m_selectedChild.setDivG(0);
        m_selectedChild.setDivTime(0L);
        m_selectedChild.setDivNo(0);

        ChildrenList.getInstance().SaveData();
    }

    protected void ResetParentCounters(){
        if (m_selectedChild==null) {
            return;
        }
        ChildInfo ret=new ChildInfo();

        m_selectedChild.setAddG2(0);
        m_selectedChild.setAddTime2(0L);
        m_selectedChild.setAddNo2(0);

        m_selectedChild.setSubG2(0);
        m_selectedChild.setSubTime2(0L);
        m_selectedChild.setSubNo2(0);

        m_selectedChild.setMultG2(0);
        m_selectedChild.setMultTime2(0L);
        m_selectedChild.setMultNo2(0);

        m_selectedChild.setDivG2(0);
        m_selectedChild.setDivTime2(0L);
        m_selectedChild.setDivNo2(0);
        m_selectedChild.setLastScoreReset(new Date().getTime());

        ChildrenList.getInstance().SaveData();
    }
    protected void CheckBounderies() {
        if (m_selectedChild==null)
        {
            return;
        }
        if ((m_selectedChild.getAddNo2()>Integer.MAX_VALUE - 50)||
            (m_selectedChild.getSubNo2()>Integer.MAX_VALUE - 50)||
            (m_selectedChild.getMultNo2()>Integer.MAX_VALUE - 50)||
            (m_selectedChild.getDivNo2()>Integer.MAX_VALUE - 50)) {
            ResetParentCounters();
        }
        Long range=24L * 60L * 60L * 1000L;

        if ((m_selectedChild.getAddTime2()>Long.MAX_VALUE - range)||
                (m_selectedChild.getSubTime2()>Long.MAX_VALUE - range)||
                (m_selectedChild.getMultTime2()>Long.MAX_VALUE - range)||
                (m_selectedChild.getDivTime2()>Integer.MAX_VALUE - range)) {
            ResetParentCounters();
        }
    }
    protected void ShowParentDialog(){

        final ChildInfoDialog cid = new ChildInfoDialog(MainActivity.this, AssignParentInfo());
        cid.setResetVisible();

        cid.doOK = new ChildInfoDialog.OnOKChildInfoListener() {
            @Override
            public void OnChildInfo() {
                cid.dismiss();
            }
            @Override
            public void OnParentResetInfo() {
                ResetChildCounters();
                ResetParentCounters();
                InitRound();
                cid.dismiss();
            }
        };
        cid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cid.show();
    }
}
