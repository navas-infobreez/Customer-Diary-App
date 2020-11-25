package com.plannet.apps.diarybook.adapters;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.activity.PendingDiaryFragment;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.utils.Callback;

import java.util.List;

import static com.plannet.apps.diarybook.DatabaseHandler.APPROVED;
import static com.plannet.apps.diarybook.DatabaseHandler.APPROVERETURN;
import static com.plannet.apps.diarybook.DatabaseHandler.COMPLETED;
import static com.plannet.apps.diarybook.DatabaseHandler.PENDING;
import static com.plannet.apps.diarybook.DatabaseHandler.PICKED;

public class CustomerDiaryAdapter extends RecyclerView.Adapter<CustomerDiaryAdapter.MyViewHolder> {

    private List<CustomerDiaryModel> customerDiaryModels;
    OnCompleteCallBack onCompleteCallBack;
    private List<CustomerModel> customerModels;
    boolean isCustomerList=false;
    Callback callback;

    public CustomerDiaryAdapter(List<CustomerDiaryModel> customerDiaryModelList, Callback callback,OnCompleteCallBack completeCallBack) {
        this.callback = callback;
        this.customerDiaryModels = customerDiaryModelList;
        this.onCompleteCallBack=completeCallBack;
    }

    public CustomerDiaryAdapter(List<CustomerModel> customerModels,boolean isCustomerList, Callback callback) {
       this.isCustomerList=isCustomerList;
        this.customerModels = customerModels;
        this.callback=callback;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView customerName, phoneNo, textDate, address;
        public Button btnpick;
        LinearLayout statusView;


        public MyViewHolder(View view) {
            super( view );
            textDate = (TextView) view.findViewById( R.id.date );
            customerName = (TextView) view.findViewById( R.id.customerName );
            phoneNo = (TextView) view.findViewById( R.id.phoneNo );
            address = (TextView) view.findViewById( R.id.address );
            btnpick = (Button) view.findViewById( R.id.add_button );
            statusView=(LinearLayout)view.findViewById(R.id.status_indication);

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
            holder.phoneNo.setText( eachItem.getCustomerContact().getContactNo() );
            holder.customerName.setText( eachItem.getCustomerName() );
            holder.address.setText( eachItem.getCustomerContact().getAddress1() );
            holder.btnpick.setVisibility( View.GONE );
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onItemClick(eachItem);
                }
            });
        } else {
            if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Sales man" )){
                holder.btnpick.setEnabled(true);
                holder.statusView.setEnabled( true );
            }else {
                holder.btnpick.setEnabled(false);
                holder.statusView.setEnabled( false );
            }

            final CustomerDiaryModel eachItem = customerDiaryModels.get( position );

            if (!eachItem.getStatus().equals(PENDING)){
                holder.btnpick.setBackgroundResource(R.drawable.picked_button);
                holder.btnpick.setTextColor(Color.WHITE);
                holder.btnpick.setText(eachItem.getStatus());
            }else {
                holder.btnpick.setBackgroundResource(R.drawable.approve_button);
                holder.btnpick.setTextColor(Color.WHITE);
                holder.btnpick.setText(eachItem.getStatus());
            }
            if (eachItem.getStatus().equals(COMPLETED)){
                holder.statusView.setBackgroundResource(R.drawable.complete_status);
            }else  if (eachItem.getStatus().equals(APPROVED)){
                holder.statusView.setBackgroundResource(R.drawable.approved_status);
            }else  if (eachItem.getStatus().equals(APPROVERETURN)){
                holder.statusView.setBackgroundResource(R.drawable.reject_status);
            }else {
                holder.statusView.setBackgroundResource(R.drawable.normal_status);
            }
            holder.address.setText( eachItem.getCustomerModel().getCustomerContact().getAddress1() );
            holder.textDate.setText( eachItem.getDate() );
            holder.customerName.setText( eachItem.getCustomerModel().getCustomerName() );
            holder.phoneNo.setText( eachItem.getCustomerModel().getCustomerContact().getContactNo() );


            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCompleteCallBack.onCompleteCallBack(eachItem);
            }
        });

            holder.btnpick.setVisibility( View.VISIBLE );

            if (eachItem.getStatus().equalsIgnoreCase( PENDING )) {
                holder.btnpick.setText( "PICK" );
            } else if (eachItem.getStatus().equalsIgnoreCase( PICKED )) {
                holder.btnpick.setText( "PICKED" );
            } else if (eachItem.getStatus().equalsIgnoreCase( COMPLETED )) {
                holder.btnpick.setText( "COMPLETED" );
            }else if (eachItem.getStatus().equalsIgnoreCase( APPROVED )) {
                holder.btnpick.setText( "APPROVED" );
            }
            holder.btnpick.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eachItem.getStatus().equalsIgnoreCase( PENDING )) {
                        eachItem.setStatus( PICKED );
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
