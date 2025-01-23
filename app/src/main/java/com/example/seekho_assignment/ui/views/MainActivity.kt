package com.example.seekho_assignment.ui.views

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bumptech.glide.Glide
import com.example.seekho_assignment.model.Anime
import com.example.seekho_assignment.model.GenreOrProducer
import com.example.seekho_assignment.ui.theme.Seekho_AssignmentTheme
import com.example.seekho_assignment.utils.Constants
import com.example.seekho_assignment.viewmodel.AnimeViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = remember { AnimeViewModel() }
            Seekho_AssignmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(
                        context = this@MainActivity,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MainNavigation(context: Context, viewModel: AnimeViewModel) {
    LaunchedEffect(Unit) {
        viewModel.getTopAnimeList()
        Log.d("Deepak", "HomeScreen: animeList ->${Gson().toJson(viewModel.animeList)}")
    }
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("HomeScreen") {
            HomeScreen(
                context = context,
                title = "Top Anime",
                viewModel = viewModel,
                onNavigateToNextScreen = { animeId ->
                    navController.navigate("AnimeScreen/${animeId}")
                }
            )
        }
        composable("AnimeScreen/{animeId}", arguments = listOf(
            navArgument("animeId") { type = NavType.LongType }
        )) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getLong("animeId") ?: 0
            AnimeDetailsScreen(
                context = context,
                title = "Anime Details",
                animeId = animeId,
                viewModel = viewModel,
                onNavigationBack = { navController.popBackStack() }
            )
        }
    }

}

@Composable
fun HomeScreen(context: Context, title: String, viewModel: AnimeViewModel, onNavigateToNextScreen:(Long) -> Unit) {
    val loading by remember { derivedStateOf { viewModel.isLoadingForHomeScreen } }
    val animeList by remember { derivedStateOf { viewModel.animeList } }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), verticalArrangement = Arrangement.Top) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            Color.Black,
            TextUnit(16f, TextUnitType.Sp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Log.d("Deepak", "HomeScreen: animeList ->${Gson().toJson(animeList)}, loading-> $loading")
            when {
                loading -> Loader()
                !animeList.isNullOrEmpty() -> ItemGrid(
                    context = context,
                    items = animeList!!
                ) { anime: Anime ->
                    onNavigateToNextScreen(anime.id)
                }
                else -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText("Failed to load items.")
                }
            }
        }
    }
}

@Composable
fun ItemGrid(context: Context, items: List<Anime>, onItemClick: (Anime) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(3.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items.size) { index ->
            GridItem(context = context, item = items[index], onClick = { onItemClick(items[index]) })
        }
    }
}

@Composable
fun GridItem(context: Context, item: Anime, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Ensures each item is square
            .clickable(onClick = onClick)
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f) // Adjust aspect ratio to match the image
        ) {
            // Image
            AndroidView(
                factory = { ctx ->
                    ImageView(ctx).apply {
                        // Set ImageView scaling or properties if needed
                        scaleType = ImageView.ScaleType.FIT_XY
                    }
                },
                update = { imageView ->
                    // Load image into ImageView using Glide
                    Glide.with(context)
                        .load(item.images.jpg.largeImageUrl)
                        .placeholder(android.R.drawable.progress_indeterminate_horizontal) // Optional placeholder
                        .error(android.R.drawable.stat_notify_error) // Optional error image
                        .into(imageView)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )

            Text(
                text = "EP: ${item.episodes}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Rating: ${item.score}/10",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            maxLines = 2
        )
    }
}

@Composable
fun Loader() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun AnimeDetailsScreen(context: Context, title: String,animeId: Long, viewModel: AnimeViewModel, onNavigationBack: () -> Unit) {
    val loading by remember { derivedStateOf { viewModel.isLoadingForAnimeScreen } }
    val anime by remember { derivedStateOf { viewModel.animeDetails } }

    LaunchedEffect(Unit) {
        viewModel.getAnimeDetails(animeId)
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            Color.Black,
            TextUnit(16f, TextUnitType.Sp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row (modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(modifier = Modifier
                .fillMaxWidth(), onClick = onNavigationBack) {
                BasicText("Back")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                loading -> Loader()
                anime != null -> AnimeDetails(anime!!, context)
                else -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText("Failed to load items.")
                }
            }
        }
    }
}

@Composable
fun AnimeDetails(anime: Anime, context: Context) {
    val scrollState = rememberScrollState()
    Log.d("Deepak", "AnimeDetails: ${Gson().toJson(anime)}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = anime.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (anime.trailer?.url != null) {
                        openYouTubeUrl(context, anime.trailer.url)
                    }
                }
        ) {
               AndroidView(
                   factory = { ctx ->
                       ImageView(ctx).apply {
                           // Set ImageView scaling or properties if needed
                           scaleType = ImageView.ScaleType.FIT_XY
                       }
                   },
                   update = { imageView ->
                       // Load image into ImageView using Glide
                       Glide.with(context)
                           .load(anime.images.jpg.largeImageUrl)
                           .placeholder(android.R.drawable.progress_indeterminate_horizontal) // Optional placeholder
                           .error(android.R.drawable.stat_notify_error) // Optional error image
                           .into(imageView)
                   },
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(300.dp)
               )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Episodes: ${anime.episodes}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = TextAlign.Justify
            )

            Text(
                text = "Rating: ${anime.rating}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = TextAlign.Justify
            )
        }
        if (!anime.genres.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(5.dp))
            TextGrid(anime.genres)
        }
        if (!anime.producers.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(5.dp))
            TextGrid(anime.producers)
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = anime.synopsis,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun TextGrid(items: List<GenreOrProducer>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 columns in each row
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items) { item ->
            Text(
                text = item.name,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

fun openYouTubeUrl(context: Context, videoUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)).apply {
        setPackage("com.google.android.youtube")
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
        context.startActivity(browserIntent)
    }
}