package org.unibl.etf.promotionsapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement implements Serializable {
    private int id;
    private String title;
    private String content;
}
