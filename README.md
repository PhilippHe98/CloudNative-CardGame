Dieses Projekt ist eine Webanwendung im Rahmen der Cloudnative Software Engineering Vorlesung. Die Anwendung ist ein Kartenspiel das an Luigi Picture Poker angelehnt ist (https://clevertop.itch.io/poker - als Referenz). 
Für die Umsetzung des Projektes wurde die Decke auf Cards benutzt https://www.deckofcardsapi.com/ . 
Die Anwendung besteht aus drei Seiten einer Registrierungsseite, einer Homepage und einer Spielseite. Die Anwendung ist verfügbar unter https://console.cloud.google.com/run/detail/europe-west1/cloudnative-cardgame/metrics?hl=de&project=cloudnative-412114

Die Registrierungsseite besteht aus einem einfachen Formular. Die Seite ist unter dem Endpunkt /register zu erreichen.
![image](https://github.com/PhilippHe98/CloudNative-CardGame/assets/79371575/2b47200a-0ad1-4589-bcd7-dbf24bb7a598)

Nachdem man sich registriert hat, wird man aufgefordert sich einzuloggen. Hat man sich erfolgreich eingeloggt wird man zur Startseite weitergeleitet.
![image](https://github.com/PhilippHe98/CloudNative-CardGame/assets/79371575/d5a66c7d-9fe9-47a1-a2b0-19eb3d3e8463)

Auf der Startseite kann man ein neues Spiel erstellen. Alle vorhandenen Spiele werden in einer Liste ausgegeben. Das neu erstellte Spiel findet man am Ende der Liste wieder. 
Ein eingeloggter Nutzer ist nur dazu autorisiert, sein eigenes Spiel zu spielen oder zu löschen. Nur ein Admin darf neben seinem eigenen Spiel auch andere Spiele löschen. Mit dem Logout Button wird man dann wieder ausgeloggt.

Wird der "Play" Button gedrückt so wird man auf die Spielseite weitergeleitet. Auf der Seite erkennt man 10 Karten. Fünf der Karten sind aufgedeckt. Das sind die Karten des Spielers. Die anderen fünf nicht aufgedeckten Karten sind die Karten des Gegners.
![image](https://github.com/PhilippHe98/CloudNative-CardGame/assets/79371575/ce8bba58-6419-4397-ad9e-9e17dfb2990a)
Der Spieler kann nun beginnen, nicht erwünschte Karten auszuwählen. Diese Karten werden dann gelb umrandet. Ist mindestens eine Karte ausgewählt, so wird der Karten tauschen Button verfügbar und damit können die ausgewählten Karten getauscht werden.
![image](https://github.com/PhilippHe98/CloudNative-CardGame/assets/79371575/acad5542-cea4-4b31-9229-dd661bd599c7)
Nachdem die ausgewählten Karten getauscht wurden, verschwindet der "Karten Tauschen" Button und dem Spieler bleibt nur noch der Karten vergleichen Button. Der "Karten Vergleichen" Button deckt  die Karten des Gegners au, zeigt unten links im Bildschirm die Paarungen der Karten an und ein " Neue Runde" Button wird sichtbar. Beim Drücken des Buttons wird eine neue Runde gestartet und der Spieler kann wieder Karten tauschen usw.
![image](https://github.com/PhilippHe98/CloudNative-CardGame/assets/79371575/9c95fc86-99ba-4134-b0cd-79b4231eb2be)
Mit dem "Back to Homepage" Button oben rechts wird man dann wieder zur Startseite weitergeleitet.

Unter /swagger-ui/index.html#/ werden die Endpunkte dokumentiert.
![image](https://github.com/PhilippHe98/CloudNative-CardGame/assets/79371575/fe58ba3b-ae44-40a6-95ba-a570344c5b37)





