import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let toggleSplashScreen: (KotlinBoolean) -> Void = { isVisible in
            if isVisible.boolValue {

            } else {
                
            }
        }
        
        return MainViewControllerKt.MainViewController(toggleSplashScreen: toggleSplashScreen)
    }


    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(edges: .all)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}



