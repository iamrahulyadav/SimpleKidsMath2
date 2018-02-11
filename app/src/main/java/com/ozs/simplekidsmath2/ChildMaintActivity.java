package com.ozs.simplekidsmath2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ChildMaintActivity extends AppCompatActivity {

    private static final String STATE_NAME="STATE_NAME";

    public final  String PREF_PHOTO_PATH="pref_photo_path";
    public final int     IMAGE_MAX_SIZE = 1200000; // 1.2MP

    public final  int  CHILD_IMAGE_WIDTH=400;
    public final  int  CHILD_IMAGE_HEIGHT=400;

    public final  int  CHOOSE_FROM_GALLERY=0;
    public final  int  CHOOSE_FROM_CAMERA=1;

    boolean      m_isPermitted = true;
    ImageView    m_ivLoadPic;
    String[]     m_aritems = { "Gallery","Camera"};
    String       m_Mode;
    String       m_ModeId;
    EditText     etName;
    ImageButton  ibPic;
    ChildrenList childList;
    AlertDialog  alert=null;
    String       m_CurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_maint);

        etName= (EditText) findViewById(R.id.editTextName);
        ibPic = (ImageButton) findViewById(R.id.imageButtonPic);

        if (savedInstanceState!=null)
        {
            if (savedInstanceState.containsKey(STATE_NAME))
            {
                String txtTitle=savedInstanceState.getString(STATE_NAME);
                etName.setText(txtTitle);
            }
            else
            {
                UpdateUIFromPreferences();
            }
        }
        else {
            UpdateUIFromPreferences();
        }

        Intent myIntent=getIntent();
        Bundle bundle=myIntent.getExtras();
        String m_Mode=bundle.getString(MainActivity.CHILD_MODE,"ADD");
        String m_ModeId=bundle.getString(MainActivity.CHILD_MODE_ID,"");

        if (m_ModeId.trim().length()==0){
            // Create new Temporary guid
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            m_ModeId = randomUUIDString;
        }

        ibPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_isPermitted) {
                    CameraClick(v);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChildMaintActivity.this);
                    // Add the buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            dialog.dismiss();
                        }
                    });
                    // Set other dialog properties
                    // Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


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



    public void CameraClick(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChildMaintActivity.this);
        builder.setTitle("Choose Source");
        builder.setItems(m_aritems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedItem = m_aritems[item].toString();

                if (selectedItem.compareTo("Gallery")==0)
                {
                    Intent intent_gallery = new Intent();
                    intent_gallery.setType("image/*");
                    intent_gallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent_gallery, CHOOSE_FROM_GALLERY);
                }
                else if (selectedItem.compareTo("Camera")==0)
                {
                    dispatchTakePictureIntent();
                }
            }
        });
        alert=builder.create();
        alert.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // mContactAccessor = new ContactAccessorSdk5();
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
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(STATE_NAME))
        {
            String txtTitle=savedInstanceState.getString(STATE_NAME);
            etName.setText(txtTitle);
        }
    }

    private void UpdateUIFromPreferences()
    {
        childList=ChildrenList.getInstance();
        childList.setContext(this);
    }

    private void SavePreferences(Boolean IsFinish)
    {
    }

    /*
     dispatchTakePictureIntent
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.ozs.simplekidsmath2.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CHOOSE_FROM_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        m_CurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {

        switch(requestCode) {
            case CHOOSE_FROM_GALLERY:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try
                    {
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);

                        Bitmap yourSelectedImage= BitmapFactory.decodeStream(imageStream);
                        m_ivLoadPic.setImageBitmap(Bitmap.createScaledBitmap(yourSelectedImage, CHILD_IMAGE_WIDTH,CHILD_IMAGE_HEIGHT, false));
                        imageStream.close();

                        InputStream imageStream2=getContentResolver().openInputStream(selectedImage);
                        WriteImage(imageStream2);

                    }
                    catch(FileNotFoundException e)
                    {
                        // File Was not found
                        e.printStackTrace();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                }
                break;
            case CHOOSE_FROM_CAMERA:

                if ((resultCode == RESULT_OK)&&(imageReturnedIntent != null)) {
                    if (imageReturnedIntent.hasExtra("data")) {
                        Bundle extras = imageReturnedIntent.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        m_ivLoadPic.setImageBitmap(imageBitmap);
                    }
                }


                if (imageReturnedIntent != null) {

                    if (imageReturnedIntent.hasExtra("data")) {

                    }
                } else {

                    setPic();

                }
                break;
        }
    }

    /*
     WriteImage
     */
    private void WriteImage(InputStream imageStream)
    {
        try {
            DeleteOldPic(m_ModeId);
            FileOutputStream out = openFileOutput("C1.png", Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024]; // Adjust if you want
            int bytesRead;
            while ((bytesRead = imageStream.read(buffer)) != -1)
            {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            // WritePref();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DeleteOldPic(String uuid)
    {
        // RemovePref();
        File file = new File(uuid+".png");
        if (file.exists()) {
            file.delete();
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = m_ivLoadPic.getWidth();
        int targetH = m_ivLoadPic.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        try {
            InputStream in = getContentResolver().openInputStream(
                    Uri.parse(m_CurrentPhotoPath));
            BitmapFactory.decodeStream(in, null, bmOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // BitmapFactory.decodeFile(m_CurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap=null;// = BitmapFactory.decodeFile(m_CurrentPhotoPath, bmOptions);
        try {
            InputStream in2 = getContentResolver().openInputStream(
                    Uri.parse(m_CurrentPhotoPath));
            bitmap=BitmapFactory.decodeStream(in2, null, bmOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        m_ivLoadPic.setImageBitmap(bitmap);
        DeleteOldPic(m_ModeId);
        try {
            FileOutputStream out = openFileOutput(m_ModeId+".png", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            WritePref();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void WritePref()
    {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(ChildMaintActivity.this);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(PREF_PHOTO_PATH,"C1.png");
        editor.commit();
    }
    /*
      RemovePref
     */
    private void RemovePref()
    {
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(ChildMaintActivity.this);
        SharedPreferences.Editor editor=sp.edit();
        editor.remove(PREF_PHOTO_PATH);
        editor.commit();
    }

}
