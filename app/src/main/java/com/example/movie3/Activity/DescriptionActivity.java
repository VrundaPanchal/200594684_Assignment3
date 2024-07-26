package com.example.movie3.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movie3.Model.MovieDetails;
import com.example.movie3.R;
import com.example.movie3.Retrofit.ApiService;
import com.example.movie3.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescriptionActivity extends AppCompatActivity {

    public String movieId;
    ApiService apiService;
    public static String API_KEY = "63e6165";
    TextView movieTitle,movieStudio,genre,year,length,imdbRating,imdbVotes,plot,actors,writers,director,country;
    ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_description);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        initView();
        apiService = RetrofitClient.getClient().create(ApiService.class);
        movieId=getIntent().getStringExtra("movieId");
        Log.e("Mango","ID:"+movieId);
        fetchMovies(movieId);
    }

    private void initView() {
        poster=findViewById(R.id.moviePoster);
        movieTitle=findViewById(R.id.mTitle);
        movieStudio=findViewById(R.id.mStudio);
        genre=findViewById(R.id.movieGenre);
        plot=findViewById(R.id.moviePlot);
        year=findViewById(R.id.mYear);
        length=findViewById(R.id.movieLength);
        imdbRating=findViewById(R.id.movieImdb_rating);
        imdbVotes=findViewById(R.id.movieImdbVotes);
        country=findViewById(R.id.movieCountry);
        director=findViewById(R.id.movieDirector);
        actors=findViewById(R.id.movieActor);
        writers=findViewById(R.id.movieWriter);
    }

    private void fetchMovies(String query) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<MovieDetails> call = apiService.getMovieDetails(movieId, API_KEY);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails details=response.body();
                if (response.isSuccessful() && response.body() != null) {
                    String posterUrl = details.getPoster();
                    movieTitle.setText(details.getTitle());
                    Log.e("Tomato","poster"+details.getPoster());

                    if (posterUrl != null && !posterUrl.isEmpty()) {
                        Glide.with(DescriptionActivity.this)
                                .load(Uri.parse(posterUrl))
                                .into(poster);
                    }
                    //Glide.with(DescriptionActivity.this).load(Uri.parse(posterUrl)).into(poster);
                    movieStudio.setText(details.getTitle());
                    genre.setText(details.getGenre());
                    imdbRating.setText(details.getImdbRating());
                    imdbVotes.setText(details.getImdbVotes());
                    year.setText(details.getYear());
                    length.setText(details.getRuntime());
                    plot.setText(details.getPlot());
                    director.setText(details.getDirector());
                    actors.setText(details.getActors());
                    writers.setText(details.getWriter());
                    country.setText(details.getCountry());

                } else {

                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Toast.makeText(DescriptionActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}