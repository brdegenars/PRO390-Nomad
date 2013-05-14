package ApplicationListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.Nomad.R;

/**
 * Created with IntelliJ IDEA.
 * User: brdegenaars
 * Date: 5/13/13
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationListAdapter extends ArrayAdapter<ApplicationListItem> {

    private Context context;
    private int layoutResourceId;
    private ApplicationListItem[] applicationListItems;

    public ApplicationListAdapter(Context context, int layoutResourceId, ApplicationListItem[] itemData) {
        super(context, layoutResourceId, itemData);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.applicationListItems = itemData;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View rowItem = convertView;
        ApplicationListItemPlaceholder listItemPlaceholder;

        if (rowItem == null){

            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            rowItem = layoutInflater.inflate(layoutResourceId, parent, false);

            listItemPlaceholder = new ApplicationListItemPlaceholder();
            listItemPlaceholder.imageView = (ImageView)rowItem.findViewById(R.id.applicationListItemImage);
            listItemPlaceholder.textView = (TextView)rowItem.findViewById(R.id.applicationListItemText);

            rowItem.setTag(listItemPlaceholder);
        } else {
            listItemPlaceholder = (ApplicationListItemPlaceholder)rowItem.getTag();
        }

        ApplicationListItem applicationListItem = applicationListItems[position];
        listItemPlaceholder.imageView.setImageDrawable(applicationListItem.getImageResourceDrawable());
        listItemPlaceholder.textView.setText(applicationListItem.getApplicationName());

        return rowItem;
    }

    static class ApplicationListItemPlaceholder {

        ImageView imageView;
        TextView textView;
    }
}
