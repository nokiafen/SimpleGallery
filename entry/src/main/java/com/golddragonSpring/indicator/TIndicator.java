package com.golddragonSpring.indicator;

import com.golddragonSpring.gallery.PagerGallery;
import com.golddragonSpring.utils.AttrUtil;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.app.Context;

public final class TIndicator extends Component implements PagerGallery.OnPageChangeListener, Component.EstimateSizeListener, Component.DrawTask {
    private PagerGallery viewPager;
    private int dotCount;
    private int fadingDotCount;
    private int selectedDotRadiusPx;
    private int dotRadiusPx;
    private int dotSeparationDistancePx;
    private boolean supportRtl;
    private boolean verticalSupport;
    private Color dotColor;
    private Color selectedDotColor;
    private Paint selectedDotPaint;
    private Paint dotPaint;
    private int selectedItemPosition;
    private int intermediateSelectedItemPosition;
    private float offsetPercent;
    private static final int DEFAULT_DOT_COUNT = 5;
    private static final int DEFAULT_FADING_DOT_COUNT = 1;
    private static final int DEFAULT_DOT_RADIUS_DP = 4;
    private static final float DEFAULT_SELECTED_DOT_RADIUS_DP = 5.5F;
    private static final int DEFAULT_DOT_SEPARATION_DISTANCE_DP = 10;

    public void onDraw(Component var1, Canvas canvas) {
        float dotCoordinate;
        float xPosition;
        float yPosition;
        for (int i = 0; i < getPagerItemCount(); i++) {
            dotCoordinate = getDotCoordinate(i);
            if (verticalSupport) {
                xPosition = getDotYCoordinate();
                yPosition = (float) getHeight() / 2 + dotCoordinate;
            } else {
                xPosition = (float) getWidth() / 2 + dotCoordinate;
                yPosition = getDotYCoordinate();
            }
            canvas.drawCircle(xPosition, yPosition, getRadius(dotCoordinate), getPaint(dotCoordinate));
        }
    }

    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        int minimumViewSize = 2 * selectedDotRadiusPx;
        if (verticalSupport) {
            setEstimatedSize(minimumViewSize, getCalculatedWidth());
        } else {
            setEstimatedSize(MeasureSpec.getMeasureSpec(getCalculatedWidth(), MeasureSpec.PRECISE), MeasureSpec.getMeasureSpec(minimumViewSize, MeasureSpec.PRECISE));
        }
        return true;
    }

    private float getDotCoordinate(int pagerPosition) {
        return (float) ((pagerPosition - intermediateSelectedItemPosition) * getDistanceBetweenTheCenterOfTwoDots()) + (float) getDistanceBetweenTheCenterOfTwoDots() * offsetPercent;
    }

    private int getDotYCoordinate() {
        return this.selectedDotRadiusPx;
    }

    private int getDistanceBetweenTheCenterOfTwoDots() {
        return 2 * this.dotRadiusPx + this.dotSeparationDistancePx;
    }

    private float getRadius(float coordinate) {
        float coordinateAbs = Math.abs(coordinate);
        float largeDotThreshold = (float) this.dotCount / (float) 2 * (float) this.getDistanceBetweenTheCenterOfTwoDots();
        float var10000;
        if (coordinateAbs < (float) (this.getDistanceBetweenTheCenterOfTwoDots() / 2)) {
            var10000 = (float) this.selectedDotRadiusPx;
        } else if (coordinateAbs <= largeDotThreshold) {
            var10000 = (float) this.dotRadiusPx;
        } else {
            float percentTowardsEdge = (coordinateAbs - largeDotThreshold) / ((float) this.getCalculatedWidth() / 2.01F - largeDotThreshold);
            var10000 = getInterpolation((float) 1 - percentTowardsEdge) * (float) this.dotRadiusPx;
        }

        return var10000;
    }

    public float getInterpolation(float input) {
        return (float) (1.0f - (1.0f - input) * (1.0f - input));
    }

    private Paint getPaint(float coordinate) {
        return Math.abs(coordinate) < (float) (this.getDistanceBetweenTheCenterOfTwoDots() / 2) ? this.selectedDotPaint : this.dotPaint;
    }

    private int getCalculatedWidth() {
        int maxNumVisibleDots = this.dotCount + 2 * this.fadingDotCount;
        return (maxNumVisibleDots - 1) * this.getDistanceBetweenTheCenterOfTwoDots() + 2 * this.dotRadiusPx;
    }


    public final void attachToViewPager( PagerGallery viewPager) {
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(this);

        this.intermediateSelectedItemPosition = selectedItemPosition = viewPager.getSelectIndex();
        invalidate();
    }

    private final int getPagerItemCount() {
        if (null != viewPager) {
            return viewPager.getChildCount();
        }
        return 0;
    }

    public final boolean isRtl() {
        return getLayoutDirection() == LayoutDirection.RTL;
    }

    private int getRTLPosition(int position) {
        return getPagerItemCount() - position - 1;
    }


    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (this.supportRtl && this.isRtl()) {
            this.intermediateSelectedItemPosition = this.getRTLPosition(position);
            this.offsetPercent = positionOffset * (float) 1;
        } else {
            this.intermediateSelectedItemPosition = position;
            this.offsetPercent = positionOffset * (float) -1;
        }

        this.invalidate();
    }

    public void onPageSelected(int position) {
        this.intermediateSelectedItemPosition = position;
        this.selectedItemPosition = this.supportRtl && this.isRtl() ? this.getRTLPosition(position) : position;
        this.invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public TIndicator( Context context,  AttrSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);

    }

    private void init(Context context, AttrSet attrs) {
        this.dotCount = DEFAULT_DOT_COUNT;
        this.fadingDotCount = DEFAULT_FADING_DOT_COUNT;
        this.selectedDotRadiusPx = AttrHelper.vp2px(DEFAULT_SELECTED_DOT_RADIUS_DP,context);
        this.dotRadiusPx = AttrHelper.vp2px((float) DEFAULT_DOT_RADIUS_DP,context);
        this.dotSeparationDistancePx = AttrHelper.vp2px((float) DEFAULT_DOT_SEPARATION_DISTANCE_DP,context);
        this.dotColor = new Color(Color.getIntColor("#E8E8E8"));
        this.selectedDotColor = new Color(Color.getIntColor("#ffffff"));
        this.selectedDotPaint = new Paint();
        this.dotPaint = new Paint();
        if (attrs != null) {
            AttrUtil attrUtil = new AttrUtil(attrs);
            this.dotCount = attrUtil.getIntValue("dotCount", DEFAULT_DOT_COUNT);
            this.fadingDotCount = attrUtil.getIntValue("fadingDotCount", DEFAULT_FADING_DOT_COUNT);
            this.dotRadiusPx = attrUtil.getIntValue("dotRadius", dotRadiusPx);
            this.selectedDotRadiusPx = attrUtil.getIntValue("selectedDotRadius", selectedDotRadiusPx);
            this.dotColor = attrUtil.getColorValue("dotColor", dotColor);
            this.selectedDotColor = attrUtil.getColorValue("selectedDotColor", selectedDotColor);
            this.dotSeparationDistancePx = attrUtil.getIntValue("dotSeparation", this.dotSeparationDistancePx);
            this.supportRtl = attrUtil.getBooleanValue("supportRTL", false);
            this.verticalSupport = attrUtil.getBooleanValue("verticalSupport", false);
        }

        selectedDotPaint.setStyle(Paint.Style.FILL_STYLE);
        selectedDotPaint.setColor(this.selectedDotColor);
        selectedDotPaint.setAntiAlias(true);
        dotPaint.setStyle(Paint.Style.FILL_STYLE);
        dotPaint.setColor(this.dotColor);
        dotPaint.setAntiAlias(true);
        setEstimateSizeListener(this);
        addDrawTask(this);
    }

    // $FF: synthetic method
    public TIndicator(Context var1, AttrSet var2, int var3, int var4) {
        this(var1, var2, var3);
    }

    public TIndicator( Context context,  AttrSet attrs) {
        this(context, attrs, 0);
    }


}

