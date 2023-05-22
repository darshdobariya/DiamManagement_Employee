package com.example.mobileauthentication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileauthentication.Demo.DataAdd;
import com.example.mobileauthentication.R;

import java.util.Collections;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.examViewHolder> {

    List<Object> list ;
    Context context;

    public DataAdapter(List<DataAdd> list, Context context)
    {
        this.context = context;
    }

    @Override
    public examViewHolder
    onCreateViewHolder(ViewGroup parent,
                       int viewType)
    {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View photoView = inflater.inflate( R.layout.dialog_add_data, parent, false);

        examViewHolder viewHolder = new examViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull examViewHolder holder, int position) {

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class examViewHolder extends RecyclerView.ViewHolder {
        public examViewHolder(@NonNull View itemView) {
            super( itemView );
        }
    }
}

