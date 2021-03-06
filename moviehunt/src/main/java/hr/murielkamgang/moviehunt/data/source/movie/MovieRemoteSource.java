package hr.murielkamgang.moviehunt.data.source.movie;

import com.google.gson.annotations.SerializedName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import hr.murielkamgang.moviehunt.data.model.movie.Constant;
import hr.murielkamgang.moviehunt.data.model.movie.Movie;
import hr.murielkamgang.moviehunt.data.model.movie.Tag;
import hr.murielkamgang.moviehunt.data.source.DataSourceException;
import hr.murielkamgang.moviehunt.data.source.base.BaseKVH;
import hr.murielkamgang.moviehunt.data.source.base.BaseRemoteDataSource;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by muriel on 3/4/18.
 */

public class MovieRemoteSource extends BaseRemoteDataSource<Movie, BaseKVH> implements MovieSourcePagingExtension,
        MovieSourcePagingExtensionAsObservable, MovieSourceExtension, MovieSourceExtensionAsObservable {

    private final Logger logger = LoggerFactory.getLogger(MovieRemoteSource.class);
    private final MovieApi movieApi;
    private final String apiKey;

    public MovieRemoteSource(Retrofit retrofit, String apiKey) {
        movieApi = retrofit.create(MovieApi.class);
        this.apiKey = apiKey;
    }

    @Override
    public List<Movie> getPopular(int page) {
        try {
            final List<Movie> results = movieApi.getPopular(apiKey, page).execute().body().results;
            tagMovies(results, Constant.TAG_POPULAR);
            return results;
        } catch (IOException e) {
            logger.debug("", e);

            throw new DataSourceException("getPopular for page: " + page, e);
        }
    }

    @Override
    public List<Movie> getTopRated(int page) {
        try {
            final List<Movie> results = movieApi.getTopRated(apiKey, page).execute().body().results;
            tagMovies(results, Constant.TAG_TOP_RATED);
            return results;
        } catch (NullPointerException | IOException e) {
            logger.debug("", e);

            throw new DataSourceException("getTopRated for page: " + page, e);
        }
    }

    @Override
    public Observable<List<Movie>> getPopularAsObservable(int page) {
        return Observable.fromCallable(() -> getPopular(page));
    }

    @Override
    public Observable<List<Movie>> getTopRatedAsObservable(int page) {
        return Observable.fromCallable(() -> getTopRated(page));
    }

    @Override
    public Movie getData(BaseKVH baseKVH) {
        try {
            return movieApi.getMovie(baseKVH.getFieldValue(), apiKey).execute().body();
        } catch (NullPointerException | IOException e) {
            logger.debug("", e);

            throw new DataSourceException("getData for baseKVH: " + baseKVH.toString(), e);
        }
    }

    @Override
    public Observable<Movie> getDataAsObservable(BaseKVH baseKVH) {
        return Observable.fromCallable(() -> getData(baseKVH));
    }

    @Override
    public List<Movie> getPopular() {
        return getPopular(1);
    }

    @Override
    public List<Movie> getTopRated() {
        return getTopRated(1);
    }

    @Override
    public List<Movie> getFavorite() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public Boolean addMovieToFavorite(BaseKVH baseKVH, boolean favorite) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public Observable<List<Movie>> getPopularAsObservable() {
        return Observable.fromCallable(this::getPopular);
    }

    @Override
    public Observable<List<Movie>> getTopRatedAsObservable() {
        return Observable.fromCallable(this::getTopRated);
    }

    @Override
    public Observable<List<Movie>> getFavoriteAsObservable() {
        throw new IllegalStateException("not implemented");
    }

    private void tagMovies(List<Movie> movies, String tag) {
        for (final Movie m : movies) {
            m.getTags().add(new Tag(tag));
        }
    }

    @Override
    public Observable<Boolean> addMovieToFavoriteAsObservable(BaseKVH baseKVH, boolean favorite) {
        throw new IllegalStateException("not implemented");
    }

    interface MovieApi {

        @GET("movie/popular")
        Call<MoviePage> getPopular(@Query("api_key") String apiKey, @Query("page") int page);

        @GET("movie/top_rated")
        Call<MoviePage> getTopRated(@Query("api_key") String apiKey, @Query("page") int page);

        @GET("movie/{movieId}")
        Call<Movie> getMovie(@Path("movieId") int movieId, @Query("api_key") String apiKey);

    }

    private static class MoviePage {

        private int page;
        @SerializedName("total_results")
        private int totalResults;
        @SerializedName("total_pages")
        private int totalPages;
        private List<Movie> results;
    }
}

