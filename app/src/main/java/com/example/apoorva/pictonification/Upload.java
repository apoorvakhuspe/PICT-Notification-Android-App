package com.example.apoorva.pictonification;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Upload.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Upload#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Upload extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int PICK_IMAGE = 1;

    String type;
    ImageView imageView;
    Spinner spinner;

    private Uri filePath;
    private EditText title;

    FirebaseStorage storage;
    StorageReference mChatPhotosStorageReference;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    public Upload() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Upload.
     */
    // TODO: Rename and change types and number of parameters
    public static Upload newInstance(String param1, String param2) {
        Upload fragment = new Upload();
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
        mChatPhotosStorageReference = storage.getReference().child("images");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload, container, false);

        ImageView choose = (ImageView) rootView.findViewById(R.id.choose);
        spinner = (Spinner) rootView.findViewById(R.id.type);
        final ImageView upload = (ImageView) rootView.findViewById(R.id.upload);
        ImageView utext = (ImageView) rootView.findViewById(R.id.uploadText);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        title = (EditText) rootView.findViewById(R.id.TitleEditText);
        Button targetAudience = (Button) rootView.findViewById(R.id.selectTargetAudience);

        final EditText entermessage = (EditText) rootView.findViewById(R.id.EnterMessage);

        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter1);

        type = spinner.getSelectedItem().toString();


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!entermessage.getText().toString().equals("")) {
                    type = spinner.getSelectedItem().toString();
                    Toast.makeText(getContext(),type, Toast.LENGTH_SHORT).show();

                    String Message = entermessage.getText().toString();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myfile",Context.MODE_PRIVATE);
                    String name = sharedPreferences.getString("name","");
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                    Date currentLocalTime = cal.getTime();

                    // Add "....:SS" For Seconds and "..... EEE" For Day
                    DateFormat time = new SimpleDateFormat("kk:mm");
                    DateFormat date = new SimpleDateFormat("dd-MM-yyyy");

                    // YOU CAN GET SECONDS BY ADDING  "...:SS" TO IT
                    time.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                    String localTime = time.format(currentLocalTime);
                    String localDate = date.format(currentLocalTime);

                    FeedData feedData = new FeedData(Message,title.getText().toString(),name,localDate + "@" + localTime,type);
                    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Feed");

                    if(type.equals("Notices")) {
                        FirebaseDatabase.getInstance().getReference().child("Feed").child("Notices").push().setValue(feedData);
                    }
                    else if(type.equals("Events"))
                    {
                        FirebaseDatabase.getInstance().getReference().child("Feed").child("Events").push().setValue(feedData);
                    }
                    else if(type.equals("Study Material"))
                    {
                        FirebaseDatabase.getInstance().getReference().child("Feed").child("Study_Material").push().setValue(feedData);
                    }

                    Toast.makeText(getContext(),"Message Uploaded",Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                    Toast.makeText(getContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
                }
            }
        });

        targetAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent targetIntent=new Intent(getActivity(),TargetAudience.class);
                startActivity(targetIntent);
                //((Activity) getActivity()).overridePendingTransition(0,0);
            }
        });



        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            final Uri selectedImageUri = data.getData();
            final StorageReference storageReference = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            storageReference.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    getURL(selectedImageUri);
                }
            });
        }
            else{
                Toast.makeText(getContext(),"No file chosen",Toast.LENGTH_SHORT).show();
            }
    }

    private void getURL(Uri selectedImageUri){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("myfile",Context.MODE_PRIVATE);
        final String name = sharedPreferences.getString("name","");
        mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                Date currentLocalTime = cal.getTime();

                // Add "....:SS" For Seconds and "..... EEE" For Day
                DateFormat time = new SimpleDateFormat("kk:mm");
                DateFormat date = new SimpleDateFormat("dd-MM-yyyy");

                // YOU CAN GET SECONDS BY ADDING  "...:SS" TO IT
                time.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                String localTime = time.format(currentLocalTime);
                String localDate = date.format(currentLocalTime);

                 FeedData feedData = new FeedData(uri.toString(),title.getText().toString(),name,localDate + "@" + localTime,type);
                 if(type.equals("Notices")) {
                      FirebaseDatabase.getInstance().getReference().child("Feed").child("Notices").push().setValue(feedData);
                 }
                 else if(type.equals("Events"))
                 {
                      FirebaseDatabase.getInstance().getReference().child("Feed").child("Events").push().setValue(feedData);
                 }
                 else if(type.equals("Study Material"))
                 {
                      FirebaseDatabase.getInstance().getReference().child("Feed").child("Study_Material").push().setValue(feedData);
                 }
            }
        });
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
