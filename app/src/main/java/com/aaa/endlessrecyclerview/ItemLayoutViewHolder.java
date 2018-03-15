package com.aaa.endlessrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Abbas on 2/7/2018.
 */

public class ItemLayoutViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public ItemLayoutViewHolder(View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.text_view);
    }
}
