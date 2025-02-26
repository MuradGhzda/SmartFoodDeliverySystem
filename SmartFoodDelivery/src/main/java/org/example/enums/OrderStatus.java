package org.example.enums;

public enum OrderStatus {
    PENDING("Beklemede"),

    COMPLETED("Tamamlandi"),
    CONFIRMED("Onaylandı"),
    PREPARING("Hazırlanıyor"),
    ON_THE_WAY("Yolda"),
    DELIVERED("Teslim Edildi"),
    CANCELLED("İptal Edildi");



    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}