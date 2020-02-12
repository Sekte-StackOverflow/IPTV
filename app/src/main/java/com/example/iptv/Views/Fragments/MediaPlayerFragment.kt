package com.example.iptv.Views.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.iptv.Models.Channel

import com.example.iptv.R
import com.example.iptv.ViewModels.MediaPlayerViewModel
import com.example.iptv.Views.Activities.FullscreenActivity
import com.example.iptv.Views.Adapters.MoviesAdapters
import com.example.iptv.Views.Adapters.PlayerAdapters
import com.example.iptv.api.SSLconfig
import com.example.iptv.api.service.AppKey
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.mikepenz.iconics.Iconics.applicationContext
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.synnapps.carouselview.ViewListener
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import kotlinx.android.synthetic.main.media_player_fragment.*

class MediaPlayerFragment : Fragment(), Player.EventListener {

    companion object {
        fun newInstance() = MediaPlayerFragment()
    }

    private lateinit var viewModel: MediaPlayerViewModel
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var moviesAdapters: MoviesAdapters

    private lateinit var moviePlayer: PlayerAdapters

    private var movieList: MutableList<Channel>? = null
    private var bannerList: MutableList<String> = mutableListOf()

    private var playbackPosition = 0L
    private lateinit var ssl: SSLconfig
    private var hlsUrl : String = ""
    private var isPlay = false

//    private var hlsUrl = "https://stream.suryaiptv.net/streams/22_.m3u8"

//    private val hlsUrl = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8video/250kbit.m3u8"

//    private val hlsUrl = "http://api.new.livestream.com/accounts/22711876/events/6759790/live.m3u8"
//    private val hlsUrl = "http://hls.ksl.com/t/KSL_NEWSRADIO/playlist.m3u8"

    private val bandwidthMeter by lazy { DefaultBandwidthMeter() }
    private val adaptiveTrackSelection by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ssl = SSLconfig()
        ssl.isSSLCestification(false)
        viewModel = ViewModelProviders.of(this).get(MediaPlayerViewModel::class.java)
        return inflater.inflate(R.layout.media_player_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!activity?.intent?.getStringExtra("resVidUri").isNullOrEmpty()) {
            hlsUrl = activity!!.intent.getStringExtra("resVidUri")
            moviePlayer = PlayerAdapters(requireContext(), hlsUrl)
            simpleExoPlayer = moviePlayer.initExoPlayer()
        }

        viewModel.init()
        viewModel.getAllChannel().observe(this, Observer {
            t -> movieListView(t)
        })

        bannerConfig()

        exo_fullscreen_button.setOnClickListener{
            fullscreenAction()
        }

//        val display = activity!!.getSystemService<WindowManager>()!!.defaultDisplay
//        val orttn = display.orientation
//        when(orttn) {
//            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE -> Toast.makeText(context, "Landscape state", Toast.LENGTH_SHORT).show()
//            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT -> Toast.makeText(context, "Potrait state", Toast.LENGTH_SHORT).show()
//        }

    }

    private fun bannerConfig() {
        bannerList = bannerData()
        carousel_banner.setViewListener(object : ViewListener {
            override fun setViewForPosition(position: Int): View {
                val myView = layoutInflater.inflate(R.layout.the_banner, null)
                val img: ImageView = myView.findViewById(R.id.img_banner)
                Picasso.get().load(bannerList[position]).into(img)
                return myView
            }
        })
        carousel_banner.pageCount = bannerList.size
    }

    private fun fullscreenAction() {
        playbackPosition = simpleExoPlayer.currentPosition
        if (simpleExoPlayer.isPlaying) {
            simpleExoPlayer.stop()
        }
        val intent = Intent(context, FullscreenActivity::class.java)
        intent.putExtra("playbackPosition", playbackPosition)
        intent.putExtra("videoUrl", hlsUrl)
        startActivity(intent)
        simpleExoPlayer.release()
    }

    private fun movieListView(channels: MutableList<Channel>) {
        movieList = channels
        moviesAdapters = MoviesAdapters(movieList)
        moviesAdapters.setOnItemClickListener{
                adapter, view, position ->
            //            Toast.makeText(requireContext(), movieList!![position].name, Toast.LENGTH_SHORT).show()
            playbackPosition = 0L
            if (isPlay) {
                simpleExoPlayer.release()
            }
            if (movieList!![position].videoUrl.isNullOrEmpty() || movieList!![position].videoUrl.equals(".")){
//                Toast.makeText(requireContext(), "NO_CHANNEL_URI", Toast.LENGTH_SHORT).show()
                Log.d(AppKey.FRAGMENT_KEY().STREAMING_F, "NO_CHANNEL_ARE_PLAY")
            } else {
                hlsUrl = movieList!![position].videoUrl
                isPlay = true
            }
            moviePlayer = PlayerAdapters(requireContext(), hlsUrl)
            simpleExoPlayer = moviePlayer.initExoPlayer()
            video_player.player = simpleExoPlayer
            simpleExoPlayer.playWhenReady = true
        }
        moviesAdapters.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        rv_movies.adapter = moviesAdapters
        rv_movies.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
    }

    override fun onStart() {
        super.onStart()
        if (hlsUrl.isNullOrEmpty()) {
            Toast.makeText(context, "No Vidio is Played", Toast.LENGTH_SHORT).show()
        } else {
            prepareToPlay()
        }
    }

    override fun onStop() {
        releaseExoPlayer()
        super.onStop()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playWhenReady) {
            if (playbackState == Player.STATE_BUFFERING) {
                progressBar.visibility = View.VISIBLE
            } else if (playbackState == Player.STATE_READY) {
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (isPlay) {
                activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                fullscreenAction()
            } else {
                activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }

    // this method for video player pause when tab changed
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (this.isVisible) {
            if (!isVisibleToUser) {
                video_player.visibility = View.GONE
                releaseExoPlayer()
            }
            if (isVisibleToUser){
                video_player.visibility = View.VISIBLE
                if (!hlsUrl.isNullOrEmpty()) {
                    prepareToPlay()
                }
            }
        }
    }

    private fun prepareToPlay() {
        video_player.player = simpleExoPlayer
        if (activity?.intent?.extras?.getLong("playbackPosition") != null) {
            playbackPosition = activity!!.intent.extras?.getLong("playbackPosition")!!
            simpleExoPlayer.seekTo(playbackPosition)
            simpleExoPlayer.playWhenReady = true
        } else {
            simpleExoPlayer.playWhenReady = false
        }
        isPlay = true
        simpleExoPlayer.addListener(this)
    }

    private fun releaseExoPlayer() {
        if (isPlay) {
            playbackPosition = simpleExoPlayer.currentPosition
            simpleExoPlayer.release()
            isPlay = false
        }
    }

//    private fun getDataDummy(): MutableList<Channel> {
//        val movies = mutableListOf<Channel>()
//        // 1
//        movies.add(
//            Channel(
//                "K-Drama",
//                "5089",
//                "https://alvaindopratama.com/eyeplus/image/Live%20Video/K-Drama.jpg",
//                "Live",
//                "https://stream.suryaiptv.net/streams/40_.m3u8"
//            )
//        )
//        // 2
//        movies.add(
//            Channel(
//                "Sinema Indonesia",
//                "1190",
//                "https://alvaindopratama.com/eyeplus/image/Live%20Video/Sinema%20Indonesia.jpg",
//                "Live",
//                "https://stream.suryaiptv.net/streams/124_.m3u8"
//            )
//        )
//        // 3
//        movies.add(
//            Channel(
//            "Sinema X",
//            "12000",
//            "https://alvaindopratama.com/eyeplus/image/Live%20Video/Sinema%20X.jpg",
//            "Live",
//                ""
//            )
//        )
//        // 4
//        movies.add(
//            Channel(
//            "TVP",
//                "10000",
//                "https://alvaindopratama.com/eyeplus/image/Live%20Video/TVP.jpg",
//            "Live",
//                ""
//
//            )
//        )
//        // 5
//        movies.add(
//            Channel(
//            "M-Cine",
//            "8090",
//            "https://alvaindopratama.com/eyeplus/image/Live%20Video/M-Cine.jpg",
//                "Live",
//                ""
//            )
//        )
//        // 6
//        movies.add(
//            Channel(
//            "FTV",
//                "2790",
//                "https://alvaindopratama.com/eyeplus/image/Live%20Video/FTV.jpg",
//                "Live",
//                ""
//            )
//        )
//        //
//        movies.add(
//            Channel(
//                "Drakor Plus",
//                "2810",
//                "https://alvaindopratama.com/eyeplus/image/Live%20Video/Drakor%20Plus.jpg",
//                "Live",
//                "https://stream.suryaiptv.net/streams/132_.m3u8"
//            )
//        )
//        movies.add(
//            Channel(
//                "Lejel Shopping",
//                "2810",
//                "https://alvaindopratama.com/eyeplus/image/Live%20Video/Lajel%20Shopping.jpg",
//                "Live",
//                ""
//            )
//        )
//        movies.add(
//            Channel(
//                "Lejel Live",
//                "2810",
//                "https://alvaindopratama.com/eyeplus/image/Live%20Video/Lajel%20Live%20Shopping.jpg",
//                "Live",
//                "https://stream.suryaiptv.net/streams/125_.m3u8"
//            )
//        )
//
//        return movies
//    }

    private fun bannerData(): MutableList<String> {
        val data = mutableListOf(
            "https://alvaindopratama.com/eyeplus/image/banner/1.jpg",
            "https://alvaindopratama.com/eyeplus/image/banner/2.jpg",
            "https://alvaindopratama.com/eyeplus/image/banner/3.jpg"
        )
        return data
    }

}
