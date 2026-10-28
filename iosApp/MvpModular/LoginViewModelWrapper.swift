import Combine
import Shared

/// Thin async/await adapter. SKIE turns the Kotlin `StateFlow` into an `AsyncSequence`.
@MainActor
final class LoginViewModelWrapper: ObservableObject {
    private let viewModel: LoginViewModel

    @Published private(set) var email: String = ""
    @Published private(set) var password: String = ""
    @Published private(set) var isSubmitting: Bool = false
    @Published private(set) var errorMessage: String?
    @Published private(set) var sessionLabel: String?

    init() {
        let resolved = SharedGraph.shared.loginViewModel()
        viewModel = resolved
        apply(resolved.state.value)
        Task { await observe() }
    }

    func onEmailChanged(_ value: String) {
        guard value != email else { return }
        viewModel.onEmailChanged(email: value)
    }

    func onPasswordChanged(_ value: String) {
        guard value != password else { return }
        viewModel.onPasswordChanged(password: value)
    }

    func submit() {
        viewModel.submit()
    }

    private func observe() async {
        for await next in viewModel.state {
            apply(next)
        }
    }

    private func apply(_ state: LoginUiState) {
        email = state.email
        password = state.password
        isSubmitting = state.isSubmitting
        errorMessage = state.errorMessage
        sessionLabel = state.session?.email
    }
}
