package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.habittracker.Adapter.HabitAdapter;
import com.example.habittracker.Model.HabitModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import com.google.firebase.firestore.EventListener;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FloatingActionButton button;
    TextView textView;
    FirebaseUser user;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseFirestore firestore;
    HabitAdapter adapter;
    List<HabitModel> mList;
    Query query;
    ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycerlview);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        firestore = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewHabit.newInstance().show(getSupportFragmentManager() , AddNewHabit.TAG);
            }
        });

        mList = new ArrayList<>();
        adapter = new HabitAdapter(MainActivity.this, mList);

        recyclerView.setAdapter(adapter);
        showData();

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showData(){
        query = firestore.collection("task").orderBy("time" , Query.Direction.DESCENDING);

        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        String id = documentChange.getDocument().getId();
                        HabitModel habitModel = documentChange.getDocument().toObject(HabitModel.class).withId(id);
                        if (habitModel.getUser().equals(user.getEmail())) {
                            mList.add(habitModel);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                Collections.reverse(mList);

            }
        });
    }
}