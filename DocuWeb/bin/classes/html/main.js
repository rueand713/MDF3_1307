var javaClass = {
   setImage: null
   }; 

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
   
   // reset the required field borders to normal
    getId("document_name").style.border = "solid black thin";
    getId("document_location").style.border = "solid black thin";
    getId("document_description").style.border = "solid black thin";
   
    // reference the dom elements by their ids and get their associated values
    var note_title = getId("document_name").value;
    var note_people = getId("document_tags").value;
    var note_time = getId("document_time").value;
    var note_location = getId("document_location").value;
    var note_description= getId("document_description").value;
    
    // create a data array of the string values to pass back to the native code
    var data = [note_title, note_people, note_time, note_location, note_description];
    
    // validate the required strings
    if (note_title == null || note_title.replace(" ", "") == "")
    {
    	valid = false;
        
        // set the required field border to red
        getId("document_name").style.border = "solid red thin";
    }
    
    if (note_location == null || note_location.replace(" ", "") == "")
    {
    	valid = false;
        
        // set the required field border to red
        getId("document_location").style.border = "solid red thin";
    }
    
    if (note_description == null || note_description.replace(" ", "") == "")
    {
    	valid = false;
        
        // set the required field border to red
        getId("document_description").style.border = "solid red thin";
    }
    
    // verify that the data is valid
    if (valid == true)
    {
    	// pass the data back to the native app interface
        Native.clicked(data);   
    }
 }
 
 // method for setting image tag (removed)
 /*function setImage()
 {
    var img = getId("captured_image");
    
    var path = "file:" + Native.getImagePath();
    
    img.setAttribute("src", path);
 }*/
 
 // create references to the button objects
 var submitButton = getId("submit_button");
 var photoButton = getId("capture_image");
 var clearButton = getId("clear_button");
 
 // set the click listener for the submit button
 submitButton.addEventListener("click", function(){
    
    // grab the text field values and pass them into the native application
    getTextValues();
 });
 
 // set the click listener for the photo capture button
 photoButton.addEventListener("click", function(){
    
    // launch the camera on the device
    Native.getCameraImage();
    
    });
 
 // set the click listener for the clear button
 clearButton.addEventListener("click", function(){
   
   // clear all of the textfield text
    getId("document_name").value = "";
    getId("document_tags").value = "";
    getId("document_time").value = "";
    getId("document_location").value = "";
    getId("document_description").value = "";
   
   });
    
 // let the set image method be visible to the native java app from the javaClass object
    javaClass.setImage = setImage;
    
});