package com.example.yadisak.androidtest3._Extension;

@SuppressWarnings("serial")
public enum CMDState {
    NEW(1), EDIT(2);

    private int value;

    private CMDState(int value) {
        this.value = value;
    }
};
