package org.mokee.settings.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import android.annotation.Nullable;

public class FrameRateAnimationView extends View {
    private float mFrameRate = 60f;
    private int mBallColor = Color.BLACK;

    private Paint mBallPaint = new Paint();

    private boolean isAnimating = false;
    private long startTime = 0l;
    private long mDuration = 1000;

    public FrameRateAnimationView(Context context) {
        this(context, null);
    }

    public FrameRateAnimationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameRateAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBallPaint.setColor(mBallColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int ballSize = getHeight() / 6;
        if (isAnimating && isAttachedToWindow()) {
            float currentOffset = 1f * ((System.currentTimeMillis() - startTime) % mDuration) / mDuration;
            float startPosition = -ballSize / 2f;
            float endPosition = getWidth() + ballSize / 2f;
            float x = (endPosition - startPosition) * currentOffset + startPosition;
            float y;
            // define y = a * x^2 + b
            float midX = getWidth() / 2f;
            float midY = getHeight() - ballSize / 2f;
            double b = ballSize / 2f; // x = 0, y = b
            double a = (midY - b) / Math.pow(midX, 2);
            if (currentOffset < 0.5f) {
                y = (float) (a * Math.pow(x, 2) + b);
            } else {
                y = (float) (a * Math.pow(x - getWidth(), 2) + b);
            }
            canvas.drawCircle(x, y, ballSize / 2f, mBallPaint);

            int nextTime = (int) (1000 / mFrameRate);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, nextTime);
        } else {
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, ballSize / 2f, mBallPaint);
        }
    }

    public void setFrameRate(float frameRate) {
        mFrameRate = frameRate;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public float getFrameRate() {
        return mFrameRate;
    }

    public void setColor(int color) {
        mBallPaint.setColor(color);
    }

    public void startFrameRateAnimation() {
        isAnimating = true;
        startTime = System.currentTimeMillis();
        invalidate();
    }

    public void stopFrameRateAnimation() {
        isAnimating = false;
    }
}
