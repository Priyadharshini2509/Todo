package com.example.todoappsqlite;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todolist;
    private MainActivity activity;
    private DatabaseHandler databaseHandler;

    public ToDoAdapter(DatabaseHandler databaseHandler,MainActivity activity){
        this.databaseHandler = databaseHandler;
        this.activity=activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);
        return new ViewHolder(itemView);
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        databaseHandler.openDatabase();
        ToDoModel item = todolist.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    databaseHandler.updateStatus(item.getId(),1);
                }
                else{
                    databaseHandler.updateStatus(item.getId(),0);
                }
            }
        });
    }
    public int getItemCount(){
        return todolist.size();
    }
    private boolean toBoolean(int n){
        return n!=0;
    }
    public void setTasks(List<ToDoModel> todolist){
        this.todolist=todolist;
        notifyDataSetChanged();
    }
    public Context getContent()
    {
        return activity;
    }
    public void deleteItem(int position){
        ToDoModel item = todolist.get(position);
        databaseHandler.deleteTask(item.getId());
        todolist.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position){
        ToDoModel item = todolist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task=view.findViewById(R.id.todocheckbox);
        }
    }
}
