/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xml.lab;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 *
 * @author Daemon
 */
public class XMLLab {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        URL url = new URL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=10/xml");
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        // Reading the feed
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List entries = feed.getEntries();
        Iterator itEntries = entries.iterator();

        while (itEntries.hasNext()) {
            SyndEntry entry = (SyndEntry) itEntries.next();
            String Region = entry.getLink().split("/")[3];
            String ID = entry.getLink().split("/")[6].split("\\?")[0].substring(2);
            getCustomerXML(Region, ID);

        }

    }

    public static void getCustomerXML(String Region, String ID) throws MalformedURLException, IllegalArgumentException, IOException, FeedException {
        URL url = new URL("http://ax.itunes.apple.com/" + Region + "/rss/customerreviews/id=" + ID + "/sortBy=mostRecent/xml");
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        // Reading the feed
        SyndFeedInput input = new SyndFeedInput();

        SyndFeed feed = input.build(new XmlReader(httpcon));

        List entries = feed.getEntries();
        Iterator itEntries = entries.iterator();

        while (itEntries.hasNext()) {
            SyndEntry entry = (SyndEntry) itEntries.next();
            String[] S = NGram(entry.getTitle(), 3);
            System.out.println(S);
        }
    }

    public static String[] NGram(String s, int len) {
        String[] parts = s.split(" ");
        String[] result=null;
        if (parts.length - len + 1 > 0) {
            result = new String[parts.length - len + 1];
            for (int i = 0; i < parts.length - len + 1; i++) {
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < len; k++) {
                    if (k > 0) {
                        sb.append(' ');
                    }
                    sb.append(parts[i + k]);
                }
                result[i] = sb.toString();
            }
        }
        return result;
    }

}
