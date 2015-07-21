package com.example.root.listviewloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 15-7-15.
 */
public class MyAdapater extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<MyItem> mList;
    private LayoutInflater mInflater;
    private ImageLoder mImageLoder;
    private int mStart, mEnd;
    public static String[] URLS;

    public MyAdapater(Context context, List<MyItem> data, ListView listView){

        mList = data;
        mInflater = LayoutInflater.from(context);
        mImageLoder = new ImageLoder(listView);
        URLS = new String[data.size()];
        for (int i = 0; i < data.size(); i++){
            URLS[i] = data.get(i).getUrl();
        }
        listView.setOnScrollListener(this);
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
//        new ImageLoder().showImageByThread(viewHolder.ivIcon,
//                mList.get(position).iconUrl);
        String url = mList.get(position).iconUrl;
        viewHolder.ivIcon.setTag(url);
//        new ImageLoder().showImageByAsyncTask(viewHolder.ivIcon, mList.get(position).iconUrl);
//        mImageLoder.showImageByAsyncTask(viewHolder.ivIcon, mList.get(position).iconUrl);
        mImageLoder.showImageByAsyncTask(viewHolder.ivIcon, url);
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
        if(scrollState == SCROLL_STATE_IDLE){
            mImageLoder.loadImages(mStart, mEnd);
        }else{
            mImageLoder.cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
    }
}
