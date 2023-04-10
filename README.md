# Devialet - MovieDB


# Table of Contents
1. [Application Architecture]
2. [Third-party libraries]
3. [Negative aspects of the project/ways for improvement]
4. [Thanks]




## Application Architecture


### Modules
- **core**: Contains the configuration of all the dependencies and their version used by the application, as well as a `Config.kt` file in which we find the definition of the API URL.
- **data**: Contains repositories, HTTP error handling, HTTP response and request templates, persistence with `Room`, and API definition with `Retrofit`.
- **features**: All the functionalities likely to be available in the application
    - **features/moviedetail**: The functionality to display the detail of a movie.
    - **features/movie**: The functionality to display the list of movies with the title, the average rating, and the cover.
- **domain**: Contains the different use cases and the definition of the repositories, the models.
- **utils**: Contains the graphic resources used in the different feature modules.

### Design pattern: MVVM (Model View View-Model)

- features/movie
    - `MovieDetailsScreen.kt` : The view, the graphical interface made with Jetpack Compose
    - `MovieDetailsViewModel.kt` : The bridge and the processing operations that take place between the database and the view
    - `State.kt` : The data model and the information presented to the view




# Third-party libraries

Libraries and their versions can be configured directly in the `build.gradle` of the Core module.

```kt
   ext.deps = [
                lottie                  : "com.airbnb.android:lottie-compose:${versions.lottie}",
                loggingInterceptor      : "com.squareup.okhttp3:logging-interceptor:${versions.loggingInterceptor}",
                runtimeLiveData         : "androidx.compose.runtime:runtime-livedata:${versions.runtimeLiveData}",
                koin                    : "io.insert-koin:koin-core:${versions.koin}",
                koinAndroid                   : "io.insert-koin:koin-android:${versions.koin}"
   ]
```

# Negative aspects of the project/ways for improvement
- Better manage modularization
- Setting up a loading animation with Lottie
- Create better error handling, & displaying a snackbar
- Implementation of unit tests, E2E and integration tests
- Remove unnecessary items and code
- Try to verify possible leaks with LeakCanary
- Comment as much as possible on the code as well as the function parameters

## Thanks

Here are the articles from which I was able to learn and inspire myself to carry out the technical test,
- Guide de modularization des applications Android (https://developer.android.com/topic/modularization?hl=fr)
- Modularization of Android Applications based on Clean Architecture (https://ahmad-efati.medium.com/modularization-of-android-applications-based-on-clean-architecture-18dc643e0562)
- Android Modularization using Clean Architecture and other Components (https://princessdharmy.medium.com/android-modularisation-using-clean-architecture-and-other-components-9ee44b061e9f)
- Jetpack Compose Modular Clean Architecture with Rorty App (https://developersancho.medium.com/jetpack-compose-modular-clean-architecture-with-rorty-app-58d801571ab9)
- Sharing components between two modules (https://stackoverflow.com/questions/34807554/sharing-components-between-modules)