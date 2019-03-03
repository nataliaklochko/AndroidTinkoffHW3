package com.nat.hw3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;




public class CustomView extends ViewGroup {

    private int deviceWidth;
    private int gapAttr;
    private int heightAttr;
    private String gravAttr;

    private String LEFT_GRAVITY = "left";
    private String RIGHT_GRAVITY = "right";


    public CustomView(Context context) {
        this(context, null, 0);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        gapAttr = ta.getDimensionPixelSize(R.styleable.CustomView_cv_gap, 0);
        heightAttr = ta.getDimensionPixelSize(R.styleable.CustomView_cv_view_height, dpToPx(30, context));
        gravAttr = (ta.getString(R.styleable.CustomView_cv_gravity) == null) ? LEFT_GRAVITY : ta.getString(R.styleable.CustomView_cv_gravity);
        ta.recycle();

    }

    public static int dpToPx(float dp, Context context) {
        float pxs = dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) pxs;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int mLeftWidth = 0;
        int rowCount = 0;
        int childWidth;
        int childHeight;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() != GONE) {

                measureChild(child, widthMeasureSpec, heightMeasureSpec);

                childWidth = child.getMeasuredWidth();
                childHeight = heightAttr;

                maxWidth += Math.max(maxWidth, childWidth + gapAttr);
                mLeftWidth += childWidth + gapAttr;

                if ((mLeftWidth / deviceWidth) > rowCount) {
                    maxHeight += childHeight + heightAttr;
                    rowCount++;
                } else {
                    maxHeight = Math.max(maxHeight, childHeight);
                }
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState)
        );

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curRight, curTop, maxHeight;

        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curRight = childRight;
        curTop = childTop;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() != GONE) {

                child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));

                if (gravAttr.equals(RIGHT_GRAVITY)) {

                    curWidth = child.getMeasuredWidth();
                    curHeight = heightAttr;

                    if (curRight - curWidth <= childLeft) {
                        curRight = childRight;
                        curTop += maxHeight + gapAttr;
                        maxHeight = 0;
                    }

                    child.layout(curRight - curWidth, curTop, curRight, curTop + curHeight);

                    if (maxHeight < curHeight)
                        maxHeight = curHeight;
                    curRight -= curWidth + gapAttr;

                } else {
                    curWidth = child.getMeasuredWidth();
                    curHeight = heightAttr;

                    if (curLeft + curWidth >= childRight) {
                        curLeft = childLeft;
                        curTop += maxHeight + gapAttr;
                        maxHeight = 0;
                    }

                    child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);

                    if (maxHeight < curHeight)
                        maxHeight = curHeight;
                    curLeft += curWidth + gapAttr;
                }

            }
        }
    }


}