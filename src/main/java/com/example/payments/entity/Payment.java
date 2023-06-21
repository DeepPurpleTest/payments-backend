package com.example.payments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_sender", referencedColumnName = "card_number")
    private Card sender;
    @ManyToOne
    @JoinColumn(name = "card_receiver", referencedColumnName = "card_number")
    private Card receiver;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Column(name = "date")
    private LocalDateTime date;
}