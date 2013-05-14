package ApplicationListAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/13/13
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationListAdapter extends ArrayAdapter<ApplicationListItem> {

    public ApplicationListAdapter(Context context, int resource, int textViewResourceId, List<ApplicationListItem> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View view = super.getView(position,convertView,parent);
        TextView textView = (TextView)view.findViewById(R.id)
    }
}
