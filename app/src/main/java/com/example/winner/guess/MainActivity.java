package com.example.winner.guess;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // define the constants
    final static int GUESS_COUNT_LIMIT = 3;
    final static int GUESS_NUM_MAX = 10;
    final static int GUESS_NUM_MIN = 1;

    int random;             // the random number for the user to guess
    Random rn;              // random number generator
    boolean guessCountLimitState = false;
    int guessCountLimit = -1;// guess count limit.  a value of -1 means unlimited # of guesses
    boolean steps_memorized = false;
    EditText guess;
    ArrayList<Integer> guesses = new ArrayList<Integer>();
    int guessedNumber;
    TextView guessHistory;
    TextView yourGuessesLabel;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public void guessClick(View view) {

        // get the guessed number
        String guessString = guess.getText().toString();

        try {
            guessedNumber = Integer.parseInt(guessString);
        } catch (NumberFormatException e) {
            // the user may have entered nothing and just hit the button.
            // if this situation is not caught, it will cause an exception
            Toast.makeText(getApplicationContext(),
                    "Please enter a number between " + GUESS_NUM_MIN + "and " + GUESS_NUM_MAX + "! Try again",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // memorize the number in the ArrayList
        guesses.add(guessedNumber);


        // print the guess history.  If the "steps_memorized" is not set, the TextField
        // is set to invisible, and the user will not see it.
        guessHistory.setText(guesses.toString());
        Log.i("Guess history: ", guesses.toString());

        // check if the guessed number is too high, too low, or correct

        if (guessedNumber > GUESS_NUM_MAX || guessedNumber < GUESS_NUM_MIN) {
            Toast.makeText(getApplicationContext(),
                    "Please enter a number between " + GUESS_NUM_MIN + "and " + GUESS_NUM_MAX + "! Try again",
                    Toast.LENGTH_SHORT).show();
        } else if (guessedNumber > random) {
            Toast.makeText(getApplicationContext(), "Your guessed number is too high! Try again", Toast.LENGTH_SHORT).show();

        } else if (guessedNumber < random) {
            Toast.makeText(getApplicationContext(), "Your guessed number is too low! Try again", Toast.LENGTH_SHORT).show();
        } else {
            // the number is guessed correctly
            //Toast.makeText(getApplicationContext(), "Your guessed number is correct!", Toast.LENGTH_SHORT).show();
            showCustomToastMsg(guessedNumber + " is correct!", R.drawable.ok);
            restartGame();
            return;
        }

        if (guessCountLimitState == true)
            if (--guessCountLimit == 0) {
                //Toast.makeText(getApplicationContext(), "Guess limit reached!", Toast.LENGTH_SHORT).show();
                showCustomToastMsg("Guess limit reached!", R.drawable.delete);
                restartGame();
            } else {
                Toast.makeText(getApplicationContext(), "You can guess " + guessCountLimit + " more times!", Toast.LENGTH_SHORT).show();
            }

    }

    private void restartGame() {
        String message = "";
        random = rn.nextInt(10) + 1;
        if (guessCountLimitState) {
            guessCountLimit = GUESS_COUNT_LIMIT;
            message = "You can guess " + guessCountLimit + " more times";
        } else {
            guessCountLimit = -1;
            message = "";
        }

        // clear the guess EditText field
        guess.setText("");
        // clear the arraylist guesses
        guesses.clear();
        // clear the display of guess history
        guessHistory.setText(guesses.toString());

        if (steps_memorized) {
            yourGuessesLabel.setVisibility(View.VISIBLE);
            guessHistory.setVisibility(View.VISIBLE);
        } else {
            yourGuessesLabel.setVisibility(View.INVISIBLE);
            guessHistory.setVisibility(View.INVISIBLE);
        }

        Log.i("Random number is", Integer.toString(random));

        Toast.makeText(getApplicationContext(), "A new game is started." + message, Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_limit_3) {

            guessCountLimitState = true;
            // recreate the random number
            restartGame();

            //Toast.makeText(getApplicationContext(), "Start a new game. Limit to three guesses per game", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.menu_no_limit) {
            guessCountLimitState = false;

            // recreate the random number
            restartGame();

            //Toast.makeText(getApplicationContext(), "Start a new game. No Limit on number of guesses.", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.menu_no_step) {
            steps_memorized = false;
            // recreate the random number
            restartGame();

            Toast.makeText(getApplicationContext(), "Start a new game. Steps not memorized on guesses per game", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.menu_step) {
            steps_memorized = true;
            // recreate the random number
            restartGame();

            Toast.makeText(getApplicationContext(), "Start a new game. Steps memorized on guesses per game", Toast.LENGTH_SHORT).show();

        } else if (item.getItemId() == R.id.menu_quit) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCustomToastMsg(String message, int icon) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        View toastView = toast.getView(); //This'll return the default View of the Toast.

    /* And now you can get the TextView of the default View of the Toast. */
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(20);
        toastMessage.setTextColor(Color.RED);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, icon);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setCompoundDrawablePadding(5);
        toastView.setBackgroundColor(Color.CYAN);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guessHistory = (TextView) findViewById(R.id.guessHistory);
        yourGuessesLabel = (TextView) findViewById(R.id.yourGuessesLabel);

        // init random number generator
        rn = new Random();

        // init the game variables
        guessCountLimitState = false;
        guessCountLimit = -1;// guess count limit.  a value of -1 means unlimited # of guesses
        steps_memorized = false;

        // init the EditText for the user input
        guess = (EditText) findViewById(R.id.guessedNumber);

        // Start the game
        restartGame();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.winner.guess/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.winner.guess/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
