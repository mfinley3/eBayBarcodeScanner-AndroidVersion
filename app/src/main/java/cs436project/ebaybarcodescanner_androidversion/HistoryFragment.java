package cs436project.ebaybarcodescanner_androidversion;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryFragment extends ListFragment {
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_list, container, false);
        // printall();
        return view;

    }

/*    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        printall();
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                historyList );

        setListAdapter(arrayAdapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position,
                            long id) {

        String[] Itemname = historyList.toArray(new String[historyList.size()]);

        Toast.makeText(getActivity(), Itemname[position], Toast.LENGTH_SHORT)
                .show();

    }

    public void addNewScan(String scan) {
        historyList.add(scan);
    }

    public void printall(){
        System.out.println("sdgsdfgsdfgsfdgsdfgsfdgfdgfdgsf");

        for(String s: historyList){
            System.out.println(s);
        }
    }*/
}
