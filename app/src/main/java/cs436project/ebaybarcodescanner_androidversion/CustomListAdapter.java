package cs436project.ebaybarcodescanner_androidversion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mfin3 on 4/25/2016.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] titles;
    private final Bitmap[] images;
    private final String[] prices;

    public CustomListAdapter(Activity context, String[] titles, Bitmap[] images, String[] prices) {
        super(context, R.layout.history_single_item, titles);
        this.context = context;
        this.titles = titles;
        this.images = images;
        this.prices = prices;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.history_single_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.listtxt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listimg);
        TextView txtPrice = (TextView) rowView.findViewById(R.id.listprice);

        txtTitle.setText(titles[position]);
        imageView.setImageBitmap(images[position]);
        txtPrice.setText(prices[position]);

        return rowView;
    }
}
