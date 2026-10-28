# PRD — MVP mobile modulaire

Statut : squelette. Le dépôt implémente la tranche verticale de connexion.
Le reste est structurel (contrats, stubs, flags).

## Problème

Une app iOS et une app Android doivent partager la même logique métier sans
partager l'UI, et sans que les features se couplent entre elles.

## Personas

- **Camille, utilisatrice** : veut se connecter et voir que sa session est
  reconnue. Plus tard : profil, achat, notifications.
- **Équipe mobile** : ajoute une feature sans ouvrir les implémentations des
  autres, et sans casser le contrat public.

## Objectifs du MVP

1. Connexion locale de démonstration, même comportement sur les deux OS.
2. Architecture feature-first figée : api / impl, core, umbrella `shared`.
3. Paiements et push présents comme contrats, désactivés par feature flag.
4. Aucun chargement dynamique de module.

## Périmètre

### Auth

- Saisie email / mot de passe.
- Validation (email, longueur du mot de passe).
- Compte démo `demo@mvp.dev` / `password123`.
- Session en mémoire (`userId`, `email`, `token`).
- Hors périmètre : vrai backend, refresh token, biométrie, inscription.

### Profil

- Lire un profil factice à partir d'un `userId`.
- Hors périmètre : édition, avatar, écran dédié. Le flag `ProfileEdit` est on
  mais aucun écran ne l'utilise encore.

### Paiements

- `PurchaseProductUseCase` refuse l'appel si le flag `Payments` est off
  (défaut).
- `PaymentGateway` a un stub Play Billing et un stub StoreKit 2.
- Hors périmètre : catalogue, restauration d'achats, reçus serveur.

### Notifications

- `RegisterPushUseCase` refuse l'appel si le flag `PushNotifications` est off
  (défaut).
- Stubs FCM et APNs.
- Hors périmètre : permission OS, topics, deep links.

## Contraintes

- Les apps parlent à un BFF, jamais aux services internes. Le client existe
  (`BffClient`) ; aucun serveur n'est livré ici.
- Erreurs métier : `Outcome<T, DomainError>`.
- ViewModels partagés, UI native.
- Modules liés à la compilation. Les flags ne chargent pas de code.

## Succès

- `./gradlew build` vert sur Linux (assemble Android + tests partagés).
- Le même `LoginViewModel` alimente Compose et SwiftUI.
- Un test de frontière échoue si une impl importe une autre impl.

## Non-objectifs

SQLDelight branché, navigation multi-écrans, CI macOS qui compile iOS,
signature App Store / Play, analytics.
