transit
=======

Android CTA Bus/Train Tracker

Set up:

1. Download maven android sdk deployer https://github.com/mosabua/maven-android-sdk-deployer
2. Follow instructions for deployer and install appropiate platform
3. Set ANDROID_HOME to root android sdk installation directory for intellij
4. Install android-support-v4 using maven from (https://github.com/robolectric/robolectric)
  mvn install:install-file -DgroupId=com.android.support \
  -DartifactId=support-v4 \
  -Dversion=19.0.1 \
  -Dpackaging=jar \
  -Dfile="$ANDROID_HOME/extras/android/support/v4/android-support-v4.jar"
5. Add cta api key to transit/res/values/keys.xml with key of "ctaApiKey"
6. Build

Wei
