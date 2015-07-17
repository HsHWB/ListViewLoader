package com.example.root.listviewloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 15-7-15.
 */
public class MyAdapater extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<MyItem> mList;
    private LayoutInflater mInflater;

    public MyAdapater(Context context, List<MyItem> data){

        mList = data;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_layout, null);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.imageView_item);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.textview_context_item);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.textview_title_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        viewHolder.tvTitle.setText(mList.get(position).title);
        viewHolder.tvContent.setText(mList.get(position).context);

        return convertView;
    }

    class ViewHolder{
        public TextView tvTitle, tvContent;
        public ImageView ivIcon;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
