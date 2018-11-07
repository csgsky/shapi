package com.allen.shapi.task.task2

import android.media.AudioFormat
import android.media.AudioRecord
import android.util.Log
import java.io.FileInputStream
import java.io.FileOutputStream

class PcmToWavUtil(sampleRate: Int, channel: Int, encoding: Int) {
    /**
     * 缓存的音频大小
     */
    private var bufferSize: Int = 0
    /**
     * 采样率
     */
    private var channel: Int = 0
    /**
     * 声道数
     */
    private var sampleRate: Int = 0

    /**
     * 初始化
     */
    init {
        this.channel = channel
        this.sampleRate = sampleRate
        this.bufferSize = AudioRecord.getMinBufferSize(sampleRate, channel, encoding)
    }

    /**
     * pcm 转 wav 文件
     */
    fun pcmToWav(inFileName: String, outFileName: String) {
        var inputStream: FileInputStream?
        var outputStream: FileOutputStream?
        var totalAudioLen: Long?
        var totalDataLen: Long?
        var longSampleRate = sampleRate
        var channels = if (channel == AudioFormat.CHANNEL_IN_MONO)
            1 else 2
        var byteRate = 16 * sampleRate * channels / 8
        val data = ByteArray(bufferSize)
        try {
            inputStream = FileInputStream(inFileName)
            outputStream = FileOutputStream(outFileName)
            totalAudioLen = inputStream.channel.size()
            totalDataLen = totalAudioLen + 36

            writeWaveFileHeader(outputString = outputStream,
                    totalAudioLen = totalAudioLen,
                    totalDataLen = totalDataLen,
                    longSampleRate = longSampleRate.toLong(),
                    channels = channels,
                    byteRate = byteRate.toLong())

            // 写入文件
            while (inputStream.read(data) != -1) {
                outputStream.write(data)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun writeWaveFileHeader(outputString: FileOutputStream,
                                    totalAudioLen: Long,
                                    totalDataLen: Long,
                                    longSampleRate: Long,
                                    channels: Int,
                                    byteRate: Long) {
        val header = ByteArray(44)
        header[0] = 'R'.toByte()
        header[1] = 'I'.toByte()
        header[2] = 'F'.toByte()
        header[3] = 'F'.toByte()

        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()

        //WAVE
        header[8] = 'W'.toByte()
        header[9] = 'A'.toByte()
        header[10] = 'V'.toByte()
        header[11] = 'E'.toByte()
        // 'fmt ' chunk
        header[12] = 'f'.toByte()
        header[13] = 'm'.toByte()
        header[14] = 't'.toByte()
        header[15] = ' '.toByte()
        // 4 bytes: size of 'fmt ' chunk
        header[16] = 16
        header[17] = 0
        header[18] = 0
        header[19] = 0
        // format = 1
        header[20] = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (longSampleRate and 0xff).toByte()
        header[25] = (longSampleRate shr 8 and 0xff).toByte()
        header[26] = (longSampleRate shr 16 and 0xff).toByte()
        header[27] = (longSampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        // block align
        header[32] = (2 * 16 / 8).toByte()
        header[33] = 0
        // bits per sample
        header[34] = 16
        header[35] = 0
        //data
        header[36] = 'd'.toByte()
        header[37] = 'a'.toByte()
        header[38] = 't'.toByte()
        header[39] = 'a'.toByte()
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = (totalAudioLen shr 8 and 0xff).toByte()
        header[42] = (totalAudioLen shr 16 and 0xff).toByte()
        header[43] = (totalAudioLen shr 24 and 0xff).toByte()
        outputString.write(header, 0, 44)
        Log.d("WavHeader", header.toString());
    }


}