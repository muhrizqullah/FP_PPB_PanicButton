package com.muhrizqullah.listcontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetail extends AppCompatActivity implements View.OnClickListener {

    private TextView nameTv, phoneTv;
    private String id, name, phone;
    private ContactRepository contactRepository;
    Button editContactBtn, deleteContactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        this.contactRepository = new ContactRepository(this);

        Intent intent = getIntent();
        this.id = intent.getStringExtra("id");
        this.name = intent.getStringExtra("name");
        this.phone = intent.getStringExtra("phone");

        this.nameTv = (TextView) findViewById(R.id.nameTv);
        this.phoneTv = (TextView) findViewById(R.id.phoneTv);

        this.editContactBtn = (Button) findViewById(R.id.editContactBtn);
        this.deleteContactBtn = (Button) findViewById(R.id.deleteContactBtn);

        this.editContactBtn.setOnClickListener(this);
        this.deleteContactBtn.setOnClickListener(this);

        loadDataById();
    }



    private void loadDataById() {
        Contact contact = contactRepository.getById(id);
        this.nameTv.setText(contact.getName());
        this.phoneTv.setText(contact.getPhone());
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.editContactBtn:
                intent = new Intent(ContactDetail.this, MutateContact.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("isEdit", true);
                startActivity(intent);
                break;
            case R.id.deleteContactBtn:
                this.contactRepository.delete(this.id);
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(this, "Successfully deleted the contact", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}