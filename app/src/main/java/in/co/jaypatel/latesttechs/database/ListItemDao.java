package in.co.jaypatel.latesttechs.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 16/1/18.
 */

@Dao
public interface ListItemDao {

    @Query("SELECT * FROM users")
    List<ListItem> getListItems();

    @Query("SELECT * FROM users WHERE id = :ID")
    ListItem getListItemById(int ID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertItem(ListItem listItem);

    @Delete
    int deleteItem(ListItem listItem);
}
