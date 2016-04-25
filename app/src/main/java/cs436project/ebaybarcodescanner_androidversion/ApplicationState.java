package cs436project.ebaybarcodescanner_androidversion;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mfin3 on 4/24/2016.
 */
public class ApplicationState extends Application {

    private ArrayList<HashMap<String, Object>> history = new ArrayList<>();

    public ArrayList<HashMap<String, Object>> getHistory() {
        return history;
    }

    public void addHistory(HashMap<String, Object> book) {
        history.add(0, book);
        if(history.size() > 10)
            history.remove(history.size() - 1);

    }



}
