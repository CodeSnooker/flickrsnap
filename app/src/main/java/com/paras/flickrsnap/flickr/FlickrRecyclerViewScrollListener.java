package com.paras.flickrsnap.flickr;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Copyright Text as per company
 */

/**
 * FlickrRecyclerViewScrollListener.java - Scroll Listener for FlickrRecyclerview. It provides
 * endless scroll by calling loadMore method when reach at the end of the list view
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public abstract class FlickrRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private StaggeredGridLayoutManager _staggeredGridLayoutManager;

    // The minimum number of items to have below your current scroll position
    // before loading more.
    private int _visibleThreshold = 2;

    // The current offset index of data you have loaded
    private int _currentPage = 1;

    // True if we are still waiting for the last set of data to load.
    private boolean _loading = false;

    // Whether the list is already displaying the last page
    private boolean _onLastPage = false;

    public abstract boolean onLoadMore(int page, int totalItemsCount);

    /**
     * @param layoutManager
     *
     * @constructor
     */
    public FlickrRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this._staggeredGridLayoutManager = layoutManager;
        _visibleThreshold = _visibleThreshold * _staggeredGridLayoutManager.getSpanCount();
    }

    /**
     * @param lastVisibleItemPositions
     *
     * @return
     */
    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    /**
     * Called whenever list is scrolled. Here it checks if the last list item (with given threshold)
     * is about to reach. If data is not already loading then it request to fetch the data from the
     * source. If list is already displaying the last page then it would not make the request.
     *
     * @param view - instance of Recycler View
     * @param dx   - amount scrolled in x direction
     * @param dy   - amount scrolled in y direction
     */
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int[] lastVisibleItemPositions = _staggeredGridLayoutManager.findLastVisibleItemPositions(null);
        //lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        int visibleItemCount = view.getChildCount();
        int totalItemCount = _staggeredGridLayoutManager.getItemCount();
        // same code as before

        if (lastVisibleItemPositions != null) {
            if (!_onLastPage && !_loading && lastVisibleItemPositions[lastVisibleItemPositions.length - 1] + _visibleThreshold > totalItemCount) {
                // Log.d("ScrollListner", "----------- LOAD MORE CALLED ---------");
                _currentPage++;
                this.onLoadMore(_currentPage, totalItemCount);
                _loading = true;


            }
        }
    }

    /**
     * Call this method whenever API finishes loading of Data
     */
    public void notifyLoadingFinished() {
        this._loading = false;
    }

    /**
     * Tells Listener whether it already on last page. That is whether already all items have been
     * fetched from the server
     *
     * @param onLastPage
     *
     * @return
     */
    public FlickrRecyclerViewScrollListener setOnLastPage(boolean onLastPage) {
        this._onLastPage = onLastPage;
        return this;
    }
}