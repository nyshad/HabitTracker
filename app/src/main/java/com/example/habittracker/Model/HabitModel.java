package com.example.habittracker.Model;

public class HabitModel extends HabitId {

    private String habit, start, user;
    private int status;

    public String getHabit() {
        return habit;
    }

    public String getStart() {
        return start;
    }

    public int getStatus() {
        return status;
    }

    public String getUser() {
        return user;
    }
}
