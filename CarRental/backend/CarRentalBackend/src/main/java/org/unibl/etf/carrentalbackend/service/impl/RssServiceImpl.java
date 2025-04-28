package org.unibl.etf.carrentalbackend.service.impl;

import com.rometools.rome.feed.synd.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.model.entities.Announcement;
import org.unibl.etf.carrentalbackend.model.entities.Promotion;
import org.unibl.etf.carrentalbackend.repository.AnnouncementRepository;
import org.unibl.etf.carrentalbackend.repository.PromotionRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.RssService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.*;

@Service
@PropertySource("classpath:application.properties")
public class RssServiceImpl implements RssService {
    private static final String PROMOTIONS_LINK = SERVER_DOMAIN_URL + API_PROMOTIONS_URL + "/";
    private static final String ANNOUNCEMENTS_LINK = SERVER_DOMAIN_URL + API_ANNOUNCEMENTS_URL + "/";

    @Value("${rss.link}")
    private String rssLink;
    private final PromotionRepository promotionRepository;
    private final AnnouncementRepository announcementRepository;

    @Autowired
    public RssServiceImpl(PromotionRepository promotionRepository, AnnouncementRepository announcementRepository) {
        this.promotionRepository = promotionRepository;
        this.announcementRepository = announcementRepository;
    }

    @Override
    public SyndFeed generateFeed() {
        SyndFeed feed = new SyndFeedImpl();
        setupRssMetadata(feed);

        List<SyndEntry> entries = new ArrayList<>();
        List<Promotion> promotions = promotionRepository.findAll();
        List<Announcement> announcements = announcementRepository.findAll();

        setupPromotionEntries(entries, promotions);
        setupAnnouncementEntries(entries, announcements);

        feed.setEntries(entries);
        return feed;
    }

    private void setupRssMetadata(SyndFeed feed){
        feed.setFeedType("rss_2.0");
        feed.setTitle("Vehicle Rental Promotions & Announcements");
        feed.setLink(rssLink);
        feed.setDescription("Stay updated with the latest promotions and announcements on vehicles for rental!");
        feed.setPublishedDate(new Date());
    }

    private void setupPromotionEntries(List<SyndEntry> entries, List<Promotion> promotions){
        promotions.forEach(promo -> {
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle("Promotion: " + promo.getTitle());
            entry.setLink(PROMOTIONS_LINK + promo.getId());
            entry.setPublishedDate(promo.getDuration());

            SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(promo.getDescription() + "\nExpires on: " + promo.getDuration());

            entry.setDescription(description);
            entries.add(entry);
        });
    }

    private void setupAnnouncementEntries(List<SyndEntry> entries, List<Announcement> announcements){
        announcements.forEach(announcement -> {
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle("Announcement: " + announcement.getTitle());
            entry.setLink(ANNOUNCEMENTS_LINK + announcement.getId()); // Optional detailed view link
            entry.setPublishedDate(new Date());

            SyndContent content = new SyndContentImpl();
            content.setType("text/plain");
            content.setValue(announcement.getContent());

            entry.setDescription(content);
            entries.add(entry);
        });
    }
}
