package com.example.yadisak.androidtest3._Extension;


@SuppressWarnings("serial")
public enum DAOState {
    SUCCESS(1), CONDITION(0), ERROR(-1);

    private int value;

    private DAOState(int value) {
        this.value = value;
    }
}

