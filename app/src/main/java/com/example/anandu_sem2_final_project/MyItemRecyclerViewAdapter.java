package com.example.anandu_sem2_final_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.anandu_sem2_final_project.Database.UserData;
import com.example.anandu_sem2_final_project.databinding.FragmentItemBinding;

import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<UserData> mValues = null;

    private CallbackDelegate delegate;
    public MyItemRecyclerViewAdapter(CallbackDelegate delegate,List<UserData> items) {
        this.delegate = delegate;
        mValues = items;
    }

    public List<UserData> getmValues() {
        return mValues;
    }

    public void setmValues(List<UserData> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.nameText.setText(mValues.get(position).getName());
        holder.binding.countryText.setText(mValues.get(position).getCountry());
        holder.binding.listItem.setTag(position);
        Bitmap selectedImage = BitmapFactory.decodeByteArray(mValues.get(position).getImage(),0,mValues.get(position).getImage().length);
        holder.binding.image.setImageBitmap(selectedImage);
        Log.i("anandu", "for position " + position + " name " + mValues.get(position).getName() +
                " country " + mValues.get(position).getCountry());

        holder.binding.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.callback(v, (Integer) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mValues != null)
            count = mValues.size();
        Log.i("anandu","Count is "+count);
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FragmentItemBinding binding;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}