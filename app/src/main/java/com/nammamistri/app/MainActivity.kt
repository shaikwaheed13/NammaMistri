package com.nammamistri.app

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.nammamistri.app.calculation.MaterialCalculator

data class Worker(
    val name: String,
    val role: String,
    val dailyRate: Double,
    var presentDays: Double = 0.0,
    var advance: Double = 0.0
) {
    val earned: Double get() = presentDays * dailyRate
    val balance: Double get() = earned - advance
}

class MainActivity : Activity() {
    private lateinit var content: LinearLayout
    private val workers = mutableListOf(
        Worker("Ravi", "Mason", 900.0, presentDays = 3.0, advance = 500.0),
        Worker("Manju", "Helper", 650.0, presentDays = 2.5, advance = 300.0)
    )

    private var brickRate = 8200.0
    private var cementRate = 430.0
    private var sandRate = 48.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showApp()
    }

    private fun showApp() {
        val root = vertical().apply {
            setBackgroundColor(Color.rgb(247, 245, 239))
        }

        root.addView(header())
        root.addView(navBar())

        val scroll = ScrollView(this)
        content = vertical().apply {
            setPadding(dp(18), dp(16), dp(18), dp(28))
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(match(), 0, 1f))

        setContentView(root)
        showDashboard()
    }

    private fun header(): View = vertical().apply {
        setPadding(dp(18), dp(20), dp(18), dp(16))
        setBackgroundColor(Color.rgb(47, 93, 80))
        addView(label("Namma Mistri", 28, Color.WHITE, true))
        addView(label("AI-powered construction assistant for rural India", 14, Color.rgb(232, 222, 198), false))
    }

    private fun navBar(): View = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        setPadding(dp(8), dp(8), dp(8), dp(8))
        setBackgroundColor(Color.rgb(23, 33, 30))
        addView(navButton("Home") { showDashboard() })
        addView(navButton("Calc") { showCalculator() })
        addView(navButton("Team") { showTeam() })
        addView(navButton("Rates") { showRates() })
    }

    private fun showDashboard() {
        content.replaceAll {
            addView(label("Active Site", 13, muted(), true))
            addView(label("Basaveshwara Nilaya", 24, ink(), true))
            addView(label("Owner: Shankar Gowda - Bengaluru Rural", 15, muted(), false))
            addSpace(14)
            addView(metric("Material calculator", "Wall estimates with bricks, cement, sand, and cost."))
            addView(metric("Labour diary", "${workers.size} workers - Rs ${workers.sumOf { it.balance }.toInt()} balance pending"))
            addView(metric("Local rates", "Bricks Rs ${brickRate.toInt()}/1000 - Cement Rs ${cementRate.toInt()}/bag"))
            addSpace(10)
            addView(primaryButton("Calculate wall materials") { showCalculator() })
        }
    }

    private fun showCalculator() {
        content.replaceAll {
            addView(label("Wall Calculator", 24, ink(), true))
            addView(label("Enter wall size in feet. Thickness can be 4.5, 6, or 9 inches.", 14, muted(), false))

            val length = input("Length feet", "12")
            val height = input("Height feet", "10")
            val thickness = input("Thickness inches", "9")
            val result = label("", 16, ink(), false).apply {
                setPadding(0, dp(14), 0, 0)
            }

            addView(length)
            addView(height)
            addView(thickness)
            addView(primaryButton("Calculate materials") {
                try {
                    val wall = MaterialCalculator.calculateWall(
                        length.number(),
                        height.number(),
                        thickness.number(),
                        brickRate,
                        cementRate,
                        sandRate
                    )
                    result.text = wall.asSummary()
                } catch (error: IllegalArgumentException) {
                    Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
            addView(result)
        }
    }

    private fun showTeam() {
        content.replaceAll {
            addView(label("Labour Diary", 24, ink(), true))
            addView(label("Tap attendance buttons to update today's wage sheet.", 14, muted(), false))

            workers.forEach { worker ->
                addView(workerRow(worker))
            }
            addSpace(8)
            addView(primaryButton("Add sample helper") {
                workers.add(Worker("New Helper ${workers.size + 1}", "Helper", 600.0))
                showTeam()
            })
        }
    }

    private fun showRates() {
        content.replaceAll {
            addView(label("Material Rates", 24, ink(), true))
            addView(label("Edit local rates used by the calculator.", 14, muted(), false))

            val bricks = input("Bricks rate per 1000", brickRate.toInt().toString())
            val cement = input("Cement rate per bag", cementRate.toInt().toString())
            val sand = input("Sand rate per cft", sandRate.toInt().toString())

            addView(bricks)
            addView(cement)
            addView(sand)
            addView(primaryButton("Save rates") {
                brickRate = bricks.number()
                cementRate = cement.number()
                sandRate = sand.number()
                Toast.makeText(this@MainActivity, "Rates saved", Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun workerRow(worker: Worker): View = vertical().apply {
        setPadding(dp(14), dp(14), dp(14), dp(14))
        setBackgroundColor(Color.WHITE)
        addView(label("${worker.name} - ${worker.role}", 18, ink(), true))
        addView(label("Rate Rs ${worker.dailyRate.toInt()}/day - Days ${worker.presentDays} - Balance Rs ${worker.balance.toInt()}", 14, muted(), false))
        addView(LinearLayout(this@MainActivity).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(smallButton("Present") {
                worker.presentDays += 1.0
                showTeam()
            })
            addView(smallButton("Half") {
                worker.presentDays += 0.5
                showTeam()
            })
            addView(smallButton("Advance") {
                worker.advance += 500.0
                showTeam()
            })
        })
    }.withMargins(bottom = 10)

    private fun metric(title: String, detail: String): View = vertical().apply {
        setPadding(dp(14), dp(12), dp(14), dp(12))
        setBackgroundColor(Color.WHITE)
        addView(label(title, 18, ink(), true))
        addView(label(detail, 14, muted(), false))
    }.withMargins(bottom = 10)

    private fun navButton(text: String, onClick: () -> Unit): Button =
        Button(this).apply {
            this.text = text
            textSize = 13f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.TRANSPARENT)
            setOnClickListener { onClick() }
            layoutParams = LinearLayout.LayoutParams(0, dp(48), 1f)
        }

    private fun primaryButton(text: String, onClick: () -> Unit): Button =
        Button(this).apply {
            this.text = text
            textSize = 16f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(161, 78, 44))
            setOnClickListener { onClick() }
            layoutParams = LinearLayout.LayoutParams(match(), dp(52)).withTop(14)
        }

    private fun smallButton(text: String, onClick: () -> Unit): Button =
        Button(this).apply {
            this.text = text
            textSize = 12f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(47, 93, 80))
            setOnClickListener { onClick() }
            layoutParams = LinearLayout.LayoutParams(0, dp(44), 1f).withTop(8)
        }

    private fun input(hint: String, value: String): EditText =
        EditText(this).apply {
            this.hint = hint
            setText(value)
            textSize = 16f
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            setSingleLine(true)
            layoutParams = LinearLayout.LayoutParams(match(), wrap()).withTop(12)
        }

    private fun label(text: String, size: Int, color: Int, bold: Boolean): TextView =
        TextView(this).apply {
            this.text = text
            textSize = size.toFloat()
            setTextColor(color)
            if (bold) typeface = android.graphics.Typeface.DEFAULT_BOLD
            gravity = Gravity.START
            setLineSpacing(0f, 1.08f)
        }

    private fun vertical(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
    }

    private fun LinearLayout.replaceAll(block: LinearLayout.() -> Unit) {
        removeAllViews()
        block()
    }

    private fun LinearLayout.addSpace(heightDp: Int) {
        addView(View(this@MainActivity), LinearLayout.LayoutParams(match(), dp(heightDp)))
    }

    private fun EditText.number(): Double = text.toString().toDoubleOrNull() ?: 0.0

    private fun View.withMargins(bottom: Int = 0): View = apply {
        layoutParams = LinearLayout.LayoutParams(match(), wrap()).apply {
            setMargins(0, 0, 0, dp(bottom))
        }
    }

    private fun LinearLayout.LayoutParams.withTop(top: Int): LinearLayout.LayoutParams = apply {
        setMargins(0, dp(top), 0, 0)
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
    private fun match() = LinearLayout.LayoutParams.MATCH_PARENT
    private fun wrap() = LinearLayout.LayoutParams.WRAP_CONTENT
    private fun ink() = Color.rgb(23, 33, 30)
    private fun muted() = Color.rgb(87, 96, 91)
}
