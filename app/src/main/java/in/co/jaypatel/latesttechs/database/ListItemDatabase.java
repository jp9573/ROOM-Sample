package in.co.jaypatel.latesttechs.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by jay on 16/1/18.
 */

@Database(entities = {ListItem.class}, version = 1, exportSchema = false)
public abstract class ListItemDatabase extends RoomDatabase {

    private static ListItemDatabase INSTANCE;

    public abstract ListItemDao listItemDao();

    public static ListItemDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                            ListItemDatabase.class,
                                            "users-database").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
