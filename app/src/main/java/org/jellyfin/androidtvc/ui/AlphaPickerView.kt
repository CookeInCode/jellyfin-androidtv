package org.jellyfin.androidtvc.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.view.children
import org.jellyfin.androidtvc.R
import org.jellyfin.androidtvc.databinding.ViewButtonAlphaPickerBinding

class AlphaPickerView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	defStyleRes: Int = 0,
) : HorizontalScrollView(context, attrs, defStyleAttr, defStyleRes) {
	var onAlphaSelected: (letter: Char) -> Unit = {}

	init {
		isFocusable = false
		isFocusableInTouchMode = false
		isHorizontalScrollBarEnabled = false

		val layout = LinearLayout(context)

		val letters = "#${resources.getString(R.string.byletter_letters)}"
		letters.forEach { letter ->
			val binding = ViewButtonAlphaPickerBinding.inflate(LayoutInflater.from(context), this, false)
			binding.button.apply {
				text = letter.toString()
				setOnClickListener { _ ->
					onAlphaSelected(letter)
				}
			}

			layout.addView(binding.root)
		}

		addView(layout)
	}

	fun focus(letter: Char) {
		children
			.filterIsInstance<Button>()
			.firstOrNull { it.text == letter.toString() }
			?.requestFocus()
	}
}
