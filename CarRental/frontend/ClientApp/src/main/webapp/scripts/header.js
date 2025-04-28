function displayVehicles(formName){
    document.getElementById(formName).submit();
}

function changeFilter(element, newClass) {
    element.classList.remove('filter-white', 'filter-blue');
    element.classList.add(newClass);
}

function logout(logoutUrl){
    window.location.replace(logoutUrl);
}

function toggleMenu() {
    const header = document.querySelector('.header');
    const menu = document.querySelector('.header__main-container');
    // so we can toggle it to not display, otherwise the controls and copyright watermark of the map gets above the navbar menu
    const leafletMap = document.getElementById("map");

    menu.classList.toggle('show');
    header.classList.toggle('show');
    leafletMap.classList.toggle('hidden');
}