package ca.inspiretechcanada.utils;

import javafx.beans.property.SimpleIntegerProperty;

public class Team {

    private final SimpleIntegerProperty number;
    private final SimpleIntegerProperty points;

    public Team(int number, int points){
        this.number = new SimpleIntegerProperty(number);
        this.points = new SimpleIntegerProperty(points);
    }

    public int getNumber(){
        return this.number.get();
    }

    public void setNumber(int number){
        this.number.set(number);
    }

    public int getPoints(){
        return this.points.get();
    }

    public void setPoints(int points){
        this.points.set(points);
    }

    public void addPoints(int points){
        this.points.set(getPoints() + points);
    }

}