package cs436project.ebaybarcodescanner_androidversion;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by mfin3 on 3/29/2016.
 */
public class XMLParse {
    private static final String ns = null;

    public static HashMap<String, Object> parse(InputStream in) throws XmlPullParserException, IOException {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }

    }

    public static String parsePrice(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readPriceFeed(parser);
        } finally {
            in.close();
        }
    }

    // TODO get xml data that contains pricing info
    private static String readPriceFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        System.out.println("______________________________________________");
        String price = "7";
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            System.out.println(name);
            if(name == null)
                continue;

        }
        System.out.println("______________________________________________");
        return price;
    }


    private static HashMap<String, Object> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        HashMap<String, Object> XMLContents = new HashMap<String, Object>();
        //parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            System.out.println(name);
            if(name == null)
                continue;
            else if(name.equals("Title"))
                XMLContents.put("title", readTitle(parser));
            else if(name.equals("StockPhotoURL"))
                XMLContents.put("image", readURL(parser));

        }
        return XMLContents;
    }

    private static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Title");
        return title;
    }

    private static String readURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "StockPhotoURL");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "StockPhotoURL");
        return title;
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
