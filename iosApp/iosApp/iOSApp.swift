import SwiftUI
import ComposeApp
import FirebaseCore
import FirebaseMessaging


class AppDelegate: NSObject, UIApplicationDelegate {

  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      FirebaseApp.configure()
      AppInitializer.shared.onApplicationStart()

    return true
  }

   func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
        NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
        return UIBackgroundFetchResult.newData
   }

  func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
  }

    
}



@main
struct iOSApp: App {

    init() {
           KoinKt.doInitKoin(baseUrl: "http://134.119.179.100:1040/")
    }

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView().preferredColorScheme(.dark)
        }
    }
}

