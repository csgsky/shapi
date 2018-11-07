package com.allen.shapi.task.task2

import android.Manifest
import android.content.pm.PackageManager
import android.media.*
import android.os.Build
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.allen.shapi.R
import com.allen.shapi.base.BaseA
import com.allen.shapi.constant.Constant
import kotlinx.android.synthetic.main.activity_two_task.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class TaskTwoActivity : BaseA(), View.OnClickListener {

    private val TAG: String = TaskTwoActivity::class.java.name

    private val MY_REQUEST_PERMISSION = 1001

    private val permissions = listOf(android.Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    // 被用户拒绝的权限列表
    private val rejectPermissionList = arrayListOf<String>()
    private var inputStream: FileInputStream? = null
    private var isRecording = false
    private var audioRecord: AudioRecord? = null
    private var audioTrack: AudioTrack? = null


    override fun attachLayoutRes(): Int = R.layout.activity_two_task

    override fun initView() {
        initToolbar(toolbar = toolbar, homeAsUpEnabled = true, title = "采集和播放音频")
        btn_control.setOnClickListener(this)
        btn_convert.setOnClickListener(this)
        btn_play.setOnClickListener(this)
        checkPermission()
    }

    private fun startRecord() {
        val minBufferSize = AudioRecord.getMinBufferSize(Constant.SAMPLE_RATE_INHZ, Constant.CHANNEL_CONFIG, Constant.AUDIO_FORMAT)
        audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, Constant.SAMPLE_RATE_INHZ, Constant.CHANNEL_CONFIG, Constant.AUDIO_FORMAT, minBufferSize)
        val data = ByteArray(minBufferSize)
        val file = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm")
        if (!file.mkdir()) {
            Log.i(TAG, "Directory not created")
        }
        if (file.exists()) {
            file.delete()
        }

        audioRecord!!.startRecording()
        isRecording = true
        Thread(Runnable {
            run {
                var os: FileOutputStream? = null
                try {
                    os = FileOutputStream(file)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (os != null) {
                    while (isRecording) {
                        val read = audioRecord!!.read(data, 0, minBufferSize)
                        if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                            try {
                                os.write(data)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    try {
                        Log.i(TAG, "run: close file output stream !")
                        os.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }).start()
    }

    /**
     * 停止录音
     */
    public fun stopRecord() {
        isRecording = false
        // 释放资源
        if (audioRecord != null) {
            audioRecord!!.stop()
            audioRecord!!.release()
            audioRecord = null
        }
    }

    /***
     * stream 播放
     */
    private fun playInModeStream() {
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val minBufferSize = AudioTrack.getMinBufferSize(Constant.SAMPLE_RATE_INHZ, channelConfig, Constant.AUDIO_FORMAT);
        audioTrack = AudioTrack(
                AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(),
                AudioFormat.Builder().setSampleRate(Constant.SAMPLE_RATE_INHZ)
                        .setEncoding(Constant.AUDIO_FORMAT)
                        .setChannelMask(channelConfig).build(),
                minBufferSize,
                AudioTrack.MODE_STREAM,
                AudioManager.AUDIO_SESSION_ID_GENERATE
        )
        audioTrack!!.play()
        val file = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm")
        try {
            inputStream = FileInputStream(file)
            Thread(Runnable {
                run {
                    try {
                        var tempBuffer = ByteArray(minBufferSize)
                        while (inputStream!!.available() > 0) {
                            val readCount = inputStream!!.read(tempBuffer)
                            if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                                continue
                            }
                            if (readCount != 0 && readCount != -1) {
                                audioTrack!!.write(tempBuffer, 0 ,readCount)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }).start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_REQUEST_PERMISSION) {
            for (i in 0 until grantResults.size step 1) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, permissions[i] + " 权限被用户禁止！")
                }
            }
        }
    }


    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (i in 0 until permissions.size step 1) {
                if (ContextCompat.checkSelfPermission(applicationContext, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    rejectPermissionList.add(permissions[i])
                }
            }
            if (rejectPermissionList.isNotEmpty()) {
                val permission = rejectPermissionList.toTypedArray()
                ActivityCompat.requestPermissions(this, permission, MY_REQUEST_PERMISSION)

            }
        }
    }

    private fun stopPlay() {
        if (audioTrack != null) {
            Log.d(TAG, "Stopping")
            audioTrack?.stop()
            Log.d(TAG, "Releasing")
            audioTrack?.release()
            Log.d(TAG, "Nulling")
        }
    }



    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_control -> {
                if (btn_control.text.toString().equals(getString(R.string.start_record))) {
                    btn_control.text = getString(R.string.stop_record)
                    startRecord()
                } else {
                    btn_control.text = getString(R.string.start_record)
                    stopRecord()
                }
            }
            R.id.btn_convert -> {
                val pcmToWavUtil = PcmToWavUtil(Constant.SAMPLE_RATE_INHZ, Constant.CHANNEL_CONFIG, Constant.AUDIO_FORMAT)
                val pcmFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.pcm")
                val wavFile = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "test.wav")
                if (!wavFile.mkdirs()) {
                    Log.e(TAG, "wavFile Directory not created")
                }
                if (wavFile.exists()) {
                    wavFile.delete()
                }
                pcmToWavUtil.pcmToWav(pcmFile.absolutePath, wavFile.absolutePath)
            }
            R.id.btn_play -> {
                if (btn_play.text.toString().equals(getString(R.string.start_play))) {
                    btn_play.text = getString(R.string.stop_play)
                    playInModeStream()
                } else {
                    btn_play.text = getString(R.string.start_play)
                    stopPlay()
                }
            }
        }
    }


}

