package com.gome.ecmall.home.more;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;

import com.baidu.mapapi.MapView;

public class MapAFrame {

    private ViewGroup mContainer;
    private ScrollView disScorllView;
    private MapView mapView;

    public MapAFrame(ViewGroup container, ScrollView disScorllView, MapView mapView) {
        // gabActivity = context;
        mContainer = container;
        this.disScorllView = disScorllView;
        this.mapView = mapView;
    }

    public void applyRotation(int position, float start, float end) {

        // Find the center of the container
        final float centerX = mContainer.getWidth() / 2.0f;
        final float centerY = mContainer.getHeight() / 2.0f;
        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position));
        mContainer.startAnimation(rotation);
    }

    /**
     * This class listens for the end of the first half of the animation. It then posts a new action that effectively
     * swaps the views when the container is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {
        private int mPosition;

        private DisplayNextView(int position) {
            mPosition = position;
        }

        public void onAnimationStart(Animation animation) {

        }

        public void onAnimationEnd(Animation animation) {
            mContainer.post(new SwapViews(mPosition));
        }

        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * This class is responsible for swapping the views and start the second half of the animation.
     */
    private final class SwapViews implements Runnable {
        private final int mPosition;

        public SwapViews(int position) {
            mPosition = position;
        }

        public void run() {
            final float centerX = mContainer.getWidth() / 2.0f;
            final float centerY = mContainer.getHeight() / 2.0f;
            Rotate3dAnimation rotation;
            if (mPosition > -1) {
                if (disScorllView != null && mapView != null) {
                    if (mapView.isShown()) {
                        mapView.setVisibility(View.GONE);
                        disScorllView.setVisibility(View.VISIBLE);
                    } else if (disScorllView.isShown()) {
                        disScorllView.setVisibility(View.GONE);
                        mapView.setVisibility(View.VISIBLE);
                    }
                }
                rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false);
            } else {

                rotation = new Rotate3dAnimation(270, 360, centerX, centerY, 310.0f, false);
            }
            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            mContainer.startAnimation(rotation);
        }
    }

}
