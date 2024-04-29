package com.swygbro.trip.backend.domain.reservation.domain;

import com.swygbro.trip.backend.domain.guideProduct.domain.GuideProduct;
import com.swygbro.trip.backend.domain.reservation.dto.SavePaymentRequest;
import com.swygbro.trip.backend.domain.user.domain.User;
import com.swygbro.trip.backend.global.entity.BaseEntity;
import com.swygbro.trip.backend.global.status.PayStatus;
import com.swygbro.trip.backend.global.status.PayStatusConverter;
import com.swygbro.trip.backend.global.status.ReservationStatus;
import com.swygbro.trip.backend.global.status.ReservationStatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "guide_id")
    private User guide;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private GuideProduct product;

    @Column(nullable = false)
    private Timestamp reservatedAt;

    @Column(nullable = false)
    private Integer personnel;

    private String message;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @Convert(converter = PayStatusConverter.class)
    private PayStatus paymentStatus;

    @Column(nullable = false)
    @Convert(converter = ReservationStatusConverter.class)
    private ReservationStatus reservationStatus;

    private String merchantUid;

    private String impUid;

    private Timestamp paidAt;

    private Timestamp cancelledAt;

    public void UpdatePaymentReservation(SavePaymentRequest savePaymentRequest) {
        this.impUid = savePaymentRequest.getImpUid();
        this.paidAt = new Timestamp(savePaymentRequest.getPaidAt() * 1000);
        this.paymentStatus = PayStatus.COMPLETE;
    }

    public void cancelReservation() {
        this.reservationStatus = ReservationStatus.CANCELLED;
    }

    public void confirmReservation() {
        this.reservationStatus = ReservationStatus.RESERVED;
    }

    public void settleReservation() {
        this.reservationStatus = ReservationStatus.SETTLED;
    }

    public void refundPayment(Date cancelledAt) {
        this.paymentStatus = PayStatus.REFUNDED;
        this.cancelledAt = new Timestamp(cancelledAt.getTime());
    }

    public void generateMerchantUid() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-");
        String prefix = dateFormat.format(new Date());

        UUID uuid = UUID.randomUUID();
        String suffix = uuid.toString().substring(0, 6);

        this.merchantUid = prefix + suffix;
    }

}