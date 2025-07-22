package com.example.fitstepo

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import java.io.File // ← ДОДАНО: потрібно для роботи з шляхом

object AvatarUtils {

    @JvmStatic
    fun loadUserAvatar(context: Context, email: String?, imageView: ImageView) {
        if (email.isNullOrBlank()) {
            Log.w("AvatarUtils", "Email is null or blank")
            imageView.setImageResource(R.drawable.default_avatar)
            return
        }

        val dbHelper = DBHelper(context)
        val user = dbHelper.getUserByEmail(email)

        val avatar = user?.avatar
        if (avatar.isNullOrBlank()) {
            Log.w("AvatarUtils", "User avatar is null or empty for email: $email")
            imageView.setImageResource(R.drawable.default_avatar)
            return
        }

        setAvatarFromUri(context, imageView, avatar)
    }

    @JvmStatic
    fun setAvatarFromUri(context: Context, imageView: ImageView, avatarUri: String) {
        try {
            if (avatarUri.startsWith("content://") || avatarUri.startsWith("file://") || avatarUri.startsWith("/")) {
                // Якщо це абсолютний шлях або URI
                val file = File(avatarUri)
                if (file.exists()) {
                    imageView.setImageURI(Uri.fromFile(file))
                    Log.d("AvatarUtils", "Loaded avatar from file: $avatarUri")
                } else {
                    imageView.setImageResource(R.drawable.default_avatar)
                    Log.w("AvatarUtils", "File not found: $avatarUri")
                }
            } else {
                // Інакше — ім’я ресурсу у drawable
                val resId = context.resources.getIdentifier(avatarUri, "drawable", context.packageName)
                if (resId != 0) {
                    imageView.setImageResource(resId)
                    Log.d("AvatarUtils", "Loaded avatar from drawable: $avatarUri")
                } else {
                    imageView.setImageResource(R.drawable.default_avatar)
                    Log.w("AvatarUtils", "Drawable not found for avatar: $avatarUri")
                }
            }
        } catch (e: Exception) {
            imageView.setImageResource(R.drawable.default_avatar)
            Log.e("AvatarUtils", "Error loading avatar: $avatarUri", e)
        }
    }

    @JvmStatic
    fun setAvatarFromPathOrDefault(context: Context, imageView: ImageView, path: String?) {
        if (!path.isNullOrEmpty()) {
            if (path == "default_avatar") {
                imageView.setImageResource(R.drawable.default_avatar)
            } else {
                val file = File(path)
                if (file.exists()) {
                    imageView.setImageURI(Uri.fromFile(file))
                } else {
                    imageView.setImageResource(R.drawable.default_avatar)
                }
            }
        } else {
            imageView.setImageResource(R.drawable.default_avatar)
        }
    }
}
// виклик
// AvatarUtils.setAvatarFromPathOrDefault(this, imgAvatar, user.avatar)