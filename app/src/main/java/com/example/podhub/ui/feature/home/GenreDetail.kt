@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.podhub.ui.feature.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.example.podhub.R
import com.example.podhub.components.BottomNavigationBar
import com.example.podhub.components.HomeHeader
import com.example.podhub.data.PodcastResponse
import com.example.podhub.models.PodcastResponseData
import com.example.podhub.storage.DataStoreManager
import com.example.podhub.ui.navigation.Routes
import com.example.podhub.viewmodels.PodcastViewModel
import com.example.podhub.utils.ShimmerPodcastRow

@Composable
fun GenreDetailScreen(
    genreName: String,
    navController: NavHostController,
    podcastViewModel: PodcastViewModel
) {

    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    val userData by dataStore.userData.collectAsState(initial = emptyMap())
    val podcasts by podcastViewModel.categoryPodcasts.collectAsState()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val isLoading by podcastViewModel.isLoading.collectAsState()

    val genreImageRes = when (genreName.lowercase()) {
        "comedy" -> R.drawable.comedy
        "news" -> R.drawable.news
        "arts" -> R.drawable.art
        "business" -> R.drawable.business
        "documentary" -> R.drawable.documentary
        "family & kids" -> R.drawable.familyandkids
        "film & tv" -> R.drawable.filmandtv
        "history" -> R.drawable.history
        "society & culture" -> R.drawable.societyandculture
        "sport" -> R.drawable.sport
        "true crime" -> R.drawable.true_crime
        else -> R.drawable.comedy
    }

    val translateGenreName = when (genreName.lowercase()) {
        "comedy" -> "Hài Kịch"
        "news" -> "Tin Tức"
        "arts" -> "Nghệ Thuật"
        "business" -> "Kinh Doanh"
        "documentary" -> "Tài Liệu"
        "family & kids" -> "Gia Đình và Trẻ Em"
        "film & tv" -> "Phim và Truyền Hình"
        "history" -> "Lịch Sử"
        "society & culture" -> "Xã Hội và Văn Hóa"
        "sport" -> "Thể Thao"
        "true crime" -> "Tội Phạm"
        else ->   "Podcast"
    }
    LaunchedEffect(genreName) {
        Log.d("gerne",genreName)
        podcastViewModel.fetchPodcastsByCategory(genreName)
    }

    Scaffold(
        topBar = {
            Column {
                HomeHeader(
                    userName = userData["name"] ?: "Guest",
                    userAvatarUrl = userData["photoUrl"] ?: ""
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            tint = Color(0xFFFFC533),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        },

        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onHomeClick = { navController.navigate("home") },
                onSearchClick = { navController.navigate("search") },
                onRoomClick = { navController.navigate("room") },
                onLibraryClick = { navController.navigate("library") }
            )
        },


    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = genreImageRes),
                        contentDescription = genreName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = translateGenreName,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC533),
                    )

                    Text(
                        text = getGenreSubtitle(genreName),
                        fontSize = 16.sp,
                        color = Color(0xFFFFC533),
                        fontWeight = FontWeight.W500

                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            if (isLoading) {
                items(6) {
                    ShimmerPodcastRow()
                }
            }else{
                items(podcasts) { podcast ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("podcast", podcast)

                                navController.navigate(Routes.PODCAST_DETAIL)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(podcast.artworkUrl100),
                            contentDescription = podcast.trackName,
                            modifier = Modifier
                                .size(56.dp)
                                .padding(end = 12.dp)
                        )

                        Column {
                            Text(
                                text = podcast.collectionName.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color(0xFFFFC533)
                            )
                            Text(
                                text = podcast.artistName.toString(),
                                fontSize = 13.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }

            }


        }
    }
}

fun getGenreSubtitle(genre: String): String {
    return when (genre.lowercase()) {
        "comedy" -> "Từ châm biếm đến hài kịch tình huống, chúng tôi mang đến cho bạn những giây phút giải trí không thể bỏ lỡ."
        "news" -> "Từ chính trị, kinh tế đến văn hóa, chúng tôi mang đến cho bạn một cái nhìn toàn diện về những gì đang diễn ra."
        "business" -> "Podcast dành cho những ai đam mê kinh doanh, nơi bạn có thể tìm thấy cảm hứng và kiến thức để phát triển sự nghiệp."
        "arts" -> "Khám Phá thế giới nghệ thuật với những câu chuyện độc đáo và tác phẩm ấn tượng"
        "documentary" -> "Những câu chuyện có thật, những bí ẩn chưa được giải mã, tất cả đều có trong podcast tài liệu của chúng tôi!"
        "true crime" -> "Khám phá những vụ án ly kỳ và bí ẩn chưa được giải quyết trong thế giới tội phạm có thật."
        "family & kids" -> "Khám phá thế giới đầy màu sắc của trẻ em với những câu chuyện thú vị và bài học bổ ích cho cả gia đình."
        "film & tv" -> "Cùng chúng tôi bàn luận về các bộ phim yêu thích, những series đang hot và những tác phẩm kinh điển không thể bỏ lỡ."
        "history" -> "Khám phá những câu chuyện chưa được kể từ quá khứ, nơi mỗi tập podcast là một hành trình qua các thời đại."
        "society & culture" -> "Khám phá những khía cạnh đa dạng của xã hội và văn hóa, từ phong tục tập quán đến những xu hướng hiện đại."
        "sport" -> "Podcast dành cho những người yêu thể thao, nơi bạn có thể kết nối với cộng đồng và chia sẻ đam mê của mình."
        else -> "top picks for $genre"
    }
}
