package com.demoapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.demoapp.Models.CarModel;
import com.demoapp.Models.CityModel;
import com.demoapp.Models.DriverModel;
import com.demoapp.Models.SmthModel;
import com.demoapp.Models.UserModel;
import com.sqlitemanager.SQLiteManager;
import com.sqlitemanager.SortOrder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize singleton SqLiteManager once:
        //Todo: Take into account upgrade stuff in version number and Handle it in Annotations, OK?
        new SQLiteManager.Builder(this)
                .setDBName("my_new_db")
                .setTableList(
                        CarModel.class,
                        DriverModel.class,
                        CityModel.class,
                        UserModel.class,
                        SmthModel.class)
                .buildDatabase();


        // Creating model
        UserModel userModel = new UserModel();
        userModel.profilePicture = "some image uri";
        userModel.fullname = "First Last name";
        userModel.age = 12;

        //SQLiteManager.getInstance().insert(userModel);

        //Inserting it to the table
        long feedback_1 = userModel.insert();

        //Inserting again. This will create new User in db.
        long feedback_1_1 = userModel.insert();

        //Creating model
        DriverModel driver = new DriverModel();
        driver.birtdate = "12-01-12";
        driver.fullname = "Some Fullname here";
        driver.email = "Some Email here";

        //Inserting to the table
        long feedback_2 = driver.insert();


        driver.birtdate = "12-01-12";
        driver.fullname = "Soeme Fullname here";
        driver.email = "Somee Email here";

        driver.insert();

        driver.birtdate = "12-01-12";
        driver.fullname = "Soeme Fulleename here";
        driver.email = "Somee Email herewe";


        //Assigning it to main model
        driver.insert();


        //Inserting again. It won't be inserted, because fullname and email
        //are unique columns. Once you entered them, you need to set different
        //values for them. So, this will return -1
        long feedback_3 = driver.insert();

        //Creating model
        CarModel carModel = new CarModel();
        carModel.releaseDate = "12-12-12";
        carModel.model = "some model";
        carModel.name = "some name";

        //Inserting to the table
        long feedback_4 = carModel.insert();

        carModel.releaseDate = "12-12-12";
        carModel.model = "some modeal";
        carModel.name = "some naasme";

        //Inserting to the table
        carModel.insert();

        carModel.releaseDate = "12-12d-12";
        carModel.model = "ssome modeal";
        carModel.name = "some naasdme";

        //Inserting to the table
        carModel.insert();

        CityModel cityModel = new CityModel();
        cityModel.id = 1;
        cityModel.num_of_roads = 2;
        cityModel.additionalInfo = "sdkkdkd";

        //CityModel has a column named "name" which is "NOT NULL"
        //So, since this object's name value is empty, this insertion
        //will not happen and return -1
        long asd = cityModel.insert();

        //This will assign the values from db data with id = 1
        //to myCarModel object.
        CarModel myCarModel = new CarModel();
        myCarModel.fill(1); // myCarModel contains data of columns where id = 1

        DriverModel driverModel2 = new DriverModel();
        driverModel2.fill(1);

        UserModel myUser = SQLiteManager.find(UserModel.class, 1); // where id = 1

        UserModel myUser2 = SQLiteManager.find("users", 1); // where id = 1


        //Get all data with type
        ArrayList<DriverModel> drivers = SQLiteManager.all(DriverModel.class);

        //Get all data with table name
        ArrayList<CityModel> cities = SQLiteManager.all("cities");


        //This will delete everything
        //(Database, Tables and all data in them)
        //SQLiteManager.deleteDatabase();

        //This will also delete everything, but rebuild them again
        //(Only tables and Constraints, Not Data). All data will be gone
        //SQLiteManager.refreshDatabase();


        List<DriverModel> driverModels = new SQLiteManager
                .Select("drivers")
                .where("id>?", "1")
                .sort("id", SortOrder.ASC)
                .limit(5)
                .getList();


        List<DriverModel> driverModels2 = new SQLiteManager
                .Select(DriverModel.class)
                .where("id>?", "1")
                .sort("id", SortOrder.RANDOM)
                .limit(2)
                .getList();

        Cursor cursor = new SQLiteManager
                .Select(DriverModel.class)
                .where("drivers.id>?", "1")
                .sort("cars.id", SortOrder.ASC)
                .limit(5)
                .innerJoin("carId")
                .columns("cars.id", "drivers.fullname", "cars.release_date", "drivers.id")
                .getCursor();
//
//        String[] names = cursor.getColumnNames();
//        if (cursor.moveToFirst()) {
//            do {
//                String stringData = cursor.getString(cursor.getColumnIndex("column_name"));
//                int intData = cursor.getInt(cursor.getColumnIndex("column_name"));
//                // ...
//                //Your code here
//                // ...
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//

        int a = 123 + 32;
        a++;
        Log.d(TAG, "Smth : " + a);

        // Get (Select) random data

        // Update in table

        // Delete data from DB with condition

        // Query Execute method

        // Select data from several tables with Foreign Key


    }
}
