package co.gotitapp.giap;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.module.annotations.ReactModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ai.gotit.giap.GIAP;

@ReactModule(name = GotItAnalyticsModule.NAME)
public class GotItAnalyticsModule extends ReactContextBaseJavaModule {
  public static final String NAME = "GotItAnalytics";

  GIAP giap;

  public GotItAnalyticsModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  static JSONObject reactToJSON(ReadableMap readableMap) throws JSONException {
    JSONObject jsonObject = new JSONObject();
    ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      ReadableType valueType = readableMap.getType(key);
      switch (valueType) {
        case Null:
          jsonObject.put(key, JSONObject.NULL);
          break;
        case Boolean:
          jsonObject.put(key, readableMap.getBoolean(key));
          break;
        case Number:
          jsonObject.put(key, readableMap.getDouble(key));
          break;
        case String:
          jsonObject.put(key, readableMap.getString(key));
          break;
        case Map:
          jsonObject.put(key, reactToJSON(readableMap.getMap(key)));
          break;
        case Array:
          jsonObject.put(key, reactToJSON(readableMap.getArray(key)));
          break;
      }
    }

    return jsonObject;
  }

  static JSONArray reactToJSON(ReadableArray readableArray) throws JSONException {
    JSONArray jsonArray = new JSONArray();
    for (int i = 0; i < readableArray.size(); i++) {
      ReadableType valueType = readableArray.getType(i);
      switch (valueType) {
        case Null:
          jsonArray.put(JSONObject.NULL);
          break;
        case Boolean:
          jsonArray.put(readableArray.getBoolean(i));
          break;
        case Number:
          jsonArray.put(readableArray.getDouble(i));
          break;
        case String:
          jsonArray.put(readableArray.getString(i));
          break;
        case Map:
          jsonArray.put(reactToJSON(readableArray.getMap(i)));
          break;
        case Array:
          jsonArray.put(reactToJSON(readableArray.getArray(i)));
          break;
      }
    }
    return jsonArray;
  }


  @ReactMethod
  public void setup(String token, String url, Promise promise) {

    synchronized (this) {
      if (giap != null) {
        promise.resolve(null);
      } else if (GIAP.getInstance() != null) {
        giap = GIAP.getInstance();
        promise.resolve(null);
      } else {
        final ReactApplicationContext reactApplicationContext = this.getReactApplicationContext();
        if (reactApplicationContext == null) {
          promise.reject(new Throwable("no React application context"));
          return;
        }
        giap = GIAP.initialize(url, token, reactApplicationContext);
        promise.resolve(null);
      }

    }
  }

  @ReactMethod
  public void createAlias(String uid) {
    giap.alias(uid);
  }

  @ReactMethod
  public void identify(String uid) {
    giap.identify(uid);
  }

  @ReactMethod
  public void track(String event, Promise promise) {
    synchronized (giap) {
      giap.track(event);
    }
    promise.resolve(null);
  }

  @ReactMethod
  public void trackWithProperties(String event, ReadableMap properties, Promise promise) {
    JSONObject obj = null;
    try {
      obj = GotItAnalyticsModule.reactToJSON(properties);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    synchronized (giap) {
      giap.track(event, obj);
    }
    promise.resolve(null);
  }

  @ReactMethod
  public void set(ReadableMap properties, Promise promise) {
    JSONObject obj = null;
    try {
      obj = GotItAnalyticsModule.reactToJSON(properties);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    synchronized (giap) {
      giap.updateProfile(obj);

    }
    promise.resolve(null);
  }

  @ReactMethod
  public void reset() {
    giap.reset();
  }
}
