package com.lionsandwich.seeilm.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Eerik on 20-Jul-16.
 */
public class Translator {



    public static String translateDayOfWeek(String day){
        day = day.toLowerCase();
        switch (day){
            case "monday":
                return "esmaspäev";
            case "tuesday":
                return "teisipäev";
            case "wednesday":
                return "kolmapäev";
            case  "thursday":
                return "neljapäev";
            case "friday":
                return "reede";
            case "saturday":
                return "laupäev";
            case "sunday":
                return "pühapäev";
            default:
                return null;
        }
    }





    private static ArrayList<String> phenEnglish = new ArrayList<>();
    private static ArrayList<String> phenEstonian= new ArrayList<>();



    public static String translatePhen(String englishWord, Context context){
        readFile(context);

        for(int i = 0; i < phenEnglish.size() ; i ++){

            if(englishWord.equalsIgnoreCase(phenEnglish.get(i))){
                return phenEstonian.get(i);
            }
        }
        return null;

    }





    private  static void readFile(Context context){
        BufferedReader reader;

        try{
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("phenTranslation.txt")));

            String line= "";
            while(line != null){
                line = reader.readLine();
                if(line != null){
                    String[] separated = line.split("\t");
                    phenEnglish.add(separated[0]);
                    phenEstonian.add(separated[1]);
                }

            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }


}
