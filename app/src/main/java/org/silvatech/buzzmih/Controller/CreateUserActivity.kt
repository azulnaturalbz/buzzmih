package org.silvatech.buzzmih.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*
import org.silvatech.buzzmih.R
import org.silvatech.buzzmih.Services.AuthService
import org.silvatech.buzzmih.Services.UserDataService
import org.silvatech.buzzmih.Utilities.BROADCAST_USER_DATA_CHANGE
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5 ,0.5 ,0.5 , 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }


    fun generateUserAvatar(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)

        createAvatarIV.setImageResource(resourceId)
    }

    fun createUserClicked(view: View) {

        enableSpinner(true)

        val userName = createUsernameTxt.text.toString()

        val email = createEmailTxt.text.toString()

        val password = createPasswordTxt.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

            AuthService.registerUser(this, email, password) { registerSuccess ->

                if (registerSuccess) {
                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        if (loginSuccess) {
                            println(AuthService.authToken)
                            println(AuthService.userEmail)
                            AuthService.createUser(this, userName, email, userAvatar, avatarColor) { createSuccess ->
                                if (createSuccess) {

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                                    enableSpinner(false)
                                    finish()
                                } else {
                                    errorToast()
                                }
                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }

        } else {

            Toast.makeText(this, "Make sure the username, email and password are filled in.", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }

    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }


    fun enableSpinner(enable: Boolean) {
        if (enable) {

            createSpinner.visibility = View.VISIBLE


        } else {

            createSpinner.visibility = View.INVISIBLE


        }

        createUserBtn.isEnabled = !enable

        createAvatarIV.isEnabled = !enable

        backgroundColorBtn.isEnabled = !enable

    }

    fun generateColorClicked(view: View) {
        var random = Random()
        var r = random.nextInt(255)
        var g = random.nextInt(255)
        var b = random.nextInt(255)

        createAvatarIV.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG ,$savedB, 1]"

    }




}
