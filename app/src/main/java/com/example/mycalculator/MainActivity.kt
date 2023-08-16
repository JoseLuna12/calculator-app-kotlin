package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var calcInput: TextView? = null
    private var lastDot: Boolean = false
    private var lastNumber: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calcInput = findViewById(R.id.calculator_input)
    }
    fun testButton (view: View){
        val buttonView = view as Button
//        Toast.makeText(this, "click", Toast.LENGTH_LONG).show()
        calcInput?.append(buttonView.text)
        lastNumber = true
    }

    fun onClearButton (view: View){
        val clearButton = view as Button
        if(clearButton.text == "CLR"){
            calcInput?.text = ""
            lastDot = false
            lastNumber = true
        }
    }

    fun onEqual (view: View) {
            if(lastNumber){
                var value = calcInput?.text.toString()
                var prefix = ""
                try {
                    if(value.startsWith("-")){
                        value = value.substring(1)
                        prefix = "-"
                    }
                    if(value.contains("-")){
                        doOperation(prefix, value, "-") { a, b ->
                            a.toDouble() - b.toDouble()
                        }
                    }
                    if(value.contains("+")){
                        doOperation(prefix, value, "+") { a, b ->
                            a.toDouble() + b.toDouble()
                        }
                    }
                    if(value.contains("/")){
                        doOperation(prefix, value, "/") { a, b ->
                            a.toDouble() / b.toDouble()
                        }
                    }
                    if(value.contains("x")){
                        doOperation(prefix, value, "x") { a, b ->
                            a.toDouble() * b.toDouble()
                        }
                    }
                }catch (e: java.lang.ArithmeticException){
                    e.printStackTrace()
                }
            }
    }

    private fun doOperation(prefix: String, value: String, operator: String, operation: (a: String, b: String) -> Double){
        if(value.contains(operator)){
            val splitValue = value.split(operator)
            var one = splitValue[0]
            if(prefix.isNotEmpty()){
                one = prefix + one
            }
            val two = splitValue[1]
            val response = operation(one, two)
            calcInput?.text =removeDecimal(response)
        }
    }

    private fun removeDecimal(value: Double): String{
        var strValue = value.toString()
        if(strValue.contains(".0")){
            strValue = strValue.substring(0, strValue.length - 2)
        }
        return strValue
    }

    fun addDot(view: View) {
        if(!lastDot && lastNumber){
            calcInput?.append(".")
            lastDot = true
            lastNumber = false
        }
    }

    fun onOperatorAdded(view: View) {
        calcInput?.text?.let {
            if(lastNumber && !hasOperator(it.toString())){
                val button = view as Button
                calcInput?.append(button.text)
                lastNumber = false
                lastDot = false
            }
        }
    }

    private fun hasOperator (value: String): Boolean {
        return if(value.startsWith("-")){
            false
        }else{
            value.contains("/") || value.contains("x") || value.contains("-") || value.contains("+")
        }
    }
}