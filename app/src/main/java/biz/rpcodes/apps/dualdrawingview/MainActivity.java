package biz.rpcodes.apps.dualdrawingview;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DualD";
    Handler h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h = new Handler();
        updateDrawingView();
    }

    // TODO: memort leak?
    private void updateDrawingView(){
        Log.i(TAG, "updateDrawingView");
        DrawingView dv = (DrawingView) findViewById(R.id.NormaldDrawingView);

        DualDrawingView dd = (DualDrawingView) findViewById(R.id.drawingView);
        dd.setOverlayBitmap(dv.getBitmap());
        dd.invalidate();

        ImageView i = (ImageView )  findViewById(R.id.imageView);
        i.setImageBitmap(dd.getBitmap());
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDrawingView();

            }
        }, 2000);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
