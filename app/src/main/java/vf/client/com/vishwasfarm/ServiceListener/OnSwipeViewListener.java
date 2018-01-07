package vf.client.com.vishwasfarm.ServiceListener;

import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import vf.client.com.vishwasfarm.R;
import vf.client.com.vishwasfarm.fragments.SubscriptionFragment;
import vf.client.com.vishwasfarm.utility.Utilities;

/**
 * Class to handle swipe gesture for view, handles left to right and right to left swipe.
 */
public class OnSwipeViewListener implements View.OnTouchListener {

    private RecyclerView mRecyclerView;
    private View mDownView;

    private OnSubscriptionListClickListener mOnLicensePlateClickListener;
    private GestureDetectorCompat mGestureDetector;

    public boolean mIsScrolling;

    private float mDownX;
    private float mDownY;

    private int mDownPosition;
    private int deleteButtonStatus = 0;
    private int mSwipeDistance = 0;

    private final int DELETE_VIEW_INVISIBLE = 0;
    private final int DELETE_VIEW_VISIBLE = 1;
    private final int DELETE_VIEW_INVALIDATE = 2;
    private final int MAX_Y_AXIS_SWIPE_VALUE = 120;
    private final int MIN_Y_AXIS_SWIPE_VALUE = -120;


    /**
     * Handles callback for License plate order item clicks
     */
    public interface OnSubscriptionListClickListener {
        void onPauseOrderClick(String fLicensePlateNumber);

        void onDeleteOrderClick(String fItemPosition);
        void onEditOrderClick(String fItemPosition);
    }

    /**
     * Constructor to initialise view and callback
     * @param fView
     * @param fCallback
     */
    public OnSwipeViewListener(View fView, OnSubscriptionListClickListener fCallback) {
        mRecyclerView = (RecyclerView) fView;
        mOnLicensePlateClickListener = fCallback;

        mGestureDetector = new GestureDetectorCompat(fView.getContext(), mGestureListener);
        mSwipeDistance = (mRecyclerView.getContext()).getResources().getDimensionPixelSize(R.dimen.dimen_60dp);
    }


    @Override
    public boolean onTouch(View fView, MotionEvent fMotionEvent) {
        mGestureDetector.onTouchEvent(fMotionEvent);
        return false;
    }

    /**
     * If delete button is visible then it hides
     */
    public void hideDeleteButton() {

        if (deleteButtonStatus == DELETE_VIEW_VISIBLE && mDownView != null) {
            deleteButtonStatus = DELETE_VIEW_INVALIDATE;
            mDownX = 0;
            mDownY = 0;
            updateScroll(false);
            onSwipeRight(mRecyclerView, mDownPosition);
        }
    }

    /**
     * Swipes the license plate to left
     * @param fView
     * @param fItemPosition
     */
    public void onSwipeLeft(View fView, int fItemPosition) {

        View lChildItem;
        LinearLayout lMainRowRelativeLayout;
        int lFirstPosition;
        int lExpectedChildView;

        lFirstPosition = ((SubscriptionFragment.CustomGridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        lExpectedChildView = fItemPosition - lFirstPosition;
        lChildItem = ((RecyclerView) fView).getChildAt(lExpectedChildView);
        lMainRowRelativeLayout = (LinearLayout) lChildItem.findViewById(R.id.rl_main_adapter_view);
        Utilities.swipeOutAnimation(lMainRowRelativeLayout, mSwipeDistance);
    }


    /**
     * SWipes the license plate right
     * @param lView
     * @param lItemPosition
     */
    public void onSwipeRight(View lView, int lItemPosition) {

        View lChildItem;
        LinearLayout lMainRowRelativeLayout;
        int firstPosition = ((SubscriptionFragment.CustomGridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int wantedChild = lItemPosition - firstPosition;


        lChildItem = ((RecyclerView) lView).getChildAt(wantedChild);
        lMainRowRelativeLayout = (LinearLayout) lChildItem.findViewById(R.id.rl_main_adapter_view);
        Utilities.swipeInAnimation(lMainRowRelativeLayout, mSwipeDistance);
    }

    /**
     * It enable or disable the recycler view scroll
     * @param fScrollStatus
     */
    public void updateScroll(boolean fScrollStatus) {
        ((SubscriptionFragment.CustomGridLayoutManager) mRecyclerView.getLayoutManager()).setmIsScrollEnabled(fScrollStatus);
    }

    /**
     * Handles gesture listener for Touch down, fling , single Tap
     */
    public GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent fMotionEvent) {

            if (deleteButtonStatus == DELETE_VIEW_VISIBLE && mDownView != null) {
                //handles the swipe right
                mDownX = 0;
                mDownY = 0;
                updateScroll(false);


                if(!isClickableView( fMotionEvent)){
                    deleteButtonStatus = DELETE_VIEW_INVALIDATE;

                    onSwipeRight(mRecyclerView, mDownPosition);
                }
            } else {

                deleteButtonStatus = DELETE_VIEW_INVISIBLE;
                updateScroll(true);
                mDownY = fMotionEvent.getRawY();
                mIsScrolling = false;

                mDownView = getChild(mRecyclerView, fMotionEvent,false);

                if ((mRecyclerView != null) && mDownView != null) {
                    mDownX = fMotionEvent.getRawX();
                    mDownY = fMotionEvent.getRawY();
                    mDownPosition = mRecyclerView.getChildAdapterPosition(mDownView);
                }
            }
            return false;
        }


        @Override
        public boolean onFling(MotionEvent fFirstDownMotionEvent, MotionEvent fMotionMoveEvent, float fVelocityX, float fVelocityY) {

            if (deleteButtonStatus == DELETE_VIEW_INVISIBLE || deleteButtonStatus == DELETE_VIEW_INVALIDATE) {
                float lDeltaX = 0;
                float lDeltaY = 0;

                if (fMotionMoveEvent.getAction() == MotionEvent.ACTION_UP) {
                    lDeltaX = fMotionMoveEvent.getRawX() - mDownX;
                    lDeltaY = fMotionMoveEvent.getRawY() - mDownY;
                }

                if (lDeltaX < 0 && ((lDeltaY < MAX_Y_AXIS_SWIPE_VALUE && lDeltaY > 0) || (lDeltaY > MIN_Y_AXIS_SWIPE_VALUE && lDeltaY < 0))) {
                    updateScroll(false);
                    mIsScrolling = false;
                    deleteButtonStatus = DELETE_VIEW_VISIBLE;
                    onSwipeLeft(mRecyclerView, mDownPosition);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent fMotionEvent) {

            if (mDownView != null && deleteButtonStatus == DELETE_VIEW_INVISIBLE) {
                //Display car page screen on license plate click
               // mOnLicensePlateClickListener.onLicensePlateOrderClick(mDownView.getTag().toString());
            } else if (mDownView != null) {
                //handle delete button click
                deleteButtonStatus = DELETE_VIEW_INVALIDATE;
                mDownX = 0;
                mDownY = 0;
                updateScroll(false);

                View child= getHiddenLinearLayout();
                View lDeleteView;
                lDeleteView = getChild(child, fMotionEvent,true);

                if (lDeleteView!=null) {
                    deleteButtonStatus = DELETE_VIEW_VISIBLE;

                    if(lDeleteView.getId() == R.id.prod_pause){
                        String lLicensePlateNumber = (String) lDeleteView.getTag();
                        mOnLicensePlateClickListener.onPauseOrderClick(lLicensePlateNumber);
                    }
                    else if(lDeleteView.getId() == R.id.prod_delete){
                        Log.d("XXX","1");
                    }
                    else if(lDeleteView.getId() == R.id.prod_modify){
                        Log.d("XXX","2");

                    }
                }
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return !mIsScrolling;
        }
    };

    /**
     * It gives the child of view
     * @param fChild
     * @param fMotionEvent
     * @param fDeleteActionFlag
     * @return
     */
    private View getChild(View fChild, MotionEvent fMotionEvent, boolean fDeleteActionFlag) {

        int[] lViewCoordinates;
        Rect lViewRect;
        int lXPosition, lYPosition, lChildCount;
        View lChildView;

        // Find the child view that was touched (perform a hit test)
        lViewRect = new Rect();
        lChildCount = ((ViewGroup)fChild).getChildCount();
        lViewCoordinates = new int[2];

        mRecyclerView.getLocationOnScreen(lViewCoordinates);
        lXPosition = (int) fMotionEvent.getRawX() - lViewCoordinates[0];
        lYPosition = (int) fMotionEvent.getRawY() - lViewCoordinates[1];

        for (int i = 0; i < lChildCount; i++) {
            lChildView = ((ViewGroup)fChild).getChildAt(i);

            lChildView.getHitRect(lViewRect);

            //getHitRect(lChildView,lViewRect);

            if (fDeleteActionFlag) {
                /*if (lChildView.getId() == R.id.prod_pause) {
                    return lChildView;
                }
*/
                if (lViewRect.contains(lXPosition, lYPosition) && lChildView.getId() == R.id.prod_pause) {
                    return lChildView;
                }
                if (lViewRect.contains(lXPosition, lYPosition) && lChildView.getId() == R.id.prod_delete) {
                    return lChildView;
                }
                if (lViewRect.contains(lXPosition, lYPosition) && lChildView.getId() == R.id.prod_modify) {
                    return lChildView;
                }
            } else {
                if (lViewRect.contains(lXPosition, lYPosition)) {
                    return lChildView;
                }
            }
        }
        return null;

    }


    private boolean isClickableView( MotionEvent fMotionEvent) {
        View child= getHiddenLinearLayout();
        View lChildView;
        lChildView = getChild(child, fMotionEvent,true);

        if(lChildView!=null){
            switch (lChildView.getId()) {
                case R.id.prod_pause:
                    return true;
                case R.id.prod_delete:
                    return true;
                case R.id.prod_modify:
                    return true;
            }
        }
            return false;
    }

    private static void getHitRect(View v, Rect rect) {
        rect.left = (int) (v.getLeft() + v.getTranslationX());
        rect.top = (int) (v.getTop() + v.getTranslationY());
        rect.right = rect.left + v.getWidth();
        rect.bottom = rect.top + v.getHeight();
    }

    private View getHiddenLinearLayout() {

        View fChild;
        View lChildView = null;

        // Find the child view that was touched (perform a hit test)
        fChild = mRecyclerView.getChildAt(0);
        lChildView = ((ViewGroup)fChild).getChildAt(0);
        return lChildView;

    }


    public GestureDetector.OnGestureListener getmGestureListener(){
        return  mGestureListener;
    }

}