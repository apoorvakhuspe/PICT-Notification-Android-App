package com.example.apoorva.pictonification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class Register extends AppCompatActivity
{

    EditText username,email,password,confirm_password;
    Button reg;
    Spinner spinner1,spinner2,spinner3,spinner4;
    TextView alreg;
    int sign;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        sign = getIntent().getIntExtra("sign", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg =(Button) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);

        alreg = (TextView) findViewById(R.id.alreadyReg);

        spinner1 = (Spinner) findViewById(R.id.year);
        spinner2 = (Spinner) findViewById(R.id.div);
        spinner3 = (Spinner) findViewById(R.id.batch);
        spinner4 = (Spinner) findViewById(R.id.department);

        if(sign==1) {

            ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(Register.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.year));
            myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(myAdapter1);

            ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(Register.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.div));
            myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(myAdapter2);

            ArrayAdapter<String> myAdapter3 = new ArrayAdapter<String>(Register.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.batch));
            myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner3.setAdapter(myAdapter3);

            ArrayAdapter<String> myAdapter4 = new ArrayAdapter<String>(Register.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.department));
            myAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner4.setAdapter(myAdapter4);
        }
        if(sign==2)
        {
            spinner1.setVisibility(View.GONE);
            spinner2.setVisibility(View.GONE);
            spinner3.setVisibility(View.GONE);
            spinner4.setVisibility(View.GONE);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        alreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent arintent = new Intent(Register.this,LogIn.class);
                finish();
                startActivity(arintent);
            }
        });

        reg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String nemail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String cpass = confirm_password.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(nemail).matches()) {
                    email.setError("Enter a valid email");
                    return;
                }

                if (!pass.equals(cpass)) {
                    confirm_password.setError("Password Error");
                    confirm_password.requestFocus();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(nemail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserInformation();

                        } else {

                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }


    void saveUserInformation()
    {
        String year,div,batch,dept;
        String uname = username.getText().toString().trim();
        String emailid = email.getText().toString().trim();
        if(sign==1) {
            year = spinner1.getSelectedItem().toString();
            div = spinner2.getSelectedItem().toString();
            batch = spinner3.getSelectedItem().toString();
            dept = spinner4.getSelectedItem().toString();
        }
        else
        {
            year="";
            div="";
            batch="";
            dept="";
        }
        String pass = password.getText().toString().trim();
        String cpass = confirm_password.getText().toString().trim();

        final UserInformation userInformation = new UserInformation(uname,emailid,year,div,batch,dept,pass,cpass);

        databaseReference.child("User").child(userInformation.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_LONG).show();
                }
                else
                {
                    databaseReference.child("User").child(userInformation.getUsername()).setValue(userInformation);
                   // FirebaseMessaging.getInstance().subscribeToTopic(userInformation.department);
                   // FirebaseMessaging.getInstance().subscribeToTopic(userInformation.year);
                   // FirebaseMessaging.getInstance().subscribeToTopic(userInformation.year+userInformation.div);
                    //FirebaseMessaging.getInstance().subscribeToTopic(userInformation.batch);
                    Intent dashboardintent= new Intent(Register.this,DashboardTeacher.class);
                    startActivity(dashboardintent);
                    Toast.makeText(getApplicationContext(),"User added",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Toast.makeText(this,"User added",Toast.LENGTH_LONG).show();
    }
}

