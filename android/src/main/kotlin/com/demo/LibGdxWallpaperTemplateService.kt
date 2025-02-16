package com.demo

import android.util.Log
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService
import com.badlogic.gdx.backends.android.AndroidWallpaperListener
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils


class LibGdxWallpaperTemplateService : AndroidLiveWallpaperService() {

   companion object {
      private const val TAG = "com.demo.LibGdxWallpaperTemplateService"
   }

   override fun onCreateApplication() {
      super.onCreateApplication()

      Log.i( TAG, "Hello, world!" )

      val config = AndroidApplicationConfiguration()
      val listener: ApplicationListener = MyLiveWallpaperListener()

      initialize(listener, config)
   }

   class MyLiveWallpaperListener : AndroidWallpaperListener, ApplicationListener {
      private var scene: WallpaperScene? = null

      override fun offsetChange(
         xOffset: Float,
         yOffset: Float,
         xOffsetStep: Float,
         yOffsetStep: Float,
         xPixelOffset: Int,
         yPixelOffset: Int
      ) {
         Log.i(TAG,"AndroidWallpaperListener: offsetChange(xOffset:$xOffset yOffset:$yOffset xOffsetSteep:$xOffsetStep yOffsetStep:$yOffsetStep xPixelOffset:$xPixelOffset yPixelOffset:$yPixelOffset)")
      }

      override fun previewStateChange(isPreview: Boolean) {
         Log.i(TAG, "AndroidWallpaperListener: previewStateChange(isPreview:$isPreview)")
      }

      override fun iconDropped(x: Int, y: Int) {
         Log.i(TAG, "AndroidWallpaperListener:  iconDropped ($x, $y)")
      }

      override fun create() {

         scene = WallpaperScene();
      }

      override fun resize(width: Int, height: Int) {
      }

      override fun render() {
         ScreenUtils.clear(0f, 0f, 1f, 1f)
         scene!!.render()
      }

      override fun pause() {
      }

      override fun resume() {
         scene!!.reset()
      }

      override fun dispose() {
         Log.i(TAG, "ApplicationListener: dispose")
      }
   }
}
