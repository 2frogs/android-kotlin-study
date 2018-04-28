package com.sample.sample_kotlin_android.findword

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.widget.Toast
import com.sample.sample_kotlin_android.R
import com.sample.sample_kotlin_android.util.MPermissionsActivity
import kotlinx.android.synthetic.main.findword_activity.*
import java.io.*


class FindWordMainActivity : MPermissionsActivity() {

    // 默认只读的list
    var dictList : MutableList<String> = mutableListOf()
    //var dictList : List<String> = emptyList()  // readonly

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.findword_activity)

        /* apply permission for ext storage */
        requestPermission(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0x0001)

        // 加载词典
        var inputReader : InputStreamReader =
                InputStreamReader(resources.assets.open("dict.txt"))
        var bufReader : BufferedReader = BufferedReader(inputReader)
        var line : String? = null
        line = bufReader.readLine()
        while(line != null) {
            dictList.add(line.toUpperCase())
            line = bufReader.readLine()
        }

        println("size=" + dictList.size);
    }

    fun fwClick(view : View) {
        when(view.id) {
            R.id.btnFind -> {    // 响应btnFind按钮
                /*
                   可以直接通过ID访问activity_main.xml里定义的组件，
                   需要import kotlinx.android.synthetic.main.activity_main.*
                 */
                val editStr = textEdit.text  // 获取textEdit输入框的值
                val findStr = findWord(editStr.toString())
                println("findStr=" + findStr);
                wordText.text  = Editable.Factory.getInstance().newEditable(findStr)
           }
        }
    }

    fun findWord(editStr : String) : String {
        var charArr  = arrayOfNulls<Array<String>>(4)
        val list: List<String> = editStr.toUpperCase().lines()
        var i : Int = 0
        for(str in list) {
            charArr[i++] = str.split(" ").toTypedArray()
        }
        FindWord.boardArr = charArr;

        var foundWords = "";

        var rCount = 0
        var fWord = ""

        for(word : String in dictList) {
            val isFind = FindWord.findWord(word)
            if(word.length >= 3 && isFind == true) {
                fWord += ("    ${word}                         ".substring(0, 24))
                rCount++;
            }

            if(rCount == 2) {
                foundWords += ("${fWord}\r\n")
                rCount = 0
                fWord = ""
            }
        }
        foundWords += ("${fWord}\r\n")

        return foundWords;
    }
}


