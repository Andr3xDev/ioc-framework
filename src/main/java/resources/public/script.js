function getGreeting() {
    let nameVar = document.getElementById("textInputGet").value;
    let url = "/api/greeting?name=" + nameVar;

    fetch(url, { method: 'GET' })
        .then(response => response.json())  
        .then(data => {
            document.getElementById("getResult").innerHTML = data.response;
        });
}

function getHellow() {
    let url = "/api/hello";

    fetch(url, { method: 'GET' })
        .then(response => response.json())  
        .then(data => {
            document.getElementById("getResult").innerHTML = data.response;
        });
}

function getColors() {
    let url = "/api/colors";

    fetch(url, { method: 'GET' })
        .then(response => response.json())  
        .then(data => {
            document.getElementById("getResult").innerHTML = data.response;
        });
}

function getGreeting() {
    let nameVar = document.getElementById("textInputPost").value;
    let url = "/api/colors?color=" + nameVar;

    fetch(url, { method: 'POST' })
        .then(response => response.json())  
        .then(data => {
            document.getElementById("getResult").innerHTML = data.response;
        });
}