console.log("This is script file");

const togleSidebar = () => {
  if ($(".sidebar").is(":visible")) {
    $(".sidebar").css("display", "none");
    $(".content").css("margin-left", "0%");
  } else {
    $(".sidebar").css("display", "block");
    $(".content").css("margin-left", "20%");
  }
};

function deleteContact(cId) {
  swal({
    title: "Do you want to delete this contact?",
    text: "Once deleted, you will not be able to recover this contact!",
    icon: "warning",
    buttons: true,
    dangerMode: true,
  }).then((willDelete) => {
    if (willDelete) {
      window.location = "/user/contact/delete/" + cId;
    } else {
      swal("Your contact is safe!");
    }
  });
}

const searchContacts = () => {
  let query = $("#search_Input").val();
  if (query == "") {
    $(".search-result").hide();
  } else {
    // sending request to server
    let url = `http://localhost:8080/search/${query}`;
    fetch(url)
      .then((response)=>{
        return response.json();
      })
        .then((data) => {
          //data
         let text=`<div class='list-group'>`;

          data.forEach(contact => {
            text +=`<a href='/user/${contact.cId}/contact' class='list-group-item list-group-action'>${contact.name} </a>`;
          });

         text += `</div>`;

         $(".search-result").html(text);
         $(".search-result").show();
        });

   
  }
};
