package com.ozs.simplekidsmath2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by z_savion on 18/02/2018.
 */
public class ChildrenListPresentor {

    public static final String APP_PICS_DIR="pics";
    private Context m_context;

    public ChildrenListPresentor(Context _context){
        this.m_context=_context;
    }

    public void setContext(Context context)
    {
        m_context = context;
    }
    /*
      Get Pics Directory Path
     */
    public File getPicsDirectory(){
        File rootPics = new File(m_context.getApplicationInfo().dataDir + File.separator + APP_PICS_DIR + File.separator);
        return rootPics;
    }
    /*
      Get Pics Directory Image Path
     */
    public File getPicsDirectory(String picName){
        File rootPics=getPicsDirectory();
        File sdImagePath = new File(rootPics + File.separator+picName);
        return sdImagePath;
    }
    /*
     Assign Pic to Image View
     */
    public void  AssignImageToImageView(ImageView iv,String imageName) {

        File sdImagePath = getPicsDirectory(imageName);
            if (iv != null) {
                if (sdImagePath.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(sdImagePath.getAbsolutePath());
                    if (myBitmap != null) {
                        iv.setImageBitmap(myBitmap);
                    }
                }
            }
    }

    public void  AssignImageToImageButton(ImageButton iv, String imageName) {
        File sdImagePath = getPicsDirectory(imageName);
        if (iv != null) {
            if (sdImagePath.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(sdImagePath.getAbsolutePath());
                if (myBitmap != null) {
                    iv.setImageBitmap(myBitmap);
                }
            }
        }
    }
    public void SaveImage(ImageView iv,String uuId) {
        // Create Pics Directory

        File rootPics = getPicsDirectory();
        File rootDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // Create Pics Directory
        if (!rootPics.exists())
        {
            rootPics.mkdirs();
        }

        iv.buildDrawingCache();
        Bitmap bm=iv.getDrawingCache();
        OutputStream fOut = null;

        try {
            File root = getPicsDirectory();
            File sdImageMainDirectory = new File(root, uuId+".jpg");
            if (sdImageMainDirectory.exists()) {
                sdImageMainDirectory.delete();
            }
            fOut = new FileOutputStream(sdImageMainDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            // Copy to downloads dir
        } catch (Exception e) {
        }

        if (MainActivity.TRACE_FLAG) {
            // For Debug, copy the file into Downloads Directory

            File root = getPicsDirectory();
            File sdImageMainDirectory = new File(root, uuId + ".jpg");
            File dst = new File(rootDownload, uuId + ".jpg");
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
                    } catch (Exception ex1) {
                        Log.e("Pic2Download", ex1.getMessage());
                        ex1.printStackTrace();
                    }
                } catch (Exception ex1) {
                    Log.e("Pic2Download", ex1.getMessage());
                    ex1.printStackTrace();
                }
            }
        }
    }

    public void CopyDefaultImage(String defImageUuid){

        try {

            // Ensure that pics directory exists
            File rootPics = getPicsDirectory();
            File rootDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!rootPics.exists())
            {
                rootPics.mkdirs();
            }
            File root = getPicsDirectory();
            File sdImageMainDirectory = new File(root, defImageUuid+".jpg");
            if (sdImageMainDirectory.exists()) {
                // Bail out if image already exists
                return;
            }

            InputStream in = m_context.getResources().openRawResource(R.raw.little_boy_grey2);
            FileOutputStream out = new FileOutputStream(sdImageMainDirectory);
            byte[] buff = new byte[1024];
            int read = 0;

            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        }
        catch(IOException ex1)
        {
            ex1.printStackTrace();
        }
    }
}
