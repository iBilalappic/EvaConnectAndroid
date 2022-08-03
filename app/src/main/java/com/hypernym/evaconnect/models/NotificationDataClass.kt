package com.hypernym.evaconnect.models

import com.google.gson.annotations.SerializedName

data class NotificationDataClass(
        @SerializedName("calendar_reminder")
        var calendarReminder: Int = 0, // 0
        @SerializedName("connection_requests")
        var connectionRequests: Int = 0, // 0
        @SerializedName("meeting_reminders")
        var meetingReminders: Int = 0, // 0
        var message: Int = 0, // 0
        @SerializedName("new_events")
        var newEvents: Int = 0, // 0
        @SerializedName("new_job_post")
        var newJobPost: Int = 0, // 0
        @SerializedName("news_update")
        var newsUpdate: Int = 0, // 0
        @SerializedName("post_comments")
        var postComments: Int = 0, // 0
        @SerializedName("post_likes")
        var postLikes: Int = 0, // 0
        @SerializedName("post_updates")
        var postUpdates: Int = 0, // 0
        @SerializedName("profile_views")
        var profileViews: Int = 0, // 1
        @SerializedName("suggested_connections")
        var suggestedConnections: Int = 0, // 1

)
