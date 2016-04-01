package cs436project.ebaybarcodescanner_androidversion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class EbayFindItem extends AsyncTask<String, Void, HashMap<String, Object>> {

    Activity mActivity;

    public EbayFindItem(Activity activity) {
        mActivity = activity;
    }


    protected HashMap<String, Object> doInBackground(String... ISBN) {
        HashMap<String, Object> XMLContents = null;
        String urlString = "http://open.api.ebay.com/shopping?callname=FindProducts&responseencoding=XML&appid=AlecKret-QRPrice-PRD-2d2cad3ef-86adddf1&siteid=0&version=525&productId.type=ISBN&productId.value=" + ISBN[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            XMLContents = XMLParse.parse(connection.getInputStream());
            connection.disconnect();
            if(XMLContents.containsKey("image")) {
                Bitmap img = BitmapFactory.decodeStream((InputStream) new URL((String)XMLContents.get("image")).getContent());
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(img, 175, 235, false);
                XMLContents.remove("image");
                XMLContents.put( "image", resizedBitmap);
            }

            //TODO Get the price from eBay, the xml file seems to lack that information
            //urlString = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.12.0&SECURITY-APPNAME=AlecKret-QRPrice-PRD-2d2cad3ef-86adddf1&RESPONSE-DATA-FORMAT=XML&REST-PAYLOAD&paginationInput.entriesPerPage=1&keywords=0471152315&sortOrder=PricePlusShippingLowest";
            //connection = (HttpURLConnection) url.openConnection();
            //XMLReader(connection.getInputStream());
            //connection.disconnect();

            //XMLContents.put("price", XMLParse.parsePrice(connection.getInputStream()));

        } catch (Exception e) {
            e.printStackTrace();
            return XMLContents;
        }
        return XMLContents;

    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(HashMap<String, Object> result) {
        if(result.containsKey("title")) {
            TextView titleTextView = (TextView)mActivity.findViewById(R.id.title);
            titleTextView.setText((String) result.get("title"));
        }

        if(result.containsKey("image")) {
            ImageView img = (ImageView) mActivity.findViewById(R.id.bookPhoto);
            img.setImageBitmap((Bitmap) result.get("image"));
        }
    }

    private String filterTitle(String XMLContents) {
        int endTitle = XMLContents.indexOf("by");
        if (endTitle != -1)
            return XMLContents.substring(0 , endTitle);
        return XMLContents;
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
