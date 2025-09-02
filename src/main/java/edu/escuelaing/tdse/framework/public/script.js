function getGreeting() {
    let nameVar = document.getElementById("textInputGet").value;
    let url = "/api/greeting?name=" + nameVar;

    fetch(url, { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            document.getElementById("getGreeting").innerHTML = data.response;
        })
}

function getHello() {
    let url = "/api/hello";

    fetch(url, { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            document.getElementById("getHello").innerHTML = data.response;
        })
        .catch(error => {
            console.error('Error in getHello:', error);
            document.getElementById("getHello").innerHTML = "Error fetching data. Check console.";
        });
}

function getColors() {
    let url = "/api/colors";

    fetch(url, { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            document.getElementById("getColors").innerHTML = JSON.stringify(data);
        })
}

function postColor() {
    let colorValue = document.getElementById("textInputPost").value;
    let url = "/api/colors";

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ color: colorValue })
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("postColor").innerHTML = JSON.stringify(data);
    })
}