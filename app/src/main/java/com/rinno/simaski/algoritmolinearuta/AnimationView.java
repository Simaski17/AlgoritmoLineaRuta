package com.rinno.simaski.algoritmolinearuta;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by simaski on 25/01/17.
 */


public class AnimationView extends View {
    Path path;
    Paint paint;
    float length;
    public String partes;

    public AnimationView(Context context)
    {
        super(context);
    }

    public AnimationView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    class Pt{
        float x, y;
        Pt(float _x, float _y){
            x = _x;
            y = _y;
        }
    }



    Pt[] myPath = { new Pt(200, 200),

            new Pt(300, 100),

            new Pt(400, 200),

            new Pt(500, 100),

            new Pt(600, 200),

            new Pt(700, 100),

            new Pt(900, 100),

            new Pt(900, 500),

            new Pt(200, 500),

            new Pt(200, 200)

    };

    public void init()
    {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);

        path = new Path();

        /*path.moveTo(200, 200);
        path.lineTo(300, 100);
        path.lineTo(400, 200);
        path.lineTo(500, 100);
        path.lineTo(600, 200);
        path.lineTo(700, 100);
        path.lineTo(900, 100);*/

        path.moveTo(myPath[0].x, myPath[0].y);

        for (int i = 1; i < myPath.length; i++){

            path.lineTo(myPath[i].x, myPath[i].y);

        }

        // Measure the path
        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

        float[] intervals = new float[]{length, length};

        ObjectAnimator animator = ObjectAnimator.ofFloat(AnimationView.this, "phase", 1.0f, 0.0f);
        animator.setDuration(7000);
        animator.start();

    }

    //is called by animtor object
    public void setPhase(float phase)
    {
        Log.d("pathview","setPhase called with:" + String.valueOf(phase));
        paint.setPathEffect(createPathEffect(length, phase, 0.0f));
        invalidate();//will calll onDraw
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset)
    {
        return new DashPathEffect(new float[] {
                pathLength, pathLength
        },
                Math.max(phase * pathLength, offset));
    }

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        c.drawPath(path, paint);
    }


    public void onResume() {
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessage(Message event) {
        partes = event.getPartes();
        Log.e("TAG","RECIBIDO: "+partes);
    }

}