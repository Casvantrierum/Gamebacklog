package com.example.cas.gamebacklog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GameAdapter.GameClickListener {

    //Local variables
    private List<Game> mGames;

    private GameAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private FloatingActionButton fab;
    //Constants used when calling the update activity
    public static final String EXTRA_GAME = "Game";
    public static final int REQUESTCODE = 1234;
    private int mModifyPosition;

    public final static int TASK_GET_ALL_GAMES = 0;
    public final static int TASK_DELETE_GAME = 1;
    public final static int TASK_UPDATE_GAME = 2;
    public final static int TASK_INSERT_GAME = 3;

    public static final int NEW_GAME = 1;
    public static final int UPDATE_GAME = 2;


    static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize the local variables

        db = AppDatabase.getInstance(this);

        new GameAsyncTask(TASK_GET_ALL_GAMES).execute();
        mGames = new ArrayList<>();


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        updateUI();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddGame();
            }
        });

/*
Add a touch helper to the RecyclerView to recognize when a user swipes to delete a list entry.
An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
and uses callbacks to signal when a user is performing these actions.
*/
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        new GameAsyncTask(TASK_DELETE_GAME).execute(mGames.get(position));
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    void goToAddGame() {
        Intent intent = new Intent(this, AddGameActivity.class);
        startActivityForResult(intent, NEW_GAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onGameDbUpdated(List list) {
        mGames = list;
        updateUI();
    }


    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new GameAdapter(mGames, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mGames);
        }
    }

    @Override
    public void gameOnClick(int i) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        mModifyPosition = i;
        intent.putExtra(EXTRA_GAME, mGames.get(i));
        startActivityForResult(intent, UPDATE_GAME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == NEW_GAME) {
            String name = data.getStringExtra("name");
            String console = data.getStringExtra("console");
            String status = data.getStringExtra("status");
            new GameAsyncTask(TASK_INSERT_GAME).execute(
                    new Game(name, console, status)
            );
        }
        if (resultCode == RESULT_OK && requestCode == UPDATE_GAME) {
            String name = data.getStringExtra("name");
            String console = data.getStringExtra("console");
            String status = data.getStringExtra("status");
            Game updatedGame = mGames.get(mModifyPosition);
            updatedGame.setName(name);
            updatedGame.setConsole(console);
            updatedGame.setStatus(status);
            new GameAsyncTask(TASK_UPDATE_GAME).execute(updatedGame);
        }
    }

    public class GameAsyncTask extends AsyncTask<Game, Void, List> {

        private int taskCode;
        public GameAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(Game... games) {
            switch (taskCode) {
                case TASK_DELETE_GAME:
                    db.gameDao().deleteGames(games[0]);
                    break;

                case TASK_UPDATE_GAME:
                    db.gameDao().updateGames(games[0]);
                    break;

                case TASK_INSERT_GAME:
                    db.gameDao().insertGames(games[0]);
                    break;
            }

            //To return a new list with the updated data, we get all the data from the database again.
            return db.gameDao().getAllGames();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGameDbUpdated(list);
        }
    }

}
