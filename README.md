transit
=======

Android CTA Bus/Train Tracker

Set up:
1.) Import
  a.) transit as android project
  b.) transit-test as android project
  c.) transit-unit as maven project
2.) Add dependencies
  a.) transit-test depends on transit and transit-unit
  b.) transit-unit depends on transit
3.) Add lib
  a.) transit - simple-xml
  b.) transit-test - robotium-solo
  c.) transit-unit
    i.) simple-xml
    ii.) android.jar
4.) Remove old apps from phone - will cause PARSE CERTIFICATE FAILURE if same app installed from another machine
5.) Run tests
  a.) transit-unit - easiest, java pojo
  b.) transit-test - requires usb device (or emulator)

wei
