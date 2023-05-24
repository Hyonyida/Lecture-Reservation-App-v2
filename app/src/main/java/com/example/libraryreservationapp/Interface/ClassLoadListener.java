package com.example.libraryreservationapp.Interface;

import java.util.List;

public interface ClassLoadListener {
    void onAllClassLoadSuccess(List<String> classNameList);
    void onAllClassLoadFailed(String message);
}
