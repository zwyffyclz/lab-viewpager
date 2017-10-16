package edu.uw.viewpager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    public static final String MOVIE_PARCEL_KEY = "movie_parcel";

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(Movie movie) {

        Bundle args = new Bundle();
        args.putParcelable(MOVIE_PARCEL_KEY, movie);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.movie_detail, container, false);

        Bundle args = getArguments();
        if(args != null){
            Movie movie = args.getParcelable(MOVIE_PARCEL_KEY);
            TextView titleView = (TextView)rootView.findViewById(R.id.txt_movie_title);
            titleView.setText(movie.toString());

            TextView urlView = (TextView)rootView.findViewById(R.id.txt_movie_url);
            urlView.setText(movie.url);

            TextView descView = (TextView)rootView.findViewById(R.id.txt_movie_description);
            descView.setText(movie.description);
        }

        return rootView;
    }

}
