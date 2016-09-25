package edu.upenn.cis573.hwk2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity implements OnClickListener {

    private Spinner spinner;
//    protected static String boardSize = "4";

    /*
    This method is called when the Activity is first launched.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Dots and Boxes");
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.spinner);
        //set up listeners
        Button playButton = (Button) findViewById(R.id.play);
        Button quitButton = (Button) findViewById(R.id.quit);
        playButton.setOnClickListener(this);
        quitButton.setOnClickListener(this);
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
    This method is called when one of this Activity's buttons is clicked.
     */
    public void onClick(View v){
        if (v.getId() == R.id.quit){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {

            GameActivity.boardSize = spinner.getSelectedItem().toString();
//            MainActivity.boardSize = spinner.getSelectedItem().toString();
            Intent start = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(start);
        }
    }

}