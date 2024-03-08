package top.mintek.sqldemo3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

  public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
  public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
  public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
  public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
  public static final String COLUMN_ID = "ID";

  public DatabaseHelper(@Nullable Context context) {
    super(context, "customer.db", null, 1);
  }

  // this is called the first time a db is accessed. Code to create db
  @Override
  public void onCreate(SQLiteDatabase db) {
    String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";
    db.execSQL(createTableStatement);
  }

  // this is called if the db version number changes (design changes)
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public boolean addOne(CustomerModel customerModel) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues cv = new ContentValues();
    cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
    cv.put(COLUMN_CUSTOMER_AGE, customerModel.getAge());
    cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());

    long insert = db.insert(CUSTOMER_TABLE, null, cv);
    if (insert == -1)
      return false;
    return true;
  }

  public List<CustomerModel> getEveryone() {
    List<CustomerModel> returnList = new ArrayList<>();
    String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(queryString, null);
    if (cursor.moveToFirst()) {
      // loop through the cursor (result set) -> put them to returned list
      do {
        int customerID = cursor.getInt(0);
        String customerName = cursor.getString(1);
        int customerAge = cursor.getInt(2);
        boolean customerActive = cursor.getInt(3) == 1;
        CustomerModel newCustomer = new CustomerModel(customerID, customerName, customerAge, customerActive);
        returnList.add(newCustomer);
      } while (cursor.moveToNext());
    } else {
      // failure. do not add anything to the list
    }
    cursor.close();
    db.close();
    return returnList;
  }

  public boolean deleteOne(CustomerModel customerModel) {
    SQLiteDatabase db = this.getWritableDatabase();
    String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " + customerModel.getId();
    Cursor cursor = db.rawQuery(queryString, null);
    if (cursor.moveToFirst()) {
      return true;
    } else {
      return false;
    }
  }

}
