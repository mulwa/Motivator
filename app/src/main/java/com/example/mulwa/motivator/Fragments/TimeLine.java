package com.example.mulwa.motivator.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mulwa.motivator.Pojo.Message;
import com.example.mulwa.motivator.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLine extends Fragment {
    private RecyclerView mRecyclerView;
    private FirebaseDatabase database;
    private DatabaseReference mref;



    public TimeLine() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_line, container, false);
        intializeUi(view);
        database = FirebaseDatabase.getInstance();


        setUpRecyclerView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Time Line");
        setUpRecyclerView();
    }
    private void intializeUi(View view){
        mRecyclerView = view.findViewById(R.id.message_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }
    private void setUpRecyclerView(){
        mref = database.getReference("Messages");
        Log.d("tag",mref.toString());

        FirebaseRecyclerAdapter<Message, MyViewHolder> mAdapter = new FirebaseRecyclerAdapter<Message, MyViewHolder>(
                Message.class,
                R.layout.message_custom_layout,
                MyViewHolder.class,
                mref

        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Message model, int position) {
                viewHolder.m_owner_name.setText(model.getUserName());
                viewHolder.m_message.setText(model.getMessage());
                Picasso.with(getContext()).load(model.getPhotoUrl()).into(viewHolder.m_icon);

            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView m_message;
        private CircleImageView m_icon;
        private TextView m_owner_name;


        public MyViewHolder(View itemView) {
            super(itemView);
            m_icon = itemView.findViewById(R.id.profile_image);
            m_message = itemView.findViewById(R.id.ed_message);
            m_owner_name = itemView.findViewById(R.id.ed_owner_name);


        }
    }
}
