package com.rsd.plumbing.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rsd.plumbing.R;
import com.rsd.plumbing.adapter.ActionbarSpinnerAdapter;
import com.rsd.plumbing.util.BitmapUtil;
import com.rsd.plumbing.util.Constants;
import com.rsd.plumbing.util.TransitionUtil;
import com.rsd.plumbing.util.TypefaceUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by wadereweti on 28/11/13.
 */
public class LengthActivity extends Activity {

    private final String TAG = "LengthActivity";
    private static final int DEFAULT_ANIMATION_LENGTH = 300;
    private static final int SHORT_ANIMATION_LENGTH = 150;

    @InjectView(R.id.imageview_pipe)                ImageView mImageView;
    @InjectView(R.id.label_pipe_size)               TextView mLabelPipeSize;
    @InjectView(R.id.container_seekbar)             LinearLayout mContainer;
    @InjectView(R.id.label_length)                  TextView mLabelLength;
    @InjectView(R.id.seekbar_length)                SeekBar mSeekBar;
    @InjectView(R.id.edittext_length)               EditText mEditText;
    @InjectView(R.id.button_gradient_calculation)   Button mButton;

    private int mOriginalHeight;
    private int mOriginalWidth;
    private int mOriginalLeft;
    private int mOriginalTop;
    private String mPipeSizeLabel;
    private int mPipeSizeValue;
    private LENGTH_METRIC mLengthMetric;

    private int mLeftDelta;
    private int mTopDelta;
    private float mWidthScale;
    private float mHeightScale;

    private float mPipeLength;

    private enum LENGTH_METRIC {
        MILLIMETRES, CENTIMETRES, METRES
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_toggle_input:
                toggleInputMode();
                hideCalculateButton();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleInputMode() {
        View visibleView;
        View alphaView;
        String labelPrompt;

        if (mSeekBar.getVisibility() == View.VISIBLE) {
            visibleView = mEditText;
            alphaView = mSeekBar;
            labelPrompt = getString(R.string.label_default_edittext_prompt);
        } else {
            visibleView = mSeekBar;
            alphaView = mEditText;
            labelPrompt = getString(R.string.label_default_seekbar_prompt);
        }

        runToggleInputAnimation(visibleView, alphaView, labelPrompt);
    }

    private void runToggleInputAnimation(final View visibleView, final View alphaView, final String labelPrompt) {
        visibleView.animate().alpha(1).withStartAction(new Runnable() {
            @Override
            public void run() {
                visibleView.setVisibility(View.VISIBLE);
                alphaView.animate().alpha(0);
                alphaView.setVisibility(View.GONE);
            }
        });

        mLabelLength.animate().setDuration(SHORT_ANIMATION_LENGTH).alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                mLabelLength.setText(labelPrompt);
                mLabelLength.animate().setDuration(SHORT_ANIMATION_LENGTH).alpha(1);
            }
        });

        if (visibleView instanceof SeekBar) {
            mSeekBar.setProgress(0);
            mEditText.removeTextChangedListener(textWatcher);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        } else {
            mEditText.setText("");
            mEditText.addTextChangedListener(textWatcher);
            mEditText.setFocusableInTouchMode(true);
            mEditText.requestFocus();
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(null);
        actionBar.setTitle("");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayHomeAsUpEnabled(true);

        List<String> options = Arrays.asList(getResources().getStringArray(R.array.length_activity_options));
        ActionbarSpinnerAdapter adapter = new ActionbarSpinnerAdapter(this, options);

        actionBar.setListNavigationCallbacks(adapter, navigationListener);
    }

    private void setupView(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getBundleExtra(Constants.BUNDLE_PIPE);
        final int pipePosition = bundle.getInt(Constants.PIPE_POSTION);
        mOriginalHeight = bundle.getInt(Constants.VIEW_HEIGHT);
        mOriginalWidth = bundle.getInt(Constants.VIEW_WIDTH);
        mOriginalLeft = bundle.getInt(Constants.VIEW_LEFT);
        mOriginalTop = bundle.getInt(Constants.VIEW_TOP);
        mPipeSizeLabel = bundle.getString(Constants.PIPE_SIZE_LABEL);
        mPipeSizeValue = bundle.getInt(Constants.PIPE_SIZE);

        if (savedInstanceState == null) {
            runScaleAnimation();
        } else {
            runBodyAnimation();
        }

        mLabelPipeSize.setTypeface(TypefaceUtil.getRobotoThin(this));
        mImageView.setImageBitmap(BitmapUtil.getBitMap(pipePosition, this));
        mLabelLength.setTypeface(TypefaceUtil.getRobotoLight(this));
        mSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        mButton.setAlpha(0);
        mButton.setTypeface(TypefaceUtil.getRobotoLight(this));
    }

    private void runBodyAnimation() {
        mLabelPipeSize.setAlpha(0);
        mLabelPipeSize.setVisibility(View.VISIBLE);
        mLabelPipeSize.setText(mPipeSizeLabel);
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

                // Animate scaled down view to current size and position
                mImageView.animate().setDuration(DEFAULT_ANIMATION_LENGTH)
                        .scaleX(1)
                        .scaleY(1)
                        .translationX(0)
                        .translationY(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        // Animate the rest of the  view in
                        runBodyAnimation();
                    }
                });
            }
        });
    }

    @OnClick(R.id.button_gradient_calculation)
    public void calculateGradientAndClipsNeeded() {
        Intent intent = new Intent(this, CalculationActivity.class);

        Bundle bundle = TransitionUtil.returnSlideInBottomScaleAndAlphaOut(this);
        bundle.putInt(Constants.PIPE_SIZE, mPipeSizeValue);
        bundle.putFloat(Constants.PIPE_LENGTH, calculateLength());

        intent.putExtra(Constants.BUNDLE_CALCULATION, bundle);

        startActivity(intent, bundle);
    }

    private float calculateLength() {
        float length;
        switch (mLengthMetric) {
            case CENTIMETRES:
                length = mPipeLength * 10;
                break;
            case METRES:
                length = mPipeLength * 1000;
                break;
            default:
                length = mPipeLength;
                break;
        }

        return length;
    }

    private String getLabelSuffix() {
//        if (mLengthMetric == null)
//            return LABEL_MILLIMETRE;

        Resources resources =  getResources();
        String suffix = "";

        switch (mLengthMetric) {
            case MILLIMETRES:
                suffix = resources.getQuantityString(R.plurals.label_millimetre, (int) mPipeLength);
                break;
            case CENTIMETRES:
                suffix = resources.getQuantityString(R.plurals.label_centimetre, (int) mPipeLength);
                break;
            case METRES:
                suffix = resources.getQuantityString(R.plurals.label_metre, (int) mPipeLength);
                break;
        }

        return suffix;
    }

    private void displayCalculateButton() {
        if (mButton.getVisibility() == View.GONE) {
            mButton.setVisibility(View.VISIBLE);
            mButton.animate().alpha(1);
        }
    }

    private void hideCalculateButton() {

        mButton.animate().alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                mButton.setVisibility(View.GONE);
            }
        });
    }

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

            float decimalValue = mPipeLength % 1;
            String editTextValue = decimalValue == 0 ? (int) mPipeLength + getLabelSuffix() : mPipeLength + getLabelSuffix();
            final String constructedString = mSeekBar.getVisibility() == View.VISIBLE ? (int) mPipeLength + getLabelSuffix() : editTextValue;

            if (!mLabelLength.getText().toString().equals(getString(R.string.label_default_seekbar_prompt)) &&
                !mLabelLength.getText().toString().equals(getString(R.string.label_default_edittext_prompt))) {
                mLabelLength.animate().setDuration(SHORT_ANIMATION_LENGTH).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mLabelLength.setText(constructedString);
                        mLabelLength.animate().setDuration(SHORT_ANIMATION_LENGTH).alpha(1);
                    }
                });
            }

            return true;
        }
    };

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (progress != 0) {
                mPipeLength = progress;
                displayCalculateButton();
                mLabelLength.setText(progress + getLabelSuffix());
            } else {
                mLabelLength.setText(getString(R.string.label_default_seekbar_prompt));
                hideCalculateButton();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() > 0) {
                mPipeLength = Float.parseFloat(s.toString());
                mLabelLength.setText(s + getLabelSuffix());
                mPipeLength = Float.parseFloat(s.toString());
                displayCalculateButton();
            } else {
                mLabelLength.setText(getString(R.string.label_default_edittext_prompt));
                hideCalculateButton();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };
}
