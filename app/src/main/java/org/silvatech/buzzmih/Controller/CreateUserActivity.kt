package org.silvatech.buzzmih.Controller

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*
import org.silvatech.buzzmih.R
import org.silvatech.buzzmih.Services.AuthService
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }


    fun generateUserAvatar(view: View){
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if(color == 0) {
            userAvatar = "light$avatar"
        }else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar,"drawable",packageName)

        createAvatarIV.setImageResource(resourceId)
    }

    fun createUserClicked(view: View){

        val email = createEmailTxt.text.toString()

        val password = createPasswordTxt.text.toString()

        AuthService.registerUser(this,email,password) { registerSuccess ->

            if(registerSuccess){
                AuthService.loginUser(this, email,password){loginSuccess ->
                    if(loginSuccess){
                        println(AuthService.authToken)
                        println(AuthService.userEmail)
                    }
                }
            }
        }

    }

    fun generateColorClicked(view: View){
        var random = Random()
        var r = random.nextInt(255)
        var g = random.nextInt(255)
        var b = random.nextInt(255)

        createAvatarIV.setBackgroundColor(Color.rgb(r,g,b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR,$savedG],$savedB,1]"

    }

}
