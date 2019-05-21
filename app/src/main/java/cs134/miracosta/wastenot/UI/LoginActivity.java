package cs134.miracosta.wastenot.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import cs134.miracosta.wastenot.Model.FirebaseDBHelper;
import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.utils.Animator;
import cs134.miracosta.wastenot.utils.Validator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "WasteNot";

    //Reference to FirebaseAuth
    public FirebaseAuth firebaseAuth;
    //Reference to FirebaseDBHelper class
    private FirebaseDBHelper db;

    //References to User model class
    private User user;

    //References to all the views
    private Button btLogin;
    private Button btRegister;
    private ConstraintLayout loader;
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
        implementListeners();
    }

    /**
     * method to initialize all the variables
     */
    private void initialize() {
        loader = findViewById(R.id.loader);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        db = new FirebaseDBHelper();

    }

    /**
     * method to implement listeners
     */
    private void implementListeners() {
        loader.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);

    }

    /**
     * Call the loginUser method after inputs validation
     */
    public void clickLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!Validator.validateText(email)) {
            Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
            Animator.animate(etEmail);
        } else if (!Validator.validateEmail(email)) {
            Toast.makeText(this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
            Animator.animate(etEmail);
        } else if (!Validator.validateText(password)) {
            Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
            Animator.animate(etPassword);
        } else {
            loginUser(email, password);
        }
    }

    /**
     * method to login the user by calling Firebase method 'signInWithEmailAndPassword'
     * and onSuccess, get the user by email and call the 'redirectUserToDashBoard' method
     *
     * @param email    email
     * @param password password
     */
    private void loginUser(String email, String password) {
        loader.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                db.getUserByEmail(firebaseUser.getEmail(), new FirebaseDBHelper.DataStatus() {
                                    @Override
                                    public void DataIsRead(List<?> items) {
                                        user = (User) items.get(0);
                                        loader.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, getString(R.string.welcome) +
                                                user.getFirstName(), Toast.LENGTH_LONG).show();
                                        if (user != null && user.getUserType() != null)
                                            redirectUserToDashBoard(user.getUserType());
                                    }

                                    @Override
                                    public void DataIsProcessed() { }

                                    @Override
                                    public void onError(String errorMessage) {
                                        loader.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, errorMessage
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            loader.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, getString(R.string.error_invalid_email_or_password), Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(LoginActivity.this, ClaimsListActivity.class));
            finishAffinity();
        } else if (userType.equals("donor")) {
            startActivity(new Intent(LoginActivity.this, DonationListActivity.class));
            finishAffinity();
        } else if (userType.equals("driver")) {
            startActivity(new Intent(LoginActivity.this, DeliveryActivity.class));
            finishAffinity();
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.error_invalid_user), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btLogin:
                clickLogin();
                break;
            case R.id.btRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finishAffinity();
                break;
        }
    }
}
