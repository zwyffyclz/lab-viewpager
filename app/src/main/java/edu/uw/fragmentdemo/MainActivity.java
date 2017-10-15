package edu.uw.fragmentdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayAdapter<Movie> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<Movie>(this,
                R.layout.list_item, R.id.txt_item, new ArrayList<Movie>());

        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                Log.v(TAG, "You clicked on: " + movie);
            }
        });
    }

    //respond to search button clicking
    public void handleSearchClick(View v){
        EditText text = (EditText)findViewById(R.id.txt_search);
        String searchTerm = text.getText().toString();

        downloadMovieData(searchTerm);
    }

    //download media information from iTunes
    private void downloadMovieData(String searchTerm) {

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

        RequestSingleton.getInstance(this).add(request);
    }
}
