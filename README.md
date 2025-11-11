# CineList - Android Movie & TV Show App

A modern Android application built with Jetpack Compose that displays movies and TV shows using the Watchmode API with an MVVM architecture and Koin dependency injection.

## Features Implemented

### 1. **Home Screen**
- Fetch and display a list of movies & TV shows using the Watchmode API
- Toggle button at the top to switch between Movies or TV Shows
- Shimmer loading effect while data is loading
- Grid layout displaying titles with posters, year, and type information
- Error handling with retry functionality

### 2. **Details Screen**
- Clicking on an item navigates to a details screen
- Shows title, description, release date, and poster image
- Displays ratings (user rating & critic score)
- Shows genres, networks (for TV shows), and runtime information
- Shimmer loading effect while data is loading
- Back navigation button

### 3. **API Integration**
- Two simultaneous API calls using `Single.zip` (RxJava2)
  - One for Movies
  - One for TV Shows
- Uses Retrofit for networking with Gson for JSON serialization
- Proper error handling with meaningful error messages

### 4. **Architecture**
- **MVVM Pattern**: Separates concerns into Model, View, and ViewModel
  - `HomeViewModel`: Manages home screen state and API calls
  - `DetailViewModel`: Manages detail screen state
- **Dependency Injection**: Uses Koin for DI
  - Configured in `appModule.kt`
  - ViewModels properly injected into Compose screens
- **Repository Pattern**: Abstracted API calls through `MediaRepository`

### 5. **UI/UX**
- Material Design 3 components
- Shimmer animation effects for skeleton loading
- Smooth transitions and navigation
- Error screens with retry buttons
- Responsive grid layout

### 6. **Error Handling**
- Graceful error display with meaningful messages
- Retry functionality for failed requests
- Error classification (network, timeout, authorization, rate limiting)

## Project Structure

```
📁 com.example.cinelist (your root package)
│
├── 📁 data
│   │
│   ├── 📁 api
│   │   └── ApiService.kt        
│   │
│   ├── 📁 model
│   │   ├── MovieListResponse.kt  
│   │   ├── TvShowListResponse.kt 
│   │   ├── TitleDetailResponse.kt
│   │   └── HomeMedia.kt          
│   │
│   └── 📁 repository
│       └── MediaRepository.kt    
│
├── 📁 di
│   └── AppModule.kt             
│
├── 📁 ui
│   │
│   ├── 📁 components
│   │   ├── ShimmerListItem.kt    
│   │   ├── MovieListItem.kt      
│   │   └── ErrorSnackbar.kt      
│   │
│   ├── 📁 details
│   │   ├── DetailsScreen.kt      
│   │   └── DetailsViewModel.kt   
│   │
│   ├── 📁 home
│   │   ├── HomeScreen.kt         
│   │   └── HomeViewModel.kt      
│   │
│   ├── 📁 navigation
│   │   └── AppNavigation.kt      
│   │
│   └── 📁 theme
│       ├── Color.kt              
│       ├── Theme.kt             
│       └── Type.kt               
│
├── 📁 util
│   ├── Constants.kt              
│   └── Resource.kt               
│
└──  MyApp.kt            
```

## Dependencies

### Core Android
- `androidx.core:core-ktx`
- `androidx.lifecycle:lifecycle-runtime-ktx`
- `androidx.activity:activity-compose`
- `androidx.lifecycle:lifecycle-viewmodel-compose`

### Jetpack Compose
- `androidx.compose.*` (UI, Material3, Navigation)

### Networking
- `com.squareup.retrofit2:retrofit`
- `com.squareup.retrofit2:converter-gson`
- `com.google.code.gson:gson`
- `com.squareup.okhttp3:okhttp`
- `com.squareup.retrofit2:adapter-rxjava2`

### Reactive Programming
- `io.reactivex.rxjava2:rxjava2`
- `io.reactivex.rxjava2:rxandroid`
- `io.reactivex.rxjava2:rxkotlin`

### Dependency Injection
- `io.insert-koin:koin-core`
- `io.insert-koin:koin-android`
- `io.insert-koin:koin-androidx-compose`

### Image Loading
- `io.coil-kt:coil-compose`

## Setup Instructions

### 1. Add API Key

The Watchmode API key and base URL should be stored in `local.properties` to avoid committing secrets. Add the following lines to the project's `local.properties`:

```properties
WATCHMODE_API_KEY=YOUR_WATCHMODE_API_KEY
WATCHMODE_API_BASE_URL=https://api.watchmode.com
```
add in `build.gradle.kts`:
```kotlin
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android{
    defaultConfig{
        // Expose watchmode.apiKey and watchmode.baseUrl as build config fields
        buildConfigField("String", "WATCHMODE_API_KEY", localProperties.getProperty("watchmode.apiKey", ""))
        buildConfigField("String", "WATCHMODE_BASE_URL", localProperties.getProperty("watchmode.baseUrl", ""))
    }
}
```
To get an API key, visit: https://www.watchmode.com/api

### 2. Build the Project

```bash
./gradlew build
```

### 3. Run the App

```bash
./gradlew installDebug
```

Or use Android Studio to run the app.

## API Integration Details

### Watchmode API Endpoints Used

1. **List Titles (Movies)**
   ```
   GET /v1/list-titles?apiKey={key}&types=movie&sort_by=popularity_desc
   ```

2. **List Titles (TV Shows)**
   ```
   GET /v1/list-titles?apiKey={key}&types=tv_series&sort_by=popularity_desc
   ```

3. **Title Details**
   ```
   GET /v1/title/{id}/details?apiKey={key}
   ```

### API Response Handling

The app uses RxJava2's `Single.zip` operator to combine two API calls:

```kotlin
Single.zip(
    apiService.getMovies(apiKey),      // Call 1
    apiService.getTvShows(apiKey),     // Call 2
    { movieResponse, tvShowResponse ->
        HomeMedia(movieResponse, tvShowResponse)
    }
)
```

## UI Components

### Shimmer Effect
Custom shimmer animation component used for skeleton loading:
- Located in `ui/components/ShimmerEffect.kt`
- Provides smooth gradient animation
- Used in both Home and Detail screens

### Navigation
Implemented using Jetpack Navigation Compose:
- Start destination: Home screen
- Detail screen with title ID parameter
- Back navigation support

## State Management

### HomeViewModel
```kotlin
data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: List<Title> = emptyList(),
    val tvShows: List<Title> = emptyList(),
    val error: String? = null,
    val selectedMediaType: MediaType = MediaType.MOVIES
)
```

### DetailViewModel
```kotlin
data class DetailUiState(
    val isLoading: Boolean = false,
    val titleDetail: TitleDetailResponse? = null,
    val error: String? = null
)
```

## Error Handling

The app handles various error scenarios:
- Network connectivity issues
- API timeouts
- Authorization errors (invalid API key)
- Rate limiting
- Server errors

Errors are displayed in a user-friendly manner with a retry button on the home screen.




## License

This project is for educational purposes.
