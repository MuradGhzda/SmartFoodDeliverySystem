package org.example.dto.unsplash;

import lombok.Data;

@Data
public class UnsplashUrls {
    private String regular;  // 1080px genişliğinde resim URL'i
    private String small;    // 400px genişliğinde resim URL'i
}