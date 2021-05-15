package com.example.pwview

import android.animation.ValueAnimator
import android.content.Context
import android.text.*
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText

class AnimateEditText(context: Context, attributeSet: AttributeSet) :
    AppCompatEditText(context, attributeSet), TextWatcher {


    init {
        isCursorVisible = false
    }

    companion object {
        private val LEFT_MULT = 10
        private val RIGHT_MULT = -10
    }

    private var listener: AnimateTextChange? = null


    fun interface AnimateTextChange {
        fun onChange(s: CharSequence?, start: Int, before: Int, count: Int)
    }

    fun addTextChange(listener: AnimateTextChange) {
        this.listener = listener
    }

    fun deleteText() {
        createAlphaAnimation(text, null, false)
    }

    fun deleteTextIfNotEmpty() {
        if (this.text.toString().isNotEmpty()) {
            deleteText()
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        createAlphaAnimation(text, type)
    }


    private fun createAlphaAnimation(
        textInput: CharSequence?,
        type: BufferType?,
        isForward: Boolean = true
    ) {

        val anim = ValueAnimator().apply {
            duration = 50
        }
        val spannable = SpannableString(textInput)


        anim.setObjectValues(spannable)

        anim.setEvaluator { fraction, startValue, endValue ->

            spannable.setSpan(
                TranslateXSpans().apply {
                    if (isForward)
                        tx = LEFT_MULT * fraction
                    else
                        tx = RIGHT_MULT * fraction
                },
                0,
                textInput?.length ?: 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )

        }


        anim.addUpdateListener {

            if (isForward) {
                Log.d("update text ", "start")
                super.setText(spannable, type)
            } else {
                if (it.animatedFraction == 1f) {
                    super.setText("" , type)
                } else if (it.animatedFraction < 1f) {
                    //text will be before delete string
                    Log.d("before delete ", text.toString())
                    super.setText(spannable, type)
                }
            }
        }
        anim.start()

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }


    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (before != count) {
            this.setText(s)
            listener?.onChange(s, start, before, count)
        }

    }


    override fun afterTextChanged(s: Editable?) {
    }


}