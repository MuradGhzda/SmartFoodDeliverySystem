package org.example.dto.unsplash;

import lombok.Data;

import java.util.List;

@Data
public class UnsplashResponse {
    private List<UnsplashResult> results;
}