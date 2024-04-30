package top.topsea.streamplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import top.topsea.streamplayer.data.viewmodel.ChatInfoViewModel
import top.topsea.streamplayer.data.viewmodel.UISetsViewModel
import top.topsea.streamplayer.ui.screen.MainScreen
import top.topsea.streamplayer.ui.screen.SettingsScreen
import top.topsea.streamplayer.ui.theme.StreamPlayerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            StreamPlayerTheme {
                // A surface container using the 'background' color from the theme

                val context = LocalContext.current
                val viewModel:ChatInfoViewModel = hiltViewModel()
                val uiViewModel:UISetsViewModel = hiltViewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable("main_screen") {
                            MainScreen(
                                navigateToProfile = { },
                                navController = navController,
                            )
                        }
                        composable("settings") {
                            SettingsScreen(uiSetsState = uiViewModel.uiSetsState)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val player = ExoPlayer.Builder(context).build()

// Build the media item.
    val mediaItem = MediaItem.fromUri("http://simple.topsea.top:23456/voice/gpt-sovits?id=0&format=mp3&streaming=true&prompt_lang=auto&preset=default&text=当然！这是一个名叫艾丽丝的年轻女孩的故事，她住在一个古老的小镇上，名叫温德尔镇。这个小镇有着漫长而充满传奇的历史，被森林和湖泊环绕，仿佛一个隐藏着许多秘密的神秘地方。 艾丽丝是个充满好奇心的女孩，她总是对小镇的传说和谜团充满兴趣。有一天，她发现了一本装满了古老符号和未解之谜的书，书的封面上写着：“温德尔的秘密”。被好奇心驱使，她开始阅读这本书，里面记录着关于小镇的许多神秘事件和传说。 在一天的午后，艾丽丝翻到了书的最后一页，那里写着关于“消失的钥匙”的传说。据说，这把钥匙是一个能够打开通往未知世界的门的神奇物品。艾丽丝迫切地想要找到这把钥匙，于是她开始了一场冒险之旅，穿越古老的森林和湖泊，寻找失落的钥匙。 在她的冒险中，她遇到了各种各样的挑战和障碍，但她坚持不懈地寻找着。最终，她来到了小镇深处的一座被遗忘已久的废弃城堡，据说那里藏着失落的钥匙。 在城堡的探索中，艾丽丝发现了一扇通往未知世界的门，而且正好需要那把失落的钥匙来打开。她兴奋地将钥匙插入锁孔，门随即缓缓打开，一股神秘的力量从门里涌出。 艾丽丝毫不犹豫地踏出了门，迈向未知的旅程。在那个奇妙的世界里，她遇到了许多神奇的生物和美丽的景色，经历了无数的冒险和探索。 最终，当她回到了温德尔镇，她已经成长了许多，充满了智慧和勇气。虽然她没有带回失落的钥匙，但她得到了更珍贵的东西——对冒险的渴望和对未知的勇气。从那以后，她继续在温德尔镇中度过每一天，永远保留着那段奇妙冒险的记忆。")
// Set the media item to be played.
    player.setMediaItem(mediaItem)
// Prepare the player.
    player.prepare()
// Start the playback.

    Button(onClick = { player.play() }) {

        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StreamPlayerTheme {
        Greeting("Android")
    }
}