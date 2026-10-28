import SwiftUI

struct LoginView: View {
    @StateObject private var model = LoginViewModelWrapper()

    var body: some View {
        NavigationStack {
            Form {
                if let sessionLabel = model.sessionLabel {
                    Section("Session") {
                        Text("Connecté en tant que \(sessionLabel)")
                    }
                } else {
                    Section("Connexion") {
                        TextField("Email", text: emailBinding)
                            .textInputAutocapitalization(.never)
                            .keyboardType(.emailAddress)
                        SecureField("Mot de passe", text: passwordBinding)
                        if let errorMessage = model.errorMessage {
                            Text(errorMessage)
                                .foregroundStyle(.red)
                        }
                        Button(model.isSubmitting ? "Connexion…" : "Se connecter") {
                            model.submit()
                        }
                        .disabled(model.isSubmitting)
                    }
                    Section {
                        Text("Compte démo : demo@mvp.dev / password123")
                            .font(.footnote)
                            .foregroundStyle(.secondary)
                    }
                }
            }
            .navigationTitle("MVP Modular")
        }
    }

    private var emailBinding: Binding<String> {
        Binding(
            get: { model.email },
            set: { model.onEmailChanged($0) }
        )
    }

    private var passwordBinding: Binding<String> {
        Binding(
            get: { model.password },
            set: { model.onPasswordChanged($0) }
        )
    }
}
