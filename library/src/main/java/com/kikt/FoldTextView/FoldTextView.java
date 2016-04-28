package com.kikt.FoldTextView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by CaiJL on 2016/4/26.
 * 调用fold方法可以折叠和展开的view
 */
public class FoldTextView extends TextView {
    private int duration = 300;
    private int minHeight = 0;
    private int maxHeight;

    public static boolean debugMode = false;
    private OnFoldChangeListener onFoldChangeListener;
    private boolean isCustom = false;

    public FoldTextView(Context context) {
        this(context, null);
    }

    public FoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isFold = false;

    public void fold() {
        if (isFold) {
            close();
        } else {
            open();
        }
    }

    private void open() {
        animator(minHeight, maxHeight);
    }

    private void close() {
        animator(maxHeight, minHeight);
    }

    private boolean isFolding = false;

    private void animator(int minHeight, int maxHeight) {
        if (isFolding) {
            return;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(minHeight, maxHeight).setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (Integer) animation.getAnimatedValue();
                if (onAnimListener != null) {
                    onAnimListener.onAnim(FoldTextView.this, animation);
                }

                if (debugMode) {
                    Log.d("FoldTextView", "animatedValue:" + animatedValue);
                }
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = animatedValue;
                requestLayout();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                isFolding = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isFolding = false;
            }
        });
        valueAnimator.start();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isFold = !isFold;
                if (onFoldChangeListener != null) {
                    onFoldChangeListener.onFoldEnd(FoldTextView.this, isFold);
                }
            }
        }, duration);
    }

    public void setOnFoldChangeListener(OnFoldChangeListener onFoldChangeListener) {
        this.onFoldChangeListener = onFoldChangeListener;
    }

    public void setOnAnimListener(OnAnimListener onAnimListener) {
        this.onAnimListener = onAnimListener;
    }

    public interface OnFoldChangeListener {
        /**
         * 是否展开的侦听
         *
         * @param textView 对应的textView
         * @param isFold   是否展开式，true为展开，false为关闭状态
         */
        void onFoldEnd(FoldTextView textView, boolean isFold);
    }

    public interface OnAnimListener {
        /**
         * 动画过程的侦听
         *
         * @param animation 动画的对象
         * @see com.nineoldandroids.animation.ValueAnimator
         */
        void onAnim(FoldTextView textView, ValueAnimator animation);
    }

    private OnAnimListener onAnimListener;

    /**
     * @return 是否是收缩状态
     */
    public boolean isFold() {
        return isFold;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (type == BufferType.NORMAL) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int height = getHeight();
                    if (!isCustom) {
                        setMax(height);
                    }
                    getLayoutParams().height = minHeight;
                    requestLayout();
                }
            });
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }
        super.setText(text, type);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMin(int minHeight) {
        this.minHeight = minHeight;
    }

    public void setMax(int height) {
        isCustom = true;
        this.maxHeight = height;
    }

    public void removeMax() {
        isCustom = false;
    }

}
