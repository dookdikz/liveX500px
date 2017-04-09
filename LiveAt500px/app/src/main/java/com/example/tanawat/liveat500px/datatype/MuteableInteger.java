package com.example.tanawat.liveat500px.datatype;

import android.os.Bundle;

/**
 * Created by Tanawat on 30/10/2559.
 */
public class MuteableInteger {
    private int value;

    public MuteableInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public Bundle onSaveInstanceState(){
        Bundle bundle = new Bundle();
        bundle.putInt("value",value);
        return bundle;
    }
    public  void onRestoreInstanceState(Bundle saveInstanceState){
        value = saveInstanceState.getInt("value");
    }
}
