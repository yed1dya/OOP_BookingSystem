package database;

import java.time.LocalDate;
import java.util.HashMap;

/*
Design Pattern - Flyweight:
Since there can be any number of rooms,
and many of them will have overlapping dates,
we'll implement this 'MyDate' class in a flyweight method -
the constructor is private, and calling the 'date()'
method returns the requested date.
 */
public class MyDate {
    private static LocalDate t = LocalDate.now();
    /*
    The dates are stored in a HashMap of 2D arrays:
    Each entry in the map is a year.
    Within the year, the array is [month][day].
    Finding the year is O(1), getting the date inside the year is also O(1).
     */
    private static HashMap<Integer, MyDate[][]> dates = new HashMap<>();
    private int year, month, day;

    private MyDate(int year, int month, int day){
        if(!dates.containsKey(year)){
            dates.put(year, new MyDate[12][31]);
        }
        this.year = year;
        this.month = month;
        this.day = day;
        dates.get(year)[month-1][day-1] = this;
    }
    public static MyDate date(int year, int month, int day){
        if(!dates.containsKey(year)){
            dates.put(year, new MyDate[12][31]);
        }
        if(dates.get(year)[month-1][day-1]!=null){
            return dates.get(year)[month-1][day-1];
        }
        else dates.get(year)[month-1][day-1] = new MyDate(year, month, day);
        return dates.get(year)[month-1][day-1];
    }
    public static MyDate current(){
        return date(t.getYear(), t.getMonthValue(), t.getDayOfMonth());
    }
    /*
    returns the number of days from the start date to the end.
     */
    public static int days(MyDate start, MyDate end){
        int count = 0;
        while (!start.equals(end)){
            count++;
            start = start.next();
        }
        return count;
    }

    /**
     * Iterator:
     * (can add options for months that have fewer days, leap years, etc.)
     * @return the next date.
     */
    public MyDate next(){
        int day = this.day==31 ? 1 : this.day+1;
        int month = day==1 ? (this.month==12 ? 1 : this.month+1) : this.month;
        int year = (day==1 && month==1) ? this.year+1 : this.year;
        return date(year, month, day);
    }
    public MyDate previous(){
        int day = this.day==1 ? 31 : this.day-1;
        int month = day==31 ? (this.month==1 ? 12 : this.month-1) : this.month;
        int year = (day==31 && month==12) ? this.year-1 : this.year;
        return date(year,month,day);
    }
    /**
     * @param other date to compare
     * @return +1 if other > this, 0 if equal, -1 else
     */
    public int compare(MyDate other){
        return (this.year > other.year) ? -1 :
                (this.year < other.year) ? 1 :
                        (this.month > other.month) ? -1 :
                                (this.month < other.month) ? 1 :
                                        Integer.compare(other.day, this.day);
    }

    public boolean equals(MyDate other){
        return this.compare(other)==0;
    }
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getDay() {
        return day;
    }
    public String toString(){
        return day+"."+month+"."+year;
    }
}
