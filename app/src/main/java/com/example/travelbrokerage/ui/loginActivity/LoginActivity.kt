package com.example.travelbrokerage.ui.loginActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.example.travelbrokerage.R
import com.example.travelbrokerage.ui.homePage.MainActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private lateinit var etMail: EditText
    private lateinit var etPassword: EditText
   // private lateinit var btnLogin: MaterialButton
  //  private lateinit var btnCreateAccount: MaterialButton
    private lateinit var awesomeValidation: AwesomeValidation
    private lateinit var warningMassage: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etMail = findViewById(R.id.et_mail)
        etPassword = findViewById(R.id.et_password)
        warningMassage = findViewById(R.id.tv_massage)
        progressBar = findViewById(R.id.progressBar)

        mAuth = FirebaseAuth.getInstance()
        setValidation()
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)
        updateFields()
    }

    @SuppressLint("CommitPrefEdits")
    fun loginOnclick(view: View) {
        if (awesomeValidation.validate()) {
            warningMassage.text = ""
            progressBar.visibility = VISIBLE
            val email = etMail.text.toString()
            val password = etPassword.text.toString()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        progressBar.visibility = INVISIBLE
                        val editor = sharedPreferences.edit()
                        editor.putString("Mail", email)
                        editor.putString("Password", password)
                        editor.apply()
                        val user = mAuth.currentUser
                        if (user?.isEmailVerified == true) {
                            val i = Intent(this, MainActivity::class.java)
                            startActivity(i)
                        } else {
                            warningMassage.text = "אנא אמת את החשבון על ידי המייל שנשלח אליך"
                        }
                    } else {
                        progressBar.visibility = INVISIBLE
                        warningMassage.text = "כתובת מייל או סיסמה אינם נכונים"
                    }
                }
        }
    }

    fun createAccountOnClick(view: View) {
        if (awesomeValidation.validate()) {
            warningMassage.text = ""
            progressBar.visibility = VISIBLE
            val email = etMail.text.toString()
            val password = etPassword.text.toString()

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener(this) { tsk ->
                                if (tsk.isSuccessful) {
                                    progressBar.visibility = INVISIBLE
                                    Toast.makeText(
                                        applicationContext,
                                        "אנא אמת את החשבון על ידי המייל שנשלח אליך",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    progressBar.visibility = INVISIBLE
                                    Toast.makeText(
                                        applicationContext,
                                        "שליחת האימות נכשלה. אנא נסה שנית מאוחר יותר",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        progressBar.visibility = INVISIBLE
                        warningMassage.text = "החשבון כבר קיים במערכת"
                    }
                }
        }
    }

    // Initializes view components in appropriate tests
    // note : uses in AwesomeValidation library
    private fun setValidation() {
        awesomeValidation = AwesomeValidation(ValidationStyle.BASIC)

        awesomeValidation.addValidation(
            this, R.id.et_mail,
            Patterns.EMAIL_ADDRESS, R.string.invalid_email
        )

        awesomeValidation.addValidation(
            this, R.id.et_password,
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", R.string.invalid_password
        )
    }

    private fun updateFields() {
        etMail.setText(sharedPreferences.getString("Mail", ""))
        etPassword.setText(sharedPreferences.getString("Password", ""))
    }
}