package com.monk3y.chess;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {




    //Declaring all pieces
    char[][] pieces ={
            {'r','n','b','q','k','b','n','r'},
            {'p','p','p','p','p','p','p','p'},
            {'-','-','-','-','-','-','-','-'},
            {'-','-','-','-','-','-','-','-'},
            {'-','-','-','-','-','-','-','-'},
            {'-','-','-','-','-','-','-','-'},
            {'P','P','P','P','P','P','P','P'},
            {'R','N','B','Q','K','B','N','R'}
    };
    int[][] places = new int[8][8];
    boolean piece_selected ;
    boolean white_turn = true;
    char selected_piece='-';
    int init_apos=0,init_npos=0;

    //Storing all moves
    char[][][] boardHistory = new char[200][8][8];
    int move_no=0;
    int max_move_no=0;

    //Promotion
    boolean is_promotion=false;
    int promotion_apos,promotion_npos;


    //Castling
    boolean white_king_moved=false;
    int white_king_move_no=-1;
    boolean black_king_moved=false;
    int black_king_move_no=-1;
    boolean white_rook1_moved=false;
    int white_rook1_move_no=-1;
    boolean white_rook2_moved=false;
    int white_rook2_move_no=-1;
    boolean black_rook1_moved=false;
    int black_rook1_move_no=-1;
    boolean black_rook2_moved=false;
    int black_rook2_move_no=-1;
    
    
    //En Passant
    //White
    boolean white_pawn1_moved_two=false;
    int white_pawn1_move_no=-1;
    boolean white_pawn2_moved_two=false;
    int white_pawn2_move_no=-1;
    boolean white_pawn3_moved_two=false;
    int white_pawn3_move_no=-1;
    boolean white_pawn4_moved_two=false;
    int white_pawn4_move_no=-1;
    boolean white_pawn5_moved_two=false;
    int white_pawn5_move_no=-1;
    boolean white_pawn6_moved_two=false;
    int white_pawn6_move_no=-1;
    boolean white_pawn7_moved_two=false;
    int white_pawn7_move_no=-1;
    boolean white_pawn8_moved_two=false;
    int white_pawn8_move_no=-1;
    //Black
    boolean black_pawn1_moved_two=false;
    int black_pawn1_move_no=-1;
    boolean black_pawn2_moved_two=false;
    int black_pawn2_move_no=-1;
    boolean black_pawn3_moved_two=false;
    int black_pawn3_move_no=-1;
    boolean black_pawn4_moved_two=false;
    int black_pawn4_move_no=-1;
    boolean black_pawn5_moved_two=false;
    int black_pawn5_move_no=-1;
    boolean black_pawn6_moved_two=false;
    int black_pawn6_move_no=-1;
    boolean black_pawn7_moved_two=false;
    int black_pawn7_move_no=-1;
    boolean black_pawn8_moved_two=false;
    int black_pawn8_move_no=-1;

    //Check
    boolean white_check=false;
    boolean black_check=false;
    //Check Places
    int[][] white_check_places = new int[8][8];
    int[][] black_check_places = new int[8][8];
    char[][] temp_pieces = new char[8][8];


    //TODO:Declare variables for Check Alert, Checkmate, Draw by (Stalemate, 50/75 Move Rule, 3 Fold Repetition, Insufficient Material)


    //This runs on Click
    @SuppressLint("SetTextI18n")
    public void click(View view) {

        //Declarations
        ImageView img= (ImageView)view;
        TextView hint = findViewById(R.id.hint);
        ImageView show_piece = findViewById(R.id.show_selected_piece);
        int apos,npos;
        String position = img.getTag().toString();


        //If piece is not selected
        if(!piece_selected && !is_promotion)
        {


            //Resetting places
            reset_places();

            //Position in array
            switch (position.charAt(0))
            {
                case 'a': apos=0;break;
                case 'b': apos=1;break;
                case 'c': apos=2;break;
                case 'd': apos=3;break;
                case 'e': apos=4;break;
                case 'f': apos=5;break;
                case 'g': apos=6;break;
                case 'h': apos=7;break;
                default:  apos=8;break;
            }
            npos = position.charAt(1)-'0'-1;
            String piece_name = pieces[npos][apos]+"";

            //If wrong piece is selected
            if(white_turn && !(piece_name.charAt(0)>'a' && piece_name.charAt(0)<'z'))
            {
                return;
            }
            else if(!white_turn && !(piece_name.charAt(0)>'A' && piece_name.charAt(0)<'Z'))
            {
                return;
            }


            //Setting Image
            switch (piece_name.charAt(0))
            {
                case 'R': show_piece.setImageResource(R.drawable.brook);break;
                case 'N': show_piece.setImageResource(R.drawable.bknight);break;
                case 'B': show_piece.setImageResource(R.drawable.bbishop);break;
                case 'Q': show_piece.setImageResource(R.drawable.bqueen);break;
                case 'K': show_piece.setImageResource(R.drawable.bking);break;
                case 'P': show_piece.setImageResource(R.drawable.bpawn);break;

                case 'r': show_piece.setImageResource(R.drawable.wrook);break;
                case 'n': show_piece.setImageResource(R.drawable.wknight);break;
                case 'b': show_piece.setImageResource(R.drawable.wbishop);break;
                case 'q': show_piece.setImageResource(R.drawable.wqueen);break;
                case 'k': show_piece.setImageResource(R.drawable.wking);break;
                case 'p': show_piece.setImageResource(R.drawable.wpawn);break;
                default:  show_piece.setImageResource(R.drawable.blank);break;

            }

            //Setting selected piece
            selected_piece= piece_name.charAt(0);
            init_apos=apos;init_npos=npos;

            //Calculate Moves
            calculateMoves(npos,apos,selected_piece);

            //Player's turn
            if(white_turn && piece_name.charAt(0)>'a' && piece_name.charAt(0)<'z')
            {
                hint.setText(getString(R.string.white)+" "+getString(R.string.select_destination));
            }
            else if(!white_turn && piece_name.charAt(0)>'A' && piece_name.charAt(0)<'Z')
            {
                hint.setText(getString(R.string.black)+" "+getString(R.string.select_destination));
            }

            //Finally
            draw_board();
            piece_selected=true;
        }

        //If piece is selected
        else if(!is_promotion)
        {
            //Position in array
            switch (position.charAt(0))
            {
                case 'a': apos=0;break;
                case 'b': apos=1;break;
                case 'c': apos=2;break;
                case 'd': apos=3;break;
                case 'e': apos=4;break;
                case 'f': apos=5;break;
                case 'g': apos=6;break;
                case 'h': apos=7;break;
                default:  apos=8;break;
            }
            npos = position.charAt(1)-'0'-1;

            //If selected same piece
            if(places[npos][apos]==1)
            {

                reset_places();
                piece_selected=false;
                selected_piece='-';
                show_piece.setImageResource(R.drawable.blank);
                return;
            }
            //If move is invalid
            if(places[npos][apos]!=2 && places[npos][apos]!=3 && places[npos][apos]!=4 && places[npos][apos]!=5 && places[npos][apos]!=6)
            {
                return;
            }

            //Promotion Move
            if(places[npos][apos]==4) {
                FrameLayout promotion = findViewById(R.id.promotion_palette);
                promotion.setVisibility(View.VISIBLE);
                draw_promotion_palette();
                ImageView queen_promotion = findViewById(R.id.queen_promotion);
                ImageView rook_promotion = findViewById(R.id.rook_promotion);
                ImageView knight_promotion = findViewById(R.id.knight_promotion);
                ImageView bishop_promotion = findViewById(R.id.bishop_promotion);

                //Resetting places
                reset_places();

                //Moving piece
                pieces[npos][apos]=selected_piece;
                pieces[init_npos][init_apos]='-';

                //Finally
                show_piece.setImageResource(R.drawable.blank);
                draw_board();
                piece_selected=false;
                selected_piece='-';


                //Storing move
                storeBoard(++move_no);
                max_move_no = move_no;


                //Player's turn
                String piece_name = pieces[npos][apos]+"";
                if(!white_turn && piece_name.charAt(0)>'a' && piece_name.charAt(0)<'z')
                {
                    hint.setText(getString(R.string.black)+" "+getString(R.string.select_promotion));
                    queen_promotion.setImageResource(R.drawable.bqueen);
                    rook_promotion.setImageResource(R.drawable.brook);
                    knight_promotion.setImageResource(R.drawable.bknight);
                    bishop_promotion.setImageResource(R.drawable.bbishop);
                }
                else if(white_turn && piece_name.charAt(0)>'A' && piece_name.charAt(0)<'Z')
                {
                    hint.setText(getString(R.string.white)+" "+getString(R.string.select_promotion));
                    queen_promotion.setImageResource(R.drawable.wqueen);
                    rook_promotion.setImageResource(R.drawable.wrook);
                    knight_promotion.setImageResource(R.drawable.wknight);
                    bishop_promotion.setImageResource(R.drawable.wbishop);
                }

                //Resetting turn
                white_turn= !white_turn;
                is_promotion=true;

                promotion_apos=apos;
                promotion_npos=npos;

                return;
            }

            //Castling Move
            if(places[npos][apos]==5) {
                //Resetting places
                reset_places();

                //Moving piece
                pieces[npos][apos] = selected_piece;
                pieces[init_npos][init_apos] = '-';

                {
                    if (selected_piece == 'k') {
                        if (apos == 6) {
                            pieces[0][5] = 'r';
                            pieces[0][7] = '-';
                        } else if (apos == 2) {
                            pieces[0][3] = 'r';
                            pieces[0][0] = '-';
                        }
                    }
                    if (selected_piece == 'K') {
                        if (apos == 6) {
                            pieces[7][5] = 'R';
                            pieces[7][7] = '-';
                        } else if (apos == 2) {
                            pieces[7][3] = 'R';
                            pieces[7][0] = '-';
                        }
                    }
                }

                //Finally
                show_piece.setImageResource(R.drawable.blank);
                draw_board();
                piece_selected = false;
                selected_piece='-';
            }

            //En Passant Move
            if(places[npos][apos]==6) {
                //Resetting places
                reset_places();

                //Moving piece
                pieces[npos][apos] = selected_piece;
                pieces[init_npos][init_apos] = '-';

                //Capturing piece
                if(selected_piece=='p')
                {
                    pieces[npos-1][apos]='-';
                }
                else if(selected_piece=='P')
                {
                    pieces[npos+1][apos]='-';
                }

                //Finally
                show_piece.setImageResource(R.drawable.blank);
                draw_board();
                piece_selected = false;
                selected_piece='-';
            }


            //Resetting places
            reset_places();

            //Moving piece
            pieces[npos][apos]=selected_piece;
            pieces[init_npos][init_apos]='-';


            //Castling Variables

            {
                if (selected_piece == 'k') {
                    white_king_moved = true;
                    white_king_move_no= move_no;
                }
                if (selected_piece == 'K') {
                    black_king_moved = true;
                    black_king_move_no= move_no;
                }
                if (selected_piece == 'r' && init_npos == 0 && init_apos == 0) {
                    white_rook1_moved = true;
                    white_rook1_move_no= move_no;
                }
                if (selected_piece == 'r' && init_npos == 0 && init_apos == 7) {
                    white_rook2_moved = true;
                    white_rook2_move_no= move_no;
                }
                if (selected_piece == 'R' && init_npos == 7 && init_apos == 0) {
                    black_rook1_moved = true;
                    black_rook1_move_no= move_no;
                }
                if (selected_piece == 'R' && init_npos == 7 && init_apos == 7) {
                    black_rook2_moved = true;
                    black_rook2_move_no= move_no;
                }
            }

            //En Passant Variables
            if(selected_piece == 'p' && init_npos == 1 && npos == 3 ) {
                switch (apos)
                {
                    case 0:
                        white_pawn1_move_no = move_no+1;
                        white_pawn1_moved_two = true;
                        break;
                    case 1:
                        white_pawn2_move_no = move_no+1;
                        white_pawn2_moved_two = true;
                        break;
                    case 2:
                        white_pawn3_move_no = move_no+1;
                        white_pawn3_moved_two = true;
                        break;
                    case 3:
                        white_pawn4_move_no = move_no+1;
                        white_pawn4_moved_two = true;
                        break;
                    case 4:
                        white_pawn5_move_no = move_no+1;
                        white_pawn5_moved_two = true;
                        break;
                    case 5:
                        white_pawn6_move_no = move_no+1;
                        white_pawn6_moved_two = true;
                        break;
                    case 6:
                        white_pawn7_move_no = move_no+1;
                        white_pawn7_moved_two = true;
                        break;
                    case 7:
                        white_pawn8_move_no = move_no+1;
                        white_pawn8_moved_two = true;
                        break;
                }
            }
            else if(selected_piece == 'P' && init_npos == 6 && npos == 4 ) {
                switch (apos)
                {
                    case 0:
                        black_pawn1_move_no = move_no+1;
                        black_pawn1_moved_two = true;
                        break;
                    case 1:
                        black_pawn2_move_no = move_no+1;
                        black_pawn2_moved_two = true;
                        break;
                    case 2:
                        black_pawn3_move_no = move_no+1;
                        black_pawn3_moved_two = true;
                        break;
                    case 3:
                        black_pawn4_move_no = move_no+1;
                        black_pawn4_moved_two = true;
                        break;
                    case 4:
                        black_pawn5_move_no = move_no+1;
                        black_pawn5_moved_two = true;
                        break;
                    case 5:
                        black_pawn6_move_no = move_no+1;
                        black_pawn6_moved_two = true;
                        break;
                    case 6:
                        black_pawn7_move_no = move_no+1;
                        black_pawn7_moved_two = true;
                        break;
                    case 7:
                        black_pawn8_move_no = move_no+1;
                        black_pawn8_moved_two = true;
                        break;
                }
            }

            //Checking if the king is in check

            //Finally
            show_piece.setImageResource(R.drawable.blank);
            draw_board();
            piece_selected=false;
            selected_piece='-';

            //Storing move
            storeBoard(++move_no);
            max_move_no = move_no;
            //Player's turn
            String piece_name = pieces[npos][apos]+"";
            if(white_turn && piece_name.charAt(0)>'a' && piece_name.charAt(0)<'z')
            {
                hint.setText(getString(R.string.black)+" "+getString(R.string.select_piece));
            }
            else if(!white_turn && piece_name.charAt(0)>'A' && piece_name.charAt(0)<'Z')
            {
                hint.setText(getString(R.string.white)+" "+getString(R.string.select_piece));
            }

            //Resetting turn
            white_turn= !white_turn;

        }
    }

    public void calculateMoves(int npos, int apos, char selected_piece)
    {
        places[npos][apos]=1;
        switch (selected_piece)
        {

            case 'r': wrook(npos,apos,places,pieces);break;
            case 'n': wknight(npos,apos,places,pieces);break;
            case 'b': wbishop(npos,apos,places,pieces);break;
            case 'q': wqueen(npos,apos,places,pieces);break;
            case 'k': wking(npos,apos,places,pieces);break;
            case 'p': wpawn(npos,apos,places,pieces);break;

            case 'R': brook(npos,apos,places,pieces);break;
            case 'N': bknight(npos,apos,places,pieces);break;
            case 'B': bbishop(npos,apos,places,pieces);break;
            case 'Q': bqueen(npos,apos,places,pieces);break;
            case 'K': bking(npos,apos,places,pieces);break;
            case 'P': bpawn(npos,apos,places,pieces);break;
            default:  break;

        }
    }


    //Legal Moves for each Piece

    //Rook
    //White Rook
    public void wrook(int npos,int apos, int[][] places, char[][] pieces)
    {

        if(!white_check)
        {
            for (int i = 1; i <= 7; i++) {
                if (npos + i <= 7 && (pieces[npos + i][apos] == '-' || pieces[npos + i][apos] > 'A' && pieces[npos + i][apos] < 'Z')) {
                    places[npos + i][apos] = 2;
                    if (pieces[npos + i][apos] > 'A' && pieces[npos + i][apos] < 'Z') {
                        places[npos + i][apos] = 3;
                        break;
                    }
                } else
                    break;
            }
            for (int i = 1; i <= 7; i++) {
                if (npos - i >= 0 && (pieces[npos - i][apos] == '-' || pieces[npos - i][apos] > 'A' && pieces[npos - i][apos] < 'Z')) {
                    places[npos - i][apos] = 2;
                    if (pieces[npos - i][apos] > 'A' && pieces[npos - i][apos] < 'Z') {
                        places[npos - i][apos] = 3;
                        break;
                    }
                } else
                    break;
            }
            for (int i = 1; i <= 7; i++) {
                if (apos + i <= 7 && (pieces[npos][apos + i] == '-' || pieces[npos][apos + i] > 'A' && pieces[npos][apos + i] < 'Z')) {
                    places[npos][apos + i] = 2;
                    if (pieces[npos][apos + i] > 'A' && pieces[npos][apos + i] < 'Z') {
                        places[npos][apos + i] = 3;
                        break;
                    }
                } else
                    break;
            }
            for (int i = 1; i <= 7; i++) {
                if (apos - i >= 0 && (pieces[npos][apos - i] == '-' || pieces[npos][apos - i] > 'A' && pieces[npos][apos - i] < 'Z')) {
                    places[npos][apos - i] = 2;
                    if (pieces[npos][apos - i] > 'A' && pieces[npos][apos - i] < 'Z') {
                        places[npos][apos - i] = 3;
                        break;
                    }
                } else
                    break;
            }
        }
    }

    //Black Rook
    public void brook(int npos, int apos, int[][] places, char[][] pieces)
    {
        if(!black_check)
        {
            for (int i = 1; i <= 7; i++) {
                if (npos + i <= 7 && (pieces[npos + i][apos] == '-' || pieces[npos + i][apos] > 'a' && pieces[npos + i][apos] < 'z')) {
                    places[npos + i][apos] = 2;
                    if (pieces[npos + i][apos] > 'a' && pieces[npos + i][apos] < 'z') {
                        places[npos + i][apos] = 3;
                        break;
                    }
                } else
                    break;
            }
            for (int i = 1; i <= 7; i++) {
                if (npos - i >= 0 && (pieces[npos - i][apos] == '-' || pieces[npos - i][apos] > 'a' && pieces[npos - i][apos] < 'z')) {
                    places[npos - i][apos] = 2;
                    if (pieces[npos - i][apos] > 'a' && pieces[npos - i][apos] < 'z') {
                        places[npos - i][apos] = 3;
                        break;
                    }
                } else
                    break;
            }
            for (int i = 1; i <= 7; i++) {
                if (apos + i <= 7 && (pieces[npos][apos + i] == '-' || pieces[npos][apos + i] > 'a' && pieces[npos][apos + i] < 'z')) {
                    places[npos][apos + i] = 2;
                    if (pieces[npos][apos + i] > 'a' && pieces[npos][apos + i] < 'z') {
                        places[npos][apos + i] = 3;
                        break;
                    }
                } else
                    break;
            }
            for (int i = 1; i <= 7; i++) {
                if (apos - i >= 0 && (pieces[npos][apos - i] == '-' || pieces[npos][apos - i] > 'a' && pieces[npos][apos - i] < 'z')) {
                    places[npos][apos - i] = 2;
                    if (pieces[npos][apos - i] > 'a' && pieces[npos][apos - i] < 'z') {
                        places[npos][apos - i] = 3;
                        break;
                    }
                } else
                    break;
            }
        }
    }


    //Knight
    //White Knight
    public void wknight(int npos,int apos, int[][] places, char[][] pieces)
    {
        if(!white_check)
        {
            if (npos >= 2 && apos >= 1 && (pieces[npos - 2][apos - 1] == '-' || pieces[npos - 2][apos - 1] > 'A' && pieces[npos - 2][apos - 1] < 'Z')) {
                places[npos - 2][apos - 1] = 2;
                if (pieces[npos - 2][apos - 1] > 'A' && pieces[npos - 2][apos - 1] < 'Z')
                    places[npos - 2][apos - 1] = 3;
            }
            if (npos >= 2 && apos <= 6 && (pieces[npos - 2][apos + 1] == '-' || pieces[npos - 2][apos + 1] > 'A' && pieces[npos - 2][apos + 1] < 'Z')) {
                places[npos - 2][apos + 1] = 2;
                if (pieces[npos - 2][apos + 1] > 'A' && pieces[npos - 2][apos + 1] < 'Z')
                    places[npos - 2][apos + 1] = 3;
            }
            if (npos >= 1 && apos >= 2 && (pieces[npos - 1][apos - 2] == '-' || pieces[npos - 1][apos - 2] > 'A' && pieces[npos - 1][apos - 2] < 'Z')) {
                places[npos - 1][apos - 2] = 2;
                if (pieces[npos - 1][apos - 2] > 'A' && pieces[npos - 1][apos - 2] < 'Z')
                    places[npos - 1][apos - 2] = 3;
            }
            if (npos >= 1 && apos <= 5 && (pieces[npos - 1][apos + 2] == '-' || pieces[npos - 1][apos + 2] > 'A' && pieces[npos - 1][apos + 2] < 'Z')) {
                places[npos - 1][apos + 2] = 2;
                if (pieces[npos - 1][apos + 2] > 'A' && pieces[npos - 1][apos + 2] < 'Z')
                    places[npos - 1][apos + 2] = 3;
            }
            if (npos <= 5 && apos <= 6 && (pieces[npos + 2][apos + 1] == '-' || pieces[npos + 2][apos + 1] > 'A' && pieces[npos + 2][apos + 1] < 'Z')) {
                places[npos + 2][apos + 1] = 2;
                if (pieces[npos + 2][apos + 1] > 'A' && pieces[npos + 2][apos + 1] < 'Z')
                    places[npos + 2][apos + 1] = 3;
            }
            if (npos <= 5 && apos >= 1 && (pieces[npos + 2][apos - 1] == '-' || pieces[npos + 2][apos - 1] > 'A' && pieces[npos + 2][apos - 1] < 'Z')) {
                places[npos + 2][apos - 1] = 2;
                if (pieces[npos + 2][apos - 1] > 'A' && pieces[npos + 2][apos - 1] < 'Z')
                    places[npos + 2][apos - 1] = 3;
            }
            if (npos <= 6 && apos <= 5 && (pieces[npos + 1][apos + 2] == '-' || pieces[npos + 1][apos + 2] > 'A' && pieces[npos + 1][apos + 2] < 'Z')) {
                places[npos + 1][apos + 2] = 2;
                if (pieces[npos + 1][apos + 2] > 'A' && pieces[npos + 1][apos + 2] < 'Z')
                    places[npos + 1][apos + 2] = 3;
            }
            if (npos <= 6 && apos >= 2 && (pieces[npos + 1][apos - 2] == '-' || pieces[npos + 1][apos - 2] > 'A' && pieces[npos + 1][apos - 2] < 'Z')) {
                places[npos + 1][apos - 2] = 2;
                if (pieces[npos + 1][apos - 2] > 'A' && pieces[npos + 1][apos - 2] < 'Z')
                    places[npos + 1][apos - 2] = 3;
            }
        }
    }

    //Black Knight
    public void bknight(int npos,int apos, int[][] places, char[][] pieces)
    {
        if(!black_check)
        {
            if (npos >= 2 && apos >= 1 && (pieces[npos - 2][apos - 1] == '-' || pieces[npos - 2][apos - 1] > 'a' && pieces[npos - 2][apos - 1] < 'z')) {
                places[npos - 2][apos - 1] = 2;
                if (pieces[npos - 2][apos - 1] > 'a' && pieces[npos - 2][apos - 1] < 'z')
                    places[npos - 2][apos - 1] = 3;
            }
            if (npos >= 2 && apos <= 6 && (pieces[npos - 2][apos + 1] == '-' || pieces[npos - 2][apos + 1] > 'a' && pieces[npos - 2][apos + 1] < 'z')) {
                places[npos - 2][apos + 1] = 2;
                if (pieces[npos - 2][apos + 1] > 'a' && pieces[npos - 2][apos + 1] < 'z')
                    places[npos - 2][apos + 1] = 3;
            }
            if (npos >= 1 && apos >= 2 && (pieces[npos - 1][apos - 2] == '-' || pieces[npos - 1][apos - 2] > 'a' && pieces[npos - 1][apos - 2] < 'z')) {
                places[npos - 1][apos - 2] = 2;
                if (pieces[npos - 1][apos - 2] > 'a' && pieces[npos - 1][apos - 2] < 'z')
                    places[npos - 1][apos - 2] = 3;
            }
            if (npos >= 1 && apos <= 5 && (pieces[npos - 1][apos + 2] == '-' || pieces[npos - 1][apos + 2] > 'a' && pieces[npos - 1][apos + 2] < 'z')) {
                places[npos - 1][apos + 2] = 2;
                if (pieces[npos - 1][apos + 2] > 'a' && pieces[npos - 1][apos + 2] < 'z')
                    places[npos - 1][apos + 2] = 3;
            }
            if (npos <= 5 && apos <= 6 && (pieces[npos + 2][apos + 1] == '-' || pieces[npos + 2][apos + 1] > 'a' && pieces[npos + 2][apos + 1] < 'z')) {
                places[npos + 2][apos + 1] = 2;
                if (pieces[npos + 2][apos + 1] > 'a' && pieces[npos + 2][apos + 1] < 'z')
                    places[npos + 2][apos + 1] = 3;
            }
            if (npos <= 5 && apos >= 1 && (pieces[npos + 2][apos - 1] == '-' || pieces[npos + 2][apos - 1] > 'a' && pieces[npos + 2][apos - 1] < 'z')) {
                places[npos + 2][apos - 1] = 2;
                if (pieces[npos + 2][apos - 1] > 'a' && pieces[npos + 2][apos - 1] < 'z')
                    places[npos + 2][apos - 1] = 3;
            }
            if (npos <= 6 && apos <= 5 && (pieces[npos + 1][apos + 2] == '-' || pieces[npos + 1][apos + 2] > 'a' && pieces[npos + 1][apos + 2] < 'z')) {
                places[npos + 1][apos + 2] = 2;
                if (pieces[npos + 1][apos + 2] > 'a' && pieces[npos + 1][apos + 2] < 'z')
                    places[npos + 1][apos + 2] = 3;
            }
            if (npos <= 6 && apos >= 2 && (pieces[npos + 1][apos - 2] == '-' || pieces[npos + 1][apos - 2] > 'a' && pieces[npos + 1][apos - 2] < 'z')) {
                places[npos + 1][apos - 2] = 2;
                if (pieces[npos + 1][apos - 2] > 'a' && pieces[npos + 1][apos - 2] < 'z')
                    places[npos + 1][apos - 2] = 3;
            }
        }
    }


    //Bishop
    //White Bishop
    public void wbishop(int npos,int apos, int[][] places, char[][] pieces)
    {
        if(!white_check)
        {
            int i, j;
            i = npos + 1;
            j = apos + 1;
            while (i <= 7 && j <= 7 && pieces[i][j] == '-') {
                places[i][j] = 2;
                i++;
                j++;
            }
            if (i <= 7 && j <= 7 && pieces[i][j] > 'A' && pieces[i][j] < 'Z')
                places[i][j] = 3;

            i = npos - 1;
            j = apos + 1;
            while (i >= 0 && j <= 7 && pieces[i][j] == '-') {
                places[i][j] = 2;
                i--;
                j++;
            }
            if (i >= 0 && j <= 7 && pieces[i][j] > 'A' && pieces[i][j] < 'Z')
                places[i][j] = 3;

            i = npos + 1;
            j = apos - 1;
            while (i <= 7 && j >= 0 && pieces[i][j] == '-') {
                places[i][j] = 2;
                i++;
                j--;
            }
            if (i <= 7 && j >= 0 && pieces[i][j] > 'A' && pieces[i][j] < 'Z')
                places[i][j] = 3;

            i = npos - 1;
            j = apos - 1;
            while (i >= 0 && j >= 0 && pieces[i][j] == '-') {
                places[i][j] = 2;
                i--;
                j--;
            }
            if (i >= 0 && j >= 0 && pieces[i][j] > 'A' && pieces[i][j] < 'Z')
                places[i][j] = 3;
        }
    }

    //Black Bishop
    public void bbishop(int npos,int apos, int[][] places, char[][] pieces)
    {
        if(!black_check)
        {
            int i, j;
            i = npos + 1;
            j = apos + 1;
            while (i <= 7 && j <= 7 && pieces[i][j] == '-') {
                places[i][j] = 2;
                i++;
                j++;
            }
            if (i <= 7 && j <= 7 && pieces[i][j] > 'a' && pieces[i][j] < 'z')
                places[i][j] = 3;

            i = npos - 1;
            j = apos + 1;
            while (i >= 0 && j <= 7 && pieces[i][j] == '-') {
                places[i][j] = 2;
                i--;
                j++;
            }
            if (i >= 0 && j <= 7 && pieces[i][j] > 'a' && pieces[i][j] < 'z')
                places[i][j] = 3;

            i = npos + 1;
            j = apos - 1;
            while (i <= 7 && j >= 0 && pieces[i][j] == '-') {
                places[i][j] = 2;
                i++;
                j--;
            }
            if (i <= 7 && j >= 0 && pieces[i][j] > 'a' && pieces[i][j] < 'z')
                places[i][j] = 3;

            i = npos - 1;
            j = apos - 1;
            while (i >= 0 && j >= 0 && pieces[i][j] == '-') {
                places[i][j] = 2;
                i--;
                j--;
            }
            if (i >= 0 && j >= 0 && pieces[i][j] > 'a' && pieces[i][j] < 'z')
                places[i][j] = 3;
        }
    }


    //Queen
    //White Queen
    public void wqueen(int npos, int apos, int[][] places, char[][] pieces)
    {
        if(!white_check)
        {
            wrook(npos, apos, places, pieces);
            wbishop(npos, apos, places, pieces);
        }
    }
    //Black Queen
    public void bqueen(int npos, int apos, int[][] places, char[][] pieces)
    {
        if(!black_check)
        {
            brook(npos, apos, places,pieces);
            bbishop(npos, apos, places, pieces);
        }
    }


    //King
    //TODO: Check
    //TODO: Checkmate
    //TODO: Draw
    //White King
    public void wking(int npos, int apos, int[][] places, char[][] pieces)
    {
        if (npos <= 6 && (pieces[npos + 1][apos] == '-' || pieces[npos + 1][apos] > 'A' && pieces[npos + 1][apos] < 'Z') && white_check_places[npos + 1][apos] == 0) {
            places[npos + 1][apos] = 2;
            if (pieces[npos + 1][apos] > 'A' && pieces[npos + 1][apos] < 'Z')
                places[npos + 1][apos] = 3;
        }
        if (npos >= 1 && (pieces[npos - 1][apos] == '-' || pieces[npos - 1][apos] > 'A' && pieces[npos - 1][apos] < 'Z') && white_check_places[npos - 1][apos] == 0) {
            places[npos - 1][apos] = 2;
            if (pieces[npos - 1][apos] > 'A' && pieces[npos - 1][apos] < 'Z')
                places[npos - 1][apos] = 3;
        }
        if (apos <= 6 && (pieces[npos][apos + 1] == '-' || pieces[npos][apos + 1] > 'A' && pieces[npos][apos + 1] < 'Z') && white_check_places[npos][apos + 1] == 0) {
            places[npos][apos + 1] = 2;
            if (pieces[npos][apos + 1] > 'A' && pieces[npos][apos + 1] < 'Z')
                places[npos][apos + 1] = 3;
        }
        if (apos >= 1 && (pieces[npos][apos - 1] == '-' || pieces[npos][apos - 1] > 'A' && pieces[npos][apos - 1] < 'Z') && white_check_places[npos][apos - 1] == 0) {
            places[npos][apos - 1] = 2;
            if (pieces[npos][apos - 1] > 'A' && pieces[npos][apos - 1] < 'Z')
                places[npos][apos - 1] = 3;
        }
        if (npos <= 6 && apos <= 6 && (pieces[npos + 1][apos + 1] == '-' || pieces[npos + 1][apos + 1] > 'A' && pieces[npos + 1][apos + 1] < 'Z') && white_check_places[npos + 1][apos + 1] == 0) {
            places[npos + 1][apos + 1] = 2;
            if (pieces[npos + 1][apos + 1] > 'A' && pieces[npos + 1][apos + 1] < 'Z')
                places[npos + 1][apos + 1] = 3;
        }
        if (npos <= 6 && apos >= 1 && (pieces[npos + 1][apos - 1] == '-' || pieces[npos + 1][apos - 1] > 'A' && pieces[npos + 1][apos - 1] < 'Z') && white_check_places[npos + 1][apos - 1] == 0) {
            places[npos + 1][apos - 1] = 2;
            if (pieces[npos + 1][apos - 1] > 'A' && pieces[npos + 1][apos - 1] < 'Z')
                places[npos + 1][apos - 1] = 3;
        }
        if (npos >= 1 && apos <= 6 && (pieces[npos - 1][apos + 1] == '-' || pieces[npos - 1][apos + 1] > 'A' && pieces[npos - 1][apos + 1] < 'Z') && white_check_places[npos - 1][apos + 1] == 0) {
            places[npos - 1][apos + 1] = 2;
            if (pieces[npos - 1][apos + 1] > 'A' && pieces[npos - 1][apos + 1] < 'Z')
                places[npos - 1][apos + 1] = 3;
        }
        if (npos >= 1 && apos >= 1 && (pieces[npos - 1][apos - 1] == '-' || pieces[npos - 1][apos - 1] > 'A' && pieces[npos - 1][apos - 1] < 'Z') && white_check_places[npos - 1][apos - 1] == 0) {
            places[npos - 1][apos - 1] = 2;
            if (pieces[npos - 1][apos - 1] > 'A' && pieces[npos - 1][apos - 1] < 'Z')
                places[npos - 1][apos - 1] = 3;
        }

        //Castling
        if (npos == 0 && apos == 4 && pieces[0][5] == '-' && pieces[0][6] == '-' && pieces[0][0] == 'r' && !white_king_moved && !white_rook2_moved && white_check_places[0][4] == 0 && white_check_places[0][5] == 0 && white_check_places[0][6] == 0)
            places[0][6] = 5;
        if (npos == 0 && apos == 4 && pieces[0][3] == '-' && pieces[0][2] == '-' && pieces[0][1] == '-' && pieces[0][0] == 'r' && !white_king_moved && !white_rook1_moved && white_check_places[0][4] == 0 && white_check_places[0][3] == 0 && white_check_places[0][2] == 0 && white_check_places[0][1] == 0)
            places[0][2] = 5;
    }

    //Black King
    public void bking(int npos, int apos, int[][] places, char[][] pieces)
    {
        if (npos <= 6 && (pieces[npos + 1][apos] == '-' || pieces[npos + 1][apos] > 'a' && pieces[npos + 1][apos] < 'z') && black_check_places[npos + 1][apos] == 0) {
            places[npos + 1][apos] = 2;
            if (pieces[npos + 1][apos] > 'a' && pieces[npos + 1][apos] < 'z')
                places[npos + 1][apos] = 3;
        }
        if (npos >= 1 && (pieces[npos - 1][apos] == '-' || pieces[npos - 1][apos] > 'a' && pieces[npos - 1][apos] < 'z') && black_check_places[npos - 1][apos] == 0) {
            places[npos - 1][apos] = 2;
            if (pieces[npos - 1][apos] > 'a' && pieces[npos - 1][apos] < 'z')
                places[npos - 1][apos] = 3;
        }
        if (apos <= 6 && (pieces[npos][apos + 1] == '-' || pieces[npos][apos + 1] > 'a' && pieces[npos][apos + 1] < 'z') && black_check_places[npos][apos + 1] == 0) {
            places[npos][apos + 1] = 2;
            if (pieces[npos][apos + 1] > 'a' && pieces[npos][apos + 1] < 'z')
                places[npos][apos + 1] = 3;
        }
        if (apos >= 1 && (pieces[npos][apos - 1] == '-' || pieces[npos][apos - 1] > 'a' && pieces[npos][apos - 1] < 'z') && black_check_places[npos][apos - 1] == 0) {
            places[npos][apos - 1] = 2;
            if (pieces[npos][apos - 1] > 'a' && pieces[npos][apos - 1] < 'z')
                places[npos][apos - 1] = 3;
        }
        if (npos <= 6 && apos <= 6 && (pieces[npos + 1][apos + 1] == '-' || pieces[npos + 1][apos + 1] > 'a' && pieces[npos + 1][apos + 1] < 'z') && black_check_places[npos + 1][apos + 1] == 0) {
            places[npos + 1][apos + 1] = 2;
            if (pieces[npos + 1][apos + 1] > 'a' && pieces[npos + 1][apos + 1] < 'z')
                places[npos + 1][apos + 1] = 3;
        }
        if (npos <= 6 && apos >= 1 && (pieces[npos + 1][apos - 1] == '-' || pieces[npos + 1][apos - 1] > 'a' && pieces[npos + 1][apos - 1] < 'z') && black_check_places[npos + 1][apos - 1] == 0) {
            places[npos + 1][apos - 1] = 2;
            if (pieces[npos + 1][apos - 1] > 'a' && pieces[npos + 1][apos - 1] < 'z')
                places[npos + 1][apos - 1] = 3;
        }
        if (npos >= 1 && apos <= 6 && (pieces[npos - 1][apos + 1] == '-' || pieces[npos - 1][apos + 1] > 'a' && pieces[npos - 1][apos + 1] < 'z') && black_check_places[npos - 1][apos + 1] == 0) {
            places[npos - 1][apos + 1] = 2;
            if (pieces[npos - 1][apos + 1] > 'a' && pieces[npos - 1][apos + 1] < 'z')
                places[npos - 1][apos + 1] = 3;
        }
        if (npos >= 1 && apos >= 1 && (pieces[npos - 1][apos - 1] == '-' || pieces[npos - 1][apos - 1] > 'a' && pieces[npos - 1][apos - 1] < 'z') && black_check_places[npos - 1][apos - 1] == 0) {
            places[npos - 1][apos - 1] = 2;
            if (pieces[npos - 1][apos - 1] > 'a' && pieces[npos - 1][apos - 1] < 'z')
                places[npos - 1][apos - 1] = 3;
        }

        //Castling
        if (npos == 7 && apos == 4 && pieces[7][5] == '-' && pieces[7][6] == '-' && pieces[7][7] == 'R' && !black_king_moved && !black_rook2_moved && black_check_places[7][4] == 0 && black_check_places[7][5] == 0 && black_check_places[7][6] == 0)
            places[7][6] = 5;
        if (npos == 7 && apos == 4 && pieces[7][3] == '-' && pieces[7][2] == '-' && pieces[7][1] == '-' && pieces[7][0] == 'R' && !black_king_moved && !black_rook1_moved && black_check_places[7][4] == 0 && black_check_places[7][3] == 0 && black_check_places[7][2] == 0 && black_check_places[7][1] == 0)
            places[7][2] = 5;
    }


    //Pawn
    //White Pawn
    public void wpawn(int npos, int apos, int[][] places, char[][] pieces)
    {
        if(!white_check)
        {
            //Normal Move
            if (pieces[npos + 1][apos] == '-')
                places[npos + 1][apos] = 2;

            //If starting position
            if (npos == 1 && pieces[npos + 2][apos] == '-')
                places[npos + 2][apos] = 2;

            //Capturing
            {
                //If something is in diagonal
                if (apos >= 1 && apos <= 6) {
                    if (pieces[npos + 1][apos + 1] >= 'A' && pieces[npos + 1][apos + 1] <= 'Z')
                        places[npos + 1][apos + 1] = 3;
                    if (pieces[npos + 1][apos - 1] >= 'A' && pieces[npos + 1][apos - 1] <= 'Z')
                        places[npos + 1][apos - 1] = 3;
                }
                //If on the edge
                else if (apos == 0) {
                    if (pieces[npos + 1][apos + 1] >= 'A' && pieces[npos + 1][apos + 1] <= 'Z')
                        places[npos + 1][apos + 1] = 3;
                } else if (apos == 7) {
                    if (pieces[npos + 1][apos - 1] >= 'A' && pieces[npos + 1][apos - 1] <= 'Z')
                        places[npos + 1][apos - 1] = 3;
                }
            }

            //Promotion
            if (npos == 6) {
                if (pieces[npos + 1][apos] == '-')
                    places[npos + 1][apos] = 4;
                if (apos >= 1 && apos <= 6) {
                    if (pieces[npos + 1][apos + 1] >= 'A' && pieces[npos + 1][apos + 1] <= 'Z')
                        places[npos + 1][apos + 1] = 4;
                    if (pieces[npos + 1][apos - 1] >= 'A' && pieces[npos + 1][apos - 1] <= 'Z')
                        places[npos + 1][apos - 1] = 4;
                } else if (apos == 0) {
                    if (pieces[npos + 1][apos + 1] >= 'A' && pieces[npos + 1][apos + 1] <= 'Z')
                        places[npos + 1][apos + 1] = 4;
                } else if (apos == 7) {
                    if (pieces[npos + 1][apos - 1] >= 'A' && pieces[npos + 1][apos - 1] <= 'Z')
                        places[npos + 1][apos - 1] = 4;
                }
            }

            //En passant
            if (npos == 4) {
                switch (apos) {
                    case 0:
                        if (pieces[npos][apos + 1] == 'P' && pieces[npos + 1][apos + 1] == '-' && black_pawn2_move_no == move_no && black_pawn2_moved_two)
                            places[npos + 1][apos + 1] = 6;
                        break;
                    case 1:
                        if (pieces[npos][apos + 1] == 'P' && pieces[npos + 1][apos + 1] == '-' && black_pawn3_move_no == move_no && black_pawn3_moved_two)
                            places[npos + 1][apos + 1] = 6;
                        if (pieces[npos][apos - 1] == 'P' && pieces[npos + 1][apos - 1] == '-' && black_pawn1_move_no == move_no && black_pawn1_moved_two)
                            places[npos + 1][apos - 1] = 6;
                        break;
                    case 2:
                        if (pieces[npos][apos + 1] == 'P' && pieces[npos + 1][apos + 1] == '-' && black_pawn4_move_no == move_no && black_pawn4_moved_two)
                            places[npos + 1][apos + 1] = 6;
                        if (pieces[npos][apos - 1] == 'P' && pieces[npos + 1][apos - 1] == '-' && black_pawn2_move_no == move_no && black_pawn2_moved_two)
                            places[npos + 1][apos - 1] = 6;
                        break;
                    case 3:
                        if (pieces[npos][apos + 1] == 'P' && pieces[npos + 1][apos + 1] == '-' && black_pawn5_move_no == move_no && black_pawn5_moved_two)
                            places[npos + 1][apos + 1] = 6;
                        if (pieces[npos][apos - 1] == 'P' && pieces[npos + 1][apos - 1] == '-' && black_pawn3_move_no == move_no && black_pawn3_moved_two)
                            places[npos + 1][apos - 1] = 6;
                        break;
                    case 4:
                        if (pieces[npos][apos + 1] == 'P' && pieces[npos + 1][apos + 1] == '-' && black_pawn6_move_no == move_no && black_pawn6_moved_two)
                            places[npos + 1][apos + 1] = 6;
                        if (pieces[npos][apos - 1] == 'P' && pieces[npos + 1][apos - 1] == '-' && black_pawn4_move_no == move_no && black_pawn4_moved_two)
                            places[npos + 1][apos - 1] = 6;
                        break;
                    case 5:
                        if (pieces[npos][apos + 1] == 'P' && pieces[npos + 1][apos + 1] == '-' && black_pawn7_move_no == move_no && black_pawn7_moved_two)
                            places[npos + 1][apos + 1] = 6;
                        if (pieces[npos][apos - 1] == 'P' && pieces[npos + 1][apos - 1] == '-' && black_pawn5_move_no == move_no && black_pawn5_moved_two)
                            places[npos + 1][apos - 1] = 6;
                        break;
                    case 6:
                        if (pieces[npos][apos + 1] == 'P' && pieces[npos + 1][apos + 1] == '-' && black_pawn8_move_no == move_no && black_pawn8_moved_two)
                            places[npos + 1][apos + 1] = 6;
                        if (pieces[npos][apos - 1] == 'P' && pieces[npos + 1][apos - 1] == '-' && black_pawn6_move_no == move_no && black_pawn6_moved_two)
                            places[npos + 1][apos - 1] = 6;
                        break;
                    case 7:
                        if (pieces[npos][apos - 1] == 'P' && pieces[npos + 1][apos - 1] == '-' && black_pawn7_move_no == move_no && black_pawn7_moved_two)
                            places[npos + 1][apos - 1] = 6;
                        break;
                }
            }
        }
    }

    //Black Pawn
    public void bpawn(int npos, int apos, int[][] places, char[][] pieces)
    {
        if(!black_check)
        {
            //Normal Move
            if (pieces[npos - 1][apos] == '-')
                places[npos - 1][apos] = 2;

            //If starting position
            if (npos == 6 && pieces[npos - 2][apos] == '-')
                places[npos - 2][apos] = 2;

            //If something is in diagonal
            if (apos >= 1 && apos <= 6) {
                if (pieces[npos - 1][apos + 1] >= 'a' && pieces[npos - 1][apos + 1] <= 'z')
                    places[npos - 1][apos + 1] = 3;
                if (pieces[npos - 1][apos - 1] >= 'a' && pieces[npos - 1][apos - 1] <= 'z')
                    places[npos - 1][apos - 1] = 3;
            }
            //If on the edge
            else if (apos == 0) {
                if (pieces[npos - 1][apos + 1] >= 'a' && pieces[npos - 1][apos + 1] <= 'z')
                    places[npos - 1][apos + 1] = 3;
            } else if (apos == 7) {
                if (pieces[npos - 1][apos - 1] >= 'a' && pieces[npos - 1][apos - 1] <= 'z')
                    places[npos - 1][apos - 1] = 3;
            }

            //Promotion
            if (npos == 1) {
                if (pieces[npos - 1][apos] == '-')
                    places[npos - 1][apos] = 4;
                if (apos >= 1 && apos <= 6) {
                    if (pieces[npos - 1][apos + 1] >= 'a' && pieces[npos - 1][apos + 1] <= 'z')
                        places[npos - 1][apos + 1] = 4;
                    if (pieces[npos - 1][apos - 1] >= 'a' && pieces[npos - 1][apos - 1] <= 'z')
                        places[npos - 1][apos - 1] = 4;
                } else if (apos == 0) {
                    if (pieces[npos - 1][apos + 1] >= 'a' && pieces[npos - 1][apos + 1] <= 'z')
                        places[npos - 1][apos + 1] = 4;
                } else if (apos == 7) {
                    if (pieces[npos - 1][apos - 1] >= 'a' && pieces[npos - 1][apos - 1] <= 'z')
                        places[npos - 1][apos - 1] = 4;
                }
            }

            //En Passant
            if (npos == 3) {
                switch (apos) {
                    case 0:
                        if (pieces[npos][apos + 1] == 'p' && pieces[npos - 1][apos + 1] == '-' && white_pawn2_move_no == move_no && white_pawn2_moved_two)
                            places[npos - 1][apos + 1] = 6;
                        break;
                    case 1:
                        if (pieces[npos][apos - 1] == 'p' && pieces[npos - 1][apos - 1] == '-' && white_pawn1_move_no == move_no && white_pawn1_moved_two)
                            places[npos - 1][apos - 1] = 6;
                        if (pieces[npos][apos + 1] == 'p' && pieces[npos - 1][apos + 1] == '-' && white_pawn3_move_no == move_no && white_pawn3_moved_two)
                            places[npos - 1][apos + 1] = 6;
                        break;
                    case 2:
                        if (pieces[npos][apos - 1] == 'p' && pieces[npos - 1][apos - 1] == '-' && white_pawn2_move_no == move_no && white_pawn2_moved_two)
                            places[npos - 1][apos - 1] = 6;
                        if (pieces[npos][apos + 1] == 'p' && pieces[npos - 1][apos + 1] == '-' && white_pawn4_move_no == move_no && white_pawn4_moved_two)
                            places[npos - 1][apos + 1] = 6;
                        break;
                    case 3:
                        if (pieces[npos][apos - 1] == 'p' && pieces[npos - 1][apos - 1] == '-' && white_pawn3_move_no == move_no && white_pawn3_moved_two)
                            places[npos - 1][apos - 1] = 6;
                        if (pieces[npos][apos + 1] == 'p' && pieces[npos - 1][apos + 1] == '-' && white_pawn5_move_no == move_no && white_pawn5_moved_two)
                            places[npos - 1][apos + 1] = 6;
                        break;
                    case 4:
                        if (pieces[npos][apos - 1] == 'p' && pieces[npos - 1][apos - 1] == '-' && white_pawn4_move_no == move_no && white_pawn4_moved_two)
                            places[npos - 1][apos - 1] = 6;
                        if (pieces[npos][apos + 1] == 'p' && pieces[npos - 1][apos + 1] == '-' && white_pawn6_move_no == move_no && white_pawn6_moved_two)
                            places[npos - 1][apos + 1] = 6;
                        break;
                    case 5:
                        if (pieces[npos][apos - 1] == 'p' && pieces[npos - 1][apos - 1] == '-' && white_pawn5_move_no == move_no && white_pawn5_moved_two)
                            places[npos - 1][apos - 1] = 6;
                        if (pieces[npos][apos + 1] == 'p' && pieces[npos - 1][apos + 1] == '-' && white_pawn7_move_no == move_no && white_pawn7_moved_two)
                            places[npos - 1][apos + 1] = 6;
                        break;
                    case 6:
                        if (pieces[npos][apos - 1] == 'p' && pieces[npos - 1][apos - 1] == '-' && white_pawn6_move_no == move_no && white_pawn6_moved_two)
                            places[npos - 1][apos - 1] = 6;
                        if (pieces[npos][apos + 1] == 'p' && pieces[npos - 1][apos + 1] == '-' && white_pawn8_move_no == move_no && white_pawn8_moved_two)
                            places[npos - 1][apos + 1] = 6;
                        break;
                    case 7:
                        if (pieces[npos][apos - 1] == 'p' && pieces[npos - 1][apos - 1] == '-' && white_pawn7_move_no == move_no && white_pawn7_moved_two)
                            places[npos - 1][apos - 1] = 6;
                        break;
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void Promotion_Palette(View view)
    {
        String piece_name = view.getTag().toString();
        TextView hint =findViewById(R.id.hint);

        if(white_turn) {
            hint.setText(getString(R.string.white)+" "+getString(R.string.select_piece));
            switch (piece_name) {
                case "queen":
                    pieces[promotion_npos][promotion_apos] = 'Q';
                    break;
                case "rook":
                    pieces[promotion_npos][promotion_apos] = 'R';
                    break;
                case "bishop":
                    pieces[promotion_npos][promotion_apos] = 'B';
                    break;
                case "knight":
                    pieces[promotion_npos][promotion_apos] = 'N';
                    break;
            }
            storeBoard(move_no);
        }

        else {
            hint.setText(getString(R.string.black)+" "+getString(R.string.select_piece));
            switch (piece_name) {
                case "queen":
                    pieces[promotion_npos][promotion_apos] = 'q';
                    break;
                case "rook":
                    pieces[promotion_npos][promotion_apos] = 'r';
                    break;
                case "bishop":
                    pieces[promotion_npos][promotion_apos] = 'b';
                    break;
                case "knight":
                    pieces[promotion_npos][promotion_apos] = 'n';
                    break;
            }
            storeBoard(move_no);
        }

        draw_board();
        is_promotion=false;

        FrameLayout palette = findViewById(R.id.promotion_palette);
        palette.setVisibility(View.INVISIBLE);

    }

    public void draw_board(){
        //Declaring all ImageViews for the pieces
        ImageView a1= findViewById(R.id.a1p);ImageView a2= findViewById(R.id.a2p);ImageView a3= findViewById(R.id.a3p);ImageView a4= findViewById(R.id.a4p);ImageView a5= findViewById(R.id.a5p);ImageView a6= findViewById(R.id.a6p);ImageView a7= findViewById(R.id.a7p);ImageView a8= findViewById(R.id.a8p);
        ImageView b1= findViewById(R.id.b1p);ImageView b2= findViewById(R.id.b2p);ImageView b3= findViewById(R.id.b3p);ImageView b4= findViewById(R.id.b4p);ImageView b5= findViewById(R.id.b5p);ImageView b6= findViewById(R.id.b6p);ImageView b7= findViewById(R.id.b7p);ImageView b8= findViewById(R.id.b8p);
        ImageView c1= findViewById(R.id.c1p);ImageView c2= findViewById(R.id.c2p);ImageView c3= findViewById(R.id.c3p);ImageView c4= findViewById(R.id.c4p);ImageView c5= findViewById(R.id.c5p);ImageView c6= findViewById(R.id.c6p);ImageView c7= findViewById(R.id.c7p);ImageView c8= findViewById(R.id.c8p);
        ImageView d1= findViewById(R.id.d1p);ImageView d2= findViewById(R.id.d2p);ImageView d3= findViewById(R.id.d3p);ImageView d4= findViewById(R.id.d4p);ImageView d5= findViewById(R.id.d5p);ImageView d6= findViewById(R.id.d6p);ImageView d7= findViewById(R.id.d7p);ImageView d8= findViewById(R.id.d8p);
        ImageView e1= findViewById(R.id.e1p);ImageView e2= findViewById(R.id.e2p);ImageView e3= findViewById(R.id.e3p);ImageView e4= findViewById(R.id.e4p);ImageView e5= findViewById(R.id.e5p);ImageView e6= findViewById(R.id.e6p);ImageView e7= findViewById(R.id.e7p);ImageView e8= findViewById(R.id.e8p);
        ImageView f1= findViewById(R.id.f1p);ImageView f2= findViewById(R.id.f2p);ImageView f3= findViewById(R.id.f3p);ImageView f4= findViewById(R.id.f4p);ImageView f5= findViewById(R.id.f5p);ImageView f6= findViewById(R.id.f6p);ImageView f7= findViewById(R.id.f7p);ImageView f8= findViewById(R.id.f8p);
        ImageView g1= findViewById(R.id.g1p);ImageView g2= findViewById(R.id.g2p);ImageView g3= findViewById(R.id.g3p);ImageView g4= findViewById(R.id.g4p);ImageView g5= findViewById(R.id.g5p);ImageView g6= findViewById(R.id.g6p);ImageView g7= findViewById(R.id.g7p);ImageView g8= findViewById(R.id.g8p);
        ImageView h1= findViewById(R.id.h1p);ImageView h2= findViewById(R.id.h2p);ImageView h3= findViewById(R.id.h3p);ImageView h4= findViewById(R.id.h4p);ImageView h5= findViewById(R.id.h5p);ImageView h6= findViewById(R.id.h6p);ImageView h7= findViewById(R.id.h7p);ImageView h8= findViewById(R.id.h8p);

        //Setting all ImageViews to respective pieces
        a1.setImageResource(pieceImage(pieces[0][0]));a2.setImageResource(pieceImage(pieces[1][0]));a3.setImageResource(pieceImage(pieces[2][0]));a4.setImageResource(pieceImage(pieces[3][0]));a5.setImageResource(pieceImage(pieces[4][0]));a6.setImageResource(pieceImage(pieces[5][0]));a7.setImageResource(pieceImage(pieces[6][0]));a8.setImageResource(pieceImage(pieces[7][0]));
        b1.setImageResource(pieceImage(pieces[0][1]));b2.setImageResource(pieceImage(pieces[1][1]));b3.setImageResource(pieceImage(pieces[2][1]));b4.setImageResource(pieceImage(pieces[3][1]));b5.setImageResource(pieceImage(pieces[4][1]));b6.setImageResource(pieceImage(pieces[5][1]));b7.setImageResource(pieceImage(pieces[6][1]));b8.setImageResource(pieceImage(pieces[7][1]));
        c1.setImageResource(pieceImage(pieces[0][2]));c2.setImageResource(pieceImage(pieces[1][2]));c3.setImageResource(pieceImage(pieces[2][2]));c4.setImageResource(pieceImage(pieces[3][2]));c5.setImageResource(pieceImage(pieces[4][2]));c6.setImageResource(pieceImage(pieces[5][2]));c7.setImageResource(pieceImage(pieces[6][2]));c8.setImageResource(pieceImage(pieces[7][2]));
        d1.setImageResource(pieceImage(pieces[0][3]));d2.setImageResource(pieceImage(pieces[1][3]));d3.setImageResource(pieceImage(pieces[2][3]));d4.setImageResource(pieceImage(pieces[3][3]));d5.setImageResource(pieceImage(pieces[4][3]));d6.setImageResource(pieceImage(pieces[5][3]));d7.setImageResource(pieceImage(pieces[6][3]));d8.setImageResource(pieceImage(pieces[7][3]));
        e1.setImageResource(pieceImage(pieces[0][4]));e2.setImageResource(pieceImage(pieces[1][4]));e3.setImageResource(pieceImage(pieces[2][4]));e4.setImageResource(pieceImage(pieces[3][4]));e5.setImageResource(pieceImage(pieces[4][4]));e6.setImageResource(pieceImage(pieces[5][4]));e7.setImageResource(pieceImage(pieces[6][4]));e8.setImageResource(pieceImage(pieces[7][4]));
        f1.setImageResource(pieceImage(pieces[0][5]));f2.setImageResource(pieceImage(pieces[1][5]));f3.setImageResource(pieceImage(pieces[2][5]));f4.setImageResource(pieceImage(pieces[3][5]));f5.setImageResource(pieceImage(pieces[4][5]));f6.setImageResource(pieceImage(pieces[5][5]));f7.setImageResource(pieceImage(pieces[6][5]));f8.setImageResource(pieceImage(pieces[7][5]));
        g1.setImageResource(pieceImage(pieces[0][6]));g2.setImageResource(pieceImage(pieces[1][6]));g3.setImageResource(pieceImage(pieces[2][6]));g4.setImageResource(pieceImage(pieces[3][6]));g5.setImageResource(pieceImage(pieces[4][6]));g6.setImageResource(pieceImage(pieces[5][6]));g7.setImageResource(pieceImage(pieces[6][6]));g8.setImageResource(pieceImage(pieces[7][6]));
        h1.setImageResource(pieceImage(pieces[0][7]));h2.setImageResource(pieceImage(pieces[1][7]));h3.setImageResource(pieceImage(pieces[2][7]));h4.setImageResource(pieceImage(pieces[3][7]));h5.setImageResource(pieceImage(pieces[4][7]));h6.setImageResource(pieceImage(pieces[5][7]));h7.setImageResource(pieceImage(pieces[6][7]));h8.setImageResource(pieceImage(pieces[7][7]));

        //Defining all ImageViews for the highlighted squares
        ImageView a1p= findViewById(R.id.a1pos);ImageView a2p= findViewById(R.id.a2pos);ImageView a3p= findViewById(R.id.a3pos);ImageView a4p= findViewById(R.id.a4pos);ImageView a5p= findViewById(R.id.a5pos);ImageView a6p= findViewById(R.id.a6pos);ImageView a7p= findViewById(R.id.a7pos);ImageView a8p= findViewById(R.id.a8pos);
        ImageView b1p= findViewById(R.id.b1pos);ImageView b2p= findViewById(R.id.b2pos);ImageView b3p= findViewById(R.id.b3pos);ImageView b4p= findViewById(R.id.b4pos);ImageView b5p= findViewById(R.id.b5pos);ImageView b6p= findViewById(R.id.b6pos);ImageView b7p= findViewById(R.id.b7pos);ImageView b8p= findViewById(R.id.b8pos);
        ImageView c1p= findViewById(R.id.c1pos);ImageView c2p= findViewById(R.id.c2pos);ImageView c3p= findViewById(R.id.c3pos);ImageView c4p= findViewById(R.id.c4pos);ImageView c5p= findViewById(R.id.c5pos);ImageView c6p= findViewById(R.id.c6pos);ImageView c7p= findViewById(R.id.c7pos);ImageView c8p= findViewById(R.id.c8pos);
        ImageView d1p= findViewById(R.id.d1pos);ImageView d2p= findViewById(R.id.d2pos);ImageView d3p= findViewById(R.id.d3pos);ImageView d4p= findViewById(R.id.d4pos);ImageView d5p= findViewById(R.id.d5pos);ImageView d6p= findViewById(R.id.d6pos);ImageView d7p= findViewById(R.id.d7pos);ImageView d8p= findViewById(R.id.d8pos);
        ImageView e1p= findViewById(R.id.e1pos);ImageView e2p= findViewById(R.id.e2pos);ImageView e3p= findViewById(R.id.e3pos);ImageView e4p= findViewById(R.id.e4pos);ImageView e5p= findViewById(R.id.e5pos);ImageView e6p= findViewById(R.id.e6pos);ImageView e7p= findViewById(R.id.e7pos);ImageView e8p= findViewById(R.id.e8pos);
        ImageView f1p= findViewById(R.id.f1pos);ImageView f2p= findViewById(R.id.f2pos);ImageView f3p= findViewById(R.id.f3pos);ImageView f4p= findViewById(R.id.f4pos);ImageView f5p= findViewById(R.id.f5pos);ImageView f6p= findViewById(R.id.f6pos);ImageView f7p= findViewById(R.id.f7pos);ImageView f8p= findViewById(R.id.f8pos);
        ImageView g1p= findViewById(R.id.g1pos);ImageView g2p= findViewById(R.id.g2pos);ImageView g3p= findViewById(R.id.g3pos);ImageView g4p= findViewById(R.id.g4pos);ImageView g5p= findViewById(R.id.g5pos);ImageView g6p= findViewById(R.id.g6pos);ImageView g7p= findViewById(R.id.g7pos);ImageView g8p= findViewById(R.id.g8pos);
        ImageView h1p= findViewById(R.id.h1pos);ImageView h2p= findViewById(R.id.h2pos);ImageView h3p= findViewById(R.id.h3pos);ImageView h4p= findViewById(R.id.h4pos);ImageView h5p= findViewById(R.id.h5pos);ImageView h6p= findViewById(R.id.h6pos);ImageView h7p= findViewById(R.id.h7pos);ImageView h8p= findViewById(R.id.h8pos);

        //Setting all highlighted squares to required color
        a1p.setImageResource(pieceHighlight(places[0][0]));a2p.setImageResource(pieceHighlight(places[1][0]));a3p.setImageResource(pieceHighlight(places[2][0]));a4p.setImageResource(pieceHighlight(places[3][0]));a5p.setImageResource(pieceHighlight(places[4][0]));a6p.setImageResource(pieceHighlight(places[5][0]));a7p.setImageResource(pieceHighlight(places[6][0]));a8p.setImageResource(pieceHighlight(places[7][0]));
        b1p.setImageResource(pieceHighlight(places[0][1]));b2p.setImageResource(pieceHighlight(places[1][1]));b3p.setImageResource(pieceHighlight(places[2][1]));b4p.setImageResource(pieceHighlight(places[3][1]));b5p.setImageResource(pieceHighlight(places[4][1]));b6p.setImageResource(pieceHighlight(places[5][1]));b7p.setImageResource(pieceHighlight(places[6][1]));b8p.setImageResource(pieceHighlight(places[7][1]));
        c1p.setImageResource(pieceHighlight(places[0][2]));c2p.setImageResource(pieceHighlight(places[1][2]));c3p.setImageResource(pieceHighlight(places[2][2]));c4p.setImageResource(pieceHighlight(places[3][2]));c5p.setImageResource(pieceHighlight(places[4][2]));c6p.setImageResource(pieceHighlight(places[5][2]));c7p.setImageResource(pieceHighlight(places[6][2]));c8p.setImageResource(pieceHighlight(places[7][2]));
        d1p.setImageResource(pieceHighlight(places[0][3]));d2p.setImageResource(pieceHighlight(places[1][3]));d3p.setImageResource(pieceHighlight(places[2][3]));d4p.setImageResource(pieceHighlight(places[3][3]));d5p.setImageResource(pieceHighlight(places[4][3]));d6p.setImageResource(pieceHighlight(places[5][3]));d7p.setImageResource(pieceHighlight(places[6][3]));d8p.setImageResource(pieceHighlight(places[7][3]));
        e1p.setImageResource(pieceHighlight(places[0][4]));e2p.setImageResource(pieceHighlight(places[1][4]));e3p.setImageResource(pieceHighlight(places[2][4]));e4p.setImageResource(pieceHighlight(places[3][4]));e5p.setImageResource(pieceHighlight(places[4][4]));e6p.setImageResource(pieceHighlight(places[5][4]));e7p.setImageResource(pieceHighlight(places[6][4]));e8p.setImageResource(pieceHighlight(places[7][4]));
        f1p.setImageResource(pieceHighlight(places[0][5]));f2p.setImageResource(pieceHighlight(places[1][5]));f3p.setImageResource(pieceHighlight(places[2][5]));f4p.setImageResource(pieceHighlight(places[3][5]));f5p.setImageResource(pieceHighlight(places[4][5]));f6p.setImageResource(pieceHighlight(places[5][5]));f7p.setImageResource(pieceHighlight(places[6][5]));f8p.setImageResource(pieceHighlight(places[7][5]));
        g1p.setImageResource(pieceHighlight(places[0][6]));g2p.setImageResource(pieceHighlight(places[1][6]));g3p.setImageResource(pieceHighlight(places[2][6]));g4p.setImageResource(pieceHighlight(places[3][6]));g5p.setImageResource(pieceHighlight(places[4][6]));g6p.setImageResource(pieceHighlight(places[5][6]));g7p.setImageResource(pieceHighlight(places[6][6]));g8p.setImageResource(pieceHighlight(places[7][6]));
        h1p.setImageResource(pieceHighlight(places[0][7]));h2p.setImageResource(pieceHighlight(places[1][7]));h3p.setImageResource(pieceHighlight(places[2][7]));h4p.setImageResource(pieceHighlight(places[3][7]));h5p.setImageResource(pieceHighlight(places[4][7]));h6p.setImageResource(pieceHighlight(places[5][7]));h7p.setImageResource(pieceHighlight(places[6][7]));h8p.setImageResource(pieceHighlight(places[7][7]));


    }

    @SuppressLint("SetTextI18n")
    public void draw_promotion_palette()
    {
        ImageView queen = findViewById(R.id.queen_promotion);
        ImageView rook = findViewById(R.id.rook_promotion);
        ImageView bishop = findViewById(R.id.bishop_promotion);
        ImageView knight = findViewById(R.id.knight_promotion);
        TextView hint = findViewById(R.id.hint);
        if(white_turn) {
            hint.setText(getString(R.string.white)+" "+getString(R.string.select_promotion));
            queen.setImageResource(R.drawable.wqueen);
            rook.setImageResource(R.drawable.wrook);
            bishop.setImageResource(R.drawable.wbishop);
            knight.setImageResource(R.drawable.wknight);
        }
        else {
            hint.setText(getString(R.string.black)+" "+getString(R.string.select_promotion));
            queen.setImageResource(R.drawable.bqueen);
            rook.setImageResource(R.drawable.brook);
            bishop.setImageResource(R.drawable.bbishop);
            knight.setImageResource(R.drawable.bknight);
        }
    }

    //Hints
    public int pieceHighlight(int toSet)
    {
        if (toSet==1)
            return R.drawable.highlighted_selected_piece_fg;
        else if(toSet==2) //Normal Move
            return R.drawable.indicator;
        else if(toSet==3) //Attack Move
            return R.drawable.indicator2;
        else if(toSet==4) //Pawn Promotion
            return R.drawable.indicator3;
        else if(toSet==5) //Castling
            return R.drawable.indicator4;
        else if(toSet==6) //En Passant
            return R.drawable.indicator5;
        else
            return R.drawable.blank;
    }

    //Updating Chess Pieces
    public int pieceImage(char pieceToSet)
    {
        int pieceImage = R.drawable.blank;
        switch (pieceToSet)
        {
            case 'R': pieceImage = R.drawable.brook;break;
            case 'N': pieceImage = R.drawable.bknight;break;
            case 'B': pieceImage = R.drawable.bbishop;break;
            case 'Q': pieceImage = R.drawable.bqueen;break;
            case 'K': pieceImage = R.drawable.bking;break;
            case 'P': pieceImage = R.drawable.bpawn;break;

            case 'r': pieceImage = R.drawable.wrook;break;
            case 'n': pieceImage = R.drawable.wknight;break;
            case 'b': pieceImage = R.drawable.wbishop;break;
            case 'q': pieceImage = R.drawable.wqueen;break;
            case 'k': pieceImage = R.drawable.wking;break;
            case 'p': pieceImage = R.drawable.wpawn;break;
        }
        return pieceImage;
    }

    //Store board history
    public void storeBoard(int move_number)
    {

        for(int i=0;i<8;i++)
            System.arraycopy(pieces[i], 0, boardHistory[move_number][i], 0, 8);
        debug(move_number);
    }
    //Debugging
    public void debug(int move_no){
        StringBuilder log = new StringBuilder();
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                log.append(boardHistory[move_no][i][j]);
            }
            log.append("\n");
        }
        Log.d("Board", log.toString());
        Log.d("Move", "Move: "+ move_no);
    }

    //Undo
    @SuppressLint("SetTextI18n")
    public void undo(View view)
    {
        if (move_no > 0 && !is_promotion) {
            //Reverting castling variables
            {
                if(move_no-1==white_king_move_no)
                    white_king_moved= false;
                if(move_no-1==black_king_move_no)
                    black_king_moved= false;
                if(move_no-1==white_rook1_move_no)
                    white_rook1_moved= false;
                if(move_no-1==white_rook2_move_no)
                    white_rook2_moved= false;
                if(move_no-1==black_rook1_move_no)
                    black_rook1_moved= false;
                if(move_no-1==black_rook2_move_no)
                    black_rook2_moved= false;
            }


            //Resetting en passant variables
            {
                if(move_no-2==white_pawn1_move_no)
                    white_pawn1_moved_two=false;
                if(move_no-2==white_pawn2_move_no)
                    white_pawn2_moved_two=false;
                if(move_no-2==white_pawn3_move_no)
                    white_pawn3_moved_two=false;
                if(move_no-2==white_pawn4_move_no)
                    white_pawn4_moved_two=false;
                if(move_no-2==white_pawn5_move_no)
                    white_pawn5_moved_two=false;
                if(move_no-2==white_pawn6_move_no)
                    white_pawn6_moved_two=false;
                if(move_no-2==white_pawn7_move_no)
                    white_pawn7_moved_two=false;
                if(move_no-2==white_pawn8_move_no)
                    white_pawn8_moved_two=false;
                if(move_no-2==black_pawn1_move_no)
                    black_pawn1_moved_two=false;
                if(move_no-2==black_pawn2_move_no)
                    black_pawn2_moved_two=false;
                if(move_no-2==black_pawn3_move_no)
                    black_pawn3_moved_two=false;
                if(move_no-2==black_pawn4_move_no)
                    black_pawn4_moved_two=false;
                if(move_no-2==black_pawn5_move_no)
                    black_pawn5_moved_two=false;
                if(move_no-2==black_pawn6_move_no)
                    black_pawn6_moved_two=false;
                if(move_no-2==black_pawn7_move_no)
                    black_pawn7_moved_two=false;
                if(move_no-2==black_pawn8_move_no)
                    black_pawn8_moved_two=false;
            }


            move_no--;
            for (int i = 0; i < 8; i++)
                System.arraycopy(boardHistory[move_no][i], 0, pieces[i], 0, 8);
            draw_board();
            if (piece_selected) {

                ImageView show_piece = findViewById(R.id.show_selected_piece);
                show_piece.setImageResource(R.drawable.blank);
                piece_selected = false;
                selected_piece = '-';
                reset_places();
            }
            white_turn = !white_turn;
            TextView hint = findViewById(R.id.hint);
            if (white_turn)
                hint.setText(getString(R.string.white) + " " + getString(R.string.select_piece));
            else
                hint.setText(getString(R.string.black) + " " + getString(R.string.select_piece));
            debug(move_no);
        }
    }

    //Redo
    @SuppressLint("SetTextI18n")
    public void redo(View view)
    {
        if (move_no < max_move_no && !is_promotion) {
            //Reapplying castling variables
            {
                if(move_no+1==white_king_move_no)
                    white_king_moved= true;
                if(move_no+1==black_king_move_no)
                    black_king_moved= true;
                if(move_no+1==white_rook1_move_no)
                    white_rook1_moved= true;
                if(move_no+1==white_rook2_move_no)
                    white_rook2_moved= true;
                if(move_no+1==black_rook1_move_no)
                    black_rook1_moved= true;
                if(move_no+1==black_rook2_move_no)
                    black_rook2_moved= true;
            }

            //Reverting en passant variables
            {
                if(move_no+1==white_pawn1_move_no)
                    white_pawn1_moved_two=true;
                if(move_no+1==white_pawn2_move_no)
                    white_pawn2_moved_two=true;
                if(move_no+1==white_pawn3_move_no)
                    white_pawn3_moved_two=true;
                if(move_no+1==white_pawn4_move_no)
                    white_pawn4_moved_two=true;
                if(move_no+1==white_pawn5_move_no)
                    white_pawn5_moved_two=true;
                if(move_no+1==white_pawn6_move_no)
                    white_pawn6_moved_two=true;
                if(move_no+1==white_pawn7_move_no)
                    white_pawn7_moved_two=true;
                if(move_no+1==white_pawn8_move_no)
                    white_pawn8_moved_two=true;
                if(move_no+1==black_pawn1_move_no)
                    black_pawn1_moved_two=true;
                if(move_no+1==black_pawn2_move_no)
                    black_pawn2_moved_two=true;
                if(move_no+1==black_pawn3_move_no)
                    black_pawn3_moved_two=true;
                if(move_no+1==black_pawn4_move_no)
                    black_pawn4_moved_two=true;
                if(move_no+1==black_pawn5_move_no)
                    black_pawn5_moved_two=true;
                if(move_no+1==black_pawn6_move_no)
                    black_pawn6_moved_two=true;
                if(move_no+1==black_pawn7_move_no)
                    black_pawn7_moved_two=true;
                if(move_no+1==black_pawn8_move_no)
                    black_pawn8_moved_two=true;
            }


            move_no++;
            for (int i = 0; i < 8; i++)
                System.arraycopy(boardHistory[move_no][i], 0, pieces[i], 0, 8);
            draw_board();
            if (piece_selected) {

                ImageView show_piece = findViewById(R.id.show_selected_piece);
                show_piece.setImageResource(R.drawable.blank);
                piece_selected = false;
                selected_piece = '-';
                reset_places();
            }
            white_turn = !white_turn;
            TextView hint = findViewById(R.id.hint);
            if (white_turn)
                hint.setText(getString(R.string.white) + " " + getString(R.string.select_piece));
            else
                hint.setText(getString(R.string.black) + " " + getString(R.string.select_piece));
            debug(move_no);
        }
    }
    //Undo All
    public void undo_all(View view)
    {
        if(!is_promotion) {
            while (move_no > 0) {
                undo(view);
            }
        }
    }

    //Redo All
    public void redo_all(View view)
    {
        if(!is_promotion) {
            while (move_no < max_move_no) {
                redo(view);
            }
        }
    }




    //Resetting the board
    @SuppressLint("SetTextI18n")
    public void reset_board(View view)
    {
        //Resetting the board
        pieces = new char[][]{
                {'r','n','b','q','k','b','n','r'},
                {'p','p','p','p','p','p','p','p'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'P','P','P','P','P','P','P','P'},
                {'R','N','B','Q','K','B','N','R'}
        };

        //Resetting promotion
        FrameLayout promotion = findViewById(R.id.promotion_palette);
        promotion.setVisibility(View.GONE);
        is_promotion=false;

        //Resetting the board history
        ImageView show_piece = findViewById(R.id.show_selected_piece);
        show_piece.setImageResource(R.drawable.blank);
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                places[i][j] = 0;
            }
        }
        
        //Initial Configuration
        TextView hint = findViewById(R.id.hint);
        hint.setText(getString(R.string.white)+" "+getString(R.string.select_piece));
        white_turn = true;
        selected_piece = '-';
        piece_selected = false;
        move_no =0;
        max_move_no=0;


        //Castling Variables Reset
        white_king_moved=false;
        white_king_move_no=-1;
        black_king_moved=false;
        black_king_move_no=-1;
        white_rook1_moved=false;
        white_rook1_move_no=-1;
        white_rook2_moved=false;
        white_rook2_move_no=-1;
        black_rook1_moved=false;
        black_rook1_move_no=-1;
        black_rook2_moved=false;
        black_rook2_move_no=-1;

        //En Passant Variables Reset
        black_pawn1_moved_two=false;
        black_pawn1_move_no=-1;
        black_pawn2_moved_two=false;
        black_pawn2_move_no=-1;
        black_pawn3_moved_two=false;
        black_pawn3_move_no=-1;
        black_pawn4_moved_two=false;
        black_pawn4_move_no=-1;
        black_pawn5_moved_two=false;
        black_pawn5_move_no=-1;
        black_pawn6_moved_two=false;
        black_pawn6_move_no=-1;
        black_pawn7_moved_two=false;
        black_pawn7_move_no=-1;
        black_pawn8_moved_two=false;
        black_pawn8_move_no=-1;

        white_pawn1_moved_two=false;
        white_pawn1_move_no=-1;
        white_pawn2_moved_two=false;
        white_pawn2_move_no=-1;
        white_pawn3_moved_two=false;
        white_pawn3_move_no=-1;
        white_pawn4_moved_two=false;
        white_pawn4_move_no=-1;
        white_pawn5_moved_two=false;
        white_pawn5_move_no=-1;
        white_pawn6_moved_two=false;
        white_pawn6_move_no=-1;
        white_pawn7_moved_two=false;
        white_pawn7_move_no=-1;
        white_pawn8_moved_two=false;
        white_pawn8_move_no=-1;

        //Check Variables Reset
        white_check=false;
        black_check=false;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                black_check_places[i][j]=0;
                white_check_places[i][j]=0;
                temp_pieces[i][j]=pieces[i][j];
            }
        }
        //Drawing the board
        draw_board();
    }

    //Resetting hints
    public void reset_places()
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8 ; j++)
            {
                places[i][j] = 0;
            }
        }
        draw_board();
    }




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        piece_selected = false;
        setContentView(R.layout.activity_main);
        draw_board();
        TextView hint = findViewById(R.id.hint);
        hint.setText(getString(R.string.select_piece));
        hint.setText(getString(R.string.white)+" "+getString(R.string.select_piece));
        storeBoard(0);
        move_no=0;

        //Castling Variables Reset
        white_king_moved=false;
        white_king_move_no=-1;
        black_king_moved=false;
        black_king_move_no=-1;
        white_rook1_moved=false;
        white_rook1_move_no=-1;
        white_rook2_moved=false;
        white_rook2_move_no=-1;
        black_rook1_moved=false;
        black_rook1_move_no=-1;
        black_rook2_moved=false;
        black_rook2_move_no=-1;

        //En Passant Variables Reset
        black_pawn1_moved_two=false;
        black_pawn1_move_no=-1;
        black_pawn2_moved_two=false;
        black_pawn2_move_no=-1;
        black_pawn3_moved_two=false;
        black_pawn3_move_no=-1;
        black_pawn4_moved_two=false;
        black_pawn4_move_no=-1;
        black_pawn5_moved_two=false;
        black_pawn5_move_no=-1;
        black_pawn6_moved_two=false;
        black_pawn6_move_no=-1;
        black_pawn7_moved_two=false;
        black_pawn7_move_no=-1;
        black_pawn8_moved_two=false;
        black_pawn8_move_no=-1;

        white_pawn1_moved_two=false;
        white_pawn1_move_no=-1;
        white_pawn2_moved_two=false;
        white_pawn2_move_no=-1;
        white_pawn3_moved_two=false;
        white_pawn3_move_no=-1;
        white_pawn4_moved_two=false;
        white_pawn4_move_no=-1;
        white_pawn5_moved_two=false;
        white_pawn5_move_no=-1;
        white_pawn6_moved_two=false;
        white_pawn6_move_no=-1;
        white_pawn7_moved_two=false;
        white_pawn7_move_no=-1;
        white_pawn8_moved_two=false;
        white_pawn8_move_no=-1;

        //Check Variables Reset
        white_check=false;
        black_check=false;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                black_check_places[i][j]=0;
                white_check_places[i][j]=0;
                temp_pieces[i][j]=pieces[i][j];
            }
        }
    }
}