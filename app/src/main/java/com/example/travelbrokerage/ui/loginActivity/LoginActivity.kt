package com.example.travelbrokerage.ui.loginActivity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.example.travelbrokerage.R
import com.example.travelbrokerage.ui.homePage.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {

    private lateinit var etMail : EditText
    private lateinit var etPassword : EditText
    private lateinit var btnLogin : MaterialButton
    private lateinit var btnCreateAccount : MaterialButton
    private lateinit var awesomeValidation: AwesomeValidation
    private lateinit var warningMassage : TextView

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etMail = findViewById(R.id.et_mail)
        etPassword = findViewById(R.id.et_password)
        warningMassage = findViewById(R.id.tv_massage)

        mAuth = FirebaseAuth.getInstance()
        setValidation()
    }

    fun loginOnclick(view: View) {
        if (awesomeValidation.validate()){
            warningMassage.text = ""
        }
    }

    fun createAccountOnClick(view: View) {
        if (awesomeValidation.validate()){
            warningMassage.text = ""
            val email = etMail.text.toString()
            val password = etPassword.text.toString()

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener(this) { tsk ->
                                if (tsk.isSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "אנא אמת את החשבון על ידי המייל שנשלח אליך",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    if (user.isEmailVerified){
                                        val i = Intent(this, MainActivity::class.java)
                                        startActivity(i)
                                    }
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "שליחת האימות נכשלה. אנא נסה שנית מאוחר יותר",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
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
}