package util;

import java.util.Objects;

public class Date implements Comparable<Date>{


    private final int day;
    private final int month;
    private final int year;

    private Date(int day,int month,int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public static Date generateDate(int day,int month,int year){

        if (month > 12 || month < 1 || !checkDaysInMonth(day,month,year)){
            return null;
        }
        return new Date(day,month,year);
    }

    private static boolean checkDaysInMonth(int day,int month,int year){
        return day <= switch (month){
            case 2 -> checkLeapYear(year) ? 29 : 28;
            case 1,3,5,7,8,10,12 -> 31;
            case 4,6,9,11 -> 30;
            default -> 32;
        };
    }

    private static boolean checkLeapYear(int year){
        return (year % 4 == 0 && year % 100 == 0) || (year % 400 == 0);
    }



    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString(){
        return "%d-%d-%d".formatted(this.day,this.month,this.year);
    }

    @Override
    public int compareTo(Date other){

        if(this.year == other.year){

            if(this.month == other.month){

                if(this.day == other.day){

                    return 0;
                }

                else{
                    return this.day- other.day;
                }
            }
            else{

               return this.month - other.month;

            }

        }
        else{

            return this.year - other.year;

        }
    }

    @Override
    public boolean equals(Object that){
        if(that == null){
            return false;
        }
        if(that == this){
            return true;
        }
        if(! (that instanceof Date)){
            return  false;
        }

        if(this.day == ((Date)that).day && this.month == ((Date)that).month &&
                this.year == ((Date)that).year && this.hashCode() == (that).hashCode()){
            return true;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return Objects.hash(day,month,year);
    }

}
