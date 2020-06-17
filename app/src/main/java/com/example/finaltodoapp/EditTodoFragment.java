package com.example.finaltodoapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.finaltodoapp.model.entity.ETodo;
import com.example.finaltodoapp.viewModel.ToDoViewModel;
import com.example.finaltodoapp.data.ToDoRoomDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditTodoFragment extends Fragment {

    View rootView;
    EditText txtTitle, txtDescription, txtDate;
    RadioGroup rgPriority;
    RadioButton rbHigh, rbMedium, rbLow, rbSelected;
    CheckBox chkIsCompleted;
    Button btnSave, btnCancel;

    AlertDialog.Builder mAlertDialog;
    DatePickerDialog mDatePickerDialog;

    public static final int HIGH_PRIORITY=1;
    public static final int MEDIUM_PRIORITY=2;
    public static final int LOW_PRIORITY=3;

    private ToDoViewModel mToDoViewModel;
    private int todoId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_todo, container, false);
        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);
        txtTitle = rootView.findViewById(R.id.edit_todo_title);
        txtDescription = rootView.findViewById(R.id.edit_todo_description);
        txtDate = rootView.findViewById(R.id.edit_todo_date);
        rgPriority = rootView.findViewById(R.id.edit_todo_rg_priority);
        rbHigh = rootView.findViewById(R.id.edit_todo_rb_priority_high);
        rbMedium = rootView.findViewById(R.id.edit_todo_rb_priority_medium);
        rbLow = rootView.findViewById(R.id.edit_todo_rb_priority_low);
        chkIsCompleted = rootView.findViewById(R.id.edit_todo_chk_is_completed);
        btnSave = rootView.findViewById(R.id.edit_todo_btn_save);
        btnCancel = rootView.findViewById(R.id.edit_todo_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveTodo();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayAlertDialog();
            }
        });

        txtDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    DiplayTodoDate();
                }
                return false;
            }
        });

        todoId = getActivity().getIntent().getIntExtra("TodoId", -1);
        if(todoId!=-1)
        {
            btnSave.setText(getText(R.string.edit_todo_update));
            ETodo todo = mToDoViewModel.getTodoById(todoId);
            txtTitle.setText(todo.getTitle());
            txtDescription.setText(todo.getDescription());
            DateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            txtDate.setText(formatter.format(todo.getTodo_date()));
            switch (todo.getPriority())
            {
                case 1:
                    rgPriority.check(R.id.edit_todo_rb_priority_high);
                    break;

                case 2:
                    rgPriority.check(R.id.edit_todo_rb_priority_medium);
                    break;

                case 3:
                    rgPriority.check(R.id.edit_todo_rb_priority_low);
                    break;
            }

            chkIsCompleted.setSelected(todo.isIs_completed());
        }
        return rootView;
    }
    void DisplayAlertDialog(){
        mAlertDialog = new AlertDialog.Builder(getContext());
        mAlertDialog.setMessage(getString(R.string.edit_todo_cancel_prompt))
                .setCancelable(false)
                .setTitle(getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher);

        mAlertDialog.setPositiveButton(getString(R.string.login_quit_app_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        mAlertDialog.setNegativeButton(getString(R.string.login_quit_app_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        mAlertDialog.show();
    }

    void DiplayTodoDate(){
        Calendar calendar = Calendar.getInstance();
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);
        int cMonth = calendar.get(Calendar.MONTH);
        int cYear = calendar.get(Calendar.YEAR);

        mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                txtDate.setText(year+"-"+month+"-"+day);

            }
        }, cYear, cMonth, cDay);
        mDatePickerDialog.show();
    }

    void SaveTodo(){
        ETodo todo = new ETodo();
        Date todoDate;
        int priority =1;
        int checkedPriority =-1;

        todo.setTitle(txtTitle.getText().toString());
        todo.setDescription(txtDescription.getText().toString());
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            todoDate = (Date)formatter.parse(txtDate.getText().toString());
            todo.setTodo_date(todoDate);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        checkedPriority = rgPriority.getCheckedRadioButtonId();
        switch(checkedPriority){
            case R.id.edit_todo_rb_priority_high:
                priority=HIGH_PRIORITY;
                break;
            case R.id.edit_todo_rb_priority_medium:
                priority=MEDIUM_PRIORITY;
                break;
            case R.id.edit_todo_rb_priority_low:
                priority=LOW_PRIORITY;
                break;
        }
        todo.setPriority(priority);
        todo.setIs_completed(chkIsCompleted.isChecked());
        if (todoId!=-1)
        {
            todo.setId(todoId);
            mToDoViewModel.update(todo);
            Toast.makeText(getActivity(),getText(R.string.edit_todo_task_updated), Toast.LENGTH_SHORT).show();

        }
        else{
            mToDoViewModel.insert(todo);
            Toast.makeText(getActivity(),getText(R.string.edit_todo_save), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }


    }
}
