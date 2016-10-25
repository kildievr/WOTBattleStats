package ru.rustam


import org.joda.time.format.DateTimeFormat

/**
  * Created by Rustam on 21.10.2016.
  */
case class WotData(
               val xp: Int,
               val cap: Int,
               val defBase: Int,
               val map: String,
               val frag: Int,
               val gold: Int,
               val hits: Int,
               val spot: Int,
               val tier: Int,
               val idNum: Int,
               val place: Int,
               val shots: Int,
               val assist: Int,
               val damage: Int,
               val freeXP: Int,
               val result: Int,
               val credits: Int,
               val pierced: Int,
               val vehicle: String,
               val battleTier: Int,
               val battleTime: String,
               val battleType: Int,
               val originalXP: Int,
               val playerName: String,
               val assistRadio: Int,
               val assistTrack: Int,
               val playerAccount: String
             )
{
  lazy val epochTime = DateTimeFormat.forPattern("dd.MM.YYYY HH:mm:ss").parseDateTime(battleTime).getMillis()/1000
}

