package com.example.pmrs.service;

import com.example.pmrs.entity.Movie;

import java.util.List;

public interface RecommendationService {
    List<Movie> getRecommendations(Long userId);
}
