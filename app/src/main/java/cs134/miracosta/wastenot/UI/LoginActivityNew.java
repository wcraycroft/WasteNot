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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs134.miracosta.wastenot.Model.User;
import cs134.miracosta.wastenot.R;
import cs134.miracosta.wastenot.utils.Animator;
import cs134.miracosta.wastenot.utils.Validator;

public class LoginActivityNew extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference ref;
    public FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    private User user;
    private Button btLogin;
    private ConstraintLayout loader;
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        initialize();
        implementListeners();
    }

    /**
     * method to initialize all the variables
     */
    private void initialize() {
        loader = findViewById(R.id.loader);
        btLogin = findViewById(R.id.btLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child("users");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * method to implement listeners
     */
    private void implementListeners() {
        loader.setOnClickListener(this);
        btLogin.setOnClickListener(this);
    }

    public void clickLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!Validator.validateText(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            Animator.animate(etEmail);
        } else if (!Validator.validateEmail(email)) {
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            Animator.animate(etEmail);
        } else if (!Validator.validateText(password)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            Animator.animate(etPassword);
        } else {
            loginUser(email, password);
        }
    }

    /**
     * method to login the user
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
                                ref.orderByChild("email")
                                        .equalTo(firebaseUser.getEmail())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChildren()) {
                                                    for (DataSnapshot dataChild : dataSnapshot.getChildren()) {
                                                        user = dataChild.getValue(User.class);
                                                    }
                                                    loader.setVisibility(View.GONE);
                                                    Toast.makeText(LoginActivityNew.this, "Welcome " +
                                                            firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                                                    if (user != null && user.getUserType() != null)
                                                        redirectUserToDashBoard(user.getUserType());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                loader.setVisibility(View.GONE);
                                                Toast.makeText(LoginActivityNew.this, databaseError.getMessage()
                                                        , Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            loader.setVisibility(View.GONE);
                            Toast.makeText(LoginActivityNew.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(LoginActivityNew.this, ClaimsListActivity.class));
            finishAffinity();
        } else {
            Toast.makeText(LoginActivityNew.this,
                    "Driver and Donor are not supported yet", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btLogin:
                clickLogin();
                break;
        }
    }
}
