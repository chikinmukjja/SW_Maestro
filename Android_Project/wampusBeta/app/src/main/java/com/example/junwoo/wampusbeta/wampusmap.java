package com.example.junwoo.wampusbeta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;



import java.util.Timer;
import java.util.TimerTask;




public class wampusmap extends AppCompatActivity implements View.OnClickListener {


    class WampusState {
        ImageButton imgbtn;
        boolean isActivated;
        int inner;
        int x;
        int y;
    }
    WampusState stateBtn[][];
    int dx[] = {-1,1,0,0}; // 상 하 좌 우
    int dy[] = {0,0,-1,1};

    private TextView txtTimer;
    private TimerTask MyTimerCallBack;
    private Timer timer;
    private int timeSecond = 0;

    private boolean btnText = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wampusmap);


        txtTimer = (TextView)findViewById(R.id.currentTimer);
        txtTimer.setText("00");


        final Button startButton = (Button)findViewById(R.id.StartButton);
        assert startButton != null;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnText) {
                    StartTimer(1);
                    startButton.setText("STOP");
                    renderMap(stateBtn[0][0]);
                    btnText = false;
               } else {
                    StopTimer();
                    startButton.setText("START");
                    btnText = true;
                }
            }
        });



        stateBtn = new WampusState[3][3];
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                stateBtn[i][j] = new WampusState();
                stateBtn[i][j].x = i;
                stateBtn[i][j].y = j;
            }
        }

        stateBtn[0][0].isActivated = true;

        // 1 -1  0
        // 0 -2 -1
        // 0  0  2
        stateBtn[0][0].imgbtn = (ImageButton)findViewById(R.id.imageButton1);
        stateBtn[0][0].imgbtn.setOnClickListener(this);
        stateBtn[0][0].inner = 1; //플레이어

        stateBtn[0][1].imgbtn = (ImageButton)findViewById(R.id.imageButton2);
        stateBtn[0][1].imgbtn.setOnClickListener(this);
        stateBtn[0][1].inner = -1; //구멍

        stateBtn[0][2].imgbtn = (ImageButton)findViewById(R.id.imageButton3);
        stateBtn[0][2].imgbtn.setOnClickListener(this);
        stateBtn[0][2].inner = 0;  //빈

        stateBtn[1][0].imgbtn = (ImageButton)findViewById(R.id.imageButton4);
        stateBtn[1][0].imgbtn.setOnClickListener(this);
        stateBtn[1][0].inner = 0;  //빈

        stateBtn[1][1].imgbtn = (ImageButton)findViewById(R.id.imageButton5);
        stateBtn[1][1].imgbtn.setOnClickListener(this);
        stateBtn[1][1].inner = -2; // 웜푸스

        stateBtn[1][2].imgbtn = (ImageButton)findViewById(R.id.imageButton6);
        stateBtn[1][2].imgbtn.setOnClickListener(this);
        stateBtn[1][2].inner = -1; //  구멍

        stateBtn[2][0].imgbtn = (ImageButton)findViewById(R.id.imageButton7);
        stateBtn[2][0].imgbtn.setOnClickListener(this);
        stateBtn[2][0].inner = 0; // 빈

        stateBtn[2][1].imgbtn= (ImageButton)findViewById(R.id.imageButton8);
        stateBtn[2][1].imgbtn.setOnClickListener(this);
        stateBtn[2][1].inner = 0;

        stateBtn[2][2].imgbtn= (ImageButton)findViewById(R.id.imageButton9);
        stateBtn[2][2].imgbtn.setOnClickListener(this);
        stateBtn[2][2].inner = 2; //gold



    }

    public void makePopUp() {

            Context mContext = getApplicationContext();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

            //R.layout.dialog는 xml 파일명이고  R.id.popup은 보여줄 레이아웃 아이디
            View layout = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.popupLayout));
            AlertDialog.Builder aDialog = new AlertDialog.Builder(wampusmap.this);

            aDialog.setTitle("GAME OVER"); //타이틀바 제목
            aDialog.setView(layout); //dialog.xml 파일을 뷰로 셋팅

            //그냥 닫기버튼을 위한 부분
            aDialog.setNegativeButton("RESET", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    StartTimer(1);
                }
            });
            //팝업창 생성
            AlertDialog ad = aDialog.create();
            ad.show();//보여줌!
    }


    private void StartTimer(long seconds){
        timer = new Timer();
        MyTimerCallBack = new TimerTask(){
            @Override
            public void run () {
                final Runnable timerAction = new Runnable() {
                    @Override
                    public void run() {
                        UpdateText();
                    }
                };
                if (timeSecond < 60) txtTimer.post(timerAction);
                else {
                    StopTimer();
                    timeSecond = 0;
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"stop Timer",Toast.LENGTH_SHORT);
                            makePopUp();
                        }
                    }, 0);
                }
            }
            @Override
            public boolean cancel() {
                return super.cancel();
            }
        };

        timer.scheduleAtFixedRate(MyTimerCallBack,0,seconds*1000);

    }


    private void StopTimer()
    {
        timeSecond =0;
        MyTimerCallBack.cancel();
        timer.cancel();
    }

    private void UpdateText(){
        timeSecond++;
        txtTimer.setText(""+timeSecond);
    }

    public boolean renderMap(WampusState _stateBtn)
    {
        if(timeSecond<0)return false;
        if(!_stateBtn.isActivated)return false;

        switch(_stateBtn.inner)
        {
            case 0://empty
                break;
            case -1://hole
                Toast.makeText(getApplicationContext(),"hole!! Dead",Toast.LENGTH_SHORT).show();
                StopTimer();
                makePopUp();
                return true;
            case -2://wampus
                Toast.makeText(getApplicationContext(),"Wampus!! Dead",Toast.LENGTH_SHORT).show();
                StopTimer();
                makePopUp();
                return true;
            case 1://player
                Toast.makeText(getApplicationContext(),"'?'를 클릭하세요",Toast.LENGTH_SHORT);
                break;
            case 2://gold
                Toast.makeText(getApplicationContext(),"great!! finish time : "+timeSecond,Toast.LENGTH_SHORT).show();
                StopTimer();
                makePopUp();
                return true;

        }
        //상하좌우 ? 표시
        //전 상황에서 갈 수 있었던 곳 모두 제외
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                stateBtn[i][j].isActivated = false;
                stateBtn[i][j].imgbtn.setImageResource(R.drawable.black);

            }
        }

        _stateBtn.imgbtn.setImageResource(R.drawable.player);

        String message = "";

        for(int i=0;i<4;i++)
        {
            if((0<=_stateBtn.x+dx[i]&&2>=_stateBtn.x+dx[i])&&(0<=_stateBtn.y+dy[i]&&2>=_stateBtn.y+dy[i])) {
                stateBtn[_stateBtn.x+dx[i]][_stateBtn.y+dy[i]].isActivated = true;
                stateBtn[_stateBtn.x+dx[i]][_stateBtn.y+dy[i]].imgbtn.setImageResource(R.drawable.quest);
                switch(stateBtn[_stateBtn.x+dx[i]][_stateBtn.y+dy[i]].inner)
                {
                    case 2:
                        message = message + " Gold ! ";
                        break;
                    case -1:
                        message = message + " Wind ! ";
                        break;
                    case -2:
                        message = message + " Smell ! ";
                        break;
                }
            }
            //상하좌우 토스트 메세지
        }

        if(message != "")
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

        return true;

    }


    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageButton1:
                renderMap(stateBtn[0][0]);
                break;

            case R.id.imageButton2:
                renderMap(stateBtn[0][1]);
                break;
            case R.id.imageButton3:
                renderMap(stateBtn[0][2]);
                break;
            case R.id.imageButton4:
                renderMap(stateBtn[1][0]);
                break;
            case R.id.imageButton5:
                renderMap(stateBtn[1][1]);
                break;
            case R.id.imageButton6:
                renderMap(stateBtn[1][2]);
                break;
            case R.id.imageButton7:
                renderMap(stateBtn[2][0]);
                break;
            case R.id.imageButton8:
                renderMap(stateBtn[2][1]);
                break;
            case R.id.imageButton9:
                renderMap(stateBtn[2][2]);
                break;


        }
    }

}
