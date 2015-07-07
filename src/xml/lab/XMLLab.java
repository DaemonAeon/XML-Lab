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
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
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
        HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
        // Reading the feed
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List entries = feed.getEntries();
        Iterator itEntries = entries.iterator();
 
        while (itEntries.hasNext()) {
            SyndEntry entry = (SyndEntry)itEntries.next();
            String mod = entry.getLink().substring(25);
            String[] arreglito = mod.split("/");
            String[] arreglito2 = arreglito[3].split("\\?");
            String ID = arreglito2[0].substring(2);
            System.out.println(ID);
            
        }
    }
    
}
