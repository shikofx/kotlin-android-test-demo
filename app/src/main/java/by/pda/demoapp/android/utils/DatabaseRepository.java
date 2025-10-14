package by.pda.demoapp.android.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import by.pda.demoapp.android.database.AppDao;
import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.model.ProductModel;

import java.util.List;

public class DatabaseRepository {
    private AppDao noteDao;
    private LiveData<List<ProductModel>> allProducts;

    public DatabaseRepository(Application application) { //application is subclass of context
        AppDatabase database = AppDatabase.getInstance(application);
        noteDao = database.personDao();
        allProducts = noteDao.getAllProducts();
    }

    public void insert(ProductModel note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }


    public void insert(List<ProductModel> note) {
        new InsertProductListAsyncTask(noteDao,note).execute();
    }

    public void update(ProductModel note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(ProductModel note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<ProductModel>> getAllNotes() {
        return allProducts;
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertProductListAsyncTask extends AsyncTask<Void, Void, Void> { //static : doesnt have reference to the
        // repo itself otherwise it could cause memory leak!
        private AppDao noteDao;
        private List<ProductModel> list;
        private InsertProductListAsyncTask(AppDao noteDao,List<ProductModel> list) {
            this.noteDao = noteDao;
            this.list = list;
        }
        @Override
        protected Void doInBackground(Void... voids) { // ...  is similar to array
            noteDao.insertProduct(list); //single note
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertNoteAsyncTask extends AsyncTask<ProductModel, Void, Void> { //static : doesnt have reference to the
        // repo itself otherwise it could cause memory leak!
        private AppDao noteDao;
        private InsertNoteAsyncTask(AppDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(ProductModel... notes) { // ...  is similar to array
            noteDao.insertProduct(notes[0]); //single note
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateNoteAsyncTask extends AsyncTask<ProductModel, Void, Void> {
        private AppDao noteDao;
        private UpdateNoteAsyncTask(AppDao noteDao) { //constructor as the class is static
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(ProductModel... notes) {
//            noteDao.Update(notes[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteNoteAsyncTask extends AsyncTask<ProductModel, Void, Void> {
        private AppDao noteDao;
        private DeleteNoteAsyncTask(AppDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(ProductModel... notes) {
//            noteDao.Delete(notes[0]);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppDao noteDao;
        private DeleteAllNotesAsyncTask(AppDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
//            noteDao.DeleteAllNotes();
            return null;
        }
    }
}
