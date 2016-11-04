package com.imooc.xpuzzle.adapter;

import com.imooc.xpuzzle.wheelview.adapter.WheelAdapter;

import java.util.List;

/**
 * Created by kevin.tian on 2016/11/3.
 */

public class WheelSkuAdapter implements WheelAdapter {

    private List<String> mDatas;

    public WheelSkuAdapter(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getItemsCount() {
        return mDatas.size();
    }

    @Override
    public String getItem(int index) {
        return mDatas.get(index);
    }

    @Override
    public int getMaximumLength() {
        return mDatas.size();
    }
}
