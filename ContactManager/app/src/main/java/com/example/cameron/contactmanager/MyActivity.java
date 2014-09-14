package com.example.cameron.contactmanager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends ActionBarActivity {

    EditText txtname, txtphone, txtemail, txtaddresss;

    //creates a list of contacts of class contact from the contact.java class file
    List<Contact> Contacts = new ArrayList<Contact>();

    ListView contactlist_view;
    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        txtname = (EditText) findViewById(R.id.txtname);
        txtphone = (EditText) findViewById(R.id.txtphone);
        txtemail = (EditText) findViewById(R.id.txtemail);
        txtaddresss = (EditText) findViewById(R.id.txtaddress);
        contactlist_view = (ListView) findViewById(R.id.listView);
        dbHandler = new DatabaseHandler(getApplicationContext());

        //Setting up the tabs at the top of the screen
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        //set up the creator tab with Indicator "Creator"
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.tab_create);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        //set up the list tab with Indicator "List"
        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tab_list);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        //set up the button
        final Button addbtn = (Button) findViewById(R.id.add_button);
        //when the button is clicked it displays the message "Your contact has been created!"
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact(dbHandler.getContactsCount(), String.valueOf(txtname.getText().toString()), String.valueOf(txtphone.getText().toString()), String.valueOf(txtemail.getText().toString()), String.valueOf(txtaddresss.getText().toString()));
                dbHandler.createContact(contact);
                Contacts.add(contact);
                populate_list();
                Toast.makeText(getApplicationContext(), txtname.getText().toString() + " has been added to your contacts!", Toast.LENGTH_SHORT).show();
            }
        });



        txtname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                addbtn.setEnabled(!txtname.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        List<Contact> addableContacts = dbHandler.getAllContacts();
        int contactCount = dbHandler.getContactsCount();

        for (int i = 0; i<contactCount; i++) {
            Contacts.add(addableContacts.get(i));
        }
        if (!addableContacts.isEmpty())
            populate_list();
    }

    private void populate_list() {
        ArrayAdapter<Contact> adapter = new ContactListAdapter();
        contactlist_view.setAdapter(adapter);
    }

    private void add_contact(String name, String phone, String email, String address) {
        Contacts.add(new Contact(0, name, phone, email, address));
    }

    private class ContactListAdapter extends ArrayAdapter<Contact> {
        public ContactListAdapter() {
            super (MyActivity.this, R.layout.listview_item, Contacts);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Contact current_contact = Contacts.get(position);

            TextView name = (TextView) view.findViewById(R.id.contact_name);
            name.setText(current_contact.getName());
            TextView phone = (TextView) view.findViewById(R.id.contact_phone);
            phone.setText(current_contact.getPhone());
            TextView email = (TextView) view.findViewById(R.id.contact_email);
            email.setText(current_contact.getEmail());
            TextView address = (TextView) view.findViewById(R.id.contact_address);
            address.setText(current_contact.getAddress());

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
