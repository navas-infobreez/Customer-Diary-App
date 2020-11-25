package com.plannet.apps.diarybook.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.models.ProductPriceDto;
import com.plannet.apps.diarybook.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsListAdapter extends RecyclerView.Adapter<ProductDetailsListAdapter.MyViewHolder> {

    private List<ProductModel> productModelList;
    Callback callBack;
    Context context;

    public ProductDetailsListAdapter(List<ProductModel> productModels,Context context, Callback completeCallBack) {

        this.productModelList = productModels;
        this.callBack = completeCallBack;
        this.context =context;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView name,key,cost;
        public Spinner priceList;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.productName);
            cost = (TextView) view.findViewById(R.id.cost);
            key = (TextView) view.findViewById(R.id.search_key);
            priceList = (Spinner) view.findViewById(R.id.priceList);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final ProductModel eachItem = productModelList.get(position);
            holder.name.setText(eachItem.getProduct_name());
            holder.cost.setText("Cost: "+eachItem.getCost_price());
            holder.key.setText(String.valueOf(eachItem.getSearchKey()!=null?eachItem.getSearchKey():""));
            final List<String>priceList=new ArrayList<>();
            for (ProductPriceDto temp:eachItem.getProductPriceDTOList()){
                priceList.add(String.valueOf(temp.getSalesPrice()));
            }
            ArrayAdapter aa = new ArrayAdapter(context , android.R.layout.simple_spinner_item, priceList );
            aa.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            holder.priceList.setAdapter( aa );

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
        if (productModelList != null)
            return productModelList.size();
        else
            return 0;

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

