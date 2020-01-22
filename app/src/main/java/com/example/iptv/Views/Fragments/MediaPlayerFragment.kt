package com.example.iptv.Views.Fragments

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.iptv.Models.Channel

import com.example.iptv.R
import com.example.iptv.ViewModels.MediaPlayerViewModel
import com.example.iptv.Views.Adapters.MoviesAdapters
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.empty_layout.*
import kotlinx.android.synthetic.main.media_player_fragment.*

class MediaPlayerFragment : Fragment(), Player.EventListener {

    companion object {
        fun newInstance() = MediaPlayerFragment()
    }

    private lateinit var viewModel: MediaPlayerViewModel
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var moviesAdapters: MoviesAdapters

    private var movieList: MutableList<Channel>? = null

    private var playbackPosition = 0L
    private val dashUrl = "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd"

    private val bandwidthMeter by lazy { DefaultBandwidthMeter() }
    private val adaptiveTrackSelection by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.media_player_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieList = getDataDummy()
        moviesAdapters = MoviesAdapters(movieList)
        moviesAdapters.setOnItemClickListener{
            adapter, view, position ->
            Toast.makeText(requireContext(), movieList!![position].name, Toast.LENGTH_SHORT).show()
        }
        moviesAdapters.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        rv_movies.adapter = moviesAdapters
        rv_movies.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
    }

    override fun onStart() {
        super.onStart()
        initialExoPlayer()
    }

    override fun onStop() {
        releaseExoPlayer()
        super.onStop()
    }



    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING) {
            progressBar.visibility = View.VISIBLE
        } else if (playbackState == Player.STATE_READY) {
            progressBar.visibility = View.GONE
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultHttpDataSourceFactory("ua", bandwidthMeter)
        val dashChuckFactory = DefaultDashChunkSource.Factory(dataSourceFactory)
        return DashMediaSource(uri, dataSourceFactory, dashChuckFactory, null, null)
    }

    private fun prepareExoPlayer() {
        val uri = Uri.parse(dashUrl)
        val mediaSource = buildMediaSource(uri)
        simpleExoPlayer.prepare(mediaSource)
    }

    private fun initialExoPlayer() {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
            requireContext(),
            DefaultRenderersFactory(requireContext()),
            DefaultTrackSelector(adaptiveTrackSelection),
            DefaultLoadControl()
        )
        prepareExoPlayer()
        video_player.player = simpleExoPlayer
        simpleExoPlayer.seekTo(playbackPosition)
        simpleExoPlayer.playWhenReady = false
        simpleExoPlayer.addListener(this)
    }

    private fun releaseExoPlayer() {
        playbackPosition = simpleExoPlayer.currentPosition
        simpleExoPlayer.release()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MediaPlayerViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun getDataDummy(): MutableList<Channel> {
        val movies = mutableListOf<Channel>()
        // 1
        movies.add(
            Channel(
                "Black Widow",
                "",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.4RUS_v0Y6SxzXNW1wUUpNQHaEK%26pid%3DApi&f=1",
                "720p",
                "Black Widow Movie 2020"
            )
        )
        // 2
        movies.add(
            Channel(
                "Loki",
                "",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.ytimg.com%2Fvi%2Fq9V0BLTUwDw%2Fmaxresdefault.jpg&f=1&nofb=1",
                "720p",
                "Loki movie"
            )
        )
        // 3
        movies.add(
            Channel(
            "Black Panther",
            "",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.opptrends.com%2Fwp-content%2Fuploads%2F2019%2F01%2FBlack-Panther-790x415.jpg&f=1&nofb=1",
            "720p",
                "Black Panther is good"
            )
        )
        // 4
        movies.add(
            Channel(
            "Avenger: Endgame",
                "",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.cinematographe.it%2Fwp-content%2Fuploads%2F2019%2F04%2FAvengers-Endgame-Must-Watch-Marvel-Movies.jpg&f=1&nofb=1",
            "720p",
                "Best Movie Ever"
            )
        )
        // 5
        movies.add(
            Channel(
            "Dunkirk",
            "",
            "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.davidleancinema.org.uk%2Fwp-content%2Fuploads%2F2017%2F07%2FDunkirk-Landscape-656x328.jpg&f=1&nofb=1",
                "720p",
            "War Movie"
            )
        )
        // 6
        movies.add(
            Channel(
            "X-man: Apocalypse",
                "",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.1DPLhqY2yOX4UhG2jHxLPAHaFs%26pid%3DApi&f=1",
                "720p",
                "Wow Movie"
            )
        )
        //
        movies.add(
            Channel(
                "Transformer: age of extinction",
                "",
                "http://manapop.com/wp-content/uploads/2014/06/landscape_movies-transformers-age-of-extinction-poster-grimlock-e1468334931340.jpg",
                "720p",
                "Old"
            )
        )
        return movies
    }

}
