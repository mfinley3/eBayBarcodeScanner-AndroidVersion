package cs436project.ebaybarcodescanner_androidversion;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EbayFindItem extends  AsyncTask<String, Void, String> {
    protected String doInBackground(String... ISBN) {

        String urlString = "http://open.api.ebay.com/shopping?callname=FindProducts&responseencoding=XML&appid=AlecKret-QRPrice-PRD-2d2cad3ef-86adddf1&siteid=0&version=525&productId.type=ISBN&productId.value=0471152315";// + ISBN;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String XMLContents = XMLReader(connection.getInputStream());
            // Give output for the command line
            //System.out.println(readStream);
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
        return "worked";

    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(String result) {
       // showDialog("Downloaded " + result + " bytes");
    }

    private static String XMLReader(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                System.out.println(nextLine);
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }



}
