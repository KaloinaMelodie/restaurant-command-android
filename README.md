# Projet PizzApp - Amélioration
## Lien youtube
https://youtube.com/shorts/KxOofID6KIE?feature=share

## Présentation
Ce projet est une reprise et amélioration de l'application **PizzApp**, intégrant de nouvelles fonctionnalités basées sur l'intelligence artificielle (IA).

---

## Équipe de Développement

### **ANDRIAMAMONJY Fitia Arivony :**
- Intégration de l'IA **Gemini** pour la gestion des commandes.
- Conception et optimisation du **prompt IA** pour transformer les commandes utilisateur en format JSON.
- Gestion de l'interaction avec l'IA et traitement des réponses JSON.

---

### **ANDRIANTAHIANA Vatosoa Finaritra :**
- Création et amélioration de l'**interface utilisateur interactive**.
- Conception du **design visuel** (choix des couleurs, disposition des éléments).
- Développement de l'interface de type **Chatbot**, permettant des interactions fluides.

---

### **RAVOAHANGILALAO Anjaniaina Kaloina Mélodie :**
- Développement des **fonctionnalités de gestion des commandes (CRUD)** 
- Traitement des **commandes vocales via SpeechRecognizer**.
- Envoi des commandes textuelles à l'IA pour interprétation.

# Objectif

Nous avons intégré des éléments d'intelligence artificielle dans notre projet **PizzApp**. L'objectif principal est d'améliorer la gestion des commandes en exploitant l'IA pour interpréter les commandes vocales des utilisateurs et les transformer en données structurées.

---

## Démarche d'Intégration du SpeechRecognizer

### Activation de la Commande Vocale
- L'utilisateur lance une commande vocale en appuyant sur l'icône du microphone.

---

### Initialisation du SpeechRecognizer
- Le composant **SpeechRecognizer** d'Android est initialisé avec une configuration spécifique :
  - **Modèle de reconnaissance :** Mode libre (`LANGUAGE_MODEL_FREE_FORM`).
  - **Langue de reconnaissance :** Français ("fr-FR").
  - **Prompt d'invite :** Un message "Parle maintenant..." s'affiche pour guider l'utilisateur.

---

### Détection de la Voix et Conversion en Texte en Temps Réel
- Le SpeechRecognizer commence à écouter la voix de l'utilisateur.
- Une fois la parole détectée :
  - Le son est converti en texte en temps réel.
  - Le texte est affiché instantanément dans l'interface pour permettre à l'utilisateur de visualiser ce qu'il a dit.
  - Si une erreur se produit (ex: aucun mot détecté, problème de microphone), un message d'erreur s'affiche.

---

### Capture et Transmission Automatique du Texte
- Le texte reconnu est automatiquement capturé.
- Il est ensuite transmis à l'IA **Gemini** pour être interprété comme une commande de pizza.

## Etapes Gemini AI
### Créer et récupérer la clé API Gemini
1.Aller dans Google AI Studio

2.Crée un projet (ou utilise un existant)

3.Clique sur API Key → Générer une clé API et copier la clé
### Configurer la clé API dans Android Studio
1.Sécuriser la clé (ne pas hardcoder)

a. dans gradle.properties 
```kotlin
GEMINI_API_KEY=API_KEY
```
b.Dans app/build.gradle.kts Sous android
```kotlin
buildFeatures {
    buildConfig = true
}
```

c.Puis dans defaultConfig 
```kotlin
buildConfigField("String", "GEMINI_API_KEY", "${project.findProperty("GEMINI_API_KEY")}")
```

2.Synchronise le Gradle

### Créer la requête HTTP vers l’API Gemini
1.Écrire le prompt complet (notre prompt pizza)

a. Définir le rôle de l'IA

```Tu es un assistant de commande de pizzas. ```

b. Définir le contexte et les données de base

Liste des pizzas disponibles et leurs prix

c. Spécifier ce que doit faire l'IA
Comprendre **une commande utilisateur**.

Extraire le nom de la pizza et d’autres informations (comme le fromage supplémentaire).

Fournir la réponse dans un format structuré (ici un JSON).


Fournir un exemple d'entrée et de sortie (Exemple de commande et format attendu)


Rendre l'exemple dynamique (Injection de l'input utilisateur)