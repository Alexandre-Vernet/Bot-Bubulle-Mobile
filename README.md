# Bot-Bubulle

Système de notification quotidienne à une heure programmée

Développé sous Android Studio 4.1.2

Par Alexandre Vernet


![icon](https://user-images.githubusercontent.com/72151831/109391919-cae3c480-7919-11eb-8338-929ae4af2fdd.png)



## Description
L'utilisateur programme une heure pour l'envoie de la notification. A partir de ce moment, une notification sera envoyé chaque jour à l'heure programmée. A tout moment, l'utilisateur peut changer l'heure et la notification sera envoyée à la nouvelle heure programmée.



## Ecran de chargement
A l'ouverture de l'application, un écran de chargement animé apparaît pendant 2 secondes.


![loading](https://user-images.githubusercontent.com/72151831/109391659-7a1f9c00-7918-11eb-8b50-4f0b203f8a43.jpg)



## Sécurité
Après l'écran de chargement, l'application demande à l'utilisateur de se connecter par biométrie si l'appareil possède un capteur d'empreinte digitale, sinon, il est possible de saisir manuellement un code secret.


![biometrics](https://user-images.githubusercontent.com/72151831/109391633-5d836400-7918-11eb-9c79-161440337639.jpg)



![activity_code](https://user-images.githubusercontent.com/72151831/109391635-5fe5be00-7918-11eb-97ec-f97c556a044b.jpg)



## Programmer l'heure de la notification
Après avoir passé l'écran de chargement et la sécurité, il est possible d'accéder à l'écran principal pour planifier une notification. A la modification


![activity_main](https://user-images.githubusercontent.com/72151831/109391715-c79c0900-7918-11eb-90c8-b7e57d6cd1c2.jpg)



## Notification reçu
Lorsque l'utilisateur reçoit la notification, il peut cliquer sur le bouton "envoyer msg" pour envoyer automatiquement un SMS pré-rempli.



## Gestion des données
Au changement de l'heure de la notification, l'heure est stockée dans la mémoire et permet au téléphone, lorsqu'il est redémarré, de redéfinir l'heure de la notification en lisant ces données. Ces données servent également à définir les aiguilles de l'horloge (activity_main) sur l'heure de la prochaine notification.
