package hal.tutorials.a7minutesworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.widget.Toast
import hal.tutorials.a7minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

//    2. ADD VIEWBINDING
    private var binding:ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//    2. PREPARE THE BINDING TO BE USED
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.flStart?.setOnClickListener{
//            Toast.makeText(this, "Here we will start the exercise.", Toast.LENGTH_LONG).show()
//            3 INTENT DIGUNAKAN UNTUK PINDAH SLIDE
            val intent = Intent(this,ExerciseActivity::class.java )
            startActivity(intent)
        }

//      17: ADD THIS CODE FOR BMI
        binding?.flBMI?.setOnClickListener{
            val intent = Intent(this,BMIActivity::class.java )
            startActivity(intent)
        }

//        22
        binding?.flHistory?.setOnClickListener {
            // Launching the History Activity
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}