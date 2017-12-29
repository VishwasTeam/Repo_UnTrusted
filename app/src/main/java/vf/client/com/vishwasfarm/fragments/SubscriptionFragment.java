package vf.client.com.vishwasfarm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import vf.client.com.vishwasfarm.R;
import vf.client.com.vishwasfarm.ServiceListener.OnDeleteSubscription;
import vf.client.com.vishwasfarm.ServiceListener.OnFragmentChange;
import vf.client.com.vishwasfarm.ServiceListener.OnSwipeViewListener;
import vf.client.com.vishwasfarm.adapter.MySubscriptionAdapter;
import vf.client.com.vishwasfarm.dialogs.CustomProductDialog;
import vf.client.com.vishwasfarm.model.VishwasMySubscription;
import vf.client.com.vishwasfarm.model.VishwasMySubscriptionList;
import vf.client.com.vishwasfarm.model.VishwasProductList;
import vf.client.com.vishwasfarm.model.VishwasUser;
import vf.client.com.vishwasfarm.parser.SubscriptionProductParser;
import vf.client.com.vishwasfarm.service.PauseSubscriptionService;
import vf.client.com.vishwasfarm.utility.ImageLoader;
import vf.client.com.vishwasfarm.utility.SwipeDetector;

import static vf.client.com.vishwasfarm.utility.VishwasConstants.SubscritpionData;
import static vf.client.com.vishwasfarm.utility.VishwasConstants.UserData;


public class SubscriptionFragment extends Fragment implements View.OnClickListener,OnDeleteSubscription,OnSwipeViewListener.OnSubscriptionListClickListener {
    private VishwasProductList mVishwasProductList;
    private VishwasMySubscriptionList mVishwasMySubscriptionList;

    private List<VishwasMySubscription> mMySubscriptionList;
//    private List<VishwasProduct> mProductList;
//    private VishwasUser mVishwasUser;
    Bundle lProductBundle;
    RecyclerView mySubscriptionList;
    MySubscriptionAdapter mMySubscriptionAdapter;
    public ImageLoader imageLoader;
    ImageView mProductImageView;
    TextView mProductTitle;
    LinearLayout inflatedChild;

    OnFragmentChange mOnFragmentChange;
    CustomProductDialog mCustomDialog;
    private VishwasUser mVishwasUser;
    private SwipeDetector mSwipeDetector;
    private CustomGridLayoutManager mLayoutManager;
    private OnSwipeViewListener mOnSwipeViewListener;

    private String TAG=SubscriptionFragment.class.getSimpleName();

    public SubscriptionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.my_subscriptions, container, false);
        imageLoader = new ImageLoader(getActivity().getApplicationContext());
        mOnFragmentChange= (OnFragmentChange) getActivity();


        lProductBundle = getActivity().getIntent().getExtras();
        if (lProductBundle != null) {
//            mVishwasProductList = lProductBundle.getParcelable(ProductData);
//            if (mVishwasProductList != null) {
//                mProductList = mVishwasProductList.getmVishwasProductList();
//            }
            mVishwasMySubscriptionList = lProductBundle.getParcelable(SubscritpionData);
            if (mVishwasMySubscriptionList != null) {
                mMySubscriptionList = mVishwasMySubscriptionList.getmVishwasMySubscriptionList();
            }
            if (lProductBundle.getParcelable(UserData) != null) {
               mVishwasUser = lProductBundle.getParcelable(UserData);
           }
        }

        //recyclerview initialization
        mLayoutManager = new CustomGridLayoutManager(getContext());
        //mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setmIsScrollEnabled(true);
        mySubscriptionList = (RecyclerView) rootView.findViewById(R.id.subscription_list);


        mOnSwipeViewListener = new OnSwipeViewListener(mySubscriptionList, (OnSwipeViewListener.OnSubscriptionListClickListener) this);
        mySubscriptionList.setOnTouchListener(mOnSwipeViewListener);

        mMySubscriptionAdapter=new MySubscriptionAdapter(this,R.layout.my_subscriptions_adapter,mMySubscriptionList,mVishwasUser.getmCustId().toString());

        mySubscriptionList.setLayoutManager(mLayoutManager);
        mySubscriptionList.setItemAnimator(new DefaultItemAnimator());

        mySubscriptionList.setAdapter(mMySubscriptionAdapter);
        return rootView;
    }

    @Override
    public void onClick(View v) {

        if(Integer.parseInt(v.getTag().toString())==0){
            //mCustomDialog=new CustomDialog(mProductList.get(Integer.parseInt(view.getTag().toString())).getProductImage_URL(), getActivity(),new ProductDetailsFragment());
            //mOnFragmentChange.onFragmentChange(new ProductDetailsFragment());
        }
        else if(Integer.parseInt(v.getTag().toString())==1){
            //mOnFragmentChange.onFragmentChange(new ProductDetailsFragment());
        }
    }

    @Override
    public void onDeleteSubsciptionResult(boolean lStatus, String lResult) {
        mVishwasMySubscriptionList=new SubscriptionProductParser().parse(lResult);
        mMySubscriptionList=mVishwasMySubscriptionList.getmVishwasMySubscriptionList();
       // mMySubscriptionAdapter=new MySubscriptionAdapter(this,R.layout.my_subscriptions_adapter,mMySubscriptionList,mVishwasUser.getmCustId().toString());
        if(mMySubscriptionAdapter!=null) {
         //   mySubscriptionList.setAdapter(mMySubscriptionAdapter);
            mMySubscriptionAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onPauseOrderClick(String fSubscriptionId) {
        Log.d(TAG,"Pause Called");

        String lStartDate="2017-11-25";
        String lEndDate="2017-11-26";

        new PauseSubscriptionService(this,mVishwasUser.getmCustId().toString(),fSubscriptionId,lStartDate,lEndDate ).execute();
    }

    @Override
    public void onDeleteOrderClick(String fLicensePlateNumber) {
        Log.d(TAG,"Delete Called");
    }

    @Override
    public void onEditOrderClick(String fItemPosition) {
        Log.d(TAG,"Called Called");
    }

    public void swipeLeft(int tag) {
        mOnSwipeViewListener.onSwipeLeft(mySubscriptionList,tag);
    }

   /* AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            if (mSwipeDetector.swipeDetected()) {
                if (mSwipeDetector.getAction() == SwipeDetector.Action.RL) {
                    Log.d("","Swipe RL");
                } else if((mSwipeDetector.getAction() == SwipeDetector.Action.LR)){
                    Log.d("","Swipe LR");
                }
            }
        }
    };
    */


    /**
     * Custom Layout manager to handle scroll on recycler view
     */
    public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean mIsScrollEnabled = true;

        public CustomGridLayoutManager(Context fContext) {
            super(fContext);
        }


        /**
         * Update the scrolling property og recycler view
         * @param fFlag
         */
        public void setmIsScrollEnabled(boolean fFlag) {
            this.mIsScrollEnabled = fFlag;
        }

        @Override
        public boolean canScrollVertically() {
            return mIsScrollEnabled && super.canScrollVertically();
        }
    }


}