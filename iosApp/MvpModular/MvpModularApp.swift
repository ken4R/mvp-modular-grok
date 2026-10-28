import SwiftUI

@main
struct MvpModularApp: App {
    init() {
        AppBootstrapKt.startApp()
    }

    var body: some Scene {
        WindowGroup {
            LoginView()
        }
    }
}
