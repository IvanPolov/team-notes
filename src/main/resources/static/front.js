function auto_grow(element) {
    element.style.height = element.scrollHeight + "px";
}

function auto_shrink(element) {
    element.style.height = '100px';
}


function showStuff(className, show) {
    console.log(className);
    let elements = document.getElementsByClassName(className);
    elements = Array.prototype.slice.call(elements);
    elements.forEach((el) => {
        if (show) el.style.display = 'block';
        else el.style.display = 'none';
    })
}


function getRandomColor() {
    console.log('getRandomColor function starts');
    let elements = document.getElementsByClassName('.header');
    elements = Array.prototype.slice.call(elements);
    console.log(elements)
    var letters = 'BCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * letters.length)];
    }
    elements.forEach((el) => {
        el.style.backgroundColor = color;
    })
}

window.onload = function () {
    showStuff('boards_block', true);
    // document.getElementById('addBoard').addEventListener('shown.bs.modal', function () {
    //     console.log('added event');
    //     document.getElementById('createBoardButton').focus();
    // });
    //
    // document.getElementById('addNote').addEventListener('shown.bs.modal', function () {
    //     document.getElementById('createNoteButton').focus();
    // });
    // document.getElementById('addUser').addEventListener('shown.bs.modal', function () {
    //     document.getElementById('inviteUserButton').focus();
    // });
}