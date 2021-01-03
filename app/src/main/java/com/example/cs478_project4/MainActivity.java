package com.example.cs478_project4;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText player_1_guess;
    EditText player_2_guess;
    EditText player_1_comment;
    EditText player_2_comment;
    EditText player_1_ans;
    EditText player_2_ans;
    Button start_btn;
    Button send_p1;
    Button send_p2;
    String num1;
    String num2;
    ListView p1_guesses;
    ListView p1_guesses_opp;
    ListView p2_guesses;
    ListView p2_guesses_opp;
    ArrayList<String>list;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String>list2;
    ArrayAdapter<String> arrayAdapter2;
    TextView msg_play1;
    TextView msg_play2;
    String player_guess;
    String player_comment;
    String player_guess2;
    String player_comment2;

    int play2_num;
    int play1_num;
    int count_p1;
    int count_p2;
    public Handler handlerThreadp1;
    public Handler handlerThreadp2;

    View view;

    //handler to handle messages for players
    public  Handler ui_handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int what = msg.what ;
            switch (what) {
                case PLAYER_1_GUESS:
                    player_2_ans.setText(num2);
                    System.out.println( "Player 1 guess" );
                    break;
                case PLAYER_1_REPLY:
                    if (num1.equals(player_guess)){
                        send_p1.setEnabled( false );
                        send_p2.setEnabled( false );
                        endgame("player 1");
                    }
                    else {
                        list.add( player_guess );
                        arrayAdapter.notifyDataSetChanged();
                        System.out.println( "Player 1 reply" );
                    }
                    break;
                case PLAYER_2_GUESS:
                    player_1_ans.setText(num1);
                    System.out.println( "Player 2 guess" );
                    break;
                case PLAYER_2_REPLY:
                    list2.add(player_guess2);
                    if (num2.equals(player_guess2)){
                        send_p1.setEnabled( false );
                        send_p2.setEnabled( false );
                        endgame("player 2");

                    }
                    else {
                        arrayAdapter2.notifyDataSetChanged();
                        System.out.println( "Player 2 reply" );
                    }
                    break;
            }

        }
    };


    public static final int PLAYER_1_GUESS = 0 ;
    public static final int PLAYER_1_REPLY = 1 ;
    public static final int PLAYER_2_GUESS = 2 ;
    public static final int PLAYER_2_REPLY = 3 ;
    public static final int PLAYER_1_SENT = 1;
    public static final int PLAYER_2_SENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        player_1_guess = (EditText) findViewById(R.id.player_1_input );
        player_2_guess = (EditText) findViewById(R.id.player_2_input );
        player_1_comment = (EditText) findViewById(R.id.msg_p1 );
        player_2_comment = (EditText) findViewById(R.id.msg_p2 );
        player_1_ans = (EditText) findViewById(R.id.Opponent_code_p1 );
        player_2_ans = (EditText) findViewById(R.id.Opponent_code_p2);
        start_btn = (Button) findViewById( R.id.start_button);
        send_p1 = (Button) findViewById( R.id.send_guess_p1 );
        send_p2 = (Button) findViewById( R.id.send_guess_p2 );

        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list );
        p1_guesses = (ListView) findViewById( R.id.p1_list );
        p1_guesses_opp = (ListView) findViewById( R.id.p1_guesslist);
        msg_play1 = (TextView) findViewById( R.id.p1_comment) ;
        msg_play2 = (TextView) findViewById( R.id.p2_comment) ;

        list2 = new ArrayList<>();
        arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list2 );
        p2_guesses = (ListView) findViewById( R.id.p2_list );
        p2_guesses_opp = (ListView) findViewById( R.id.p2_guesslist);

        send_p1.setEnabled( false );
        send_p2.setEnabled( false );
        player_1_guess.setFocusable( false );
        player_2_guess.setFocusable( false );
        player_1_comment.setEnabled( false );
        player_2_comment.setEnabled( false );

    }

    // Starting a new game
    public void start(View v){


        player_1_guess.setFocusableInTouchMode( true );
        player_2_guess.setFocusableInTouchMode( true );
        send_p1.setEnabled( true );
        send_p2.setEnabled( true );

        player_1_comment.setEnabled( true );
        player_2_comment.setEnabled( true );



        player_2_ans.getText().clear();
        player_1_ans.getText().clear();
        list.clear();
        arrayAdapter.notifyDataSetChanged();
        list2.clear();
        arrayAdapter2.notifyDataSetChanged();


        msg_play1.setText( "Starting New Game" );
        msg_play2.setText( "Starting New Game" );



        Thread t1 = new Thread(new player_1()) ;
        Thread t2 = new Thread(new player_2()) ;
        count_p1 = 0;
        count_p2 = 0;
        t1.start();
        t2.start();

    }

    // Player 1 thread
    public class player_1 implements Runnable{


        @Override
        public void run() {
            Looper.prepare();

            handlerThreadp1 = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    msg = ui_handler.obtainMessage(PLAYER_2_SENT) ;
                    msg.arg1 = Integer.parseInt( player_1_guess.getText().toString() );
                    ui_handler.sendMessage(msg) ;
                    // process incoming messages here
                    System.out.println( "player 1" );
                }
            };
            while(true) {
                Random r = new Random();
                play2_num = r.nextInt( 10000 - 1000 ) + 1000;
                num2 = Integer.toString( play2_num );
                if ((num2.charAt(0) != num2.charAt( 1 )) &&(num2.charAt(0) != num2.charAt( 2 ))&&(num2.charAt(0) != num2.charAt( 3 )) && (num2.charAt(1) != num2.charAt( 2 ))
                && (num2.charAt(1) != num2.charAt(3)) && (num2.charAt(2) != num2.charAt( 3 ))){
                    break;
                }
            }


            Message message = ui_handler.obtainMessage(MainActivity.PLAYER_1_GUESS) ;
            ui_handler.sendMessage( message);


            send_p1.setOnClickListener( new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                player_guess = player_1_guess.getText().toString();
                                                player_1_guess.getText().clear();
                                                p1_guesses.setAdapter( arrayAdapter );
                                                p1_guesses_opp.setAdapter( arrayAdapter );

                                                if (player_guess.length() != 4 || player_guess.charAt( 0 ) == player_guess.charAt( 1 ) || player_guess.charAt( 0 ) == player_guess.charAt( 2 )
                                                || player_guess.charAt( 0 ) == player_guess.charAt( 3 ) || player_guess.charAt( 2 ) == player_guess.charAt( 1 ) || player_guess.charAt( 3 ) == player_guess.charAt( 1 )
                                                || player_guess.charAt( 2 ) == player_guess.charAt( 3 )) {
                                                    msg_play2.setText( "The input is Invalid." );
                                                    player_1_comment.getText().clear();
                                                }

                                                else {
                                                    player_comment = player_1_comment.getText().toString();
                                                    msg_play1.setText( player_comment );
                                                    msg_play2.setText( "" );
                                                    player_1_comment.getText().clear();
                                                    //Contidions to check for the number
                                                    count_p1 = count_p1+1;
                                                    if(count_p1 >= 20){
                                                        endgame( "None" );
                                                    }
                                                    else {

                                                        Message msg = ui_handler.obtainMessage( MainActivity.PLAYER_1_REPLY );
                                                        ui_handler.sendMessage( msg );

//                                                        try {
//                                                            Thread.sleep( 2000 );
//                                                        } catch (InterruptedException e) {
//                                                            System.out.println( "Thread interrupted!" );
//                                                        }


                                                        send_p1.setEnabled( false );
                                                        send_p2.setEnabled( true );
                                                    }
                                                }

                                            }
                                        });

            System.out.println( "play 1" );
            Looper.loop();
        }
    }

    //Ending the game function
    private void endgame(String player) {
        if(player.equals("None")) {
             start(view);
        }
        else if(player.equals( "player 1" )){
            msg_play1.setText("player 1 won the game!!" );
            msg_play2.setText( "player 1 won the game!!!!" );
            System.out.println( "START NEW GAME" );
            System.out.println( player + " won the game!!" );

            try {
                Thread.sleep( 2000 );
            } catch (InterruptedException e) {
                System.out.println( "Thread interrupted!" );
            }
//            start( view );
        }
        else if (player.equals( "player 2" )){
            msg_play1.setText("player 2 won the game!!" );
            msg_play2.setText( "player 2 won the game!!!!" );
            System.out.println( "START NEW GAME" );
            System.out.println( player + " won the game!!" );

            try {
                Thread.sleep( 2000 );
            } catch (InterruptedException e) {
                System.out.println( "Thread interrupted!" );
            }
//            start( view );
        }
    }


    //player 2 thread
    public class player_2 implements Runnable {


        @Override
        public void run() {
            Looper.prepare();

            handlerThreadp2 = new Handler( Looper.getMainLooper() ) {
                @Override
                public void handleMessage(Message msg) {
                    msg = ui_handler.obtainMessage(PLAYER_1_SENT) ;
                    msg.arg1 = Integer.parseInt( player_2_guess.getText().toString() );
                    ui_handler.sendMessage(msg) ;

                    try { Thread.sleep(2000); }
                    catch (InterruptedException e) { System.out.println("Thread interrupted!") ; } ;

                }
            };

            while(true) {
                Random r1 = new Random();
                play1_num = r1.nextInt( 10000 - 1000 ) + 1000;
                num1 = Integer.toString( play1_num );
                if ((num1.charAt(0) != num1.charAt( 1 )) &&(num1.charAt(0) != num1.charAt( 2 ))&&(num1.charAt(0) != num1.charAt( 3 )) && (num1.charAt(1) != num1.charAt( 2 ))
                        && (num1.charAt(1) != num1.charAt(3)) && (num1.charAt(2) != num1.charAt( 3 ))){
                    break;
                }
            }


            Message message2 = ui_handler.obtainMessage( MainActivity.PLAYER_2_GUESS );
            ui_handler.sendMessage( message2 );

            try { Thread.sleep(2000); }
            catch (InterruptedException e) { System.out.println("Thread interrupted!") ; } ;


            send_p2.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    player_guess2 = player_2_guess.getText().toString();
                    player_2_guess.getText().clear();
                    p2_guesses.setAdapter( arrayAdapter2 );
                    p2_guesses_opp.setAdapter( arrayAdapter2 );

                    if (player_guess2.length() != 4 || player_guess2.charAt( 0 ) == player_guess2.charAt( 1 ) || player_guess2.charAt( 0 ) == player_guess2.charAt( 2 )
                            || player_guess2.charAt( 0 ) == player_guess2.charAt( 3 ) || player_guess2.charAt( 2 ) == player_guess2.charAt( 1 ) || player_guess2.charAt( 3 ) == player_guess2.charAt( 1 )
                            || player_guess2.charAt( 2 ) == player_guess2.charAt( 3 ) ){
                        msg_play1.setText( "The input is Invalid." );
                        player_2_comment.getText().clear();
                    } else {
                        player_comment2 = player_2_comment.getText().toString();
                        msg_play2.setText( player_comment2 );
                        msg_play1.setText( "" );
                        player_2_comment.getText().clear();
                        count_p2 = count_p2 + 1;
                        if (count_p2 >= 20) {
                            endgame( "None" );
                        } else {

                            Message replymsgp2 = ui_handler.obtainMessage( MainActivity.PLAYER_2_REPLY );
                            ui_handler.sendMessage( replymsgp2 );

                                try {
                                    Thread.sleep( 2000 );
                                } catch (InterruptedException e) {
                                    System.out.println( "Thread interrupted!" );
                                }

                            send_p1.setEnabled( true );
                            send_p2.setEnabled( false );
                        }
                    }

                }
            } );
//            System.out.println( "play 2" );
            Looper.loop();

        }
    }
}