package com.fitness.gymManagementSystem.dto;

public class BuyMembershipRequest {
    private String packageId;
    private String paymentMethod;

    public BuyMembershipRequest() {}

    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
