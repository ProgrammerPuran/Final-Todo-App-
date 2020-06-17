package com.example.finaltodoapp.data;

import android.app.Application;
import android.os.AsyncTask;
import com.example.finaltodoapp.model.entity.ETodo;
import java.util.List;
import androidx.lifecycle.LiveData;

public class TodoRepository {
    private ToDoDAO mToDoDAO;
    private LiveData<List<ETodo>> mAllTodoList;

    public TodoRepository(Application application) {
        ToDoRoomDatabase database = ToDoRoomDatabase.getDatabase(application);
        mToDoDAO = database.mToDoDAO();
        mAllTodoList = mToDoDAO.getAllToDos();
    }

    public LiveData<List<ETodo>> getmAllTodoList() {
        return mAllTodoList;
    }

    public ETodo getTodoById(int id)
    {
        return mToDoDAO.getToDoById(id);
    }

    public void insert(ETodo todo){
        new insertTodoAsynchronousTask(mToDoDAO).execute(todo);
    }

    public void update(ETodo todo){
        new insertTodoAsynchronousTask(mToDoDAO).execute(todo);
    }

    public void deleteAll()
    {
        new deleteAllTodoAsynchTask(mToDoDAO).execute();
    }

    public void deleteById(ETodo todo)
    {
        new deleteByIdTodoAsynchTask(mToDoDAO).execute(todo);
    }

    private static class insertTodoAsynchronousTask extends AsyncTask<ETodo, Void, Void>{
        private ToDoDAO mToDoDAO;
        private insertTodoAsynchronousTask(ToDoDAO todoDAO){
            mToDoDAO = todoDAO;
        }
        @Override
        protected Void doInBackground(ETodo... todos) {
            mToDoDAO.insert(todos[0]);
            return null;
        }
    }

    private static class updateTodoAsynchTask extends AsyncTask<ETodo, Void, Void>

    {
        private ToDoDAO mToDoDAO;
        private updateTodoAsynchTask(ToDoDAO todoDAO)
        {
            mToDoDAO = todoDAO;
        }
        @Override
        protected Void doInBackground(ETodo... todos) {

            mToDoDAO.update(todos[0]);
            return null;
        }
    }

    private static class deleteAllTodoAsynchTask extends AsyncTask<ETodo, Void, Void>

    {
        private ToDoDAO mToDoDAO;
        private deleteAllTodoAsynchTask(ToDoDAO todoDAO)
        {
            mToDoDAO = todoDAO;
        }
        @Override
        protected Void doInBackground(ETodo... todos) {
            mToDoDAO.deleteAll();
            return null;
        }
    }

    private static class deleteByIdTodoAsynchTask extends AsyncTask<ETodo, Void, Void>

    {
        private ToDoDAO mToDoDAO;
        private deleteByIdTodoAsynchTask(ToDoDAO todoDAO)
        {
            mToDoDAO = todoDAO;
        }
        @Override
        protected Void doInBackground(ETodo... todos) {
            mToDoDAO.deleteById(todos[0]);
            return null;
        }
    }
}
