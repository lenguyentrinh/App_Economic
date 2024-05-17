package com.example.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demo.R;
import com.example.demo.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class DienThoaiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    List <SanPhamMoi> array;
    private static final int VIEW_TYPE_DATA= 0;
    private static final int VIEW_TYPE_LOADING= 1;

    public DienThoaiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType== VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dienthoai,parent,false);
            return new MyViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPhamMoi sanPham = array.get(position);
            myViewHolder.tensp.setText(sanPham.getTensp());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.giasp.setText("Gia:"+decimalFormat.format(Double.parseDouble(sanPham.getGiasp()))+"Đ");
            myViewHolder.mota.setText(sanPham.getMota());
            myViewHolder.idsp.setText(sanPham.getId()+"");
            Glide.with(context).load(sanPham.getHinhanh()).into(myViewHolder.hinhanh);
        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.proccessbar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) ==null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    public class  LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar proccessbar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            proccessbar= itemView.findViewById(R.id.proccessbar);

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tensp, giasp, mota,idsp;
        ImageView hinhanh;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tensp = itemView.findViewById(R.id.itemdt_ten);
            giasp =itemView.findViewById(R.id.itemdt_gia);
            mota = itemView.findViewById(R.id.itemdt_mota);
            idsp = itemView.findViewById(R.id.itemdt_idsp);
            hinhanh=itemView.findViewById(R.id.itemdt_image);
        }
    }
}