package com.muhrizqullah.listcontact;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MutateContact extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEt, phoneEt;
    private Button fab;
    private String id, name, phone;
    private ActionBar actionBar;
    private ContactRepository contactRepository;
    Boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutate_contact);

        this.contactRepository = new ContactRepository(this);

        this.actionBar = getSupportActionBar();
        this.actionBar.setTitle("Add Contact");
        // back button
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setDisplayShowHomeEnabled(true);

        this.nameEt = (EditText) findViewById(R.id.nameEt);
        this.phoneEt = (EditText) findViewById(R.id.phoneEt);
        this.fab = (Button) findViewById(R.id.fab);
        this.fab.setOnClickListener(this);

        Intent intent = getIntent();
        this.isEdit = intent.getBooleanExtra("isEdit", false);

        if(this.isEdit) {
            this.actionBar.setTitle("Update Contact");
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            phone = intent.getStringExtra("phone");

            nameEt.setText(name);
            phoneEt.setText(phone);
        }
    }

    @Override
    public void onClick(View view) {
        if(this.isEdit) {
            updateData();
        } else {
            saveData();
        }
    }

    private void updateData() {
        this.name = this.nameEt.getText().toString();
        this.phone = this.phoneEt.getText().toString();

        if(this.name.isEmpty() || this.phone.isEmpty()) {
            Toast.makeText(this, "Please fill all data", Toast.LENGTH_SHORT).show();
            return;
        }

        contactRepository.update(this.id, this.name, this.phone);

        Toast.makeText(getApplicationContext(), "Successfully updated the contact", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void saveData() {
        this.name = this.nameEt.getText().toString();
        this.phone = this.phoneEt.getText().toString();

        if(this.name.isEmpty() || this.phone.isEmpty()) {
            Toast.makeText(this, "Please fill all data", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = contactRepository.insert(new Contact("", this.name, this.phone));

        Toast.makeText(getApplicationContext(), "Successfully added new contact", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // back button handler
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}