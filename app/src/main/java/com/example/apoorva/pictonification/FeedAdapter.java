package com.example.apoorva.pictonification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder>
{
   private List<FeedData> feedList;
   Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView message;
        TextView name;
        TextView date;
        WebView pdf;
        TouchImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (TouchImageView) view.findViewById(R.id.feedImage);
            title = (TextView) view.findViewById(R.id.feedTitle);
            date = (TextView) view.findViewById(R.id.forumDate);
            message = (TextView) view.findViewById(R.id.forumMessage);
            name = (TextView) view.findViewById(R.id.forumName);
        }
    }

    public FeedAdapter(List<FeedData> feedList,Context context){
        this.context = context;
        this.feedList = feedList;
    }

    @Override
    public FeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view,parent,false);

        return new FeedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedAdapter.MyViewHolder holder, int position) {
        FeedData feedData = feedList.get(position);
        //holder.image.setText(feedData.getUrl());
        holder.title.setText(feedData.getTitle());
        holder.date.setText(feedData.getDate());
        holder.message.setText(feedData.getMessage());
        holder.name.setText(feedData.getName());

        String msg = holder.message.getText().toString().trim();

        if (Patterns.WEB_URL.matcher(msg).matches()) {

            holder.message.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .build();

            ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                    .defaultDisplayImageOptions(defaultOptions)
                    .memoryCache(new WeakMemoryCache())
                    .discCacheSize(100*1024*1024)
                    .build();

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(imageLoaderConfiguration);

            imageLoader.displayImage(feedData.getMessage(), holder.image, defaultOptions);

        return;
        }
        else
        {
            holder.image.setVisibility(View.GONE);
            holder.message.setVisibility(View.VISIBLE);
        }
        //holder.image.setVisibility(View.GONE);

        /*String senderDetails = String.valueOf(feedData.getSenderName());
        senderDetails = senderDetails + "(" + String.valueOf(feedData.getSenderNumber()) + ")";
        holder.Sender.setText(senderDetails);*/
    }
    @Override
    public int getItemCount() {
        return feedList.size();
    }
}
