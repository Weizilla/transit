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

## Tomcat

### Build and deploy

1. Build project using maven
2. Build the image using the `transit/transit-war/docker/docker-build` script, passing in the path to the built war file. Example: `transit/transit-war/docker/docker-build`
3. Set the env var CTA_API_KEY to the cta api key
4. Run the image using the `transit/transit-war/docker/docker-run` script 

## Play

### Set up:

1. Add cta api key to `transit-play/conf/api-key.conf` with key of `apiKey`

### Deploy
1. Build with `./activator dist` command
2. Copy the newly built zip file at `transit-play/target/universal/transit-play-[version].zip`
to destination
3. Extract and run `bin/transit-play`
