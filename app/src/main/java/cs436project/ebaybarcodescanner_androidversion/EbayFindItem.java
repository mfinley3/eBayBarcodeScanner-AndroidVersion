package cs436project.ebaybarcodescanner_androidversion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class EbayFindItem extends AsyncTask<String, Void, HashMap<String, Object>> {

    Activity mActivity;
    ProgressDialog progressSpinner = null;

    public interface AsyncResponse {
        void processFinish(HashMap<String, Object> output);
    }

    public AsyncResponse delegate = null;

    public EbayFindItem(Activity activity, AsyncResponse delegate) {
        this.delegate = delegate;
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {

        progressSpinner  = new ProgressDialog(mActivity);
        progressSpinner.setMessage("Searching eBay for the book. Please wait.");
        progressSpinner.show();

    }

    protected HashMap<String, Object> doInBackground(String... ISBN) {
        HashMap<String, Object> XMLContents = null;
        HashMap<String, Object> XMLPriceContents;
        String urlString = "http://open.api.ebay.com/shopping?callname=FindProducts&responseencoding=XML&appid=AlecKret-QRPrice-PRD-2d2cad3ef-86adddf1&siteid=0&version=525&productId.type=ISBN&productId.value=" + ISBN[0];
        String priceUrlString = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.12.0&SECURITY-APPNAME=AlecKret-QRPrice-PRD-2d2cad3ef-86adddf1&RESPONSE-DATA-FORMAT=XML&REST-PAYLOAD&paginationInput.entriesPerPage=1&keywords=" + ISBN[0] + "&sortOrder=PricePlusShippingLowest";

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

        } catch (Exception e) {
            e.printStackTrace();
            return XMLContents;
        }

        try {
            URL url = new URL(priceUrlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            XMLPriceContents = XMLParse.parsePrice(connection.getInputStream());
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            return XMLContents;
        }
        XMLContents.putAll(XMLPriceContents);
        return XMLContents;

    }

    protected void onProgressUpdate(Integer... progress) {

    }

//    private String filterTitle(String XMLContents) {
//            int endTitle = XMLContents.indexOf("by");
//                if (endTitle != -1)
//                    return XMLContents.substring(0 , endTitle);
//            return XMLContents;
//    }


    protected void onPostExecute(HashMap<String, Object> result) {
        if(result.containsKey("title")) {
            TextView titleTextView = (TextView)mActivity.findViewById(R.id.title);
            titleTextView.setText((String) result.get("title"));
            TextView headTextView = (TextView)mActivity.findViewById(R.id.eBayListHead);
            headTextView.setText("eBay Listing Found!");
            headTextView.setPaintFlags(headTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            headTextView.setClickable(false);
            titleTextView.setClickable(false);

//            result.put("title", filterTitle((String) result.get("title")));

        } else {
            TextView headTextView = (TextView)mActivity.findViewById(R.id.eBayListHead);
            headTextView.setText("We couldn't find this book");
            headTextView.setClickable(true);
            TextView titleTextView = (TextView)mActivity.findViewById(R.id.title);
            titleTextView.setText("This may mean the book is listed differently.\nClick here to do a manual search.");
            titleTextView.setClickable(true);
            super.onPostExecute(result);
            progressSpinner.dismiss();
            return;
        }

        if(result.containsKey("image")) {
            ImageView img = (ImageView) mActivity.findViewById(R.id.bookPhoto);
            img.setImageBitmap((Bitmap) result.get("image"));
        } else {
            ImageView img = (ImageView) mActivity.findViewById(R.id.bookPhoto);
            img.setImageResource(R.mipmap.ic_no_image_aval);
        }

        if(result.containsKey("price")) {
            TextView priceTextView = (TextView)mActivity.findViewById(R.id.priceView);
            priceTextView.setText("The lowest price on eBay is: $" + (String) result.get("price"));
        } else {
            TextView priceTextView = (TextView)mActivity.findViewById(R.id.priceView);
            priceTextView.setText("Could not find price information on eBay.\n This usually means there are no current listings.");
        }

        if(result.containsKey("categoryId")) {
            result.put("catID", result.get("categoryId"));
        }

        delegate.processFinish(result);
        super.onPostExecute(result);
        progressSpinner.dismiss();
    }
}
