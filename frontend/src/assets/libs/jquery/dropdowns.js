function openDropdown(){
    if(!$('.dropdown-menu.mydropdown').hasClass("show")){
        $('.dropdown-menu.mydropdown').addClass("show");
    } else{
        $('.dropdown-menu').removeClass("show");
    }
}

function openDropright(){
    if(!$('.dropdown-menu.mydropright').hasClass("show")){
        $('.dropdown-menu.mydropright').addClass("show");
    } else{
        $('.dropdown-menu.mydropright').removeClass("show");
    }
}