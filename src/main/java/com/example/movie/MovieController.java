package com.example.movie;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MovieController {

    String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=be2a38521a7859c95e2d73c48786e4bb";
    String imageUrl = "http://image.tmdb.org/t/p/original";


    public List<Movie> getMovies(String route){
        RestTemplate restTemplate = new RestTemplate();


        ResultsPage result = restTemplate.getForObject(route, ResultsPage.class);
        System.out.println("These are the movies:");
        System.out.println(result.getResults());

        for (Movie movie: result.getResults()) {
            movie.setPosterPath(imageUrl + movie.getPosterPath());
        }

        return result.getResults();
    }

    @RequestMapping(path = "/now-playing", method = RequestMethod.GET)
    public String nowPlaying(Model model){

         model.addAttribute("results",getMovies(url));

        return "now-playing";
    }

    @RequestMapping(path = "/popular-name", method = RequestMethod.GET)
    public String mediumPopular(Model model){

        List<Movie> movies = getMovies(url).stream()
                .filter(movie -> movie.title.length() >= 10)
                .filter(movie -> movie.getPopularity() > 30 && movie.getPopularity() < 80)
                .collect(Collectors.toList());

        model.addAttribute("results", movies);
        return "popular-name";
    }
}
