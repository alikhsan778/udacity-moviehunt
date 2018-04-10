package hr.murielkamgang.moviehunt.helper;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by muriel on 3/11/18.
 */

@Singleton
public class PreferenceHelper {

    public static final String PREF_TOP_RATING = "PREF_NAME_TOP_RATING";
    public static final String PREF_MOST_POPULAR = "PREF_NAME_MOST_POPULAR";
    public static final String PREF_FAVORITE = "PREF_FAVORITE";
    private static final String BEEZER_SHARE_PREF_NAME = "SHARE_PREF_NAME";
    private static final String PREF_SORTING_TYPE_KEY = "PREF_SORTING_TYPE_KEY";
    private final Context context;

    @Inject
    public PreferenceHelper(Context context) {
        this.context = context;
    }

    private SharedPreferences.Editor editor() {
        return context.getSharedPreferences(BEEZER_SHARE_PREF_NAME, Context.MODE_PRIVATE).edit();
    }

    private SharedPreferences preferences() {
        return context.getSharedPreferences(BEEZER_SHARE_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setPopularSorting() {
        editor().putString(PREF_SORTING_TYPE_KEY, PREF_MOST_POPULAR).commit();
    }

    public void setTopRatingSorting() {
        editor().putString(PREF_SORTING_TYPE_KEY, PREF_TOP_RATING).commit();
    }

    public void setFavoriteSorting() {
        editor().putString(PREF_SORTING_TYPE_KEY, PREF_FAVORITE).commit();
    }

    public String getSortingType() {
        return preferences().getString(PREF_SORTING_TYPE_KEY, PREF_MOST_POPULAR);
    }

}
