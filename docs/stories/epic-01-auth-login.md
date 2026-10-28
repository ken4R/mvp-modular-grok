# Epic 01 — Connexion

Objectif : une utilisatrice se connecte avec le compte démo, sur Android et
iOS, à travers le même ViewModel KMP.

Le backend n'existe pas. Le repository est un faux, derrière `AuthRepository`.

## Stories

| Id | Titre | Statut |
| --- | --- | --- |
| 1.1 | Cas d'utilisation de connexion | Done |
| 1.2 | Faux repository | Done |
| 1.3 | ViewModel partagé | Done |
| 1.4 | Écran Android | Done |
| 1.5 | Wrapper iOS | Done (non compilé sur Linux) |
| 1.6 | Remplacer le faux repository par le BFF | Ready |

## Hors epic

Profil, paiements et notifications ont leur contrat et un stub. Leurs écrans
feront des epics séparés.
