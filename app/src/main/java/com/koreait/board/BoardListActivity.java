package com.koreait.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BoardListActivity extends AppCompatActivity {
    private RecyclerView rvList;
    private BoardListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);
        adapter = new BoardListAdapter();//객체화

        rvList = findViewById(R.id.rvList);

        rvList.setAdapter(adapter);

        getBoardList();

    }
    //글쓰기 Activity 로 이동
    public void clkWrite(View v) {
        Intent intent = new Intent(this, BoardWriteActivity.class);
        startActivity(intent);
    }

    //통신
    private void getBoardList() {
        Retrofit retrofit = RetroFitObj.getInstance();
        BoardService service = retrofit.create(BoardService.class);

        Call<List<BoardVO>> call = service.selBoardList();

        //비동기 처리
        call.enqueue(new Callback<List<BoardVO>>() {
            @Override
            public void onResponse(Call<List<BoardVO>> call, Response<List<BoardVO>> res) {
                if (res.isSuccessful()) {
                    List<BoardVO> result = res.body();
                    adapter.setList(result);
                    adapter.notifyDataSetChanged();

                    for (BoardVO vo : result) {
                        Log.i("myLog", vo.getTitle());
                    }
                } else {
                    Log.e("myLog", "통신 오류 : " + res.code());
                }
            }

            @Override
            public void onFailure(Call<List<BoardVO>> call, Throwable t) {
                Log.e("myLog", "통신 자체 실패");
            }
        });

    }
}

class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.MyViewHolder> {
    private List<BoardVO> list;

    public void setList(List<BoardVO> list) {
        this.list = list;

    }

    @NonNull
    @Override
    //layout 만드는 담당
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_board, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    //호출할 내용
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BoardVO vo = list.get(position);
        holder.setItem(vo);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();//몇개냐
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIboard;
        private TextView tvTitle;
        private TextView tvWriter;
        private TextView tvRdt;

        public MyViewHolder(View v) {
            super(v);
            tvIboard = v.findViewById(R.id.tvIboard);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvWriter = v.findViewById(R.id.tvWriter);
            tvRdt = v.findViewById(R.id.tvRdt);
        }

        public void setItem(BoardVO param) {
            tvIboard.setText(String.valueOf(param.getIboard()));//문자열로 변환, 인트값은 안쓴다
            tvTitle.setText(param.getTitle());
            tvWriter.setText(param.getWriter());
            tvRdt.setText(param.getRdt());
        }
    }
}