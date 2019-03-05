package com.example.apoorva.pictonification;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;


public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.MyViewHolder>
{
    private List<FeedData> feedList;
    boolean check=false;
    Context context;
    private ChildEventListener childEventListener;
    private ForumAdapter feedAdapter;
    private List<FeedData> forumList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private RecyclerView forumRecyclerView;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        TextView name;
        TextView date, answerText,flag;
        EditText forumAnswer;
        ImageView arrow;


        public MyViewHolder(View view) {
            super(view);

            forumRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_forum);

            date = (TextView) view.findViewById(R.id.forumDate);
            message = (TextView) view.findViewById(R.id.forumMessage);
            name = (TextView) view.findViewById(R.id.forumName);
            forumAnswer = (EditText) view.findViewById(R.id.answer);
            arrow = (ImageView) view.findViewById(R.id.arrow);
            answerText = (TextView) view.findViewById(R.id.answertext);
            flag = (TextView) view.findViewById(R.id.flag);

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("Feed").child("Forum");
            childEventListener=null;

            feedAdapter = new ForumAdapter(forumList,context);
        }
    }

    public ForumAdapter(List<FeedData> feedList,Context context){

        this.context = context;
        this.feedList = feedList;
        //sharedPreferences = context.getSharedPreferences("myfile", Context.MODE_PRIVATE);
    }



@NonNull
    @Override
    public ForumAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_forum,parent,false);

        return new ForumAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ForumAdapter.MyViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("myfile", Context.MODE_PRIVATE);;
        String name = sharedPreferences.getString("name","");

        final FeedData feedData = feedList.get(position);
        holder.date.setText(feedData.getDate());
        holder.message.setText(feedData.getMessage());
        holder.name.setText(feedData.getName());
        holder.answerText.setText(feedData.getAnswer());
        check= feedData.getFlag();
        //holder.flag.setText(feedData.getFlag());
        holder.message.setVisibility(View.VISIBLE);

        boolean t=name.contains("EMP");
        /*
        if(t)
        {
            holder.arrow.setVisibility(View.VISIBLE);
            holder.forumAnswer.setVisibility(View.VISIBLE);
            if(check==true)
            {
                holder.forumAnswer.setVisibility(View.GONE);
                holder.arrow.setVisibility(View.GONE);
                holder.answerText.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.forumAnswer.setVisibility(View.VISIBLE);
                holder.arrow.setVisibility(View.VISIBLE);
                holder.answerText.setVisibility(View.GONE);
            }
        }
        else
        {
            holder.arrow.setVisibility(View.GONE);
            holder.forumAnswer.setVisibility(View.GONE);
            if(check==true)
            {
                //holder.forumAnswer.setVisibility(View.GONE);
                //holder.arrow.setVisibility(View.GONE);
                holder.answerText.setVisibility(View.VISIBLE);
            }
            else
            {
               // holder.forumAnswer.setVisibility(View.VISIBLE);
                //holder.arrow.setVisibility(View.VISIBLE);
                holder.answerText.setVisibility(View.GONE);
            }
        }*/
        if(t && check==true)
        {
            holder.forumAnswer.setVisibility(View.GONE);
            holder.arrow.setVisibility(View.GONE);
            holder.answerText.setVisibility(View.VISIBLE);
        }
        else if(t && check==false)
        {
            holder.forumAnswer.setVisibility(View.VISIBLE);
            holder.arrow.setVisibility(View.VISIBLE);
            holder.answerText.setVisibility(View.GONE);
        }
        else if(!t && check==true)
        {
            holder.arrow.setVisibility(View.GONE);
            holder.forumAnswer.setVisibility(View.GONE);
            holder.answerText.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.arrow.setVisibility(View.GONE);
            holder.forumAnswer.setVisibility(View.GONE);
            holder.answerText.setVisibility(View.GONE);
        }
        /*
        if(check==true)
        {
            holder.forumAnswer.setVisibility(View.GONE);
            holder.arrow.setVisibility(View.GONE);
            holder.answerText.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.forumAnswer.setVisibility(View.VISIBLE);
            holder.arrow.setVisibility(View.VISIBLE);
            holder.answerText.setVisibility(View.GONE);
        }*/

        attachDatabaseReadListener();

        holder.arrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /// button click event

                holder.forumAnswer.setVisibility(View.GONE);
                holder.arrow.setVisibility(View.GONE);
                String Answer = holder.forumAnswer.getText().toString();
                feedData.flag=true;



                holder.answerText.setText("ANSWER: "+Answer);
                //added vishakha
                feedData.answer="ANSWER : " + Answer;
                //holder.answerText.setTextColor(000);
                holder.answerText.setVisibility(View.VISIBLE);


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("Feed").child("Forum").orderByChild("message").equalTo(feedData.getMessage());

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });




                //FeedData feedData = new FeedData(message, name, localDate + "@" + localTime,true,Answer);
                //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Feed");
                //FirebaseDatabase.getInstance().getReference().child("Feed").child("Forum").child(feedData.getAnswer()).removeValue();
                ref.child("Feed").child("Forum").push().setValue(feedData);
            }
        });

    }
    public void attachDatabaseReadListener(){
        if(childEventListener==null){
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FeedData feedData = dataSnapshot.getValue(FeedData.class);
                    forumList.add(0, feedData);
                    feedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FeedData feedData = dataSnapshot.getValue(FeedData.class);
                    forumList.add(feedData);
                    feedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FeedData feedData = dataSnapshot.getValue(FeedData.class);
                    forumList.add(feedData);
                    feedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context,databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }

    public void detachDatabaseListener(){
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }

    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}
