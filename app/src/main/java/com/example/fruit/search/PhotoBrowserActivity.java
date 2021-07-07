package com.example.fruit.search;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.chrisbanes.photoview.PhotoView;
import com.example.fruit.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoBrowserActivity extends AppCompatActivity implements View.OnClickListener{

    private String imageUrl;
    private PhotoView mPhotoView;
    private ImageView mRotateLeft;
    private ImageView mRotateRight;
    private Button mSaveToLocal;
    private static final String TAG = "TEST";
    private Bitmap mBitmap;
    private Animation mAnimation;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        getWindow().getDecorView().setSystemUiVisibility(option);
        setContentView(R.layout.activity_photo_browser);

        try{
            //需要的权限
            Log.d(TAG,"permission");
            String[] permArr={
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            };
            boolean needReq = false;
            for(int i=0;i<permArr.length;i++){
                if(ContextCompat.checkSelfPermission(this, permArr[i])!= PackageManager.PERMISSION_GRANTED){
                    needReq = true;
                    break;
                }
            }
            if(needReq)
                ActivityCompat.requestPermissions(this,permArr,1);
        }catch (Exception e){
            e.printStackTrace();
        }

        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        imageUrl = getIntent().getStringExtra("image");
        mPhotoView = (PhotoView)findViewById(R.id.photo_view);
        mRotateLeft = (ImageView)findViewById(R.id.rotate_left);
        mRotateRight = (ImageView)findViewById(R.id.rotate_right);
        mSaveToLocal = (Button)findViewById(R.id.save_to_local);
        mPhotoView.setOnClickListener(this);
        mRotateLeft.setOnClickListener(this);
        mRotateRight.setOnClickListener(this);
        mSaveToLocal.setOnClickListener(this);

        try {
            Bitmap bm = ((BitmapDrawable) PhotoBrowserActivity.loadImageFromUrl(imageUrl)).getBitmap();
            mBitmap = bm;
            mPhotoView.setImageBitmap(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Drawable loadImageFromUrl(String url) throws IOException {

        URL m = new URL(url);
        URLConnection conn = m.openConnection();
        InputStream i = conn.getInputStream();
        Drawable d = Drawable.createFromStream(i, "src");
        return d;
    }
//    private int saveImageToGallery(Bitmap bmp) {
//
//        //生成路径
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        File directory = cw.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        Log.d(TAG,directory.toString());
//        //文件名为时间
//        long timeStamp = System.currentTimeMillis();
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String sd = sdf.format(new Date(timeStamp));
//        String fileName = sd + ".png";
//
//        File file = new File(directory,fileName);
//
//        Log.d(TAG,fileName);
//
//        try {
//
//            Log.d(TAG,"TEST1");
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//            boolean test = bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
//            Log.d(TAG,"TEST2");
//            Log.d(TAG,Boolean.toString(test));
//            bos.flush();
//            bos.close();
//
//            //通知系统相册刷新
//            PhotoBrowserActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                    Uri.fromFile(file)));
//            Log.d(TAG,"TEST3");
//            return 2;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }

    public static void saveImageToGallery2(Context context, Bitmap image){
        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "Souffle_%s.png";
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);

        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES
                + File.separator + "Souffle"); //Environment.DIRECTORY_SCREENSHOTS:截图,图库中显示的文件夹名。"dh"
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, mImageFileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATE_ADDED, mImageTime / 1000);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, mImageTime / 1000);
        values.put(MediaStore.MediaColumns.DATE_EXPIRES, (mImageTime + DateUtils.DAY_IN_MILLIS) / 1000);
        values.put(MediaStore.MediaColumns.IS_PENDING, 1);

        ContentResolver resolver = context.getContentResolver();
        final Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {

            try (OutputStream out = resolver.openOutputStream(uri)) {
                if (!image.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    throw new IOException("Failed to compress");
                }
            }

            values.clear();
            values.put(MediaStore.MediaColumns.IS_PENDING, 0);
            values.putNull(MediaStore.MediaColumns.DATE_EXPIRES);
            resolver.update(uri, values, null, null);
            Toast.makeText(context,R.string.save_successfully,Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rotate_left:
                mAnimation = AnimationUtils.loadAnimation(this,R.anim.rotate_left);
                mAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mPhotoView.setRotationBy(90);
                        mAnimation.setFillAfter(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mAnimation.setFillAfter(true);
                mPhotoView.startAnimation(mAnimation);

                break;
            case R.id.rotate_right:
                mAnimation = AnimationUtils.loadAnimation(this,R.anim.rotate_right);
                mAnimation.setFillAfter(true);
                mPhotoView.startAnimation(mAnimation);
                mAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mPhotoView.setRotationBy(-90);
                        mAnimation.setFillAfter(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;
            case R.id.save_to_local:
                saveImageToGallery2(this,mBitmap);
                break;
            default:
                finish();
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
