package com.example.pmrs.service;

import com.example.pmrs.entity.Movie;
import com.example.pmrs.repository.MovieRepository;
import org.python.core.PyException;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyList;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PythonRecommendationService implements RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(PythonRecommendationService.class);
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> getRecommendations(Long userId) {
        List<Movie> recommendations = new ArrayList<>();
        PythonInterpreter interpreter = null;

        // Initialize streams to capture Python output
        java.io.ByteArrayOutputStream out = null;
        java.io.ByteArrayOutputStream err = null;

        // 初始化Python解释器
        try {
            // Capture Python stdout and stderr first to ensure availability for all error
            // cases
            out = new java.io.ByteArrayOutputStream();
            err = new java.io.ByteArrayOutputStream();

            interpreter = new PythonInterpreter();
            interpreter.setOut(out);
            interpreter.setErr(err);
            logger.info("Python interpreter initialized and output streams set");

            // 加载推荐模型文件
            logger.debug("Attempting to load recommendation_model.py from classpath");
            ClassPathResource resource = new ClassPathResource("recommendation_model.py");
            File modelFile = resource.getFile();
            logger.info("Loading recommendation model from: {}", modelFile.getAbsolutePath());
            logger.debug("Model file existence check: exists={}, readable={}", modelFile.exists(), modelFile.canRead());
            interpreter.execfile(modelFile.getAbsolutePath());
            logger.info("Recommendation model loaded successfully");

            // 调用Python函数
            logger.debug("Retrieving Python function 'get_recommendations'");
            PyFunction getRecsFunc = interpreter.get("get_recommendations", PyFunction.class);
            if (getRecsFunc == null) {
                logger.error("Python function 'get_recommendations' not found in the model file");
                // Print Python output
                logger.info("Python stdout: {}", out.toString());
                logger.info("Python stderr: {}", err.toString());
                return recommendations;
            }
            logger.debug("Successfully retrieved Python function 'get_recommendations'");

            PyInteger pyUserId = new PyInteger(userId.intValue());
            logger.debug("Converted userId from Long {} to PyInteger {}", userId, pyUserId);
            logger.info("Calling get_recommendations with userId: {}", userId);
            PyList pyRecommendations = null;

            try {
                pyRecommendations = (PyList) getRecsFunc.__call__(pyUserId);
                logger.debug("Python function call completed successfully");
            } catch (Exception e) {
                logger.error("Error calling Python function", e);
                // Print Python output even if the function call fails
                logger.info("Python stdout: {}", out.toString());
                logger.info("Python stderr: {}", err.toString());
                throw e;
            }

            // Print Python output
            logger.info("Python stdout: {}", out.toString());
            logger.info("Python stderr: {}", err.toString());

            // Log detailed PyList information (truncated to avoid long logs)
            String pyListStr = pyRecommendations.toString();
            String truncatedPyListStr = pyListStr.length() > 200 ? pyListStr.substring(0, 200) + "..." : pyListStr;
            logger.debug("Python function returned PyList content (truncated): {}", truncatedPyListStr);
            logger.info("Python function returned PyList: {} with size: {}", pyRecommendations,
                    pyRecommendations.size());

            // 解析结果
            // 解析结果
            logger.debug("Parsing {} recommendation items from PyList", pyRecommendations.size());
            for (Object item : pyRecommendations) {
                try {
                    logger.debug("Processing recommendation item: {} (type: {})", item, item.getClass().getName());
                    Integer movieId;
                    if (item instanceof org.python.core.PyInteger) {
                        // 处理PyInteger类型
                        movieId = ((org.python.core.PyInteger) item).getValue();
                        logger.debug("Converted PyInteger item {} to movieId {}", item, movieId);
                    } else if (item instanceof Number) {
                        // 处理其他数字类型
                        movieId = ((Number) item).intValue();
                        logger.debug("Converted Number item {} to movieId {}", item, movieId);
                    } else {
                        // 处理字符串或其他类型
                        String itemStr = item.toString();
                        logger.debug("Converting string item '{}' to movieId", itemStr);
                        movieId = Integer.parseInt(itemStr);
                        logger.debug("Converted string item '{}' to movieId {}", itemStr, movieId);
                    }
                    logger.debug("Retrieving movie from database with ID {}", movieId);
                    Movie movie = movieRepository.findById(Long.valueOf(movieId)).orElse(null);
                    if (movie != null) {
                        recommendations.add(movie);
                        logger.info("Added movie to recommendations: {} (ID: {})".format(movie.getTitle(), movieId));
                        logger.debug("Successfully added movie {} (ID: {}) to recommendations list", movie.getTitle(),
                                movieId);
                    } else {
                        logger.warn("Movie with ID {} not found in database", movieId);
                    }
                } catch (Exception e) {
                    logger.error("Error processing recommendation item: {} of type {}", item, item.getClass(), e);
                }
            }
            logger.debug("Successfully parsed recommendation items, total added: {}", recommendations.size());

        } catch (IOException e) {
            logger.error("Error loading recommendation model file", e);
        } catch (PyException e) {
            logger.error("Python exception occurred during recommendation calculation", e);
            // Print Python output even if exception occurs during script execution
            logger.info("Python stdout: {}", out.toString());
            logger.info("Python stderr: {}", err.toString());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred during recommendation calculation", e);
            // Print Python output for any unexpected exception
            logger.info("Python stdout: {}", out.toString());
            logger.info("Python stderr: {}", err.toString());
        } finally {
            if (interpreter != null) {
                interpreter.close();
                logger.info("Python interpreter closed");
            }
        }
        return recommendations;
    }
}
