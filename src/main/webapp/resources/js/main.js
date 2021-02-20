let rGlobal = 1;

let checkboxes_y = $('.chy').toArray();
let checkboxes_r = $('.chr').toArray();
let rs = [1, 1.5, 2, 2.5, 3];

selectGlR();

function selectGlR(){
    for(let i = 0; i<5; i++){
        console.log(rGlobal)
        if(checkboxes_r[i].checked) rGlobal = rs[i];
    }
}

// selects last selected checkbox | unselects it
function fy(index) {
    for (let checkbox of checkboxes_y) {
        if (checkbox !== checkboxes_y[index-1]) {
            $(checkbox)[0].checked = false;
            checkbox.checked = false;

        }
    }
}

function fr(index) {
    for (let checkbox of checkboxes_r) {
        if (checkbox !== checkboxes_r[index-1]) {
            checkbox.checked = false;
        }
    }
}

window.alert = function () {

};

function updateClock() {
    let now = new Date();
    let time = now.getHours() + ':' + now.getMinutes() + ':' + now.getSeconds();
    // a cleaner way than string concatenation
    let date = [now.getDate(),
        now.getMonth() + 1,
        now.getFullYear()].join('.');
    document.getElementById('time').innerHTML = [time, date].join(' | ');
    setTimeout(updateClock, 8000);
}



function setY(newY) {
    document.getElementById('mainForm:dataY').setAttribute('value', newY);
}

function setR(newR) {
    rGlobal = newR;
    console.log(newR)
}

function validate(x, y) {
    let xCheck = x >= -3 && x <= 3;
    let yCheck = y >= -2 && y <= 0.5;
    return xCheck && yCheck;
}

const clickAnswer = function (event) {

    var box = document.getElementById("image-coordinates").getBoundingClientRect();


    var xCord = ((event.clientX - box.x) / box.width) * 300;
    var yCord = ((event.clientY - box.y) / box.height) * 300;

    xCord = xCord - 150;
    yCord = yCord - 150;

    var r = parseFloat(rGlobal);

    var x = (xCord / 120) * r;
    var y = -(yCord / 120) * r;

    console.log("Click happened X: " + x + " Y: " + y);

    if(validate(x, y)) {
        x = parseFloat(x.toString().slice(0, 5));
        y = parseFloat(y.toString().slice(0, 5));
        r = parseFloat(r.toString().slice(0, 5));

        document.getElementById('textForm:text_input_X').setAttribute('value', x);
        document.getElementById('hiddenForm:_dataY').setAttribute('value', y);
        document.getElementById('hiddenForm:_dataX').setAttribute('value', x);

        document.getElementById('hiddenForm:_sendButton').click();
        $("#errorMessage").css("display", "none");
    }else {
        $("#errorMessage").css("display", "block");
    }

};

function onSubmitForm(){
    $("#errorMessage").css("display", "none");
}

function setX() {
    let x = document.getElementById('textForm:text_input_X').value;
    document.getElementById('mainForm:dataX').setAttribute('value', x);
}

function setY(value){
    document.getElementById('mainForm:dataY').setAttribute('value', value);
}

document.addEventListener('DOMContentLoaded', function () {
    updateClock();
    document.getElementById('pictureBox').addEventListener('click', clickAnswer);
    //document.getElementById('image-coordinates').addEventListener('click', clickAnswer);

});

