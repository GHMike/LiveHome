package com.ting.a.livehome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ting.a.livehome.R;
import com.ting.a.livehome.bean.MessageInfo;

import java.util.List;

public class MainAdapter extends BaseAdapter {
    private Context mContext;
    private List<MessageInfo> data = null;

    public MainAdapter(Context context, List<MessageInfo> lstImageItem) {
        this.mContext = context;
        this.data = lstImageItem;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public MessageInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<MessageInfo> getData() {
        return data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = null;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            //找控件
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main, null);
            mViewHolder.title = convertView.findViewById(R.id.title);
            mViewHolder.image = convertView.findViewById(R.id.image);
            mViewHolder.context = convertView.findViewById(R.id.context);
            mViewHolder.hot_text = convertView.findViewById(R.id.hot_text);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            //绑定数据
            MessageInfo item = data.get(position);
            mViewHolder.title.setText(item.getNewsName());
            mViewHolder.context.setText(item.getNewsResume());
            mViewHolder.hot_text.setText(item.getNewsHost()+"");//数字类型不能直接赋值;
            try {
                Glide.with(mContext).load(item.getNewsPicUrl().toString()).into(mViewHolder.image);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


    final class ViewHolder {
        TextView title;
        ImageView image;
        TextView context;
        TextView hot_text;

    }
}
