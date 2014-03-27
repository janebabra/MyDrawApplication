package com.greendog.example.mydrawapplication.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

public class MySurfaceViewActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new MySurface(this));
    }

    class MySurface extends SurfaceView implements SurfaceHolder.Callback {
        private SecondThread thread;
        private int x = 100;
        private int y = 200;
        private boolean isMoving;
        public MySurface(Context context) {
            super(context);
            getHolder().addCallback(this);
            thread = new SecondThread(getHolder(), this);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                x = (int) event.getX();
                y = (int) event.getY();
                isMoving = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                isMoving = false;
            }
            return true;
        }

        @Override
        public void onDraw(Canvas canvas) {
            Paint paint = new Paint();

            paint.setColor(Color.WHITE);

            paint.setTextSize(20);

            paint.setAntiAlias(true);
            canvas.drawColor(0xff000080);
            canvas.drawText("Hello Android", 10, 20, paint);
            canvas.drawText("Example to draw dynamically on canvas", 10, 60, paint);
            canvas.drawText("DRAG ICON TO MOVE", 10, 100, paint);
            Bitmap image = BitmapFactory.decodeResource(getResources(),	R.drawable.little_penguin);
            if (isMoving)
                canvas.drawText("ICON IS MOVING", 10, 130, paint);
            else
                canvas.drawText("ICON IS NOT MOVING", 10, 130, paint);
            canvas.drawText("x is " + x + ": y is " + y, x - 20, y - 20, paint);
            canvas.drawBitmap(image, x, y, null);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {}

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            thread.setRunning(true);
            thread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            thread.setRunning(false);
            while (retry) {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {}
            }
        }
    }

    class SecondThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private MySurface mySurface;
        private boolean _run = false;

        public SecondThread(SurfaceHolder surfaceHolder, MySurface mySurface) {
            this.surfaceHolder = surfaceHolder;
            this.mySurface = mySurface;
        }

        public void setRunning(boolean run) {
            _run = run;
        }

        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        mySurface.onDraw(c);
                    }
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}