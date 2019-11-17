# RippleDragDrop
Drag drop selection with ripple animation

## Include your project
add build.gradle
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
```
dependencies {
	implementation 'com.github.tak8997:RippleDragDrop:2.0.0'
}
```

## Usage

```xml
<com.tak8997.library.RippleDragDrop
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        app:itemCount="4"
        app:itemSize="50dp"
        app:itemGap="50dp"/>
```

```
with(rippleDragDrop) {
    setItemCount(4)
    setItemSize(50)
    setItemGap(50)
}
```
