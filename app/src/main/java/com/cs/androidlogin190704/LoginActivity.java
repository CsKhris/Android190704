package com.cs.androidlogin190704;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.TextStyle;

public class LoginActivity extends AppCompatActivity {

    EditText memberId, memberPw;
    LinearLayout mainView;
    Button loginBtn;

    // mainView의 색상을 변경하는 Handler 생성
    // 일반 Thread에서는 View의 화면 변경을 하면 안됩니다.
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            String result = (String)msg.obj;
            if(result.equals("true")){
                  mainView.setBackgroundColor(Color.BLUE);
            }else {
                  mainView.setBackgroundColor(Color.RED);
            }
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(memberPw.getWindowToken(), 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        memberId = (EditText) findViewById(R.id.memberid);
        memberPw = (EditText) findViewById(R.id.memberpw);
        loginBtn = (Button) findViewById(R.id.loginbtn);
        mainView = (LinearLayout) findViewById(R.id.mainview);

        loginBtn.setOnClickListener(view -> {
            Thread th = new Thread(){
                String json = null;
                public void run(){
                    try{
                        String addr = "http://192.168.0.105:8080/login?id=" +
                                memberId.getText().toString().trim().toUpperCase() +
                                "&pw=" + memberPw.getText().toString().trim();

                        URL url = new URL(addr);

                        HttpURLConnection con = (HttpURLConnection)url.openConnection();

                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        StringBuilder sb = new StringBuilder();

                        while(true){
                            String line = br.readLine();
                            if(line == null) {
                                break;
                            }
                            sb.append(line);
                        }
                        //Log.e("Data", sb.toString());
                        json = sb.toString();
                        br.close();
                        con.disconnect();

                    }catch (Exception e){
                        Log.e("Download Exception", e.getMessage());
                    }
                    try {
                        if (json != null) {
                            JSONObject result = new JSONObject(json);
                            String msg = result.getString("result");
                            Message message = new Message();
                            message.obj = msg;
                            handler.sendMessage(message);
                        }
                    }catch (Exception e){
                        Log.e("Parsing Exception", e.getMessage());
                    }
                }
            };
            th.start();
        });
    }
}
