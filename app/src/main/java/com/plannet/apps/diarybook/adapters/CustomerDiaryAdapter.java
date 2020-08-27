package com.plannet.apps.diarybook.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.activity.PendingDiaryFragment;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.utils.Callback;

import java.util.List;

public class CustomerDiaryAdapter extends RecyclerView.Adapter<CustomerDiaryAdapter.MyViewHolder> {

    private List<CustomerDiaryModel> customerDiaryModels;
    OnCompleteCallBack callBack;
    private List<CustomerModel> customerModels;
    boolean isCustomerList=false;
    Callback callback;

    public CustomerDiaryAdapter(List<CustomerDiaryModel> customerDiaryModelList, Callback callback,OnCompleteCallBack completeCallBack) {
        this.callback = callback;
        this.customerDiaryModels = customerDiaryModelList;
        this.callBack=completeCallBack;
    }

    public CustomerDiaryAdapter(List<CustomerModel> customerModels,boolean isCustomerList) {
       this.isCustomerList=isCustomerList;
        this.customerModels = customerModels;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView customerName, phoneNo, textDate, address;
        public Button btnpick;


        public MyViewHolder(View view) {
            super( view );
            textDate = (TextView) view.findViewById( R.id.date );
            customerName = (TextView) view.findViewById( R.id.customerName );
            phoneNo = (TextView) view.findViewById( R.id.phoneNo );
            address = (TextView) view.findViewById( R.id.address );
            btnpick = (Button) view.findViewById( R.id.add_button );

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
        if (isCustomerList) {
            final CustomerModel eachItem = customerModels.get( position );
            holder.phoneNo.setText( eachItem.getPhone_no() );
            holder.customerName.setText( eachItem.getCustomerName() );
            holder.btnpick.setText( eachItem.getAddress1() );
            holder.btnpick.setVisibility( View.GONE );
        } else {
            final CustomerDiaryModel eachItem = customerDiaryModels.get( position );

            holder.textDate.setText( eachItem.getDate() );
            holder.customerName.setText( eachItem.getCustomerName() );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onCompleteCallBack(eachItem);
            }
        });

            holder.btnpick.setVisibility( View.VISIBLE );

            if (eachItem.getStatus().equalsIgnoreCase( PendingDiaryFragment.PENDING )) {
                holder.btnpick.setText( "PICK" );
            } else if (eachItem.getStatus().equalsIgnoreCase( PendingDiaryFragment.PICKED )) {
                holder.btnpick.setText( "PICKED" );
            } else if (eachItem.getStatus().equalsIgnoreCase( PendingDiaryFragment.COMPLETED )) {
                holder.btnpick.setText( "COMPLETED" );
            }
            holder.btnpick.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eachItem.getStatus().equalsIgnoreCase( PendingDiaryFragment.PENDING )) {
                        eachItem.setStatus( PendingDiaryFragment.PICKED );
                    } else if (eachItem.getStatus().equalsIgnoreCase( PendingDiaryFragment.PICKED )) {
                        eachItem.setStatus( PendingDiaryFragment.COMPLETED );
                    } else if (eachItem.getStatus().equalsIgnoreCase( PendingDiaryFragment.COMPLETED )) {
                        eachItem.setStatus( PendingDiaryFragment.APPROVED );
                    }
                    callback.onItemClick( eachItem );
                }
            } );
        }
    }

    @Override
    public int getItemCount() {
        if (isCustomerList){
            return customerModels.size();
        }else {
            return customerDiaryModels.size();
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView( recyclerView );
    }

}
