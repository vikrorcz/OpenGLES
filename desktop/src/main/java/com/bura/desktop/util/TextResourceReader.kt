package com.bura.desktop.util

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

class TextResourceReader {
    companion object {
        fun readTextFileFromResource(
            resourceId: String
        ): String {
            val body = StringBuilder()
            try {

                val inputStream = FileInputStream(resourceId)
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var nextLine: String?
                while (bufferedReader.readLine().also { nextLine = it } != null) {
                    body.append(nextLine)
                    body.append('\n')
                }
            } catch (e: IOException) {
                throw RuntimeException(
                    "Could not open resource: $resourceId", e
                )
            }
            return body.toString()
        }
    }
}