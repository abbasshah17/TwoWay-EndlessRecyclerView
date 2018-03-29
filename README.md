# TwoWay-EndlessRecyclerView

[**TwoWayEndlessAdapter**](app/src/main/java/com/aaa/endlessrecyclerview/TwoWayEndlessAdapter.java) extends `RecyclerView.Adapter` to support two way endless scrolling.

To make use of the class simply extend from it and implement the `TwoWayEndlessAdapter.onBindViewHolder(ViewHolder holder, DataItem item, int position)` method.

The sample app also includes an example implementation in [`TwoWayEndlessAdapterImp`](app/src/main/java/com/aaa/endlessrecyclerview/TwoWayEndlessAdapterImp.java). Which is then used to set Adapter to the `RecyclerView`.

# Demo Video
<img src="https://media.giphy.com/media/8PySLrPDEihUasa1LZ/giphy.gif" width="220" height="400" />
