package com.example.user_school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class list_schools extends AppCompatActivity implements Adapter_recViewSchool_list.onItemClickListener {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerView;
    TextInputLayout searchBar;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schools);
        searchBar=findViewById(R.id.search_bar);
        recyclerView=findViewById(R.id.recViewSchoolsList);
        setupRecyclerView();
        searchBar.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty())
                {
                    query= db.collection("schools");
                }
                else
                {
                    query= db.collection("schools").orderBy("city")
                        .startAt(s.toString())
                        .endAt(s.toString() + "\uf8ff");

                }
                FirestoreRecyclerOptions<data_schools> options=new FirestoreRecyclerOptions.Builder<data_schools>()
                        .setQuery(query,data_schools.class).build();
                adapter.updateOptions(options);
            }
        });

    }

    private void setupRecyclerView() {
        query= db.collection("schools");
        FirestoreRecyclerOptions<data_schools> options=new FirestoreRecyclerOptions.Builder<data_schools>()
                .setQuery(query,data_schools.class).build();
        adapter = new Adapter_recViewSchool_list(options,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        String school_uid=documentSnapshot.getId();
        Intent intent=new Intent(getApplicationContext(),School_page.class);
        intent.putExtra("school_uid",school_uid);
        startActivity(intent);
    }
}