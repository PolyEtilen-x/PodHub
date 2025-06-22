package com.example.podhub.data

import com.example.podhub.models.Podcast

object SamplePodcasts {

    val podcastList = listOf(
        Podcast(
            id = "1",
            title = "Mindful Moments",
            author = "ZenCast",
            imageUrl = "https://i.imgur.com/abc123.png",
            audioUrl = "https://www.sample-videos.com/audio/mp3/wave.mp3"
        ),
        Podcast(
            id = "2",
            title = "Night Lo-Fi",
            author = "LoFi Studio",
            imageUrl = "https://i.imgur.com/def456.png",
            audioUrl = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
        ),
        Podcast(
            id = "3",
            title = "Deep Focus Beats",
            author = "StudyFlow",
            imageUrl = "https://i.imgur.com/ghi789.png",
            audioUrl = "https://www.sample-videos.com/audio/mp3/india-national-anthem.mp3"
        ),
        Podcast(
            id = "4",
            title = "Coffee & Jazz",
            author = "RelaxHouse",
            imageUrl = "https://i.imgur.com/jkl987.png",
            audioUrl = "https://www.sample-videos.com/audio/mp3/rock.mp3"
        )
    )
}
