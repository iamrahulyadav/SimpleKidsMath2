package com.ozs.simplekidsmath2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ChildMaintActivity extends AppCompatActivity {

    private static final String STATE_NAME="STATE_NAME";
    private static final String STATE_MODE="STATE_MODE";
    private static final String STATE_UUID="STATE_UUID";

    boolean      m_isPermitted = true;

    String       m_Mode;
    String       m_ModeId;
    EditText     etName;
    ImageButton  ibPic;
    ChildrenList childList;
    AlertDialog  alert=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_maint);

        etName= (EditText) findViewById(R.id.editTextName);


        if (savedInstanceState!=null)
        {
            if (savedInstanceState.containsKey(STATE_NAME))
            {
                String txtTitle=savedInstanceState.getString(STATE_NAME);
                etName.setText(txtTitle);
            }
            if (savedInstanceState.containsKey(STATE_UUID))
            {
                m_ModeId=savedInstanceState.getString(STATE_UUID);

            }
            if (savedInstanceState.containsKey(STATE_MODE))
            {
                m_Mode=savedInstanceState.getString(STATE_MODE);
            }

        }
        else {
            UpdateUIFromPreferences();
            Intent myIntent=getIntent();
            Bundle bundle=myIntent.getExtras();
            m_Mode=bundle.getString(MainActivity.CHILD_MODE,"ADD");
            m_ModeId=bundle.getString(MainActivity.CHILD_MODE_ID,"");

            if (m_ModeId.trim().length()==0){
                // Create new Temporary guid
                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
                m_ModeId = randomUUIDString;
            }

        }



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {
            m_isPermitted=false;
            ActivityCompat.requestPermissions(ChildMaintActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    11);
            return;
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
                m_isPermitted = false;
                return;

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }

            return;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home) {
            SavePreferences(true);
            finish();
        }
        return true;
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
        outState.putString(STATE_NAME,etName.getText().toString());
        outState.putString(STATE_UUID,m_ModeId);
        outState.putString(STATE_UUID,m_Mode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(STATE_NAME))
        {
            String txtTitle=savedInstanceState.getString(STATE_NAME);
            etName.setText(txtTitle);
        }
        if (savedInstanceState.containsKey(STATE_UUID))
        {
            m_ModeId=savedInstanceState.getString(STATE_UUID);

        }
        if (savedInstanceState.containsKey(STATE_MODE))
        {
            m_Mode=savedInstanceState.getString(STATE_MODE);
        }
    }

    private void UpdateUIFromPreferences()
    {
        childList=ChildrenList.getInstance();
        childList.setContext(this);
    }

    private void SavePreferences(Boolean IsFinish)
    {
        SaveImage();
    }
    /** Start pick image activity with chooser. */
    public void onSelectImageClick(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Crop Child Image")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Done")
                .setRequestedSize(400, 400)
                .setCropMenuCropButtonIcon(R.mipmap.ic_launcher)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                Toast.makeText(
                        this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
                        .show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


    protected void SaveImage(){

        File rootPics = new File(getApplicationInfo().dataDir+File.separator+"pics");
        File rootDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!rootPics.exists())
        {
            rootPics.mkdirs();
        }


        ImageView  imageview = (ImageView) findViewById(R.id.quick_start_cropped_image);
        imageview.buildDrawingCache();
        Bitmap bm=imageview.getDrawingCache();
        OutputStream fOut = null;
        Uri outputFileUri;
        try {
            PackageManager m = getPackageManager();
            String s = getPackageName();
            File root = new File(getApplicationInfo().dataDir + File.separator + "pics" + File.separator);
            File sdImageMainDirectory = new File(root, m_ModeId+".jpg");
            outputFileUri = Uri.fromFile(sdImageMainDirectory);
            fOut = new FileOutputStream(sdImageMainDirectory);
        } catch (Exception e) {
            Toast.makeText(this, "Error occured. Please try again later.",
                    Toast.LENGTH_SHORT).show();
        }
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            // Copy to downloads dir
        } catch (Exception e) {
        }

        // For Debug, copy the file into Downloads Directory

        File root = new File(getApplicationInfo().dataDir + File.separator + "pics" + File.separator);
        File sdImageMainDirectory = new File(root, m_ModeId+".jpg");
        File dst = new File(rootDownload, m_ModeId+".jpg");
        if (sdImageMainDirectory.exists()) {
            // Copy the file
            try (InputStream in = new FileInputStream(sdImageMainDirectory)) {
                try (OutputStream out = new FileOutputStream(dst)) {
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }catch(Exception ex1){
                    Log.e("Pic2Download",ex1.getMessage());
                    ex1.printStackTrace();
                }
            }catch(Exception ex1){
                Log.e("Pic2Download",ex1.getMessage());
                ex1.printStackTrace();
            }
        }
    }
}