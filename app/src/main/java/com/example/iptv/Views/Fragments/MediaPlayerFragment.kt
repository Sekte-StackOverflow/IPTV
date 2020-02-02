package com.example.iptv.Views.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.iptv.Models.Channel

import com.example.iptv.R
import com.example.iptv.ViewModels.MediaPlayerViewModel
import com.example.iptv.Views.Activities.FullscreenActivity
import com.example.iptv.Views.Adapters.MoviesAdapters
import com.example.iptv.Views.Adapters.PlayerAdapters
import com.example.iptv.api.service.SSLconfig
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

    private val hlsUrl = "https://stream.suryaiptv.net/streams/22_.m3u8"

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
        return inflater.inflate(R.layout.media_player_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviePlayer = PlayerAdapters(requireContext(), hlsUrl)
        simpleExoPlayer = moviePlayer.initExoPlayer()

        movieList = getDataDummy()
        moviesAdapters = MoviesAdapters(movieList)
        moviesAdapters.setOnItemClickListener{
            adapter, view, position ->
            Toast.makeText(requireContext(), movieList!![position].name, Toast.LENGTH_SHORT).show()
        }
        moviesAdapters.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        rv_movies.adapter = moviesAdapters
        rv_movies.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        // banner
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

        exo_fullscreen_button.setOnClickListener{
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

    }

    override fun onStart() {
        super.onStart()
        prepareToPlay()
    }

    override fun onStop() {
        super.onStop()
        releaseExoPlayer()
    }


    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING) {
            progressBar.visibility = View.VISIBLE
        } else if (playbackState == Player.STATE_READY) {
            progressBar.visibility = View.GONE
        }
    }

    // this method for video player pause when tab changed
//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if (this.isVisible) {
//            if (!isVisibleToUser) {
//                onPause()
//            }
//            if (isVisibleToUser){
//                onResume()
//            }
//        }
//    }

    private fun prepareToPlay() {
        video_player.player = simpleExoPlayer
        if (activity?.intent?.extras?.getLong("playbackPosition") != null) {
            playbackPosition = activity!!.intent.extras?.getLong("playbackPosition")!!
            simpleExoPlayer.seekTo(playbackPosition)
            simpleExoPlayer.playWhenReady = true
        } else {
            Handler().postDelayed({
                simpleExoPlayer.playWhenReady = false
            }, 1000)
        }
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

    private fun getDataDummy(): MutableList<Channel> {
        val movies = mutableListOf<Channel>()
        // 1
        movies.add(
            Channel(
                "Black Widow",
                "5089",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.4RUS_v0Y6SxzXNW1wUUpNQHaEK%26pid%3DApi&f=1",
                "VOD"
            )
        )
        // 2
        movies.add(
            Channel(
                "Loki",
                "1190",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.ytimg.com%2Fvi%2Fq9V0BLTUwDw%2Fmaxresdefault.jpg&f=1&nofb=1",
                "VOD"
            )
        )
        // 3
        movies.add(
            Channel(
            "Black Panther",
            "12000",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.opptrends.com%2Fwp-content%2Fuploads%2F2019%2F01%2FBlack-Panther-790x415.jpg&f=1&nofb=1",
            "VOD"
            )
        )
        // 4
        movies.add(
            Channel(
            "Avenger: Endgame",
                "10000",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.cinematographe.it%2Fwp-content%2Fuploads%2F2019%2F04%2FAvengers-Endgame-Must-Watch-Marvel-Movies.jpg&f=1&nofb=1",
            "LIVE"
            )
        )
        // 5
        movies.add(
            Channel(
            "Dunkirk",
            "8090",
            "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.davidleancinema.org.uk%2Fwp-content%2Fuploads%2F2017%2F07%2FDunkirk-Landscape-656x328.jpg&f=1&nofb=1",
                "VOD"
            )
        )
        // 6
        movies.add(
            Channel(
            "X-man: Apocalypse",
                "2790",
                "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.1DPLhqY2yOX4UhG2jHxLPAHaFs%26pid%3DApi&f=1",
                "LIVE"
            )
        )
        //
        movies.add(
            Channel(
                "Transformer: age of extinction",
                "2810",
                "http://manapop.com/wp-content/uploads/2014/06/landscape_movies-transformers-age-of-extinction-poster-grimlock-e1468334931340.jpg",
                "PREMIUM"
            )
        )
        return movies
    }

    private fun bannerData(): MutableList<String> {
        val data = mutableListOf(
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fs-media-cache-ak0.pinimg.com%2Foriginals%2F17%2F88%2F78%2F178878d41985a3632773af5bb8094e00.png&f=1&nofb=1",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fpng.pngtree.com%2Fthumb_back%2Ffw800%2Fback_pic%2F04%2F29%2F94%2F9458401a0ca2e51.jpg&f=1&nofb=1",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse4.mm.bing.net%2Fth%3Fid%3DOIP.8Lg15BDg5nb6MJp9EWXggwHaEK%26pid%3DApi&f=1",
            "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fs-media-cache-ak0.pinimg.com%2F736x%2F79%2Ff2%2F20%2F79f22051baeef591c9e047f45032a614.jpg&f=1&nofb=1"
        )
        return data
    }

}
