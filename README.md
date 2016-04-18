# MovieApp


change api key in app/build.gradle

    buildTypes.each {
        it.buildConfigField 'String', 'MOVIE_API_KEY', "Put your api key here"
    }
