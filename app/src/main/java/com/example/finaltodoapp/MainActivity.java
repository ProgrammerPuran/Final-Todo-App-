package com.example.finaltodoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.finaltodoapp.viewModel.ToDoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabAddNew;
    Fragment mFragement;
    FragmentManager mFragmentManager;
    ToDoViewModel mToDoViewModel;
    AlertDialog.Builder mAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragement = new TodoListFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.list_container, mFragement)
                .commit();

        fabAddNew=findViewById(R.id.fab_add_new_todo);
        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditTodoActivity.class);
                startActivity(intent);
            }
        });

        mToDoViewModel= ViewModelProviders.of(this).get(ToDoViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_delete_all:

// setting Dialogbox for Delete all.
                mAlertDialog = new AlertDialog.Builder(MainActivity.this);

                mAlertDialog.setMessage(getString(R.string.edit_todo_task_deletedAll))
                        .setCancelable(false)
                        .setTitle(getString(R.string.app_name));
                        //.setIcon(R.drawable.todo);

                mAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mToDoViewModel.deleteAll();

                    }
                });

                mAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                mAlertDialog.show();

                break;

            //setting Alertdialogbox for logout.

            case R.id.menu_logout:

                mAlertDialog = new AlertDialog.Builder(MainActivity.this);

                mAlertDialog.setMessage(getString(R.string.edit_logout))
                        .setCancelable(false)
                        .setTitle(getString(R.string.app_name));
                        //.setIcon(R.drawable.todo);

                mAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("todo_pref", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

                mAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                mAlertDialog.show();

                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
