package org.guog.gh.mretrofittest.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import org.guog.gh.mretrofittest.R;
import org.guog.gh.mretrofittest.adapter.PicAdapter;
import org.guog.gh.mretrofittest.bean.PicInfo;
import org.guog.gh.mretrofittest.service.PicService;
import org.guog.gh.mretrofittest.service.RetrofitWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PicActivity extends AppCompatActivity {

    private RecyclerView recylerView;
    private List<String> mData = new ArrayList<>();
    private List<String> mData_pic = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_pic);
        WindowManager wm = this.getWindowManager();
        int width =(int)(wm.getDefaultDisplay().getWidth()*0.9);
        recylerView = (RecyclerView) this.findViewById(R.id.recyclerView_pic);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        final PicAdapter adapter = new PicAdapter(PicActivity.this, mData,mData_pic,width);
        recylerView.setAdapter(adapter);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }

            }
        };

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.CYAN, Color.RED);
        assert swipeRefreshLayout != null;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(PicActivity.this, "onRefresh", Toast.LENGTH_SHORT).show();
                PicService picService = RetrofitWrapper.getInstance().create(PicService.class);
                Call<PicInfo> call = picService.getPicInfoList(
                         1, 10,  "30d7111054cb9b93a6e02e8940606c26");

                call.enqueue(new Callback<PicInfo>() {
                    @Override
                    public void onResponse(Call<PicInfo> call, final Response<PicInfo> response) {
                        Toast.makeText(PicActivity.this, "pic success", Toast.LENGTH_SHORT).show();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PicInfo ji = response.body();
                                mData.clear();
                                mData_pic.clear();
                                List<PicInfo.ResultBean.DataBean> list = ji.getResult().getData();
                                for (PicInfo.ResultBean.DataBean bean : list) {
                                    mData.add(bean.getContent());
                                    mData_pic.add(bean.getUrl());
                                }
                                swipeRefreshLayout.setRefreshing(false);
                                handler.sendEmptyMessage(1);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<PicInfo> call, Throwable t) {
                        Toast.makeText(PicActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.mainMenu_joke:
                Toast.makeText(PicActivity.this,"笑话",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PicActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.mainMenu_pic:
                Toast.makeText(PicActivity.this,"趣图",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
