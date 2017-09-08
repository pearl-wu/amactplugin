var exec = require('cordova/exec');

exports.version = function(win, fail){
    exec(win, fail, "amactplugin", "version", []);
}

exports.openapp = function(message, win, fail){
    exec(win, fail, "amactplugin", "openapp", [message]);
}

exports.openweb = function(message, win, fail){
    exec(win, fail, "amactplugin", "openweb", [message]);
}

exports.openwebshow = function(message, win, fail){
    exec(win, fail, "amactplugin", "openwebshow", [message]);
}

exports.openwebclose = function(win, fail){
    exec(win, fail, "amactplugin", "openwebclose", []);
}
