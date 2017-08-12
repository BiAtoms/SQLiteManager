package com.demoapp;

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

        //Inserting it to the table
        long feedback_1 = userModel.insert();

        //Inserting again. This will create new User in db.
        long feedback_1_1 = userModel.insert();

        //Creating model
        DriverModel driver = new DriverModel();
        driver.birtdate = "12-01-12";
        driver.fullname = "Some Fullname here";
        driver.email = "Some Email here";

        //Creating foreign key model
        CarModel carModelq = new CarModel();
        carModelq.id = 1;

        //Assigning it to main model
        driver.carModel = carModelq;

        //Inserting to the table
        long feedback_2 = driver.insert();


        driver.birtdate = "12-01-12";
        driver.fullname = "Soeme Fullname here";
        driver.email = "Somee Email here";

        carModelq.id = 2;

        //Assigning it to main model
        driver.carModel = carModelq;
        driver.insert();
        driver.birtdate = "12-01-12";
        driver.fullname = "Soeme Fulleename here";
        driver.email = "Somee Email herewe";

        carModelq.id = 3;

        //Assigning it to main model
        driver.carModel = carModelq;
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




        //This will assign the values from db data with id = 1
        //to myCarModel object.
        CarModel myCarModel = new CarModel();
        myCarModel.fill(1); // myCarModel contains data of columns where id = 1

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


        ArrayList<DriverModel> userModelsWithName = new SQLiteManager
                .Select("drivers")
                .where("drivers.id>?", "1")
                .sort("cars.id", SortOrder.ASC)
                .limit(5)
                .innerJoin("carId")
                .columns("cars.id", "drivers.fullname", "cars.release_date", "drivers.id")
                .get();

//        Random, and first
//        ArrayList<UserModel> userModels = new SQLiteManager
//                .Select(UserModel.class)
//                .where("id>? and name=?", "12", "name")
//                .sort(SortOrder.ASC)
//                .limit(5)
//                .innerJoin("carId")
//                .columns("id", "email")
//                .fill();

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
