package com.example.podhub.ui.feature.home

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.podhub.R

data class PodcastCategory(
    val name: String,
    @DrawableRes val imageRes: Int
)

val podcastCategories = listOf(
    PodcastCategory("Comedy", R.drawable.comedy),
    PodcastCategory("News", R.drawable.news),
    PodcastCategory("Arts", R.drawable.art),
    PodcastCategory("Business", R.drawable.business),
    PodcastCategory("Documentary", R.drawable.documentary),
    PodcastCategory("Family & Kids", R.drawable.familyandkids),
    PodcastCategory("Film & TV", R.drawable.filmandtv),
    PodcastCategory("History", R.drawable.history),
    PodcastCategory("Society & Culture", R.drawable.societyandculture),
    PodcastCategory("Sport", R.drawable.sport),
    PodcastCategory("True Crime", R.drawable.true_crime),
)

@Composable
fun PodcastCategoriesScreen(
    navController: NavHostController
) {
    val categories = podcastCategories.chunked(2)
    val modifier: Modifier = Modifier




    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { category ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                            .clickable {
                                navController.navigate("genre/${Uri.encode(category.name)}")
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val translateGenreName = when (category.name) {
                            "Comedy" -> "Hài Kịch"
                            "News" -> "Tin Tức"
                            "Arts" -> "Nghệ Thuật"
                            "Business" -> "Kinh Doanh"
                            "Documentary" -> "Tài Liệu"
                            "Family & Kids" -> "Gia Đình và Trẻ Em"
                            "Film & TV" -> "Phim và Truyền Hình"
                            "History" -> "Lịch Sử"
                            "Society & Culture" -> "Xã Hội và Văn Hóa"
                            "Sport" -> "Thể Thao"
                            "True Crime" -> "Tội Phạm"
                            else ->   "Podcast"
                        }
                        Image(
                            painter = painterResource(id = category.imageRes),
                            contentDescription = category.name,
                            modifier = Modifier.size(140.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = translateGenreName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFC533)
                        )
                    }
                }

                if (row.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
