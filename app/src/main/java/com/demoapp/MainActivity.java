package com.demoapp;

import android.database.sqlite.SQLiteQueryBuilder;
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
        userModel.age = 123;

        //Inserting it to the table
        long info1 = userModel.insert();
        userModel.insert();
        userModel.insert();
        userModel.insert();

        //Creating model
        DriverModel driver = new DriverModel();
        driver.birtdate = "12-01-12";
        driver.fullname = "Fullnaasddajjjsdme here";
        driver.email = "Emailsadasdda hjjjere";

        CarModel carModelq = new CarModel();
        carModelq.id = 1;

        driver.carModel = carModelq;

        //Inserting to the table
        long info = driver.insert();

        //Creating model
        driver.birtdate = "12-d01-12";
        driver.fullname = "Fuldlname here";
        driver.email = "Email hdere";

        //Inserting to the table
        driver.insert();

        //Creating model
        driver.birtdate = "12-fd01-12";
        driver.fullname = "Fuldlnfame here";
        driver.email = "Email hddere";

        //Inserting to the table
        driver.insert();

        //Creating model
        driver.birtdate = "12-d01a-12";
        driver.fullname = "Fuldlnasme here";
        driver.email = "Email hddere";

        //Inserting to the table
        driver.insert();

        //Creating model
        CarModel carModel = new CarModel();
        carModel.releaseDate = "adasd";
        carModel.model = "asdasd";
        carModel.name = "addasd";

        //Inserting to the table
        carModel.insert();

        //Creating model
        carModel.releaseDate = "addasd";
        carModel.model = "asdasdd";
        carModel.name = "addasdd";

        //Inserting to the table
        carModel.insert();

        //Get all data with type
        ArrayList<DriverModel> drivers = SQLiteManager.all(DriverModel.class);

        //Get all data with table name
        ArrayList<CityModel> cities = SQLiteManager.all("cities");


        //This will delete everything
        //(Database, Tables and all data inside them)
        //SQLiteManager.deleteDatabase();

        //This will also delete everything, but rebuild them again
        //(Only tables and Constraints, Not Data). All data will be gone
        //SQLiteManager.refreshDatabase();


        ArrayList<DriverModel> userModelsWithName = new SQLiteManager
                .Select("drivers")
                .where("id>? and name=?", "12", "name")
                .sort(SortOrder.ASC)
                .limit(5)
                .innerJoin("carId")
                .columns("cars.id", "drivers.fullname")
                .get();

//        Random, and first
//        ArrayList<UserModel> userModels = new SQLiteManager
//                .Select(UserModel.class)
//                .where("id>? and name=?", "12", "name")
//                .sort(SortOrder.ASC)
//                .limit(5)
//                .innerJoin("carId")
//                .columns("id", "email")
//                .get();
//

        int a = 123 + 32;
        Log.d(TAG, "${}" + a);

        // Get (Select) random data

        // Update in table

        //Write find method which finds item with id only

        // Delete data from DB with condition

        // Query Execute method

        // Select data from several tables with Foreign Key


    }
}
