package biz.rpcodes.apps.dualdrawingview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class DrawingView extends View {

private Paint mPaint;

private Bitmap mBitmap;

private Path mPath;

private Canvas mCanvas;

private float mDownY;

private float mDownX;

private int mNumPoints;

    public Integer mHeight = 320;
    public Integer mWidth = 200;

    // The minimum y of the current sample size, for zooming
    private float yMin;
    // the max y for sample size, for zooming
    private float yMax;

/**
 * TODO: Handler is un-used
 * @param context
 * @param h
 */
public DrawingView(Context context, Handler h) {
    super(context);
    // mHandler = h;
    init();
}

public DrawingView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
}

public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
}
/**
 * Create a bitmap (and canvas?) that we will draw to
 * when we onTouch
 * Then onDraw will draw this mBitmap (and not need to worry about paths)
 *
 */
protected void init() {
    // set up paint TRANSMITTED
    mPaint = new Paint(Color.WHITE);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth((float) 3.0);
    // mPaint.setStrokeJoin(Paint.Join.ROUND);
    // path (for action move?)
    mPath = new Path();
    // displayed background
    this.setBackgroundColor(Color.TRANSPARENT);

    // for bitmap
    mBitmap = null;
    mCanvas = null;

    // start with a reasonable value
}

@Override
public void onWindowFocusChanged (boolean hasFocus) {
        // the height will be set at this point
        if (  null == mBitmap ){
            int height = this.getHeight();
            int width = this.getWidth();
            // doesnt change
            mWidth = Integer.valueOf(width);
            mHeight = Integer.valueOf(height);
            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            // this is the color that is TRANSMITTED
            // client doesnt like this: mCanvas.drawColor(Color.WHITE);
            mCanvas.drawColor(Color.TRANSPARENT);

            // set min one width fromt he bottom
            // so when we add a width to it later
            // we reach the bottom
            // if we go above yMin, we only return mWidth etc
            // until we hit a larger width
//            yMin = height - mWidth - 1;
//            yMax = yMin + 1;
        }
}


static void fullscreenWindowForActivity(Activity A){
    Window w = A.getWindow();
    if ( null != w){
        A.requestWindowFeature(Window.FEATURE_NO_TITLE);
        A.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}

@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    // draw "cache"
    if (mBitmap != null){
        // TODO; Draw OTHER bitmap also
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    } else {
        Log.e("ERROR","NULL ON DRAW BMP");
    }

}

/*
 * Records points on to the path
 * Ignores all pointerId except 0
 *
 */
@Override
public boolean onTouchEvent(MotionEvent event) {
    // get pointer index from the event object
    int pointerIndex = event.getActionIndex();

    // get pointer ID
    int pointerId = event.getPointerId(pointerIndex);

    // get masked (not specific to a pointer) action
    int maskedAction = event.getActionMasked();

    // store position of mot recebt point
    // USE INDEX, NOT PointerID
    float x = (float) event.getX(pointerIndex);
    float y = (float) event.getY(pointerIndex);

    // For each action, add each point to the path
    switch(maskedAction) {

        case MotionEvent.ACTION_DOWN:
            // track first down press
            mDownX = x;
            mDownY = y;
            // set by processMove
            mNumPoints = 0;

            mPath.moveTo(mDownX, mDownY);
            break;

        case MotionEvent.ACTION_MOVE:
            processActionMove(event, pointerIndex);
            // conect history to final point
            // mPath.lineTo(x, y);
            // The canvas may be null at this point if onWindowFocusChanged hasnt fiTRANSPARENT yet
            if ( mCanvas != null ){
                mPaint.setColor(Color.WHITE);
                mCanvas.drawPath(mPath, mPaint);
            }
            // TODO: we should check all the points this way
            if (Math.abs(mDownX - x) <= 0.01
                       && Math.abs(mDownY - y) <= 0.01) {
                doDot(x, y);
            }
//            mPath.reset();
            // if we continue to move, we would need old points...
            mDownX = x;
            mDownY = y;
            break;

        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            //mPath.reset();
            break;

    }
    invalidate();

    return true;
}

    private void doDot(float x, float y){
        final float dx = 3.f;
        final float dy = 3.f;

        mPaint.setColor(Color.WHITE);
        mCanvas.drawLine(mDownX, mDownY, x + dx, y + dy, mPaint);
    }
/**
 *
 * @param event
 * @param pointerIndex
 */
protected void processActionMove(MotionEvent event, int pointerIndex){
    // use downX,Y as first point
    float xi,yi;
    mNumPoints = event.getHistorySize();
    //if ( points > 0) {
        // Start i at 1 because we checked x0 and y0
        for (int i = 0; i < mNumPoints; i++) {
            xi = event.getHistoricalX(pointerIndex, i); //(MotionEvent.AXIS_X, pointerId, i);
            yi = event.getHistoricalY(pointerIndex, i);
            mPath.lineTo(xi, yi);
        }
    //}
//    }
//    if (doDot){
//        mPath.lineTo(event.getX(pointerIndex) + dx
//                , event.getY(pointerIndex) + dy);
//    }
}

public Bitmap getBitmap() {
        return mBitmap;
}

public void clearDrawingData() {
    mCanvas.drawColor(Color.TRANSPARENT);
    invalidate();
}
}