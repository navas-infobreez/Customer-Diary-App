package com.plannet.apps.diarybook.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.models.CustomerDiaryLineModel;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;

import java.util.List;

public class DiaryLineAdapter extends RecyclerView.Adapter<DiaryLineAdapter.MyViewHolder> {

    private List<CustomerDiaryLineModel> customerDiaryLineModels;
    OnCompleteCallBack callBack;

    public DiaryLineAdapter(List<CustomerDiaryLineModel> customerDiaryLineModels, OnCompleteCallBack completeCallBack) {

        this.customerDiaryLineModels = customerDiaryLineModels;
        this.callBack = completeCallBack;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView name,qty,category,amount,details,space;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            qty = (TextView) view.findViewById(R.id.qty);
            category = (TextView) view.findViewById(R.id.category);
            amount = (TextView) view.findViewById(R.id.amount);
            details = (TextView) view.findViewById(R.id.details);
            space = (TextView) view.findViewById(R.id.space);


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_line_list_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        if (position!=0) {
            final CustomerDiaryLineModel eachItem = customerDiaryLineModels.get(position-1);
            holder.name.setText(eachItem.getProduct_name());
            holder.qty.setText(String.valueOf(eachItem.getQty()));
            holder.amount.setText(String.valueOf(eachItem.getPrice()));
            holder.category.setText(eachItem.getCategory());
            if (eachItem.getDetails()!=null) {
                holder.details.setVisibility(View.VISIBLE );
                holder.space.setVisibility(View.INVISIBLE);
                holder.details.setText(eachItem.getDetails());
            }else {
                holder.details.setVisibility(View.GONE);
                holder.space.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onCompleteCallBack(eachItem);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (customerDiaryLineModels != null)
            return customerDiaryLineModels.size();
        else
            return 0;

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
