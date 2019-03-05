package com.example.apoorva.pictonification;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class TargetAudience extends Activity{
    Spinner s1,s2,s3;

    private List<TargetAudienceData> targetList = new ArrayList<>();
    private RecyclerView targetRecyclerView;
    private TargetAudienceAdapter targetAudienceAdapter;
    String criteria="#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_audience);


        targetRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_target);


        targetAudienceAdapter = new TargetAudienceAdapter(targetList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        targetRecyclerView.setLayoutManager(layoutManager);
        targetRecyclerView.setItemAnimator(new DefaultItemAnimator());
        targetRecyclerView.setAdapter(targetAudienceAdapter);
        targetList.add(new TargetAudienceData());
        targetAudienceAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action

                targetList.add(new TargetAudienceData());
                targetAudienceAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),targetAudienceAdapter.Criteria,Toast.LENGTH_SHORT).show();

            }
        });
    }

}
