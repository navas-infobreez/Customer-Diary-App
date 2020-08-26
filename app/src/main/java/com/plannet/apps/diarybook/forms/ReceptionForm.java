package com.plannet.apps.diarybook.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.databases.Customer;
import com.plannet.apps.diarybook.models.CustomerModel;
import java.util.ArrayList;
import java.util.List;

public class ReceptionForm extends AppCompatActivity {
    EditText name, place, address, email, phone, profession, purpose, noOfperson, qtyRequierd;
    Button add;
    Customer customerDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception_form);
        initUi();
        intDb();
    }

    private void intDb() {
        customerDb=new Customer(getApplicationContext());
    }

    private void initUi() {
        name = (EditText) findViewById(R.id.txt_name);
        place = (EditText) findViewById(R.id.txt_place);
        address = (EditText) findViewById(R.id.txt_address);
        email = (EditText) findViewById(R.id.txt_email);
        phone = (EditText) findViewById(R.id.txt_phone);
        profession = (EditText) findViewById(R.id.txt_profession);
        purpose = (EditText) findViewById(R.id.txt_purpose);
        noOfperson = (EditText) findViewById(R.id.txt_noAccoumpaining);
        qtyRequierd = (EditText) findViewById(R.id.no_qty_requerd);
        add = (Button) findViewById(R.id.add_custoner_button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void saveData() {
        if (name.getText().toString().isEmpty() || name.getText().toString().equals("")) {
            name.setError("please enter name");
        } else if (phone.getText().toString().isEmpty() || phone.getText().toString().equals("")) {
            phone.setError("please enter phone number");
        }else {
            getData();
        }
    }

    private void getData() {
        List<CustomerModel>customerModelList=new ArrayList<>();
        CustomerModel customerModel=new CustomerModel();
        customerModel.setCustomerName(name.getText().toString());
        customerModel.setAddress1(address.getText().toString());
        customerModel.setPhone_no(phone.getText().toString());
        customerModel.setEmail(email.getText().toString());
        customerModel.setCity(place.getText().toString());
        customerModelList.add(customerModel);
        customerDb.insertCustomers(customerModelList);
        cearData();

    }

    private void cearData() {
        name.getText().clear();
        profession.getText().clear();
        phone.getText().clear();
        address.getText().clear();
        email.getText().clear();
        place.getText().clear();
        noOfperson.getText().clear();
        qtyRequierd.getText().clear();
        purpose.getText().clear();
    }
}