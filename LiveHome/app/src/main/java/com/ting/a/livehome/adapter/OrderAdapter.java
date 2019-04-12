package com.ting.a.livehome.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ting.a.livehome.R;
import com.ting.a.livehome.bean.MessageInfo;
import com.ting.a.livehome.bean.UserOrderInfo;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context mContext;
    private List<UserOrderInfo> data = null;

    public OrderAdapter(Context context, List<UserOrderInfo> lstImageItem) {
        this.mContext = context;
        this.data = lstImageItem;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public UserOrderInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<UserOrderInfo> getData() {
        return data;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = null;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order, null);

            mViewHolder.title = convertView.findViewById(R.id.title);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            final UserOrderInfo item = data.get(position);

            switch (item.getOrderState()){
                case "-1":
                    mViewHolder.title.setText("订单：" + item.getOrderNo() + "已取消");
                    mViewHolder.title.setTextColor(Color.RED);
                    break;
                case "1":
                    mViewHolder.title.setText("订单：" + item.getOrderNo() + "，等待确认");
                    mViewHolder.title.setTextColor(Color.BLACK);
                    break;
                case "2":
                    mViewHolder.title.setText("订单：" + item.getOrderNo() + "已确认");
                    mViewHolder.title.setTextColor(Color.BLACK);
                    break;
                case "3":
                    mViewHolder.title.setText("订单：" + item.getOrderNo() + "正在为您配送");
                    mViewHolder.title.setTextColor(Color.BLACK);
                    break;
                case "4":
                    mViewHolder.title.setText("订单：" + item.getOrderNo() + "已完成");
                    mViewHolder.title.setTextColor(Color.GREEN);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


    final class ViewHolder {
        TextView title;

    }
}
