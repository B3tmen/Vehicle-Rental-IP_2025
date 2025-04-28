package org.unibl.etf.clientapp.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Manufacturer {
    private Integer id;
    private String name;
    private String state;
    private String address;
    private String phoneNumber;
    private String fax;
    private String email;
}
