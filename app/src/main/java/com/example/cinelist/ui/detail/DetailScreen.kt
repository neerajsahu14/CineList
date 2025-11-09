package com.example.cinelist.ui.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.SignalWifiConnectedNoInternet4
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.cinelist.data.model.TitleDetailResponse
import com.example.cinelist.ui.components.ShimmerDetailPlaceholder
import com.example.cinelist.ui.components.shimmerEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    titleId: Int,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = koinViewModel(
        parameters = { parametersOf(titleId) }
    )
) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    if (uiState.error != null && uiState.titleDetail != null) {
        LaunchedEffect(uiState.error) {
            Toast.makeText(context, uiState.error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.titleDetail?.title ?: "Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = {
            paddingValues ->
            when {
                uiState.isLoading && uiState.titleDetail == null -> {
                    ShimmerDetailPlaceholder(modifier = Modifier.padding(paddingValues))
                }
                uiState.error != null && uiState.titleDetail == null -> {
                    ErrorScreen(
                        error = uiState.error,
                        onRetry = { viewModel.loadTitleDetails() },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
                uiState.titleDetail != null -> {
                    DetailContent(
                        titleDetail = uiState.titleDetail,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    )
}

@Composable
private fun ErrorScreen(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val icon = if (error.contains("network", ignoreCase = true)) {
            Icons.Outlined.SignalWifiConnectedNoInternet4
        } else {
            Icons.Outlined.BrokenImage
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Oops! Something went wrong",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = error,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 24.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Button(
            onClick = onRetry,
            modifier = Modifier
                .height(44.dp)
                .width(120.dp)
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun DetailContent(
    titleDetail: TitleDetailResponse,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(titleDetail.poster)
                .crossfade(true)
                .build(),
            contentDescription = titleDetail.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().aspectRatio(0.75f),
            loading = { Spacer(modifier = Modifier.fillMaxSize().shimmerEffect()) },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎬", fontSize = 64.sp)
                }
            }
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(titleDetail.title, fontSize = 26.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (titleDetail.userRating != null) {
                    RatingChip(rating = titleDetail.userRating, label = "⭐ User")
                }
                if (titleDetail.criticScore != null) {
                    RatingChip(rating = titleDetail.criticScore.toDouble() / 10, label = "👥 Critic")
                }
                if (!titleDetail.usRating.isNullOrEmpty()) {
                    Badge(containerColor = MaterialTheme.colorScheme.tertiaryContainer) {
                        Text(titleDetail.usRating, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                    }
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (!titleDetail.releaseDate.isNullOrEmpty()) {
                    InfoColumn("📅 Release Date", titleDetail.releaseDate)
                }
                if (titleDetail.runtimeMinutes != null) {
                    InfoColumn("⏱️ Runtime", "${titleDetail.runtimeMinutes} min")
                }
            }

            if (!titleDetail.genreNames.isNullOrEmpty()) {
                HorizontalDivider()
                InfoSection("🎭 Genres") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        titleDetail.genreNames.take(4).forEach { genre ->
                            Badge(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                                Text(text = genre, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            
            if (!titleDetail.networkNames.isNullOrEmpty()) {
                HorizontalDivider()
                InfoSection("📡 Networks") {
                    Text(
                        titleDetail.networkNames.joinToString(", "),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (!titleDetail.plotOverview.isNullOrEmpty()) {
                HorizontalDivider()
                InfoSection("📖 Synopsis") {
                    Text(titleDetail.plotOverview, fontSize = 14.sp, lineHeight = 22.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 0.3.sp)
                }
            }
        }
    }
}

@Composable
private fun InfoColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InfoSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        content()
    }
}

@Composable
private fun RatingChip(rating: Double, label: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(String.format(Locale.US, "%.1f", rating), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
            Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onTertiaryContainer, fontWeight = FontWeight.Medium)
        }
    }
}
