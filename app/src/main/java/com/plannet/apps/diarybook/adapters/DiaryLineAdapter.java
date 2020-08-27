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


        public TextView name,qty;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            qty = (TextView) view.findViewById(R.id.qty);

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

        final CustomerDiaryLineModel eachItem = customerDiaryLineModels.get(position);
        holder.name.setText(eachItem.getProduct_name());
        holder.qty.setText(String.valueOf(eachItem.getQty()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onCompleteCallBack(eachItem);
            }
        });


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
