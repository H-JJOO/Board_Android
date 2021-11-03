package com.koreait.board;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BoardService {

    @POST("ins")//POST 때만 @Body Json 써야하니까
    Call<Void> insBoard(@Body BoardVO param);//@Body 알아서 Json 을 문자열로 바꿔줌

    @GET("selList")//Query 문 없으니까 빈칸
    Call<List<BoardVO>> selBoardList();

    @GET("sel")//Query 문 있으니까 @Query 하고 값, 타입 변수명
    Call<BoardVO> selBoardDetail(@Query("iboard") int iboard);

    @POST("upd")
    Call<Void> updBoard(@Body BoardVO param);//응답 데이터가 없으면 Void

    @GET("del")
    Call<Void> delBoard(@Query("iboard") int ibaord);



}
