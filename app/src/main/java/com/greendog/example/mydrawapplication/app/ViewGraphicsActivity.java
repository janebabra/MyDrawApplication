package com.greendog.example.mydrawapplication.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class ViewGraphicsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        public MyView(Context context) {
            super(context);
        }

        @Override
        public void onDraw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(30);
            paint.setAntiAlias(true);
            canvas.drawColor(Color.YELLOW);
            canvas.drawText("Android Draw in View", 40, 40, paint);
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.android);
            canvas.drawBitmap(image, 40, 80, null);
        }
    }
}