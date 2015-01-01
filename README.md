# transit

CTA Bus/Train Tracker

## Android

### Set up:

1. Download maven android sdk deployer [https://github.com/mosabua/maven-android-sdk-deployer]()
2. Follow instructions for deployer and install appropriate platform
3. Set `ANDROID_HOME` to root android sdk installation directory 
4. Install android-support-v4 using maven from [https://github.com/robolectric/robolectric]()
```
mvn install:install-file -DgroupId=com.android.support \
  -DartifactId=support-v4 \
  -Dversion=19.0.1 \
  -Dpackaging=jar \
  -Dfile="$ANDROID_HOME/extras/android/support/v4/android-support-v4.jar"
```
5. Add cta api key to `transit-android/res/values/keys.xml` with key of `ctaApiKey`
6. Build

## Play

### Set up:

1. Add cta api key to `transit-play/conf/api-key.conf` with key of `apiKey`

### Deploy
1. Build with `./activator dist` command
2. Copy the newly built zip file at `transit-play/target/universal/transit-play-[version].zip`
to destination
3. Extract and run `bin/transit-play`
