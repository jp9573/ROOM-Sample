package in.co.jaypatel.latesttechs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.co.jaypatel.latesttechs.database.ListItem;
import in.co.jaypatel.latesttechs.database.ListItemDatabase;

public class MainActivity extends AppCompatActivity {

    EditText name;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name);
        listView = findViewById(R.id.listView);

        //Loads the content of db when activity first created
        refreshData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Releasing the INSTANCE object
        ListItemDatabase.destroyInstance();
    }

    //Called When SAVE button clicks
    public void saveData(View view) {
        if(name.getText().length() > 0) {
            String strName = name.getText().toString();
            InsertItem insertItem = new InsertItem();
            insertItem.execute(strName);
            name.setText("");
        }
    }

    //Called When FIND button clicks
    public void findData(View view) {
        try {
            int id = Integer.parseInt(name.getText().toString());
            FindItem findItem = new FindItem();
            findItem.execute(id);
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please enter valid id", Toast.LENGTH_SHORT).show();
        }
    }

    //Called When DELETE button clicks
    public void deleteData(View view) {
        try {
            int id = Integer.parseInt(name.getText().toString());
            DeleteItem deleteItem = new DeleteItem();
            deleteItem.execute(id);
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Please enter valid id", Toast.LENGTH_SHORT).show();
        }
    }

    //Called When SHOW ALL DATA button clicks
    public void showAllData(View view) {
        refreshData();
    }

    void refreshData() {
        FetchAllItems fetchAllItems = new FetchAllItems();
        fetchAllItems.execute();
    }

    //It will run background thread to insert item in db
    class InsertItem extends AsyncTask<String, Void, Long> {

        @Override
        protected Long doInBackground(String... params) {
            ListItem listItem = new ListItem(params[0]);
            long id = ListItemDatabase.getDatabase(MainActivity.this).listItemDao().insertItem(listItem);
            return id;
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);
            if(id > 0) {
                Toast.makeText(MainActivity.this, "Data Saved Successfully!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "Error in saving data", Toast.LENGTH_SHORT).show();
            }
            refreshData();
        }
    }

    //It will run background thread to fetch all the items from db
    class FetchAllItems extends AsyncTask<Void, Void, List<ListItem>> {

        @Override
        protected List<ListItem> doInBackground(Void... voids) {
            List<ListItem> items =  ListItemDatabase.getDatabase(MainActivity.this).listItemDao().getListItems();
            return items;
        }

        @Override
        protected void onPostExecute(List<ListItem> listItems) {
            super.onPostExecute(listItems);
            if(listItems.size() > 0) {
                ArrayList<String> arrayList= new ArrayList<>();
                for(ListItem item: listItems) {
                    arrayList.add(item.getId() + ". " + item.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);
            }else {
                ArrayList<String> arrayList= new ArrayList<>();
                arrayList.add("No data found");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);
            }
        }
    }

    //It will run background thread to find an item from db
    class FindItem extends AsyncTask<Integer, Void, ListItem> {

        @Override
        protected ListItem doInBackground(Integer... params) {
            int id = params[0];
            ListItem item = ListItemDatabase.getDatabase(MainActivity.this).listItemDao().getListItemById(id);
            return item;
        }

        @Override
        protected void onPostExecute(ListItem listItem) {
            super.onPostExecute(listItem);
            if(listItem != null) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(listItem.getId() + ". " + listItem.getName());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);
            }else {
                Toast.makeText(MainActivity.this, "No data found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //It will run background thread to delete an item from db
    class DeleteItem extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int id = params[0];
            ListItem item = ListItemDatabase.getDatabase(MainActivity.this).listItemDao().getListItemById(id);
            int res = ListItemDatabase.getDatabase(MainActivity.this).listItemDao().deleteItem(item);
            return res;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);
            if(res > 0) {
                Toast.makeText(MainActivity.this, "Data Deleted Successfully!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "Error in deleting data", Toast.LENGTH_SHORT).show();
            }
            refreshData();
        }
    }
}
