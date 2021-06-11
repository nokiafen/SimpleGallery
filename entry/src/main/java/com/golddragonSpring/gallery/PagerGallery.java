package com.golddragonSpring.gallery;

import com.golddragonSpring.utils.BlurTransformation;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.*;
import ohos.agp.components.element.PixelMapElement;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.multimodalinput.event.TouchEvent;

import java.util.HashMap;
import java.util.Map;

import static ohos.multimodalinput.event.TouchEvent.*;

public class PagerGallery extends ComponentContainer implements Component.EstimateSizeListener, ComponentContainer.ArrangeListener, Component.TouchEventListener, Component.BindStateChangedListener {
    private float leftExtraPad = 0;
    private float itemPad = 0;
    private int picNormalWidth = 400;
    private int picNormalHeight = 400;
    private float topDistance = 0;
    private float itemInternalDistance;
    private int mHeight;
    private int offSet = 0;
    ScrollHelper mScroller;
    private int selectIndex = 2;
    private int calulateWidth;
    private int lastIndex = -1;
    BlurTransformation blurTransformation;

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
        setBindStateChangedListener(this);
        float density = AttrHelper.getDensity(context);
        leftExtraPad = density * 38;
        itemPad = density * 5;
        topDistance = density * 16;
        setEstimateSizeListener(this);
        setArrangeListener(this);
        setTouchEventListener(this);
        mScroller = new ScrollHelper();
        blurTransformation = new BlurTransformation(100);
    }


    @Override
    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        int myWidth = measureWidth(widthMeasureSpec);
        int myHeight = measureHeight(heightMeasureSpec);
        setEstimatedSize(MeasureSpec.getMeasureSpec(myWidth, MeasureSpec.PRECISE), MeasureSpec.getMeasureSpec(myHeight, MeasureSpec.PRECISE));

        picNormalWidth = Float.valueOf(myWidth - itemPad * 2).intValue();
        picNormalHeight = Float.valueOf(myHeight * 0.6f).intValue();
        calulateWidth = (int) (myWidth - leftExtraPad * 2);
        itemInternalDistance = calulateWidth + itemPad;
        for (int i = 0; i < getChildCount(); i++) {
            Component child = getComponentAt(i);
            child.estimateSize(MeasureSpec.getMeasureSpec(calulateWidth, MeasureSpec.PRECISE), MeasureSpec.getMeasureSpec(picNormalHeight, MeasureSpec.PRECISE));
        }
        if (selectIndex != 0 && mCurrentDistance == 0) {
            mCurrentDistance = -selectIndex * itemInternalDistance;
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
                if (Math.abs(xVelocity) >= MIN_VELOCITY) {
                    new AnimHelper((int) mCurrentDistance,xVelocity).start();
                }else
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
        mCurrentDistance = -selectIndex * itemInternalDistance;
        postLayout();
    }


    @Override
    public boolean onArrange(int left, int top, int width, int height) {
        final int count = getChildCount();
        float startX = leftExtraPad + mCurrentDistance;

        for (int i = 0; i < count; i++) {
            Component child = getComponentAt(i);
            if (child.getVisibility() != Component.HIDE) {
                child.arrange((int) startX, (int) getEstimatedHeight() / 2 - child.getEstimatedHeight() / 2, calulateWidth, child.getEstimatedHeight());
                child.setScale(caculateScaleRatio(startX, i), caculateScaleRatio(startX, i));
            }
            startX += calulateWidth + itemPad;
        }
        return true;
    }

    private float caculateScaleRatio(float startX, int i) {
        float middleLineOffset = Math.abs(getEstimatedWidth() / 2f - (startX + calulateWidth / 2f));
        float radio = middleLineOffset / itemInternalDistance;

        if (radio > 1) {
            return 0.9f;
        } else {
            return (1 - radio) * 0.1f + 0.9f;
        }
    }

    private void computeLocation() {
        if (mCurrentDistance >= 0) {
            mCurrentDistance = 0;

        } else if (mCurrentDistance <= -((getChildCount() - 1) * itemInternalDistance)) {
            mCurrentDistance = -((getChildCount() - 1) * itemInternalDistance);

        }
        float temp = mCurrentDistance;
        selectIndex = -Math.round(temp / itemInternalDistance);
        if (selectIndex != lastIndex) {
            lastIndex = selectIndex;
            setBackground(new PixelMapElement(getBlurPixelMapFromIndex(selectIndex)));
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(selectIndex);
            }
        }
        postLayout();
    }

    public void setImages(int[] resourseId) {
        removeAllComponents();
        blurPixelMap.clear();
        Image image;
        for (int i = 0; i < resourseId.length; i++) {
            image = new Image(getContext());
            image.setScaleMode(Image.ScaleMode.CENTER);
            image.setPixelMap(resourseId[i]);
            image.setCornerRadius(AttrHelper.vp2px(10,getContext()));
            addComponent(image);
        }
        postLayout();

    }

    public void setSelectIndex(int index) {
        if (index < 0 || index > getChildCount() - 1) {
            return;
        }
        selectIndex = index;
        mCurrentDistance = -selectIndex * itemInternalDistance;
        postLayout();
//        setBackground(new PixelMapElement(getBlurPixelMapFromIndex(index)));
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

    private Map<Integer, PixelMap> blurPixelMap = new HashMap<>();

    public PixelMap getBlurPixelMapFromIndex(int index) {
        PixelMap pixelMap = blurPixelMap.get(index);
        if (pixelMap == null) {
           PixelMap pixelMapTem= ((Image) getComponentAt(index)).getPixelMap();
            blurPixelMap.put(index, pixelMap = blurTransformation.transform(pixelMapTem,0.36f));
        }
        return pixelMap;
    }


    public int getSelectIndex() {
        return selectIndex;
    }


    private OnPageChangeListener onPageChangeListener;

    public void addOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
        setBackground(new PixelMapElement(getBlurPixelMapFromIndex(selectIndex)));
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {

    }

    public interface OnPageChangeListener {

        /**
         * This method will be invoked when the current page is scrolled, either as part
         * of a programmatically initiated smooth scroll or a user initiated touch scroll.
         *
         * @param position             Position index of the first page currently being displayed.
         *                             Page position+1 will be visible if positionOffset is nonzero.
         * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
         * @param positionOffsetPixels Value in pixels indicating the offset from position.
         */
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        void onPageSelected(int position);

        /**
         * Called when the scroll state changes. Useful for discovering when the user
         * begins dragging, when the pager is automatically settling to the current page,
         * or when it is fully stopped/idle.
         *
         * @param state The new scroll state.
         */
        void onPageScrollStateChanged(int state);
    }


    public class  AnimHelper extends AnimatorValue implements  AnimatorValue.ValueUpdateListener , Animator.StateChangedListener {
        private  int startValue;
        private  int totalProgress;

        public AnimHelper(int startValue,int totalProgress) {
            this.startValue = startValue;
            this.totalProgress = totalProgress;
            setValueUpdateListener(this);
            setStateChangedListener(this);
        }

        @Override
        public void onUpdate(AnimatorValue animatorValue, float v) {
             mCurrentDistance = startValue+ (int)(v*totalProgress);
             computeLocation();
        }

        @Override
        public void onStart(Animator animator) {

        }

        @Override
        public void onStop(Animator animator) {
            computeValueUp();
        }

        @Override
        public void onCancel(Animator animator) {

        }

        @Override
        public void onEnd(Animator animator) {

        }

        @Override
        public void onPause(Animator animator) {

        }

        @Override
        public void onResume(Animator animator) {

        }
    }

}

