package com.example.dontsmoke;

public class Model {

    private String drink, sleep, id, date, food, status;

    public Model() {

    }

    public Model(String drink, String sleep, String id, String date, String food, String status) {
        this.drink = drink;
        this.sleep = sleep;
        this.id = id;
        this.date = date;
        this.food = food;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
