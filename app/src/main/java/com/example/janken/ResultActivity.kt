package com.example.janken

import android.app.Instrumentation
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.example.janken.databinding.ActivityMainBinding
import com.example.janken.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    val gu = 0
    val choki = 1
    val pa = 2



    private lateinit var binding:ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("myhand",0)

        val my_hand : Int

        my_hand = when(id) {
            R.id.gu -> {
                binding.comhand.setImageResource(R.drawable.gu2)
                gu
            }
            R.id.choki -> {
                binding.myhand.setImageResource(R.drawable.choki2)
                choki
            }
            R.id.pa -> {
                binding.myhand.setImageResource(R.drawable.pa2)
                pa
            }
            else -> gu
        }

        //コンピューターの手を決める
        //val com_hand = (Math.random() * 3).toInt()
        val com_hand = getHand()


        when(com_hand){
            gu -> binding.comhand.setImageResource(R.drawable.gu2)

            choki ->binding.comhand.setImageResource(R.drawable.choki2)

            pa ->binding.comhand.setImageResource(R.drawable.pa2)

        }

        val Result = (com_hand - my_hand + 3) % 3
        when(Result){
            0 -> binding.result.setText(R.string.draw)
            1 -> binding.result.setText(R.string.win)
            2 -> binding.result.setText(R.string.lose)
        }
        binding.button.setOnClickListener { finish() }

        //じゃんけん結果を保存
        saveData(my_hand,com_hand,Result)
    }

    //result の結果 0はあいこ 1は負け 2は勝ち
    private fun saveData(myhand:Int, comhand: Int, result: Int ){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT" , 0 )
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT" , 0)
        val lastComHand = pref.getInt("GAME_RESULT" , 0)
        val lastGameResult = pref.getInt("GAME_RESULT" , -1)

        val edtWinningStreakCount: Int = when{
            lastGameResult == 2 && result == 2 -> winningStreakCount+1
            else -> 0
        }

        val editor = pref.edit()
        editor.putInt("GAME_COUNT" , gameCount+1)
            .putInt("WINNING_COUNT", edtWinningStreakCount)
            .putInt("LAST_MY_HAND", myhand)
            .putInt("LAST_COM_HAND", comhand)
            .putInt("BEFORE_LAST_COM_HAND", lastComHand)
            .putInt("GAME_RESULT", result)
            .apply()
    }
    private fun getHand():Int{
        var hand = (Math.random()*3).toInt()
        var pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT" , 0 )
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT" , 0)
        val lastMyHand = pref.getInt("LAST_MY_HAND", 0)
        val lastComHand = pref.getInt("GAME_RESULT" , 0)
        val beforeLastComHand = pref.getInt("BEFORE_LAST_COM_HAND", 0)
        val gameResult = pref.getInt("GAME_RESULT" , -1)

        if(gameCount == 1){
            if(gameResult == 2){
                //前回の勝負が１回目でCOMが勝利の場合、次のCOMの手を変更する
                while (lastComHand == hand){
                    hand = (Math.random()*3).toInt()
                }
            }else if(gameResult == 1){
                //前回の勝負が１回目でCOMが負けた場合、相手の出した手に勝つ手を出す
                hand = (lastMyHand -1 + 3)% 3
            }
        }else if(winningStreakCount > 0){
            if(beforeLastComHand == lastComHand){
                //同じ手で勝利した場合は、手を変える
                while (lastComHand == hand){
                    hand = (Math.random()*3).toInt()
                }
            }
        }

        return hand
    }


}