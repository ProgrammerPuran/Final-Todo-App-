package com.example.finaltodoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finaltodoapp.data.ToDoRoomDatabase;
import com.example.finaltodoapp.model.entity.ETodo;
import com.example.finaltodoapp.viewModel.ToDoViewModel;

import java.text.SimpleDateFormat;
import java.util.List;


public class TodoListFragment extends Fragment {

    View rootView;
    RecyclerView todoRecyclerView;
    ToDoViewModel mToDoViewModel;
    ToDoRoomDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_todo_list, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        todoRecyclerView = rootView.findViewById(R.id.todo_recycler_view);
        todoRecyclerView.setLayoutManager(layoutManager);
        mToDoViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);
        database = ToDoRoomDatabase.getDatabase(getActivity().getApplicationContext());

        LoadRV();

        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        List<ETodo> todoList = mToDoViewModel.getmAllTodos().getValue();
                        TodoAdapter adapter = new TodoAdapter(todoList);
                        ETodo todo = adapter.getTodoAt(viewHolder.getAdapterPosition());
                        mToDoViewModel.deleteById(todo);
                    }
                }).attachToRecyclerView(todoRecyclerView);
        return rootView;
    }

    void LoadRV() {
        mToDoViewModel.getmAllTodos().observe(this, new Observer<List<ETodo>>() {
            @Override
            public void onChanged(List<ETodo> todoList) {
                TodoAdapter adapter = new TodoAdapter(todoList);
                todoRecyclerView.setAdapter(adapter);
            }
        });
    }

    private class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mDate;

        public TodoViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_todo, parent, false));
            mTitle = itemView.findViewById(R.id.list_item_tv_Name);
            mDate = itemView.findViewById(R.id.list_item_tv_Date);

            // Setting the onclicklistner on Recycler View
            mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TodoAdapter adapter = new TodoAdapter(mToDoViewModel.getmAllTodos().getValue());
                    int position = getAdapterPosition();
                    ETodo eTodo = adapter.getTodoAt(position);

                    Intent intent = new Intent(getActivity(), EditTodoActivity.class);
                    intent.putExtra("TodoId", eTodo.getId());
                    startActivity(intent);
                }
            });

            // Setting the onclicklistner on Recycler View
            mDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TodoAdapter adapter = new TodoAdapter(mToDoViewModel.getmAllTodos().getValue());
                    int position = getAdapterPosition();
                    ETodo eTodo = adapter.getTodoAt(position);

                    Intent intent = new Intent(getActivity(), EditTodoActivity.class);
                    intent.putExtra("TodoId", eTodo.getId());
                    startActivity(intent);
                }
            });
        }

        public void bind(ETodo todo) {
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
            mTitle.setText(todo.getTitle());
            mDate.setText(dateFormater.format(todo.getTodo_date()));
        }
    }

    private class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {
        List<ETodo> mETodoList;
        public TodoAdapter(List<ETodo> todoList) {
            mETodoList = todoList;
        }
        @NonNull
        @Override
        public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TodoViewHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
            ETodo todo = mETodoList.get(position);
            LinearLayout layout = (LinearLayout)((ViewGroup)holder.mTitle.getParent());
            switch (todo.getPriority())
            {
                case 1:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_high_priority));
                    break;

                case 2:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_medium_priority));
                    break;

                case 3:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_low_priority));
                    break;
            }
            holder.bind(todo);
        }
        @Override
        public int getItemCount() {
            return mETodoList.size();
        }

        public ETodo getTodoAt(int index) {
            return mETodoList.get(index);
        }
    }
}


