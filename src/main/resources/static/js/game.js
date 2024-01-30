var selectedCards = [];
var playerHand = [];
var opponentHand = [];

window.onload = function () {
    // Zeige Vorder oder Rückseite der Karten an
    document.getElementById("compare-button").addEventListener("click", function () {
        var cardBacks = document.getElementsByClassName("card-back");
        var cardFronts = document.getElementsByClassName("card-front");
        for (var i = 0; i < cardBacks.length; i++) {
            cardBacks[i].style.display = "none";
            cardFronts[i].style.display = "block";
        }
        console.log("clicked");
    });

    // Speichere die Karten des Spielers in einer Liste
    let cardElements = document.querySelectorAll('[data-playerCard-code]');
    playerHand = Array.from(cardElements).map(element => element.getAttribute('data-playerCard-code'));
    console.log(playerHand);
}

function setSelectButtonValue() {
    // Ändert den Text des Buttons, je nachdem ob Karten ausgewählt sind oder nicht
    var selectButton = document.getElementById("exchange-button");
    console.log(selectedCards.length);
    if (selectedCards.length == 0) {
        selectButton.textContent = "Karten halten";
    } else {
        selectButton.textContent = "Karten austauschen";
    }
}

function selectCard(cardId) {
    // Fügt die Karte zur Liste der ausgewählten Karten hinzu oder entfernt sie
    var index = selectedCards.indexOf(cardId);
    var cardElement = document.getElementById(cardId); // For css
    if (index > -1) {
        selectedCards.splice(index, 1);
        cardElement.classList.remove('selected');
    } else {
        selectedCards.push(cardId);
        cardElement.classList.add('selected'); // For css

        for (var i = 0; i < selectedCards.length; i++) {
            var selectedCardElement = document.getElementById(selectedCards[i]);
            console.log(selectedCardElement.getAttribute('data-playerCard-code'));
        }
    }
    setSelectButtonValue();
}

function getSelectedCardCodes() {
    var cardCodes = "";
    for (var i = 0; i < selectedCards.length; i++) {
        var cardElement = document.getElementById(selectedCards[i]);
        if (i == selectedCards.length - 1) {
            cardCodes += cardElement.getAttribute('data-playerCard-code');
            break;
        }
        cardCodes += cardElement.getAttribute('data-playerCard-code') + ",";
    }
    console.log(cardCodes);

    var cardCodesInput = document.getElementById('cardsToExchange');
    cardCodesInput.value = cardCodes;
}

function disableButton() {
    setTimeout(function () {
        document.getElementById("exchange-button").disabled = true;
    }, 10);
}

function compareCards() {

    let pairs = 0;
    let triples = 0;
    let fullHouse = 0;
    let quadruples = 0;
    let quintuples = 0;

    var cardCodes = [];
    var cardCodesCounterList = [];


    for (i = 0; i < playerHand.length; i++) {

        // Is the card code already in the cardCodes array?
        currentCardCode = playerHand[i]
        if (cardCodes.includes(currentCardCode)) {
            continue;
        } else {
            // If not, add it to the array and count how many times it occurs in the playerHand
            cardCodes.push(playerHand[i]);

            let currentCardValueCounter = 0;

            for (j = 0; j < playerHand.length; j++) {
                if (playerHand[i] == playerHand[j]) {
                    currentCardValueCounter++;
                }
            }

            // Add currentCardValueCounter to cardCodesCounter array
            cardCodesCounterList.push(currentCardValueCounter);
            
        }
    }
    console.log(cardCodesCounterList);

    // Count how many pairs, triples, quadruples and quintuples there are based on the cardCodesCounter array
    for(i = 0; i < cardCodesCounterList.length; i++) {
        switch(cardCodesCounterList[i]) {
            case 2:
                pairs++;
                break;
            case 3:
                triples++;
                break;
            case 4:
                quadruples++;
                break;
            case 5:
                quintuples++;
                break;
        }
    }

    if(pairs != 0 && triples == 0) console.log("Pairs: " + pairs);
    if(triples != 0 && pairs == 0) console.log("Triples: " + triples);
    if(quadruples != 0) console.log("Quadruples: " + quadruples);
    if(quintuples != 0) console.log("Quintuples: " + quintuples);
    if(pairs != 0 && triples != 0) console.log("Full House: ");
    if(pairs == 0 && triples == 0 && quadruples == 0 && quintuples == 0) console.log("Nothing");

}




