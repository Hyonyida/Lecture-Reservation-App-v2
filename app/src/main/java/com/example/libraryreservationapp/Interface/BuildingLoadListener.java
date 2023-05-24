package com.example.libraryreservationapp.Interface;

import java.util.List;

public interface BuildingLoadListener {
    void onAllBuildingLoadSuccess(List<String> buildingNameList);
    void onAllBuildingLoadFailed(String message);
}
