package cs436project.ebaybarcodescanner_androidversion;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


public class HistoryFragment extends ListFragment {


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        ApplicationState state = ((ApplicationState) getActivity().getApplicationContext());

        ArrayList<HashMap<String,Object>> history = state.getHistory();

        ((HomepageActivity)getActivity()).selectFromHistory(history.get(position).get("barcode").toString());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        ApplicationState state = ((ApplicationState) getActivity().getApplicationContext());
        ArrayList<HashMap<String,Object>> history = state.getHistory();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<Bitmap> images = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();

        for(HashMap hash : history){
            titles.add(filterTitle(hash.get("title").toString()));
            images.add((Bitmap) hash.get("image"));
            prices.add("Lowest price was: $" + hash.get("price").toString());
        }

        String[] values = titles.toArray(new String[titles.size()]);
        Bitmap[] image = images.toArray(new Bitmap[images.size()]);
        String[] price = prices.toArray(new String[prices.size()]);

        CustomListAdapter adapter = new CustomListAdapter(getActivity(), values, image, price);
        setListAdapter(adapter);


        return view;
    }
    private String filterTitle(String XMLContents) {
        int endTitle = XMLContents.indexOf("by");
        if (endTitle != -1)
            return XMLContents.substring(0 , endTitle);
        return XMLContents;
    }
}
