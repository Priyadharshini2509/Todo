package com.example.todoappsqlite;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newtasktext;
    private Button newtasksavebutton;
    private DatabaseHandler databaseHandler;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newtasktext = getView().findViewById(R.id.newtasktext);
        newtasksavebutton=getView().findViewById(R.id.newtaskbutton);

        databaseHandler = new DatabaseHandler(getActivity());
        databaseHandler.openDatabase();
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newtasktext.setText(task);
            if(task.length()>0)
                newtasksavebutton.setTextColor(ContextCompat.getColor(getContext(), R.color.teal_200));
        }
        newtasktext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newtasksavebutton.setEnabled(false);
                    newtasksavebutton.setTextColor(Color.GRAY);
                }
                else{
                    newtasksavebutton.setEnabled(true);
                    newtasksavebutton.setTextColor(ContextCompat.getColor(getContext(), R.color.teal_200));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final boolean finalIsUpdate = isUpdate;
        newtasksavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newtasktext.getText().toString();
                if(finalIsUpdate){
                    databaseHandler.updateTask(bundle.getInt("id"),text);
                }
                else{
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);
                }
                dismiss();
            }
        });
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
        super.onDismiss(dialog);
    }
}
