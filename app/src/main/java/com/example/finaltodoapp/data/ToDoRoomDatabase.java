package com.example.finaltodoapp.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.finaltodoapp.model.entity.ETodo;

import java.util.Date;

@Database(entities = {ETodo.class}, version = 1, exportSchema = false)
public abstract class ToDoRoomDatabase extends RoomDatabase {
     public abstract ToDoDAO mToDoDAO();

     private static ToDoRoomDatabase INSTANCE;
     public static ToDoRoomDatabase getDatabase(Context context) {
         if (INSTANCE == null) {
             synchronized (ToDoRoomDatabase.class) {
                 if (INSTANCE == null) {
                     INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                             ToDoRoomDatabase.class, "todo.db")
                             .allowMainThreadQueries()
                             .fallbackToDestructiveMigration()
                             .build();
                 }
             }

         }
         return INSTANCE;
     }

    private static class populateDbAsynchTask extends AsyncTask<ETodo, Void, Void>

    {
        private ToDoDAO mTodoDAO;
        private populateDbAsynchTask(ToDoRoomDatabase db)
        {
            mTodoDAO = db.mToDoDAO();
        }

        //Generating Override Method (doInBackground)


        @Override
        protected Void doInBackground(ETodo... todos) {

            Date date = new Date();
            ETodo todo = new ETodo("Demo Title", "Demo Description", date,1, false);
            mTodoDAO.insert(todo);
            return null;
        }
    }

    private static RoomDatabase.Callback sCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDbAsynchTask(INSTANCE).execute();
        }
    };
}
