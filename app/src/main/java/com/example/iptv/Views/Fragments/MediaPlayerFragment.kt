package com.example.iptv.Views.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.iptv.Models.*

import com.example.iptv.R
import com.example.iptv.ViewModels.*
import com.example.iptv.Views.Activities.FullscreenActivity
import com.example.iptv.Views.Adapters.MoviesAdapters
import com.example.iptv.Views.Adapters.PlayerAdapters
import com.example.iptv.api.APIClient
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
import com.mikepenz.materialdrawer.util.ifNull
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.synnapps.carouselview.ViewListener
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import kotlinx.android.synthetic.main.media_player_fragment.*
import org.json.JSONObject
import java.util.*

class MediaPlayerFragment : Fragment(), Player.EventListener {

    companion object {
        fun newInstance() = MediaPlayerFragment()
    }

    private lateinit var viewModel: MediaPlayerViewModel
    private lateinit var myActivities: myActivitiesViewModel

    private lateinit var session: SessionViewModel
    private lateinit var banner: BannerViewModel

    private lateinit var dialog: Dialog
    private lateinit var dialogData: Banner
    private lateinit var user: User

    private var tmpClick = -1

    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var moviesAdapters: MoviesAdapters

    private lateinit var moviePlayer: PlayerAdapters

    private var movieList: MutableList<Channel>? = null
    private var bannerList: MutableList<Banner> = mutableListOf()

    private var playbackPosition = 0L
    private var isLogin = false

    private lateinit var ssl: SSLconfig
    private var hlsUrl : String = ""
    private var isPlay = false
    private var status = ""

    private lateinit var ipPublic: PublicIPAddress

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ssl = SSLconfig()
        ssl.isSSLCestification(false)
        viewModel = ViewModelProviders.of(this).get(MediaPlayerViewModel::class.java)
        session = ViewModelProviders.of(this).get(SessionViewModel::class.java)
        banner = ViewModelProviders.of(this).get((BannerViewModel::class.java))
        myActivities = ViewModelProviders.of(this).get(myActivitiesViewModel::class.java)
        myActivities.init()

        session.init(requireContext())
        session.getPublicIP().observe(this, Observer {
            ipPublic = it
        })


        return inflater.inflate(R.layout.media_player_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        banner.init()
        banner.getImageDialog().observe(this, Observer {
            dialogData = it[0]
            dialog = Dialog(requireContext())
            val sharePref = activity!!.getSharedPreferences("My_DIALOG", Context.MODE_PRIVATE)
            showDiaolog(sharePref.getBoolean("INIT_START", false))
            sharePref.edit().putBoolean("INIT_START", false).apply()
        })


        if (!activity?.intent?.getStringExtra("resVidUri").isNullOrEmpty()) {
            hlsUrl = activity!!.intent.getStringExtra("resVidUri")
            moviePlayer = PlayerAdapters(requireContext(), hlsUrl)
            simpleExoPlayer = moviePlayer.initExoPlayer()
        }

        viewModel.init()
        viewModel.getAllChannel().observe(this, Observer {
            t -> movieListView(t)
        })
        session.getUser().observe(this, Observer {
            user = it
        })
        session.isLoginLive().observe(this, Observer {
            isLogin = it
        })

        banner.getBanner("Channel").observe(this, Observer {
            img -> bannerConfig(img)
        })

    }

    private fun modeVideo(status: String) {
        if (status.toLowerCase().contains("live")) {
            exo_modeVideo.visibility = View.VISIBLE
            exo_seek_bar.visibility = View.INVISIBLE
        } else {
            exo_modeVideo.visibility = View.GONE
            exo_seek_bar.visibility = View.VISIBLE
        }
    }

    private fun bannerConfig(list: MutableList<Banner>) {
        bannerList = list
        carousel_banner.setViewListener(object : ViewListener {
            override fun setViewForPosition(position: Int): View {
                val myView = layoutInflater.inflate(R.layout.the_banner, null)
                val img: ImageView = myView.findViewById(R.id.img_banner)
                img.setOnClickListener {
                    if(bannerList[position].link == "null" || bannerList[position].link == "" || bannerList[position].link == null) {
//                        Toast.makeText(requireContext(), "NO_URL", Toast.LENGTH_SHORT).show()
                        Log.d("MEDIA", "BANNER_URL_NULL")
                    } else {
                        startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(bannerList[position].link)))
                    }

                }
                val url = APIClient.IMAGE_PATH + bannerList[position].img
                Picasso.get().load(url).into(img)
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
        intent.putExtra("status", status)
        startActivity(intent)
        simpleExoPlayer.release()
    }

    @SuppressLint("DefaultLocale")
    private fun movieListView(channels: MutableList<Channel>) {
        movieList = channels
        moviesAdapters = MoviesAdapters(movieList)
        moviesAdapters.setOnItemClickListener{
                adapter, view, position ->
            playbackPosition = 0L
            if (isPlay) {
                simpleExoPlayer.release()
            }
            if (movieList!![position].userType.toLowerCase() == "subscriber") {
                if (isLogin) {
                    playChannel(position)
                } else {
                    Log.d(AppKey.FRAGMENT_KEY().STREAMING_F, "SUBSCRIBE_FIRST")
                    Toast.makeText(context, "Please Subscribe first", Toast.LENGTH_SHORT).show()
                }
            } else {
                playChannel(position)
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

    private fun playChannel(position: Int) {
        if (movieList!![position].videoUrl.isNullOrEmpty() || movieList!![position].videoUrl.equals(".")){
            Toast.makeText(requireContext(), "NO_CHANNEL_URI", Toast.LENGTH_SHORT).show()
            Log.d(AppKey.FRAGMENT_KEY().STREAMING_F, "NO_CHANNEL_ARE_PLAY")
        } else {
            status = movieList!![position].status
            modeVideo(status)
            hlsUrl = movieList!![position].videoUrl
            if (tmpClick != position) {
                moviesAdapters.updateView(position)
                moviesAdapters.notifyDataSetChanged()
                watchingRes(movieList!![position])
                tmpClick = position
            }
            isPlay = true

        }
    }

    private fun watchingRes(channel: Channel) {
        val userActivity = UserActivities(channel.id.toString(), channel.name, user.id)
        Log.d("MEDIA_FRAGMENT", JSONObject.quote(userActivity.toString()))
        myActivities.postMyActivity(userActivity).observe(this, Observer {
            Log.d("POST status", "[${it.response.status}] : ${it.response.message}")
        })
    }

    override fun onStart() {
        super.onStart()
        if (hlsUrl.isNullOrEmpty()) {
            Log.d(AppKey.FRAGMENT_KEY().STREAMING_F,"No Video Are Played")
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

    @SuppressLint("SourceLockedOrientationActivity")
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

    private fun showDiaolog(show: Boolean) {
        if (show) {
//            val path = "https://lejel.co.id/media/popupbanner/pop-up-valentine.png"
            val path = APIClient.IMAGE_PATH + dialogData.img
            dialog.setContentView(R.layout.custom_dialog_xl)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            val tmp = dialog.window!!.decorView.findViewById<ImageView>(R.id.dialog_img_XL)
            tmp.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(dialogData.link)))
            }
            Picasso.get().load(path).into(tmp)
            dialog.show()
        }
    }

}
