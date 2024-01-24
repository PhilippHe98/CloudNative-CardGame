var selectedCards = [];

window.onload = function() {
    document.getElementById("exchange-button").addEventListener("click", function() {
        var cardBack = document.getElementById("card-back");
        var cardFront = document.getElementById("card-front");
        cardBack.style.display = "none";
        cardFront.style.display = "block";
        console.log("clicked");
    });
}

function setSelectButtonValue() {
    var selectButton = document.getElementById("exchange-button");
    console.log(selectedCards.length);
    if(selectedCards.length == 0) {
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
            console.log(selectedCardElement.dataset.cardCode);
        }
    }
    setSelectButtonValue();
}

function removeCards() {
   
    var countInput = document.getElementById("count");
    countInput.value = selectedCards.length;
    // Entfernt die ausgewählten Karten und zieht neue Karten
    for (var i = 0; i < selectedCards.length; i++) {
        var cardElement = document.getElementById(selectedCards[i]);
        
        cardElement.parentNode.removeChild(cardElement);
    }
}

function getSelectedCardCodes() {
    var cardCodes = "";
    for (var i = 0; i < selectedCards.length; i++) {
        var cardElement = document.getElementById(selectedCards[i]);
        if(i == selectedCards.length - 1) {
            cardCodes += cardElement.dataset.cardCode;
            break;
        }
        cardCodes += cardElement.dataset.cardCode + ",";
    }
    console.log(cardCodes);

    var cardCodesInput = document.getElementById('cardCodes');
    cardCodesInput.value = cardCodes;
}


