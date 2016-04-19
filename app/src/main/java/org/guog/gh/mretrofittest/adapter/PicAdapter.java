package org.guog.gh.mretrofittest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.guog.gh.mretrofittest.R;

import java.util.List;

/**
 * Created by gh on 16/4/18.
 */

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.MyViewHolder> {

    private Context context;
    private List<String> mData;
    private List<String> mData_pic;
    private int width;

    public PicAdapter(Context context, List<String> mData,List<String> mData_pic,int width) {
        this.context = context;
        this.mData = mData;
        this.mData_pic = mData_pic;
        this.width = width;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public PicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_pic, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(PicAdapter.MyViewHolder holder, int position) {
        holder.tv.setText(mData.get(position));
//        holder.iv.setImageURI();

        Uri uri = Uri.parse(mData_pic.get(position));
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();
        holder.iv.setController(draweeController);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,width);
        params.addRule(RelativeLayout.BELOW,R.id.AdPic_title);
        holder.iv.setLayoutParams(params);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        SimpleDraweeView iv ;
        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.AdPic_title);
            iv = (SimpleDraweeView)view.findViewById(R.id.AdPic_iv);
        }
    }

}
