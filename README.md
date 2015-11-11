# react-native-android-location

Location acquisition through Network or GPS from android device without using Google Play Service.

### Installation

#### Install the npm package
```bash
npm i --save react-native-android-location
```

### Add it to your android project

* In `android/settings.gradle`

```gradle
...
include ':RNAlocation', ':app'
project(':RNAlocation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-location')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':react-native-android-location')
}
```

* register module (in MainActivity.java)

```java
import com.syarul.rnalocation.RNALocation;  // <--- import

public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {
  ......

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mReactRootView = new ReactRootView(this);

    mReactInstanceManager = ReactInstanceManager.builder()
      .setApplication(getApplication())
      .setBundleAssetName("index.android.bundle")
      .setJSMainModuleName("index.android")
      .addPackage(new MainReactPackage())
      .addPackage(new RNALocation()) // <-- Register package here
      .setUseDeveloperSupport(BuildConfig.DEBUG)
      .setInitialLifecycleState(LifecycleState.RESUMED)
      .build();

    mReactRootView.startReactApplication(mReactInstanceManager, "example", null);

    setContentView(mReactRootView);
  }

  ......

}
```

#### Add permissions to your Project

Add this to your AndroidManifest file;

``` xml
// file: android/app/src/main/AndroidManifest.xml

<uses-permission android:name="android.permission.ACCESS_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

## Example
```javascript
'use strict';

var React = require('react-native');

// For registering the Callback-Listener
var { DeviceEventEmitter } = require('react-native');

var RNALocation = require('react-native-android-location');

var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
} = React;

var RNALocation = React.createClass({
  // Create and Reset initial State Longitude (lng) and Latitude (lat)	
  getInitialState: function() { return { lng: 0.0, lat: 0.0}; },

  componentDidMount: function() {
  	  // Register Listener Callback !Important!
      DeviceEventEmitter.addListener('updateLocation', function(e: Event) {
          this.setState({lng: e.Longitude, lat: e.Latitude });
      }.bind(this));
      // Initialize RNALocation
      RNALocation.getLocation();
  },

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.location}>
          Lng: {this.state.lng} Lat: {this.state.lat}
        </Text>
      </View>
    );
  }
});

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  location: {
    textAlign: 'center'
  }
});

AppRegistry.registerComponent('RNALocation', () => RNALocation);
```
