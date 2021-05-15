package com.example.pwview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.text.*
import android.text.style.*
import android.util.AttributeSet
import android.util.Log
import android.view.animation.BounceInterpolator
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class AnimateEditText(context: Context, attributeSet: AttributeSet) :
    AppCompatEditText(context, attributeSet), TextWatcher {

    private var listener: AnimateTextChange? = null

    init {
        this.isCursorVisible = false
    }

    fun interface AnimateTextChange {
        fun onChange(s: CharSequence?, start: Int, before: Int, count: Int)
    }

    fun addTextChange(listener: AnimateTextChange) {
        this.listener = listener
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        createAlphaAnimation(text, type)
    }


    private fun createAlphaAnimation(target: CharSequence?, type: BufferType?) {

        val spannable = SpannableString(target)
        val anim = ValueAnimator().apply {
            duration = 80
        }

        anim.setObjectValues(spannable)

        anim.setEvaluator { fraction, startValue, endValue ->

            spannable.setSpan(
                TranslateXSpans().apply {
                    tx = 8 * fraction
                },
                0,
                target?.length ?: 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )

        }
        anim.addUpdateListener {
            Log.d("update text ", "start")
            super.setText(spannable, type)
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