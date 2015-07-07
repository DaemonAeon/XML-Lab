/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xml.lab;

import com.sun.syndication.feed.synd.SyndContentImpl;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.MalformedURLException;
import org.jdom.Element;

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
            String list = entry.getLink().split("/")[6].split("\\?")[0].substring(2);
            System.out.println("ID: " + list);
            getIDRSS(list);
            System.out.println("-------------------------------------------");
        }
    }

    public static void getIDRSS(String id) throws Exception {
        URL url = new URL("http://ax.itunes.apple.com/us/rss/customerreviews/id=" + id + "/sortBy=mostRecent/xml");
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        // Reading the feed
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List<SyndEntry> entries = feed.getEntries();
        for (SyndEntry x : entries) {
            SyndContentImpl syc = (SyndContentImpl)x.getContents().get(0);
            String value = syc.getValue();
            List<Element> foreign = (List<Element>) x.getForeignMarkup();
            for (Element y : foreign) {;
                if (y.getName().equals("rating")) {
                    System.out.println(value);
                    System.out.println(y.getValue());
                }
            }
        }
    }

}
