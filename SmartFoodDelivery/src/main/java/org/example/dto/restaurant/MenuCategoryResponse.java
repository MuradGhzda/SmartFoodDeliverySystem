package org.example.dto.restaurant;

import lombok.Data;

import java.util.List;

@Data
public class MenuCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private List<MenuItemResponse> items;
}
