package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.model.User;
import cs134.miracosta.wastenot.utils.Animator;
import cs134.miracosta.wastenot.utils.Validator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private DatabaseReference ref;
    public FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    private Button btRegister;
    private RadioGroup rgUserType;
    private ConstraintLayout loader;
    private String selectedUserType = "";
    private EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
        implementListeners();
    }

    /**
     * method to initialize all the variables
     */
    private void initialize() {
        loader = findViewById(R.id.loader);
        btRegister = findViewById(R.id.btRegister);
        rgUserType = findViewById(R.id.rgUserType);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child("users");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * method to implement listeners
     */
    private void implementListeners() {
        loader.setOnClickListener(this);
        btRegister.setOnClickListener(this);

        rgUserType.setOnCheckedChangeListener(this);
    }

    public void clickRegister() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!Validator.validateText(selectedUserType))
            Toast.makeText(this, "Select User Type", Toast.LENGTH_SHORT).show();
        else if (!Validator.validateText(firstName)) {
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show();
            Animator.animate(etFirstName);
        } else if (!Validator.validateText(lastName)) {
            Toast.makeText(this, "Enter Last Name", Toast.LENGTH_SHORT).show();
            Animator.animate(etLastName);
        } else if (!Validator.validateText(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            Animator.animate(etEmail);
        } else if (!Validator.validateEmail(email)) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            Animator.animate(etEmail);
        } else if (!Validator.validateText(password)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            Animator.animate(etPassword);
        } else if (!Validator.validateText(confirmPassword)) {
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            Animator.animate(etConfirmPassword);
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Confirm password does not match", Toast.LENGTH_SHORT).show();
            Animator.animate(etConfirmPassword);
        } else {
            User user = new User(selectedUserType, firstName, lastName, email, password);
            registerNewUser(user);
        }
    }

    /**
     * method to register a new user
     *
     * @param user user to register
     */
    private void registerNewUser(final User user) {
        loader.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference pushed = ref.push();
                            String key = pushed.getKey();
                            user.setNodeName(key);
                            pushed.setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            loader.setVisibility(View.GONE);
                                            Toast.makeText(RegisterActivity.this, "User Registered successfully",
                                                    Toast.LENGTH_SHORT).show();
                                            redirectUserToDashBoard(user.getUserType());

                                        }
                                    });
                        } else {
                            loader.setVisibility(View.GONE);
                            // If sign in fails, display a message to the firebaseUser.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            if (task.getException() != null) {
                                Toast.makeText(RegisterActivity.this,
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    /**
     * method to redirect the user to specific activity
     *
     * @param userType selected user type
     */
    private void redirectUserToDashBoard(String userType) {
        if (userType.equals("claimer")) {
            startActivity(new Intent(RegisterActivity.this, ClaimsListActivity.class));
            finishAffinity();
        } else {
            Toast.makeText(RegisterActivity.this,
                    "Driver and Donor are not supported yet", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btRegister:
                clickRegister();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rbClaimer)
            selectedUserType = "claimer";
        else if (checkedId == R.id.rbDonor)
            selectedUserType = "donor";
        else if (checkedId == R.id.rbDriver)
            selectedUserType = "driver";
    }
}
