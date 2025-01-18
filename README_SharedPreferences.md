# PreferenceManager (in `Utilities.kt`)

## 0.5 Don't need to call `PreferenceManager.initialize` again (it is already called in `MainActivity.kt`)

## 1. Get preference (get)
```
// default_value for when there isn't any key in app's SharedPreferences
PreferenceManager.get<type>(key, default_value)
```

```kotlin
// Example:
PreferenceManager.getString()
PreferenceManager.getInt()
```

## 2. Put preference (save)
```
PreferenceManager.put<type>(key, value)
```

```kotlin
// Example:
PreferenceManager.putString()
PreferenceManager.putInt()
```
