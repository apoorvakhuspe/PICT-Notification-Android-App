package com.example.apoorva.pictonification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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


public class LogIn extends AppCompatActivity
{
    Button loginbtn;
    EditText username,password;
    TextView signup,newuser;
    ProgressBar pb;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    TextView cnt;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        loginbtn =(Button) findViewById(R.id.lbtn);
        signup =(TextView) findViewById(R.id.signup);
        newuser =(TextView) findViewById(R.id.newuser);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.pass);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);

        cnt = (TextView)findViewById(R.id.num);
        cnt.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            SharedPreferences sharedPreferences = getSharedPreferences("myfile",Context.MODE_PRIVATE);
            String name = sharedPreferences.getString("name","");

            if(name.contains("EMP"))
                startActivity(new Intent(LogIn.this,DashboardTeacher.class));
            else if((name.contains("C2K"))||(name.contains("I2K"))||(name.contains("E2K")))
                startActivity(new Intent(LogIn.this,Dashboard.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        loginbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String uname = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(uname.isEmpty() || pass.isEmpty()) {
                    if (uname.isEmpty()) {
                        username.setError("Username is required");
                        username.requestFocus();
                    }

                    if (pass.isEmpty()) {
                        password.setError("Password is required");
                        password.requestFocus();
                    }
                }
                else
                {
                    pb.setVisibility(View.VISIBLE);
                    checkUserCredentials();
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pb.setVisibility(View.VISIBLE);
                Intent regintent=new Intent(LogIn.this,Register.class);
                regintent.putExtra("sign",1);
                startActivity(regintent);
                pb.setVisibility(View.GONE);
            }
        });

        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                Intent regintent=new Intent(LogIn.this,Register.class);
                regintent.putExtra("sign",2);
                startActivity(regintent);
                pb.setVisibility(View.GONE);
            }
        });
    }

    void checkUserCredentials()
    {
        final String uname = username.getText().toString().trim();
        final String pass = password.getText().toString().trim();

        final UserInformation userInformation = new UserInformation(uname,pass);

        databaseReference.child("User").child(userInformation.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    UserInformation userInformation1 = dataSnapshot.getValue(UserInformation.class);

                    firebaseAuth.signInWithEmailAndPassword(userInformation1.getEmail_id(), pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Intent dashboardintent = null;

                                if((uname.contains("C2K"))||(uname.contains("I2K"))||(uname.contains("E2K")))
                                {
                                    dashboardintent = new Intent(LogIn.this, Dashboard.class);

                                }
                                else if(uname.contains("EMP"))
                                {
                                    dashboardintent = new Intent(LogIn.this, DashboardTeacher.class);
                                }

                                SharedPreferences sharedPreferences = getSharedPreferences("myfile", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("name",uname);
                                editor.commit();
                                editor.apply();
                                finish();
                                startActivity(dashboardintent);



                            } else {
                                pb.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();
                                cnt.setVisibility(View.VISIBLE);
                                //cnt.setBackgroundColor(Color.RED);
                                counter--;
                                cnt.setText(Integer.toString(counter));

                                if (counter == 0) {
                                    loginbtn.setEnabled(false);
                                }
                            }
                        }
                    });
                }
                else
                {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"User does not exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();

    }
}