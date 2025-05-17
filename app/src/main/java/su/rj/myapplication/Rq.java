package su.rj.myapplication;

import androidx.annotation.NonNull;
import androidx.room.*;

import java.util.ArrayList;
import java.util.List;

public class Rq {
    private String creds;
    private ArrayList<SubRq> subrqs;
    private final int id;

    public Rq(String creds, ArrayList<SubRq> subrqs, int id) {
        this.creds = creds;
        this.subrqs = subrqs;
        this.id = id;
    }

    public String getCreds() {
        return creds;
    }

    public void setCreds(String creds) {
        this.creds = creds;
    }

    public ArrayList<SubRq> getSubrqs() {
        return subrqs;
    }

    public void setSubrqs(ArrayList<SubRq> subrqs) {
        this.subrqs = subrqs;
    }

    public int getId() {
        return id;
    }

    @Entity
    public static class SubRq{
        @ColumnInfo(name = "parent_rq_id")
        public int parentRqId;
        @PrimaryKey
        public int id;
        @ColumnInfo(name = "tovar_id")
        public int tovarid;
        @ColumnInfo(name = "count")
        public int count;
        @ColumnInfo(name = "creds")
        public String creds;

        @NonNull
        @Override
        public String toString() {
            return "SubRQ id = " + id + ", tovarid = " + tovarid + ", count = " + count + ", creds = " + creds;
        }
    }
    @Dao
    public interface SubRqDAO{
        @Query("SELECT * FROM SubRq")
        List<SubRq> getAll();
        @Delete
        void delete(SubRq subRq);
        @Insert
        void insert(SubRq subRq);
        @Query("DELETE FROM SubRq")
        void deleteAll();
    }
    @Entity
    public static class Tovar{
        @ColumnInfo(name = "name")
        public String name;
        @PrimaryKey
        public int id;
        @ColumnInfo(name = "count")
        public int count;

        @NonNull
        @Override
        public String toString() {
            return "Tovar id = " + id + ", name = " + name + ", count = " + count;
        }
    }
    @Dao
    public interface TovarDAO{
        @Query("SELECT * FROM Tovar")
        List<Tovar> getAll();
        @Delete
        void delete(Tovar tovar);
        @Insert
        void insert(Tovar tovar);
    }
    @Database(entities = {SubRq.class,Tovar.class}, version = 1)
    public abstract static class AppDatabase extends RoomDatabase {
        public abstract SubRqDAO subRqDAO();
        public abstract TovarDAO tovarDAO();
    }
}
