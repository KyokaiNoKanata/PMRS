package com.example.pmrs.config;

import com.example.pmrs.entity.Movie;
import com.example.pmrs.entity.Rating;
import com.example.pmrs.entity.User;
import com.example.pmrs.repository.MovieRepository;
import com.example.pmrs.repository.RatingRepository;
import com.example.pmrs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public void run(String... args) throws Exception {
        // 创建示例用户
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@example.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setEmail("user2@example.com");
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("user3");
        user3.setPassword("password3");
        user3.setEmail("user3@example.com");
        userRepository.save(user3);

        User user4 = new User();
        user4.setUsername("user4");
        user4.setPassword("password4");
        user4.setEmail("user4@example.com");
        userRepository.save(user4);

        // 创建示例电影
        Movie movie1 = new Movie();
        movie1.setTitle("The Shawshank Redemption");
        movie1.setGenre("Drama");
        movie1.setDirector("Frank Darabont");
        movie1.setReleaseYear(1994);
        movie1.setDescription(
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.");
        movie1.setRating(9.3);
        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("The Godfather");
        movie2.setGenre("Crime, Drama");
        movie2.setDirector("Francis Ford Coppola");
        movie2.setReleaseYear(1972);
        movie2.setDescription(
                "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.");
        movie2.setRating(9.2);
        movieRepository.save(movie2);

        Movie movie3 = new Movie();
        movie3.setTitle("The Dark Knight");
        movie3.setGenre("Action, Crime, Drama");
        movie3.setDirector("Christopher Nolan");
        movie3.setReleaseYear(2008);
        movie3.setDescription(
                "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.");
        movie3.setRating(9.0);
        movieRepository.save(movie3);

        Movie movie4 = new Movie();
        movie4.setTitle("Pulp Fiction");
        movie4.setGenre("Crime, Drama");
        movie4.setDirector("Quentin Tarantino");
        movie4.setReleaseYear(1994);
        movie4.setDescription(
                "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.");
        movie4.setRating(8.9);
        movieRepository.save(movie4);

        Movie movie5 = new Movie();
        movie5.setTitle("Forrest Gump");
        movie5.setGenre("Drama, Romance");
        movie5.setDirector("Robert Zemeckis");
        movie5.setReleaseYear(1994);
        movie5.setDescription(
                "The presidencies of Kennedy and Johnson, the events of Vietnam, Watergate, and other history unfold through the perspective of an Alabama man with an IQ of 75.");
        movie5.setRating(8.8);
        movieRepository.save(movie5);

        // 创建示例评分
        Rating rating1 = new Rating();
        rating1.setUser(user1);
        rating1.setMovie(movie1);
        rating1.setRating(5.0);
        ratingRepository.save(rating1);

        Rating rating2 = new Rating();
        rating2.setUser(user1);
        rating2.setMovie(movie2);
        rating2.setRating(4.0);
        ratingRepository.save(rating2);

        Rating rating3 = new Rating();
        rating3.setUser(user1);
        rating3.setMovie(movie3);
        rating3.setRating(3.0);
        ratingRepository.save(rating3);

        Rating rating4 = new Rating();
        rating4.setUser(user2);
        rating4.setMovie(movie1);
        rating4.setRating(4.0);
        ratingRepository.save(rating4);

        Rating rating5 = new Rating();
        rating5.setUser(user2);
        rating5.setMovie(movie3);
        rating5.setRating(5.0);
        ratingRepository.save(rating5);

        Rating rating6 = new Rating();
        rating6.setUser(user2);
        rating6.setMovie(movie4);
        rating6.setRating(2.0);
        ratingRepository.save(rating6);

        Rating rating7 = new Rating();
        rating7.setUser(user3);
        rating7.setMovie(movie2);
        rating7.setRating(5.0);
        ratingRepository.save(rating7);

        Rating rating8 = new Rating();
        rating8.setUser(user3);
        rating8.setMovie(movie3);
        rating8.setRating(4.0);
        ratingRepository.save(rating8);

        Rating rating9 = new Rating();
        rating9.setUser(user3);
        rating9.setMovie(movie5);
        rating9.setRating(3.0);
        ratingRepository.save(rating9);

        Rating rating10 = new Rating();
        rating10.setUser(user4);
        rating10.setMovie(movie1);
        rating10.setRating(3.0);
        ratingRepository.save(rating10);

        Rating rating11 = new Rating();
        rating11.setUser(user4);
        rating11.setMovie(movie4);
        rating11.setRating(5.0);
        ratingRepository.save(rating11);

        Rating rating12 = new Rating();
        rating12.setUser(user4);
        rating12.setMovie(movie5);
        rating12.setRating(4.0);
        ratingRepository.save(rating12);
    }
}
