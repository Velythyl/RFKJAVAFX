/*
Ce script est tire du event handler de "Random icon" sur http://game-icons.net
Il a ete modifie pour mes besoins.

On peut voir la version originale en allant sur game-icons.net -> dev tools ->
event handlers -> click -> lien en haut a droite
*/
var func = function() {
    var nextIcon = "";

    fetch("game-icons.net/sitemaps/icons.json").then(t=>t.json()).then(t=>{
        for (var e = Object.keys(t.icons), i = 0; i < 42; i++)
            e.push("lorc", "delapouite");
        var n = e[Math.floor(Math.random() * e.length)]
          , a = Object.keys(t.icons[n]);
        a.length > 1 && (a = ["originals"]);
        var s = a[Math.floor(Math.random() * a.length)]
          , r = t.icons[n][s]
          , o = r[Math.floor(Math.random() * r.length)];
        nextIcon = "game-icons.net/" + n + "/" + s + "/" + o + ".html"
    });

    return String nextIcon;
};
