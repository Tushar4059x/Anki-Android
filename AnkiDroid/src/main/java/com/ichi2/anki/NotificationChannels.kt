/****************************************************************************************
 * Copyright (c) 2018 Timothy Rae <perceptualchaos2@gmail.com>                          *
 * Copyright (c) 2018 Mike Hardy <mike@mikehardy.net>                                   *
 *                                                                                      *
 * This program is free software; you can redistribute it and/or modify it under        *
 * the terms of the GNU General Public License as published by the Free Software        *
 * Foundation; either version 3 of the License, or (at your option) any later           *
 * version.                                                                             *
 *                                                                                      *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY      *
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A      *
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.             *
 *                                                                                      *
 * You should have received a copy of the GNU General Public License along with         *
 * this program.  If not, see <http://www.gnu.org/licenses/>.                           *
 ****************************************************************************************/

package com.ichi2.anki

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import timber.log.Timber

object NotificationChannels {
    /**
     * Create or update all the notification channels for the app
     *
     * In Oreo and higher, you must create a channel for all notifications.
     * This will create the channel if it doesn't exist, or if it exists it will update the name.
     *
     * Note that once a channel is created, only the name may be changed as long as the application
     * is installed on the user device. All other settings are fully under user control.

     * TODO should be called in response to [Intent.ACTION_LOCALE_CHANGED]
     * @param context the context for access to localized strings for channel names
     */
    @RequiresApi(26)
    fun setup(context: Context) {
        val res = context.resources
        for (channel in Channel.entries) {
            val id = channel.id
            val name = channel.getName(res)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            Timber.i("Creating notification channel with id/name: %s/%s", id, name)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(id, name, importance)
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            manager.createNotificationChannel(notificationChannel)
        }
    }
}

enum class Channel(
    val id: String,
    @StringRes val nameId: Int,
) {
    GENERAL("General Notifications", R.string.app_name),
    SYNC("Synchronization", R.string.sync_title),
    GLOBAL_REMINDERS("Global Reminders", R.string.widget_minimum_cards_due_notification_ticker_title),
    DECK_REMINDERS("Deck Reminders", R.string.deck_conf_reminders),
    ;

    fun getName(res: Resources) = res.getString(nameId)
}
