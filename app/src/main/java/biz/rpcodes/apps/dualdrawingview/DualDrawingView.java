package biz.rpcodes.apps.dualdrawingview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Pagga on 12/10/2015.
 */
public class DualDrawingView extends DrawingView {

    private Bitmap mBG;

    private Paint mBGPaint;

    public DualDrawingView(Context context, Handler h) {
        super(context, h);
    }

    public DualDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DualDrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * Intended for use with a transparent BG image
     */
    public void setOverlayBitmap(Bitmap b){
       mBG = b;
    }

    @Override
    protected void init(){
        mBGPaint= new Paint(Color.WHITE);
        mBGPaint.setStyle(Paint.Style.STROKE);
        mBGPaint.setStrokeWidth((float) 3.0);

        super.init();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        // draw "cache"
        if (mBG != null){
            // TODO; Draw OTHER bitmap also
            canvas.drawBitmap(mBG, 0, 0, mBGPaint);
        } else {
            Log.e("ERROR", "NULL ON BG BMP");
        }
    }

}
