// kotlin
package com.example.cinelist.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinelist.data.model.Title
import com.example.cinelist.ui.components.ShimmerCardPlaceholder
import com.example.cinelist.util.MediaType
import com.example.cinelist.ui.components.ErrorScreen
import com.example.cinelist.ui.components.MediaCard
import com.example.cinelist.util.HomeUiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTitleClick: (Int) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "CineList",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        if (uiState.error != null && uiState.movies.isEmpty() && uiState.tvShows.isEmpty()) {
            val friendlyError = if (uiState.error.contains("failed to connect", ignoreCase = true))
                "Network error. Check your connection and try again."
            else uiState.error

            ErrorScreen(
                error = friendlyError,
                onRetry = { viewModel.loadHomeMedia() },
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            ContentScreen(
                uiState = uiState,
                onMediaTypeChange = { viewModel.toggleMediaType(it) },
                onTitleClick = onTitleClick,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}


@Composable
private fun ContentScreen(
    uiState: HomeUiState,
    onMediaTypeChange: (MediaType) -> Unit,
    onTitleClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val moviesGridState = rememberLazyGridState()
    val tvShowsGridState = rememberLazyGridState()

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterButton(
                label = "Movies",
                isSelected = uiState.selectedMediaType == MediaType.MOVIES,
                onClick = { onMediaTypeChange(MediaType.MOVIES) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            )
            FilterButton(
                label = "TV Shows",
                isSelected = uiState.selectedMediaType == MediaType.TV_SHOWS,
                onClick = { onMediaTypeChange(MediaType.TV_SHOWS) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            )
        }

        if (uiState.isLoading) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(12) {
                    ShimmerCardPlaceholder()
                }
            }
        } else {
            val (titles, gridState) = if (uiState.selectedMediaType == MediaType.MOVIES) {
                uiState.movies to moviesGridState
            } else {
                uiState.tvShows to tvShowsGridState
            }

            if (titles.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No titles available")
                }
            } else {
                MediaGrid(
                    titles = titles,
                    gridState = gridState,
                    onTitleClick = onTitleClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun FilterButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val elevation by animateDpAsState(if (isSelected) 8.dp else 2.dp, label = "elevation")
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation
        )
    ) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MediaGrid(
    titles: List<Title>,
    gridState: LazyGridState,
    onTitleClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(titles, key = { it.id }) { title ->
            MediaCard(
                title = title,
                onClick = { onTitleClick(title.id) }
            )
        }
    }
}
