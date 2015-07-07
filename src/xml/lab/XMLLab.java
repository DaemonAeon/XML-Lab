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
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.jdom.Element;

/**
 *
 * @author Daemon
 */
public class XMLLab {

    private static HashMap<String, Integer> mapGood;
    private static HashMap<String, Integer> mapBad;
    private static int GoodCounter = 0;
    private static int BadCounter = 0;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("FREE APPS");
        getAll("topfreeapplications");
        System.out.println();
        System.out.println("PAID APPS");
        getAll("toppaidapplications");
        
        
    }

    public static void getAll(String LISTOF) throws MalformedURLException, IOException, IllegalArgumentException, FeedException, Exception {
        mapGood = new HashMap<>();
        mapBad = new HashMap<>();

        URL url = new URL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/"+LISTOF+"/limit=10/xml");
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();

        // Reading the feed
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List entries = feed.getEntries();
        Iterator itEntries = entries.iterator();

        while (itEntries.hasNext()) {
            SyndEntry entry = (SyndEntry) itEntries.next();
            String list = entry.getLink().split("/")[6].split("\\?")[0].substring(2);
            getIDRSS(list);
        }

        System.out.println("# of Good Comments " + GoodCounter);
        System.out.println("TOP 10 Good Comments");

        ValueComparator vc = new ValueComparator(mapGood);
        TreeMap<String, Integer> tc = new TreeMap<>(vc);
        tc.putAll(mapGood);
        for (int i = 0; i < 10; i++) {
            System.out.println(tc.keySet().toArray()[i]);
        }
        System.out.println();
        System.out.println("================================");
        System.out.println();
        System.out.println("# of Bad Comments " + GoodCounter);
        System.out.println("TOP 10 Bad Comments");

        vc = new ValueComparator(mapBad);
        tc = new TreeMap<>(vc);
        tc.putAll(mapBad);
        for (int i = 0; i < 10; i++) {
            System.out.println(tc.keySet().toArray()[i]);
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
            SyndContentImpl syc = (SyndContentImpl) x.getContents().get(0);
            String value = syc.getValue();

            List<Element> foreign = (List<Element>) x.getForeignMarkup();
            for (Element y : foreign) {;
                if (y.getName().equals("rating")) {
                    int rating = Integer.parseInt(y.getValue());
                    if (rating >= 3) {
                        GoodCounter++;
                        String[] ngram = NGram(value.trim(), 2);
                        for (String h : ngram) {
                            if (!mapGood.containsKey(h)) {
                                mapGood.put(h, 0);
                            } else {
                                int index = mapGood.get(h);
                                mapGood.put(h, ++index);
                            }
                        }
                    } else {
                        BadCounter++;
                        String[] ngram = NGram(value.trim(), 2);
                        for (String h : ngram) {
                            if (!mapBad.containsKey(h)) {
                                mapBad.put(h, 0);
                            } else {
                                int index = mapBad.get(h);
                                mapBad.put(h, ++index);
                            }
                        }
                    }
                }
            }

        }
    }

    public static String[] NGram(String s, int len) {
        String[] parts = s.split(" ");
        String[] result = null;
        if (parts.length - len + 1 >= 0) {
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

    static class ValueComparator implements Comparator<String> {

        HashMap<String, Integer> base;

        public ValueComparator(HashMap<String, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.    
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }

}
