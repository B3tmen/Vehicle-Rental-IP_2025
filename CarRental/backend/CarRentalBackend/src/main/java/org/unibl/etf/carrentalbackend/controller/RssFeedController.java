package org.unibl.etf.carrentalbackend.controller;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.carrentalbackend.service.interfaces.RssService;

import java.io.StringWriter;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_RSS_URL;

@RestController
@RequestMapping(API_RSS_URL)
public class RssFeedController {
    private final RssService rssService;

    @Autowired
    public RssFeedController(RssService rssService) {
        this.rssService = rssService;
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public String consumeFeed(){
        try{
            SyndFeed feed = rssService.generateFeed();
            SyndFeedOutput output = new SyndFeedOutput();
            StringWriter writer = new StringWriter();
            output.output(feed, writer);

            return writer.toString();
        } catch (Exception e){
            e.printStackTrace();
            return "<error>Unable to generate RSS feed</error>";
        }
    }
}
