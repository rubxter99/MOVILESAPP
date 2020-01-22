package com.example.imagebanner;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private TimerTask mTask2;
    private Timer mTimer2;
    private Integer contador=0;
    private ImageView[] images;

    private TimerTask mTask=null;
    private Timer mTimer=null;
    private Integer mBarra =3;
    private String rutamg="";
    private MyTaskProgressDialog m;
    private TextView txtimage;
    private  final String TAG = getClass().getSimpleName();
    private final String[] urls = {"https://edit.org/img/blog/2018101613-banner-cyber-monday-neon.jpg.pagespeed.ce.kXehntmsoJ.jpg","https://aws1.discourse-cdn.com/envato/original/3X/9/0/90942f4afa2153da94cedacfcef9ac592a131647.jpg" ,"https://thumbs.dreamstime.com/z/cyber-monday-discount-sale-concept-inscription-design-template-banner-vector-illustration-eps-130761400.jpg"};

    private static final Integer MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView();
        Button btn_Download=(Button) findViewById(R.id.btn_Download);
        Button btn_Delete=(Button) findViewById(R.id.btn_Delete);
        txtimage=(TextView)findViewById(R.id.txt1);


        btn_Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                }else{
                     m=new MyTaskProgressDialog();
                    m.execute("https://st3.idealista.com/news/archivos/styles/news_detail/public/2018-11/black_friday_pixabay.png\n"
                            ,"https://e00-elmundo.uecdn.es/assets/multimedia/imagenes/2019/11/29/15750265057275.jpg");

                }
               }
        });
        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    deleteFile("https://st3.idealista.com/news/archivos/styles/news_detail/public/2018-11/black_friday_pixabay.png"
                            ,"https://e00-elmundo.uecdn.es/assets/multimedia/imagenes/2019/11/29/15750265057275.jpg");

            }

        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        mTask2.cancel();
        m.cancel(true);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(mTimer2!=null){
            ImageView();
        }

    }



    private void ImageView() {
        final Integer TIMER_TASK_DELAY = 1;
        final Integer TIMER_TASK_PERIOD = 5000;
        final ImageView imagenfinal=(ImageView) findViewById(R.id.imagen1);
        mTask2 = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Log.d("Main","TimerTask");

                        if(contador==0){
                            Picasso.get().load(urls[contador]).error(R.drawable.ic_launcher_background).into(imagenfinal);
                        }else if(contador==1){
                            Picasso.get().load(urls[contador]).error(R.drawable.ic_launcher_background).into(imagenfinal);
                        }else{
                            Picasso.get().load(urls[contador]).error(R.drawable.ic_launcher_background).into(imagenfinal);
                        }
                        contador++;
                        if(contador==3){
                            contador=0;
                        }

                    }
                });
            }
        };
        mTimer2 = new Timer();
        mTimer2.schedule(mTask2, TIMER_TASK_DELAY, TIMER_TASK_PERIOD);
    }

    public void deleteFile(String... params)  {
        String[] mFilesNames = params;
        for (int i=0;i<mFilesNames.length;i++) {
            final  String fileName = mFilesNames[i].toString().substring(mFilesNames[i].toString().lastIndexOf("/") + 1);
            final File destination = new File(Environment.getExternalStorageDirectory().toString() + "/" + fileName);
            if (destination.exists())
            {
                Log.d(TAG,"File already exists! Remove it");
                destination.delete();
                txtimage.setText("");
            }
        }








    }
    public class MyTaskProgressDialog extends AsyncTask<String,Integer,Void> {
        private ProgressDialog mPd;
        private String[] mFilesNames;
        private Context mContext;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mPd=new ProgressDialog(MainActivity.this);
            mPd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mPd.setTitle("ProgressDialog");
            mPd.setMessage("Download file...");
            mPd.show();

        }
        public void setContext(Context context){
            mContext=context;
        }

        private String downloadFile (final URL destUrl, final String directory) throws IOException
        {
            Log.d("downloadFile", "downloadFile(" + destUrl.toString() + ", " + directory);


            final  String fileName = destUrl.toString().substring(destUrl.toString().lastIndexOf("/") + 1);

            // This will be useful so that you can show a typical 0-100% progress bar
            final URLConnection connection = destUrl.openConnection();
            connection.connect();
            final int fileLength = connection.getContentLength();

            // Download the file
            final File destinationFolder = new File(directory);
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            final File destination = new File(destinationFolder, fileName);
            if (destination.exists())
            {
                Log.d(TAG,"File already exists! Remove it");
                destination.delete();
            }

            Log.d(TAG, "Starting download on: " + destination.toString());

            final InputStream input = new java.io.BufferedInputStream(connection.getInputStream());
            OutputStream output = null;
            try {
                output = new FileOutputStream(destination);
            } catch (IOException ex) {
                Log.e(TAG, "Error opening destination file: " + ex.getMessage());
                return null;
            }

            final byte data[] = new byte[4096];

            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            Log.d(TAG, "Finished download on: " + destination.toString());

            return destination.toString();


        }

        @Override
        protected Void doInBackground(String... params) {
            mFilesNames=params;
            for (int i=0;i<mFilesNames.length;i++){
                publishProgress(i);
                try{

                      rutamg=rutamg+downloadFile(new URL(mFilesNames[i]), Environment.getExternalStorageDirectory().toString())+"\n";
                        Log.d(TAG, Environment.getExternalStorageDirectory().toString());
                        Thread.sleep(2000);



                }

                catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            if(mPd.isShowing()){
                mPd.dismiss();
                txtimage.setText(rutamg);
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer numFile=values[0];
            mPd.setMessage("Descargando fichero:"+numFile.toString());
            mPd.setProgress(((values[0]+1)*100/ mBarra));
        }
    }
}
