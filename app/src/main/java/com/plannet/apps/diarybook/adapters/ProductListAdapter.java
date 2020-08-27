package com.plannet.apps.diarybook.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.models.CustomerDiaryLineModel;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private List<CustomerDiaryLineModel> customerDiaryModels;
    OnCompleteCallBack callBack;

    public ProductListAdapter(List<CustomerDiaryLineModel> customerDiaryModelList, OnCompleteCallBack completeCallBack) {

        this.customerDiaryModels = customerDiaryModelList;
        this.callBack=completeCallBack;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView name;



        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final CustomerDiaryLineModel eachItem = customerDiaryModels.get(position);
        holder.name.setText(eachItem.getProduct_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onCompleteCallBack(eachItem);
            }
        });


    }

    @Override
    public int getItemCount() {
        return customerDiaryModels.size();

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

