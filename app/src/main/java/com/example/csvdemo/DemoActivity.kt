package com.example.csvdemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.csvdemo.databinding.ActivityDemoBinding
import org.json.JSONObject

class DemoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitBtn.setOnClickListener {
            Log.d("Database", "Uploading user details...")
            addDataToDatabase()
        }
    }

    private fun addDataToDatabase() {
        val name = binding.inputName.text.toString()
        val surname = binding.inputSurname.text.toString()
        val rollNo = binding.inputRollno.text.toString()

//        val url = "http://localhost/learning/addCourses.php"
        val url = "http://192.168.245.105/learning/addCourses.php"
        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(Method.POST, url, { response ->
            Log.d("Database", "Response: $response")
            try {
                val jsonObject = JSONObject(response)
                Toast.makeText(this,
                    "Success: ${jsonObject.getString("message")}",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.d("Database", "Error in response", e)
            }
        }, { e ->
            Toast.makeText(this,
                "Fail to get response = $e",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("Database", "Error", e)
        }) {
            override fun getBodyContentType(): String {
                return "application/x-www-form-urlencoded; charset=UTF-8"
            }

            override fun getParams(): MutableMap<String, String>? {
                Log.d("Database", "Getting params")
                val params = mutableMapOf<String, String>()
                params["name"] = name
                params["surname"] = surname
                params["roll_no"] = rollNo
                return params
            }
        }

        queue.add(request)
    }
}