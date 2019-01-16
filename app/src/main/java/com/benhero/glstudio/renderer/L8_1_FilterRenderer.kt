package com.benhero.glstudio.renderer

import android.content.Context
import com.benhero.glstudio.R
import com.benhero.glstudio.base.BaseRenderer
import com.benhero.glstudio.filter.*
import com.benhero.glstudio.util.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 滤镜渲染
 *
 * @author Benhero
 */
class L8_1_FilterRenderer(context: Context) : BaseRenderer(context) {
    val filterList = ArrayList<BaseFilter>()
    var drawIndex = 0
    var isChanged = false
    var currentFilter: BaseFilter
    var textureBean: TextureHelper.TextureBean? = null

    init {
        filterList.add(BaseFilter(context))
        filterList.add(InverseFilter(context))
        filterList.add(GrayFilter(context))
        filterList.add(LightUpFilter(context))
        filterList.add(ScaleFilter(context))
        currentFilter = filterList.get(0)
    }

    override fun onSurfaceCreated(glUnused: GL10, config: EGLConfig) {
        currentFilter.onCreated()
        textureBean = TextureHelper.loadTexture(context, R.drawable.pikachu)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
        currentFilter.onSizeChanged(width, height)
        currentFilter.textureBean = textureBean
    }

    override fun onDrawFrame(glUnused: GL10) {
        if (isChanged) {
            currentFilter = filterList[drawIndex]

            filterList.forEach {
                if (it != currentFilter) {
                    it.onDestroy()
                }
            }

            currentFilter.onCreated()
            currentFilter.onSizeChanged(outputWidth, outputHeight)
            currentFilter.textureBean = textureBean
            isChanged = false
        }

        currentFilter.onDraw()
    }

    override fun onClick() {
        super.onClick()
        drawIndex++
        drawIndex = if (drawIndex >= filterList.size) 0 else drawIndex
        isChanged = true
    }
}