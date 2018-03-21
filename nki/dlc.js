/*
Ce script tente de downloader les 82 images les plus recentes de game-icons.net

Il fait ceci en allant sur game-icons.net/recent.html et en allant chercher tous
les elements de classe .icon

Puis, on download ces elements avec une for loop avec le path
nki/tempImg/counter.png
*/

var func = function() {
    //https://stackoverflow.com/questions/9236692/create-a-dom-document-from-an-url
    URL url = new URL("game-icons.net/recent.html");
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(url.openStream());

    var icons = doc.getElementsByClassName('icon');
    var finalIcons = new Array();

    for(var counter=0; counter<icons.length; counter++) {
        finalIcons.push("game-icons.net"+icons.item(counter));
    }

    return Java.to(finalIcons,"java.lang.String[]");
}
