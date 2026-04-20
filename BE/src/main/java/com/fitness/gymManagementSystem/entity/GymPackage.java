    package com.fitness.gymManagementSystem.entity;
    
    import jakarta.persistence.*;
    import java.math.BigDecimal;
    
    @Entity
    @Table(name = "packages")
    public class GymPackage {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        @Column(nullable = false, length = 100, unique = true)
    private String packageName;
    
        @Column(nullable = false, precision = 12, scale = 2)
        private BigDecimal price;
    
        @Column(nullable = false)
        private int durationMonths;
    
        @Column(length = 500)
        private String description;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private PackageStatus status = PackageStatus.ACTIVE;
    
        // ===== Getter / Setter =====
    
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    
        public String getPackageName() { return packageName; }
        public void setPackageName(String packageName) { this.packageName = packageName; }
    
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    
        public int getDurationMonths() { return durationMonths; }
        public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }
    
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    
        @Override
        public String toString() {
            return "Package{id=" + id + ", packageName='" + packageName + "', price=" + price
                    + ", durationMonths=" + durationMonths + "}";
        }
    }