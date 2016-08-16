package com.lionsandwich.seeilm.utils;

import java.util.ArrayList;

/**
 * Created by Eerik on 20-Jul-16.
 */
public class NumberToWord {

    private static ArrayList<String> words = new ArrayList<>();


    private static void addElementsToList(){
        words.add("null");
        words.add("üks");
        words.add("kaks");
        words.add("kolm");
        words.add("neli");
        words.add("viis");
        words.add("kuus");
        words.add("seitse");
        words.add("kaheksa");
        words.add("üheksa");
        words.add("kümme");




    }

    public static String converter(int number){
        String returnValue = null;

        addElementsToList();

        if(number <0){
            returnValue =  "miinus " +convertMinusDegress(number);
        }else{
            returnValue =  "pluss "  +convertPlusDegrees(number);
        }

        return returnValue;
    }


    private static String convertMinusDegress(int number){
        number = number * -1;
        if(number > 10 && number < 20){
            int numberHolder = number -10;
            return   words.get(numberHolder) + "teist";
        }else if(number > 19){
            int numberHolder = number /10 ;
            int unit         = number - numberHolder *10;
            if(unit == 0){
                return  words.get(numberHolder) + "kümmend";
            }else{
                return words.get(numberHolder) + "kümmend " + words.get(unit);

            }
        }else if(number >99){
            return "...";
        }
        return  words.get(number);
    }

    private static String convertPlusDegrees(int number){
        if(number > 10 && number < 20){
            int numberHolder = number -10;
            return words.get(numberHolder) + "teist";
        }else if(number > 19){
            int numberHolder = number /10 ;
            int unit         = number - numberHolder *10;
            if(unit == 0){
                return words.get(numberHolder) + "kümmend";
            }else{
                return words.get(numberHolder) + "kümmend " + words.get(unit);

            }
        }else if(number >99){
            return "...";
        }
        return words.get(number);
    }



}
