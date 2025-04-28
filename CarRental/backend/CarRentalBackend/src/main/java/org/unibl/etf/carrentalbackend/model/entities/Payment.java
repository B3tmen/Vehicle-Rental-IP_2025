package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.unibl.etf.carrentalbackend.model.enums.CardType;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Integer id;


    @Column(name = "card_last_4_digits", nullable = false, columnDefinition = "CHAR(4)")
    private String cardLast4Digits;

    @Column(name = "token", nullable = false, length = 128)
    private String token;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CardType type;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "card_holder_first_name", nullable = false, length = 45)
    private String cardHolderFirstName;

    @Column(name = "card_holder_last_name", nullable = false, length = 45)
    private String cardHolderLastName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_client_id", nullable = false)
    private Client client;

}