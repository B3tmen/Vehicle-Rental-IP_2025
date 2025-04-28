package org.unibl.etf.carrentalbackend.service.interfaces;

import com.rometools.rome.feed.synd.SyndFeed;

public interface RssService {
    SyndFeed generateFeed();
}
