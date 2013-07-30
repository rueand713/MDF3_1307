window.addEventListener("DOMContentLoaded", function(){
    
    // method for finding and returning a DOM element by its id
 function getId(x)
 {
    return document.getElementById(x);
 }
 
 // method for interfacing between the web page and the native app
 function getTextValues()
 {
    var valid = true;
	
    // reference the dom elements by their ids and get their associated values
    var note_title = getId("document_name").value;
    var note_people = getId("document_tags").value;
    var note_time = getId("document_time").value;
    var note_location = getId("document_location").value;
    var note_description= getId("document_description").value;
    
    var data = [note_title, note_people, note_time, note_location, note_description];
    
    // validate the required strings
    if (note_title == null || note_title.replace(" ", "") == "")
    {
    	valid = false;
    }
    else if (note_location == null || note_location.replace(" ", "") == "")
    {
    	valid = false;
    }
    else if (note_description == null || note_description.replace(" ", "") == "")
    {
    	valid = false;
    }
    
    // verify that the data is valid
    if (valid == true)
    {
    	// pass the data back to the native app interface
        Native.clicked(data);   
    }
 }
 
 var acceptButton = getId("accept_button");
 
 acceptButton.addEventListener("click", function(){
    
    // grab the text field values and pass them into the native application
    getTextValues();
 })
    
})