package com.example.spectacleapp.Utils;

import com.example.spectacleapp.Models.Spectacles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpectaclesFilterUtils {

    // Filter by title substring (case-insensitive)
    public static List<Spectacles> filterByTitle(List<Spectacles> list, String keyword) {
        List<Spectacles> result = new ArrayList<>();
        for (Spectacles s : list) {
            if (s.getTitle() != null && s.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }

    // Filter by genre (case-insensitive substring match)
    // Filter by genre (case-insensitive substring match)
    public static List<Spectacles> filterByGenre(List<Spectacles> list, String targetGenre) {
        List<Spectacles> result = new ArrayList<>();

        // Ensure targetGenre is not null or empty
        if (targetGenre == null || targetGenre.trim().isEmpty()) {
            return result;  // Return empty list if targetGenre is null or empty
        }

        for (Spectacles s : list) {
            if (s.getGenre() != null && !s.getGenre().isEmpty()) {
                // Iterate through each genre in the list (if it's a List<String>)
                for (String genre : s.getGenre()) {
                    if (genre.toLowerCase().contains(targetGenre.toLowerCase())) {
                        result.add(s);
                        break; // Stop once a match is found
                    }
                }
            }
        }
        return result;
    }


    // Filter by minimum rating
    public static List<Spectacles> filterByMinRating(List<Spectacles> list, int minRating) {
        List<Spectacles> result = new ArrayList<>();
        for (Spectacles s : list) {
            if (s.getRat() >= minRating) {
                result.add(s);
            }
        }
        return result;
    }
    public static List<Spectacles> sortByRatingDescending(List<Spectacles> list) {
        List<Spectacles> sortedList = new ArrayList<>(list); // make a copy
        Collections.sort(sortedList, new Comparator<Spectacles>() {
            @Override
            public int compare(Spectacles s1, Spectacles s2) {
                return Integer.compare(s2.getRat(), s1.getRat()); // descending order
            }
        });
        return sortedList;
    }

    // Filter by year substring (case-insensitive)
    public static List<Spectacles> filterByYear(List<Spectacles> list, int year) {
        List<Spectacles> result = new ArrayList<>();
        for (Spectacles s : list) {
            if (s.getYear() == year) {
                result.add(s);
            }
        }
        return result;
    }

    // Filter by price range (if any ticket falls in the range)
    public static List<Spectacles> filterByPriceRange(List<Spectacles> list, double maxPrice) {
        List<Spectacles> result = new ArrayList<>();
        for (Spectacles s : list) {
            if (s.getPrice() != null) {
                for (Double price : s.getPrice()) {
                    if (price <= maxPrice) {
                        result.add(s);
                        break; // Add once per spectacle
                    }
                }
            }
        }
        return result;
    }

    // Filter by day substring (case-insensitive)
    public static List<Spectacles> filterByDay(List<Spectacles> list, String targetDay) {
        List<Spectacles> result = new ArrayList<>();
        for (Spectacles s : list) {
            if (s.getDaySchedules() != null) {
                for (var schedule : s.getDaySchedules()) {
                    if (schedule.getDate().toLowerCase().contains(targetDay.toLowerCase())) {
                        result.add(s);
                        break;
                    }
                }
            }
        }
        return result;
    }


}
