package com.rentflow.rental;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="payments")
public class Payment {
    @Id @Column(columnDefinition="char(36)") private UUID id;
    @Column(name="company_id",columnDefinition="char(36)",nullable=false) private UUID companyId;
    @Column(name="booking_id",columnDefinition="char(36)",nullable=false) private UUID bookingId;
    @Column(nullable=false,precision=12,scale=2) private BigDecimal amount;
    @Column(name="payment_method",nullable=false) private String paymentMethod;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private PaymentStatus status;
    @Column(name="transaction_reference") private String transactionReference;
    @Column(name="payment_date",nullable=false) private Instant paymentDate;
    private String notes;
    @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
    public UUID getId(){return id;} public void setId(UUID v){id=v;}
    public UUID getCompanyId(){return companyId;} public void setCompanyId(UUID v){companyId=v;}
    public UUID getBookingId(){return bookingId;} public void setBookingId(UUID v){bookingId=v;}
    public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
    public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String v){paymentMethod=v;}
    public PaymentStatus getStatus(){return status;} public void setStatus(PaymentStatus v){status=v;}
    public String getTransactionReference(){return transactionReference;} public void setTransactionReference(String v){transactionReference=v;}
    public Instant getPaymentDate(){return paymentDate;} public void setPaymentDate(Instant v){paymentDate=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}
