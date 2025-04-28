package org.unibl.etf.promotionsapp.model.beans;

import lombok.Data;
import org.unibl.etf.promotionsapp.model.dto.Announcement;
import org.unibl.etf.promotionsapp.service.AnnouncementService;

import java.io.Serializable;
import java.util.List;

@Data
public class AnnouncementBean implements Serializable {
    private List<Announcement> announcements;
    private AnnouncementService announcementService;

    public AnnouncementBean(String token) {
        this.announcementService = new AnnouncementService(token);
        this.announcements = announcementService.getAll();
    }

}
