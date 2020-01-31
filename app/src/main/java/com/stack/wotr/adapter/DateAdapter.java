package com.stack.wotr.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stack.wotr.R;
import com.stack.wotr.interfaces.OnRecyclerViewClick;
import com.stack.wotr.model.Item;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = DateAdapter.class.getSimpleName();
    protected List<Item> mOriginalData = new ArrayList<>();
    protected List<Item> mResultData = new ArrayList<>();

    protected Activity mActivity;
    private ItemFilter mFilter = new ItemFilter();
    OnRecyclerViewClick mOnRecyclerViewClick;
    int mLastPosition = 0;

    public DateAdapter(Activity activity, OnRecyclerViewClick mOnRecyclerViewClick) {
        mActivity = activity;
        this.mOnRecyclerViewClick = mOnRecyclerViewClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_date,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Item data = mResultData.get(position);
        try {
            final ViewHolder viewHolder = (ViewHolder) holder;

            if (data != null) {
                viewHolder.textItemName.setText(data.getTitle());
                if (data.isCheck()) {
                    viewHolder.textItemName.setTextColor(ContextCompat.getColor(mActivity, android.R.color.white));
                    viewHolder.textItemName.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.rounded_circle_check));
                } else {
                    viewHolder.textItemName.setTextColor(ContextCompat.getColor(mActivity, R.color.colorTextSecond));
                    viewHolder.textItemName.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.rounded_circle));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mResultData.size();
    }

    public void addItem(Item data) {

        mOriginalData.add(data);
        mResultData.add(data);
        int index = mOriginalData.indexOf(data);
        notifyItemInserted(index);
        if (data.isCheck()) {
            mLastPosition = index;
        }
    }

    public void addAllItem(List<Item> data) {
        mOriginalData = data;
        mResultData = data;
        notifyDataSetChanged();
    }

    public void removeItem(int index) {
        mOriginalData.remove(index);
        mResultData.remove(index);
        notifyItemRemoved(index);
    }

    public void removeItem(Item data) {
        int index = mOriginalData.indexOf(data);
        mOriginalData.remove(data);
        mResultData.remove(data);
        notifyItemRemoved(index);
    }

    public Filter getFilter() {
        return mFilter;
    }


    public Item getItem(int index) {
        return mResultData.get(index);
    }

    public int getLastPosition() {
        return mLastPosition;
    }

    public List<Item> getAllItems() {
        return mResultData;
    }

    public void replace(int i, Item Item) {
        if (Item.isCheck()){
            mLastPosition = i;
        }

        mOriginalData.set(i, Item);
        notifyItemChanged(i);
    }

    public void removeAll() {
        mResultData.clear();
        mOriginalData.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        LinearLayout rootView;
        TextView textItemName;

        public ViewHolder(View itemView) {
            super(itemView);

            textItemName = itemView.findViewById(R.id.textTitle);
            rootView = itemView.findViewById(R.id.rootViewDate);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnRecyclerViewClick.onItemClick(rootView, getLayoutPosition());
                }
            });
        }
    }


    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            int count = mOriginalData.size();

            final ArrayList<Item> tempFilterList = new ArrayList<Item>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = mOriginalData.get(i).getTitle();
                if (filterableString.toLowerCase().contains(filterString)) {
                    tempFilterList.add(mOriginalData.get(i));
                }
            }

            results.values = tempFilterList;
            results.count = tempFilterList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mResultData.clear();
            mResultData = (ArrayList<Item>) results.values;
            notifyDataSetChanged();
        }
    }


}