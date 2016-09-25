package edu.upenn.cis573.hwk2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.content.Intent;
import android.view.Menu;

public class GameActivity extends android.support.v7.app.ActionBarActivity {

    private GameView view;
    protected static String boardSize;

    /*
    This method is called when the Activity is first launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Dots and Boxes");
        view = new GameView(this);
        view.reset();
        setContentView(view);
    }

    /*
    This method is called when the menu is created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
     This method is called when a menu option is selected.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_undo) view.undo();
        else if (id == R.id.action_clear) view.reset();
        else if (id == R.id.action_quit) {

            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Are you sure you want to quit?").setTitle("Don't leave me!");

            // 3. Make YES and NO buttons
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                finish();                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            // 4. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }

}