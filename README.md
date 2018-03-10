# ArchEx

ArchEx is a set of Kotlin extensions for the [Android Architechture Components](https://developer.android.com/topic/libraries/architecture/index.html). 

There were a few things that I didn't like about using the Architecture Components in Kotlin, such as the nullability of LiveData values and Observers. This library provides very basic alternatives to the LiveData class, which can be used in a null-safe manner. These classes are Live, and MutableLive, which act in a similar way to LiveData, and are implemented using a LiveData delegate. 

You will also find the class LiveViewModel, which is an extension of the ViewModel class, which provides a `state` object, and can be observed as if it was a LiveData. I have found this useful as a base class for building ViewModels, as I generally try to represent View state through a single state object. 



