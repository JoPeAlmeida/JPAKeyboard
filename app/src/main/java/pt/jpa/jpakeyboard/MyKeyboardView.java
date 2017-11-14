package pt.jpa.jpakeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by sebasi on 21/07/2017.
 */

public class MyKeyboardView extends KeyboardView {

    private Context context;

    public MyKeyboardView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        context = ctx;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        Typeface font = Typeface.createFromAsset(context.getAssets(),
                "jpaFont.ttf"); //Insert your font here.
        paint.setTypeface(font);

        List<Key> keys = getKeyboard().getKeys();
        for(Key key: keys) {
            if(key.label != null)
                canvas.drawText(key.label.toString(), key.x, key.y, paint);
        }
    }
}
