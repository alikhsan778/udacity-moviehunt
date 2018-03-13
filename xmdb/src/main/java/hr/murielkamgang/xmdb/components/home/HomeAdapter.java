package hr.murielkamgang.xmdb.components.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import hr.murielkamgang.xmdb.R;
import hr.murielkamgang.xmdb.components.base.BaseRecyclerViewAdapter;
import hr.murielkamgang.xmdb.components.di.ActivityScoped;
import hr.murielkamgang.xmdb.data.model.movie.Movie;
import hr.murielkamgang.xmdb.util.Utils;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by muriel on 3/4/18.
 */

@ActivityScoped
class HomeAdapter extends BaseRecyclerViewAdapter<Movie, BaseRecyclerViewAdapter.ItemBaseVH> {

    @Inject
    Picasso picasso;

    @Inject
    HomeAdapter() {
    }

    @Override
    public ItemBaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_home, parent, false));
    }

    class MovieVH extends ItemBaseVH {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.view)
        View view;

        MovieVH(View itemView) {
            super(itemView);
            view.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(HomeAdapter.this, MovieVH.this, getAdapterPosition());
                }
            });
        }

        @Override
        protected void performBinding(Movie movie) {
            picasso
                    .load(Utils.makePosterUrlFor(imageView.getContext(), movie.getPosterPath()))
                    .fit()
                    .transform(new RoundedCornersTransformation(10, 0))
                    .into(imageView);
        }
    }
}