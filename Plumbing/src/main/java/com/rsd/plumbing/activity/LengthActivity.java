package com.rsd.plumbing.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.rsd.plumbing.R;
import com.rsd.plumbing.util.BitmapUtil;
import com.rsd.plumbing.util.Constants;
import com.rsd.plumbing.util.DisplayUtil;
import com.rsd.plumbing.util.TransitionUtil;
import com.rsd.plumbing.util.TypefaceUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by wadereweti on 28/11/13.
 */
public class LengthActivity extends Activity {

    private final String TAG = "LengthActivity";
    private static final String TITLE = "";
    private static final int DEFAULT_ANIMATION_LENGTH = 300;
    private static final int SHORT_ANIMATION_LENGTH = 150;
    private static final String LABEL_MILLIMETRE = " millimetres";
    private static final String LABEL_CENTIMETRE = " centimetres";
    private static final String LABEL_METRE = " metres";

    @InjectView(R.id.imageview_pipe)                ImageView mImageView;
    @InjectView(R.id.label_pipe_size)               TextView mLabelPipeSize;
    @InjectView(R.id.container_seekbar)             LinearLayout mContainer;
    @InjectView(R.id.label_length)                  TextView mLabelLength;
    @InjectView(R.id.seekbar_length)                SeekBar mSeekBar;
    @InjectView(R.id.button_gradient_calculation)   Button mButton;

    private int mOriginalHeight;
    private int mOriginalWidth;
    private int mOriginalLeft;
    private int mOriginalTop;
    private String mPipeSize;
    private LENGTH_METRIC mLengthMetric;

    private enum LENGTH_METRIC {
        MILLIMETRES, CENTIMETRES, METRES
    }

    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;

    private int mActivityWidth;

    private int mPipeLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_length);
        ButterKnife.inject(this);

        setupActionBar();
        setupView(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_length_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupActionBar() {
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.length_activity_options, R.layout.item_actionbar_spinner);

        ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                switch (itemPosition) {
                    case 0:
                        mLengthMetric = LENGTH_METRIC.MILLIMETRES;
                        break;
                    case 1:
                        mLengthMetric = LENGTH_METRIC.CENTIMETRES;
                        break;
                    case 2:
                        mLengthMetric = LENGTH_METRIC.METRES;
                        break;
                }

                if (!mLabelLength.getText().toString().equals(getString(R.string.label_default_length_prompt))) {
                    mLabelLength.animate().setDuration(SHORT_ANIMATION_LENGTH).alpha(0).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            mLabelLength.setText(mPipeLength + getLabelSuffix());
                            mLabelLength.animate().setDuration(SHORT_ANIMATION_LENGTH).alpha(1);
                        }
                    });
                }

                return true;
            }
        };



        ActionBar actionBar = getActionBar();
        actionBar.setTitle(TITLE);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(spinnerAdapter, navigationListener);
    }

    private void setupView(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getBundleExtra(Constants.BUNDLE_PIPE);
        final int pipePosition = bundle.getInt(Constants.PIPE_POSTION);
        mOriginalHeight = bundle.getInt(Constants.VIEW_HEIGHT);
        mOriginalWidth = bundle.getInt(Constants.VIEW_WIDTH);
        mOriginalLeft = bundle.getInt(Constants.VIEW_LEFT);
        mOriginalTop = bundle.getInt(Constants.VIEW_TOP);
        mPipeSize = bundle.getString(Constants.PIPE_SIZE);

        mActivityWidth = DisplayUtil.getWindowWidth(getWindowManager().getDefaultDisplay());

        if (savedInstanceState == null) {
            runScaleAnimation();
        } else {
            runBodyAnimation();
        }

        mLabelPipeSize.setTypeface(TypefaceUtil.getRobotoThin(this));

        mImageView.setImageBitmap(BitmapUtil.getBitMap(pipePosition, this));
        mSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        //mButton.setX(-mActivityWidth);
        mButton.setAlpha(0);
    }

    private void runBodyAnimation() {
        mLabelPipeSize.setAlpha(0);
        mLabelPipeSize.setVisibility(View.VISIBLE);
        mLabelPipeSize.setText(mPipeSize);
        mLabelPipeSize.animate().setDuration(DEFAULT_ANIMATION_LENGTH).alpha(1);

        mContainer.animate().setDuration(DEFAULT_ANIMATION_LENGTH).alpha(1);
    }

    private void runScaleAnimation() {
        mImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImageView.getViewTreeObserver().removeOnPreDrawListener(this);

                int[] screenLocation = new int[2];
                mImageView.getLocationOnScreen(screenLocation);
                mLeftDelta = mOriginalLeft - screenLocation[0];
                mTopDelta = mOriginalTop - screenLocation[1];
                mWidthScale = 0.5f;
                mHeightScale = mOriginalHeight / mImageView.getHeight();

                runEntryAnimation();

                return true;
            }

            private void runEntryAnimation() {
                /* Set starting values for properties we're going to animate. These
                   values scale and position the full size version down to the thumbnail
                   size/location, from which we'll animate it back up */
                mImageView.setPivotX(0);
                mImageView.setPivotY(0);
                mImageView.setScaleX(mWidthScale);
                mImageView.setScaleY(mHeightScale);
                mImageView.setTranslationX(mLeftDelta);
                mImageView.setTranslationY(mTopDelta);

                //Animate scaled down view to current size and position
                mImageView.animate().setDuration(DEFAULT_ANIMATION_LENGTH)
                        .scaleX(1)
                        .scaleY(1)
                        .translationX(0)
                        .translationY(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        runBodyAnimation();

                        /*mLabelPipeSize.setAlpha(0);
                        mLabelPipeSize.setVisibility(View.VISIBLE);
                        mLabelPipeSize.setText(mPipeSize);
                        mLabelPipeSize.animate().setDuration(DEFAULT_ANIMATION_LENGTH).alpha(1);

                        mContainer.animate().setDuration(DEFAULT_ANIMATION_LENGTH).alpha(1);*/
                    }
                });
            }
        });
    }

    @OnClick(R.id.button_gradient_calculation)
    public void calculateGradientAndClipsNeeded() {
        Intent intent = new Intent(this, CalculationActivity.class);

        Bundle bundle = TransitionUtil.returnSlideInBottomScaleAndAlphaOut(this);
        bundle.putString(Constants.PIPE_SIZE, mPipeSize);
        bundle.putInt(Constants.PIPE_LENGTH, calculateLength());

        startActivity(intent, bundle);
    }

    private int calculateLength() {
        int length;
        switch (mLengthMetric) {
            case CENTIMETRES:
                length = mPipeLength / 10;
                break;
            case METRES:
                length = mPipeLength / 100;
                break;
            default:
                length = mPipeLength;
                break;
        }

        return length;
    }

    private String getLabelSuffix() {
        if (mLengthMetric == null)
            return LABEL_MILLIMETRE;

        String suffix = "";

        switch (mLengthMetric) {
            case MILLIMETRES:
                suffix = LABEL_MILLIMETRE;
                break;
            case CENTIMETRES:
                suffix = LABEL_CENTIMETRE;
                break;
            case METRES:
                suffix = LABEL_METRE;
                break;
            default:
                suffix = LABEL_MILLIMETRE;
        }

        return suffix;
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (progress != 0) {
                displayCalculateButton();
                mLabelLength.setText(progress + getLabelSuffix());
                mPipeLength = progress;
            } else {
                mLabelLength.setText(getString(R.string.label_default_length_prompt));
                hideCalculateButton();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}

        private void displayCalculateButton() {
            if (mButton.getVisibility() == View.GONE) {
                mButton.setVisibility(View.VISIBLE);
                //mButton.animate().translationX(0);
                mButton.animate().alpha(1);
            }
        }

        private void hideCalculateButton() {
            /*mButton.animate().translationX(-mActivityWidth).withEndAction(new Runnable() {
                @Override
                public void run() {
                    mButton.setVisibility(View.INVISIBLE);
                }
            });*/

            mButton.animate().alpha(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    mButton.setVisibility(View.GONE);
                }
            });
        }
    };
}
