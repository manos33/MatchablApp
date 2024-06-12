package com.example.matchabl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class CircularImageView extends AppCompatImageView {
    private Path path;

    public CircularImageView(Context context) {
        super(context);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        path.reset();
        path.addCircle(w / 2f, h / 2f, Math.min(w, h) / 2f, Path.Direction.CCW);
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
