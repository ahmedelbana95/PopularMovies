package com.ahmed.popular.movies.models;

/**
 * Model class for films of the actor's filmography
 */
public class Film {
    private String id;
    private String title;


    private String posterPath;
    private String overview;
    private String release_date;
    private String role;
    private String vote_average;
    private String backdrop_path;

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getRole() {
        return role;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPosterPath() {

        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Film() {

    }


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
