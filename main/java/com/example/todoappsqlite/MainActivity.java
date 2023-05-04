package com.example.todoappsqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener  {

    private RecyclerView taskrecycleview;
    private ToDoAdapter taskadapter;
    private FloatingActionButton floatingActionButton;

    private List<ToDoModel> tasklist;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        databaseHandler = new DatabaseHandler(this);
        databaseHandler.openDatabase();

        tasklist= new ArrayList<>();

        taskrecycleview = findViewById(R.id.taskRecyclerView);
        taskrecycleview.setLayoutManager(new LinearLayoutManager(this));
        taskadapter = new ToDoAdapter(databaseHandler, this );
        taskrecycleview.setAdapter(taskadapter);

        floatingActionButton = findViewById(R.id.fab);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskadapter));
        itemTouchHelper.attachToRecyclerView(taskrecycleview);

        //taskadapter.setTasks(tasklist);
        tasklist=databaseHandler.getAllTasks();
        Collections.reverse(tasklist);
        taskadapter.setTasks(tasklist);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
    }

    public void handleDialogClose(DialogInterface dialog){
        tasklist = databaseHandler.getAllTasks();
        Collections.reverse(tasklist);
        taskadapter.setTasks(tasklist);
        taskadapter.notifyDataSetChanged();
    }
}