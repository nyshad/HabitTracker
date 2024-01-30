package com.example.habittracker.Model;

import com.google.firebase.firestore.Exclude;

import org.checkerframework.checker.nullness.qual.NonNull;

public class HabitId {

    @Exclude
    public String HabitId;

    public <T extends HabitId> T withId(@NonNull final String id) {
        this.HabitId = id;
        return (T) this;
    }
}
