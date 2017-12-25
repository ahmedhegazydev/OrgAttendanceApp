package com.example.ahmed.orgattendanceapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.example.ahmed.orgattendanceapp.activities.EmployeeActivity;
import com.example.ahmed.orgattendanceapp.activities.EmployerActivity;
import com.example.ahmed.orgattendanceapp.common.Common;
import com.example.ahmed.orgattendanceapp.model.UserEmployer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irozon.sneaker.Sneaker;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    public final static String REF_USERS_ROOT = "OrgAttendanceUsers",
            REF_USERS_EMPLOYEES = "REF_USERS_EMPLOYEES", REF_USERS_EMPLOYERS = "REF_USERS_EMPLOYERS";
    @BindView(R.id.btnShowEmployer)
    Button btnShowEmployer;
    @BindView(R.id.btnShowEmployee)
    Button btnShowEmployee;
    @BindView(R.id.viewSwitcher)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.llSub)
    LinearLayout llSub;
    @BindView(R.id.tilEmail)
    TextInputLayout textInputLayout;
    Context context = null;
    Common common = null;
    boolean booleanDontClickMore1 = true, booleanDontClickMore2 = true;
    @BindView(R.id.fabAddNewEmail)
    FloatingActionButton fabAddNewEmail;
    @BindView(R.id.llMain)
    LinearLayout llMainForAddingEmails;
    EditText etBase = null;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().trim().length() != 0 && Common.isValidEmail(etBase.getText().toString())) {
                fabAddNewEmail.show();
            } else {
                fabAddNewEmail.hide();
            }
        }
    };
    LayoutInflater layoutInflater = null;
    @BindView(R.id.viewSwitcherAccount)
    ViewSwitcher vsAccount;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnCreateAccount)
    Button btnCreateAccount;
    @BindView(R.id.etUserEmail)
    EditText etUserEmail;
    @BindView(R.id.etUserPass)
    EditText etUserPass;
    //    @BindAnim(R.anim.slide_in_right)
//    Animation animSlideIn;
//    @BindAnim(R.anim.slide_out_left)
//    Animation animSlideOut;
    @BindView(R.id.btnLoginNow)
    Button btnLoginNow;
    @BindView(R.id.etUserEmailLogin)
    EditText etEmailLogin;
    @BindView(R.id.etUserPassLogin)
    EditText etPassLogin;
    boolean dontRepeate1 = true, dontRepeate2 = true;
    SpotsDialog dialogRegister = null;
    List<EditText> listetEmails = new ArrayList<>();
    SpotsDialog dialogLogin = null;
    FirebaseUser firebaseUser = null;
    DatabaseReference refEmployees = null, refEmployers = null;
    @BindView(R.id.switchEmployee)
    SwitchButton switchEmployee;
    @BindView(R.id.switchEmployer)
    SwitchButton switchEmployer;
    Button.OnClickListener clickListenerShowEmployee = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (viewSwitcher.getDisplayedChild() == 0) {
                viewSwitcher.showNext();

                switchEmployer.setEnabled(true);
                switchEmployer.setChecked(false);
                switchEmployer.setEnabled(false);

                switchEmployee.setEnabled(true);
                switchEmployee.setChecked(true);
                switchEmployee.setEnabled(false);
                //llSub.setBackgroundColor(getResources().getColor(R.color.CadetBlue));
            }


        }
    };
    Button.OnClickListener clickListenerShowEmployer = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (viewSwitcher.getDisplayedChild() == 1) {
                viewSwitcher.showPrevious();
                switchEmployer.setEnabled(true);
                switchEmployer.setChecked(true);
                switchEmployer.setEnabled(false);

                switchEmployee.setEnabled(true);
                switchEmployee.setChecked(false);
                switchEmployee.setEnabled(false);
                //booleanDontClickMore1 = false;
                //booleanDontClickMore2 = true_answer;
                //llSub.setBackgroundColor(getResources().getColor(R.color.DarkGoldenrod));

            }


        }
    };
    String userType = "";


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRefs();
        init();
        initViewSwitcherAcccount();
        initLoginAccount();
        checkSessionForUser();

    }

    private void initRefs() {
        refEmployees = FirebaseDatabase.getInstance()
                .getReference(REF_USERS_ROOT)
                .child(REF_USERS_EMPLOYEES);
        refEmployers = FirebaseDatabase.getInstance()
                .getReference(REF_USERS_ROOT)
                .child(REF_USERS_EMPLOYERS);


    }

    private void checkSessionForUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            dialogLogin = new SpotsDialog(context, "....");
            dialogLogin.show();
            checkIfEmployeeOrEmployer(FirebaseAuth.getInstance().getCurrentUser());
            return;
        }
    }


    private void initLoginAccount() {

        btnLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {
        if (TextUtils.isEmpty(etEmailLogin.getText())) {
            Crouton.showText((Activity) context, "Enter email !!", Style.ALERT);
            return;
        }
        if (!Common.isValidEmail(etEmailLogin.getText())) {
            Crouton.showText((Activity) context, "Enter valid email !!", Style.ALERT);
            return;
        }
        if (TextUtils.isEmpty(etPassLogin.getText())) {
            Crouton.showText((Activity) context, "Enter password !!", Style.ALERT);
            return;
        }

        dialogLogin = new SpotsDialog(context, "Logging ...");
        dialogLogin.show();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmailLogin.getText().toString(),
                etPassLogin.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkIfEmployeeOrEmployer(task.getResult().getUser());
                        } else {
                            if (dialogLogin.isShowing()) dialogLogin.dismiss();
                            Sneaker.with((Activity) context).setTitle("Check email and password").sneakError();
                        }


                    }
                });


    }

    private void checkIfEmployeeOrEmployer(final FirebaseUser user) {
        refEmployers.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    if (dialogLogin.isShowing()) dialogLogin.dismiss();

                    Intent i = new Intent(getApplicationContext(), EmployerActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    refEmployees.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                                if (dialogLogin.isShowing()) dialogLogin.dismiss();

                                Intent i = new Intent(getApplicationContext(), EmployeeActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                error();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void error() {
//        if (viewSwitcher.getDisplayedChild() == 0) {
//            Sneaker.with((Activity) context).setTitle("You aren't registered as Employer").sneakWarning();
//        } else {
//            Sneaker.with((Activity) context).setTitle("You aren't registered as Employee").sneakWarning();
//        }
        if (vsAccount.getDisplayedChild() == 1) {
            vsAccount.setDisplayedChild(0);
        }
        Sneaker.with((Activity) context).setTitle("You aren't registered as user\n Please check email and password").sneakWarning();
        if (dialogLogin.isShowing()) dialogLogin.dismiss();

    }

    private void createAccount() {

        if (TextUtils.isEmpty(etUserEmail.getText().toString())) {
            Sneaker.with((Activity) context).setTitle("Enter email").sneakError();
            return;
        }
        if (!Common.isValidEmail(etUserEmail.getText().toString())) {
            Sneaker.with((Activity) context).setTitle("Enter valid email").sneakError();
            return;
        }
        if (TextUtils.isEmpty(etUserPass.getText().toString())) {
            Sneaker.with((Activity) context).setTitle("Enter password").sneakError();
            return;
        }

        if (etUserPass.getText().toString().length() < 7) {
            Sneaker.with((Activity) context).setTitle("Password too short !!!").sneakWarning();
            return;
        }

        boolean allEditTextsEmpty = true;
        //check if employer or employee
        if (viewSwitcher.getDisplayedChild() == 0) {//employer
            for (int i = 0; i < listetEmails.size(); i++) {
                if (!TextUtils.isEmpty(listetEmails.get(i).getText())) {
                    allEditTextsEmpty = false;
                    break;
                }
            }
            if (allEditTextsEmpty) {
                common.showAlertDialog("Set one employee's email as minimum to register as an Employer");
                return;
            } else {
                if (!checkValidEmails()) {
                    common.showAlertDialog("Enter valid emails for your employees");
                    return;

                } else {
                    new AlertDialog.Builder(context)
                            .setMessage("You will be registered as Employer")
                            .setPositiveButton("Ok, Register", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    userType = REF_USERS_EMPLOYERS;
                                    register();
                                }
                            })
                            .setCancelable(true)
                            .show();


                }

            }

        } else {//employee
            new AlertDialog.Builder(context)
                    .setMessage("You will be registered as Employee")
                    .setPositiveButton("Ok, Register", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userType = REF_USERS_EMPLOYEES;
                            register();
                        }
                    })
                    .setCancelable(true)
                    .show();

        }


    }


    private void register() {


        dialogRegister = new SpotsDialog(context, "Please wait ...");
        dialogRegister.show();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(etUserEmail.getText().toString(), etUserPass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword) {
                                // Log.d(TAG, "onComplete: weak_password");
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                ///Log.d(TAG, "onComplete: malformed_email");

                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                dialogRegister.dismiss();
                                Sneaker.with((Activity) context).setTitle("Email already exists").sneakError();
                                //Log.d(TAG, "onComplete: exist_email");
                            } catch (Exception e) {
                                //Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                        } else {
                            registeredSuccess(task);
                        }


                    }
                });


    }

    private void registeredSuccess(Task<AuthResult> task) {

        firebaseUser = task.getResult().getUser();

        //add user in firebase database
        FirebaseDatabase.getInstance().getReference(REF_USERS_ROOT)
                .child(userType)
                .child(firebaseUser.getUid())
                .setValue(
                        new UserEmployer(etUserEmail.getText().toString(),
                                etUserPass.getText().toString(), "", "", "")
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Sneaker.with((Activity) context).setTitle("Registered successfully").sneakSuccess();
                    if (viewSwitcher.getDisplayedChild() == 0) {
                        new AlertDialog.Builder(context)
                                .setPositiveButton("Ok, Next", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new
                                                Intent(getApplicationContext(), EmployerActivity.class));
                                        finish();
                                    }
                                })
                                .setMessage("You have been registered as employer")
                                .setIcon(R.drawable.true_answer)
                                .show();
                    } else {
                        new AlertDialog.Builder(context)
                                .setPositiveButton("Ok, Next", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new
                                                Intent(getApplicationContext(), EmployeeActivity.class));
                                        finish();
                                    }
                                })
                                .setMessage("You have been registered as employee")
                                .setIcon(R.drawable.true_answer)
                                .show();
                    }

                } else {
                    Sneaker.with((Activity) context).setTitle("Failed internet connection").sneakError();
                }
                if (dialogRegister.isShowing()) dialogRegister.dismiss();
            }
        });


    }

    private boolean checkValidEmails() {
        boolean allEmailsValid = true;
        for (int i = 0; i < listetEmails.size(); i++) {
            if (!Common.isValidEmail(listetEmails.get(i).getText().toString())) {
                allEmailsValid = false;
                break;
            }
        }

        return allEmailsValid;
    }

    private void initViewSwitcherAcccount() {

        vsAccount.setDisplayedChild(1);
        vsAccount.setInAnimation(common.createSlideInLeftAnim());
        vsAccount.setOutAnimation(common.createSlideOutRightAnim());


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vsAccount.getDisplayedChild() == 0) {
                    vsAccount.showPrevious();

                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vsAccount.getDisplayedChild() == 1) {
                    vsAccount.showNext();
                }
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    private void init() {
        context = this;
        ButterKnife.bind(this);
        //----------------------------
        btnShowEmployee.setOnClickListener(clickListenerShowEmployee);
        btnShowEmployer.setOnClickListener(clickListenerShowEmployer);
        //---------------------------
        common = new Common(context);

        viewSwitcher.setInAnimation(common.createSlideInLeftAnim());
        viewSwitcher.setOutAnimation(common.createSlideOutRightAnim());
        viewSwitcher.setDisplayedChild(0);

        etBase = textInputLayout.getEditText();
        assert etBase != null;
        etBase.addTextChangedListener(textWatcher);
        listetEmails.add(etBase);


        fabAddNewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewEmailView();
                fabAddNewEmail.hide();
            }
        });

        layoutInflater = (LayoutInflater) getApplicationContext()//instead of getApplicationContext() -> mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        switchEmployee.setEnabled(false);
        switchEmployer.setEnabled(false);

    }

    private void addNewEmailView() {
        //@SuppressLint("InflateParams") final View viewEmailItem = layoutInflater.inflate(R.layout.email_item, null);
        @SuppressLint("InflateParams") final View viewEmailItem = LayoutInflater.from(context)
                .inflate(R.layout.email_item, null, false);


        llMainForAddingEmails.addView(viewEmailItem);
        viewEmailItem.startAnimation(common.createFadeInAnimation());

        final EditText editText = viewEmailItem.findViewById(R.id.etEmail);

        viewEmailItem.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //llMainForAddingEmails.removeView(viewEmailItem);
                Animation animation = common.createFadeOutAnimation();
//                animation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {}
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        viewEmailItem.setVisibility(View.GONE);
//                    }
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {}
//                });
                viewEmailItem.startAnimation(animation);
                viewEmailItem.setVisibility(View.GONE);
                for (int i = 0; i < listetEmails.size(); i++) {
                    if (listetEmails.get(i) == editText) {
                        listetEmails.remove(editText);
                    }
                }
                Log.e("listEmails", listetEmails.size() + "");


            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() != 0 && Common.isValidEmail(editText.getText().toString())) {
                    fabAddNewEmail.show();
                } else {
                    fabAddNewEmail.hide();
                }
            }
        });

        if (!listetEmails.contains(editText)) {
            listetEmails.add(editText);
        } else {

        }
        Log.e("listEmails", listetEmails.size() + "");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSessionForUser();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
