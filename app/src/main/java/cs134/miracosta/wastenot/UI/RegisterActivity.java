package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.Location;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.utils.Animator;
import cs134.miracosta.wastenot.utils.Validator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    public static final String TAG = "WasteNot";

    //Reference to FirebaseAuth
    public FirebaseAuth firebaseAuth;
    //Reference to FirebaseDBHelper class
    private FirebaseDBHelper mDB;

    //References to all the views
    private Button btRegister;
    private RadioGroup rgUserType;
    private ConstraintLayout loader;
    private String selectedUserType = "";
    private EditText etFirstName, etLastName, etEmail, etCompanyName, etPassword, etConfirmPassword, eAddress, eCity, eState, eZipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDB = new FirebaseDBHelper();

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
        etCompanyName = findViewById(R.id.etCompanyName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        eAddress = findViewById(R.id.etAddress);
        eCity = findViewById(R.id.etCity);
        eState = findViewById(R.id.etState);
        eZipCode = findViewById(R.id.etZip);

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

    /**
     * Call the registerNewUser method after inputs validation
     */
    public void clickRegister() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String companyName = etCompanyName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String address = eAddress.getText().toString().trim();
        String city = eCity.getText().toString().trim();

        if (!Validator.validateText(selectedUserType))
            Toast.makeText(this, getString(R.string.user_type), Toast.LENGTH_SHORT).show();
        else if (!Validator.validateText(firstName)) {
            Toast.makeText(this, getString(R.string.user_first_name), Toast.LENGTH_SHORT).show();
            Animator.animate(etFirstName);
        } else if (!Validator.validateText(lastName)) {
            Toast.makeText(this, getString(R.string.user_last_name), Toast.LENGTH_SHORT).show();
            Animator.animate(etLastName);
        } else if (!Validator.validateText(email)) {
            Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
            Animator.animate(etEmail);
        } else if (!Validator.validateEmail(email)) {
            Toast.makeText(this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
            Animator.animate(etEmail);
        } else if (!Validator.validateText(password)) {
            Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
            Animator.animate(etPassword);
        } else if (!Validator.validateText(confirmPassword)) {
            Toast.makeText(this, getString(R.string.confirm_password), Toast.LENGTH_SHORT).show();
            Animator.animate(etConfirmPassword);
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, getString(R.string.confirm_password_no_match), Toast.LENGTH_SHORT).show();
            Animator.animate(etConfirmPassword);
        } else if (!Validator.validateText(address)) {
        Toast.makeText(this, getString(R.string.enter_valid_address), Toast.LENGTH_SHORT).show();
        Animator.animate(eAddress);
        } else if (!Validator.validateText(city)) {
        Toast.makeText(this, getString(R.string.enter_valid_city), Toast.LENGTH_SHORT).show();
        Animator.animate(eCity);
        } else if (!password.equals(confirmPassword)) {
        Toast.makeText(this, getString(R.string.confirm_password_no_match), Toast.LENGTH_SHORT).show();
        Animator.animate(etConfirmPassword);
        }
        else {
            User user = new User(selectedUserType, firstName, lastName, email, companyName, new Location(this, address, city, eState.getText().toString().trim(), eZipCode.getText().toString().trim()));
            // Passing password privately so it doesn't get added to User database
            registerNewUser(user, password);
        }
    }

    /**
     * Register the user by calling Firebase method 'createUserWithEmailAndPassword'
     * and onSuccess, add the user to Firebase Cloud Firestore and call the 'redirectUserToDashBoard' method onSuccess
     *
     * @param user user to register
     */
    private void registerNewUser(final User user, String password)
    {
        loader.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mDB.addUser(user, new FirebaseDBHelper.DataStatus() {
                                @Override
                                public void DataIsRead(List<?> items)
                                {
                                }

                                @Override
                                public void DataIsProcessed()
                                {
                                    loader.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, getString(R.string.user_registered_msg),
                                            Toast.LENGTH_SHORT).show();
                                    redirectUserToDashBoard(user.getUserType());
                                }

                                @Override
                                public void onError(String errorMessage)
                                {
                                    loader.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, errorMessage,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            loader.setVisibility(View.GONE);
                            // If sign in fails, display a message to the firebaseUser.
                            if (task.getException() != null) {
                                Toast.makeText(RegisterActivity.this,
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        getString(R.string.error_authentication), Toast.LENGTH_SHORT).show();
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
        } else if (userType.equals("donor")) {
            startActivity(new Intent(RegisterActivity.this, DonationListActivity.class));
            finishAffinity();
        } else if (userType.equals("driver")) {
            startActivity(new Intent(RegisterActivity.this, DeliveryActivity.class));
            finishAffinity();
        } else {
            Toast.makeText(RegisterActivity.this, getString(R.string.error_invalid_user), Toast.LENGTH_SHORT).show();
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
