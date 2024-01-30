package com.example.habittracker.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Model.HabitModel;
import com.example.habittracker.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.MyViewHolder> {

    private List<HabitModel> habitList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    public HabitAdapter(MainActivity mainActivity , List<HabitModel> habitList) {
        this.habitList = habitList;
        activity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task , parent , false);
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        HabitModel habitModel = habitList.get(position);
        holder.mCheckBox.setText(habitModel.getHabit());
        holder.mStartDate.setText(" Started On " + habitModel.getStart());

        holder.mCheckBox.setChecked(toBoolean(habitModel.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    firestore.collection("task").document(habitModel.HabitId).update("status", 1);
                } else {
                    firestore.collection("task").document(habitModel.HabitId).update("status", 0);
                }
            }
        });
    }

    private boolean toBoolean(int status) {
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mStartDate;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mStartDate = itemView.findViewById(R.id.mStartDate);
            mCheckBox = itemView.findViewById(R.id.mCheckBox);
        }
    }
}
