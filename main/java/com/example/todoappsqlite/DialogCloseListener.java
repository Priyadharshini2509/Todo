package com.example.todoappsqlite;

import android.content.DialogInterface;

public interface DialogCloseListener {

    public default void handleDialogClose(DialogInterface dialog) {

    }
}
