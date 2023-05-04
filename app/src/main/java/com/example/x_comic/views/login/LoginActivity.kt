package com.example.x_comic.views.login

import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.x_comic.R
import com.example.x_comic.databinding.ActivityLoginBinding
import com.example.x_comic.databinding.LayoutDialogSendpassBinding
import com.example.x_comic.models.User
import com.example.x_comic.viewmodels.*
import com.example.x_comic.views.main.MainActivity
import com.example.x_comic.views.signup.SignupActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var bindingDialog: LayoutDialogSendpassBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userViewModel: UserViewModel
    private var RC_SIGN_IN = 123321
    private lateinit var mClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // TODO: Lúc này người dùng đã đăng nhập rồi.
        if (FirebaseAuthManager.auth.currentUser != null) {
            nextMainActivity()
            var currentUser = FirebaseAuthManager.getUser()
            if (currentUser != null) {
                userViewModel.callApi(currentUser.uid)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        bindingDialog = LayoutDialogSendpassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val progressDialog = ProgressDialog(this)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        binding.loginBtn.setOnClickListener {
            val email = binding.usernameET.text.toString().trim()
            val password = binding.passwordET.text.toString().trim()

            val result = verify(email, password)

            val isValid = result.first
            val msg = result.second

            if (!isValid) {
                displayErrorMsg(msg)
            }
            else {
                progressDialog.show()
                progressDialog.setMessage("Loading...")
                loginViewModel.login(email, password).observe(this, Observer { success ->
                    if (success) {
                        val userAuth = FirebaseAuthManager.getUser()

                        if (userAuth != null) {
                            nextMainActivity()
                        }
                        progressDialog.cancel()
                    } else {
                        displayErrorMsg(FirebaseAuthManager.msg)
                        progressDialog.cancel()
                    }
                })
            }
        }

        binding.signupTV.setOnClickListener {
            nextSignupActivity()
        }

        binding.forgotPwdTV.setOnClickListener {
            showSendEmailDialog()
        }

        // code này để yêu cầu địa chỉ email của người dùng
        mClient = createRequest()
        // nhấn để login bằng google

        // TODO: Xử lý code ở UI rất nhiều. Do có sử dụng Intent nên là chuyển sang thư mục khác khó xài
        binding.signInButton.setOnClickListener {
            signIn(mClient)
        }
    }

    private fun createRequest() : GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    private fun signIn(mGoogleSignInClient: GoogleSignInClient) {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: com.google.android.gms.tasks.Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        val progressDialog = ProgressDialog(this)
        val account = completedTask.getResult(ApiException::class.java)
        if (account.idToken != null) {
            progressDialog.show()
            progressDialog.setMessage("Loading...")
            loginViewModel.loginByGoogle(account.idToken!!).observe(this, Observer { success ->
                if (success) {
                    val userAuth = FirebaseAuthManager.getUser()

                    if (userAuth != null) {
                        // TODO: 1. kiểm tra ở Realtime đã có dữ liệu hay chưa
                        // 2. nếu chưa có thì thêm mới rồi đăng nhập
                        // 3. nếu có rồi thì đăng nhập luôn
                        // 4. truyền dữ liệu ở realtime vào main ui

                        val uid = userAuth.uid

                        userViewModel.isExist(uid) { ok ->
                            if (ok) {
                                // User exists
                            } else {
                                val user = User()
                                user.id = userAuth.uid
                                user.email = userAuth.email.toString()

                                userViewModel.addUser(user)
                            }
                        }
                        nextMainActivity()
                    }
                    progressDialog.cancel()
                } else {
                    displayErrorMsg(FirebaseAuthManager.msg)
                    progressDialog.cancel()
                }
            })
        } else {
            displayErrorMsg("Account idToken not found!")
        }
    }
    private fun showSendEmailDialog() {
        var dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog_sendpass)

        var window: Window? = dialog.window

        if (window == null) {
            return
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        var windowAttribution: WindowManager.LayoutParams = window.attributes

        windowAttribution.gravity = Gravity.CENTER

        window.attributes = windowAttribution


        val sendBtn = dialog.findViewById<Button>(R.id.sendBtn)
        val emailET = dialog.findViewById<EditText>(R.id.sendEmailET)

        sendBtn.setOnClickListener {
            val email: String = emailET.text.toString().trim()

            FirebaseAuthManager.auth.sendPasswordResetEmail(email)
                .addOnFailureListener {
                    setEditTextBorderColor(emailET, Color.RED)
                }
                .addOnSuccessListener {
                    dialog.dismiss()
                    Toast.makeText(this, "Send", Toast.LENGTH_LONG).show()
                }
        }
        dialog.show()
    }

    private fun setEditTextBorderColor(editText: EditText, color: Int) {
        val shape = GradientDrawable()
        shape.setStroke(2, color) // set the border width and color
        shape.cornerRadius = 4f // set the corner radius
        shape.setColor(Color.WHITE) // set the background color

        editText.background = shape // set the custom drawable as the background of the EditText
    }

    private fun verify(email: String, password: String): Pair<Boolean, String> {
        var isValid = true
        var msg = ""

        if (email.isEmpty()) {
            isValid = false
            setEditTextBorderColor(binding.usernameET, Color.RED)
            msg = "Email is not empty!"
        }
        if (password.isEmpty()) {
            isValid = false
            setEditTextBorderColor(binding.passwordET, Color.RED)
            msg = "Password is not empty!"
        }

        if (email.isEmpty() && password.isEmpty()) {
            msg = "Email and password are not empty!"
        }

        return Pair(isValid, msg)
    }

    private fun displayErrorMsg(msg: String) {
        val snackbar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.love))
        snackbar.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    private fun nextMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun nextSignupActivity() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}