package com.example.libraryreservationapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryreservationapp.Common.BookItemDecoration;
import com.example.libraryreservationapp.Interface.ClassLoadListener;
import com.example.libraryreservationapp.Interface.BookLoadListener;
import com.example.libraryreservationapp.R;
import com.example.libraryreservationapp.Book;
import com.example.libraryreservationapp.StudentBookAdapter;
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

public class BooksStep1Fragment extends Fragment implements ClassLoadListener, BookLoadListener{

    CollectionReference booksRef;
    ClassLoadListener classLoadListener;
    BookLoadListener bookLoadListener;

    MaterialSpinner spinner;
    RecyclerView recycler_class;

    Unbinder unbinder;

    AlertDialog dialog;

    static BooksStep1Fragment instance;

    public static BooksStep1Fragment getInstance(){
        if (instance == null)
            instance = new BooksStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        booksRef = FirebaseFirestore.getInstance().collection("books");
        classLoadListener = this;
        bookLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_books_step1, container, false);
        spinner = v.findViewById(R.id.classSpinner);
        recycler_class = v.findViewById(R.id.recycler_class);
        unbinder = ButterKnife.bind(this, v);

        initView();
        loadAllClasses();
        return v;
    }

    private void initView() {
        recycler_class.setHasFixedSize(true);
        recycler_class.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler_class.addItemDecoration(new BookItemDecoration(4));
    }

    //Spinner load values
    private void loadAllClasses() {
        booksRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<String> list = new ArrayList<>();
                    list.add("Select Course");
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                        String course = documentSnapshot.getString("course");
                        if(!list.contains(course)) {
                            list.add(course);
                        }
                    }
                    classLoadListener.onAllClassLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                classLoadListener.onAllClassLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllClassLoadSuccess(List<String> classNameList) {
        spinner.setItems(classNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0) {
                    loadBooksOfClass(item.toString());
                } else
                    recycler_class.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAllClassLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    private void loadBooksOfClass(final String className) {
        dialog.show();

        booksRef = FirebaseFirestore.getInstance().collection("books");

        booksRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            List<Book> list = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                        Book book = documentSnapshot.toObject(Book.class);
                        book.setBookId(documentSnapshot.getId());
                        if(book.getCourse().equals(className)) {
                            list.add(book);
                        }
                    }
                    bookLoadListener.onAllBookLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bookLoadListener.onAllBookLoadFailed(e.getMessage());
            }
        });
    }



    @Override
    public void onAllBookLoadSuccess(List<Book> bookList) {
        Log.d("LOADBOOKS", "onAllBookLoadSuccess");
        StudentBookAdapter adapter = new StudentBookAdapter(getActivity(), bookList);
        recycler_class.setAdapter(adapter);
        recycler_class.setVisibility(View.VISIBLE);

        dialog.dismiss();
    }

    @Override
    public void onAllBookLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}