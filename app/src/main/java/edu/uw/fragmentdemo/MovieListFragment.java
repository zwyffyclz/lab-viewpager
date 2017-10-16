package edu.uw.fragmentdemo;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {

    public static final String TAG = "MovieListFragment";
    private static final String SEARCH_PARAM_KEY = "search_term";

    //an interface for those who can respond to interactions with this Fragment
    interface OnMovieSelectedListener {
        void onMovieSelected(Movie movie);
    }

    private ArrayAdapter<Movie> adapter;
    private OnMovieSelectedListener callback;

    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance(String searchTerm) {
        
        Bundle args = new Bundle();
        args.putString(SEARCH_PARAM_KEY, searchTerm);
        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnMovieSelectedListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMovieSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_movie_list, container, false);

        adapter = new ArrayAdapter<Movie>(getActivity(),
                R.layout.list_item, R.id.txt_item, new ArrayList<Movie>());

        ListView listView = (ListView)rootView.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                Log.v(TAG, "You clicked on: " + movie);
                callback.onMovieSelected(movie);
            }
        });

        Bundle args = getArguments();
        if(args != null){
            String searchTerm = args.getString(SEARCH_PARAM_KEY);
            if(searchTerm != null)
                downloadMovieData(searchTerm);
        }

        return rootView;
    }

    //download media information from iTunes
    public void downloadMovieData(String searchTerm) {

        String urlString = "";
        try {
            urlString = "https://itunes.apple.com/search?term="+ URLEncoder.encode(searchTerm, "UTF-8")+"&media=movie&entity=movie&limit=25";
            //Log.v(TAG, urlString);
        }catch(UnsupportedEncodingException uee){
            Log.e(TAG, uee.toString());
            return;
        }

        Request request = new JsonObjectRequest(Request.Method.GET, urlString, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        ArrayList<Movie> movies = new ArrayList<Movie>();

                        try {
                            //parse the JSON results
                            JSONArray results = response.getJSONArray("results"); //get array from "search" key
                            for(int i=0; i<results.length(); i++){
                                JSONObject track = results.getJSONObject(i);
                                if(!track.getString("wrapperType").equals("track")) //skip non-track results
                                    continue;
                                String title = track.getString("trackName");
                                String year = track.getString("releaseDate");
                                String description = track.getString("longDescription");
                                String url = track.getString("trackViewUrl");
                                Movie movie = new Movie(title, year, description, url);
                                movies.add(movie);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.clear();
                        for(Movie movie : movies) {
                            adapter.add(movie);
                        }                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        RequestSingleton.getInstance(getActivity()).add(request);
    }

}
