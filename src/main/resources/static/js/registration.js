function validatePassword() {
    var password = document.getElementById('password').value;
    var confirmPassword = document.getElementById('confirmPassword').value;
    if (password != confirmPassword) {
        alert('Passwörter stimmen nicht überein!');
        return false;
    }
    return true;
}