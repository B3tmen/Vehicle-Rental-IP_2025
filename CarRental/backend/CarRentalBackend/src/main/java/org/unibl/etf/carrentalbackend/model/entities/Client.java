package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.unibl.etf.carrentalbackend.model.enums.CitizenType;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client")
@PrimaryKeyJoinColumn(name = "fk_user_id")
@Data
public class Client extends User {
    @Column(name = "personal_card_number", nullable = false, columnDefinition = "CHAR(13)")
    private String personalCardNumber;

    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 45)
    private String phoneNumber;

    @Column(name = "citizen_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CitizenType citizenType;

    @Column(name = "drivers_licence")
    private Integer driversLicence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_avatar_image_id")
    private Image avatarImage;


}