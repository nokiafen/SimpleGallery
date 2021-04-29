package com.example.myapplication11.gallery;

import ohos.agp.components.*;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

import static ohos.multimodalinput.event.TouchEvent.*;
import static ohos.multimodalinput.event.TouchEvent.CANCEL;

public class PagerGallery extends ComponentContainer implements Component.EstimateSizeListener, ComponentContainer.ArrangeListener, Component.TouchEventListener {
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "PagerGallery");
    private float leftExtraPad = 0;
    private float itemPad = 0;
    private int picNormalWidth = 400;
    private int picNormalHeight = 400;
    private float topDistance = 0;
    private float itemInternalDistance;
    private int mHeight;
    private int offSet = 0;
    ScrollHelper mScroller;
    private int selectIndex = 0;
    private int calulateWidth;

    public PagerGallery(Context context) {
        super(context);
    }

    public PagerGallery(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(context, attrSet);
    }

    public PagerGallery(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }

    private void init(Context context, AttrSet attrSet) {
        float density = AttrHelper.getDensity(context);
        leftExtraPad = density * 38;
        itemPad = density * 5;
        topDistance = density * 16;
        setEstimateSizeListener(this);
        setArrangeListener(this);
        setTouchEventListener(this);
        mScroller = new ScrollHelper();
    }


    @Override
    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        int myWidth = measureWidth(widthMeasureSpec);
        int myHeight = measureHeight(heightMeasureSpec);
        setEstimatedSize(MeasureSpec.getMeasureSpec(myWidth, MeasureSpec.PRECISE), MeasureSpec.getMeasureSpec(myHeight, MeasureSpec.PRECISE));

        picNormalWidth = Float.valueOf(myWidth - itemPad * 2).intValue();
        picNormalHeight = Float.valueOf(myHeight * 0.5f).intValue();
        calulateWidth = (int) (myWidth - leftExtraPad * 2);
        itemInternalDistance = calulateWidth + itemPad;
        HiLog.warn(LABEL, "write in main onEstimateSize: counter %{public}d %{public}d %{public}f %{public}d", picNormalWidth, picNormalHeight, itemInternalDistance, mHeight);
        for (int i = 0; i < getChildCount(); i++) {
            Component child = getComponentAt(i);
            child.estimateSize(MeasureSpec.getMeasureSpec(calulateWidth, MeasureSpec.PRECISE), MeasureSpec.getMeasureSpec(picNormalHeight, MeasureSpec.PRECISE));
        }


        return true;
    }


    private int measureWidth(int widthMeasureSpec) {
        int measureMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureSize = MeasureSpec.getSize(widthMeasureSpec);
        int result = getMinWidth();
        switch (measureMode) {
            case MeasureSpec.NOT_EXCEED:
            case MeasureSpec.PRECISE:
                result = measureSize;
                break;
            case MeasureSpec.UNCONSTRAINT:
                break;
            default:
                break;

        }
        return result;
    }

    private int measureHeight(int heightMeasure) {
        int measureMode = MeasureSpec.getMode(heightMeasure);
        int measureSize = MeasureSpec.getSize(heightMeasure);
        int result = 200;
        switch (measureMode) {
            case MeasureSpec.PRECISE:
                result = Math.max(result, measureSize);
                break;
            case MeasureSpec.NOT_EXCEED:
                result = Math.min(result, measureSize);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        final int action = touchEvent.getAction();
        final int xCoordinate = (int) (int) touchEvent.getPointerPosition(0).getX();
        final int yCoordinate = (int) touchEvent.getPointerPosition(0).getY();
        HiLog.warn(LABEL, "write in main onTouchEvent: WheelHorizontalViewR  %{public}d", touchEvent.getAction());

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityDetector.obtainInstance();
        }
        mVelocityTracker.addEvent(touchEvent);
        switch (action) {
            case PRIMARY_POINT_DOWN:
                isMoving = false;
                mInitialX = xCoordinate;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case POINT_MOVE:
                if (isScaling) {
                    break;
                }
                int dx = xCoordinate - mLastX;
                if (!isMoving) {
                    final int dy = yCoordinate - mLastY;
                    if (Math.abs(xCoordinate - mInitialX) <= SCROLL_SLOP || Math.abs(dx) <= Math.abs(dy)) {
                        break;
                    }
                    isMoving = true;
                }
                mCurrentDistance += dx;
                computeLocation();
                break;
            case PRIMARY_POINT_UP:
                if (isScaling || !isMoving) {
                    break;
                }
                mVelocityTracker.calculateCurrentVelocity(1000, MAX_VELOCITY, MAX_VELOCITY);
                final int xVelocity = (int) mVelocityTracker.getHorizontalVelocity();
                HiLog.warn(LABEL, "write in main process: POINT_MOVEf %{public}d ", xVelocity);
                if (Math.abs(xVelocity) >= MIN_VELOCITY) {

                }
//                isMoving = false;
//                mCurrentDistance = 0;
//                postLayout();
                computeValueUp();
            case CANCEL:
                invalidate();
                break;

            default:
                break;
        }
        mLastX = xCoordinate;
        mLastY = yCoordinate;
        return true;
    }

    private void computeValueUp() {
        mCurrentDistance=selectIndex*itemInternalDistance;
        postLayout();
    }


    @Override
    public boolean onArrange(int left, int top, int width, int height) {
        final int count = getChildCount();
        float startX = leftExtraPad + mCurrentDistance;
        HiLog.warn(LABEL, "write in main onArrange: counter %{public}f ", startX);

        for (int i = 0; i < count; i++) {
            Component child = getComponentAt(i);
            if (child.getVisibility() != Component.HIDE) {
                child.arrange((int) startX, (int) getEstimatedHeight() / 2 - child.getEstimatedHeight() / 2, calulateWidth, child.getEstimatedHeight());
//                caculateScaleRatio(startX);
//                if (i == Math.abs(selectIndex)) {
//                    child.setScale(1, 1);
//                }else {
//                    child.setScale(0.9f,0.9f);
//                }
                child.setScale(caculateScaleRatio(startX,i),caculateScaleRatio(startX,i));
                HiLog.warn(LABEL, "write in main onArrange: counter %{public}f , %{public}d  %{public}d", startX, child.getWidth(), child.getHeight());
            }
            startX += getEstimatedWidth() - (int) leftExtraPad * 2 + itemPad;
        }
        return true;
    }

    private float caculateScaleRatio(float startX,int i) {
        float radio =Math.abs(getEstimatedWidth()/2-startX)/(calulateWidth/2);
        HiLog.warn(LABEL, "write in main onArrange: caculateScaleRatio %{public}f  i= %{public}d" ,radio,i);

        if (radio>1) {
            return 0.9f;
        }else {
            return  0.9f+0.1f* radio;
        }
    }

    private void computeLocation() {
        HiLog.warn(LABEL, "write in main process: computeLocation %{public}f %{public}f ", leftExtraPad, mCurrentDistance);
        if (mCurrentDistance >= 0) {
            mCurrentDistance = 0;

        } else if (mCurrentDistance <= -((getChildCount() - 1) * itemInternalDistance)) {
            mCurrentDistance = -((getChildCount() - 1) * itemInternalDistance);

        }
        float temp = mCurrentDistance;
        selectIndex= Math.round(temp/itemInternalDistance);
        HiLog.warn(LABEL, "write in main process: selectIndex %{public}d", selectIndex);
        postLayout();
    }

    private VelocityDetector mVelocityTracker;
    private boolean isMoving;
    private boolean isScaling;
    private float mCurrentDistance;
    private int mInitialX;
    private int mLastX;
    private int mLastY;
    private final int SCROLL_SLOP = 8;
    private final int MIN_VELOCITY = 50;
    private final int MAX_VELOCITY = 8000;

}
