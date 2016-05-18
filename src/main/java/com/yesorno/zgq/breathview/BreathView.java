package com.yesorno.zgq.breathview;

/**
 * Created by 37902 on 2016/5/18.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 37902 on 2016/3/27.
 */
public class BreathView extends View {
    public static int FIRSTTITLEISTARGET = 1;
    public static int SECONDTITLEISTARGET = 2;

    private static boolean STOPSTATUS = false;
    private static boolean RUNNINGSTATUS = true;
    private boolean viewStatus;

    private static boolean ISBLINKING = true;
    private static boolean NOTBLINKING = false;
    private boolean blinkingStatus;

    private static final int itemCountIntDef = 2;
    private int itemCountInt;

    private String firstItemTitle;
    private String secodItemTitle;
    private String titleShowing;

    private String targetTitle;

    private int firstTitleColor;
    private int secodTitleColor;
    private int last_select_title_color;


    private int titleSize = 1;
    private int initTitleSize;
    private int maxTitleSize = 1;
    private int titleSizeStep;


    private static int CHANGEBIGGER = 1;
    private static int CHANGESMALLER = 2;
    private int transfomStatus;

    private int firstItemColor;
    private int secodItemColor;
    private int itemColorDef;

    private Rect mRect = new Rect();

    private Context context;

    private Paint paint = new Paint();

    private OnStartBlinkingListener listener;

    public BreathView(Context context) {
        super(context);
    }

    public BreathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        itemColorDef = getResources().getColor(R.color.colorPrimary);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BreathView);
        itemCountInt = typedArray.getInt(R.styleable.BreathView_item_count, itemCountIntDef);
        firstItemTitle = typedArray.getString(R.styleable.BreathView_first_item_title);
        secodItemTitle = typedArray.getString(R.styleable.BreathView_secod_item_title);
        firstItemColor = typedArray.getColor(R.styleable.BreathView_first_item_color, itemColorDef);
        secodItemColor = typedArray.getColor(R.styleable.BreathView_secod_item_color, itemColorDef);
        init();
        initPaint();
    }

    public BreathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        titleShowing = firstItemTitle;
        transfomStatus = CHANGESMALLER;
        blinkingStatus = NOTBLINKING;
        viewStatus = STOPSTATUS;
    }

    private void initPaint() {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setStrokeWidth(3);
        paint.setColor(firstItemColor);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    public String getFirstItemTitle() {
        return firstItemTitle;
    }

    public void setFirstItemTitle(String firstItemTitle) {
        this.firstItemTitle = firstItemTitle;
    }

    public String getSecodItemTitle() {
        return secodItemTitle;
    }

    public void setSecodItemTitle(String secodItemTitle) {
        this.secodItemTitle = secodItemTitle;
    }

    public int getItemCountInt() {
        return itemCountInt;
    }

    public void setItemCountInt(int itemCountInt) {
        this.itemCountInt = itemCountInt;
    }

    public String getTargetTitle() {
        return targetTitle;
    }

    public void setTargetTitle(String targetTitle) {
        this.targetTitle = targetTitle;
    }

    public int getFirstTitleColor() {
        return firstTitleColor;
    }

    public void setFirstTitleColor(int firstTitleColor) {
        this.firstTitleColor = firstTitleColor;
    }

    public int getSecodTitleColor() {
        return secodTitleColor;
    }

    public void setSecodTitleColor(int secodTitleColor) {
        this.secodTitleColor = secodTitleColor;
    }

    public int getLast_select_title_color() {
        return last_select_title_color;
    }

    public void setLast_select_title_color(int last_select_title_color) {
        this.last_select_title_color = last_select_title_color;
    }

    private void changeShowTitle() {
        if (titleShowing.equals(firstItemTitle)) {
            titleShowing = secodItemTitle;
        } else {
            titleShowing = firstItemTitle;
        }
    }

    //view status
    private boolean getViewStatus() {
        return viewStatus;
    }

    public void setViewStatusStop() {
        viewStatus = STOPSTATUS;
    }

    private void setViewStatusRunning() {
        viewStatus = RUNNINGSTATUS;
    }


    //blinking status
    private boolean getBlinkingStatus() {
        return blinkingStatus;
    }

    private void setBlinkingStatus(boolean status) {
        this.blinkingStatus = status;
    }

    //transfom status
    private int getTransfomStatus() {
        return transfomStatus;
    }

    private void setTransfomStatus(int status) {
        this.transfomStatus = status;
    }


    public void reset() {
        init();
        titleSize = initTitleSize;
        setBlinkingStatus(false);
        setViewStatusStop();
        setTransfomStatus(CHANGESMALLER);
        invalidate();
    }

    public void start() {
        setBlinkingStatus(true);
        setViewStatusRunning();
        if (listener != null) {
            listener.onStartBlinking();
        }
        invalidate();
    }

    public void stop() {
        setBlinkingStatus(false);
    }

    public void setTargetTitle(int i) {
        if (i == 1)
            targetTitle = firstItemTitle;
        if (i == 2)
            targetTitle = secodItemTitle;
    }

    private void changTransformStatus() {
        if (getTransfomStatus() == CHANGEBIGGER) {
            setTransfomStatus(CHANGESMALLER);
            paint.setColor(firstItemColor);
        } else {
            setTransfomStatus(CHANGEBIGGER);
            paint.setColor(secodItemColor);
        }
    }

    public interface OnStartBlinkingListener {
        public void onStartBlinking();
    }

    public void setOnStartListener(OnStartBlinkingListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getWidth();
        int height = getHeight();
        switch (itemCountInt) {
            case 2:
                maxTitleSize = (width > height ? height : width) * 3 / 6;
                initTitleSize = maxTitleSize / 2;
                titleSizeStep = maxTitleSize / 40;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getViewStatus() == STOPSTATUS) {
            paint.setTextSize(initTitleSize);
            paint.getTextBounds(firstItemTitle, 0, 1, mRect);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(titleShowing, getMeasuredWidth() / 2, baseline, paint);
            return;
        }
        if (getViewStatus() == RUNNINGSTATUS) {
            paint.setTextSize(titleSize);
            paint.getTextBounds(titleShowing, 0, 1, mRect);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(titleShowing, getMeasuredWidth() / 2, baseline, paint);
            drawNextStep();
        }
    }

    private void drawNextStep() {
        if (getBlinkingStatus() == NOTBLINKING && titleShowing.equals(targetTitle) && transfomStatus == CHANGEBIGGER) {
            titleSize += titleSizeStep;
            if (titleSize > maxTitleSize) {
                return;
            }
            invalidate();
        } else {
            if (transfomStatus == CHANGEBIGGER) {
                titleSize += titleSizeStep;
                if (titleSize > maxTitleSize / 2) {
                    titleSize -= titleSizeStep;
                    changTransformStatus();
                }
            }
            if (transfomStatus == CHANGESMALLER) {
                titleSize -= titleSizeStep;
                if (titleSize < 0) {
                    titleSize = 1;
                    changTransformStatus();
                    changeShowTitle();
                }
            }
            invalidate();
        }
    }
}
