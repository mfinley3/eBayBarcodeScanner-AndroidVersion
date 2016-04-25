package cs436project.ebaybarcodescanner_androidversion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class HistoryFragment extends ListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container,
                false);

        String[] values = new String[] { "Message1", "Message2", "Message3" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        return rootView;
    }

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

