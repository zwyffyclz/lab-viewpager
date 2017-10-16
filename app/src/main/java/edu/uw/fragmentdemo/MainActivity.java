package edu.uw.fragmentdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnMovieSelectedListener {

    private static final String TAG = "MainActivity";
    public static final String MOVIE_LIST_FRAGMENT_TAG = "MoviesListFragment";
    public static final String MOVIE_DETAIL_FRAGMENT_TAG = "DetailFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //respond to search button clicking
    public void handleSearchClick(View v){
        EditText text = (EditText)findViewById(R.id.txt_search);
        String searchTerm = text.getText().toString();

        MovieListFragment fragment = MovieListFragment.newInstance(searchTerm);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, MOVIE_LIST_FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onMovieSelected(Movie movie) {
        DetailFragment fragment = DetailFragment.newInstance(movie);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, MOVIE_DETAIL_FRAGMENT_TAG);
        ft.addToBackStack(null); //remember for the back button
        ft.commit();

        //showHelloDialog();
        //HelloDialogFragment.newInstance().show(getSupportFragmentManager(), null);
        //DetailFragment.newInstance(movie).show(getSupportFragmentManager(), null);
    }

    public void showHelloDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!")
                .setMessage("Danger Will Robinson!"); //note chaining
        builder.setPositiveButton("I see it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.v(TAG, "You clicked okay! Good times :)");
            }
        });
        builder.setNegativeButton("Noooo...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "You clicked cancel! Sad times :(");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static class HelloDialogFragment extends DialogFragment {

        public static HelloDialogFragment newInstance() {

            Bundle args = new Bundle();
            HelloDialogFragment fragment = new HelloDialogFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Alert!")
                    .setMessage("Danger Will Robinson!"); //note chaining
            builder.setPositiveButton("I see it!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.v(TAG, "You clicked okay! Good times :)");
                }
            });
            builder.setNegativeButton("Noooo...", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.v(TAG, "You clicked cancel! Sad times :(");
                }
            });

            AlertDialog dialog = builder.create();
            return dialog;
        }
    }


}
