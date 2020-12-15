include(":app", ":YoutubePlayer")
rootProject.name = "AndroidGenericAdapter"

buildCache {
    local {
        // Set local build cache directory.
        directory = "${settingsDir}/build-caches"
    }
}
