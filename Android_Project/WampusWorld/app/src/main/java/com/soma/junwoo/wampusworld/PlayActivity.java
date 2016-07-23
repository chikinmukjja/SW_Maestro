package com.soma.junwoo.wampusworld;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

// 내가 본 사물들을 어떻게 추상화 시킬 것인가 : 객체
// 주체가 바로볼때 객체를 어떻게 추상화시킬 것이냐

public class PlayActivity extends AppCompatActivity {

    private Intent loginIntent;

    private TextView txtTimer;
    private TimerTask MyTimerCallBack;
    private Timer timer;
    private int timeSecond = 0;

    private Button startButton;
    private boolean btnText = true;

    private List<WampusState> states;
    List<ImageButton> stateIndex;
    private WampusState curState;

    private final int DIMENSION = 5; //your number of image buttons

    int dx[] = {-1,1,0,0}; // 상 하 좌 우
    int dy[] = {0,0,-1,1};

    class WampusState {
        ImageButton imgbtn;
        boolean isActivated;
        int inner;
        int x;
        int y;
        boolean past;
        WampusState(int _x,int _y)
        {
            x = _x;
            y = _y;
            isActivated = false;
            past = false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        loginIntent = getIntent();
        txtTimer = (TextView)findViewById(R.id.time);
        txtTimer.setText("00");

        final Button rankButton = (Button)findViewById(R.id.lankButton);
        assert rankButton != null;
        rankButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(),
                        LankingActivity.class);
                intent.putExtra("name",loginIntent.getStringExtra("name"));
                startActivity(intent);
            }
        });



        startButton = (Button)findViewById(R.id.StartButton);
        assert startButton != null;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnText) {
                    makeMap(states);
                    if(renderMap(states.get(4*DIMENSION))) Log.i("D","redermap true"); // 시작위치
                    StartTimer(1);
                    startButton.setText("RESET");
                    btnText = false;
                } else {
                    StopTimer();
                    initializeMap(states);
                }
            }
        });


        states = new ArrayList<>();
        stateIndex = new ArrayList<>();
        GridLayout gameBoard = (GridLayout)findViewById(R.id.gridLayout);

        int width = Math.round(getResources().getDisplayMetrics().widthPixels/(DIMENSION));
        int height = Math.round(getResources().getDisplayMetrics().heightPixels/(DIMENSION+4));

        for (int rowCounter = 0; rowCounter < DIMENSION; rowCounter++)
            for (int columnCounter = 0; columnCounter < DIMENSION; columnCounter++) {

                WampusState state = new WampusState(rowCounter,columnCounter);

                state.imgbtn = new ImageButton(this);
                state.imgbtn.setImageResource(R.drawable.bar_back);
                state.imgbtn.setBackgroundColor(Color.DKGRAY);
                state.imgbtn.setPadding(3,3,3,3);
                state.imgbtn.setScaleType(ImageView.ScaleType.FIT_CENTER);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(0, 0, 0, 0);

                state.imgbtn.setLayoutParams(params);
                state.imgbtn.getLayoutParams().height=height;
                state.imgbtn.getLayoutParams().width= width;

                states.add(state);
                stateIndex.add(state.imgbtn);

                state.imgbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = stateIndex.indexOf(v);
                        //gameProcessing
                        renderMap(states.get(index));
                    }
                });
                gameBoard.addView(state.imgbtn);
            }


    }

    private int px(float dips)
    {
        float DP = getResources().getDisplayMetrics().density;
        return Math.round(dips * DP);
    }

    private void StartTimer(long seconds){
        txtTimer.setText("00");
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
                if (timeSecond < 60)
                {
                    txtTimer.post(timerAction);
                }
                else {
                    StopTimer();
                    timeSecond = 0;
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //initializeMap(states);
                            Toast.makeText(getApplicationContext(),"stop Timer",Toast.LENGTH_SHORT).show();
                            failurePopUp(-2);
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
//      txtTimer.setText("00");
        MyTimerCallBack.cancel();
        timer.cancel();
    }

    private void UpdateText(){
        timeSecond++;
        txtTimer.setText(""+timeSecond);
    }

    public boolean renderMap(WampusState _stateBtn)
    {

        Log.i("CUR",""+_stateBtn.x+" "+_stateBtn.y);
        if(timeSecond<0)return false;
        if(!_stateBtn.isActivated)return false;
        Log.i("CUR",""+_stateBtn.inner);
        switch(_stateBtn.inner)
        {
            case 0://empty
                sensingMap(_stateBtn);
                movePlayer(_stateBtn);
                _stateBtn.inner = 1;
                break;

            case -1://hole
                Toast.makeText(getApplicationContext(),"hole!! Dead",Toast.LENGTH_SHORT).show();
                StopTimer();
                failurePopUp(-1);
                return true;

            case -2://wampus
                Toast.makeText(getApplicationContext(),"Wampus!! Dead",Toast.LENGTH_SHORT).show();
                StopTimer();
                failurePopUp(-2);
                return true;

            case 1://player
                sensingMap(_stateBtn);
                curState = _stateBtn;

                Toast.makeText(getApplicationContext(),"'?'를 클릭하세요",Toast.LENGTH_SHORT).show();
                _stateBtn.isActivated = false;
                _stateBtn.imgbtn.setImageResource(R.drawable.bar_back_player);

                //전에 표시 되었던 물음표 삭제
                for(int i=0;i<4;i++)
                {
                    if((0<=_stateBtn.x+dx[i]&&DIMENSION>_stateBtn.x+dx[i])&&(0<=_stateBtn.y+dy[i]&&DIMENSION>_stateBtn.y+dy[i])) {
                        int index = (_stateBtn.x + dx[i]) * DIMENSION + _stateBtn.y + dy[i];

                        if (!states.get(index).past) {
                            states.get(index).isActivated = true;
                            states.get(index).imgbtn.setImageResource(R.drawable.bar_back_question);
                        }else {
                            states.get(index).imgbtn.setImageResource(R.drawable.bar_back_open);
                        }
                    }
                }

                break;
            case 2://gold
                Toast.makeText(getApplicationContext(),"great!! finish time : "+timeSecond,Toast.LENGTH_SHORT).show();
                successPopUp(timeSecond);
                StopTimer();
                return true;

        }



        return true;

    }

    public void failurePopUp(int stateInner) {

        Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        //R.layout.dialog는 xml 파일명이고  R.id.popup은 보여줄 레이아웃 아이디
        View layout = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.failureView));
        AlertDialog.Builder aDialog = new AlertDialog.Builder(PlayActivity.this);
        if(stateInner == -1){
            layout.setBackgroundResource(R.drawable.bar_back_hole);
        }else{
            layout.setBackgroundResource(R.drawable.bar_back_monster);
        }

        aDialog.setTitle("GAME OVER"); //타이틀바 제목
        aDialog.setView(layout); //dialog.xml 파일을 뷰로 셋팅

        //그냥 닫기버튼을 위한 부분
        aDialog.setNegativeButton("RESET", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                initializeMap(states);
                makeMap(states);
                if(renderMap(states.get(4*DIMENSION))) Log.i("D","redermap true");
                StartTimer(1);
                startButton.setText("RESET");
                btnText = false;
            }
        });
        //팝업창 생성
        AlertDialog ad = aDialog.create();
        ad.show();//보여줌!
    }

    public void successPopUp(final int score) {

        Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        //R.layout.dialog는 xml 파일명이고  R.id.popup은 보여줄 레이아웃 아이디
        View layout = inflater.inflate(R.layout.success, (ViewGroup) findViewById(R.id.successView));
        AlertDialog.Builder aDialog = new AlertDialog.Builder(PlayActivity.this);

        aDialog.setTitle("GAME SUCCESS"); //타이틀바 제목
        aDialog.setView(layout); //dialog.xml 파일을 뷰로 셋팅


        View content =  inflater.inflate(R.layout.success, null);
        aDialog.setView(content);
        TextView textView = (TextView) content.findViewById(R.id.scoreText);
        textView.setText(""+timeSecond);
        //그냥 닫기버튼을 위한 부분
        aDialog.setNegativeButton("RECORD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //취소시\
                initializeMap(states);
                makeMap(states);
                //Log.i("TAG"," ffff      "+score);
                recordScore(score);
                //if(renderMap(states.get(4*DIMENSION))) Log.i("D","redermap true");
                // StartTimer(1);
            }
        });
        //팝업창 생성
        AlertDialog ad = aDialog.create();
        ad.show();//보여줌!
    }

    public void recordScore(int score)
    {
        SimpleDateFormat s = new SimpleDateFormat("yyyy.MM.dd : H");
        String time = s.format(new Date());

        Intent intent = new Intent(getApplicationContext(), LankingActivity.class);
        intent.putExtra("score",score);
        intent.putExtra("time",time);
        intent.putExtra("id",loginIntent.getStringExtra("id"));
        intent.putExtra("name",loginIntent.getStringExtra("name"));
        startActivity(intent);

    }

    public void makeMap(List<WampusState> _states)
    {
        // Random 생성
        int empty = 0;
        int hole = 0;
        int wampus = 0;

        for(int i=0;i<_states.size();i++){
            Random rand = new Random();
            int num = rand.nextInt(3); //0 1 2

            switch(num)
            {
                case 0:
                    _states.get(i).inner = num;
                    empty++;
                    break;
                case 1:
                    if(hole > 3){
                        i = i - 1;
                        break;
                    }
                    _states.get(i).inner = -1;
                    hole++;
                    break;
                case 2:
                    if(wampus > 3){
                        i = i - 1;
                        break;
                    }
                    _states.get(i).inner = -2;
                    wampus++;
                    break;
            }
        }
        // 웜푸스 3, 구멍 3, 나머지 엠티
        // 골드 생성(1,3)
        _states.get(1*DIMENSION+3).inner = 2;
        _states.get(4*DIMENSION+0).inner = 1;
        _states.get(4*DIMENSION+0).isActivated = true;
        _states.get(4*DIMENSION+0).past = true;

    }

    public void initializeMap(List<WampusState> _states)
    {
        for(int i=0;i<_states.size();i++)
        {
            _states.get(i).imgbtn.setImageResource(R.drawable.bar_back);
            _states.get(i).isActivated = false;
            _states.get(i).past = false;
        }
        timeSecond=0;
        txtTimer.setText("00");
        startButton.setText("START");
        btnText = true;
    }

    public void movePlayer(WampusState _stateBtn)
    {
        _stateBtn.isActivated = false;

        for(int i=0;i<4;i++) {
            if ((0 <= curState.x + dx[i] && DIMENSION > curState.x + dx[i]) && (0 <= curState.y + dy[i] && DIMENSION > curState.y + dy[i])) {

                int index = (curState.x + dx[i]) * DIMENSION + curState.y + dy[i];
                states.get(index).isActivated = false;

                if (states.get(index).past) { // 이전에 왔던 장소면
                    states.get(index).imgbtn.setImageResource(R.drawable.bar_back_open);
                } else {
                    states.get(index).imgbtn.setImageResource(R.drawable.bar_back);
                }
            }
        }


        _stateBtn.isActivated = false;
        _stateBtn.past = true;

        _stateBtn.imgbtn.setImageResource(R.drawable.bar_back_player);
        //전에 표시 되었던 물음표 삭제
        for(int i=0;i<4;i++)
        {
            if((0<=_stateBtn.x+dx[i]&&DIMENSION>_stateBtn.x+dx[i])&&(0<=_stateBtn.y+dy[i]&&DIMENSION>_stateBtn.y+dy[i])) {
                int index = (_stateBtn.x + dx[i]) * DIMENSION + _stateBtn.y + dy[i];
                states.get(index).isActivated = true;
                if (!states.get(index).past) {

                    states.get(index).imgbtn.setImageResource(R.drawable.bar_back_question);
                }else {
                    states.get(index).imgbtn.setImageResource(R.drawable.bar_back_open);
                }
            }
            //상하좌우 토스트 메세지
        }

        curState.inner = 0;
        curState = _stateBtn;

    }

    public void sensingMap(WampusState _stateBtn)
    {
        String message = "";

        for(int i=0;i<4;i++)
        {
            if((0<=_stateBtn.x+dx[i]&&DIMENSION>_stateBtn.x+dx[i])&&(0<=_stateBtn.y+dy[i]&&DIMENSION>_stateBtn.y+dy[i])) {
                int index = (_stateBtn.x + dx[i]) * DIMENSION + _stateBtn.y + dy[i];

                switch(states.get(index).inner)
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

        }

        if(message != "")
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }
}

