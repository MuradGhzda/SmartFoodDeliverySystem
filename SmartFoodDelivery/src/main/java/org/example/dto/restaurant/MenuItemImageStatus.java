package org.example.dto.restaurant;

import lombok.Data;

@Data
public class MenuItemImageStatus {
    private Long menuItemId;
    private String name;
    private boolean hasImage;
    private String imageUrl;
}