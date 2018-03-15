package com.aaa.endlessrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Abbas on 3/15/2018.
 */

public class TwoWayEndlessAdapterImp<VH extends RecyclerView.ViewHolder> extends TwoWayEndlessAdapter<VH, ValueItem> {

    @Override
    public int getItemViewType(int position)
    {
        return R.layout.item_layout;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemViewLayout = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        switch (viewType)
        {
            case R.layout.item_layout:

                return (VH) new ItemLayoutViewHolder(itemViewLayout);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(VH holder, ValueItem item, int position)
    {
        switch (getItemViewType(position)) {
            case R.layout.item_layout:
                ItemLayoutViewHolder viewHolder = (ItemLayoutViewHolder) holder;

                viewHolder.textView.setText(item.data);
                break;
        }
    }
}
