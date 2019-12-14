package io.agaghd.rphonesystem.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

public class RecordSurface extends SurfaceTemplete {

    public RecordSurface(Context context) {
        super(context);
    }

    public RecordSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RecordSurface(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDrawingOnSurface(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(200, 200, 200, paint);
    }
}
