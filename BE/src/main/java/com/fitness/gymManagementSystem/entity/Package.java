package com.fitness.gymManagementSystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "packages")
public class Package {

    @Id
    private String id; // VD: P01, P02

    @Column(nullable = false)
    private String packageName;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double durationMonths; // 1, 3, 6, 12

    private String description;

    // ===== Getter / Setter =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(double durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ===== toString =====

    @Override
    public String toString() {
        return "GymPackage{" +
                "id='" + id + '\'' +
                ", packageName='" + packageName + '\'' +
                ", price=" + price +
                ", durationMonths=" + durationMonths +
                ", description='" + description + '\'' +
                '}';
    }
}