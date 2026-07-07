package com.portfolio.changa_api.model;

import com.portfolio.changa_api.shared.enums.States;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shifts")
@EqualsAndHashCode
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "client_address", nullable = false)
    private String clientAddress;

    @Column(name = "client_full_name", nullable = false)
    private String clientFullName;

    @Column(name = "client_phone_number", nullable = false)
    private String clientPhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private States state;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
