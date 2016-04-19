package org.guog.gh.mretrofittest.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by gh on 16/4/19.
 */
public class PicFragment extends Fragment {

    private Activity activity;

    public PicFragment() {

    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    private RecyclerView recylerView;
    private List<String> mData = new ArrayList<>();
    private List<String> mData_pic = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Pic onCreateView", "Pic onCreateView");
        View view = inflater.inflate(R.layout.activity_pic, container, false);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_pic);
        WindowManager wm = activity.getWindowManager();
        int width = (int) (wm.getDefaultDisplay().getWidth() * 0.9);
        recylerView = (RecyclerView) view.findViewById(R.id.recyclerView_pic);
        recylerView.setLayoutManager(new LinearLayoutManager(activity));
        final PicAdapter adapter = new PicAdapter(activity, mData, mData_pic, width);
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
                Toast.makeText(activity, "onRefresh", Toast.LENGTH_SHORT).show();
                PicService picService = RetrofitWrapper.getInstance().create(PicService.class);
                Call<PicInfo> call = picService.getPicInfoList(
                        1, 10, "30d7111054cb9b93a6e02e8940606c26");

                call.enqueue(new Callback<PicInfo>() {
                    @Override
                    public void onResponse(Call<PicInfo> call, final Response<PicInfo> response) {
                        Toast.makeText(activity, "pic success", Toast.LENGTH_SHORT).show();

                        activity.runOnUiThread(new Runnable() {
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
                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("PicFragment","onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("PicFragment","onDestroyView");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("PicFragment","onPause");
    }
}
