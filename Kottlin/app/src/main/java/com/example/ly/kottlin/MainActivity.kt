package com.example.ly.kottlin

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.util.Log
import android.widget.Toast
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {
    var fileUrl: String? = null
    var etUrl: TextView? = null
    var btUrl: Button? = null
    var btOpen: Button? = null
    var btSend: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        init();

        btUrl!!.setOnClickListener() {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"//设置类型，我这里是任意类型，任意后缀的可以这样写。
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 1)
        }

        btOpen!!.setOnClickListener() {

            if (fileUrl != null) {
                try {
                    val file = File(fileUrl)
                    val intent2 = Intent("android.intent.action.VIEW")
                    intent2.addCategory("android.intent.category.DEFAULT")
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri = Uri.fromFile(file)
                    if (fileUrl!!.contains(".docx")) {
                        intent2.setDataAndType(uri, "application/msword")
                    } else if (fileUrl!!.contains(".xlsx")) {
                        intent2.setDataAndType(uri, "application/vnd.ms-excel")
                    } else if (fileUrl!!.contains(".txt")) {
                        intent2.setDataAndType(uri, "application/WPSOffice_450.apk")
                        startActivity(intent2)
                    } else {
                        intent2.setDataAndType(uri, "text/plain")
                    }
                    startActivity(intent2)
                } catch (e: Exception) {
                    //没有安装第三方的软件会提示
                    val toast = Toast.makeText(this, "没有找到打开该文件的应用程序", Toast.LENGTH_SHORT)
                    toast.show()
                }
            } else {
                Toast.makeText(this, "请选择或输入文件路径", Toast.LENGTH_SHORT).show()
            }
        }

        btSend!!.setOnClickListener() {
            val i = Intent(this, SecondActivity::class.java)
            startActivity(i);
        }
    }

    fun init() {
        etUrl = findViewById(R.id.et_url)
        btUrl = findViewById(R.id.bt_url)
        btOpen = findViewById(R.id.bt_open)
        btSend = findViewById(R.id.bt_open1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Log.d("lylog", data.toString());
                val uri = data!!.getData()
                runOnUiThread() {
                    etUrl!!.setText(uri.getPath().toString())
                }
                fileUrl = etUrl!!.getText().toString()
                Log.d("lylog", fileUrl.toString());
            }
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(messageEvent: MessageEvent) {

        runOnUiThread() {
            val msg: Message = Message()
            msg!!.what = 1;
            msg.obj = messageEvent.message
            Log.d("lylog", "Event")
            etUrl!!.setText(msg.obj.toString() + "")

        }
    }
    }
