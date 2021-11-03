package com.koreait.board;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.CellSignalStrengthGsm;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BoardWriteActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etCtnt;
    private EditText etWriter;
    private BoardService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//딱 한번만 호출
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        etTitle = findViewById(R.id.etTitle);//xml 연결
        etCtnt = findViewById(R.id.etCtnt);//연결
        etWriter = findViewById(R.id.etWriter);//연결

        Retrofit retrofit = RetroFitObj.getInstance();//주소값 받아오기
        service = retrofit.create(BoardService.class);

    }



    public void clkReg(View v) {
        String title = etTitle.getText().toString();//적힌 값을 문자열로 정리
        String ctnt = etCtnt.getText().toString();//정리
        String writer = etWriter.getText().toString();//정리

        BoardVO data = new BoardVO();//정리된거 한 그릇에 담기(Json 으로 변경하기 편해서)
        data.setTitle(title);//담기
        data.setCtnt(ctnt);//담기
        data.setWriter(writer);//담기

        Call<Void> call = service.insBoard(data);
        //비동기
        call.enqueue(new Callback<Void>() {//enqueue 신호 쏴주는 역할
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("myLog", "통신 성공");
                    finish();
                } else {
                    Log.i("myLog", "통신 오류");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("myLog", "통신 자체 실패");
            }
        });

    }
}