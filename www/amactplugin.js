var exec = require('cordova/exec');
module.exports = {
    version : function (win, fail){
        exec(win, fail, "amactplugin", "version", []);
    },
    openapp: function (message, win, fail){
        exec(win, fail, "amactplugin", "openapp", [message]);
    },
    openweb: function (message, win, fail){
        exec(win, fail, "amactplugin", "openweb", [message]);
    },
    openwebshow: function (message, win, fail){
        exec(win, fail, "amactplugin", "openwebshow", [message]);
    },
    openwebclose: function (win, fail){
        exec(win, fail, "amactplugin", "openwebclose", []);
    },
    getcookie: function (message,win, fail){
        exec(win, fail, "amactplugin", "cookieget", [message]);
    },
    setcookie: function (message,win, fail){
        exec(win, fail, "amactplugin", "cookieset", [message]);
    },
    clearcookie: function (win, fail){
        exec(win, fail, "amactplugin", "cookieclear", []);
    },
    encode: function (message,win, fail){
        exec(win, fail, "amactplugin", "encode", [message]);
    }
};
