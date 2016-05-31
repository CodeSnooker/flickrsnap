package com.paras.flickrsnap;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paras.flickrsnap.flickr.FlickrAPI;
import com.paras.flickrsnap.flickr.FlickrPhoto;
import com.paras.flickrsnap.flickr.FlickrRecyclerViewAdapter;
import com.paras.flickrsnap.flickr.FlickrRecyclerViewScrollListener;
import com.paras.flickrsnap.flickr.FlickrResponse;
import com.paras.flickrsnap.retro.RetroServiceGenerator;
import com.paras.flickrsnap.util.NetworkConnection;
import com.paras.flickrsnap.util.ui.NotificationUtility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Copyright Text as per company
 */

/**
 * MainActivity.java - Activity class to display search results of tagged search on Flickr
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    private String requestedTag = "";
    private FlickrAPI apiService;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FlickrRecyclerViewScrollListener flickrRecyclerViewScrollListener;
    private ProgressBar tagSearchProgressBar;
    private RelativeLayout mainLayout;
    TextView searchResults;
    RecyclerView recyclerView;
    FlickrRecyclerViewAdapter viewAdapter;
    private SharedPreferences preferences;

    /**
     * Creates Menu toolbar
     *
     * @param menu
     *
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();

        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.setQuery("", false);
                searchView.setIconified(true);
                query = query.replaceAll("[^\\w]", "");
                requestedTag = query;
                searchResults.setText("Searching for tag \"" + query + "\"");
                tagSearchProgressBar.setIndeterminate(true);
                tagSearchProgressBar.setVisibility(View.VISIBLE);
                searchTag(query, 1, false);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d(TAG, "#onQueryTextChange: " + newText);
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetroServiceGenerator.init(Constants.FLICKR_API_BASE_URL, Constants.FLICKR_API_KEY);
        //RealmService.init(this);

        initAPIServices();
        initUIComponents();
    }

    /**
     * Initializes Retro API Service Class required by this activity
     */
    private void initAPIServices() {
        apiService = RetroServiceGenerator.createService(FlickrAPI.class);
    }

    /**
     * Initializes UI components associated with this activity
     */
    private void initUIComponents() {
        tagSearchProgressBar = (ProgressBar) findViewById(R.id.tagSearchProgress);
        searchResults = (TextView) findViewById(R.id.textView);
        preferences = this.getSharedPreferences(Constants.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);


        // Recycler View
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(Constants.COLUMNS_IN_ROW, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        flickrRecyclerViewScrollListener = new FlickrRecyclerViewScrollListener(staggeredGridLayoutManager) {

            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                searchTag(requestedTag, page, true);
                return false;
            }

            @Override
            public int getLastVisibleItem(int[] lastVisibleItemPositions) {
                return super.getLastVisibleItem(lastVisibleItemPositions);
            }
        };

        recyclerView.addOnScrollListener(flickrRecyclerViewScrollListener);

        viewAdapter = new FlickrRecyclerViewAdapter(MainActivity.this, new ArrayList<FlickrPhoto>());
        recyclerView.setAdapter(viewAdapter);

    }

    /**
     * Searches for tag on the Flickr
     *
     * @param tag      - Tag to be searched
     * @param page     - Page Index
     * @param toAppend - Whether to append in the existing data set otherwise existing dataset will
     *                 be wipped out and will be replaced with new search results
     */
    private void searchTag(final String tag, final int page, final boolean toAppend) {

        if (NetworkConnection.isOnline(this)) {


            Call<FlickrResponse> call = apiService.findByTag(tag, 50, page);
            call.enqueue(new Callback<FlickrResponse>() {
                @Override
                public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {

                    final FlickrResponse flickrResponse = response.body();

                    if (!flickrResponse.isFailed()) {

                        final boolean isLastPage = flickrResponse.getFlickrPhotoCollection().getPages() <= page;


                        ArrayList<FlickrPhoto> photos = flickrResponse.getFlickrPhotoCollection().getPhotos();
                        // Log.d(TAG, "Photos fetchd in current session: " + photos.size());

                        if (response.isSuccessful()) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Update search text and visibility of progress bar
                                    searchResults.setText("Showing " + flickrResponse.getFlickrPhotoCollection().getTotal() + " Results");
                                    tagSearchProgressBar.setVisibility(View.GONE);

                                    // Open shared preferences and update the new dataset whether any of the item is already marked as favourite
                                    ArrayList<FlickrPhoto> flickrPhotos = flickrResponse.getFlickrPhotoCollection().getPhotos();
                                    SharedPreferences.Editor editor = preferences.edit();

                                    for (FlickrPhoto photo : flickrPhotos) {
                                        boolean isFavourite = preferences.getBoolean(photo.getId(), false);
                                        if (isFavourite) {
                                            editor.putBoolean(photo.getId(), true);
                                            photo.setFavourite(true);
                                        }
                                    }

                                    // Save the shared preferences
                                    editor.commit();

                                    // Update view adapter and scroll listener
                                    viewAdapter.setOnLastPage(isLastPage);
                                    flickrRecyclerViewScrollListener.setOnLastPage(isLastPage);

                                    if (!toAppend) {
                                        viewAdapter.setRequestedTag("#" + tag.toLowerCase());
                                        viewAdapter.setPhotos(flickrResponse.getFlickrPhotoCollection().getPhotos());
                                        viewAdapter.notifyDataSetChanged();
                                        recyclerView.scrollToPosition(0);
                                        staggeredGridLayoutManager.scrollToPosition(0);
                                    } else {
                                        viewAdapter.appendPhotos(flickrResponse.getFlickrPhotoCollection().getPhotos());
                                        viewAdapter.notifyDataSetChanged();
                                        flickrRecyclerViewScrollListener.notifyLoadingFinished();
                                    }


                                }

                            });
                        }
                    } else {
                        NotificationUtility.showToast(MainActivity.this, flickrResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<FlickrResponse> call, Throwable t) {
                    Log.e("MainActivity", "Call failed: " + t.getMessage());
                    NotificationUtility.showToast(MainActivity.this, t.getMessage());
                }
            });
        } else {

            NotificationUtility.showSnackbar(MainActivity.this, mainLayout, Constants.ERROR_NO_INTERNET_CONNECTION, "OK");
            if (page == 1) {
                searchResults.setText(R.string.search_for_a_tag);
            }
            tagSearchProgressBar.setVisibility(View.GONE);
        }
    }


}
