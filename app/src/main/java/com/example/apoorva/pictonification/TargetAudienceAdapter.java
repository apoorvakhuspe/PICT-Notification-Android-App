package com.example.apoorva.pictonification;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TargetAudienceAdapter extends RecyclerView.Adapter<TargetAudienceAdapter.MyViewHolder> {

    private List<TargetAudienceData> targetList;
    Context context;
    public  String Criteria="#";
    public class MyViewHolder extends RecyclerView.ViewHolder {

        Spinner spinner1;
        Spinner spinner2;
        Spinner spinner3;

        public MyViewHolder(View view) {
            super(view);
            spinner1 = (Spinner) view.findViewById(R.id.spinner1);
            spinner2 = (Spinner) view.findViewById(R.id.spinner2);
            spinner3 = (Spinner) view.findViewById(R.id.spinner3);

            spinner2.setVisibility(View.INVISIBLE);
            spinner3.setVisibility(View.INVISIBLE);
        }
    }
    public TargetAudienceAdapter(List<TargetAudienceData> targetList,Context context){
        this.context = context;
        this.targetList = targetList;
    }

    @NonNull
    @Override
    public TargetAudienceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_target,parent,false);
        return new TargetAudienceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TargetAudienceAdapter.MyViewHolder holder, int position) {

        holder.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Toast.makeText(context, "Send To All", Toast.LENGTH_LONG).show();
                    holder.spinner2.setVisibility(View.INVISIBLE);
                    holder.spinner3.setVisibility(View.INVISIBLE);
                    //Criteria+="ALL#";
                } else if (i == 1) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add("COMP");
                    list.add("E&TC");
                    list.add("IT");
                    list.add("Applied Science");

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(context,
                            android.R.layout.simple_spinner_dropdown_item, list);
                    holder.spinner2.setAdapter(dataAdapter2);
                    holder.spinner2.setVisibility(View.VISIBLE);
                    holder.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i < 3) {

                                ArrayList<String> list = new ArrayList<>();
                                list.add("SE");
                                list.add("TE");
                                list.add("BE");
                                ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(context,
                                        android.R.layout.simple_spinner_dropdown_item, list);
                                holder.spinner3.setAdapter(dataAdapter3);
                                holder.spinner3.setVisibility(View.VISIBLE);
                            } else {
                                holder.spinner3.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                } else if (i == 2) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add("FE");
                    list.add("SE");
                    list.add("TE");
                    list.add("BE");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(context,
                            android.R.layout.simple_spinner_dropdown_item, list);
                    holder.spinner2.setAdapter(dataAdapter2);
                    holder.spinner2.setVisibility(View.VISIBLE);
                    holder.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            ArrayList<String> list = new ArrayList<>();
                            list.add("1");
                            list.add("2");
                            list.add("3");
                            list.add("4");
                            list.add("5");
                            list.add("6");
                            list.add("7");
                            list.add("8");
                            list.add("9");
                            list.add("10");
                            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_dropdown_item, list);
                            holder.spinner3.setAdapter(dataAdapter3);
                            holder.spinner3.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                } else if (i == 3) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add("A");
                    list.add("B");
                    list.add("C");
                    list.add("E");
                    list.add("F");
                    list.add("G");
                    list.add("H");
                    list.add("K");
                    list.add("L");
                    list.add("M");
                    list.add("N");
                    list.add("R");
                    list.add("S");
                    list.add("T");
                    list.add("U");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(context,
                            android.R.layout.simple_spinner_dropdown_item, list);
                    holder.spinner2.setAdapter(dataAdapter2);
                    holder.spinner2.setVisibility(View.VISIBLE);
                    holder.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            ArrayList<String> list = new ArrayList<>();
                            list.add("1");
                            list.add("2");
                            list.add("3");
                            list.add("4");
                            list.add("5");
                            list.add("6");
                            list.add("7");
                            list.add("8");
                            list.add("9");
                            list.add("10");
                            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_dropdown_item, list);
                            holder.spinner3.setAdapter(dataAdapter3);
                            holder.spinner3.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        TargetAudienceData targetData = targetList.get(position);
        //holder.image.setText(feedData.getUrl());
        holder.spinner1.getDisplay();//(targetData.getSpinner1());
        //holder.spinner2.setText(targetData.getSpinner2());
        //holder.spinner3.setText(targetData.getSpinner3());

        Criteria+=holder.spinner1.getSelectedItem().toString()+"#";

    }
    @Override
    public int getItemCount() {
        return targetList.size();
    }
}
