(function () {
    var paths = {
        "jquery": "libs/jquery/dist/jquery-3.1.0.min"
    };

    // RequireJS: initialize rjs runtime
    requirejs.config({
        baseUrl: "/assets/javascripts",
        paths: paths,
        waitSeconds: 0
    });
}).call(this);
