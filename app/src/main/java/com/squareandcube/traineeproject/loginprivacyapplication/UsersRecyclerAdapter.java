package com.squareandcube.traineeproject.loginprivacyapplication;

/**
 * Created by SILAJIT on 11-09-2017.
 */

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {

    private List<CondidateRegisterModel> listUsers;

    public UsersRecyclerAdapter(List<CondidateRegisterModel> listUsers) {
        this.listUsers = listUsers;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.textViewFirstName.setText(listUsers.get(position).getFirstName());
        holder.textViewLastName.setText(listUsers.get(position).getLastName());
        holder.textViewPhoneNo.setText(listUsers.get(position).getPhoneNo());
        holder.textViewDateOfBirth.setText(listUsers.get(position).getDOB());
        holder.textViewEmail.setText(listUsers.get(position).getEmail());
        holder.textViewPassword.setText(listUsers.get(position).getPassword());
    }

    @Override
    public int getItemCount() {
        Log.v(UsersRecyclerAdapter.class.getSimpleName(),""+listUsers.size());
        return listUsers.size();
    }


    /**
     * ViewHolder class
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewFirstName;
        public AppCompatTextView textViewLastName;
        public AppCompatTextView textViewPhoneNo;
        public AppCompatTextView textViewDateOfBirth;
        public AppCompatTextView textViewEmail;
        public AppCompatTextView textViewPassword;

        public UserViewHolder(View view) {
            super(view);
            textViewFirstName = (AppCompatTextView) view.findViewById(R.id.textViewFirstName);
            textViewLastName = (AppCompatTextView) view.findViewById(R.id.textViewLastName);
            textViewPhoneNo = (AppCompatTextView) view.findViewById(R.id.textViewPhoneNo);
            textViewDateOfBirth = (AppCompatTextView) view.findViewById(R.id.textViewDateOfBirth);
            textViewEmail = (AppCompatTextView) view.findViewById(R.id.textViewEmail);
            textViewPassword = (AppCompatTextView) view.findViewById(R.id.textViewPassword);
        }
    }

}
