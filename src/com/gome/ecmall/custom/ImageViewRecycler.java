package com.gome.ecmall.custom;

import java.util.LinkedList;

import android.content.Context;
import android.widget.ImageView;

public class ImageViewRecycler {

    private LinkedList<ImageView> viewList = new LinkedList<ImageView>();
    private Context context;

    public ImageViewRecycler(Context context) {
        this.context = context;
    }

    public void ensureCapacity(int size) {
        int currentSize = viewList.size();
        if (currentSize >= size) {
            return;
        }
        int needSize = size - currentSize;
        for (int i = 0; i < needSize; i++) {
            requestView();
        }
    }

    public ImageView requestView() {
        if (viewList.size() > 0) {
            return viewList.removeFirst();
        } else {
            return new ImageView(context);
        }
    }

    public void releaseView(ImageView view) {
        view.setBackgroundDrawable(null);
        view.setImageDrawable(null);
        viewList.addLast(view);
    }

}
