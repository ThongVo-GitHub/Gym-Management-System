package com.fitness.gymManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fitness.gymManagementSystem.entity.InvoiceStatus;
import com.fitness.gymManagementSystem.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InvoiceResponse(

    Long id,

    // USER INFO
    Long userId,
    String username,

    // PACKAGE INFO
    Long packageId,
    String packageName,
    BigDecimal price,

    // PAYMENT INFO
    PaymentMethod paymentMethod,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate paymentDate,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate expiredDate,

    // STATUS
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    InvoiceStatus status,

    // OPTIONAL (payment gateway / VNPay / Stripe / etc)
    String txnRef,

    // AUDIT (VERY IMPORTANT FOR DEBUG)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    Instant createdAt
) {}