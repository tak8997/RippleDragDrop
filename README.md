# RippleDragDrop
Drag drop selection with ripple animation


## RippleDragDrop View
![20201206_173758](https://user-images.githubusercontent.com/19990905/101276088-01fc7b00-37ee-11eb-894e-259f65a7d198.gif)


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
	implementation 'com.github.tak8997:RippleDragDrop:2.1.1'
}
```

## Usage


	<com.tak8997.library.RippleDragDrop
		android:id="@+id/rippleDragDrop"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:layout_centerInParent="true"
		app:itemCount="4"
		app:itemSize="50dp"
		app:itemGap="50dp" />


or


	with(rippleDragDrop) {
		Builder()
	   	  .setItemSize(50)
		  .setItemCount(4)
		  .setItemGap(50)
		  .build()	
	}

