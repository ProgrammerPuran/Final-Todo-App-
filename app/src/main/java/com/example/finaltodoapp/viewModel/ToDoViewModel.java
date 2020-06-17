package com.example.finaltodoapp.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.finaltodoapp.data.TodoRepository;
import com.example.finaltodoapp.model.entity.ETodo;
import java.util.List;

public class ToDoViewModel extends AndroidViewModel {
    private TodoRepository mToDoRepository;
    private LiveData<List<ETodo>> mAllTodos;

    public ToDoViewModel(@NonNull Application application) {
        super(application);

        mToDoRepository = new TodoRepository(application);
        mAllTodos = mToDoRepository.getmAllTodoList();
    }

    public LiveData<List<ETodo>> getmAllTodos(){
        return mAllTodos;
    }

    public void insert(ETodo todo){
        mToDoRepository.insert(todo);
    }

    public void update (ETodo todo)
    {
        mToDoRepository.update(todo);
    }

    public void deleteById(ETodo todo)
    {
        mToDoRepository.deleteById(todo);
    }

    public void deleteAll() {
        mToDoRepository.deleteAll();
    }

    public ETodo getTodoById(int id)
    {
        return mToDoRepository.getTodoById(id);
    }


}
