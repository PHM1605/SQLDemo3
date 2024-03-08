package top.mintek.sqldemo3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  // references to buttons and other controls
  Button btn_add, btn_viewAll;
  EditText et_name, et_age;
  Switch sw_activeCustomer;
  ListView lv_customerList;
  ArrayAdapter customerArrayAdapter;
  DatabaseHelper databaseHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btn_add = findViewById(R.id.btn_add);
    btn_viewAll = findViewById(R.id.btn_viewAll);
    et_age = findViewById(R.id.et_age);
    et_name = findViewById(R.id.et_name);
    sw_activeCustomer = findViewById(R.id.sw_active);
    lv_customerList = findViewById(R.id.lv_customerList);

    databaseHelper = new DatabaseHelper(MainActivity.this);
    showCustomerOnListView();

    // button listeners for the add and view buttons
    btn_add.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v) {
        CustomerModel customerModel;
        try {
          customerModel = new CustomerModel(
              -1,
              et_name.getText().toString(),
              Integer.parseInt(et_age.getText().toString()),
              sw_activeCustomer.isChecked());
          Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
          Toast.makeText(MainActivity.this, "Error creating customer", Toast.LENGTH_SHORT).show();
          customerModel = new CustomerModel(-1, "error", 0, false);
        }

        boolean success = databaseHelper.addOne(customerModel);
        showCustomerOnListView();
//        Toast.makeText(MainActivity.this, "Success= " + success, Toast.LENGTH_SHORT).show();
      }
    });
    btn_viewAll.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showCustomerOnListView();
      }
    });

    lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CustomerModel clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
        databaseHelper.deleteOne(clickedCustomer);
        showCustomerOnListView();
      }
    });
  }

  private void showCustomerOnListView() {
    customerArrayAdapter = new ArrayAdapter<CustomerModel>(
        MainActivity.this,
        android.R.layout.simple_list_item_1,
        databaseHelper.getEveryone()
    );
    lv_customerList.setAdapter(customerArrayAdapter);
  }
}