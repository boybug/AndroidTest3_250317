package com.example.yadisak.androidtest3._Interface;

import com.example.yadisak.androidtest3._Extension.DAOState;

public interface ICRUDResult {
    public void onReturn(DAOState status, String message);
}
