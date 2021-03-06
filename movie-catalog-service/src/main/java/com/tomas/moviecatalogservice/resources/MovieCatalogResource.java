package com.tomas.moviecatalogservice.resources;

import com.tomas.moviecatalogservice.models.CatalogItem;
import com.tomas.moviecatalogservice.models.Movie;
import com.tomas.moviecatalogservice.models.Rating;
import com.tomas.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClinetBuilder;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        //  Get all rated movie Id's
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
            //  For each movie id, call movie info service & get details
             Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
            //  Put them all together
            return new CatalogItem(movie.getName(), "Test", rating.getRating());
        }
        ).collect(Collectors.toList());

    }
}


//            Movie movie = webClinetBuilder.build()
//                    .get() // Method GET
//                    .uri("http://localhost:8082/movies/\" + rating.getMovieId()")
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();
