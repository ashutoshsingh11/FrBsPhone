package com.example.frbsphone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private  lateinit var mAuth : FirebaseAuth

    private  lateinit var  btnGetOtp :Button
//    private  lateinit var  verifyOtp :Button
//    private  lateinit var  etOtp :EditText
    private  lateinit var  etNum :EditText
      var verifiationId =""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGetOtp = findViewById(R.id.btnGetOtp)
        val btnVerifyOtp = findViewById<Button>(R.id.btnVerifyOtp)
        val etOtp = findViewById<EditText>(R.id.etOtp)
        etNum = findViewById(R.id.etNum)

        mAuth = Firebase.auth
        btnGetOtp.setOnClickListener{
            val phoneNumber = "+91${etNum.text}"
            sendVerifyOtp(phoneNumber)
        }
        btnVerifyOtp.setOnClickListener{
            val  otp = etOtp.text.toString()
            verifyOtp(otp)
        }
        
    }
    private fun verifyOtp(otp : String){
        val credential = PhoneAuthProvider.getCredential(verifiationId,otp)
        signInWithCreden(credential)
    }
    private fun signInWithCreden(phoneAuthCredential: PhoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    Toast.makeText(this,"done",Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
                }

            }
    }
    private fun sendVerifyOtp (phoneNumber:String){
        val options =PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(verificationCallBack)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    val verificationCallBack : OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(this@MainActivity,"Done",Toast.LENGTH_SHORT).show()
            }
            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_SHORT).show()
            }
            override fun onCodeSent(s: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, p1)
                verifiationId = s
            }
        }




}