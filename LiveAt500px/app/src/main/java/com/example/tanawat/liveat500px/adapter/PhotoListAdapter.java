package com.example.tanawat.liveat500px.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tanawat.liveat500px.R;
import com.example.tanawat.liveat500px.dao.PhotoItemCollectionDao;
import com.example.tanawat.liveat500px.dao.PhotoItemDao;
import com.example.tanawat.liveat500px.datatype.MuteableInteger;
import com.example.tanawat.liveat500px.manager.PhotoListManager;
import com.example.tanawat.liveat500px.view.PhotoListItem;

/**
 * Created by Tanawat on 23/9/2559.
 */
public class PhotoListAdapter extends BaseAdapter {

    PhotoItemCollectionDao dao;
    MuteableInteger lastPositionInterger;

    public PhotoListAdapter(MuteableInteger lastPositionInterger) {
        this.lastPositionInterger = lastPositionInterger;
    }

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
    }

    @Override
    public int getCount() {
        if (dao == null) {
            return 1;
        }
        if (dao.getData() == null) {
            return 1;
        }
        return dao.getData().size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return dao
                .getData()
                .get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position % 2 == 0 ? 0 : 1 ;
//    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getCount() - 1 ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if(getItemViewType(position)==0){
        if (position == getCount() - 1) {
            ProgressBar item;
            if (convertView != null)
                item = (ProgressBar) convertView;
            else
                item = new ProgressBar(parent.getContext());
            return item;
        }

        PhotoListItem item;
        if (convertView != null) {
            item = (PhotoListItem) convertView;
        } else {
            item = new PhotoListItem(parent.getContext());
        }
        PhotoItemDao dao = (PhotoItemDao) getItem(position);
        item.setNameText(dao.getCaption());
        item.setDescriptionText(dao.getUsername() + "\n" + dao.getCamera());
        item.setImgUrl(dao.getImageUrl());
        if (position > lastPositionInterger.getValue()) {
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(), R.anim.up_from_bottom);
            item.startAnimation(anim);
            lastPositionInterger.setValue(position);
        }
        return item;


    }

    public void increaseLastPosition(int amount) {
        lastPositionInterger.setValue(lastPositionInterger.getValue()+amount);
    }
}
