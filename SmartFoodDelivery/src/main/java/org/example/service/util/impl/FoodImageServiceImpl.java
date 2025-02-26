package org.example.service.util.impl;

import org.example.service.util.FoodImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.example.dto.unsplash.UnsplashResponse;

import java.util.HashMap;
import java.util.Map;

@Service
public class FoodImageServiceImpl implements FoodImageService {
    @Value("${unsplash.access.key}")
    private String UNSPLASH_ACCESS_KEY;

    private final String UNSPLASH_URL = "https://api.unsplash.com/search/photos";
    private final RestTemplate restTemplate;
    private final Map<String, String> foodTranslations;

    public FoodImageServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.foodTranslations = initializeFoodTranslations();
    }

    private Map<String, String> initializeFoodTranslations() {
        Map<String, String> translations = new HashMap<>();

        // Türk Mutfağı - daha spesifik aramalar
        translations.put("adana kebap", "adana kebab plate turkish food");
        translations.put("urfa kebap", "urfa kebab plate turkish cuisine");
        translations.put("kuzu şiş", "turkish lamb shish kebab plate");
        translations.put("patlıcan kebabı", "turkish eggplant kebab dish");
        translations.put("kıymalı pide", "turkish ground meat pide bread");
        translations.put("kaşarlı pide", "turkish cheese pide");
        translations.put("kuşbaşılı pide", "turkish meat pide traditional");
        translations.put("mercimek çorbası", "turkish red lentil soup");
        translations.put("işkembe çorbası", "turkish tripe soup traditional");
        translations.put("ayran", "ayran turkish yogurt drink");
        translations.put("şalgam", "salgam turkish turnip juice");
        translations.put("karnıyarık", "karniyarik turkish stuffed eggplant");
        translations.put("imam bayıldı", "imam bayildi eggplant dish");
        translations.put("mantı", "turkish manti dumplings plate");
        translations.put("humus", "hummus plate middle eastern");
        translations.put("babagannuş", "baba ganoush authentic");
        translations.put("acılı ezme", "spicy ezme turkish meze");
        translations.put("künefe", "kunafa kunefe dessert");
        translations.put("sütlaç", "turkish rice pudding dessert");

        // İtalyan Mutfağı - gerçek restoran fotoğrafları
        translations.put("margherita", "authentic margherita pizza");
        translations.put("pepperoni pizza", "pepperoni pizza restaurant");
        translations.put("quattro formaggi", "four cheese pizza authentic");
        translations.put("spaghetti carbonara", "authentic carbonara pasta plate");
        translations.put("penne arrabbiata", "penne arrabbiata pasta dish");
        translations.put("bruschetta", "italian bruschetta appetizer");
        translations.put("carpaccio", "beef carpaccio appetizer");
        translations.put("fettuccine alfredo", "fettuccine alfredo pasta plate");
        translations.put("linguine alle vongole", "linguine vongole clams");
        translations.put("risotto ai funghi", "mushroom risotto italian");
        translations.put("risotto alla milanese", "risotto alla milanese saffron");

        // Burgerler ve Sandviçler - profesyonel çekimler
        translations.put("classic burger", "gourmet beef burger restaurant");
        translations.put("klasik burger", "gourmet beef burger restaurant");
        translations.put("spicy chicken burger", "spicy chicken burger restaurant");
        translations.put("chili burger", "spicy beef burger restaurant");
        translations.put("vegan burger", "gourmet vegan burger");
        translations.put("club sandwich", "club sandwich restaurant");
        translations.put("vejeteryan sandwich", "vegetarian sandwich fresh");
        translations.put("tavuk wrap", "chicken wrap sandwich");
        translations.put("falafel wrap", "falafel wrap sandwich");

        // Asya Mutfağı - otantik görüntüler
        translations.put("pad thai", "authentic pad thai noodles");
        translations.put("vegetable noodle", "asian vegetable stir fry noodles");
        translations.put("kung pao chicken", "authentic kung pao chicken");
        translations.put("beef & broccoli", "chinese beef and broccoli");
        translations.put("california roll", "california roll sushi plate");
        translations.put("spicy tuna roll", "spicy tuna roll sushi");
        translations.put("salmon sashimi", "fresh salmon sashimi plate");
        translations.put("tuna sashimi", "fresh tuna sashimi plate");

        // Yan Ürünler ve Mezeler
        translations.put("patates kızartması", "crispy french fries restaurant");
        translations.put("soğan halkası", "crispy onion rings appetizer");

        return translations;
    }

    public String getFoodImage(String foodName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Client-ID " + UNSPLASH_ACCESS_KEY);

            // Yemek adını çevir ve arama terimini optimize et
            String searchTerm = translateFoodName(foodName.toLowerCase());
            System.out.println("Searching for: " + searchTerm); // Debug için

            String url = String.format("%s?query=%s&orientation=landscape&content_filter=high",
                    UNSPLASH_URL, searchTerm);

            ResponseEntity<UnsplashResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    UnsplashResponse.class
            );

            if (response.getBody() != null && !response.getBody().getResults().isEmpty()) {
                String imageUrl = response.getBody().getResults().get(0).getUrls().getRegular();
                System.out.println("Found image URL: " + imageUrl); // Debug için
                return imageUrl;
            }
        } catch (Exception e) {
            System.err.println("Error getting image for: " + foodName + " - " + e.getMessage());
        }
        return "/images/items/default-food.jpg";
    }

    private String translateFoodName(String foodName) {
        // Önce özel çevirileri kontrol et
        String translation = foodTranslations.get(foodName.toLowerCase());
        if (translation != null) {
            return translation;
        }

        // Özel çeviri yoksa genel Türkçe karakter dönüşümü yap
        return foodName.toLowerCase()
                .replace("ı", "i")
                .replace("ğ", "g")
                .replace("ü", "u")
                .replace("ş", "s")
                .replace("ö", "o")
                .replace("ç", "c")
                .replace(" ", "+");
    }
}