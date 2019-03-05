package com.example.apoorva.pictonification;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Forum.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Forum#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Forum extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<FeedData> forumList = new ArrayList<>();
    private RecyclerView forumRecyclerView;
    private ForumAdapter feedAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    FirebaseStorage storage;
    EditText forumMessage,forumAnswer;
    ImageView uploadforum,arrow;
    TextView answerText;

    public Forum() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Forum.
     */
    // TODO: Rename and change types and number of parameters
    public static Forum newInstance(String param1, String param2) {
        Forum fragment = new Forum();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_forum, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myfile", Context.MODE_PRIVATE);;
        String name = sharedPreferences.getString("name","");
        forumRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_forum);

        answerText = (TextView) rootView.findViewById(R.id.answertext);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Feed").child("Forum");
        childEventListener=null;

        feedAdapter = new ForumAdapter(forumList,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        forumRecyclerView.setLayoutManager(layoutManager);
        forumRecyclerView.setItemAnimator(new DefaultItemAnimator());
        forumRecyclerView.setAdapter(feedAdapter);

        forumMessage = (EditText) rootView.findViewById(R.id.forumMessage);
        uploadforum = (ImageView) rootView.findViewById((R.id.uploadforum));
        arrow= (ImageView) rootView.findViewById(R.id.arrow);
        forumAnswer= (EditText) rootView.findViewById(R.id.answer);

        if(name.contains("EMP"))
        {
            forumMessage.setVisibility(View.GONE);
            uploadforum.setVisibility(View.GONE);
        }

        attachDatabaseReadListener();

        uploadforum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Message = forumMessage.getText().toString();
                Message= "QUESTION : " + Message;
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myfile", Context.MODE_PRIVATE);
                String name = sharedPreferences.getString("name", "");
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                Date currentLocalTime = cal.getTime();

                // Add "....:SS" For Seconds and "..... EEE" For Day
                DateFormat time = new SimpleDateFormat("kk:mm");
                DateFormat date = new SimpleDateFormat("dd-MM-yyyy");

                // YOU CAN GET SECONDS BY ADDING  "...:SS" TO IT
                time.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                String localTime = time.format(currentLocalTime);
                String localDate = date.format(currentLocalTime);

                FeedData feedData = new FeedData(Message, name, localDate + "@" + localTime,false,"");
                //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Feed");

                FirebaseDatabase.getInstance().getReference().child("Feed").child("Forum").push().setValue(feedData);
                }});
        /*arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forumAnswer.setVisibility(View.GONE);
                String Answer = forumAnswer.getText().toString();
                answerText.setText("ANSWER: "+Answer);
                answerText.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Message Uploaded", Toast.LENGTH_SHORT).show();
            }});
*/


            return rootView;
        //return inflater.inflate(R.layout.fragment_forum, container, false);
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
                    Toast.makeText(getContext(),databaseError.toString(), Toast.LENGTH_SHORT).show();
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
