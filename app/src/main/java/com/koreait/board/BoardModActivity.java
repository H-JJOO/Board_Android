package com.koreait.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BoardModActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etCtnt;
    private EditText etWriter;
    private BoardService service;
    private int iboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        etTitle = findViewById(R.id.etTitle);//xml 연결
        etCtnt = findViewById(R.id.etCtnt);//연결
        etWriter = findViewById(R.id.etWriter);//연결

        Intent intent = getIntent();
        iboard = intent.getIntExtra("iboard", 0);//intent 값 가져오기


        Retrofit retrofit = RetroFitObj.getInstance();//딱한번만 실행, Retrofit 주소값을 배포, retrofit 능력으로 구현해준다.
        service = retrofit.create(BoardService.class);//내가 호출하고싶은 서비스를 호출할수 있음

        getBoardDetail();
    }




    public void getBoardDetail() {
        Call<BoardVO> call = service.selBoardDetail(iboard);
        call.enqueue(new Callback<BoardVO>() {
            @Override
            public void onResponse(Call<BoardVO> call, Response<BoardVO> res) {
                if (res.isSuccessful()) {
                    BoardVO vo = res.body();
                    etTitle.setText(vo.getTitle());
                    etCtnt.setText(vo.getCtnt());
                    etWriter.setText(vo.getWriter());
                } else {
                    Log.e("myLog", "통신 오류");
                }
            }

            @Override
            public void onFailure(Call<BoardVO> call, Throwable t) {
                Log.e("myLog", "통신 자체 실패");
            }
        });
    }

    public void clkReg(View v) {
        String title = etTitle.getText().toString();
        String ctnt = etCtnt.getText().toString();
        String writer = etWriter.getText().toString();

        BoardVO data = new BoardVO();
        data.setIboard(iboard);
        data.setTitle(title);
        data.setCtnt(ctnt);
        data.setWriter(writer);

        Call<Void> call = service.updBoard(data);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    finish();
                } else {
                    Log.e("myLog", "통신 오류");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("myLog", "통신 자체 실패");
            }
        });

    }

}