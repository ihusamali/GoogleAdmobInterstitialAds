package com.example.googleadmobinterstitialads

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivityTag"
    lateinit var btn: Button
    lateinit var progressBar: ProgressBar


    lateinit var handler: Handler
    lateinit var runnable: java.lang.Runnable

    var isAdLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.button)
//        progressBar = findViewById(R.id.progress_circular)

        loadInterstitialAd()

        btn.setOnClickListener {
            Toast.makeText(this, "button pressed", Toast.LENGTH_SHORT).show()

            waitForAdToLoadAndThenMoveNext()
        }
    }



    private fun waitForAdToLoadAndThenMoveNext() {
//        Toast.makeText(this, "wait fun", Toast.LENGTH_SHORT).show()

        handler = Handler(Looper.getMainLooper())
        Toast.makeText(this, "handler", Toast.LENGTH_SHORT).show()

        runnable = Runnable {
            Toast.makeText(this, "runnable", Toast.LENGTH_SHORT).show()

//            progressBar.progress = progressBar.progress.plus(1)
            if (mInterstitialAd == null && isAdLoading) {
                runnable.let { handler.postDelayed(it, 1) }
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
            } else {
                //ad not loading, check if ad is loaded or ad load failed and handle the cases accordingly

                if (mInterstitialAd != null) {
                    Toast.makeText(this, "show ad", Toast.LENGTH_SHORT).show()

                    showAd()
                }
                else{
                    Toast.makeText(this, "next activity", Toast.LENGTH_SHORT).show()

                    val intent  = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                }


            }
        }
        handler.postDelayed(runnable, 1)

    }

        private fun showAd() {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    super.onAdClicked()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()

                    val intent = Intent(this@MainActivity, MainActivity2::class.java)
                    startActivity(intent)
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                }
            }

            mInterstitialAd?.show(this)


        }

        private fun loadInterstitialAd() {
            isAdLoading = true
            var adRequest = AdRequest.Builder().build()

            InterstitialAd.load(
                this,
                "ca-app-pub-3940256099942544/1033173712",
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        adError.toString().let { Log.d(TAG, it) }
                        mInterstitialAd = null
                        isAdLoading = false
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d(TAG, "Ad was loaded.")
                        mInterstitialAd = interstitialAd
                        isAdLoading = false
                    }

                })

        }
    }
