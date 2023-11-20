package com.example.mynewcalculater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.mynewcalculater.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private var lastOperand = 0
    private var lastOperator = ""
    private var inputNums = StringBuilder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        val view = binding.root
        setContentView(view)

        binding.tvOne.setOnClickListener(this)
        binding.tvTwo.setOnClickListener(this)
        binding.tvZero.setOnClickListener(this)

        binding.tvPlus.setOnClickListener {
            clickOperator(it)
        }
        binding.back.setOnClickListener{
            clickUnaryOperator({x:Int -> removeLastdigit(x)})
        }
        binding.tvSign.setOnClickListener {
            clickUnaryOperator( {x:Int -> -x} )
        }
        binding.tvEqual.setOnClickListener {
            clickOperator(it)
        }

    }

    private fun clickUnaryOperator( converter: (Int) -> Int ) {
        if(lastOperator == "")
        {
            lastOperand = converter(lastOperand)
            if(binding.tvResult.text.toString().isNotEmpty())
            {
                var result = binding.tvResult.text.toString().toInt()
                result = converter(result)
                binding.tvResult.text = result.toString()
            }
        }
        if(inputNums.isNotEmpty() && inputNums.toString().toInt()!= 0)
        {
            var value = inputNums.toString().toInt()
            value = converter(value)
            inputNums.clear()
            inputNums.append(value.toString())
        }
        displayExpression()
    }


    private fun removeLastdigit(num: Int): Int {
        var s = num.toString();
        if(s.isNotEmpty()){
            s =s.dropLast(1)
            if(s.isNotEmpty())
                return s.toInt()
        }
        return 0
    }

    private fun clickOperator(v: View?) {
        val textView= v as TextView

        if(inputNums.isEmpty())
        {
            lastOperator = textView.text.toString()
            if(lastOperator == "=") {
                lastOperator = ""
                binding.tvResult.text = lastOperand.toString()
            }
            displayExpression()
            return
        }

        var result = lastOperand.toString()
        if(lastOperator != "") {
            var operand2 = inputNums.toString().toInt()
            result = calculate(lastOperand, operand2, lastOperator)
        }
        if(!result.isEmpty())
            lastOperand = result.toInt()
        lastOperator = textView.text.toString()

        if(lastOperator == "=")
            lastOperator = ""
        binding.tvResult.text = result

        inputNums.clear()
        displayExpression()

    }

    private fun calculate(op1: Int, op2: Int, operator: String): String {
        var result = ""
        if(operator == "+")
            result = (op1 + op2).toString()
        else if(operator == "-")
            result = (op1 - op2).toString()
        return result
    }


    override fun onClick(v: View?) {
        val textView= v as TextView
        Log.i("MYTAG", "Texview ${textView.text} clicked")
        if (v != null) {
            clickNumber(v)
        }
    }

    private fun clickNumber(v: TextView) {
        inputNums.append(v.text)
        var n = inputNums.toString().toInt()
        inputNums.clear()
        inputNums.append(n.toString())
        if(lastOperator == "")
            lastOperand = n
        displayExpression()
    }

    private fun displayExpression() {
        var str = StringBuilder()
        str.append(lastOperand)
        if(lastOperator!= "")
        {
            str.append(" $lastOperator ")
            if(!inputNums.isEmpty())
                str.append(inputNums)
        }

        binding.tvProgress.text = str.toString()
    }
}