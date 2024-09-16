package hal.tutorials.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import hal.tutorials.a7minutesworkout.databinding.ActivityExerciseBinding
import hal.tutorials.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import java.lang.Exception
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
//    3 ADD BINDING FOR THE 2ND SLIDE
    private var binding : ActivityExerciseBinding? = null

//    4. THIS IS FOR TIMER
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

//    15 berikut ini adalah code untuk buat variabel resTimerDuration:
    private var restTimerDuration: Long = 1 // default untuk 1 dtk

//    6 add data from exercise list
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1 // jika diingkrement akan -1 + 1 = 0  jadi posisinya di indeks 0 atau awal dari arraylist

//    9 add this to add feature: text to speech:
    private var tts: TextToSpeech? = null

//    10. add this code to add media player:
    private var player: MediaPlayer? = null

//    13. add this for RecyclerView to add list of current exercise
    private var exerciseAdapter: ExerciseStatusAdapter? = null


//    5. THIS IS FOR EXERCISE TIMER
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    //    15 berikut ini adalah code untuk buat variabel resTimerDuration:
    private var exerciseTimerDuration: Long = 1 // default untuk 1 dtk


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        3 INITIATE THE BINDING IN 2ND SLIDE
        binding = ActivityExerciseBinding.inflate(layoutInflater)

        setContentView(binding?.root)

//        3 ADD TOOLBAR/action bar
        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener{
//            16: add custom dialog
            customDIalogForBackButton()
        }

//        6 add data constants
        exerciseList = Constants.defaulExersiceList()

//        9. add text to speech
        tts = TextToSpeech(this, this)

//        4. CALL THE TIMER METHOD
        setupRestView()
//        13 : add this code:
        setupExerciseStatusRecyclerView()
    }

//    16: add this code
    override fun onBackPressed() {
        customDIalogForBackButton()
//        super.onBackPressed()
    }

//    16: add this code
    private fun customDIalogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false) // ketika klik di luar dialog button maka tidak bisa.
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()
    }

//    13. add this method:
    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

//    4. EXTRA METHOD IF WE HAVE TO CHANGE THE OTHER TIMER METHOD WE CAN DO THIS IN THIS FUNCTION
    private fun setupRestView(){
    //    10. add this code to add media player:
        try {
            val soundURI = Uri.parse("android.resource://hal.tutorials.a7minutesworkout/" + R.raw.press_start) // add sound
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false // biar ga looping
            player?.start()
        } catch (e: Exception){
            e.printStackTrace()
        }


    //    7. ganti nama jadi flRestview yang tadinya flprogressbar
        binding?.flRestView?.visibility = View.VISIBLE // INI AKAN MENUTUPI KALAU GONE AKAN MENGHAPUS
//    7. add this code
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE

//        8 add this code: untuk menambahkan nama upcoming activity dan upcoming exercise
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE

//    THIS WILL RESET THE TIMER WHEN ITS DONE
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
//        8 add this code: digunakan untuk menampilkan nama exercise yang akan datang
        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition+1].getName()

        setRestProgressBar()
    }

//    5. method ini digunakan pada saat exercise nya muncul
    private fun setupExerciseView() {
//    7. ganti nama jadi flRestview yang tadinya flprogressbar
        binding?.flRestView?.visibility = View.INVISIBLE // INI AKAN MENUTUPI KALAU GONE AKAN MENGHAPUS
//    7. add this code
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
//        8 add this code: untuk menghilangkan nama upcoming activity dan upcoming exercise
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

//    add this code:
    if(exerciseTimer != null){
        exerciseTimer?.cancel()
        exerciseProgress = 0
    }

//    9: add this code: to get string name and send to text to speech
    speakOut(exerciseList!![currentExercisePosition].getName())



//    7.
    binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage()) // get the image via int
    binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()// get the name via int


    setExerciseProgressBar()
    }

//    4. SET TIMER DAN BINDING THE TIMER
    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress

//    15: add the restTimerDuration in CountDownTimer: dikali 1000 untuk mempercepat durasi testing
    restTimer = object: CountDownTimer(restTimerDuration*1000, 1000){ // 10 detik dengan interval 1 detik
        override fun onTick(p0: Long) {
            restProgress++
            binding?.progressBar?.progress =10 -restProgress
            binding?.tvTimer?.text = (10 - restProgress).toString() // hitung mundur
        }
        override fun onFinish() {
//            6
            currentExercisePosition++ // ketika exercisenya selesai maka akan next ke berikutnya
//            Toast.makeText(this@ExerciseActivity, "Here now we will start the exercise.", Toast.LENGTH_SHORT ).show()
//            14 add this code:
            exerciseList!![currentExercisePosition].setIsSelected(true)
            exerciseAdapter!!.notifyDataSetChanged()

            setupExerciseView()
        }
    }.start()
    }

    //    5. SET EXERCISE TIMER DAN BINDING THE TIMER
    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress
//    15: add the exercisetTimerDuration in CountDownTimer: dikali 1000 untuk mempercepat durasi testing
        exerciseTimer = object: CountDownTimer(exerciseTimerDuration*1000, 1000){ // 30 detik dengan interval 1 detik
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress =30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString() // hitung mundur
            }
            override fun onFinish() {
                //            14 add this code:
//                15: move this code to the below of if statement:

//                Toast.makeText(this@ExerciseActivity, "30 seconds are over, lets go to the rest view.", Toast.LENGTH_SHORT ).show()
//                7 add
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsSelected(true) // current exercise is completed
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else{
//                    Toast.makeText(this@ExerciseActivity, "Congratulation! You have completed the 7 minutes workout.", Toast.LENGTH_SHORT ).show()
//                    15: add this code to finish:
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }
// 4.
    override fun onDestroy() {
        super.onDestroy()
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
//    5.
    if(exerciseTimer != null){
        exerciseTimer?.cancel()
        exerciseProgress = 0
    }
//    9: add this code: SHUTTING DOWN THE TEXT TO SPEECH FEATURE WHEN ACTIVITY IS DESTROYED
//    START
    if (tts != null){
        tts!!.stop()
        tts!!.shutdown()
    }

//    10: add this code
    if(player != null){
        player!!.stop()
    }

//        4.
        binding = null
    }

    //    9: add this code: untuk output
    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US) // BAHASANYA ENGLISH US
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){ // PERIKSA APAAKAH SUARANYA ADA ATAU TIDAK
               Log.e("TTS", "The Language specified is not supported")
            } else{
                Log.e("TTS", "Initialization Failed!")
            }
        }
    }

//    9: add this code: untuk output
    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")

    }

}