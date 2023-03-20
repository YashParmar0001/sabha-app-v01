package com.example.csvdemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.csvdemo.databinding.ActivityMainBinding
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.importFileBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" // to allow any file type
            intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "text/csv",
                "application/vnd.ms-excel"
            ))
            startActivityForResult(intent, 0)
        }

        binding.dataAddBtn.setOnClickListener {
            startActivity(Intent(this, DemoActivity::class.java))
            finish()
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onActivityResult(requestCode, resultCode, data)",
        "androidx.appcompat.app.AppCompatActivity"
    )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
//                Log.d("CSVReader", "File selected: $uri")
//                val inputStream = contentResolver.openInputStream(uri)
//
//                val reader =
//                    CSVReader(FileReader(
//                        File(uri.toString().replace("content", "file"))))
//                reader.linesRead
//
//                inputStream?.close()
                readFile(uri)
            }

        }

    }

    private fun readFile(uri: Uri) {
        val fileType = getFilePathFromContentUri(uri).split(".")[1]
        val inputStream = contentResolver.openInputStream(uri)

        when (fileType) {
            "xlsx" -> {
                try {
                    val reader = XSSFWorkbook(inputStream)
                    val noOfSheet = reader.numberOfSheets
                    Log.d("XLSX", "No of sheets: $noOfSheet")
                    // TODO: Handle sheet data here
                    for (sheet in reader) {
                        for (row in sheet) {
                            for (cell in row) {
                                Log.d("XLSX", cell.toString())
                            }
                        }
                    }
                } catch (e: IOException) {
                    Toast.makeText(this, "Unable to process file", Toast.LENGTH_SHORT).show()
                }
            }
            "xls" -> {
                try {
                    val reader = HSSFWorkbook(inputStream)
                    val noOfSheet = reader.numberOfSheets
                    Log.d("XLSX", "No of sheets: $noOfSheet")
                    // TODO: Handle sheet data here
                } catch (e: IOException) {
                    Toast.makeText(this, "Unable to process file", Toast.LENGTH_SHORT).show()
                }
            }
            "csv" -> {
                // TODO: Add CSV reader here
                try {
                    val reader = HSSFWorkbook(inputStream)
                    val noOfSheet = reader.numberOfSheets
                    Log.d("XLSX", "No of sheets: $noOfSheet")
                    // TODO: Handle sheet data here
                } catch (e: IOException) {
                    Toast.makeText(this, "Unable to process file", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "Please enter valid file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFilePathFromContentUri(contentUri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val cursor = applicationContext.contentResolver.query(contentUri, projection, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            cursor.moveToFirst()
            val fileName = cursor.getString(columnIndex)
            Log.d("XLSX", "File name: $fileName")
            val dir = Environment.getExternalStorageDirectory()
            cursor.close()
            return File(dir, fileName).absolutePath
        }
        return "null"
    }

}