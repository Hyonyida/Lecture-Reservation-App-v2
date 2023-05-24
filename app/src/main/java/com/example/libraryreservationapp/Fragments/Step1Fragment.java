package com.example.libraryreservationapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryreservationapp.Common.RoomItemDecoration;
import com.example.libraryreservationapp.Interface.BuildingLoadListener;
import com.example.libraryreservationapp.Interface.RoomLoadListener;
import com.example.libraryreservationapp.R;
import com.example.libraryreservationapp.Room;
import com.example.libraryreservationapp.StudentRoomAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class Step1Fragment extends Fragment implements BuildingLoadListener, RoomLoadListener {

    CollectionReference roomsRef;
    BuildingLoadListener buildingLoadListener;
    RoomLoadListener roomLoadListener;

    MaterialSpinner spinner;
    RecyclerView recycler_building;

    Unbinder unbinder;

    AlertDialog dialog;

    static Step1Fragment instance;

    public static Step1Fragment getInstance(){
        if (instance == null)
            instance = new Step1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomsRef = FirebaseFirestore.getInstance().collection("room");
        buildingLoadListener = this;
        roomLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_step1, container, false);
        spinner = v.findViewById(R.id.buildingSpinner);
        recycler_building = v.findViewById(R.id.recycler_building);
        unbinder = ButterKnife.bind(this, v);

        initView();
        loadAllBuildings();
        return v;
    }

    private void initView() {
        recycler_building.setHasFixedSize(true);
        recycler_building.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_building.addItemDecoration(new RoomItemDecoration(4));
    }

    //Spinner load values
    private void loadAllBuildings() {
        roomsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<String> list = new ArrayList<>();
                    list.add("건물을 선택해주세요");
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                        String building = documentSnapshot.getString("building");
                        if(!list.contains(building)) {
                            list.add(building);
                        }
                    }
                    buildingLoadListener.onAllBuildingLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                buildingLoadListener.onAllBuildingLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllBuildingLoadSuccess(List<String> buildingNameList) {
        spinner.setItems(buildingNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0) {
                    loadRoomsOfBuilding(item.toString());
                } else
                    recycler_building.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAllBuildingLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    private void loadRoomsOfBuilding(final String buildingName) {
        dialog.show();

        roomsRef = FirebaseFirestore.getInstance().collection("room");

        roomsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            List<Room> list = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                        Room room = documentSnapshot.toObject(Room.class);
                        room.setRoomId(documentSnapshot.getId());
                        if(room.getBuilding().equals(buildingName) && room.isAvailable()) {
                            list.add(room);
                        }
                    }
                    roomLoadListener.onAllRoomLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                roomLoadListener.onAllRoomLoadFailed(e.getMessage());
            }
        });
    }



    @Override
    public void onAllRoomLoadSuccess(List<Room> roomList) {
        StudentRoomAdapter adapter = new StudentRoomAdapter(getActivity(), roomList);
        recycler_building.setAdapter(adapter);
        recycler_building.setVisibility(View.VISIBLE);

        dialog.dismiss();
    }

    @Override
    public void onAllRoomLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
