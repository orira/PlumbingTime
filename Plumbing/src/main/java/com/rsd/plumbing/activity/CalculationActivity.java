package com.rsd.plumbing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rsd.plumbing.R;
import com.rsd.plumbing.util.Constants;
import com.rsd.plumbing.util.TypefaceUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wadereweti on 28/11/13.
 */
public class CalculationActivity extends Activity {

    private final String TAG = "CalculationActivity";
    private final String TITLE = "Calculations";
    private final String LABEL_SUFFIX = " ml";

    private final int mMinimumLengthBetweenClips = 500;
    private int mBaseGradient = 0;
    private float mGradientRatio;

    @InjectView(R.id.calculated_gradient)       TextView mCalculatedGradient;
    @InjectView(R.id.label_calculated_gradient) TextView mCalculatedGradientLabel;

    @InjectView(R.id.number_of_clips)           TextView mNumberOfClips;
    @InjectView(R.id.label_number_of_clips)     TextView mNumberOfClipsLabel;

    @InjectView(R.id.clip_gradient)             TextView mClipGradient;
    @InjectView(R.id.label_clip_gradient)       TextView mClipGradientLabel;

    @InjectView(R.id.container_final_gradient)  RelativeLayout mContainerFinalClipGradient;
    @InjectView(R.id.final_clip_gradient)       TextView mFinalClipGradient;
    @InjectView(R.id.label_final_clip_gradient) TextView mFinalClipGradientLabel;

    private float mPipeSize;
    private float mLengthNeeded;
    private int mLengthMetric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        ButterKnife.inject(this);

        setupActionBar();
        setupView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.fade_in_and_scale_up, R.animator.slide_out_bottom);
        finish();
    }

    private void setupActionBar() {
        getActionBar().setTitle(TITLE);
        getActionBar().setBackgroundDrawable(null);
    }

    private void setupView() {
        Bundle bundle = getIntent().getBundleExtra(Constants.BUNDLE_CALCULATION);

        if (bundle != null) {
            mPipeSize = bundle.getInt(Constants.PIPE_SIZE);
            mLengthNeeded = bundle.getFloat(Constants.PIPE_LENGTH);
            mLengthMetric = bundle.getInt(Constants.LENGTH_METRIC);

            calculateGradientAndClipsRequired();
        }
    }

    private void calculateGradientAndClipsRequired() {
        float totalGradient = calculateGradient();
        float numberOfClips = calculateNumberOfClipsRequired();
        float remainingGradient = calculateRemainingGradient(numberOfClips);

        mClipGradientLabel.setTypeface(TypefaceUtil.getRobotoLight(this));
        mClipGradient.setText(Integer.toString(mBaseGradient) + LABEL_SUFFIX);
        mClipGradient.setTypeface(TypefaceUtil.getRobotoBold(this));

        mNumberOfClipsLabel.setTypeface(TypefaceUtil.getRobotoLight(this));
        mNumberOfClips.setText(Integer.toString((int) numberOfClips));
        mNumberOfClips.setTypeface(TypefaceUtil.getRobotoBold(this));

        mCalculatedGradientLabel.setTypeface(TypefaceUtil.getRobotoLight(this));
        mCalculatedGradient.setText(Integer.toString((int) totalGradient) + LABEL_SUFFIX);
        mCalculatedGradient.setTypeface(TypefaceUtil.getRobotoBold(this));

        if (remainingGradient != 0) {
            mContainerFinalClipGradient.setVisibility(View.VISIBLE);
            mFinalClipGradientLabel.setTypeface(TypefaceUtil.getRobotoLight(this));
            mFinalClipGradient.setText(Integer.toString((int) remainingGradient) + LABEL_SUFFIX);
            mFinalClipGradient.setTypeface(TypefaceUtil.getRobotoBold(this));
        }
    }

    private float calculateGradient() {

        if (mPipeSize == 150) {
            mBaseGradient = 15;
        } else if (mPipeSize == 100) {
            mBaseGradient = 17;
        } else if (mPipeSize == 80) {
            mBaseGradient = 20;
        } else if (mPipeSize == 65) {
            mBaseGradient = 25;
        }

        mGradientRatio = (float) mBaseGradient / 1000;
        float totalGradient = mLengthNeeded * mGradientRatio;

        return totalGradient;
    }

    private float calculateNumberOfClipsRequired() {
        double wholeCalculation = mLengthNeeded / mMinimumLengthBetweenClips;
        int clipsNeeded = wholeCalculation < 0 ? 0 : (int) wholeCalculation;

        return clipsNeeded;
    }

    private float calculateRemainingGradient(float numberOfClips) {
        float finalGradient = 0;
        float remainingDistance;

        if (numberOfClips > 0) {
            remainingDistance = mLengthNeeded % mMinimumLengthBetweenClips;
            finalGradient = remainingDistance * mGradientRatio;
        }

        return finalGradient;
    }
}
