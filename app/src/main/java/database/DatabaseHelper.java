package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    // SQLite database and table names are not case sensitive
    public static final String DATABASE_NAME = "BeanAndLeaf.db";

    private SQLiteDatabase db;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users(" +
                "UserID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Username TEXT NOT NULL," +
                "Password TEXT NOT NULL," +
                "Email TEXT NOT NULL," +
                "UserType TEXT NOT NULL," +
                "Gender TEXT," +
                "PicURL TEXT)");
        db.execSQL("CREATE TABLE Stores(" +
                "StoreID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserID INTEGER NOT NULL," +
                "StoreLat REAL NOT NULL," +
                "StoreLong REAL NOT NULL," +
                "StoreName TEXT NOT NULL," +
                "FOREIGN KEY (UserID) REFERENCES Users(UserID))");
        db.execSQL("CREATE TABLE MenuItems(" +
                "MenuItemID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "StoreID INTEGER NOT NULL," +
                "Price REAL NOT NULL," +
                "ItemName TEXT NOT NULL," +
                "FOREIGN KEY (StoreID) REFERENCES Stores(StoreID))");
        db.execSQL("CREATE TABLE Orders(" +
                "OrderID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserID INTEGER NOT NULL," +
                "MenuItemID INTEGER NOT NULL," +
                "StoreID INTEGER NOT NULL," +
                "Quantity INTEGER NOT NULL," +
                "OrderTime TEXT NOT NULL," +
                "FOREIGN KEY (UserID) REFERENCES Users(UserID)," +
                "FOREIGN KEY (MenuItemID) REFERENCES MenuItems(MenuItemID)," +
                "FOREIGN KEY (StoreID) REFERENCES Stores(StoreID))");
        db.execSQL("CREATE TABLE Trips(" +
                "TripID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserID INTEGER NOT NULL," +
                "StartLoc TEXT NOT NULL," +
                "EndLoc TEXT NOT NULL," +
                "TripDuration INTEGER NOT NULL," +
                "FOREIGN KEY (UserID) REFERENCES Users(UserID))");
        db.execSQL("CREATE TABLE TripOrders(" +
                "TripID INTEGER NOT NULL," +
                "OrderID INTEGER NOT NULL," +
                "FOREIGN KEY (TripID) REFERENCES Trips(TripID)," +
                "FOREIGN KEY (OrderID) REFERENCES Orders(OrderID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Stores");
        db.execSQL("DROP TABLE IF EXISTS MenuItems");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS Trips");
        db.execSQL("DROP TABLE IF EXISTS TripOrders");
        onCreate(db);
    }

    public boolean insertUser(String name, String email, String password, String userType, String gender) {
        ContentValues cv = new ContentValues();
        cv.put("Username", name);
        cv.put("Password", password);
        cv.put("Email", email);
        cv.put("UserType", userType);
        cv.put("Gender", gender);
        long result = db.insert("Users", null, cv);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public String verifyUser(String email, String password, String userType) {
        String whereClause = "SELECT Username, Password FROM Users WHERE Email=? AND UserType=?";
        String whereArgs[] = {email, userType};

        Cursor res = db.rawQuery(whereClause, whereArgs);
        if (res.moveToNext()) {
            if (res.getString(1).contentEquals(password)) {
                return res.getString(0); // valid login, return username
            }
            else {
                return "INVALID"; // wrong password
            }
        }
        else {
            return "NULL"; // no user found
        }
    }

    public int getUserId(String email, String userType) {
        String whereClause = "SELECT UserID FROM Users WHERE Email=? AND UserType=?";
        String whereArgs[] = {email, userType};

        Cursor res = db.rawQuery(whereClause, whereArgs);
        if (res.moveToNext()) {
            return res.getInt(0);
        }
        return -1;
    }

    public String getUserGender(String email, String userType) {
        String whereClause = "SELECT Gender FROM Users WHERE Email=? AND UserType=?";
        String whereArgs[] = {email, userType};

        Cursor res = db.rawQuery(whereClause, whereArgs);
        if (res.moveToNext()) {
            return res.getString(0);
        }
        return null;
    }

    public String getUserName(String email, String userType) {
        String whereClause = "SELECT Username FROM Users WHERE Email=? AND UserType=?";
        String whereArgs[] = {email, userType};

        Cursor res = db.rawQuery(whereClause, whereArgs);
        if (res.moveToNext()) {
            return res.getString(0);
        }
        return null;
    }

    public String getUserPassword(String email, String userType) {
        String whereClause = "SELECT Password FROM Users WHERE Email=? AND UserType=?";
        String whereArgs[] = {email, userType};

        Cursor res = db.rawQuery(whereClause, whereArgs);
        if (res.moveToNext()) {
            return res.getString(0);
        }
        return null;
    }

    public boolean updateUserName(String email, String userType, String newName) {
        ContentValues cv = new ContentValues();
        cv.put("Username", newName);
        String whereClause = "Email=? AND UserType=?";
        String whereArgs[] = {email, userType};
        long result = db.update("Users", cv, whereClause, whereArgs);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateUserEmail(String email, String userType, String newEmail) {
        ContentValues cv = new ContentValues();
        cv.put("Email", newEmail);
        String whereClause = "Email=? AND UserType=?";
        String whereArgs[] = {email, userType};
        long result = db.update("Users", cv, whereClause, whereArgs);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updateUserGender(String email, String userType, String newGender) {
        ContentValues cv = new ContentValues();
        cv.put("Gender", newGender);
        String whereClause = "Email=? AND UserType=?";
        String whereArgs[] = {email, userType};
        long result = db.update("Users", cv, whereClause, whereArgs);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean updatePic(String name, String email, String password, String userType,
                              String gender, String picURL) {
        ContentValues cv = new ContentValues();
        cv.put("Username", name);
        cv.put("Password", password);
        cv.put("Email", email);
        cv.put("UserType", userType);
        cv.put("Gender", gender);
        cv.put("PicURL", picURL);
        String whereClause = "PicURL=?";
        String whereArgs[] = {picURL};
        long result = db.update("Users", cv, whereClause, whereArgs);
        if (result > 1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean insertStore(Integer userId, Float lat, Float lon, String storeName) {
        ContentValues cv = new ContentValues();
        cv.put("UserID", userId);
        cv.put("StoreLat", lat);
        cv.put("StoreLong", lon);
        cv.put("StoreName", storeName);
        long result = db.insert("Stores", null, cv);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public ArrayList<String> getStores(Integer userId) {
        ArrayList<String> stores = new ArrayList<>();
        String whereClause = "SELECT StoreName FROM Stores WHERE UserID=?";
        String whereArgs[] = {Integer.toString(userId)};

        Cursor res = db.rawQuery(whereClause, whereArgs);
        while (res.moveToNext()) {
            stores.add(res.getString(0));
        }
        return stores;
    }

    public boolean removeStore(Integer userId, String storeLoc, String storeName) {
        String whereClause = "UserID=? AND StoreLoc=? AND StoreName=?";
        String whereArgs[] = {userId.toString(), storeLoc, storeName};
        long result = db.delete("Stores", whereClause, whereArgs);
        if (result > 0){
            return true;
        }
        else{
            return false;
        }

    }
    public boolean insertMenuItem(Integer storeId, String price, String name) {
        ContentValues cv = new ContentValues();
        cv.put("StoreID", storeId);
        cv.put("Price", price);
        cv.put("ItemName", name);
        long result = db.insert("MenuItems", null, cv);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }
    public boolean removeMenuItem(Integer storeId, String price, String name) {
        String whereClause = "storeId=? AND Price=? AND ItemName=?";
        String whereArgs[] = {storeId.toString(), price, name};
        long result = db.delete("MenuItems", whereClause, whereArgs);
        if (result > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean insertOrder(Integer userId, Integer menuItemId, Integer storeId, Integer quantity, String time) {
        ContentValues cv = new ContentValues();
        cv.put("UserID", userId);
        cv.put("MenuItemID", menuItemId);
        cv.put("StoreID", storeId);
        cv.put("Quantity", quantity);
        cv.put("OrderTime", time);
        long result = db.insert("Orders", null, cv);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean insertTrip(Integer userId, String startLoc, String endLoc, Integer tripDuration) {
        ContentValues cv = new ContentValues();
        cv.put("UserID", userId);
        cv.put("StartLoc", startLoc);
        cv.put("EndLoc", endLoc);
        cv.put("TripDuration", tripDuration);
        long result = db.insert("Trips", null, cv);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean insertTripOrder(Integer tripId, Integer orderId) {
        ContentValues cv = new ContentValues();
        cv.put("TripID", tripId);
        cv.put("OrderID", orderId);
        long result = db.insert("TripOrders", null, cv);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }
}
