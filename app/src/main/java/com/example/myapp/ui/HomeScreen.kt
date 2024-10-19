package com.example.myapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicDashboard(navController: NavController) {

    var isPlaying by remember { mutableStateOf(false) } // State to control NowPlayingBar visibility

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "MusicApp", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("SearchScreen")
                    }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = {
                        navController.navigate("ProfileScreen")
                    }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Featured Section
                item {
                    FeaturedMusicSection(onPlayClicked = { isPlaying = true })
                }

                // Categories Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Your Playlists",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    PlaylistRow()
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Recently Played",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    RecentlyPlayedRow()
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Trending Now",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    TrendingMusicRow()
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Bhojpuri Songs",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    BhojpuriSongsRow()
                }
            }

            // Now Playing Bar (Conditional visibility based on `isPlaying` state)
            if (isPlaying) {
                NowPlayingBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    onPauseClicked = { isPlaying = false }
                )
            }
        }
    }
}

@Composable
fun FeaturedMusicSection(onPlayClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
            .background(Color.LightGray, shape = MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        // Featured Album Cover
        Image(
            painter = painterResource(id = R.drawable.album_cover),
            contentDescription = "Featured Album",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        // Overlay Play Button
        IconButton(onClick = onPlayClicked) {
            Icon(
                painter = painterResource(id = R.drawable.play),
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Composable
fun PlaylistRow() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {   // Pass song to PlaylistCard
            PlaylistCard()
        }
    }
}

@Composable
fun PlaylistCard() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Gray, shape = MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Text("Play Songs", color = Color.Red, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RecentlyPlayedRow() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {
            PlaylistCard()  // Sample data
        }
    }
}

@Composable
fun BhojpuriSongsRow() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {  // Sample recently played tracks
            MusicCard() // Sample data
        }
    }
}

@Composable
fun TrendingMusicRow() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {
            PlaylistCard()
        }
    }
}

@Composable
fun MusicCard() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Gray, shape = MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Text("Play", color = Color.White)
    }
}

@Composable
fun NowPlayingBar(
    modifier: Modifier = Modifier,
    onPauseClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Now Playing: Song Title", color = Color.White)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* TODO: Play previous */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.left_arrow),
                        contentDescription = "Previous",
                        tint = Color.White
                    )
                }
                IconButton(onClick = onPauseClicked) {  // Pause action
                    Icon(
                        painter = painterResource(id = R.drawable.play),
                        contentDescription = "Pause",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { /* TODO: Play next */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.right_arrow),
                        contentDescription = "Next",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
