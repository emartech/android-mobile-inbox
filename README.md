# android-mobile-inbox

With [Emarsys SDK](https://github.com/emartech/android-emarsys-sdk), you can [fetch inbox messages](https://github.com/emartech/android-emarsys-sdk/wiki#7-messageinbox) for a user.

This library is a plug and play inbox, that you can reuse and customise to your own branding.

Setup
-----
Make sure to already have the Android [Emarsys SDK](https://github.com/emartech/android-emarsys-sdk) added as a dependency in your Gradle file, and configured by calling `Emarsys.setup()` in your Application class, then add the `emarsys-mobile-inbox` module to your project.

The project provides different components to suit a variety of use-cases:

- `EmarsysInboxActivity` - An Activity that manages its own Fragments and lifecycles. Minimum setup required. Add it to your `AndroidManifest.xml` file and use an Intent to launch it.
- `EmarsysInboxFragment` - A Fragment that manages the navigation between `EmarsysInboxListFragment` and `EmarsysInboxDetailFragment`. Can be embedded in any Activity as the [primary navigation fragment](https://developer.android.com/guide/navigation/navigation-programmatic).
- `EmarsysInboxListFragment` - A Fragment that shows a list of messages in the user's inbox. Can be embedded in any Activity and will use `EmarsysInboxViewModel` to notify it about any events, such as a message being selected. The Activity can then choose to either handle the selected Message itself, or transition to a `EmarsysInboxDetailFragment`.
- `EmarsysInboxViewModel` - A ViewModel that can be used to easily extend your own Activities and Fragments with the Emarsys Messaging Inbox functionality.

Requirements
----
- The minimum Android version should be at least API level 24.
- Requires compileSdkVersion 31 or higher.
- Emarsys SDK is using AndroidX.

TODO
----
- Fully styleable Widgets.
- Distribution with AAR/Maven.

Submodules
-----
If you want to add this repository as a submodule of your project, so that you can pull new changes directly to your project and not have the inbox as a part of it:
- In terminal, run:
`git submodule add https://github.com/emartech/android-mobile-inbox <Name you want the submodule to have>` and `git submodule update --init --recursive`
- settings.gradle should look like this:

  ```
  include ':<submodule name>'
  include ':app'
  rootProject.name = "<root project name>"
  project(':<submodule name>').projectDir = new File('<submodule name>/emarsys-mobile-inbox')
  ```

- build.gradle (:app) should include:
`implementation project(':<submodule name>')`
- If, at any moment, the inbox folder appears empty, run `git submodule update --init --recursive` . If it doesn't appear at all, run `git submodule add --force https://github.com/emartech/android-mobile-inbox <submodule name>`

Contributing
----
Should you have any suggestions or bug reports, please raise an [Emarsys support request](https://help.emarsys.com/hc/en-us/articles/360012853058-Support-at-Emarsys-Raising-a-support-request).

Code of Conduct
----
Please see our [Code of Conduct](https://github.com/emartech/.github/blob/main/CODE_OF_CONDUCT.md) for detail.

Licensing
----
Please see our [LICENSE](https://github.com/emartech/android-mobile-inbox/blob/master/LICENSE) for copyright and license information.
