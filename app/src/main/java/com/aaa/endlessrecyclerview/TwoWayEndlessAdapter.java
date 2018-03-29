package com.aaa.endlessrecyclerview;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;

import com.aaa.endlessrecyclerview.utils.EndlessLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The {@link TwoWayEndlessAdapter} class provides an implementation to manage two end data
 * insertion into a {@link RecyclerView} easy by handling all of the logic within.
 * <p>To implement a TwoWayEndlessAdapter simply extend from it, provide the class type parameter
 * of the data type and <code>Override onBindViewHolder(ViewHolder, DataItem, int)</code> to bind
 * the view to.</p>
 *
 * @param <DataItem> A class type that can used by the data adapter.
 * @version 1.0.0
 * @author Abbas
 * @see android.support.v7.widget.RecyclerView.Adapter
 * @see TwoWayEndlessAdapterImp
 */

public abstract class TwoWayEndlessAdapter<VH extends RecyclerView.ViewHolder, DataItem> extends RecyclerView.Adapter<VH> {

    /*
    * Data Adapter Container.
    * */
    protected List<DataItem> data;

    private Callback mEndlessCallback = null;

    /*
    * Number of items before the last to get the lazy loading callback to load more items.
    * */
    private int bottomAdvanceCallback = 0;

    private boolean isFirstBind = true;

    /**
     * @param callback A listener to set if want to receive bottom and top reached callbacks.
     * @see TwoWayEndlessAdapter.Callback
     */
    public void setEndlessCallback(Callback callback)
    {
        mEndlessCallback = callback;
    }

    /**
     * Appends the provided list at the bottom of the {@link RecyclerView}
     *
     * @param bottomList The list to append at the bottom of the {@link RecyclerView}
     */
    public void addItemsAtBottom(ArrayList<DataItem> bottomList)
    {
        if (data == null) {
            throw new NullPointerException("Data container is `null`. Are you missing a call to setDataContainer()?");
        }

        if (bottomList == null || bottomList.isEmpty()) {
            return;
        }

        int adapterSize = getItemCount();

        data.addAll(adapterSize, bottomList);

        notifyItemRangeInserted(adapterSize, adapterSize + bottomList.size());
    }

    /**
     * Prepends the provided list at the top of the {@link RecyclerView}
     *
     * @param topList The list to prepend at the bottom of the {@link RecyclerView}
     */
    public void addItemsAtTop(ArrayList<DataItem> topList)
    {
        if (data == null) {
            throw new NullPointerException("Data container is `null`. Are you missing a call to setDataContainer()?");
        }

        if (topList == null || topList.isEmpty()) {
            return;
        }

        Collections.reverse(topList);
        data.addAll(0, topList);

        notifyItemRangeInserted(0, topList.size());
    }

    /**
     * To call {@link TwoWayEndlessAdapter.Callback#onBottomReached()} before the exact number of items to when the bottom is reached.
     * @see this.bottomAdvanceCallback
     * @see Callback
     * */
    public void setBottomAdvanceCallback(int bottomAdvance)
    {
        if (bottomAdvance < 0) {
            throw new IndexOutOfBoundsException("Invalid index, bottom index must be greater than 0");
        }

        bottomAdvanceCallback = bottomAdvance;
    }

    /**
     * Provide an instance of {@link Map} where the data will be stored.
     * */
    public void setDataContainer(List<DataItem> data)
    {
        this.data = data;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which
     * will have the updated adapter position.
     *
     * Any class that extends from {@link TwoWayEndlessAdapter} should not Override this method but
     * should Override {@link #onBindViewHolder(VH, DataItem, int)} instead.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(VH holder, int position)
    {
        EndlessLogger.logD("onBindViewHolder() for position : " + position);

        onBindViewHolder(holder, data.get(position), position);

        if (position == 0 && !isFirstBind) {
            notifyTopReached();
        }
        else if ((position + bottomAdvanceCallback) >= (getItemCount() - 1)) {
            notifyBottomReached();
        }

        isFirstBind = false;
    }

    /**
     * Called by {@link TwoWayEndlessAdapter} to display the data at the specified position. This
     * method should update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect
     * the item at the given position.
     * <p>
     * Note that unlike {@link android.widget.ListView}, {@link TwoWayEndlessAdapter} will not call
     * this method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring/verifying the related data item
     * inside this method and should not keep a copy of it. If you need the position of an item
     * later on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()}
     * which will have the updated adapter position.
     *
     * Any class that extends from {@link TwoWayEndlessAdapter} must Override this method.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *               item at the given position in the data set.
     * @param data The data class object associated with the corresponding position which contains
     *            the updated content that represents the item at the given position in the data
     *            set.
     * @param position The position of the item within the adapter's data set.
     */
    public abstract void onBindViewHolder(VH holder, DataItem data, int position);

    /**
     * Sends the {@link Callback#onTopReached} callback if provided.
     * */
    protected void notifyTopReached()
    {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mEndlessCallback != null) {
                    mEndlessCallback.onTopReached();
                }
            }
        }, 50);

    }

    /**
     * Sends the {@link Callback#onBottomReached} callback if provided.
     * */
    protected void notifyBottomReached()
    {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mEndlessCallback != null) {
                    mEndlessCallback.onBottomReached();
                }
            }
        }, 50);
    }

    /**
     * The {@link TwoWayEndlessAdapter.Callback} class provides an interface notify when bottom or
     * top of the list is reached.
     */
    public interface Callback {
        /**
         * To be called when the first item of the {@link RecyclerView}'s data adapter is bounded to
         * the view.
         * Except the first time.
         * */
        void onTopReached();
        /**
         * To be called when the last item of the {@link RecyclerView}'s data adapter is bounded to
         * the view.
         * Except the first time.
         * */
        void onBottomReached();
    }
}
