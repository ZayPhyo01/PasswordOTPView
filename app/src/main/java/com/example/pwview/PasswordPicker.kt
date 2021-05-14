package com.example.pwview

import android.animation.ObjectAnimator
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import java.lang.StringBuilder

class PasswordPicker(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet), TextWatcher {

    lateinit var edtPw1: AppCompatEditText
    lateinit var edtPw2: AppCompatEditText
    lateinit var edtPw3: AppCompatEditText
    lateinit var edtPw4: AppCompatEditText
    lateinit var edtPw5: AppCompatEditText
    lateinit var edtPw6: AppCompatEditText

    interface Listener {
        fun getSubmitPassword(pw: String)
    }

    private val listOfPwEditText = ArrayList<AppCompatEditText>()
    private val listOfPwAnimation = ArrayList<ObjectAnimator>()

    private fun loadAnimation() {
        Log.d("animation size", "${listOfPwEditText.size}")
        listOfPwEditText.forEachIndexed { i, e ->
            Log.d("add $i", "animation")
            listOfPwAnimation.add(ObjectAnimator.ofFloat(e, "translationX", 0f, 40f , 0f).apply {
                interpolator = BounceInterpolator()


                duration = 500
            })
        }
    }

    val OTP_MAX = 6
    private var currentIndex = 0;
    lateinit var pwView: View


    override fun onFinishInflate() {
        super.onFinishInflate()
        createPasswordSlots()
        loadAnimation()
        edtPw1.requestFocus()
    }

    private fun createPasswordSlots() {
        edtPw1 = findViewById(R.id.pwField1)
        edtPw2 = findViewById(R.id.pwField2)
        edtPw3 = findViewById(R.id.pwField3)
        edtPw4 = findViewById(R.id.pwField4)
        edtPw5 = findViewById(R.id.pwField5)
        edtPw6 = findViewById(R.id.pwField6)

        listOfPwEditText.apply {
            add(edtPw1)
            add(edtPw2)
            add(edtPw3)
            add(edtPw4)
            add(edtPw5)
            add(edtPw6)
        }
        listenPasswordFill()
    }

    private fun listenPasswordFill() {
        listOfPwEditText.forEachIndexed { i, e ->
            e.addTextChangedListener(this)
            e.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (e.text.toString().isEmpty()) {
                        onTapBackKey()
                    }
                }
                false
            }
            e.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    currentIndex = i
                }
            }
        }

    }

    private fun goToNext() {
        currentIndex++
        if (currentIndex < listOfPwEditText.size) {
            listOfPwEditText[currentIndex].requestFocus()
        }
    }

    var listener: Listener? = null

    fun setOnPasswordSubmitListener(listener: Listener) {
        this.listener = listener
    }

    private fun onTapBackKey() {

        currentIndex--
        if (currentIndex >= 0) {
            listOfPwEditText[currentIndex].requestFocus()
        } else {
            currentIndex = 0
        }
    }

    private fun startAnimation(i: Int) {
        listOfPwAnimation[i].start()

    }

    fun verify(): Boolean {

        val password = StringBuilder()
        listOfPwEditText.forEachIndexed { i, e ->
            if (e.text.toString().isNotEmpty()) {
                password.append(e.text)
            } else {
                listOfPwEditText[i].background =
                    ContextCompat.getDrawable(context, R.drawable.bg_pw_error)
                startAnimation(i)
            }
        }
        if (password.length == this.childCount) {
            listener?.getSubmitPassword(password.toString())
            return true
        }
        return false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        if (s.toString().isOneChar() || s.toString().isNotEmpty()) {
            listOfPwEditText[currentIndex].background =
                ContextCompat.getDrawable(context, R.drawable.bg_pw)
            goToNext()
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

}