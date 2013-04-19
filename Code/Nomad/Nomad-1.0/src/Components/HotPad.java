package Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 4/19/13
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class HotPad extends ImageButton{

    private Bitmap displayImage = null;

    public HotPad(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }
}
