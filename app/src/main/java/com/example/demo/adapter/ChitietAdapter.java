package com.example.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demo.R;
import com.example.demo.model.Item;

import java.util.List;


public class ChitietAdapter extends RecyclerView.Adapter<ChitietAdapter.MyViewHolder> {
    Context context;
    List<Item> itemList;

    public ChitietAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtten.setText(item.getTensp()+"");
        holder.txtsoluong.setText("Quantity: "+item.getSoluong());
        Glide.with(context).load(item.getHinhanh()).into(holder.imagechitiet);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imagechitiet;
        TextView txtten, txtsoluong;
        public MyViewHolder(View itemView) {
            super(itemView);
            imagechitiet= itemView.findViewById(R.id.item_imgchitiet);
            txtten  = itemView.findViewById(R.id.item_tenspchititet);
            txtsoluong= itemView.findViewById(R.id.item_soluongchititet);
        }
    }
}
