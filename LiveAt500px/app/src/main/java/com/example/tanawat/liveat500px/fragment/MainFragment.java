package com.example.tanawat.liveat500px.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tanawat.liveat500px.R;
import com.example.tanawat.liveat500px.activity.MoreInfoActivity;
import com.example.tanawat.liveat500px.adapter.PhotoListAdapter;
import com.example.tanawat.liveat500px.dao.PhotoItemCollectionDao;
import com.example.tanawat.liveat500px.dao.PhotoItemDao;
import com.example.tanawat.liveat500px.datatype.MuteableInteger;
import com.example.tanawat.liveat500px.manager.HttpManager;
import com.example.tanawat.liveat500px.manager.PhotoListManager;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class MainFragment extends Fragment {
    public interface FragmentListener{
        void onPhotoItemClicked(PhotoItemDao dao);
    }
    ///Variable
    ListView listView;
    PhotoListAdapter listAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    PhotoListManager photoListManager;
    Button btnNewPhotos;
    MuteableInteger lastPositionInterger;

    /***************
     * Function
     *************/

    //    CheckBox checkBut;
    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//Initial
        init(savedInstanceState);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView,savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        photoListManager = new PhotoListManager();
        lastPositionInterger = new MuteableInteger(-1);
    }

    private void initInstances(final View rootView,Bundle saveInstanceState) {

        // Init 'View' instance(s) with rootView.findViewById here
//        checkBut = (CheckBox) rootView.findViewById(R.id.checkAir);
//        checkBut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().openAir();
//      call.enqueue(new Callback<PhotoItemCollectionDao>() {
//          @Override
//          public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {
//              Toast.makeText(Contextor.getInstance().getContext(),"Successe",
//                            Toast.LENGTH_SHORT).show();
//          }
//
//          @Override
//          public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {
//              Toast.makeText(Contextor.getInstance().getContext(),"Faile",
//                      Toast.LENGTH_SHORT).show();
//          }
//      });
//
//            }
//        });


        listView = (ListView) rootView.findViewById(R.id.listView);
        btnNewPhotos = (Button) rootView.findViewById(R.id.btnNewPhotos);

        btnNewPhotos.setOnClickListener(buttonClickListener);
        listAdapter = new PhotoListAdapter(lastPositionInterger);
        listAdapter.setDao(photoListManager.getDao());
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(listViewItemClickListener);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(pullToRefreshListener);

        listView.setOnScrollListener(listViewScrollListener);
        if(saveInstanceState==null){
            refreshData();
        }

    }

    private void refreshData() {
        if (photoListManager.getCount() == 0) {
            reloadData();
        } else {
            reloadDataNewer();
        }
    }


    private void reloadDataNewer() {
        int maxId = photoListManager.getMaximumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoListAfterId(maxId);
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD_NEWER));
    }

    private void reloadData() {
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoList();
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD));
    }

    boolean isLoadingMore = false;

    private void loadMoreData() {
        if (isLoadingMore) {
            return;
        }
        isLoadingMore = true;
        int minId = photoListManager.getMinimumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoListBeforeId(minId);
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_LOAD_MORE));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
        //TODO: Save PhotoListManager outState
        outState.putBundle("photoListManager",photoListManager.onSaveInstanceState());
        outState.putBundle("lastPositionInterger",lastPositionInterger.onSaveInstanceState());

    }

    private void onRestoreInstanceState(Bundle saveInstanceState) {
        photoListManager.onRestoreInstanceState(saveInstanceState.getBundle("photoListManager"));
        lastPositionInterger.onRestoreInstanceState(saveInstanceState.getBundle("lastPositionInterger"));
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void showButtonNewPhotos() {
        btnNewPhotos.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_in);
        btnNewPhotos.startAnimation(anim);
    }

    private void hideButtonNewPhotos() {
        btnNewPhotos.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_out);
        btnNewPhotos.startAnimation(anim);


    }

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(),
                text,
                Toast.LENGTH_SHORT).show();
    }

    /******
     * Listener Zone
     */

    final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnNewPhotos) {
                listView.smoothScrollToPosition(0);
                hideButtonNewPhotos();
            }

        }
    };
    final SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };
    final AbsListView.OnScrollListener listViewScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (view == listView) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    if (photoListManager.getCount() > 0) {
                        loadMoreData();
                    }
                }
            }

        }
    };
    final AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position<photoListManager.getCount()){
                PhotoItemDao dao = photoListManager.getDao().getData().get(position);
                FragmentListener listener = (FragmentListener) getActivity();
                listener.onPhotoItemClicked(dao);
            }

        }
    };

    /***
     * Inner Class
     */

    class PhotoListLoadCallback implements Callback<PhotoItemCollectionDao> {
        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWER = 2;
        public static final int MODE_LOAD_MORE = 3;

        int mode;

        public PhotoListLoadCallback(int mode) {
            this.mode = mode;
        }

        @Override
        public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {
            swipeRefreshLayout.setRefreshing(false);
            if (response.isSuccessful()) {
                PhotoItemCollectionDao dao = response.body();

                int firstVisiblePosition = listView.getFirstVisiblePosition();
                View c = listView.getChildAt(0);
                int top = c == null ? 0 : c.getTop();
                if (mode == MODE_RELOAD_NEWER) {
                    photoListManager.insertDaoAtTopPosition(dao);
                } else if (mode == MODE_LOAD_MORE) {
                    photoListManager.appendDaoAtBottomPosition(dao);

                } else {
                    photoListManager.setDao(dao);
                }
                clearLoadingMoreFlagCapable(mode);
                listAdapter.setDao(photoListManager.getDao());
                listAdapter.notifyDataSetChanged();

                if (mode == MODE_RELOAD_NEWER) {
                    int additionalSize = (dao != null && dao.getData() != null) ? dao.getData().size() : 0;
                    listAdapter.increaseLastPosition(additionalSize);
                    listView.setSelectionFromTop(firstVisiblePosition + additionalSize, top);
                    if (additionalSize > 0) {
                        showButtonNewPhotos();
                    } else {

                    }

                } else {

                }
                //TODO: Toast na
                showToast("Load Completed");
            } else {
                clearLoadingMoreFlagCapable(mode);
                try {
                    showToast(response.errorBody().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {
            clearLoadingMoreFlagCapable(mode);
            swipeRefreshLayout.setRefreshing(false);
            showToast(t.toString());
        }

        private void clearLoadingMoreFlagCapable(int mode) {
            if (mode == MODE_LOAD_MORE) {
                isLoadingMore = false;
            }
        }
    }
}
