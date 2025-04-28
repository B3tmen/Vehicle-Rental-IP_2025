package org.unibl.etf.promotionsapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion implements Serializable {
    private int id;
    private String title;
    private String description;
    private Date duration;

}
