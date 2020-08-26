package com.plannet.apps.diarybook.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;

import java.util.List;

public class CustomerDiaryAdapter extends RecyclerView.Adapter<CustomerDiaryAdapter.MyViewHolder> {

    private List<CustomerDiaryModel> customerDiaryModels;

    public CustomerDiaryAdapter(List<CustomerDiaryModel> customerDiaryModelList) {

        this.customerDiaryModels = customerDiaryModelList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView customerName, phoneNo, textDate, address;
        public Button add_button;


        public MyViewHolder(View view) {
            super( view );
            textDate = (TextView) view.findViewById( R.id.date );
            customerName = (TextView) view.findViewById( R.id.customerName );
            phoneNo = (TextView) view.findViewById( R.id.phoneNo );
            address = (TextView) view.findViewById( R.id.address );
            add_button = (Button) view.findViewById( R.id.add_button );

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.diary_list_view, parent, false );
        return new MyViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final CustomerDiaryModel eachItem = customerDiaryModels.get( position );
        holder.textDate.setText( eachItem.getDate() );
        holder.customerName.setText( eachItem.getCustomerName() );

    }

    @Override
    public int getItemCount() {
        return customerDiaryModels.size();

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView( recyclerView );
    }

}
