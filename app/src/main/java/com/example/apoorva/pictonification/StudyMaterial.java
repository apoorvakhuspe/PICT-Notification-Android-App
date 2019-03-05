package com.example.apoorva.pictonification;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudyMaterial.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudyMaterial#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyMaterial extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<FeedData> feedList = new ArrayList<>();
    private RecyclerView feedRecyclerView;
    private FeedAdapter feedAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public StudyMaterial() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudyMaterial.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyMaterial newInstance(String param1, String param2) {
        StudyMaterial fragment = new StudyMaterial();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_study_material, container, false);
        feedRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Feed").child("Study_Material");
        childEventListener=null;

        feedAdapter = new FeedAdapter(feedList,getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        feedRecyclerView.setLayoutManager(layoutManager);
        feedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        feedRecyclerView.setAdapter(feedAdapter);

        attachDatabaseReadListener();

        //return inflater.inflate(R.layout.fragment_events, container, false);
        return rootView;
    }

    public void attachDatabaseReadListener(){
        if(childEventListener==null){
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
                    mBuilder.setSmallIcon(R.drawable.ic_action_notification);
                    mBuilder.setContentTitle("Pictonification");
                    mBuilder.setContentText("New study material uploaded");
                    long[] v = {500,1000};
                    mBuilder.setVibrate(v);
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mBuilder.setSound(uri);
                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0,mBuilder.build());
                    FeedData feedData = dataSnapshot.getValue(FeedData.class);
                    feedList.add(0, feedData);
                    feedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FeedData feedData = dataSnapshot.getValue(FeedData.class);
                    feedList.add(feedData);
                    feedAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FeedData feedData = dataSnapshot.getValue(FeedData.class);
                    feedList.add(feedData);
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
