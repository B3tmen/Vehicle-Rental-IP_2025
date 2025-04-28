package org.unibl.etf.promotionsapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager implements Serializable {
    private int id;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;

}
