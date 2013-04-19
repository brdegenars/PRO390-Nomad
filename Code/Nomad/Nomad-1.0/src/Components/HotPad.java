package Components;

import android.content.Context;
import android.content.Intent;
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

    public HotPad(Context context) {
        super(context);

        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent.createChooser(new Intent(Intent.ACTION_CHOOSER), "Select Application");
                return true;
            }
        });
    }
}
