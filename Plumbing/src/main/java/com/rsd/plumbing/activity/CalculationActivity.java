package com.rsd.plumbing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rsd.plumbing.R;
import com.rsd.plumbing.util.BitmapUtil;
import com.rsd.plumbing.util.Constants;
import com.rsd.plumbing.util.DisplayUtil;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by wadereweti on 28/11/13.
 */
public class CalculationActivity extends Activity {

    private final String TAG = "CalculationActivity";
    private final String TITLE = "Your Calculated Gradient is";

    @InjectView(R.id.label_calculated_gradient) TextView mCalculatedGradient;
    @InjectView(R.id.label_number_of_clips)     TextView mNumberOfClips;

    private int mPipeSize;
    private int mLengthNeeded;
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
        overridePendingTransition(R.animator.fade_in_and_scale_up, R.animator.slide_in_bottom);
        finish();
    }

    private void setupActionBar() {
        getActionBar().setTitle(TITLE);
    }

    private void setupView() {
        Bundle bundle = getIntent().getBundleExtra(Constants.BUNDLE_CALCULATION);

        if (bundle != null) {
            mPipeSize = bundle.getInt(Constants.PIPE_SIZE);
            mLengthNeeded = bundle.getInt(Constants.PIPE_LENGTH);
            mLengthMetric = bundle.getInt(Constants.LENGTH_METRIC);

            calculateGradientAndClipsRequired();
        }
    }

    private void calculateGradientAndClipsRequired() {
        float gradient = calculateGradient();
        int numberOfClips = calculateNumberOfClipsRequired();

        mCalculatedGradient.setText(gradient + " is needed from start to finish");
        mNumberOfClips.setText(numberOfClips + "is needed for clips");
    }

    private float calculateGradient() {
        int baseGradient = 0;

        if (mPipeSize == 150) {
            baseGradient = 15;
        } else if (mPipeSize == 100) {
            baseGradient = 17;
        } else if (mPipeSize == 80) {
            baseGradient = 20;
        } else if (mPipeSize == 65) {
            baseGradient = 25;
        }

        float gradientRatio = baseGradient / 1000;
        float totalGradient = mLengthNeeded * gradientRatio;

        return totalGradient;
    }

    private int calculateNumberOfClipsRequired() {
        int minimumLength = 500;

        return mLengthNeeded / minimumLength;
    }
}
