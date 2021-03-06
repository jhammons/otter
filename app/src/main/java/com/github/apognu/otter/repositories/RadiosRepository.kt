package com.github.apognu.otter.repositories

import android.content.Context
import com.github.apognu.otter.R
import com.github.apognu.otter.utils.FunkwhaleResponse
import com.github.apognu.otter.utils.Radio
import com.github.apognu.otter.utils.RadiosCache
import com.github.apognu.otter.utils.RadiosResponse
import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader

class RadiosRepository(override val context: Context?) : Repository<Radio, RadiosCache>() {
  override val cacheId = "radios"
  override val upstream = HttpUpstream<Radio, FunkwhaleResponse<Radio>>(HttpUpstream.Behavior.Progressive, "/api/v1/radios/radios/", object : TypeToken<RadiosResponse>() {}.type)

  override fun cache(data: List<Radio>) = RadiosCache(data)
  override fun uncache(reader: BufferedReader) = gsonDeserializerOf(RadiosCache::class.java).deserialize(reader)

  override fun onDataFetched(data: List<Radio>): List<Radio> {
    return data
      .map { radio ->
        radio.apply { radio_type = "custom" }
      }
      .toMutableList()
      .apply {
        context?.let { context ->
          add(0, Radio(0, "random", context.getString(R.string.radio_random_title), context.getString(R.string.radio_random_description)))
          add(1, Radio(0, "less-listened", context.getString(R.string.radio_less_listened_title), context.getString(R.string.radio_less_listened_description)))
        }
      }
  }
}