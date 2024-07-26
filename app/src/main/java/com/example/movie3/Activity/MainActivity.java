package com.example.movie3.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie3.Adapter.MovieAdapter;
import com.example.movie3.Model.Movie;
import com.example.movie3.Model.MovieResponse;
import com.example.movie3.OnClicked;
import com.example.movie3.R;
import com.example.movie3.Retrofit.ApiService;
import com.example.movie3.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> movieList = new ArrayList<>();
    ApiService apiService;
    public static String API_KEY = "63e6165";
    MovieAdapter dataAdapter;
    RecyclerView showMovieList;
SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        initView();
        apiService = RetrofitClient.getClient().create(ApiService.class);
//      Log.d("FETCH_MOVIES", "API Service initialized");
//      fetchMovies("Mission");
        searchMovie();
    }

    private void initView() {
        showMovieList = findViewById(R.id.recMovieList);
        showMovieList.setLayoutManager(new LinearLayoutManager(this));
        //Log.d("DALBHAT" , "model :"+model.size());
    }

    private void searchMovie(){
        searchView = findViewById(R.id.searchMovie);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchMovies(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchMovies(String query) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<MovieResponse> call = apiService.getMovies(query, API_KEY);
        Log.e("DALBHAT", "Checking1 " + call.toString());
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieList.clear();
                    movieList.addAll(response.body().getSearch());
                    dataAdapter = new MovieAdapter(MainActivity.this, movieList,onClicked);
                    showMovieList.setAdapter(dataAdapter);
                    Log.e("DALBHAT", "movie Listttttt" + movieList.size());
                    // movieAdapter.notifyDataSetChanged();
                } else {
                    Log.e("DALBHAT", "Checking2");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    OnClicked onClicked = new OnClicked() {
        @Override
        public void on_clicked(String id) {
            Intent i = new Intent(MainActivity.this, DescriptionActivity.class);
            i.putExtra("movieId", id);
            startActivity(i);
        }
    };

//    public void fetchMovies(String query){
//        apiService.getMovies(query, API_KEY).enqueue(new Callback<MovieResponse>() {
//            @Override
//            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                if (response.body() != null && response.body().getResponse().equals("True")) {
//                    movieList.clear();
//                    movieList.addAll(response.body().getSearch());
//                    Log.d("DALBHAT", "Checking5"+movieList.size());
//                } else {
//                    Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieResponse> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    public void fetchMovies(String query) {
//        Log.d("FETCH_MOVIES", "fetchMovies called with query: " + query);
//
//        apiService.getMovies(query, API_KEY).enqueue(new Callback<MovieResponse>() {
//            @Override
//            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                Log.d("FETCH_MOVIES", "onResponse called");
//                if (response.isSuccessful() && response.body() != null) {
//                    Log.d("FETCH_MOVIES", "API call successful");
//
//                    MovieResponse movieResponse = response.body();
//                    if ("True".equals(movieResponse.getResponse())) {
//                        movieList.clear();
//                        movieList.addAll(movieResponse.getSearch());
//                        Log.d("FETCH_MOVIES", "Movies fetched successfully. Total movies: " + movieList.size());
//                    } else {
//                        Log.d("FETCH_MOVIES", "No results found");
//                        Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Log.e("FETCH_MOVIES", "Failed to fetch movies: " + response.message());
//                    Toast.makeText(MainActivity.this, "Failed to fetch movies: " + response.message(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieResponse> call, Throwable t) {
//                Log.e("FETCH_MOVIES", "Error fetching movies", t);
//                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}