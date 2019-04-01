package com.ezrs.feature

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.media.ImageReader
import android.media.projection.MediaProjectionManager
import android.support.v4.app.ActivityCompat.startActivity
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import com.bsk.floatingbubblelib.FloatingBubbleTouchListener
import com.ezrs.feature.R.id.image


class BubbleUtils(var context: Context,var root: View): FloatingBubbleTouchListener  {


    override fun onUp(x: Float, y: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRemove() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMove(x: Float, y: Float) {
        TODO(reason = "not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDown(x: Float, y: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTap(expanded: Boolean) {
        drawScrShoot(makeScreenShot())
    }

    fun drawScrShoot(bitmap: Bitmap) {
        var imageView : ImageView= root.findViewById(R.id.imageViewTest) as ImageView
        imageView.setImageBitmap(bitmap)
    }



    fun makeScreenShot(): Bitmap
    {
        val manager = getSystemService(context,MediaProjectionManager::class.java)
        var inte = manager!!.createScreenCaptureIntent()


//        startActivityForResult(start,manager.createScreenCaptureIntent(), 100);
        var mp = manager.getMediaProjection(-1,inte)

        var mImageReader = ImageReader.newInstance(400, 400, ImageFormat.RGB_565, 1);


        var vd = mp.createVirtualDisplay("test",400,400,300,0,mImageReader.surface,null,null)

        var image = mImageReader.acquireLatestImage()
        val planes = image.getPlanes()
        val buffer = planes[0].getBuffer().rewind()
        val pixelStride = planes[0].getPixelStride()
        val rowStride = planes[0].getRowStride()
        val rowPadding = rowStride - pixelStride * 400
        // create bitmap
        val bitmap = Bitmap.createBitmap(400 + rowPadding / pixelStride, 400, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        vd.release();
        mp.stop();
        mImageReader = null
        image.close()
        return bitmap
    }


}
