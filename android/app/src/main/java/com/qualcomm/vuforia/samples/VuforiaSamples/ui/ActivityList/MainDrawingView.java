package com.qualcomm.vuforia.samples.VuforiaSamples.ui.ActivityList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by geoff on 8/3/14.
 */
public class MainDrawingView extends View {
    private Paint paint = new Paint();
    private Path path = new Path();

    public MainDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setColor(Color.parseColor("#111111"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                postTouchEvent(event);
                path.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_UP:
                postTouchEvent(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                postTouchEvent(event);
                path.lineTo(eventX, eventY);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    // send DOWNs, batch MOVEs
    private void postTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        String eventType = eventActionToString(event.getAction());
        long t = event.getEventTime();

        RequestParams data = new RequestParams();
        data.put("x", Float.toString(x));
        data.put("y", Float.toString(y));
        data.put("t", Long.toString(t));
        data.put("e", eventType);
        postData(data);
    }

    private static String eventActionToString(int eventAction) {
        switch (eventAction) {
            case MotionEvent.ACTION_CANCEL: return "Cancel";
            case MotionEvent.ACTION_DOWN: return "Down";
            case MotionEvent.ACTION_MOVE: return "Move";
            case MotionEvent.ACTION_OUTSIDE: return "Outside";
            case MotionEvent.ACTION_UP: return "Up";
            case MotionEvent.ACTION_POINTER_DOWN: return "Pointer Down";
            case MotionEvent.ACTION_POINTER_UP: return "Pointer Up";
            default: return "";
        }
    }

    private void postData(RequestParams data) {
        Log.d("LOGGING", "Sending postData");
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.42.33:8080/", data, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) { }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
        });
    }
}
