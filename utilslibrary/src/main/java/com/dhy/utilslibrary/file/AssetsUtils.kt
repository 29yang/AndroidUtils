package com.dhy.utilslibrary.file

import android.content.Context
import android.content.res.AssetManager
import com.dhy.utilslibrary.logger.LoggerUtil
import java.io.*

/**
 * Created by dhy
 * Date: 2020/11/27
 * Time: 16:46
 * describe:
 */
class AssetsUtils {
    companion object {
        private val TAG = AssetsUtils::class.java.simpleName

        /**
         * 从assets目录中复制整个文件夹内容
         *
         * @param context Context 使用CopyFiles类的Activity
         * @param oldPath String  原文件路径  如：/aa
         * @param newPath String  复制后路径  如：xx:/bb/cc
         */
        fun copyFilesAssets(
            context: Context,
            oldPath: String,
            newPath: String
        ) {
            try {
                val fileNames =
                    context.assets.list(oldPath) //获取assets目录下的所有文件及目录名
                if (fileNames != null && fileNames.isNotEmpty()) { //如果是目录
                    val file = File(newPath)
                    file.mkdirs() //如果文件夹不存在，则递归
                    for (fileName in fileNames) {
                        copyFilesAssets(context, "$oldPath/$fileName", "$newPath/$fileName")
                    }
                } else { //如果是文件
                    val inputStream = context.assets.open(oldPath)
                    val fos =
                        FileOutputStream(File(newPath))
                    val buffer = ByteArray(1024)
                    var byteCount = 0
                    while (inputStream.read(buffer)
                            .also { byteCount = it } != -1
                    ) { //循环从输入流读取 buffer字节
                        fos.write(buffer, 0, byteCount) //将读取的输入流写入到输出流
                    }
                    fos.flush() //刷新缓冲区
                    inputStream.close()
                    fos.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * 读取assets目录下json文件内容
         */
        fun getJson(
            fileName: String,
            context: Context
        ): String {
            //将json数据变成字符串
            val stringBuilder = StringBuilder()
            try {
                //获取assets资源管理器
                val assetManager = context.assets
                //通过管理器打开文件并读取
                val bf = BufferedReader(
                    InputStreamReader(
                        assetManager.open(fileName)
                    )
                )
                var line: String?
                while (bf.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return stringBuilder.toString()
        }

        /**
         * 从assets目录中复制整个文件夹内容
         * 参考 [.copyAssetsFolder]
         */
        fun copyAssetsFolder(
            context: Context,
            assetsRelPath: String,
            newPath: String
        ) {
            copyAssetsFolder(null, context, assetsRelPath, newPath)
        }

        /**
         * 从assets目录中复制整个文件夹内容
         *
         * @param assetManager  外部传入 AssetManager(如espresso),若为null,则使用参数 context 自动获取
         * @param context       Context 使用CopyFiles类的Activity
         * @param assetsRelPath String  assets原目录/文件的相对路径  如：aa,表示 assets/aa/ 目录 或者 assets/aa 文件
         * @param newPath       String  复制后路径  如：xx:/bb/cc
         */
        fun copyAssetsFolder(
            assetManager: AssetManager?,
            context: Context,
            assetsRelPath: String, newPath: String
        ) {
            var assetManager = assetManager
            var assetsRelPath = assetsRelPath
            val len = assetsRelPath.length
            if (len == 0) {
                LoggerUtil.e(TAG, "copyAssetsFolder fail as srcFile path is empty,newPath:$newPath")
                return
            }
            if (assetManager == null) {
                assetManager = context.assets
            }
            try {
                if (assetsRelPath.endsWith("/")) { // 带分隔符会被识别为文件,而非目录,导致报错
                    assetsRelPath = assetsRelPath.substring(0, len - 1)
                }
                val fileNames =
                    assetManager!!.list(assetsRelPath) //获取assets目录下的所有文件及目录名
                if (fileNames != null && fileNames.isNotEmpty()) { //如果是目录
                    val file = File(newPath)
                    file.mkdirs() //如果文件夹不存在，则递归
                    for (fileName in fileNames) {
                        copyAssetsFolder(
                            assetManager,
                            context,
                            "$assetsRelPath/$fileName",
                            "$newPath/$fileName"
                        )
                    }
                } else { //如果是文件
                    val inputStream = assetManager.open(assetsRelPath)
                    val fos = FileOutputStream(File(newPath))
                    val buffer = ByteArray(1024)
                    var byteCount = 0
                    while (inputStream.read(buffer)
                            .also { byteCount = it } != -1
                    ) { //循环从输入流读取 buffer字节
                        fos.write(buffer, 0, byteCount) //将读取的输入流写入到输出流
                    }
                    fos.flush() //刷新缓冲区
                    inputStream.close()
                    fos.close()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                LoggerUtil.w(TAG, "复制asset文件出错: $assetsRelPath${e.message}".trimIndent())
            }
        }
    }


}