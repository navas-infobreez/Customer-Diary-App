package com.plannet.apps.diarybook.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.Callback;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private List<UserModel> userModelList;
            Callback callBack;

    public UserListAdapter(List<UserModel> userModels, Callback completeCallBack) {

            this.userModelList = userModels;
            this.callBack = completeCallBack;
            }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name,roll,mob;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.userName);
            roll = (TextView) view.findViewById(R.id.userRoll);
            mob = (TextView) view.findViewById(R.id.phoneNo);

        }
    }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

                final UserModel eachItem = userModelList.get(position);
                holder.name.setText(eachItem.getName());
                holder.roll.setText(String.valueOf(eachItem.getRole_name()));
                if (eachItem.getUserContacts()!=null)
                    holder.mob.setText(String.valueOf(eachItem.getUserContacts().getPhoneNumber()));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callBack.onItemClick(eachItem);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        callBack.onItemLongClick(eachItem);
                        return false;
                    }
                });
            }

        @Override
        public int getItemCount() {
            if (userModelList != null)
                return userModelList.size();
            else
                return 0;

        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
}
