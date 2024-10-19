package com.example.myapp.ui
import SearchViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.myapp.Data.Song
import com.example.myapp.uiState.UiState

@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel(), navController: NavHostController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Search TextField
        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(8.dp)) {
                    if (searchText.text.isEmpty()) {
                        Text("Enter a song name", color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        // Search Button
        Button(onClick = {
            viewModel.search(searchText.text)
        }) {
            Text("Search")
        }

        // Observe UI state and display content accordingly
        val uiState by viewModel.uiState.collectAsState()

        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
            is UiState.Success -> {
                val songs = (uiState as UiState.Success).songs // Safe casting to access songs
                SongList(songs = songs) // Show the song list
            }
            is UiState.Error -> {
                val message = (uiState as UiState.Error).message // Safe casting to access error message
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun SongList(songs: List<Song>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(songs.size) { index ->
            SongItem(song = songs[index])
        }
    }
}

@Composable
fun SongItem(song: Song) {
    Row(modifier = Modifier.padding(8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(song.image),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = song.title)
            Text(text = "Artist: ${song.artist}")
            Text(text = "Album: ${song.album}")
        }
    }
}

