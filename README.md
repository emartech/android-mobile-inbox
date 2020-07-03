# android-mobile-inbox

Plug-and-play implementation of the Emarsys user-centric Messaging Inbox, based on the Android [Emarsys SDK](https://github.com/emartech/android-emarsys-sdk).

Setup
-----
Make sure to already have the Android [Emarsys SDK](https://github.com/emartech/android-emarsys-sdk) added as a dependency in your Gradle file, and configured by calling `Emarsys.setup()` in your Application class, then add the `emarsys-mobile-inbox` module to your project.

The project provides different components to suit a variety of use-cases:

- `EmarsysInboxActivity` - An Activity that manages its own Fragments and lifecycles. Minimum setup required. Add it to your `AndroidManifest.xml` file and use an Intent to launch it.
- `EmarsysInboxFragment` - A Fragment that manages the navigation between `EmarsysInboxListFragment` and `EmarsysInboxDetailFragment`. Can be embedded in any Activity as the [primary navigation fragment](https://developer.android.com/guide/navigation/navigation-programmatic).
- `EmarsysInboxListFragment` - A Fragment that shows a list of messages in the user's inbox. Can be embedded in any Activity and will use `EmarsysInboxViewModel` to notify it about any events, such as a message being selected. The Activity can then choose to either handle the selected Message itself, or transition to a `EmarsysInboxDetailFragment`.
- `EmarsysInboxViewModel` - A ViewModel that can be used to easily extend your own Activities and Fragments with the Emarsys Messaging Inbox functionality.

TODO
----
- Fully styleable Widgets.
- Distribution with AAR/Maven.