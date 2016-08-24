package com.soma.junwoo.wampusworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by junwoo on 2016-08-04.
 */
public class Util {

    interface OnPopupResponse {
        void onResponse();
    }

    public class PopupRespose {
        PopupRespose() {

        }
    }

    public static void failurePopUp(Activity mContext, int stateInner, final OnPopupResponse on) {

        //Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        //R.layout.dialog는 xml 파일명이고  R.id.popup은 보여줄 레이아웃 아이디
        View layout = inflater.inflate(R.layout.dialog, (ViewGroup) mContext.findViewById(R.id.failureView));
        AlertDialog.Builder aDialog = new AlertDialog.Builder(mContext);
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
                on.onResponse();;

//                initializeMap(states);
//                makeMap(states);
//                if(renderMap(states.get(4*DIMENSION))) Log.i("D","redermap true");
//                StartTimer(1);
//                startButton.setText("RESET");
//                btnText = false;
            }
        });
        //팝업창 생성
        AlertDialog ad = aDialog.create();
        ad.show();//보여줌!
    }
}
