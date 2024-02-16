package org.jellyfin.androidtvc.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import org.jellyfin.androidtvc.databinding.ViewNumberSpinnerBinding

class NumberSpinnerView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
	val binding = ViewNumberSpinnerBinding.inflate(LayoutInflater.from(context), this, true)

	var increment = 10L
	var valueChangedListener: ValueChangedListener<Long>? = null

	var value = 0L
		set(value) {
			field = value
			binding.value.text = value.toString()
			valueChangedListener?.onValueChanged(value)
		}

	init {
		binding.increase.setOnClickListener { value += increment }
		binding.decrease.setOnClickListener { value -= increment }
	}
}
