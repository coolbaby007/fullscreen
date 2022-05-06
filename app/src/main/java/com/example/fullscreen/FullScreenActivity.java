package com.example.fullscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FullScreenActivity extends AppCompatActivity {

    private int TIME = 1000;
    TextView tv_date;
    MyTextView tv_info;
    TextView tv_Name;
    TextView tv_Identity;
    TextView tv_checktime;


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm:ss");
                Date d = new Date();
                String fmtdate = formatter.format(d);
                tv_date.setText(fmtdate);
            }
            super.handleMessage(msg);
        };
    };

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            // 发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_full_screen);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_info = (MyTextView) findViewById(R.id.tv_time5);
        tv_info.setSpecifiedTextsColor("健康状况核验未见异常（绿码）","未见异常（绿码）", Color.parseColor("#007f0a"));
        timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行

        tv_Name = (TextView) findViewById(R.id.tv_Name);
        tv_Identity = (TextView) findViewById(R.id.tv_Identity);
        if(MainActivity.number == 0) {
            tv_Name.setText(MainActivity.name0);
            tv_Identity.setText(MainActivity.identity0);
        } else if(MainActivity.number == 1) {
            tv_Name.setText(MainActivity.name1);
            tv_Identity.setText(MainActivity.identity1);
        }

        tv_checktime = (TextView) findViewById(R.id.tv_checktime);
        tv_checktime.setText("阴性 " + MainActivity.checktime);

        Space space_bottom = (Space) findViewById(R.id.space_bottom);
        ViewGroup.LayoutParams params = space_bottom.getLayoutParams();
        params.height += (Integer.parseInt(MainActivity.checkOffset));
        space_bottom.setLayoutParams(params);

        tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo();
            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            Resources resources = this.getApplicationContext().getResources();
            @SuppressLint("ResourceType")
            InputStream istream = resources.openRawResource(R.drawable.golden);
            Bitmap golden = BitmapFactory.decodeStream(istream);

            try {
                Bitmap mapqr = MainActivity.instance.cropQR(FullScreenActivity.this);
                Bitmap newBmp = Utils.newBitmap(golden, mapqr);
                ImageView imggoden = findViewById(R.id.image_golden);
                imggoden.setImageBitmap(newBmp);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void showInfo(){
        if(MainActivity.number == 0) {
            tv_Name.setText(MainActivity.name1);
            tv_Identity.setText(MainActivity.identity1);
            MainActivity.number = 1;
        } else if(MainActivity.number == 1) {
            tv_Name.setText(MainActivity.name0);
            tv_Identity.setText(MainActivity.identity0);
            MainActivity.number = 0;
        }
    }

    private File fetchQR(){
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File dir = new File(sdCardRoot, "myqr");
        if(!dir.exists()) dir.mkdir();
        String path = dir.getPath() + "/qr.jpg";
        File file = new File(path);
        if(file.exists())
            return file;
        else
            return null;
        /*for (File f : yourDir.listFiles()) {
            if (f.isFile()) {
                String extname = Utils.getFileExtension(f);
                if(extname != null && (extname.equals("jpg") || extname.equals("jpeg") || extname.equals("png"))) return f;
            }
        }*/

    }

  //  private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
   //     Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),firstBitmap.getConfig());
   //     Canvas canvas = new Canvas(bitmap);
   //     canvas.drawBitmap(firstBitmap, new Matrix(), null);
   //     canvas.drawBitmap(secondBitmap, 0, 0, null);
   //     return bitmap;
  //  }
}