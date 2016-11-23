package ru.rustam


import org.joda.time.format.DateTimeFormat


/**
  * Created by Rustam on 21.10.2016.
  */
case class WotData(
                    xp: Int,
                    cap: Int,
                    defBase: Int,
                    map: String,
                    frag: Int,
                    gold: Int,
                    hits: Int,
                    spot: Int,
                    tier: Int,
                    idNum: Int,
                    place: Int,
                    shots: Int,
                    assist: Int,
                    damage: Int,
                    freeXP: Int,
                    result: Int,
                    credits: Int,
                    pierced: Int,
                    vehicle: String,
                    battleTier: Int,
                    battleTime: String,
                    battleType: Int,
                    originalXP: Int,
                    playerName: String,
                    assistRadio: Int,
                    assistTrack: Int,
                    playerAccount: String
                  ) {
  lazy val epochTime = DateTimeFormat.forPattern("dd.MM.YYYY HH:mm:ss").parseDateTime(battleTime).getMillis / 1000
}

