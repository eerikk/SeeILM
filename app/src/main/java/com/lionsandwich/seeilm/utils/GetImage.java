package com.lionsandwich.seeilm.utils;

import android.content.Context;
import android.util.Log;

import com.lionsandwich.seeilm.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Eerik on 21-Jul-16.
 */
public class GetImage {



    private static final int day   = 3;
    private static final int night = 2;

    public static int getPhenImage(Context context, String time, String desc){
        String image = null;
        switch(time){
            case "day":
                image = getImage(context, desc, day);
                break;
            case "night":
                image = getImage(context, desc, night);
                break;
        }
        int resID = context.getResources().getIdentifier(image , "mipmap", context.getPackageName());

        return resID;
    }



    private static String getImage(Context context, String desc, int type){
        String image = null;
        BufferedReader reader;
        try{
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("phenTranslation.txt")));

            String line= "";
            while(line != null){
                line = reader.readLine();
                if(line != null){
                    if(line.contains(desc)){
                        String[] separated = line.split("\t");
                        image = separated[type];
                    }
                }
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }

        return image;
    }
}
