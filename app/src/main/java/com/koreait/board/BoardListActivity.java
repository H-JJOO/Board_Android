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


    }

    @Override
    protected void onStart() {
        super.onStart();
        getBoardList();
    }

    //글쓰기 Activity 로 이동
    public void clkWrite(View v) {//View 는 화면단에 나오는(Layout, Button,...) 모든 것들의 부모 클래스
        Intent intent = new Intent(this, BoardWriteActivity.class);//this -> BoardWriteActivity(목적지 정보)
        startActivity(intent);//화면 이동
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
//                        Log.i("myLog", vo.getTitle());
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
        //list에 접근해야함 (리스트에 있는 자료를 써야하기때문에)
        holder.itemView.setOnClickListener(new View.OnClickListener() {//list 에 접근 용의, 이름 없이 객체화, 각 줄 마다 이벤트 걸었다
            @Override
            public void onClick(View view) {//적어준걸 클릭하면 호출될거임
                Log.i("myLog", "iboard : " + vo.getIboard());

                Intent intent = new Intent(view.getContext(), BoardDetailActivity.class);//이사짐 차를 BoardDetailActivity.class 로 이동, 디테일 화면 띄우기
                intent.putExtra("iboard", vo.getIboard());//intent 값 넣기
                view.getContext().startActivity(intent);//포함되어있는 Activity의 주소값 얻을수 있음
            }
        });
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
            super(v);//item_board.xml 주소값
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