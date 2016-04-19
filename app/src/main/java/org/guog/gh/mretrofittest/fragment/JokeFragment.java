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
import android.widget.Toast;

import org.guog.gh.mretrofittest.R;
import org.guog.gh.mretrofittest.adapter.HomeAdapter;
import org.guog.gh.mretrofittest.bean.JokeInfo;
import org.guog.gh.mretrofittest.service.JokeService;
import org.guog.gh.mretrofittest.service.RetrofitWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gh on 16/4/19.
 */
public class JokeFragment extends Fragment {

    private Activity activity;

    public JokeFragment() {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Joke onCreateView", "Joke onCreateView");
        View view = inflater.inflate(R.layout.activity_main, container, false);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        recylerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recylerView.setLayoutManager(new LinearLayoutManager(activity));
        final HomeAdapter adapter = new HomeAdapter(activity, mData);

        adapter.setItemClickListener(new HomeAdapter.ItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(activity, "click the item which pos is " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(activity, "long click the item which pos is " + position, Toast.LENGTH_SHORT).show();
            }
        });

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
                JokeService jokeService = RetrofitWrapper.getInstance().create(JokeService.class);
                String time = String.valueOf(System.currentTimeMillis() / 1000);
                Call<JokeInfo> call = jokeService.getJokeInfoList(
                        "desc", 1, 10, time, "30d7111054cb9b93a6e02e8940606c26");

                call.enqueue(new Callback<JokeInfo>() {
                    @Override
                    public void onResponse(Call<JokeInfo> call, final Response<JokeInfo> response) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JokeInfo ji = response.body();
                                mData.clear();
                                List<JokeInfo.ResultBean.DataBean> list = ji.getResult().getData();
                                for (JokeInfo.ResultBean.DataBean bean : list) {
                                    mData.add(bean.getContent());
                                }
                                swipeRefreshLayout.setRefreshing(false);
                                handler.sendEmptyMessage(1);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<JokeInfo> call, Throwable t) {
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
        Log.e("JokeFragment", "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("JokeFragment", "onDestroyView");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("JokeFragment", "onPause");
    }
}
